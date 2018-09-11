package com.eveb.saasops.batch.game.og.domain;

public enum OGBetTypeEnum {
    Enum_Real_Games("Real Games", "真人");

    private String key;
    private String value;

    OGBetTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    OGBetTypeEnum() {
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
