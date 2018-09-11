package com.eveb.saasops.batch.game.vr.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * VR彩票游戏投注记录
 */
@Getter
@Setter
public class BetRecord  extends BetLog {
    private BigDecimal cost;//投注金额
    private BigDecimal unit;//投注单位
    private BigDecimal lossPrize;//重新颁奖带来的损失
    private BigDecimal playerPrize;//玩家中奖金额
    private String merchantCode;//商户代码
    private BigDecimal merchantPrize;//商户中奖金额
    private Integer state;//投注状态
    private BigDecimal count;//投注注数
    private BigDecimal multiple;//投注倍数
    private Integer channelId;//频道ID
    private String channelCode;//频道代码
    private Integer subState;//次级状态
    private Date createTime;//下注时间
    private Date updateTime;//最后更新时间
    private String note;//备注
    private String winningNumber;//开奖数字
    private String issueNumber;//期号
    private String betTypeName;//投注种类名称
    private String channelName;//频道名称
    private String position;//投注位置
    private String number;//投注数字
    private String odds;//赔率，有可能中文
    private BigDecimal playerOdds;//玩家赔率
    private String playerName;//玩家名称
    private String serialNumber;//投注订单号
    private List<PrizeDetail> prizeDetail;//中奖明细


}