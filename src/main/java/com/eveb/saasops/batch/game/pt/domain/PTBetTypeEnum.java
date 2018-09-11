package com.eveb.saasops.batch.game.pt.domain;

public enum PTBetTypeEnum {

    Enum_Card_Games("Card Games", "电子"),
    Enum_Fixed_Odds("Fixed Odds", "电子"),
    Enum_GAMT_SLOT("GAMT:SLOT", "电子"),
    Enum_GTS_Games("GTS Games", "电子"),
    Enum_Keno("Keno", "电子"),
    Enum_Live_Games("Live Games", "真人"),
    Enum_Mahjong("Mahjong", "电子"),
    Enum_Mini_games("Mini games", "电子"),
    Enum_None("None", "电子"),
    Enum_Pachinko("Pachinko", "电子"),
    Enum_PlaytechTV("PlaytechTV", "电子"),
    Enum_Progressive_Slot_Machines("Progressive Slot Machines", "电子"),
    Enum_Progressive_Video_Pokers("Progressive Video Pokers", "电子"),
    Enum_Scratchcards("Scratchcards", "电子"),
    Enum_Sidegames("Sidegames", "电子"),
    Enum_Slot_Games("Slot Games", "电子"),
    Enum_Slot_Machines("Slot Machines", "电子"),
    Enum_Table_Games("Table Games", "电子"),
    Enum_Tournaments("Tournaments", "电子"),
    Enum_VF_Games("VF Games", "电子"),
    Enum_VIP_Live_Games("VIP Live Games", "电子"),
    Enum_Video_Pokers("Video Pokers", "电子");

    private String key;
    private String value;

    PTBetTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    PTBetTypeEnum() {
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
