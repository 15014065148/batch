package com.eveb.saasops.batch.feign.feign.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by William on 2018/1/25.
 */
@Data
public class SetBacicOnlinepay {
    private Integer id;

    //账户名称
    private String name;
    //支付编码
    private String code;
    //支付平台Id

    private Integer paymentId;

    private  String merNo;
    //会员组Id

    private Integer mbrGroupId;
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

    private String payClass;

    private String payWay;

    private Integer mbrGroupNum;

    private String paymentName;

    private Integer bankNum;

    private String bankName;

    private String isEnables;

    private String mbrGroups;
}
