package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
@Table(name = "set_bacic_onlinePay")
public class SetBacicOnlinepay implements Serializable{
private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //账户名称
    private String name;
    //支付编码
    private String code;
    //支付平台Id
    private Integer paymentId;
    private  String merNo;
    //是否启用 1启用 2禁言
    private Integer isEnable;
    //创建者
    private String createUser;
    //创建时间
    private Date createTime;
    //更新人
    private String modifyUser;
    //最后一次更新时间
    private Date modifyTime;
    private String description;
    private String password;
    private Integer sort ;
    private Integer minLimit;
    private Integer maxLimit;
    private Integer maxLimitDaily;
    private Integer mbrGroupType;
    private Integer domainId;
    @Transient
    private String payClass;
    @Transient
    private String payWay;
    private Integer isQR;

    @Transient
    private Integer mbrGroupNum;
    @Transient
    private String paymentName;
    @Transient
    private Integer bankNum;
    @Transient
    private String bankName;
    @Transient
    private String isEnables;
    @Transient
    private String mbrGroups;
    @Transient
    private Integer[] banks;
    @Transient
    private String bankOptions;
    @Transient
    private String selectedGroup;
    @Transient
    private BigDecimal depositAmount;
    @Transient
    private BigDecimal depositAmountDaily;
    @Transient
    private Integer payCount;
}