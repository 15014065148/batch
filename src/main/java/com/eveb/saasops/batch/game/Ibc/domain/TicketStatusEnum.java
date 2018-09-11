package com.eveb.saasops.batch.game.Ibc.domain;

public enum TicketStatusEnum {

    Enum_Waiting("waiting", "等待"),
    Enum_Running("running", "进行中"),
    Enum_Draw("draw", "平局"),
    Enum_Reject("reject", "拒绝"),
    Enum_Refund("refund", "退钱"),
    Enum_Void("void", "取消"),
    Enum_Lose("lose", "输"),
    Enum_HalfWon("half won", "上半场赢"),
    Enum_HalfLose("half lose", "上半场输"),
    Enum_Won("won", "赢");

    private String key;
    private String value;

    TicketStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    TicketStatusEnum() {
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
