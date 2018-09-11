package com.eveb.saasops.batch.game.gg.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2018/08/02 Jeff
 */
@Data
public class GgBetLog extends BetLog {
    /**
     * 投注金额
     **/
    private BigDecimal bet;
    /**
     * 游戏编码
     **/
    private String gameId;
    /**
     * 货币
     **/
    private String cuuency;
    /**
     * 局号
     **/
    private String linkId;
    /**
     * 用户名
     **/
    private String accountno;
    /**
     * 唯一编码
     **/
    private String autoid;
    /**
     * 时间
     **/
    private String bettimeStr;
    /**
     * 标识设备类型 0=PC Web  1=Android  2=iOS  3=Android Web  4=iOS Web
     **/
    private String origin;
    /**
     * 输赢
     **/
    private BigDecimal profit;

}
