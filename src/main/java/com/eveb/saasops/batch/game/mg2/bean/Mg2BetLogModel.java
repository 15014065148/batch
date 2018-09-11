package com.eveb.saasops.batch.game.mg2.bean;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Data
@ToString
@EqualsAndHashCode(callSuper=true)
public class Mg2BetLogModel extends BetLog {

    /**API前缀**/
    private String apiPrefix;
    /**前缀**/
    private String sitePrefix;
    private String id; //转账编号
    private String parent_transaction_id; //关联单号
    private String account_id;  //账号id
    private String account_ext_ref; //会员账号
    /**
     * 1001 web
     * 1002 html5
     * 1003 download
     * 1004 android客户端
     */
    private String application_id;//创建交易时游戏装置ID, 设备
    private String currency_unit;//货币代码
    private Date transaction_time; //2016-01-01 13:21:33.591
    private BigDecimal balance;//转帐后的余额
    private String created_by;//创建该用户的帐户ID
    private Date created; //2016-01-01 13:21:33.591 转帐单号建立时间
    private String session; //19751-19452-22
    private String ip; //
    private String wallet_code;//钱包类型代码
    private String external_ref;//外部交易参考编号
    private String category; //类型（投注或赢得,转账） wager :投注，TRANSFER：转账，payout
    private String sub_category;//不用管
    private String balance_type;//信用额度 ，没用
    private String type; //转账类型，
    private BigDecimal amount; //转账金额
    private MetaDate meta_data; //

}
