package com.eveb.saasops.batch.game.pt.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pt.domain.PTBetLog;
import com.eveb.saasops.batch.game.pt.domain.PtRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class PTRequest {

    @Value("${pt.url}")
    public String requestUrl;
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    /**等待时常，避免任务发送太快**/
    private final int waitMillis=3000;


    public List<PTBetLog> getBetList(PtRequestParameter para) throws Exception {
        /**从第一页开始查询**/
        List<PTBetLog> list = new ArrayList<>();
        String key = ((Map) JSON.parse(para.getApi().getSecureCode())).get("X_ENTITY_KEY").toString();
        String url = requestUrl.replace("{startdate}", para.getStartdate()).replace("{enddate}",para.getEnddate())+para.getPagenum();
//        String rsstr = HttpsRequestUtil.send(url, key);
        String rsstr=okHttpProxyUtils.postJson(initPtClient(key),url,null);
        log.info("PT请求URL :" + url);
        log.info("PT返回数据 :" + rsstr);
        Map rsmap = (Map) JSON.parse(rsstr);
        Map pagemap = (Map) JSON.parse(rsmap.get("pagination").toString());
        list = JSON.parseArray(rsmap.get("result").toString(), PTBetLog.class);
        int totalPages = Integer.parseInt(pagemap.get("totalPages").toString());
        /**根据返回的总页数循环往下查询**/
        if (para.getPagenum() < totalPages) {
            Thread.sleep(waitMillis);
            para.setPagenum(para.getPagenum() + 1);
            list.addAll(getBetList(para));
        }
        return list;
    }

    /***
     * 初始化PT连接
     */
    private OkHttpClient initPtClient(String X_ENTITY_KEY)throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        InputStream stream = this.getClass().getResourceAsStream("/key/VBETCNYTLE.p12");
        File file = new File("VBETCNYTLE.p12");
        FileUtils.copyInputStreamToFile(stream, file);
        FileInputStream fis = new FileInputStream(file);
        ks.load(fis, "iQ3xuZrS".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "iQ3xuZrS".toCharArray());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        OkHttpClient.Builder ptbuilder = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        }).sslSocketFactory(sc.getSocketFactory());
        ptbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("X_ENTITY_KEY", X_ENTITY_KEY).build();
                return chain.proceed(request);
            }
        });
        ptbuilder.readTimeout(60, TimeUnit.SECONDS);
        ptbuilder.connectTimeout(20, TimeUnit.SECONDS);
        return ptbuilder.build();
    }
}
