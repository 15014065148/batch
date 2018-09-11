package com.eveb.saasops.batch.game.pt2.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pt2.domain.Pt2BetLog;
import com.eveb.saasops.batch.game.pt2.domain.Pt2RequestParameter;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class Pt2Request {

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    public List<Pt2BetLog> request(Pt2RequestParameter parameter)throws Exception {
        List<Pt2BetLog> rsList = new ArrayList<>();
        Map paramap = new HashMap();
        Map apimap = (Map) JSON.parse(parameter.getApi().getSecureCode());
        paramap.put("secretKey", apimap.get("secretKey"));
        paramap.put("username", parameter.getApi().getAgyAcc());
        paramap.put("password", parameter.getApi().getMd5Key());
        String loginrs = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, apimap.get("loginurl").toString(), paramap);
        Map maps = (Map) JSON.parse(loginrs);
        log.info("PT2请求Token :" + maps);
        String historyurl = String.format(apimap.get("historyurl").toString(), parameter.getOffset(), ApplicationConstant.CONSTANT_PT2_DATA_MAX_COUNT, parameter.getStartTime(), parameter.getEndTime());
        String rsjson = okHttpProxyUtils.get(initPt2Client(maps.get("accessToken").toString()), historyurl);
        log.info("PT2请求url :" + historyurl);
        log.info("PT2返回数据 :" + rsjson);
        List<Pt2BetLog> list = JSON.parseArray(rsjson, Pt2BetLog.class);
        rsList.addAll(list);
        if (list != null && list.size() > ApplicationConstant.CONSTANT_PT2_DATA_MAX_COUNT) {
            parameter.setOffset(parameter.getOffset() + 1);
            rsList.addAll(request(parameter));
        }
        return list;
    }

    /***
     * 初始化连接
     * @param token
     * @return
     */
    private OkHttpClient initPt2Client(String token)
    {
        /**使用代理**/
        OkHttpClient.Builder pt2builder = okHttpProxyUtils.setProxys(new OkHttpClient.Builder());
        pt2builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("x-access-token", token).build();
                return chain.proceed(request);
            }
        });
        return pt2builder.build();
    }
}
