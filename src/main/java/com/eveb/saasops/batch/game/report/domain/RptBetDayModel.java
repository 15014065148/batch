package com.eveb.saasops.batch.game.report.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RptBetDayModel {

    /**统计日期*/
    private String startday;
    /**用户名*/
    private String username;
    /**游戏平台*/
    private String platform;
    /**游戏类型*/
    private String gametype;
    private BigDecimal bet;
    /**有效投注*/
    private BigDecimal validbet;
    /**派彩*/
    private BigDecimal payout;
    /**彩金下注*/
    private BigDecimal jackpotBet;
    /**彩金额*/
    private BigDecimal jackpotPayout;
    /**小费*/
    private BigDecimal tip;
    /**总存款*/
    private BigDecimal deposit;
    /**总体款*/
    private BigDecimal withdrawal;
    /**反水*/
    private BigDecimal rebate;
    /**单量*/
    private Integer quantity;

    public RptBetDayModel() {
    }
}
