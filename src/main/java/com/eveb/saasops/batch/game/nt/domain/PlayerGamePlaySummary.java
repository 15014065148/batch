package com.eveb.saasops.batch.game.nt.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "getPlayerGamePlaySummaryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerGamePlaySummary implements Serializable {

    @XmlElement(name = "summary")
    private List<GamePlaySummary> gamePlaySummary;

    public PlayerGamePlaySummary() {
    }

    public PlayerGamePlaySummary(List<GamePlaySummary> gamePlaySummary) {
        this.gamePlaySummary = gamePlaySummary;
    }

    public List<GamePlaySummary> getGamePlaySummary() {
        return gamePlaySummary;
    }

    public void setGamePlaySummary(List<GamePlaySummary> gamePlaySummary) {
        this.gamePlaySummary = gamePlaySummary;
    }
}
