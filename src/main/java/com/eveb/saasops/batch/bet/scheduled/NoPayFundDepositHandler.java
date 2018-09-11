package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.entity.FundDeposit;
import com.eveb.saasops.batch.bet.entity.SetBacicOnlinepay;
import com.eveb.saasops.batch.bet.service.FundDepositService;
import com.eveb.saasops.batch.bet.service.ThirdPayService;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.feign.feign.sercice.saasopsV2Service.SaasopsV2Service;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.SiteCodeAesUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.isNull;

/**
 * 对每个库中超过从现在时间算起设置小时为支付的订单作失败处理
 *
 * Created by William on 2018/3/5.
 */

@Slf4j
@Service
@JobHander(value = "NoPayFundDepositHandler")
public class NoPayFundDepositHandler  extends JobHandler {

    @Resource
    private SaasopsV2Service saasopsV2;

    @Autowired
    SysService sysService;

    @Autowired
    FundDepositService fundDepositService;

    @Autowired
    ThirdPayService thirdPayService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("开始执行未支付订单清理---");
        Object[] objects = prepareArguments(strings);
        List<String> apis = isNull(objects[indexApi]) ? null : (List<String>) objects[indexApi];
        List<String> siteCodeList = new ArrayList<>();
        if (isNull(apis)) {
            siteCodeList = sysService.getSiteCodeList();
        }else {
            for (String api : apis) {
                siteCodeList.addAll(sysService.getSiteCodeList(api));
            }
        }

        Map<String,List<Integer>> paySuccessMap = new HashMap<>();
        for(String siteCode : siteCodeList){
            String schemaCode =new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
            //获取待支付订单,超过2天未支付的
            List<FundDeposit> fundDeposits=fundDepositService.getDepositeNoPay(schemaCode);
            thirdPayService.clearpaySuccessMap();
            int i = 1;
            for (FundDeposit fundDeposit: fundDeposits) {
                SetBacicOnlinepay onlinepay =fundDepositService.queryOnlinePayOne(schemaCode,fundDeposit.getOnlinePayId());
                Long orderNo = fundDeposit.getOrderNo();
                try {
                    CompletableFuture<String> payResult=thirdPayService.getPayResult(orderNo,fundDeposit.getId(),siteCode,onlinepay);
                    CompletableFuture.allOf(payResult).join();
                    if(i == fundDeposits.size()) {
                        saasopsV2.paySuccess(SiteCodeAesUtil.encrypt(siteCode), thirdPayService.getPaySuccessMap().get(siteCode));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        log.info("数据"+thirdPayService.getTotalCount());
        log.info(paySuccessMap.toString());
        return ReturnT.SUCCESS;
    }

}
