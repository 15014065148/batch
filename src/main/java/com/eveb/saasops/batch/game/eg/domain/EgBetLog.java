package com.eveb.saasops.batch.game.eg.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class EgBetLog extends BetLog {

    /*** 前缀**/
    private String sitePrefix;
    private String apiPrefix;
    /***自增ID**/
    private Long id;
    /***会员名**/
    private String userName;
    /***游戏结果ID**/
    private Long gameRecordId;
    /***单号**/
    private String orderNumber;
    /***桌号**/
    private Integer tableId;
    /***靴数**/
    private Integer stage;
    /***局号**/
    private Integer inning;
    /***11表示百家乐，12表示龙虎，13表示轮盘，14表示骰宝，15德州扑克，16番摊**/
    private Integer gameNameId;
    /***投注类型**/
    private Integer gameBettingKind;
    /***番摊,轮盘,骰宝的投注区,以”,”分割,每个区为一个下注区,内容为102^50.0^-50.0^,下注区^下注金额^输赢金额,**/
    private String gameBettingContent;
    /***游戏结果类型：1表示输，2表示赢3表示和，4表示无效**/
    private Integer resultType;
    /***投注金额**/
    private BigDecimal bettingAmount;
    /***赔率**/
    private BigDecimal compensateRate;
    /***输赢金额**/
    private BigDecimal winLoseAmount;
    /***余额**/
    private BigDecimal balance;
    /***交易时间**/
    private Date addTime;
    /***0表示东方**/
    private Integer platformId;
    /***顺序号**/
    private Long vendorId;
    /***有效投注**/
    private BigDecimal ValidAmount;
    /***标准百家乐 = 111, 免佣百家乐 = 112, 超级百家乐 = 113, 标准连环百家乐 = 114, 免佣连环百家乐 = 115 龙虎 = 211，轮盘 = 311,骰宝 = 411，德州 = 511,番摊 = 611 **/
    private Integer gameKind;

}
