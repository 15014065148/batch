package com.eveb.saasops.batch.sys.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class HttpRequestUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);    //日志记录

    /**
     * 发送Post请求
     *
     * @param url 路径
     * @return
     */
    public static String httpsPost(String url, String param) throws Exception {
        //构建请求
        URL postUrl = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) postUrl.openConnection();//打开连接
        con.setRequestMethod("POST");//post方式提交

        con.setDoOutput(true);//打开读写属性，默认均为false
        con.setDoInput(true);
        con.setUseCaches(false);//Post请求不能使用缓存
        con.setInstanceFollowRedirects(true);

        //添加头信息
        con.setRequestProperty("Content-Type", "application/json");

        DataOutputStream out = new DataOutputStream(con.getOutputStream());

        //发送请求
        String data = param;
        out.writeBytes(data);
        out.flush();
        out.close();

        //接收数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String line;
        StringBuffer responseText = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            responseText.append(line).append("\r\n");
        }
        reader.close();
        con.disconnect();

        return responseText.toString();
    }

    /**
     * 发送Post请求
     *
     * @param url 路径
     * @return
     */
    public static String httpsPost(String url) throws Exception {
        //构建请求
        URL postUrl = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) postUrl.openConnection();//打开连接
        con.setRequestMethod("POST");//post方式提交
        con.setDoOutput(true);//打开读写属性，默认均为false
        con.setDoInput(true);
        con.setUseCaches(false);//Post请求不能使用缓存
        con.setInstanceFollowRedirects(true);
        //添加头信息
        con.setRequestProperty("Content-Type", "application/json");
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        //发送请求
        out.flush();
        out.close();
        //接收数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String line;
        StringBuffer responseText = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            responseText.append(line).append("\r\n");
        }
        reader.close();
        con.disconnect();
        return responseText.toString();
    }

    public static String httpPost(String url) throws Exception {
        //构建请求
        URL postUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postUrl.openConnection();//打开连接
        con.setRequestMethod("POST");//post方式提交
        con.setDoOutput(true);//打开读写属性，默认均为false
        con.setDoInput(true);
        con.setUseCaches(false);//Post请求不能使用缓存
        con.setInstanceFollowRedirects(true);
        //添加头信息
        con.setRequestProperty("Content-Type", "application/json");
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        //发送请求
        out.flush();
        out.close();
        //接收数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String line;
        StringBuffer responseText = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            responseText.append(line).append("\r\n");
        }
        reader.close();
        con.disconnect();
        return responseText.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static String httpGet(String url, String token) {
        //get请求返回结果
        String strResult = null;
        try {
            RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
            CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            //发送get请求
            HttpGet request = new HttpGet(url);
            //添加头信息
            request.setHeader("x-access-token", token);

            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return strResult;
    }
    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static Document httpGet(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}