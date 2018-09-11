package com.eveb.saasops.batch.game.ab.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AbEgameLogModel {
    /**
     * 客户用户名
     */
    private String client;
    /**
     * 注单编号
     */
    private Long betNum;
    /**
     * 游戏局编号
     */
    private String gameround;
    /**
     * 游戏类型
     */
    private Integer egameType;
    /**
     * 投注时间
     */
    private String betTime;
    /**
     * 投注金额
     */
    private BigDecimal betAmount;
    /**
     * 有效投注金额
     */
    private BigDecimal validAmount;
    /**
     * 输赢金额
     */
    private BigDecimal winOrLoss;
    /**
     * Jackpot 投注金额
     */
    private BigDecimal jackpotBetAmount;
    /**
     * Jackpot 有效投注金额
     */
    private BigDecimal jackpotValidAmout;
    /**
     * Jackpot 输赢金额
     */
    private BigDecimal jackpotWinOrLoss;
    /**
     * 游戏类型
     */
    private Integer gameType;

}
