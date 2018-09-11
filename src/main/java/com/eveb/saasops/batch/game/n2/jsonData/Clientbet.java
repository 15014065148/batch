package com.eveb.saasops.batch.game.n2.jsonData;

import java.math.BigDecimal;
import java.util.List;

public class Clientbet {

    private List<Betdetail> betdetail;
    private BigDecimal bet_amount;
    private BigDecimal handle;
    private String login;
    private BigDecimal payout_amount;
    private BigDecimal hold;

    public List<Betdetail> getBetdetail() {
        return betdetail;
    }

    public void setBetdetail(List<Betdetail> betdetail) {
        this.betdetail = betdetail;
    }

    public BigDecimal getBet_amount() {
        return bet_amount;
    }

    public void setBet_amount(BigDecimal bet_amount) {
        this.bet_amount = bet_amount;
    }

    public BigDecimal getHandle() {
        return handle;
    }

    public void setHandle(BigDecimal handle) {
        this.handle = handle;
    }

    public BigDecimal getPayout_amount() {
        return payout_amount;
    }

    public void setPayout_amount(BigDecimal payout_amount) {
        this.payout_amount = payout_amount;
    }

    public BigDecimal getHold() {
        return hold;
    }

    public void setHold(BigDecimal hold) {
        this.hold = hold;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

}