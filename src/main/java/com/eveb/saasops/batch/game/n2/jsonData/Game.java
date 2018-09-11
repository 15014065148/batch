package com.eveb.saasops.batch.game.n2.jsonData;

import java.util.List;

public class Game {

    private List<Deal> deal;
    private String code;

    public void setDeal(List<Deal> deal) {
        this.deal = deal;
    }

    public List<Deal> getDeal() {
        return deal;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}