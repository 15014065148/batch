package com.eveb.saasops.batch.bet.service.payImpl;

import com.eveb.saasops.batch.bet.entity.PzpayContent;
import com.eveb.saasops.batch.bet.entity.SetBacicOnlinepay;
import com.eveb.saasops.batch.bet.service.FundDepositService;
import com.eveb.saasops.batch.bet.service.ThirdPayService;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.ASCIIUtils;
import com.eveb.saasops.batch.sys.util.JsonUtil;
import com.eveb.saasops.batch.sys.util.MD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 盘子支付实现类
 * Created by William on 2018/3/6.
 */
@Slf4j
@Service
public class PzPayServiceImpl extends ThirdPayService {

    private String pzPayQueryUrl ="http://skru28291kdi.mailcsharp.com/v1/query.do";

    @Autowired
    private FundDepositService fundDepositService;

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private SysService sysService;

    @Transactional
    @Async(value = "ThirdPayAsyncExecutor")
    @Override
    public CompletableFuture<String> getPayResult(Long orderNo, Integer id, String siteCode, SetBacicOnlinepay onlinepay) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_no", "");
        params.put("partner_id", onlinepay.getMerNo()); //对应收款帐号
        String urlParams = ASCIIUtils.formatUrlMap(params, false, false);
        String sign = MD5.getMD5((urlParams + "&key="+onlinepay.getPassword().split("\\|\\|")[0]));
        String payUrl = pzPayQueryUrl + "?" + urlParams + "&sign=" + sign;
        String payRS = okHttpProxyUtils.get(okHttpProxyUtils.payClient, payUrl);
        log.info("请求request参数{}",payUrl);
        log.info("siteCode{} 请求返回参数{}",siteCode,payRS);
        Map<String, Object> rs = jsonUtil.toMap(payRS);
        if (rs.get("success").toString().equals("true")) {
            PzpayContent pzpayContent = jsonUtil.fromJson(jsonUtil.toJson(rs.get("content")), PzpayContent.class);
            if (pzpayContent.getTrade_status().equals("SUCCESS")) {
                //支付成功
                addPaySuccessId(siteCode,id);
            }else {
                setPayFaild(siteCode,id);
            }
        }else {
            setPayFaild(siteCode,id);
        }
        plusCount();
        return CompletableFuture.completedFuture(payRS);
    }

    private void setPayFaild(String siteCode,Integer id){
        String schemaCode =new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
        fundDepositService.depositeNoPay(schemaCode,id);
    }



}
