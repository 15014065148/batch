package com.eveb.saasops.batch.sys.constants;


public class DepotConstants {

    public static final String DEPOT_BBIN = "BBIN";
    public static final String DEPOT_AGIN = "AGIN";
    public static final String DEPOT_TTG = "TTG";
    public static final String DEPOT_PP = "PP";
    public static final String DEPOT_MG = "MG";
    public static final String DEPOT_PT = "PT";
    public static final String DEPOT_KG = "KG";
    public static final String DEPOT_AB = "AB";
    public static final String DEPOT_OPUS = "OPUS";
    public static final String DEPOT_188 = "188";
    public static final String DEPOT_IBC = "IBC";

    public static final String BBIN_API_CHECKUSRBALANCE = "CheckUsrBalance";//查询BBIN 会员余额

    public static final String AGIN_M_DOBUSINESS = "doBusiness.do?";
    public static final String AGIN_FUN_GETBALANCE = "gb";//余额查找
    public static final String AGIN_DESCODE_KEY = "desCode";

    public interface CurTpye {
        // 币种
        String curCny = "CNY";// 人民币
        String curKrw = "KRW";// 韩元
        String curMyr = "MYR";// 马来西亚币
        String curUsd = "USD";// 美元
        String curJpy = "JPY";// 日元
        String curThb = "THB";// 泰铢
        String curBtc = "BTC";// 比特币
        String curIdr = "IDR";// 印尼盾
        String curVnd = "VND";// 越南盾
        String curEur = "EUR";// 欧元
        String curInr = "INR";// 印度卢比
    }

    public interface Actype {
        int trueAccount = 1;// 真钱账户
        int testAccount = 0;// 试玩账号
    }

    public interface OddTpye {
        // 人民币下注范围
        String OddA = "A";// 20-50000
        String OddB = "B";// 50-5000
        String OddC = "C";// 20-10000
        String OddD = "D";// 200-20000
        String OddE = "E";// 300-30000
        String OddF = "F";// 400-40000
        String OddG = "G";// 500-50000
        String OddH = "H";// 1000-100000
        String OddI = "I";// 2000-200000
    }

    public interface PTJsonKey {
        String ENTITY_KEY_NAME = "X_ENTITY_KEY";
    }

    public interface PTMod{
        String info="player/info";//会员信息 余额
    }
}
