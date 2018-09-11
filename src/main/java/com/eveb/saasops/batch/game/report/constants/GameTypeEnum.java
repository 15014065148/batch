package com.eveb.saasops.batch.game.report.constants;

/****
 * 游戏种类
 */
public enum GameTypeEnum{

    Enum_Slot("Slot","电子"),
    Enum_Sport("Sport","体育"),
    Enum_Live("Live","真人"),
    Enum_Lottery("Lottery","彩票"),
    Enum_SlotId("5","电子"),
    Enum_SportId("3","真人");
    private String key;
    private String value;

    GameTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    GameTypeEnum() {
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
