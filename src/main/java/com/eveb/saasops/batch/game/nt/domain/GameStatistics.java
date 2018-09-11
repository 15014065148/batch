package com.eveb.saasops.batch.game.nt.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement(name = "gameStatistics")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameStatistics implements Serializable {

    @XmlElement(name = "bet")
    private BigDecimal bet;

    @XmlElement(name = "currency")
    private String currency;

    @XmlElement(name = "gameId")
    private String gameId;

    @XmlElement(name = "partyId")
    private Integer partyId;

    @XmlElement(name = "round")
    private Integer round;

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "win")
    private BigDecimal win;

    public GameStatistics() {
    }

    public GameStatistics(
            BigDecimal bet,
            String currency,
            String gameId,
            Integer partyId,
            Integer round,
            String username,
            BigDecimal win) {
        this.bet = bet;
        this.currency = currency;
        this.gameId = gameId;
        this.partyId = partyId;
        this.round = round;
        this.username = username;
        this.win = win;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getWin() {
        return win;
    }

    public void setWin(BigDecimal win) {
        this.win = win;
    }
}
