package com.eveb.saasops.batch.game.bg.domian;


import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * BG视讯投注信息
 * 2018-08-08 Jeff
 */
@Data
public class BgVideoBetLog extends BetLog {
    /**
     * 订单ID
     **/
    private String orderId;
    /**
     * 业务批次ID
     **/
    private String tranId;
    /**
     * 厅代码
     **/
    private String sn;
    /**
     * 用户ID
     **/
    private String uid;
    /**
     * 用户登录ID
     **/
    private String loginId;
    /**
     * 模块ID ：0 平台, 1 彩票模块, 2 视讯模块, 3 体育模块, 4 游戏模块
     **/
    private Integer moduleId;
    /**
     * 模块名称
     **/
    private String moduleName;
    /**
     * 游戏ID
     **/
    private String gameId;
    /**
     * 玩法名称
     **/
    private String gameName;
    /**
     * 注单状态:1 未结算, 2 结算赢,3 结算和,4 结算输,5 取消,6 过期,7 系统取消
     **/
    private Integer orderStatus;
    /**
     * 下注额
     **/
    private BigDecimal bAmount;
    /**
     * 结算额
     **/
    private BigDecimal aAmount;
    /**
     * 订单来源:1 PC端 浏览器, 2 PC客户端, 3 安卓APP, 4 iPhone App, 5 安卓平板App, 6 Apple Pad App, 7 系统后台, 8 H5(手机端), 9 微信
     **/
    private Integer orderFrom;
    /**
     * 下注时间
     **/
    private Date orderTime;
    /**
     * 最后修改时间
     **/
    private Date lastUpdateTime;
    /**
     * 下注来源IP
     **/
    private String fromIp;
    /**
     * 下注期数
     **/
    private String issueId;
    /**
     * 玩法ID
     **/
    private String playId;
    /**
     * 玩法名称
     **/
    private String playName;
    /**
     * 玩法名称(En)
     **/
    private String playNameEn;
    /**
     * 打码量(有效投注)
     **/
    private BigDecimal validBet;
    /**
     * 派彩(输赢)
     **/
    private BigDecimal payment;

}
