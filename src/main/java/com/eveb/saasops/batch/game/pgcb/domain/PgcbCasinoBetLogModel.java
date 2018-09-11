package com.eveb.saasops.batch.game.pgcb.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电子游戏接水信息 2017-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PgcbCasinoBetLogModel extends BetLog {
    /**
     * 游戏ID
     */
    private String casino_id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 订单号
     */
    private String order_number;
    /**
     * 游戏名称（欢乐牛牛）
     */
    private String game_name;
    /**
     * 下注金额
     */
    private String amount;
    /**
     * 中奖金额
     */
    private String amount_win;
    /**
     * 下注号码
     */
    private String number;
    /**
     * 游戏结果
     */
    private String game_result;
    /**
     * 游戏时间
     */
    private String created_at;
    /**
     * 经销商ID
     */
    private String reseller_id;


}
