package com.eveb.saasops.batch.game.report.constants;

/***
 * 游戏平台
 * key 为平台的code 应用于ElasticSearch的index
 */
public enum PlatFromEnum {

    Enum_PT("PT", "PT"),
    Enum_NT("NT", "NT"),
    Enum_BBIN("BBIN", "BBIN"),
    Enum_AGIN("AGIN", "AGIN"),
    Enum_PT2("PT2", "PT2"),
    Enum_PNG("PNG", "PNG"),
    Enum_IBC("IBC", "IBC"),
    Enum_T188("T188", "T188"),
    Enum_EG("EG", "EG"),
    Enum_OPUSCA("OPUSCA", "OPUSCA"),
    Enum_OPUSSB("OPUSSB", "OPUSSB"),
    Enum_PB("PB", "PB"),
    Enum_MG("MG", "MG"),
    Enum_MG2("MG2", "MG2"),
    Enum_AB("AB", "AB"),
    Enum_BNG("BNG", "BNG"),
    Enum_N2("N2","N2"),
    Enum_TTG("TTG","TTG"),
    Enum_HG("HG", "HG"),
    Enum_GNS("GNS","GNS"),
    Enum_OG("OG","OG"),
    Enum_VR("VR","VR"),
    Enum_FG("FG", "FG"),
    Enum_PGCB("PG", "PG"),
    Enum_GG("GG", "GG"),
    Enum_BG("BG", "BG");

    private String key;
    private String value;

    PlatFromEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    PlatFromEnum() {
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
