package com.eveb.saasops.batch.sys.constants;

import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;

public class ElasticSearchConstant {

    public static final int SEARCH_COUNT = 10000;//最大查询数量

    /*** Report Index***/
    public static final String REPORT_INDEX = "report";
    public static final String REPORT_TYPE = "rpt_bet_rcd";

    /***PT Type***/
    public static final String PT_INDEX = PlatFromEnum.Enum_PT.getKey().toLowerCase();
    public static final String PT_TYPE = "log_bet_rcd_pt";

    /***N2 Type***/
    public static final String N2_INDEX = PlatFromEnum.Enum_N2.getKey().toLowerCase();
    public static final String N2_TYPE = "log_bet_rcd_n2";

    /***PT2 Type***/
    public static final String PT2_INDEX = PlatFromEnum.Enum_PT2.getKey().toLowerCase();
    public static final String PT2_TYPE = "log_bet_rcd_pt2";

    /***BBIN Type***/
    public static final String BBIN_INDEX_LIVE = "bbin_live";
    public static final String BBIN_TYPE_LIVE = "log_bet_rcd_bbin_live";
    public static final String BBIN_INDEX_LIVE_MDF = "bbin_live_mdf";
    public static final String BBIN_TYPE_LIVE_MDF = "log_bet_rcd_bbin_live_mdf";

    public static final String BBIN_INDEX_SLOT = "bbin_slot";
    public static final String BBIN_TYPE_SLOT = "log_bet_rcd_bbin_slot";
    public static final String BBIN_INDEX_SLOT_MDF = "bbin_slot_mdf";
    public static final String BBIN_TYPE_SLOT_MDF = "log_bet_rcd_bbin_slot_mdf";

    public static final String BBIN_INDEX_SPORT = "bbin_sport";
    public static final String BBIN_TYPE_SPORT = "log_bet_rcd_bbin_sport";
    public static final String BBIN_INDEX_SPORT_MDF = "bbin_sport_mdf";
    public static final String BBIN_TYPE_SPORT_MDF = "log_bet_rcd_bbin_sport_mdf";

    public static final String BBIN_INDEX_LOTTERY = "bbin_lottery";
    public static final String BBIN_TYPE_LOTTERY = "log_bet_rcd_bbin_lottery";
    public static final String BBIN_INDEX_LOTTERY_MDF = "bbin_lottery_mdf";
    public static final String BBIN_TYPE_LOTTERY_MDF = "log_bet_rcd_bbin_lottery_mdf";

    public static final String BBIN_INDEX_TIP = "bbin_tip";
    public static final String BBIN_TYPE_TIP = "log_bet_rcd_bbin_tip";
    public static final String BBIN_INDEX_TIP_MDF = "bbin_tip_mdf";
    public static final String BBIN_TYPE_TIP_MDF = "log_bet_rcd_bbin_tip_mdf";

    public static final String BBIN_INDEX_HUNTER = "bbin_hunter";
    public static final String BBIN_TYPE_HUNTER = "log_bet_rcd_bbin_hunter";
    public static final String BBIN_INDEX_HUNTER_MDF = "bbin_hunter_mdf";
    public static final String BBIN_TYPE_HUNTER_MDF = "log_bet_rcd_bbin_hunter_mdf";

    /***AGIN Type***/
    public static final String AGIN_INDEX = "agin";
    //存放电子和真人数据
    public static final String AGIN_TYPE = "log_bet_rcd_agin";
    public static final String AGIN_INDEX_MDF = "agin_mdf";
    public static final String AGIN_TYPE_MDF = "log_bet_rcd_agin_mdf";

    public static final String AGIN_INDEX_LIVECARDRESULT = "agin_lcr";
    public static final String AGIN_TYPE_LIVECARDRESULT = "log_agin_lcr";

    public static final String AGIN_INDEX_SLOT = "agin_slot";
    public static final String AGIN_TYPE_SLOT = "log_bet_rcd_agin_slot";
    public static final String AGIN_INDEX_SLOT_MDF = "agin_slot_mdf";
    public static final String AGIN_TYPE_SLOT_MDF = "log_bet_rcd_agin_slot_mdf";

    public static final String AGIN_INDEX_HUNTER = "agin_hunter";
    public static final String AGIN_TYPE_HUNTER = "log_bet_rcd_agin_hunter";
    public static final String AGIN_INDEX_HUNTER_MDF = "agin_hunter_mdf";
    public static final String AGIN_TYPE_HUNTER_MDF = "log_bet_rcd_agin_hunter_mdf";

