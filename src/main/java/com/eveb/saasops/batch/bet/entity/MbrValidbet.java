package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class MbrValidbet {

    private Integer id;

    private Integer activityId;

    private BigDecimal validBet;

    private String time;

    private Integer accountId;

    private String loginName;

    private String schemaPrex;
}