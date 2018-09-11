package com.eveb.saasops.batch.game.ab.domain;

import com.eveb.saasops.batch.game.GameBetLogModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
public class AbModifyBetLogModel extends GameBetLogModel {
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
    private String gameRoundId;
    /**
     * 游戏类型
     */
    private Integer gameType;
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
     * 注单状态
     */
    private Integer state;
    /**
     * 币种
     */
    private String currency;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * 投注类型
     */
    private Integer betType;
    /**
     * 开牌结果
     */
    private String gameResult;
    /**
     * 游戏局结束时间
     */
    private String gameRoundEndTime;
    /**
     * 游戏局开始时间
     */
    private String gameRoundStartTime;
    /**
     * 桌台名称
     */
    private String tableName;
    /**
     * 桌台类型
     */
    private String commission;
}
