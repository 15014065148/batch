package com.eveb.saasops.batch.game.pb.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pb.domain.PbBetLog;
import com.eveb.saasops.batch.game.pb.domain.PbRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Component
public class PbRequest {

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    private static final String ALGORITHM = "AES";
    private static final String INIT_VECTOR = "RandomInitVector";

    public List<PbBetLog> request(PbRequestParameter parameter) throws Exception
    {
        String agentCode=parameter.getApi().getAgyAcc();
        Map secureMap= (Map)JSON.parse(parameter.getApi().getSecureCode());
        String token=generateToken(agentCode,secureMap.get("agentKey").toString(),secureMap.get("secretKey").toString());
        Map headers = new HashMap();
        headers.put("userCode", agentCode);
        headers.put("token", token);
        String url = String.format("https://paapistg.oreo88.com/b2b/report/all-wagers?dateFrom=%s&dateTo=%s&settle=%s",parameter.getStartTime(),parameter.getEndTime(),parameter.getSettle());
        String result = okHttpProxyUtils.get(initPbClient(headers),url,new HashMap<>());
        log.info("PB请求url :"+url);
        log.info("PB返回数据 :"+result);
        return JSON.parseArray(result,PbBetLog.class);
    }

    /***
     * 初始化连接
     * @param head
     * @return
     */
    private OkHttpClient initPbClient(Map head)
    {
        /**使用代理**/
        OkHttpClient.Builder pbbuilder = new OkHttpClient.Builder();
        pbbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("userCode", head.get("userCode").toString())
                        .addHeader("token", head.get("token").toString()).build();
                return chain.proceed(request);
            }
        });
        return pbbuilder.build();
    }

    private static String generateToken(String agentCode, String agentKey, String secretKey) throws
            NoSuchAlgorithmException {
        String sTimestamp = String.valueOf(System.currentTimeMillis());
        String hashToken = DigestUtils.md5Hex(agentCode + sTimestamp + agentKey);
        String tokenPayLoad = String.format("%s|%s|%s", agentCode, sTimestamp, hashToken);
        String token = encryptAES(secretKey, tokenPayLoad);
        return token;
    }

    private static String encryptAES(String secretKey, String tokenPayLoad) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(tokenPayLoad.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}
