package com.eveb.saasops.batch.game.Ibc.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class IbcBetLog extends BetLog {

    /**
     * 前缀
     **/
    private String sitePrefix;
    private String apiPrefix;
    /**
     * 交易记录 ID
     **/
    private Long transId;
    /**
     * 球员的名字
     **/
    private String playerName;
    /**
     * ISO 8601 格式
     **/
    private Date transactionTime;
    /**
     * 游戏比赛 ID
     **/
    private BigInteger matchId;
    /**
     * 联赛 ID
     **/
    private Integer leagueId;
    /**
     * 联赛名称
     **/
    private String leagueName;
    /**
     * 请参阅运动类型表
     **/
    private String sportType;
    /**
     * 客场团队 ID
     **/
    private BigInteger awayId;
    /**
     * 客队名称
     **/
    private String awayIdName;
    /**
     * 首页团队 ID
     **/
    private BigInteger homeId;
    /**
     * 主场球队名称
     **/
    private String homeIdName;
    /**
     * ISO 8601 格式
     **/
    private Date matchDateTime;
    /**
     * Parlay 参考编号
     **/
    private BigInteger parlayRefNo;
    /**
     * 请参阅打赌类型表
     **/
    private Integer betType;
    /**
     * 投注站团队
     **/
    private String betTeam;
    /**
     * 障碍
     **/
    private BigDecimal hdp;
    /**
     * 客队的障碍
     **/
    private BigDecimal awayHDP;
    /**
     * 主队的障碍
     **/
    private BigDecimal homeHDP;
    /**
     * 赔率
     **/
    private BigDecimal odds;
    /**
     * 赔率类型。请参阅附录。 赔率类型表
     **/
    private Integer oddsType;
    /**
     * 客队的得分
     **/
    private BigDecimal awayScore;
    /**
     * 主场球队得分
     **/
    private BigDecimal homeScore;
    /**
     * 是活的状态
     **/
    private String isLive;
    private String isLucky;
    private String bet_Tag;
    /**
     * 最后球码
     **/
    private String lastBallNo;
    /**
     * 状态
     **/
    private String ticketStatus;
    /**
     * 打赌的股份
     **/
    private BigDecimal stake;
    /**
     * 赢/亏金额
     **/
    private BigDecimal winLoseAmount;
    /**
     * 赢/亏金额
     **/
    private BigDecimal afterAmount;
    /**
     * ISO 8601 格式
     **/
    private Date winLostDateTime;
    private String currency;
    private Integer versionKey;

}
