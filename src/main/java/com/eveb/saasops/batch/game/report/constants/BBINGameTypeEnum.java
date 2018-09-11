package com.eveb.saasops.batch.game.report.constants;

public enum BBINGameTypeEnum {

    Enum_Sport("1","Sport"),
    Enum_Live("3","Live"),
    Enum_Slot("5","Slot"),
    Enum_Hunter("8","Hunter"),
    Enum_Lottery("12","Lottery"),
    Enum_3DSlot("15","3DSlot"),
    Enum_Tip("99","Tip");

    private String key;
    private String value;

    BBINGameTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    BBINGameTypeEnum() {
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
