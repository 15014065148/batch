package com.eveb.saasops.batch;


import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pgcb.domain.PgcbCasinoBetLogModel;
import com.eveb.saasops.batch.game.pgcb.domain.PgcbGiftBetLogModel;
import com.eveb.saasops.batch.game.pgcb.domain.PgcbLotteryBetLogModel;
import com.eveb.saasops.batch.game.pgcb.domain.PgcbRequestParameterModel;
import com.eveb.saasops.batch.game.pgcb.request.PgcbDownloadFile;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.sys.util.DateUtil;
import com.eveb.saasops.batch.sys.util.MD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 盘古彩播, 返回"status": 1, 则正常
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PhCaiBoRequestTest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    //测试环境
    private String partner = "787217335"; //外接商户ID
    private String sign = "qrMRAq8D8rhWHgHwZVfjkfZ3kuQokqd50AJ"; //外接商户密钥;
    private String url = "http://api.pgcaibo123.com/";

    /*private String partner = "98912079"; //外接商户ID
    private String sign = "XDpQL9QY6yqcAAoVHtjucBhZEjUVsPYma7n"; //外接商户密钥;
    private String url = "http://wj.pg.live/";*/

    /**
     * 1.注册/登录
     *
     * @throws Exception
     */
    @Test
    public void testRegisterUser() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        String username = "ybhjeff123";
        String password = "123456";
        Integer orderNumber = 0;
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&password=" + password + "&sign=" + sign);
        mapParam.put("username", username);
        mapParam.put("password", password);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "register-user", mapParam);
        System.out.println(result);
    }

    //登录
    @Test
    public void testLoginUser() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        String username = "ybhjeff123";
        String password = "123456";
        Integer orderNumber = 0; //长度10位随机数字
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&password=" + password + "&sign=" + sign);
        mapParam.put("username", username);
        mapParam.put("password", password);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("token", token);
        mapParam.put("login_side", "1");//登陆方式：1电脑版，2手机网页版，3手机APP
        mapParam.put("ip", "192.168.5.208");
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "login-user", mapParam);
        System.out.println(result);
    }

    /**
     * 2.余额/转账
     *
     * @throws Exception
     */
    @Test
    public void testBalanceUser() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        String username = "ybhjeff123";
        Integer orderNumber = 0; //长度10位随机数字
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&sign=" + sign);
        mapParam.put("username", username);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "balance-user", mapParam);
        System.out.println(result);
    }

    //转账
    @Test
    public void testTransferUser() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        String username = "ybhjeff123";
        Integer orderNumber = 0; //长度10位随机数字
        String type = "in"; //in：从第三方转入到盘古彩票，  out：从盘古彩票转出到第三方
        String amount = "1000"; //转账金额
        String transationNo = "NO2018072601";//商户网站的订单号
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&type=" + type + "&amount=" + amount + "&transationNo=" + transationNo + "&sign=" + sign);
        mapParam.put("username", username);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("type", type);
        mapParam.put("amount", amount);
        mapParam.put("transationNo", transationNo);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "transfer-user", mapParam);
        System.out.println(result);
    }

    //账号转账查询
    @Test
    public void testTransferStatus() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        Integer orderNumber = 0; //长度10位随机数字
        String transationNo = "NO2018072501";//商户网站的订单号
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&transationNo=" + transationNo + "&sign=" + sign);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("transationNo", transationNo);
        mapParam.put("partner", partner);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "transfer-status", mapParam);
        System.out.println(result);
    }

    /**
     * 3.获取彩种
     *
     * @throws Exception
     */
    @Test
    public void testLotteries() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        Integer orderNumber = 0; //长度10位随机数字
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&sign=" + sign);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "lotteries", mapParam);
        System.out.println(result);
    }

    /**
     * 4.获取会员数字ID
     *
     * @throws Exception
     */
    @Test
    public void testNumberUser() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        String username = "ybhjeff123";
        Integer orderNumber = 0; //长度10位随机数字
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&sign=" + sign);
        mapParam.put("username", username);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "number-user", mapParam);
        System.out.println(result);
    }

    /**
     * 5.更新密码
     *
     * @throws Exception
     */
    @Test
    public void testUpdatePasswordUser() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        String username = "ybhjeff123";
        String newPassword = "123456";
        Integer orderNumber = 0;
        String type = "1";//更新密码类型1,更新PC登陆密码;2,更新APP登陆密码
        String token = MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&newPassword=" + newPassword + "&type=" + type + "&sign=" + sign);
        mapParam.put("username", username);
        mapParam.put("newPassword", newPassword);
        mapParam.put("orderNumber", orderNumber + "");
        mapParam.put("partner", partner);
        mapParam.put("type", type);
        mapParam.put("token", token);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.proxyClient, url + "update-password-user", mapParam);
        System.out.println(result);
    }

    /**
     * 6.彩票接水
     *
     * @throws Exception
     */
    @Test
    public void testLotteryUser() throws Exception {
        PgcbRequestParameterModel parameter = new PgcbRequestParameterModel();


        TGmApi api = new TGmApi();
        Map<String, String> map = new HashMap<String, String>();/*
        map.put("ftpIp","58.82.236.30");
        map.put("ftpUser","eveb");
        map.put("ftpPass","EFgcgi0ibvo9");*/
        api.setSecureCode("{\n" +"    \"ftpIp\": \"58.82.236.30\",\n" +"    \"ftpUser\": \"eveb\",\n" +"    \"ftpPass\": \"EFgcgi0ibvo9\"\n" +
                "}");
        parameter.setApi(api);

        /*parameter.setUrl("58.82.236.30");
        parameter.setUsername("eveb");
        parameter.setPassword("EFgcgi0ibvo9");*/

        Date startDate = DateUtil.parse("2018-07-26 10:00:00", DateUtil.FORMAT_18_DATE_TIME);
        Date endDate = DateUtil.parse("2018-07-30 10:30:00", DateUtil.FORMAT_18_DATE_TIME);
        parameter.setStartDate(startDate);
        parameter.setEndDate(endDate);
        //parameter.setRemotePath("201807/26");
        List<String> dateList = PgcbDownloadFile.downFileByDate(parameter);

        List<PgcbLotteryBetLogModel> lotteryList = new ArrayList<PgcbLotteryBetLogModel>();
        for (String jsonStr : dateList) {
            List<PgcbLotteryBetLogModel> lottery = JSON.parseArray(jsonStr, PgcbLotteryBetLogModel.class);
            lotteryList.addAll(lottery);
        }

        System.out.println(lotteryList);
    }

    /**
     * 7.礼物接水
     *
     * @throws Exception
     */
    @Test
    public void testGiftUser() throws Exception {
        PgcbRequestParameterModel parameter = new PgcbRequestParameterModel();

        TGmApi api = new TGmApi();
        Map<String, String> map = new HashMap<String, String>();/*
        map.put("ftpIp","58.82.236.30");
        map.put("ftpUser","eveb");
        map.put("ftpPass","EFgcgi0ibvo9");*/
        api.setSecureCode("{\n" +"    \"ftpIp\": \"58.82.236.30\",\n" +"    \"ftpUser\": \"eveb\",\n" +"    \"ftpPass\": \"EFgcgi0ibvo9\"\n" +
                "}");
        parameter.setApi(api);

        /*parameter.setUrl("58.82.236.30");
        parameter.setUsername("eveb");
        parameter.setPassword("EFgcgi0ibvo9");*/

        Date startDate = DateUtil.parse("2018-07-27 11:00:00", DateUtil.FORMAT_18_DATE_TIME);
        Date endDate = DateUtil.parse("2018-07-27 12:00:00", DateUtil.FORMAT_18_DATE_TIME);
        parameter.setStartDate(startDate);
        parameter.setEndDate(endDate);
        //parameter.setRemotePath("gift/201807/26");
        parameter.setRemotePath("gift/");
        List<String> dateList = PgcbDownloadFile.downFileByDate(parameter);

        List<PgcbGiftBetLogModel> giftList = new ArrayList<PgcbGiftBetLogModel>();
        for (String jsonStr : dateList) {
            List<PgcbGiftBetLogModel> gift = JSON.parseArray(jsonStr, PgcbGiftBetLogModel.class);
            giftList.addAll(gift);
        }
        System.out.println(giftList);
    }

    /**
     * 8.电子游戏接水
     *
     * @throws Exception
     */
    @Test
    public void testCasinoUser() throws Exception {
        PgcbRequestParameterModel parameter = new PgcbRequestParameterModel();

        TGmApi api = new TGmApi();
        Map<String, String> map = new HashMap<String, String>();/*
        map.put("ftpIp","58.82.236.30");
        map.put("ftpUser","eveb");
        map.put("ftpPass","EFgcgi0ibvo9");*/
        api.setSecureCode("{\n" +"    \"ftpIp\": \"58.82.236.30\",\n" +"    \"ftpUser\": \"eveb\",\n" +"    \"ftpPass\": \"EFgcgi0ibvo9\"\n" +
                "}");
        parameter.setApi(api);

        /*parameter.setUrl("58.82.236.30");
        parameter.setUsername("eveb");
        parameter.setPassword("EFgcgi0ibvo9");*/


        Date startDate = DateUtil.parse("2018-07-26 15:00:00", DateUtil.FORMAT_18_DATE_TIME);
        Date endDate = DateUtil.parse("2018-07-30 15:30:00", DateUtil.FORMAT_18_DATE_TIME);
        parameter.setStartDate(startDate);
        parameter.setEndDate(endDate);
        // parameter.setRemotePath("casino/201807/26");
        parameter.setRemotePath("casino/");

        List<String> dateList = PgcbDownloadFile.downFileByDate(parameter);

        List<PgcbCasinoBetLogModel> giftList = new ArrayList<PgcbCasinoBetLogModel>();
        for (String jsonStr : dateList) {
            List<PgcbCasinoBetLogModel> gift = JSON.parseArray(jsonStr, PgcbCasinoBetLogModel.class);
            giftList.addAll(gift);
        }
        System.out.println(giftList);
    }

    public static void main(String args[]) {
        System.out.println((new Date().getTime() + "").substring(9));
    }
}
