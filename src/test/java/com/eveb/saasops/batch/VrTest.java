package com.eveb.saasops.batch;

import com.eveb.saasops.batch.sys.util.AesUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VrTest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    /**
     * 新增用户测试
     */
    @Test
    public void testCreateUser() throws Exception {
        String url = "https://fe.vrbetdemo.com/Account/CreateUser";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "    \"playerName\": \"testUser2\"\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);//ueSZdFPTZ0V9Ll+Lh5fsaMdBFNfclxzJHtzV75+SqlUWOZ1NzclwfzkBQ8vcfSUU
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, url, map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

    /**
     * 用户登入测试
     */
    @Test
    public void testLogin() throws Exception {
        String url = "https://fe.vrbetdemo.com/Account/LoginValidate";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "playerName=testUser&loginTime=2018-08-01T08:03:56Z";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        requesForm=URLEncoder.encode(requesForm,"utf-8");
        System.out.println("URLEncode之后的数据：" + requesForm);
        map.put("data", requesForm);
        String requetUrl=url+"?"+"version=1.0&id=EVEB&data="+requesForm;
        System.out.println(requetUrl);
          String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient,url,map);
        System.out.println(result);
    }
    /**
     * 强制玩家注销
     */
    @Test
    public void testLoginOut() throws Exception {
        String url = "https://fe.vrbetdemo.com/Account/KickUser";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "    \"playerName\": \"testUser\"\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

    /**
     * 玩家钱包转点
     */
    @Test
    public void testTransaction() throws Exception {
        String url = "https://fe.vrbetdemo.com/UserWallet/Transaction";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "\"serialNumber\": \"KDSJFIOELKDFLKUJEO\",\n" +
                "    \"playerName\": \"testUser\",\n" +
                "\"type\": 0,\n" +
                "\"amount\": 50000,\n" +
                "\"createTime\": \"2018-07-18T08:03:56Z\"\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

    /**
     * 玩家钱包转点记录查询
     */
    @Test
    public void testTransactionRecord() throws Exception {
        String url = "https://fe.vrbetdemo.com/UserWallet/TransactionRecord";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "\"startTime\": \"2018-07-18T08:03:56Z\",\n" +
                "\"endTime\": \"2018-07-18T08:03:56Z\",\n" +
                "\"serialNumber\": \"KDSJFIOELKDFLKUJEO\",\n" +
                "\"playerName\": \"testUser\",\n" +
                "\"recordPage\": 0,\n" +
                "\"recordCountPerPage\": 100\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

    /**
     * 玩家余额查询
     */
    @Test
    public void testUserBalance() throws Exception {
        String url = "https://fe.vrbetdemo.com/UserWallet/Balance";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "\"playerName\": \"testUser\"\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

    /**
     * 玩家投注记录查询
     */
    @Test
    public void testBetRecord() throws Exception {
        String url = "https://fe.vrbetdemo.com/MerchantQuery/Bet";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }
    /**
     * 玩家追号记录查询
     */
    @Test
    public void testTrackRecord() throws Exception {
        String url = "https://fe.vrbetdemo.com/MerchantQuery/Schedule";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "\"startTime\": \"2018-07-28T10:04:34Z\",\n" +
                "\"endTime\": \"2018-07-29T10:04:34Z\",\n" +
                "\"channelId\": -1,\n" +
                "\"playerName\": \"\",\n" +
                "\"serialNumber\": \"\",\n" +
                "\"state\": -1,\n" +
                "\"recordCountPerPage\": 450,\n" +
                "\"recordPage\": 0\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

    /**
     * 玩家打赏记录查询
     */
    @Test
    public void testDonateRecord() throws Exception {
        String url = "https://fe.vrbetdemo.com/MerchantQuery/Donate";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "\"startTime\": \"2018-07-28T00:00:00Z\",\n" +
                "\"endTime\": \"2018-07-28T12:00:00Z\",\n" +
                "\"channelId\":-1,\n" +
                "\"playerName\": \"\",\n" +
                "\"serialNumber\": \"\",\n" +
                "\"recordCountPerPage\": 1000,\n" +
                "\"recordPage\": 0\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }
    /**
     * 通道查询
     * TODO:该接口没调通
     */
    @Test
    public void testChannelRecord() throws Exception {
        String url = "https://fe.vrbetdemo.com/MerchantQuery/Channel";
        String key = "JT406B4DTD6N8VB0NDP8PBTHD8FB0PNZ";
        Map map = new HashMap();
        map.put("version", "1.0");
        map.put("id", "EVEB");

        String json = "{\n" +
                "\"currentTime\":\"2018-07-28T04:08:52Z\"\n" +
                "}";
        //加密
        String requesForm = AesUtil.aesEncrypt32(json, key);
        System.out.println("加密后的数据：" + requesForm);
        map.put("data", requesForm);
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient,url,map);
        //解密
        String decryptResult = AesUtil.aesDecrypt32(result, key);
        System.out.println("解密的数据：" + decryptResult);
    }

 public String postMap(String url, Map<String, String> map) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            response = httpClient.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            long lenth = entity.getContentLength();
            if(lenth != -1 && lenth < 2048)
            {
                result = EntityUtils.toString(entity, "UTF-8");
            }else{
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }
    public String get(String url)
      {
                 String result = null;
                 CloseableHttpClient httpClient = HttpClients.createDefault();
                 HttpGet get = new HttpGet(url);
                 CloseableHttpResponse response = null;
                 try {
                         response = httpClient.execute(get);
                         if(response != null && response.getStatusLine().getStatusCode() == 200)
                             {
                                 HttpEntity entity = response.getEntity();
                                 result = entityToString(entity);
                             }
                         return result;
                     } catch (IOException e) {
                         e.printStackTrace();
                     }finally {
                         try {
                                 httpClient.close();
                                 if(response != null)
                                     {
                                         response.close();
                                     }
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                     }
                 return null;
             }
}
