package com.eveb.saasops.batch.game.t188.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class T188BetLog extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private String userCode;
    private Long wagerNo;
    /**String Wager Date/Time (yyyy/mm/dd hh:mm:ss)**/
    private Date dateCreated;
    private String memberIPAddress;
    /**Int 1: Euro 2: HK 3:Malay 4: Indo**/
    private Integer oddsTypeId;
    private BigDecimal odds;
    private BigDecimal handicap;
    private BigDecimal totalStakeF;
    private String betTypeName;
    private String periodName;
    private String sportName;
    private String competitionName;
    private String marketLineGroupName;
    private String eventName;
    private Integer eventId;
    private Date dateEvent;
    private Integer scoreHome;
    private Integer scoreAway;
    private String selectionName;
    private Integer betStatus;
    private Integer settlementStatus;
    private BigDecimal winLossAmount;
    private String voidReason;
}
