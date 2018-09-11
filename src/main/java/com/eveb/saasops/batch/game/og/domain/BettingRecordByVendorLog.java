package com.eveb.saasops.batch.game.og.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 *真人游戏记录
 * */
@Getter
@Setter
public class BettingRecordByVendorLog extends BetLog {

    private Long productID;//自增ＩＤ，唯一性
    private String userName;//会员名
    private Long gameRecordID;//游戏结果ＩＤ
    private String orderNumber;//单号
    private Integer tableID;//桌号
    private Integer stage;//靴数
    private Integer inning;//局号
    private Integer gameNameID;//11表示百家乐，12表示龙虎，13表示轮盘，14表示骰宝，15德州扑克，16番摊,17AG电子游戏
    private Integer gameBettingKind;//投注类型
    private String gameBettingContent;//番摊,轮盘,骰宝的投注区
    private Byte resultType;//游戏结果类型：1表示输，2表示赢3表示和，4表示无效
    private BigDecimal bettingAmount;//投注金额
    private BigDecimal compensateRate;//赔率
    private BigDecimal winLoseAmount;//输赢金额
    private BigDecimal balance;//余额
    private Date addTime;//交易时间
    private Integer platformID;//0东方旗舰厅 1IBC 2AG 5明升 21东方锦绣厅 22东方鸿盛厅
    private Long vendorId;//顺序号
    private BigDecimal validAmount;//有效投注额
    private String gameResult;//游戏结果

}
