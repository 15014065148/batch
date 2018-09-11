package com.eveb.saasops.batch.game.bg.util;


/**
 * 请求返回参数
 * 2018-08-08 Jeff
 */
public class BgParamerUtil {
    public static final String BG_URL_JSONRPC = "2.0";
    /**
     * 视讯注单接口路径
     **/
    public static final String BG_METHOD_URL_VIDEO = "open.order.query";
    /**
     * 视讯小费注单接口路径
     **/
    public static final String BG_METHOD_URL_TIP = "open.video.user.tip.query";
    /**
     * 彩票注单接口路径
     **/
    public static final String BG_METHOD_URL_LOTTERY = "open.order.bg.nlottery.query";
    /**
     * 捕鱼注单接口路径
     **/
    public static final String BG_METHOD_URL_FISH = "open.order.bg.fishing.query";

    /**
     * 电游注单接口路径
     **/
    public static final String BG_METHOD_URL_EGANME = "open.order.bg.egame.query";
    /**
     * BG默认币种
     */
    public static final String CONSTANT_BG_CURRENCY = "CNY";

    /**
     * BG捕鱼游戏ID
     */
    public static final String CONSTANT_BG_GAMEID = "hunter";

}
