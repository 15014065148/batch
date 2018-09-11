package com.eveb.saasops.batch.game.png.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.png.domain.PngBetLong;
import com.eveb.saasops.batch.game.png.domain.PngRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PngRequest {

    @Autowired
    private OkHttpProxyUtils httpProxyUtils;
    /***
     * 获取注单
     * @return
     */
    public List<PngBetLong> request(PngRequestParameter parameter)throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map apimap = (Map) JSON.parse(parameter.getApi().getSecureCode());
        String rurl =String.format(apimap.get("url").toString(), apimap.get("guid"), parameter.getStartTime(), parameter.getEndTime());
        String rs = httpProxyUtils.postJson(httpProxyUtils.proxyClient,rurl,null);
        log.info("PNG请求Date url :" + String.format(apimap.get("url").toString(), apimap.get("guid"), sdf.format(Long.valueOf(parameter.getStartTime()+"000")),sdf.format(Long.valueOf(parameter.getEndTime()+"000"))));
        log.info("PNG请求url :" + rurl);
        log.info("PNG返回数据 :" + rs);
        Map rsmap = (Map) JSON.parse(rs);
        List<PngBetLong> list = JSON.parseArray(rsmap.get("Data").toString(), PngBetLong.class);
        return list;
    }
}
