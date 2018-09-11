package com.eveb.saasops.batch.game.report.constants;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 14:28 2017/12/4
 **/
public enum CodeTypeEnum {

    Enum_Wager_Code("wager_code","玩法类型"),
    Enum_Game_Code("game_code","游戏分类"),
    Enum_Result_Code("result_code","注单结果"),
    Enum_Status_Code("status_code","注单状态"),
    Enum_Origin_Code("origin_code","设备类型"),
    Enum_Odds_Code("odds_code","盘口"),
    Enum_Game_Hunter_Code("game_hunter_code","捕鱼游戏分类");

    private String key;
    private String value;

    CodeTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
