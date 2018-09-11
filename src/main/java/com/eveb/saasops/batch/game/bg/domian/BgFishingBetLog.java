package com.eveb.saasops.batch.game.bg.domian;


import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * BG捕鱼投注信息
 * 2018-08-08 Jeff
 */
@Data
public class BgFishingBetLog extends BetLog {
    /**
     * 厅代码
     **/
    private String sn;
    /**
     * 用户ID
     **/
    private String userId;
    /**
     * 用户登录ID
     **/
    private String loginId;
    /**
     * 局号
     **/
    private String issueId;
    /**
     * 注单号
     **/
    private String betId;
    /**
     * 发炮数量
     **/
    private Integer fireCount;
    /**
     * 下注额
     **/
    private BigDecimal betAmount;
    /**
     * 有效投注
     **/
    private BigDecimal validAmount;
    /**
     * 结算金额
     **/
    private BigDecimal calcAmount;
    /**
     * 派彩(输赢)
     **/
    private BigDecimal payout;
    /**
     * 下注时间(UTC-4)
     **/
    private Date orderTime;
    /**
     * 奖池如果没有奖池为0（奖励用户的金额）
     **/
    private BigDecimal jackpot;
    /**
     * 奖池类型，默认为0 （目前只有一种奖励类型 默认值为0 打中宝箱并获得彩金）
     **/
    private Integer jackpotType;

}
