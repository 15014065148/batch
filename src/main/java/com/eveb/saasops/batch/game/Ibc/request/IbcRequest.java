package com.eveb.saasops.batch.game.Ibc.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.Ibc.domain.IbcBetLog;
import com.eveb.saasops.batch.game.Ibc.domain.IbcRequestParameter;
import com.eveb.saasops.batch.sys.util.EvebMD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IbcRequest {

    @Autowired
    private OkHttpProxyUtils httpProxyUtils;
    private static String baseurl="http://api.prod.ib.gsoft88.net/api/";
    /**{"error_code":23000,"message":"Record not exists"}**/
    private static String errcode="23000";

    public List<IbcBetLog> request(IbcRequestParameter parameter) throws Exception {
        String basemd5=String.format("%s/api/"+"GetSportBettingDetail?OpCode=%s&StartTime=%s&EndTime=%s",parameter.getApi().getMd5Key(),parameter.getApi().getAgyAcc(),parameter.getStartTime(),parameter.getEndTime());
        String md5= EvebMD5.getMD5(basemd5).toUpperCase();
        String url=String.format(parameter.getApi().getPcUrl()+"api/GetSportBettingDetail?OpCode=%s&StartTime=%s&EndTime=%s&securitytoken=%s",parameter.getApi().getAgyAcc(),parameter.getStartTime(),parameter.getEndTime(),md5);
        String rs= httpProxyUtils.get(httpProxyUtils.proxyClient,url);
        log.info("IBC请求url :"+url);
        log.info("IBC返回数据 :"+rs);
        Map map=(Map)JSON.parse(rs);
        if(map.get("error_code").toString().equals(errcode))
        {
            return new ArrayList<>();
        }
        return JSON.parseArray(map.get("Data").toString(),IbcBetLog.class);
    }

}
