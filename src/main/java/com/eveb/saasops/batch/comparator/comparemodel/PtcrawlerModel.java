package com.eveb.saasops.batch.comparator.comparemodel;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by William on 2018/2/14.
 */
@Data
public class PtcrawlerModel {
    
    private BigDecimal realBalanceChange;//真实结存变化
    private BigDecimal deposits;//存款
    private BigDecimal withdraws; //提款
    private BigDecimal compensation;//
    private BigDecimal commission;//
    private Integer activePlayers;//活跃人数
    private BigDecimal bonuses;//奖金
    private BigDecimal progressiveShare; //累计下注
    private BigDecimal progressiveWins;//累计盈利
    private BigDecimal bets;//投注
    private BigDecimal wins;//盈利
    private BigDecimal netLoss; //净亏损
    private BigDecimal netPurchase;//净存款
    private BigDecimal netGaming;//净收益
    private BigDecimal houseEarnings;//淨賺

}
