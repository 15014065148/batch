package com.eveb.saasops.batch.game.nt.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "summary")
@XmlAccessorType(XmlAccessType.FIELD)
public class GamePlaySummary implements java.io.Serializable {

    @XmlElement(name = "betPerGame")
    private BigDecimal betPerGame;

    @XmlElement(name = "gameName")
    private String gameName;

    @XmlElement(name = "handle")
    private BigDecimal handle;

    @XmlElement(name = "hold")
    private BigDecimal hold;

    @XmlElement(name = "holdPercent")
    private BigDecimal holdPercent;

    @XmlElement(name = "numberOfBets")
    private Integer numberOfBets;

    @XmlElement(name = "numberOfGames")
    private Integer numberOfGames;

    @XmlElement(name = "platformName")
    private String platformName;

    public GamePlaySummary() {
    }

    public GamePlaySummary(BigDecimal betPerGame, String gameName, BigDecimal handle, BigDecimal hold, BigDecimal holdPercent, Integer numberOfBets, Integer numberOfGames, String platformName) {
        this.betPerGame = betPerGame;
        this.gameName = gameName;
        this.handle = handle;
        this.hold = hold;
        this.holdPercent = holdPercent;
        this.numberOfBets = numberOfBets;
        this.numberOfGames = numberOfGames;
        this.platformName = platformName;
    }

    public BigDecimal getBetPerGame() {
        return betPerGame;
    }

    public void setBetPerGame(BigDecimal betPerGame) {
        this.betPerGame = betPerGame;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public BigDecimal getHandle() {
        return handle;
    }

    public void setHandle(BigDecimal handle) {
        this.handle = handle;
    }

    public BigDecimal getHold() {
        return hold;
    }

    public void setHold(BigDecimal hold) {
        this.hold = hold;
    }

    public BigDecimal getHoldPercent() {
        return holdPercent;
    }

    public void setHoldPercent(BigDecimal holdPercent) {
        this.holdPercent = holdPercent;
    }

    public Integer getNumberOfBets() {
        return numberOfBets;
    }

    public void setNumberOfBets(Integer numberOfBets) {
        this.numberOfBets = numberOfBets;
    }

    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(Integer numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
