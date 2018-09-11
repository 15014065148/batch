package com.eveb.saasops.batch.sys.constants;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 全局定义基础数据
 */
public class ApplicationConstant {


    /**
     * 任务重试失败次数
     **/
    public static final int CONSTANT_RETRYLIMIT = 5;
    /**
     * 任务失败等待重试时间 秒
     **/
    public static final int CONSTANT_WAITSECON = 1000;
    /**
     * 最大提交数量
     **/
    public static final int CONSTANT_COMMINTMAX = 1000;
    /**
     * 执行失败得状态码
     **/
    public static final int CONSTANT_JOBEXECUTE_FAIL = 500;
    /**
     * 执行成功得状态码
     **/
    public static final int CONSTANT_JOBEXCUTE_SUCCEED = 200;
    /**
     * 处理中状态码
     **/
    public static final int CONSTANT_JOBEXECUTE_PRO = 700;
    /**
     * ES存储的时间格式
     **/
    public static final String CONSTANT_DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 时间格式类型
     **/
    public static class DateFormat {
        public static final SimpleDateFormat SDF_YYYYMMddTHHmmssSSSZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        public static final SimpleDateFormat SDF_YYYYMMdd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public static final SimpleDateFormat SDF_YYYYMMddHHmmss = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        public static final SimpleDateFormat SDF_YYYYMMdd = new SimpleDateFormat("yyyy-MM-dd");
    }

    /***包网还是API客户，0：包网，1：API***/
    public static final Integer CONSTANT_ISAPI_FALSE = 0;

    /**
     * 游戏类型 Map<String,Map>
     **/
    public static Map CONSTANT_GAMECODE_MAP = Collections.synchronizedMap(new HashMap<>());

    /**
     * 抓取数据的默认配置
     **/
    public static final int CONSTANT_INTEVAL = 5;
    /**
     * 延迟获取记录,投注后游戏方需要过了一定的时间才能提供数据
     **/
    public static final int CONSTANT_DELAY = 5;
    /**
     * 异常信息，针对体育
     **/
    public static final String CONSTANT_ERR_MSG = "有未完成注单，需重试!";
    /**
     * 北京时间与美东时间差距，AG、BBIN固定
     **/
    public static final int CONSTANT_TIMEZONE_INTEVAL = -12;

    /**
     * BBIN 抓取注单配置
     **/
    /**
     * 获取异动注单区间，单位分钟
     **/
    public static final int CONSTANT_BBIN_INTEVAL = 5;
    public static final int CONSTANT_BBIN_MODIFIED_INTEVAL = 5;
    public static final int Constant_Inteval_24 = 24 * 60;

    public static final String CONSTANT_BBIN_LOTTERY_LT = "LT";
    public static final String CONSTANT_BBIN_LOTTERY_OTHER = "OTHER";

    /**
     * AGIN 抓取注单配置
     **/
    /***捕鱼注单延迟获取时间，捕鱼注单文件生成较晚***/
    public static final int CONSTANT_AGIN_HUNTER_DELAY = 20;
    /**
     * 延迟配置
     */
    public static final int CONSTANT_AGIN_LIVE_DELAY = 10;

    /**
     * PT 抓取注单配置
     **/
    /**
     * 建议不要呼叫实时数据（因为数据大概有10分钟延迟）,每次获取记录的时间间隔10分钟或10 分钟以上，即，现在获取10分钟之前的记录。避免频繁呼叫PT服务器被误认为是而已攻击而封锁 您的IP
     */
    public static final int CONSTANT_PT_DELAY = 10;
    /***PT 呼叫区间必须在半个小时之内，大于半个小时系统会报错。建议每次呼叫区间在 10-30分钟，根 据您的数据量调整***/
    public static final int CONSTANT_PT_INTEVAL = 30;


    /**
     * PNG 抓取注单配置
     **/
    /***PNG 呼叫区间必须在半个小时之内，大于半个小时系统会报错。建议每次呼叫区间在 10-30分钟，根 据您的数据量调整***/
    public static final int CONSTANT_PNG_INTEVAL = 30;


    /**
     * MG 抓取注单配置
     **/
    /***呼叫区间必须在一个小时之内，大于一个小时系统会报错。***/
    public static final int CONSTANT_MG_INTEVAL = 60;

    /**
     * Mg2 抓注单
     */
    public static final int CONSTANT_MG2_INTEVAL = 60;


    /**
     * AB 抓注单
     * 调用次数限制: 8 次/10 分钟
     * 呼叫区间必须在一个小时之内，大于一个小时系统会报错
     */
    public static final int CONSTANT_AB_INTEVAL = 60;

    /**
     * AB 电子
     */
    public static final int CONSTANT_AB_EGAME_INTEVAL = 60;

    /**
     * N2
     * 日期值不能为未来日期 时间必须是当前时间的15分钟之前
     */
    public static final int CONSTANT_N2_EGAME_DELAY = 15;
    public static final int CONSTANT_N2_EGAME_INTEVAL = 1440;
    /**
     * AB 异动单
     * 调用次数限制: 1 小时/次
     */
    public static final int CONSTANT_AB_MODIFIED_INTEVAL = 24 * 60;

