package com.eveb.saasops.batch;

import com.eveb.saasops.batch.game.gg.request.GgDESEncrypt;
import com.eveb.saasops.batch.sys.util.MD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 2018/07-25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GgRequestTest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    /**
     * 创建或检测账户
     *
     * @throws Exception
     */
    @Test
    public void createOrCheckAccout() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String method = "ca";//数值 ,是一個常數
        String type = "1";
        String cur = "CNY";//货币种类
        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/method=" + method + "/\\\\/actype=" + type + "/\\\\/cur=" + cur);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 查询账户余额
     *
     * @throws Exception
     */
    @Test
    public void getBalance() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String method = "gb";//数值 ,是一個常數
        String cur = "CNY";//货币种类
        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/method=" + method + "/\\\\/cur=" + cur);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 账户转账
     *
     * @throws Exception
     */
    @Test
    public void transferCredit() throws Exception {
        String cagent = "TE231";//代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String method = "tc";//数值 ,是一個常數
        String billno = cagent + "2018080102";//billno=(cagent+序列)
        String type = "IN"; //值 = “IN” or “OUT”,IN: 从网站账号转款到游戏账号,OUT: 从游戏账号转款到网站账号
        double credit = 200.00;//转款额度，最大 2 位小数
        String cur = "CNY";//货币种类

        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/method=" + method + "/\\\\/billno=" + billno + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/cur=" + cur);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 查询转账订单状态
     *
     * @throws Exception
     */
    @Test
    public void queyOrderStatus() throws Exception {
        String cagent = "TE231";//代理编码
        String desKey = "12345678";
        String md5Key = "123456";


        String method = "qx";//数值 ,是一個常數
        String billno = cagent + "2018080102";//billno=(cagent+序列)


        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/method=" + method + "/\\\\/billno=" + billno);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 获取游戏地址
     *
     * @throws Exception
     */
    @Test
    public void forwardGame() throws Exception {
        String cagent = "TE231";//代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String method = "fw";//数值 ,是一個常數
        String sid = cagent + "123465789";   //sid=(cagent+序列),
        String lang = "zh-CN"; //语言
        String gametype = "101"; //游戏类型，“0”为游戏大厅，请参考第五章游戏类型说明
        String ip = "121.97.17.204"; //玩家 ip
        String ishttps = "1"; //ishttps=1 返回 https 的游戏地址, 不传或其它值返回 http 的地址


        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/method=" + method + "/\\\\/sid=" + sid + "/\\\\/lang=" + lang + "/\\\\/gametype=" + gametype + "/\\\\/ip=" + ip + "/\\\\/ishttps=" + ishttps);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 修改账户密码
     *
     * @throws Exception
     */
    @Test
    public void updatePassword() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String newpassword = "123456";//游戏账号的密码必须
        String method = "up";//数值 ,是一個常數
        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/newpassword=" + newpassword + "/\\\\/method=" + method);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do/", mapParam);System.out.println(result);
    }

    /**
     * 踢出用户
     *
     * @throws Exception
     */
    @Test
    public void killSession() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String method = "ty";//数值 ,是一個常數
        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/method=" + method);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 重置账户密码
     *
     * @throws Exception
     */
    @Test
    public void resetPassword() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String loginname = "ybhjeff111";//游戏账号的登录名
        String password = "123456";//游戏账号的密码
        String method = "rp";//数值 ,是一個常數
        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/password=" + password + "/\\\\/method=" + method);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://testapi.gg626.com/api/doLink.do", mapParam);
        System.out.println(result);
    }

    /**
     * 读取用户投注记录
     *
     * @throws Exception
     */
    @Test
    public void betRecordReport() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String startdate = "2018-08-01 15:50:00";//开始时间，格式 yyyy-MM-ddHH:mm:ss
        String enddate = "2018-08-01 16:00:00";//游戏账号的密码
        //String gameId = "101";//游戏编码   + "/\\\\/gameId=" + gameId
        String method = "tr";//数值 ,是一個常數    5分钟
        method = "br";//10分钟数据

        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/startdate=" + startdate + "/\\\\/enddate=" + enddate  + "/\\\\/method=" + method);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "http://testapi.gg626.com:5050/api/doReport.do", mapParam);
        System.out.println(result);
    }

    /**
     * 读取用户历史投注记录
     *
     * @throws Exception
     */
    @Test
    public void historyBetRecordReport() throws Exception {
        String cagent = "TE231"; //代理编码
        String desKey = "12345678";
        String md5Key = "123456";

        String startdate = "2018-08-01 15:50:00";//开始时间，格式 yyyy-MM-ddHH:mm:ss
        String enddate = "2018-08-01 15:55:00";//游戏账号的密码
        String gameId = "101";//游戏编码
        String method = "hbr";//数值 ,是一個常數

        GgDESEncrypt des = new GgDESEncrypt(desKey);
        String params = des.encrypt("cagent=" + cagent + "/\\\\/startdate=" + startdate + "/\\\\/enddate=" + enddate + "/\\\\/gameId=" + gameId + "/\\\\/method=" + method);
        String key = MD5.getMD5(params + md5Key);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("params", params);
        mapParam.put("key", key);

        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "http://testapi.gg626.com:5050/api/doReport.do/", mapParam);
        System.out.println(result);
    }


}
