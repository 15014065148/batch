package com.eveb.saasops.batch.game.mg.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg.domain.MgBetLog;
import com.eveb.saasops.batch.game.mg.domain.MgRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MgRequest {

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    private final String requestUrl="http://ag.adminserv88.com/lps/secure/hortx/%s?start=%s&end=%s&timezone=%s";
    private final String loginUrl="http://ag.adminserv88.com/lps/j_spring_security_check?j_username=%s&j_password=%s";

    public List<MgBetLog> request(MgRequestParameter parameter) throws Exception {
        String loginmsg=okHttpProxyUtils.postJson(initMgClient(""),String.format(loginUrl,parameter.getApi().getAgyAcc(),parameter.getApi().getMd5Key()),null);
        log.info(parameter.getApi().getAgyAcc()+" MG请求Token :"+loginmsg);
        Map map= (Map) JSON.parse(loginmsg);
        String url = String.format(requestUrl,map.get("id"),parameter.getStartdate(),parameter.getEnddate(),parameter.getTimezone());
        String rs=okHttpProxyUtils.get(initMgClient(map.get("token").toString()),url);
        log.info("MG请求url :"+url);
        log.info("MG返回数据 :"+rs);
        return JSON.parseArray(rs,MgBetLog.class);
    }

    /***
     * 初始化连接
     * @param token
     * @return
     */
    private OkHttpClient initMgClient(String token)
    {
        /**使用代理**/
        OkHttpClient.Builder mgbuilder = okHttpProxyUtils.setProxys(new OkHttpClient.Builder());
        log.info("MG请求使用代理");
        mgbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Requested-With", "X-Api-Client")
                        .addHeader("X-Api-Call", "X-Api-Client")
                        .addHeader("X-Api-Auth", token).build();
                return chain.proceed(request);
            }
        });
        return mgbuilder.build();
    }
}
