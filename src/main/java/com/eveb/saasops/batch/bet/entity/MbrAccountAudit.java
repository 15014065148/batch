package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class MbrAccountAudit{
    private Integer id;

    private String loginName;

    private Integer accountId;

    private String time;

    private BigDecimal validBet;

    private BigDecimal depositAmount;

    private BigDecimal depositOutBalance;

    private Double depositAudit;

    private BigDecimal administrationCost;

    private BigDecimal costRatio;

    private BigDecimal discountAmount;

    private Double discountAudit;

    private BigDecimal discountBalance;

    private BigDecimal discountDeduct;

    private BigDecimal depositBalance;

    private Integer status;

    private BigDecimal remainValidBet;

    private Boolean isDrawings;

    private Boolean isCalculate;

    private String auditRule;

    private Boolean isDiscount;

    private Integer depositId;

    private String financialCode;

    private Integer scope;

    private String memo;

    private String modifyUser;

    private String modifyTime;

    private Boolean sort;

    private String schemaPrex;
}