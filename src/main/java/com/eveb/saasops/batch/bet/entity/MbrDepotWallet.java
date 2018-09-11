package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "mbr_depot_wallet")
public class MbrDepotWallet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //会员ID
    private Integer accountId;

    //会员账号
    private String loginName;

    private String pwd;

    //平台ID号 平台号ID为0是本平台
    private Integer depotId;

    //平台名称
    private String depotName;

    //会员资金余额
    private BigDecimal balance;

    //0未登陆,1已登陆
    private Byte isLogin;

    //0未转账,1已转账
    private Byte isTransfer;

    private String time;

    private String schemaPrex;
}