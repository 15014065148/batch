package com.eveb.saasops.batch.sys.util;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.SysEnum;
import com.eveb.saasops.batch.sys.service.OKHttpClientService;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.Authenticator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OkHttpProxyUtils {

    @Autowired
    private OKHttpClientService service;

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";
    public final static String PATCH = "PATCH";
    public final static String APPLICATION_JSON = "application/json";
    public final static String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public final static String APPLICATION_FORM_ENTITY = "multipart/form-data";
    private final static String UTF8 = "UTF-8";
    private final static String GBK = "GBK";
    private final static String DEFAULT_CHARSET = UTF8;
    private final static String DEFAULT_METHOD = GET;
    private final static boolean DEFAULT_LOG = true;



    public OkHttpClient client;

    public OkHttpClient proxyClient;

    /***波音请求实例，加入拦截器及代理*/
    public OkHttpClient bbinclient;

    public OkHttpClient payClient;


    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(60, 5, TimeUnit.MINUTES))
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS).build();
        proxyClient=setProxys(new OkHttpClient.Builder()).connectionPool(new ConnectionPool(60, 5, TimeUnit.MINUTES))
                .readTimeout(111, TimeUnit.SECONDS)
                .connectTimeout(111, TimeUnit.SECONDS).build();
        initBbinClient();

        payClient= new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request=chain.request().newBuilder()
                                .addHeader("Content-Type","application/json")
                                .addHeader("Accept","application/json")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectionPool(new ConnectionPool(60, 5, TimeUnit.MINUTES))
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS).build();
    }

    /***
     * 初始化波音连接
     */
    private void initBbinClient() {
        OkHttpClient.Builder bbinbuilder =setProxys(new OkHttpClient.Builder());
        /***因波音限制API并发数，故取消等待***/
        bbinbuilder.connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES));
        bbinbuilder.readTimeout(20, TimeUnit.SECONDS);
        bbinclient = bbinbuilder.connectTimeout(20, TimeUnit.SECONDS).build();
    }

    /**
     * 初始化带着请求头信息的请求
     * @param headMap
     * @return
     */
    public OkHttpClient initHeadClient(Map<String,String > headMap) {
        /**使用代理**/
        OkHttpClient.Builder builder = setProxys(new OkHttpClient.Builder());
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder=chain.request().newBuilder();
                if (MapUtils.isNotEmpty(headMap)) {
                    headMap.forEach(builder::addHeader);
                }
                Request request=builder.build();
                return chain.proceed(request);
            }
        });
        return builder.build();
    }



    public OkHttpClient.Builder setProxys(OkHttpClient.Builder builder ) {
        List<String> proxylist = service.getProxys(SysEnum.enumProxy.getKey());
        if (proxylist.size() > 0) {
            builder.proxySelector(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {
                    List<Proxy> list = new ArrayList<>();
                    proxylist.forEach(proxystr -> {
                        Map map = JSON.parseObject(proxystr);
                        switch (map.get("proxyType").toString().toUpperCase()) {
                            case "SOCKS":
                                list.add(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(map.get("ip").toString(), (Integer) map.get("port"))));
                                break;
                            case "HTTP":
                                list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(map.get("ip").toString(), (Integer) map.get("port"))));
                                break;
                        }
                    });
                    return list;
                }
                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    log.error("uri: {} socketAddress: {} IOException: {}", uri, sa, ioe);
                }
            }).proxyAuthenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    /**默认*/
                    String user = "eveada";
                    String password = "hALTendi";
                    for (String str : proxylist) {
                        Map map = JSON.parseObject(str);
                        if ("HTTP".equals(map.get("proxyType").toString().toUpperCase())) {
                            user = map.get("user").toString();
                            password = map.get("password").toString();
                        }
                    }
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", Credentials.basic(user, password))
                            .build();
                }
            });
        }
        return builder;
    }

    /**
     * GET请求
     *
     * @param url URL地址
     * @return
     */
    public String get(OkHttpClient httpClient, String url)throws Exception {
        return execute(OkHttp.builder().url(url).build(), httpClient);
    }

    /**
     * GET请求
     *
     * @param url URL地址
     * @return
     */
    public String get(OkHttpClient httpClient, String url, String charset)throws Exception {
        return execute(OkHttp.builder().url(url).responseCharset(charset).build(), httpClient);
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public String get(OkHttpClient httpClient, String url, Map<String, String> queryMap)throws Exception {
        return execute(OkHttp.builder().url(url).queryMap(queryMap).build(), httpClient);
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public String get(OkHttpClient httpClient, String url, Map<String, String> queryMap, String charset)throws Exception {
        return execute(OkHttp.builder().url(url).queryMap(queryMap).responseCharset(charset).build(), httpClient);
    }

    /**
     * POST
     * application/json
     *
     * @param url
     * @param obj
     * @return
     */
    public String postJson(OkHttpClient httpClient, String url, Object obj) throws Exception {
        return execute(OkHttp.builder().url(url).method(POST).data(JSON.toJSONString(obj)).mediaType(APPLICATION_JSON).build(), httpClient);
    }

    public  String postXml(OkHttpClient httpClient,String url,String param) {
        return postXmlParams(httpClient,url, param, null);
    }
    public  String postXml(OkHttpClient httpClient,String url,String param,String mediaType) {
        return postXmlParams(httpClient,url, param, mediaType);
    }
    /**
     * @param url
     * @param xml
     * @return
     */
    public  String postXmlParams(OkHttpClient httpClient,String url, String xml, String mediaType) {
        String responseBody = "";
        RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.util.StringUtils.isEmpty(mediaType) ? "application/xml; charset=utf-8" : mediaType), xml);
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(requestBody);
        Request request = builder.build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.debug("post方法url地址{}参数为{}", url, xml);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * POST
     * application/x-www-form-urlencoded
     *
     * @param url
     * @param formMap
     * @return
     */
    public String postForm(OkHttpClient httpClient, String url, Map<String, String> formMap)throws Exception {
        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors
                    .joining("&"));
        }
        return execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    private String post(OkHttpClient httpClient, String url, String data, String mediaType, String charset)throws Exception {
        return execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(mediaType).responseCharset(charset).build(), httpClient);
    }

    /**
     * delete
     * @param httpClient
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public String delete(OkHttpClient httpClient, String url, String data)throws Exception {
        return execute(OkHttp.builder().url(url).method(DELETE).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    /**
     * put
     * @param httpClient
     * @param url
     * @param formMap
     * @return
     * @throws Exception
     */
    public String putForm(OkHttpClient httpClient, String url, Map<String, String> formMap)throws Exception {
        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors
                    .joining("&"));
        }
        return execute(OkHttp.builder().url(url).method(PUT).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    /**
     * 通用执行方法
     */
    private String execute(OkHttp okHttp, OkHttpClient httpClient) throws Exception {
        if (StringUtils.isBlank(okHttp.requestCharset)) {
            okHttp.requestCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isBlank(okHttp.responseCharset)) {
            okHttp.responseCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isBlank(okHttp.method)) {
            okHttp.method = DEFAULT_METHOD;
        }
        if (StringUtils.isBlank(okHttp.mediaType)) {
            okHttp.mediaType = APPLICATION_JSON;
        }
        if (okHttp.requestLog) {//记录请求日志
            log.info(okHttp.toString());
        }

        String url = okHttp.url;

        Request.Builder builder = new Request.Builder();

        if (MapUtils.isNotEmpty(okHttp.queryMap)) {
            String queryParams = okHttp.queryMap.entrySet().stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("&"));
            url = String.format("%s%s%s", url, url.contains("?") ? "&" : "?", queryParams);
        }
        builder.url(url);

        if (MapUtils.isNotEmpty(okHttp.headerMap)) {
            okHttp.headerMap.forEach(builder::addHeader);
        }

        String method = okHttp.method.toUpperCase();
        String mediaType = String.format("%s;charset=%s", okHttp.mediaType, okHttp.requestCharset);

        if (StringUtils.equals(method, GET)) {
            builder.get();
        } else if (ArrayUtils.contains(new String[]{POST, PUT, DELETE, PATCH}, method)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), okHttp.data);
            builder.method(method, requestBody);
        }else
        {
            throw new Exception(String.format("http method:%s not support!", method));
        }
        String result;
        try {
            Response response = httpClient.newCall(builder.build()).execute();
            result = response.body().string();
            if (okHttp.responseLog) {//记录返回日志
                log.info(String.format("Got response->%s", result));
            }
        } catch (Exception e) {
            log.error(okHttp.toString(), e);
            return null;
        }
        return result;
    }

    @Builder
    @ToString(exclude = {"requestCharset", "responseCharset", "requestLog", "responseLog"})
    static class OkHttp {
        private String url;
        @Builder.Default
        private String method = DEFAULT_METHOD;
        private String data;
        @Builder.Default
        private String mediaType = APPLICATION_JSON;
        private Map<String, String> queryMap;
        private Map<String, String> headerMap;
        @Builder.Default
        private String requestCharset = DEFAULT_CHARSET;
        @Builder.Default
        private boolean requestLog = DEFAULT_LOG;
        @Builder.Default
        private String responseCharset = DEFAULT_CHARSET;
        @Builder.Default
        private boolean responseLog = DEFAULT_LOG;
    }
}
