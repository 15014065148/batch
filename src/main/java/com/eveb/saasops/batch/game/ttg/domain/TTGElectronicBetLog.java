package com.eveb.saasops.batch.game.ttg.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TTG电子游戏记录
 */
@Getter
@Setter
public class TTGElectronicBetLog extends BetLog {

    private String playerId;//玩家ID  MGMalan520
    private String partnerId;//合作伙伴ID  MGMBET
    private Long transactionId;//交易ID  574676564
    private Date transactionDate;//交易日期  20180712 08:26:20
    private String currency;//货币 CNY
    private String game;//游戏 LegendOfLinkH5
    private String transactionSubType;//类型 Wager：投注 Resolve：派彩
    private Long handId;// 2836983225
    private BigDecimal amount;//金额 -2.50

}
