package com.eveb.saasops.batch.game.bg.domian;


import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * BG视讯小费信息
 * 2018-08-08 Jeff
 */
@Data
public class BgVideoTipBetLog extends BetLog {
    /**
     * 厅代码
     **/
    private String sn;
    /**
     * 用户ID
     **/
    private String userId;
    /**
     * 小费ID
     **/
    private String tipId;
    /**
     * 用户登录ID
     **/
    private String loginId;
    /**
     * 金额
     **/
    private BigDecimal amount;
    /**
     * 荷官工号
     **/
    private String staffNo;
    /**
     * 来源IP
     **/
    private String fromIp;
    /**
     * 操作时间
     **/
    private Date createTime;
    /**
     * 数据版本号
     **/
    private String version;

    /**
     * 下载时间
     */
    private Date downloadTime;
}
