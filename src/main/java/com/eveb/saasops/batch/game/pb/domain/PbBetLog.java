package com.eveb.saasops.batch.game.pb.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class PbBetLog extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private String wagerId;
    private String eventName;
    private String parentEventName;
    private String headToHead;
    private Date wagerDateFm;
    private Date eventDateFm;
    private Date settleDateFm;
    private String status;
    private String homeTeam;
    private String awayTeam;
    private String selection;
    private Integer handicap;
    private BigDecimal odds;
    private Integer oddsFormat;
    private Integer betType;
    private String league;
    private BigDecimal stake;
    private String sport;
    private String currencyCode;
    private String inplayScore;
    private Boolean inPlay;
    private String homePitcher;
    private String awayPitcher;
    private String homePitcherName;
    private String awayPitcherName;
    private Integer period;
    private String cancellationStatus;
    private String parlaySelections;
    private String category;
    private BigDecimal toWin;
    private BigDecimal toRisk;
    private String product;
    private BigDecimal parlayMixOdds;
    private String competitors;
    private String userCode;
    private String loginId;
    private BigDecimal winLoss;
    private BigDecimal commission;
    private String scores;
    private BigDecimal turnover;
    private String result;
}
