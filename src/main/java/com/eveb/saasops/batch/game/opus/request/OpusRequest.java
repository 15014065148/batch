package com.eveb.saasops.batch.game.opus.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.opus.domain.OpusLiveBetLog;
import com.eveb.saasops.batch.game.opus.domain.OpusRequestParameter;
import com.eveb.saasops.batch.game.opus.domain.OpusSportBetLog;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import com.eveb.saasops.batch.sys.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OpusRequest {

    @Autowired
    private OkHttpProxyUtils httpProxyUtils;

    public List<OpusSportBetLog> requestSport(OpusRequestParameter parameter) throws Exception {
        Map map=(Map)JSON.parse(parameter.getApi().getSecureCode());
        String paras=String.format("TransactionMemberDetail.API?operator_id=%s&date_start=%s&date_end=%s",map.get("Operator_ID").toString(),parameter.getStartTime(),parameter.getEndTime());
        String url=parameter.getApi().getPcUrl()+paras;
        String rs= httpProxyUtils.get(httpProxyUtils.client,url);
        log.info("OPUS请求url :"+url);
        log.info("OPUS返回数据 :"+rs);
        Map rsmap= XmlUtil.xml2JSON(rs.getBytes());
        if(GameCodeConstants.CONSTANT__OPUS_TRANSACTION_NOT_FOUND.equals(((List)((Map) rsmap.get("transactionmember_detail")).get("status_code")).get(0).toString()))
        {
            return new ArrayList<>();
        }
        rs=((Map)((List)((Map) rsmap.get("transactionmember_detail")).get("bets")).get(0)).get("row").toString();
        rs=rs.replace("[", "").replace("]", "");
        System.out.println(rs);

        return JSON.parseArray("[" + rs + "]",OpusSportBetLog.class);
    }

    public List<OpusLiveBetLog> requestLive(OpusRequestParameter parameter) throws Exception {
        Map map=(Map)JSON.parse(parameter.getApi().getSecureCode());
        String paras=String.format("TransactionDetail.API?operator_id=%s&secret_key=%s&site_code=%s&product_code=%s&start_time=%s&end_time=%s",map.get("Operator_ID").toString(),map.get("Secret_Key").toString(),map.get("Site_Code").toString(),map.get("Product_Code").toString(),parameter.getStartTime(),parameter.getEndTime());
        String url=parameter.getApi().getPcUrl()+paras;
        String rs=httpProxyUtils.get(httpProxyUtils.client,url);
        Map rsmap= XmlUtil.xml2JSON(rs.getBytes());
        log.info("OPUS请求url :"+url);
        log.info("OPUS返回数据 :"+rs);
        Object rsobj=((Map) rsmap.get("Transaction_Detail")).get("bets");
        if(rsobj==null)
        {
            return new ArrayList<>();
        }
        rs=((Map)((List)rsobj).get(0)).get("row").toString();
        rs=rs.replace("[", "").replace("]", "");
        return JSON.parseArray("[" + rs + "]",OpusLiveBetLog.class);
    }
}

