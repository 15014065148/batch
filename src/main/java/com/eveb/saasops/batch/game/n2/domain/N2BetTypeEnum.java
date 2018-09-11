package com.eveb.saasops.batch.game.n2.domain;

public enum N2BetTypeEnum {
    Enum_50002("50002", "轮盘"),
    Enum_51002("51002", "电子轮盘 "),
    Enum_52002("52002", "电子轮盘手动"),
    Enum_60001("60001", "骰宝 "),
    Enum_61001("61001", "电子骰宝 "),
    Enum_62001("62001", "电子骰宝手动"),
    Enum_90091("90091", "传统百家乐"),
    Enum_91091("91091", "电子传统百家乐"),
    Enum_90092("90092", "免佣百家乐"),
    Enum_91092("91092", "电子免佣百家乐"),
    Enum_bank("bank", "投庄金额"),
    Enum_player("player", "投闲金额 "),
    Enum_tie("tie", "投和金额"),
    Enum_bankdp("bankdp", "投庄对子金额"),
    Enum_playerdp("playerdp", "投闲对子金额"),
    Enum_big("big", "投大金额"),
    Enum_small("small", "投小金额"),
    Enum_perfectdp("perfectdp", "投完美对子金额"),
    Enum_eitherdp("eitherdp", "投任何对子金额"),
    Enum_bankdpc("bankdpc", "投庄对子组合金额"),
    Enum_playerdpc("playerdpc", "投闲对子组合金额"),
    Enum_bankd7("bankd7", "投龙7金额"),
    Enum_playerp8("playerp8", "投熊貓8金额"),
    Enum_bankdb("bankdb", "投庄龙宝金额"),
    Enum_playerdb("playerdb", "投闲龙宝金额"),
    Enum_bankcase("bankcase", "投庄列牌胜金额 "),
    Enum_playercase("playercase", "传统百家乐"),
    Enum_bank0("bank0", "投庄总合为 0"),
    Enum_bank1("bank1", "投庄总合为 1"),
    Enum_bank2("bank2", "投庄总合为 2"),
    Enum_bank3("bank3", "投庄总合为 3"),
    Enum_bank4("bank4", "投庄总合为 4"),
    Enum_bank5("bank5", "投庄总合为 5"),
    Enum_bank6("bank6", "投庄总合为 6"),
    Enum_bank7("bank7", "投庄总合为 7"),
    Enum_bank8("bank8", "投庄总合为 8"),
    Enum_bank9("bank9", "投庄总合为 9"),
    Enum_player0("player0", "投闲总合为 0"),
    Enum_player1("player1", "投闲总合为 1"),
    Enum_player2("player2", "投闲总合为 2"),
    Enum_player3("player3", "投闲总合为 3"),
    Enum_player4("player4", "投闲总合为 4"),
    Enum_player5("player5", "投闲总合为 5"),
    Enum_player6("player6", "投闲总合为 6"),
    Enum_player7("player7", "投闲总合为 7"),
    Enum_player8("player8", "投闲总合为 8"),
    Enum_player9("player9", "投闲总合为 9");


    private String key;
    private String value;

    N2BetTypeEnum(String key, String value) {
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
