package com.eveb.saasops.batch.game.report.constants;

import java.math.BigDecimal;

public class GameCodeConstants {

    public static final String CONSTANT_CODE_DEVICETYPE="deviceType";

    public static final String CONSTANT_CODE_ORIGIN_PC="PC";

    public static final String CONSTANT_CODE_ORIGIN_PHONE="移动";

    public static final String CONSTANT_WIN="赢";

    public static final String CONSTANT_LOST="输";

    public static final String CONSTANT_TIE="和";

    public static final String CONSTANT_ISPAID="已结算";
    public static final String CONSTANT_NOPAID="未结算";
    public static final String CONSTANT_CANCELID="撤单";

    /***BBIN***/
    public static final String CONSTANT_BBIN_GAMETYPE_JACKPOT="5888";
    public static final String[] CONSTANT_BBIN_LIVE_RS=new String[]{"0"};
    public static final String[] CONSTANT_BBIN_SPORT_RS=new String[]{"N","X","S"};
    public static final String[] CONSTANT_BBIN_SLOT_RS=new String[]{"0","-77"};
    public static final String[] CONSTANT_BBIN_LOTTERY_RS=new String[]{"N"};


    /***188 ***/
    //確認單
    public static final Integer CONSTANT__T188_CHECK=401;
    //取消單
    public static final Integer CONSTANT__T188_CANCEL=403;
    //未結算
    public static final Integer CONSTANT__T188_PROCESS=431;
    //已結算
    public static final Integer CONSTANT__T188_FINSH=433;

    /**MG**/
    public static final String CONSTANT_CODE_MG_BETTYPE="mgsaspibet";
    public static final String CONSTANT_CODE_MG_REFUND="mgsapirefund";

    /**NT**/
    public static final String CONSTANT_CODE_NT_TRANTYPE_WIN="WIN";
    public static final String CONSTANT_CODE_NT_TRANSFTYPE_IN="TRANSF_IN";
    public static final String CONSTANT_CODE_NT_TRANSFTYPE_OUT="TRANSF_OUT";

    /**AGIN**/
    /***转账类别 (1=場景捕魚, 2=抽獎, 7= 捕魚王獎勵)**/
    public static final Integer CONSTANT_CODE_AGIN_TYPE=1;
    public static final Integer CONSTANT_CODE_AGIN_TYPE_JACKPOT=2;
    public static final String CONSTANT_CODE_DATATYPE_LIVE="BR";
    public static final String CONSTANT_CODE_DATATYPE_SLOT="EBR";
    public static final String CONSTANT_CODE_PLATFORMTYPE_YOPLAY="YOPLAY";
    public static final String CONSTANT_CODE_PLATFORMTYPE_XIN="XIN";

    /**OPUS**/
    public static final String CONSTANT_CODE_OPUS_LIVE_STATUS_WIN="Win";
    public static final String CONSTANT_CODE_OPUS_LIVE_STATUS_LOSS="Loss";
    public static final String CONSTANT_CODE_OPUS_LIVE_STATUS_TIP="Tip";
    public static final String CONSTANT__OPUS_TRANSACTION_NOT_FOUND="16";
    /**Postpond**/
    public static final String CONSTANT_CODE_OPUS_SPORT_WINLOST_STATUS_P="P";
    /**Cancel**/
    public static final String CONSTANT_CODE_OPUS_SPORT_WINLOST_STATUS_C="C";

    /***
     * 根据派彩返回输赢结果
     * @param payout
     * @return
     */
    public static String getWinLoss(BigDecimal payout) {
        if(payout==null)
        {
            return "";
        }
        if (payout.compareTo(BigDecimal.ZERO) > 0) {
            return GameCodeConstants.CONSTANT_WIN;
        } else if (payout.compareTo(BigDecimal.ZERO) < 0) {
            return GameCodeConstants.CONSTANT_LOST;
        } else {
            return GameCodeConstants.CONSTANT_TIE;
        }
    }
}
