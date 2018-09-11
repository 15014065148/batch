package com.eveb.saasops.batch.game.mg2.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg2.bean.Mg2BetLogModel;
import com.eveb.saasops.batch.game.mg2.bean.Mg2RequestParameter;
import com.eveb.saasops.batch.game.mg2.bean.Mg2Response;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.sys.util.JsonUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class Mg2Request {

    @Setter
    @Getter
    private static Map<String,String> tokenMap;

    /*private final  static String MG2_URL ="https://api.adminserv88.com";
    private static String USER_NAME_AUTH ="LBCNY_auth";
    private static String PASSWORD_AUTH ="uBDEDZat7XWVPPL#7$7vgFsA";
    private static String USER_NAME_API ="LBCNY_api";
    private static String PASSWORD_API ="DuTmexdh^oRB2o^8zInOjRv^";*/
    private static Integer PARENT_ID = 957535;
    private static String TRANSACTION__FEED_URL ="/v1/feed/transaction";
    /**等待时常，避免任务发送太快**/
    private final int waitMillis=3000;
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    @Autowired
    private JsonUtil jsonUtil;

    @SuppressWarnings("unchecked")
    public List<Mg2BetLogModel> getTransactionFeed(Mg2RequestParameter parameter) throws InterruptedException {
        TGmApi api = parameter.getApi();
        Map<String ,String> secureCode=(Map<String,String>)JSON.parse(api.getSecureCode());
        if(tokenMap == null){
            mg2Token(secureCode.get("userNameApi"),secureCode.get("passwordApi"),api.getAgyAcc(),api.getMd5Key(),api.getPcUrl());
        }
        String rs = null;
        try {
            rs = mgHttpClientGet(String.format(api.getPcUrl()+TRANSACTION__FEED_URL+"?company_id=%s&start_time=%s&end_time=%s&page_size=%s&page=%s",
                    secureCode.get("parentId"), parameter.getStartTime(),parameter.getEndTime(),parameter.getPageSize(),parameter.getPage()),tokenMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mg2Response mg2Response=jsonUtil.fromJson(rs,Mg2Response.class);
        log.info("Mg2Response {}   "+JSON.toJSON(mg2Response));
        List<Mg2BetLogModel> mg2BetLogModels = new ArrayList<>(mg2Response.getData());
        /**根据条数如果等于最大值，请求量*2 **/
        if (mg2BetLogModels.size() == parameter.getPageSize()) {
            Thread.sleep(waitMillis);
            parameter.setPageSize(parameter.getPageSize() * 2);
            mg2BetLogModels =(getTransactionFeed(parameter));
        }
        return mg2BetLogModels;
    }

    public static String getTime(Date time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(time);
    }

    public static Date getTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
          return  sdf.parse(time);
        } catch (ParseException e) {
            log.debug("时间格式化错误 time{}" +time);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  获取mg2Token
     * {"access_token":"xxxxxxxxxxxxxxx","token_type":"bearer","refresh_token":"x xxxxxxxx",
     *  "expires_in":3599,"scope":"scope1:r,scope2:w","jti":"a242676010e8-4ed3-95c9-a9ac991eaec6"}
     * @return
     * @throws Exception
     */
    public void mg2Token(String userApi,String passwordApi,String userNameAuth,String passwordAuth,String mg2Url)  {
        Map<String,String> map =new HashMap<>();
        map.put("grant_type","password");
        map.put("username",userApi);
        map.put("password",passwordApi);
        String rs = null;
        try {
            rs = okHttpProxyUtils.postForm(initMg2Client(userNameAuth,passwordAuth),mg2Url+"/oauth/token",map);
        } catch (Exception e) {
            log.debug("创建Mg2Token 方法请求url地址{}地址为{},请求头为{}", "/oauth/token", JSON.toJSONString(map));
            e.printStackTrace();
        }
        Map<String,Object> rsMap=jsonUtil.toMap(rs);
        if(this.tokenMap == null){
            this.tokenMap =new HashMap<>();
        }
        this.tokenMap.put("Authorization",rsMap.get("token_type")+" "+rsMap.get("access_token"));
    }



    /**
     * Authorization: Basic dGVzdGF1dGh1c2VyOnRlc3RzZWNyZXQK
     * Accept: application/json ;charset=UTF-8
     * X-DAS-TX: 123e4567-e89b-12d3-a456-426655440000
     * X-DAS-CURRENCY: USD
     * X-DAS-TZ: UTC+9
     * X-DAS-LANG: en
     * @return
     */
     public OkHttpClient initMg2Client(String userNameAuth,String passwordAuth){
        /**使用代理**/
        OkHttpClient.Builder mgbuilder = okHttpProxyUtils.setProxys(new OkHttpClient.Builder());
        mgbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                       // .addHeader("Authorization", "Basic "+Base64.getEncoder().encodeToString((USERNAME+":"+PASSWORD).getBytes()))
                        .addHeader("Username",userNameAuth)
                        .addHeader("Password",passwordAuth)
                        .addHeader("Authorization","Basic TEJDTllfYXV0aDp1QkRFRFphdDdYV1ZQUEwjNyQ3dmdGc0E=")
                        .addHeader("Accept", "application/json;charset=UTF-8")
                        .addHeader("X-DAS-CURRENCY", "CNY")
                        .addHeader("X-DAS-TZ","UTC+8")
                        .addHeader("X-DAS-LANG","en")
                        .addHeader("X-DAS-TX",UUID.randomUUID().toString())
                        .addHeader("content-type","application/x-www-form-urlencoded")
                        .build();
                return chain.proceed(request);
            }
        });
        return mgbuilder.build();
    }



    public static Map<String,String> mg2HeadParams(){
        Map<String,String> headlers = new HashMap<>();
        headlers.put("Accept","application/json;charset=UTF-8");
        headlers.put("X-DAS-CURRENCY","CNY");
        headlers.put("X-DAS-TZ","UTC+8");
        headlers.put("X-DAS-LANG","en");
        headlers.put("X-DAS-TX-ID",UUID.randomUUID().toString());
        headlers.put("Content-type","application/json");
        return headlers;
    }

    /**
     * Get方法
     * @param url
     * @param headParams
     * @return
     */
    public String mgHttpClientGet(String url, Map<String, String> headParams) throws Exception {
        headParams.putAll(mg2HeadParams());
        /**使用代理**/
        OkHttpClient.Builder mgbuilder = okHttpProxyUtils.setProxys(new OkHttpClient.Builder());
        mgbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder =chain.request().newBuilder();
                for(String key: headParams.keySet()){
                    builder.addHeader(key,headParams.get(key));
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        });
        return okHttpProxyUtils.get(mgbuilder.build(),url);
    }

}
