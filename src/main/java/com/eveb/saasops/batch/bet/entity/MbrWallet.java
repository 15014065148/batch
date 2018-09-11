package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
@Table(name = "mbr_wallet")
public class MbrWallet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //会员ID
    private Integer accountId;

    //会员账号
    private String loginName;

    //会员资金余额
    private BigDecimal balance;

    private String schemaPrex;
}