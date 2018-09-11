package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Table(name = "mbr_account")
public class MbrAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //会员账号
    private String loginName;

    //会员组
    private Integer groupId;

    //总代 top 冗余字段
    private Integer tagencyId;

    //直属代理 direct
    private Integer cagencyId;

    //是否验证手机(1验证，0未验证)
    private Byte isVerifyMoblie;

    //是否验证邮箱(1验证，0未验证)
    private Byte isVerifyEmail;

    //手机否接收消息(1允许，0不允许)
    private Byte isAllowMsg;

    //邮箱否接收消息(1允许，0不允许)
    private Byte isAllowEmail;

    //真实名称
    private String realName;

}