package com.eveb.saasops.batch.game;

public class GameBetLogModel<BetLogModel> {
    /**
     * API前缀
     **/
    private String apiPrefix;
    /**
     * 前缀
     **/
    private String sitePrefix;
    BetLogModel betLogModel;

    public String getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    public String getSitePrefix() {
        return sitePrefix;
    }

    public void setSitePrefix(String sitePrefix) {
        this.sitePrefix = sitePrefix;
    }

    public BetLogModel getBetLogModel() {
        return betLogModel;
    }

    public void setBetLogModel(BetLogModel betLogModel) {
        this.betLogModel = betLogModel;
    }

    public GameBetLogModel(BetLogModel betLogModel) {
        this.betLogModel = betLogModel;
    }

    public GameBetLogModel() {
    }
}
