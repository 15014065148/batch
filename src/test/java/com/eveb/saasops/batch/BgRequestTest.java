package com.eveb.saasops.batch;


import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bg.domian.BgFishingBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgVideoBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgVideoTipBetLog;
import com.eveb.saasops.batch.game.bg.util.HashUtil;
import com.eveb.saasops.batch.sys.util.MD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.util.StringUtil;


import java.util.*;


/**
 * BG平台接口测试
 * 2018-08-07   Jeff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BgRequestTest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;


    /**
     * 创建代理号
     *
     * @throws Exception
     */
    @Test
    public void createAgent() throws Exception {
        String method = "open.agent.create";//请求路径
        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥
        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhjeff123";//代理登录ID
        String password = "123456";//代理登录密码, length(password)值范围：[16, 128]
        String sign = MD5.getMD5(random + sn + loginId + secretKey); //签名摘要sign=md5(random+sn+loginId+secretKey)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);
        postDate.put("password", password);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 创建会员
     *
     * @throws Exception
     */
    @Test
    public void createUser() throws Exception {
        String method = "open.user.create";//请求路径

        String agentPassword = "123456";//代理密码
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String digest = MD5.getMD5(random + sn + secretCode); //签名摘要digest=md5(random+sn+secretCode)

        String loginId = "ybhganme123";//登录ID
        String nickname = "ybhganme123";//会员昵称
        String agentLoginId = "ybhjeff123";//代理商账号，将会员账号创建到指定的代理名下

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);
        postDate.put("nickname", nickname);
        postDate.put("agentLoginId", agentLoginId);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 查询会员信息
     *
     * @throws Exception
     */
    @Test
    public void getUser() throws Exception {
        String method = "open.user.get";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String digest = MD5.getMD5(random + sn + loginId + secretCode); //签名摘要digest=md5(random+sn+loginId+secretCode)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);


        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }


    /**
     * 启用会员账号
     *
     * @throws Exception
     */
    @Test
    public void openUser() throws Exception {
        String method = "open.user.enable";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String digest = MD5.getMD5(random + sn + loginId + secretCode); //签名摘要digest=md5(random+sn+loginId+secretCode)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);


        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result); //result	 操作结果. 1: 成功; 0: 失败
    }

    /**
     * 停用会员账号
     *
     * @throws Exception
     */
    @Test
    public void closeUser() throws Exception {
        String method = "open.user.disable";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String digest = MD5.getMD5(random + sn + loginId + secretCode); //签名摘要digest=md5(random+sn+loginId+secretCode)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);//result	 操作结果. 1: 成功; 0: 失败
    }

    /**
     * 查询账户余额
     *
     * @throws Exception
     */
    @Test
    public void getBlance() throws Exception {
        String method = "open.balance.get";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String digest = MD5.getMD5(random + sn + loginId + secretCode); //签名摘要digest=md5(random+sn+loginId+secretCode)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);//result	 float 操作结果. 账户余额
    }

    /**
     * 额度转换记录
     *
     * @throws Exception
     */
    @Test
    public void querTransferBlance() throws Exception {
        String method = "open.balance.transfer.query";//请求路径
        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+secretKey)
        String loginId = "ybhganme123";//用户登录ID
        String agentId = "49910262";//代理ID
        String agentLoginId = "ybhjeff123";//代理登录ID
        String startTime = "2018-08-07 15:00:00"; //下注开始时间
        String endTime = "2018-08-10 18:00:00"; //下注截止时间
        Integer bizId = null; //业务单据ID
        String sortHint = "desc"; //排序方式:asc,升序;desc,降序
        Integer pageIndex = 1;//页索引，默认1
        Integer pageSize = 15;//页大小，默认15
        String etag = ""; //ETag,类似http协议的etag头的作用.(缓存加速)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);
        postDate.put("startTime", startTime);
        postDate.put("endTime", endTime);
        postDate.put("bizId", null);
        postDate.put("pageIndex", pageIndex.toString());
        postDate.put("pageSize", pageSize.toString());

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 额度转换
     *
     * @throws Exception
     */
    @Test
    public void transferBlance() throws Exception {
        String method = "open.balance.transfer";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String amount = "200";
        String bizId = "002"; //转账业务ID，由调用方提供
        String checkBizId = ""; //是否检查转账业务ID的唯一性. 1: 检查; 0: 不检查(默认)

        String digest = MD5.getMD5(random + sn + loginId + amount + secretCode); //签名摘要digest=md5(random+sn+loginId+amount+secretCode)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);
        postDate.put("amount", amount);
        postDate.put("bizId", bizId);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 注单统计结果
     *
     * @throws Exception
     */
    @Test
    public void orderSum() throws Exception {
        String method = "open.sn.order.sum";//请求路径
        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+secretKey)
        String moduleId = "1";//模块ID
        String gameId = "1";//游戏ID
        List<String> loginIds = null;//用户登录ID列表
        List<Integer> statuses = null;//注单状态列表
        String bySettle = "0"; //是否按结算时间查询;0,否(默认);1,是;
        String startTime = "2018-08-07 15:00:00"; // 开始时间
        String endTime = "2018-08-10 18:00:00"; // 截止时间


        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("bySettle", bySettle);
        postDate.put("startTime", startTime);
        postDate.put("endTime", endTime);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 会员登出
     *
     * @throws Exception
     */
    @Test
    public void logoutUser() throws Exception {
        String method = "open.user.logout";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String digest = MD5.getMD5(random + sn + loginId + secretCode); //签名摘要digest=md5(random+sn+loginId+secretCode)

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);

        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);//操作结果.1,成功;0,失败
    }

    /**
     * 获取视讯游戏链接地址
     *
     * @throws Exception
     */
    @Test
    public void videoGanmeUrl() throws Exception {
        String method = "open.video.game.url";//请求路径

        String agentPassword = "123456";//
        String secretCode = HashUtil.sha1Base64(agentPassword);//加密密码

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String loginId = "ybhganme123";//登录ID
        String digest = MD5.getMD5(random + sn + loginId + secretCode); //签名摘要digest=md5(random+sn+loginId+secretCode)

        String locale = "zh_CN";//语言
        String isMobileUrl = "0";//是否返回手机端地址. 1:是; 0:否(默认)
        String isHttpsUrl = "1";//是否返回https地址. 1,是(默认);0,否
        String returnUrl = "";//从游戏中退出时返回的地址，请使用UrlEncode进行编码; 示例: http%3a%2f%2fwww.bg567.com%2f

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("digest", digest);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);
        postDate.put("locale", locale);
        postDate.put("isMobileUrl", isMobileUrl);
        postDate.put("isHttpsUrl", isHttpsUrl);


        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 视讯注单
     *
     * @throws Exception
     */
    @Test
    public void videoReport() throws Exception {
        String method = "open.order.query";//请求路径

        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+secretKey)

        String agentLoginId = "ybhjeff123";//登录ID
        String startTime = "2018-08-07 00:00:00";
        String endTime = "2018-08-07 24:00:00";


        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("agentLoginId", agentLoginId);
        postDate.put("startTime", startTime);
        postDate.put("endTime", endTime);

        List<BgVideoBetLog> betList = new ArrayList<>();
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);

        Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(jsonMap.get("result").toString());
        String items = resultMap.get("items").toString();
        if(StringUtil.isNotEmpty(items)){
            betList = JSON.parseArray(items,BgVideoBetLog.class);
        }
        System.out.println(betList);
    }

    /**
     * 小费查询
     *
     * @throws Exception
     */
    @Test
    public void tipReport() throws Exception {
        String method = "open.video.user.tip.query";//请求路径

        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+secretKey)

        String agentLoginId = "ybhjeff123";//登录ID
        String startTime = "2018-08-07 00:00:00";
        String endTime = "2018-08-07 24:00:00";


        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        //postDate.put("agentLoginId", agentLoginId);
        postDate.put("startTime", startTime);
        postDate.put("endTime", endTime);

        List<BgVideoTipBetLog> betList = new ArrayList<>();
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);

        Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(jsonMap.get("result").toString());
        String items = resultMap.get("items").toString();
        if(StringUtil.isNotEmpty(items)){
            betList = JSON.parseArray(items,BgVideoTipBetLog.class);
        }
        System.out.println(betList);
    }

    /**
     * 捕鱼游戏链接地址
     *
     * @throws Exception
     */
    @Test
    public void fishGanmeUrl() throws Exception {
        String method = "open.game.bg.fishing.url";//请求路径
        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+loginId+secretKey)

        String loginId = "ybhganme123";//登录ID
        String isMobileUrl = "0";//是否返回手机端地址. 1:是; 0:否(默认)
        String isHttpsUrl = "1";//是否返回https地址. 1,是(默认);0,否
        String returnUrl = "";//从游戏中退出时返回的地址，请使用UrlEncode进行编码; 示例: http%3a%2f%2fwww.bg567.com%2f

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);


        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }

    /**
     * 捕鱼注单
     *
     * @throws Exception
     */
    @Test
    public void fishReport() throws Exception {
        String method = "open.order.bg.fishing.query";//请求路径
        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+secretKey)

        String agentLoginId = "ybhjeff123";//登录ID
        String startTime = "2018-08-07 00:00:00";
        String endTime = "2018-08-07 24:00:00";


        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("agentLoginId", agentLoginId);
        postDate.put("startTime", startTime);
        postDate.put("endTime", endTime);

        List<BgFishingBetLog> fishList = new ArrayList<>();
        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(jsonMap.get("result").toString());
        String items = resultMap.get("items").toString();
        if(StringUtil.isNotEmpty(items)){
            fishList = JSON.parseArray(items,BgFishingBetLog.class);
        }
        System.out.println(fishList);
    }


    /**
     * 电子游戏链接地址
     *
     * @throws Exception
     */
    @Test
    public void bgGanmeUrl() throws Exception {
        String method = "open.game.bg.egame.url";//请求路径
        String secretKey = "8153503006031672EF300005E5EF6AEF";//秘钥

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sn = "am00";//厅代码
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+loginId+secretKey)

        String loginId = "ybhganme123";//登录ID
        String isMobileUrl = "0";//是否返回手机端地址. 1:是; 0:否(默认)
        String isHttpsUrl = "1";//是否返回https地址. 1,是(默认);0,否
        String returnUrl = "";//从游戏中退出时返回的地址，请使用UrlEncode进行编码; 示例: http%3a%2f%2fwww.bg567.com%2f
        String orderFrom = "1";// 终端类别
        String locale = "zh_CN";//语言

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", "2.0");

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("loginId", loginId);


        String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, "http://am.bgvip55.com/cloud/api/", postDate);
        System.out.println(result);
    }
}
