package com.eveb.saasops.batch.bet.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
//返水优惠活动规则
public class WaterRebatesRuleListDto {

    //有效投注
    private BigDecimal validAmountMin;

    //有效投注"
    private BigDecimal validAmountMax;

    //返水比例
    private BigDecimal donateRatio;
}
