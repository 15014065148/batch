package com.eveb.saasops.batch.game.agin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
public class AGINSlotBetLogModel extends AginBetLogModel {

    private String slottype;//老虎机类型
    private Long mainbillno;//主订单号
    private BigDecimal betAmountBase;//扣除jackpot的投注 額度
    private BigDecimal betAmountBonus;//Jackpot下注额度
    private  BigDecimal netAmountBase;//游戏派彩
    private  BigDecimal netAmountBonus;//Jackpot派彩
}
