package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
//返水优惠
public class OprActBonus implements Serializable {

    private Integer id;

    //会员ID
    private Integer accountId;

    //会员名
    private String loginName;

    //申请时间
    private String applicationTime;

    //转账记录ID
    private Integer billDetailId;

    //奖励比例
    private BigDecimal donateRatio;

    //有效投注额
    private BigDecimal validBet;

    //活动ID
    private Integer activityId;

    //审核人
    private String auditUser;

    //审核时间
    private String auditTime;

    //0 拒绝 1通过 2待处理")
    private Integer status;

    //奖励红利
    private BigDecimal bonusAmount;

    //备注
    private String memo;

    //1 有效投注额 2显示存款金额
    private Integer isShow;

    //存款金额
    private BigDecimal depositedAmount;

    //活动规则ID
    private Integer ruleId;

    private String schemaPrex;

    private Long orderNo;

    private String orderPrefix;
}