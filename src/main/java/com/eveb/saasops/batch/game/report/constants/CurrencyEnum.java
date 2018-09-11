package com.eveb.saasops.batch.game.report.constants;

public enum CurrencyEnum {

    CNY_ENUM("CNY","RMB"),
    RMB_ENUM("RMB","RMB");

    private String key;
    private String value;

    CurrencyEnum(String key, String value) {
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

    public static String  getValue(String  key)
    {
        for (CurrencyEnum enums:CurrencyEnum.values())
        {
            if(key.toUpperCase().equals(enums.getKey()))
            {
                return enums.getValue();
            }
        }
        return key;
    }
}
