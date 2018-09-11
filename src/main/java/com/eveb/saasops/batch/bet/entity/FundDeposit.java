package com.eveb.saasops.batch.bet.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
public class FundDeposit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer id;

    //会员ID(mbr_account
    private Integer accountId;

    // "公司入款设置ID(set_basic_sys_deposit 主键)")
    private Integer companyPayId;

    // "收款账户（线上支付ID）")
    private Integer onlinePayId;

    // "0 线上入款 ,1 公司入款")
    private Integer mark;

    // "0 失败 1 成功 2待处理")
    private Integer status;

    // "0 未支付 1 支付")
    private Boolean isPayment;

    // "存款金额")
    private BigDecimal depositAmount;

    // "存款人姓名")
    private String depositUser;

    // "优惠金额")
    private BigDecimal discountAmount;

    // "手续费")
    private BigDecimal handlingCharge;

    // "实际到账")
    private BigDecimal actualArrival;

    // "审核人")
    private String auditUser;

    // "审核时间")
    private String auditTime;

    // "ip")
    private String ip;

    // "活动ID")
    private Integer activityId;

    // "备注")
    private String memo;

    private String createUser;

    private String createTime;

    private String modifyUser;

    private String modifyTime;

    @JsonSerialize(using = ToStringSerializer.class)
    // "订单号")
    private Long orderNo;

    // "订单前缀")
    private String orderPrefix;

    // "转账记录ID")
    private Integer billDetailId;

    @Transient
    // "会员名")
    private String loginName;

    @Transient
    private String groupName;

    @Transient
    private String agyAccount;

    @Transient
    // "开始时间 yyyy-MM-dd HH:mm:ss")
    private String createTimeFrom;

    @Transient
    // "结束时间 yyyy-MM-dd HH:mm:ss")
    private String createTimeTo;

    @Transient
    // "会员组ID")
    private Integer groupId;

    @Transient
    // "支付次数")
    private int depositCount;

    @Transient
    // "线上支付ID名称")
    private String onlinePayName;

    @Transient
    // "公司入款类型")
    private String depositType;

    @Transient
    // "是否提款 0否 1是")
    private Boolean isDrawings;
    @Transient
    // "真实姓名")
    private String realName;
    
   public interface Mark
   {
	   int onlinePay=0;//0 线上入款
	   int offlinePay=1;//1 公司入款
   }
   public interface Status
   {
	   int fail=0;//0 失败
	   int suc=1;//1 成功
	   int apply=2;//2待处理
   }
   public interface PaymentStatus
   {
	   boolean unPay=false;
	   boolean pay=true;
   }
}