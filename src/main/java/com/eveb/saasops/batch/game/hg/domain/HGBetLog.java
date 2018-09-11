package com.eveb.saasops.batch.game.hg.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 2018/07/10 Jeff
 */
@Data
public class HGBetLog extends BetLog {
    /**
     * 下注开始时间
     */
    private String betStartDate;
    /**
     * 下注结束时间
     */
    private String betEndDate;
    /**
     *赌注名称
     */
    private String accountId;
    /**
     * 下注的游戏类型id
     */
    private String tableId;
    /**
     * 游戏id
     */
    private String gameId;
    /**
     * 下注id
     */
    private String betId;
    /**
     *  有效投注
     */
    private BigDecimal betAmount;
    /**
     *  派彩
     */
    private BigDecimal payout;
    /**
     * 币种
     */
    private String currency;
    /**
     * 游戏类型
     */
    private String gameType;
    /**
     * 投注的位置
     */
    private String betSpot;
    /**
     *下注编号
     */
    private String betNo;
    /**
     * 下注模式
     */
    private String betMode;


}