    /**
     * BNG
     */
    public static final int CONSTANT_BNG_INTEVAL = 60;
    /**
     * 减去UTC时间，然后延迟五分钟
     **/
    public static final int CONSTANT_BNG_DELAY = 8 * 60 + 5;

    /**
     * IBC 抓取注单配置
     **/
    /*** **StartTime 开始时间和 EndTime 结束时间只允许 12 小时最多持续时间 ***/
    public static final int CONSTANT_IBC_INTEVAL = 12 * 60;
    /**
     * UTC-4 美东时间 然后延迟五分钟
     **/
    public static final int CONSTANT_IBC_DELAY = 12 * 60 + 5;


    /**
     * PT2 抓取注单配置
     */
    public static final int CONSTANT_PT2_INTEVAL = 30;
    /**
     * 减去UTC时间，然后延迟五分钟
     **/
    public static final int CONSTANT_PT2_DELAY = 8 * 60 + 5;
    public static final int CONSTANT_PT2_DATA_MAX_COUNT = 100;
    /**
     * OG 平台最多返回数据量
     */
    /***  一次最大返回数据量***/
    public static final int CONSTANT_OG_DATACOUNTS = 300;
    /**
     * OG 平台访问频率限制
     */
    /***  一分钟之内最多只能访问3次***/
    public static final int CONSTANT_OG_CALLON = 10000;

    /**
     * NT 抓取注单配置
     */
    /***  获取注单配置***/
    public static final int CONSTANT_NT_INTEVAL = 30;


    /**
     * 188 抓取注单配置
     */
    /*** 188 获取注单配置 Return the wager (bet) information for one member or all members by Date Range.The date range should be within 1 day.***/
    public static final int CONSTANT_T188_INTEVAL = 24 * 60;

    /**
     * OPUS 抓取注单配置
     */
    /***  获取注单配置***/
    public static final int CONSTANT_OPUS_DELAY = 5;
    public static final int CONSTANT_OPUS_INTEVAL = 15;

    /***
     * PB 抓取注单配置
     */
    /***使用美东时间***/
    public static final int CONSTANT_PB_DELAY = 12 * 60 + 5;
    public static final int CONSTANT_PB_INTEVAL = 24 * 60;

    /**
     * TTG 抓取注单配置
     **/
    /**
     * 建议不要呼叫实时数据（因为数据大概有10分钟延迟）,每次获取记录的时间间隔10分钟或10 分钟以上，即，现在获取10分钟之前的记录。避免频繁呼叫PT服务器被误认为是而已攻击而封锁 您的IP
     */
    public static final int CONSTANT_TTG_DELAY = 10;
    /***PT 呼叫区间必须在半个小时之内，大于半个小时系统会报错。建议每次呼叫区间在 10-30分钟，根 据您的数据量调整***/
    public static final int CONSTANT_TTG_INTEVAL = 30;
    /***
     * GNS 电子抓取注单配置
     */
    public static final BigDecimal CONSTANT_GNS_AMOUNT_UNIT = new BigDecimal(100);
    public static final int CONSTANT_GNS_DELAY = 8 * 60 + 5;
    public static final int CONSTANT_GNS_INTEVAL = 24 * 60;
    public static final Integer CONSTANT_GNS_LIMIT = 500;
    public static final Integer CONSTANT_GNS_START_INDEX = 0;
    /***
     * VR 彩票抓取注单配置
     */
    public static final int CONSTANT_VR_INTEVAL = 12 * 60;
    public static final String CONSTANT_VR_VERSION = "1.0";

    /***呼叫区间必须在一个小时之内，大于一个小时系统会报错。***/
    public static final int CONSTANT_HG_INTEVAL = 60;

    /***FG 抓取注单配置***/
    public static final String CONSTANT_FG_CURRENCY = "CNY";
    public static final int CONSTANT_FG_INTEVAL = 5;

    /***PGCB 抓取注单配置***/
    public static final String CONSTANT_FGCB_CURRENCY = "CNY";
    public static final int CONSTANT_PGCB_INTEVAL = 5;


    /***GG 抓取注单配置***/
    public static final String GG_METHOD_BET = "br";//获取三天内 10分钟 数据
    public static final String GG_METHOD_BET_HISTORY = "hbr";//获取三天前数据
    public static final int CONSTANT_GG_INTEVAL = 10;


    /***BG 抓取注单配置***/
    public static final int CONSTANT_BG_INTEVAL = 10;

    /****
     * 根据平台、代码类型、分类、代码获取代码内容
     * @param platForm
     * @param codeType
     * @param group
     * @param code
     * @return
     */
    public static String getCodeContent(String platForm, String codeType, String group, String code) {
        Object object = ((Map) CONSTANT_GAMECODE_MAP.get(platForm)).get(codeType);
        object = object == null ? null : ((Map) object).get(group);
        if (object == null)
            return "";
        List<Map> maps = (List<Map>) object;
        for (Map m : maps) {
            object = m.get(code);
            if (object != null)
                break;
        }
        return object == null ? "" : object.toString();
    }

}
