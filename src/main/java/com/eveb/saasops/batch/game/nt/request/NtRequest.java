package com.eveb.saasops.batch.game.nt.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.nt.domain.NTBetLog;
import com.eveb.saasops.batch.game.nt.domain.NtRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NtRequest {

    @Autowired
    private OkHttpProxyUtils httpProxyUtils;
    private String url="ps/ssw/datafeed/transactionsTimeRange?brandId=%s&brandPassword=%s&startTime=%s&endTime=%s&utcTimeZoneOffset=%s";

    /***
     * 获取所有的交易记录，包含注单
     * @return
     */
    public  List<NTBetLog> request(NtRequestParameter parameter)throws Exception {
        Map apimap = (Map) JSON.parse(parameter.getApi().getSecureCode());
        String rurl = parameter.getApi().getPcUrl() + String.format(url, apimap.get("brandId"), apimap.get("brandPassword"), parameter.getStartTime(), parameter.getEndTime(), parameter.getTimezone());
        String rs=httpProxyUtils.postJson(httpProxyUtils.proxyClient,rurl,null);
        log.info("NT请求url :" + rurl);
        log.info("NT返回数据 :" + rs);
        Map rsmap = (Map) JSON.parse(rs);
        List<NTBetLog> list = JSON.parseArray(rsmap.get("transactions").toString(), NTBetLog.class);
        return list;
    }
}
