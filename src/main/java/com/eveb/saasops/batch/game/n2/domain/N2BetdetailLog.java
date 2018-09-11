package com.eveb.saasops.batch.game.n2.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class N2BetdetailLog {
    /**
     * 投注金额
     */
    private BigDecimal bank;
    /**
     * 投闲金额
     */
    private BigDecimal player;
    /**
     * 投和金额
     */
    private BigDecimal tie;
    /**
     * 投庄对子金额
     */
    private BigDecimal bankdp;
    /**
     * 投闲对子金额
     */
    private BigDecimal playerdp;
    /**
     * 投大金额
     */
    private BigDecimal big;
    /**
     * 投小金额
     */
    private BigDecimal small;
    /**
     * 投完美对子金额
     */
    private BigDecimal perfectdp;
    /**
     * 投任何对子金额
     */
    private BigDecimal eitherdp;
    /**
     * 投庄对子组合金额
     */
    private String bankdpc;
    /**
     * 投闲对子组合金额
     */
    private BigDecimal playerdpc;
    /**
     * 投龙 7 金额
     */
    private BigDecimal bankd7;
    /**
     * 投凤 8 金额
     */
    private BigDecimal playerp8;
    /**
     * 投庄龙宝金额
     */
    private BigDecimal bankdb;
    /**
     * 投闲龙宝金额
     */
    private BigDecimal playerdb;
    /**
     * 投庄列牌胜金额
     */
    private BigDecimal bankcase;
    /**
     * 投闲列牌胜金额
     */
    private BigDecimal playercase;
    /**
     * 投庄总合为 0
     */
    private String bank0;
    /**
     * 投庄总合为 1
     */
    private String bank1;
    /**
     * 投庄总合为 2
     */
    private String bank2;
    /**
     * 投庄总合为 3
     */
    private String bank3;
    /**
     * 投庄总合为 4
     */
    private String bank4;
    /**
     * 投庄总合为 5
     */
    private String bank5;
    /**
     * 投庄总合为 6
     */
    private String bank6;
    /**
     * 投庄总合为 7
     */
    private String bank7;
    /**
     * 投庄总合为 8
     */
    private String bank8;
    /**
     * 投庄总合为 9
     */
    private String bank9;
    /**
     * 投闲总合为 0
     */
    private String player0;
    /**
     * 投闲总合为 1
     */
    private String player1;
    /**
     * 投闲总合为 2
     */
    private String player2;
    /**
     * 投闲总合为 3
     */
    private String player3;
    /**
     * 投闲总合为 4
     */
    private String player4;
    /**
     * 投闲总合为 5
     */
    private String player5;
    /**
     * 投闲总合为 6
     */
    private String player6;
    /**
     * 投闲总合为 7
     */
    private String player7;
    /**
     * 投闲总合为 8
     */
    private String player8;
    /**
     * 投闲总合为 9
     */
    private String player9;
}
