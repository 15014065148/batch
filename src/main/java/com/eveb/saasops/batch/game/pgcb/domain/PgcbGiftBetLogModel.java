package com.eveb.saasops.batch.game.pgcb.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 礼物接水信息 2017-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PgcbGiftBetLogModel extends BetLog {
    /**
     * 会员用户名
     */
    private String username;
    /**
     * 礼物金额
     */
    private Integer amount;
    /**
     * 订单号
     */
    private String order_number;
    /**
     * 礼物类型（1:现金礼物,2:钻石礼物）
     */
    private Integer type;
    /**
     * 送礼时间
     */
    private String created_at;
    /**
     * 下载时间
     */
    private Date downloadTime;

}
