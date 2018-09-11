package com.eveb.saasops.batch.game.agin.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class AginBetLogModel extends BetLog {

    private String dataType;//下注记录详情
    private Long billNo;//注单流水号
    private String apiPrefix;//API前缀
    private String sitePrefix;//前缀
    private String playerName;//玩家账号
    private String agentCode;//代理商编号
    private String gameCode;//游戏局号
    private BigDecimal netAmount;//玩家输赢额度
    private Date betTime;//投注时间
    private String gameType;//游戏类型
    private BigDecimal betAmount;//投注金额
    private BigDecimal validBetAmount;//有效投注额度
    private String flag;//结算状态
    private String playType;//游戏玩法
    private String currency;//货币类型
    private String tableCode;//桌子编号
    private String loginIP;//玩家IP
    private Date recalcuTime;//注单重新派彩时间
    private String platformType;//平台类型
    private String remark;//额外信息
    private String round;//平台内的大厅类型
    private String result;//结果
    private BigDecimal beforeCredit;//玩家下注前的剩余额度
    private String deviceType;//设备类型

}
