package com.eveb.saasops.batch.game.png.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class PngBetLong extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private Long transactionId;//交易id
    private Integer status;//交易状态
    private BigDecimal amount;//添加到用户账户的金额
    private Date time;//交易的UTC时间
    private Integer productGroup;//产品组
    private String externalUserId;//用户外部ID
    private Integer gamesessionId;//gamesessionID
    private Integer gamesessionState;//gamesession状态
    private Integer gameId;//游戏的ID 请参见货币、国家和语言附录
    private Long roundId;//该交易所属回合
    private String roundData;//若经配置，会包含细节约整数据
    private BigDecimal roundLoss;//该回合的总投注
    private BigDecimal jackpotLoss;//玩家输的/添加到任何奖池中的金额
    private BigDecimal jackpotGain;//玩家获得/从任何奖池中赢得的金额
    private String currency;//交易的货币。请参考货币、国家和语言附录
    private BigDecimal balance;//交易完成后的用户余额
    private Integer numRounds;//已玩的回合数
    private BigDecimal totalLoss;//游戏会话中的总投注额，包括任何从免费投注中的投注
    private BigDecimal totalGain;//游戏会话中赢取的金额，包括任何从免费游戏中赢取的金额
    private Integer externalFreegameId;//外部免费游戏ID，若交易是为了一个免费的游戏
    private Integer messageType;//消息类型
    private String messageId;//全球唯一的消息ID
    private Date messageTimestamp;//消息添加到队列的时间，精确到秒
}
