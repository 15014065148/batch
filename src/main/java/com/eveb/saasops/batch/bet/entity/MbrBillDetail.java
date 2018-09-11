package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class MbrBillDetail implements Serializable {

    private Integer id;

    //产生交易记录order
    private Long orderNo;

    //会员登陆名称
    private String loginName;

    private Integer accountId;

    //财务类别代码
    private String financialCode;

    //操作金额
    private BigDecimal amount;

    //操作后余额
    private BigDecimal afterBalance;

    //操作前的余额
    private BigDecimal beforeBalance;

    //操作类型，0 支出1 收入
    private Byte opType;

    //生成订单时间
    private String orderTime;

    private String memo;

    //平台id
    private Integer depotId;

    private String schemaPrex;
}