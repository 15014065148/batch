package com.eveb.saasops.batch.game.bbin.domain;

public enum MethodEnum {

    Enum_Slot("Slot","BetRecord"),
    Enum_Sport("Sport","BetRecord"),
    Enum_Live("Live","BetRecord"),
    Enum_Hunter("Hunter","WagersRecordBy30"),
    Enum_Hunter_Master("Hunter","WagersRecordBy38"),
    Enum_Lottery("Lottery","BetRecord"),
    Enum_Slot_Mdf("Slot_Mdf","BetRecordByModifiedDate3"),
    Enum_Sport_Mdf("Sport_Mdf","BetRecordByModifiedDate3"),
    Enum_Live_Mdf("Live_Mdf","BetRecordByModifiedDate3"),
    Enum_Hunter_Mdf("Hunter_Mdf","WagersRecordBy30"),
    Enum_Hunter_Master_Mdf("Hunter_Mdf","WagersRecordBy38"),
    Enum_Lottery_Mdf("Lottery_Mdf","BetRecordByModifiedDate3");

    private String key;
    private String value;

    MethodEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    MethodEnum() {
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