    /***MG Type***/
    public static final String MG_INDEX = PlatFromEnum.Enum_MG.getKey().toLowerCase();
    public static final String MG_TYPE = "log_bet_rcd_mg";
    /***MG2 Type***/
    public static final String MG2_INDEX = PlatFromEnum.Enum_MG2.getKey().toLowerCase();
    public static final String MG2_TYPE = "log_bet_rcd_mg2";
    /***NT Type***/
    public static final String NT_INDEX = PlatFromEnum.Enum_NT.getKey().toLowerCase();
    public static final String NT_TYPE = "log_bet_rcd_nt";

    /***PNG ***/
    public static final String PNG_INDEX = PlatFromEnum.Enum_PNG.getKey().toLowerCase();
    public static final String PNG_TYPE = "log_bet_rcd_png";

    /***188 Type***/
    public static final String T188_INDEX = PlatFromEnum.Enum_T188.getKey().toLowerCase();
    public static final String T188_TYPE = "log_bet_rcd_t188";

    /***IBC Type***/
    public static final String IBC_INDEX = PlatFromEnum.Enum_IBC.getKey().toLowerCase();
    public static final String IBC_TYPE = "log_bet_rcd_ibc";

    /***EV Type***/
    public static final String EG_INDEX = PlatFromEnum.Enum_EG.getKey().toLowerCase();
    public static final String EG_TYPE = "log_bet_rcd_eg";

    /***Opus Type***/
    public static final String OPUS_INDEX_LIVE = PlatFromEnum.Enum_OPUSCA.getKey().toLowerCase();
    public static final String OPUS_TYPE_LIVE = "log_bet_rcd_opus_live";
    public static final String OPUS_INDEX_SPORT = PlatFromEnum.Enum_OPUSSB.getKey().toLowerCase();
    public static final String OPUS_TYPE_SPORT = "log_bet_rcd_opus_sport";

    /***PT Type***/
    public static final String PB_INDEX = PlatFromEnum.Enum_PB.getKey().toLowerCase();
    public static final String PB_TYPE = "log_bet_rcd_pb";


    public static final String BNG_INDEX = PlatFromEnum.Enum_BNG.getKey().toLowerCase();

    public static final String BNG_TYPE = "log_bet_rcd_bng";
    public static final String AB_INDEX = PlatFromEnum.Enum_AB.getKey().toLowerCase();
    public static final String AB_TYPE = "log_bet_rcd_ab";

    /***og Type***/
    public static final String OG_INDEX= PlatFromEnum.Enum_OG.getKey().toLowerCase();
    public static final String OG_TYPE="log_bet_rcd_og";
    /***ttg Type***/
    public static final String TTG_INDEX= PlatFromEnum.Enum_TTG.getKey().toLowerCase();
    public static final String TTG_TYPE="log_bet_rcd_ttg";

    /***hg Type***/
    public static final String HG_INDEX = PlatFromEnum.Enum_HG.getKey().toLowerCase();
    public static final String HG_TYPE = "log_bet_rcd_hg";

    /***GNS Type***/
    public static final String GNS_INDEX= PlatFromEnum.Enum_GNS.getKey().toLowerCase();
    public static final String GNS_TYPE="log_bet_rcd_gns";

    /***VR Type***/
    public static final String VR_INDEX= PlatFromEnum.Enum_VR.getKey().toLowerCase();
    public static final String VR_TYPE="log_bet_rcd_vr";

    /***fg Type***/
    public static final String FG_INDEX = PlatFromEnum.Enum_FG.getKey().toLowerCase();
    public static final String FG_TYPE = "log_bet_rcd_fg";

    /***pgcb Type***/
    public static final String PGCB_INDEX = PlatFromEnum.Enum_PGCB.getKey().toLowerCase();
    public static final String PGCB_TYPE_LOTTERY = "log_bet_rcd_pgcb_lottery";
    public static final String PGCB_TYPE_GIFT = "log_bet_rcd_pgcb_gift";
    public static final String PGCB_TYPE_CASINO = "log_bet_rcd_pgcb_casino";

    /***fg Type***/
    public static final String GG_INDEX = PlatFromEnum.Enum_GG.getKey().toLowerCase();
    public static final String GG_TYPE = "log_bet_rcd_gg";

    /***bgcb Type***/
    public static final String BG_INDEX = PlatFromEnum.Enum_BG.getKey().toLowerCase();
    public static final String BG_TYPE_VIDEO = "log_bet_rcd_bg_video";
    public static final String BG_TYPE_TIP = "log_bet_rcd_bg_tip";
    public static final String BG_TYPE_FISH = "log_bet_rcd_bg_fish";
}
