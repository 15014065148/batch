package com.eveb.saasops.batch.game.n2.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class N2BetLog extends BetLog implements Serializable {
    private String sitePrefix;
    private String apiPrefix;
    /**
     * 游戏code
     */
    private String gameCode;
    /**
     * 牌局独特 ID
     */
    private String dealId;
    /**
     * 牌局开始日期和时间("yyyy - mm - dd hh:mm:ss")
     */
    private Date startdate;
    /**
     * 牌局结束日期和时间 ("yyyy - mm - dd hh:mm:ss")
     */
    private Date enddate;
    /**
     * 返回状态 Success(1) - Fail(0)
     */
    private String dealStatus;

    /**
     * N2Live系统返回状态 - Success （成功） - Fail （失败）
     */
    private String status;
    /**
     * 游戏代码 -T4G030143
     */
    private String dealCode;
    /**
     * 玩家名称
     */
    private String palyLogin;
    /**
     * 总投注金额
     */
    private BigDecimal betAmount;
    /**
     * 系统派彩金额 （包括本金）
     */
    private BigDecimal payoutAmount;
    /**
     * 押码
     */
    private BigDecimal handle;
    /**
     * 纯赢/纯输
     */
    private BigDecimal hold;
    /**
     * 投注信息
     */
    private N2BetdetailLog n2BetdetailLog;
    /**
     * 结果信息
     */
    private String result;
    /**
     * 投注信息
     */
    private String betdetail;
    /**
     * 多个牌局结果
     */
    private String dealdetails;

}
