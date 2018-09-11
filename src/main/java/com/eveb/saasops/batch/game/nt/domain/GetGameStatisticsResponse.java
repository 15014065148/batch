package com.eveb.saasops.batch.game.nt.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "getGameStatisticsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetGameStatisticsResponse {

    @XmlElement(name = "gameStatistics")
    private List<GameStatistics> GameStatistics;

    public GetGameStatisticsResponse() {
    }

    public GetGameStatisticsResponse(List<com.eveb.saasops.batch.game.nt.domain.GameStatistics> gameStatistics) {
        GameStatistics = gameStatistics;
    }

    public List<com.eveb.saasops.batch.game.nt.domain.GameStatistics> getGameStatistics() {
        return GameStatistics;
    }

    public void setGameStatistics(List<com.eveb.saasops.batch.game.nt.domain.GameStatistics> gameStatistics) {
        GameStatistics = gameStatistics;
    }
}
