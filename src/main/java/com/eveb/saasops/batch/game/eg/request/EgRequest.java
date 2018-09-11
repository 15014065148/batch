package com.eveb.saasops.batch.game.eg.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.eg.domain.EgBetLog;
import com.eveb.saasops.batch.game.eg.domain.EgRequestParameter;
import com.eveb.saasops.batch.game.eg.service.EgElasticService;
import com.eveb.saasops.batch.sys.util.AesUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Component
public class EgRequest {

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    @Autowired
    private EgElasticService service;

    private static final String paraurl="cagent=%s&vendorid=%s&method=gbrbv&time=%s";

    public List<EgBetLog> request(EgRequestParameter parameter) throws Exception {
        String agent=parameter.getApi().getAgyAcc();
        String key= parameter.getApi().getMd5Key();
        String maxVendorId=service.getEvBetLogMax(agent);
        String para=String.format(paraurl,agent,maxVendorId,diffSeconds());
        String url=parameter.getApi().getPcUrl()+"doBusiness.do?params="+ AesUtil.aesHexEncrypt(para,key);
        String rs= okHttpProxyUtils.get(initEvClient(agent),url);
        log.info("EV请求url :"+parameter.getApi().getPcUrl()+"?params="+para);
        log.info("EV返回数据 :"+rs);
        Map rsmap=JSON.parseObject(rs);
        return JSON.parseArray(rsmap.get("msg").toString(),EgBetLog.class);
    }

    /***
     * 初始化连接
     * @param agent
     * @return
     */
    private OkHttpClient initEvClient(String agent)
    {
        /**使用代理**/
        OkHttpClient.Builder evbuilder = okHttpProxyUtils.setProxys(new OkHttpClient.Builder());
        evbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("User-Agent", "WEB_LIB_GI_"+agent).build();
                return chain.proceed(request);
            }
        });
        return evbuilder.build();
    }

    /**
     * 获取指定时间到格林威治时间的秒数
     * UTC：格林威治时间1970年01月01日00时00分00秒（UTC+8北京时间1970年01月01日08时00分00秒）
     * @return
     */
    public static long diffSeconds(){
        LocalDateTime dateTime=LocalDateTime.now();
        return dateTime.toEpochSecond(ZoneOffset.of("+08:00"));
    }
}
