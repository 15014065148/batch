package com.eveb.saasops.batch.game.pgcb.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 彩票接水信息 2017-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PgcbLotteryBetLogModel extends BetLog {
    /**
     * 经销商标识
     */
    private String label;
    /**
     * 会员用户名
     */
    private String username;
    /**
     * 彩种名称
     */
    private String lottery_name;
    /**
     * 订单号
     */
    private String order_number;
    /**
     * 金额模式（1:元,0.1:角,0.01:分）
     */
    private String moshi;
    /**
     * 订单状态（1:未开奖,2:未中奖,3:中奖,4:撤单）
     */
    private Integer status;
    /**
     * 彩种数字ID
     */
    private Integer lottery_id;
    /**
     * 下注时间
     */
    private String created_at;
    /**
     * 结算时间
     */
    private String updated_at;
    /**
     * 下注详情
     */
    private String remark;
    /**
     * 期数
     */
    private String expect;
    /**
     * 注单数
     */
    private Integer total;
    /**
     * 下注金额
     */
    private String amount;
    /**
     * 中奖金额
     */
    private String amount_win;
    /**
     * 开奖号码
     */
    private String game_result;
    /**
     * 下注号码
     */
    private String number;
    /**
     * 下注设备（1PC,2H5,3安卓,4苹果）
     */
    private Integer device_type;
    /**
     * 赔率
     */
    private String jiangjin_bili;
    /**
     * 单注金额
     */
    private String unit_price;
    /**
     * 下注返点金额
     */
    private String amount_fandian;
    /**
     * 中奖注数
     */
    private Integer total_win;
    /**
     * 倍数
     */
    private Integer beishu;
    /**
     * 注单类型（1:真实会员的注单,0:虚拟会员的注单）
     */
    private Integer istrue;
}
