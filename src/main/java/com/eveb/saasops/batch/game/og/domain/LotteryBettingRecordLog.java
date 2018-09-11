package com.eveb.saasops.batch.game.og.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

/**
 *彩票投注记录
 * */
@Getter
@Setter
public class LotteryBettingRecordLog extends BetLog {

    private String productID;//自增ＩＤ，唯一性
    private String userName;//会员名
    private String gameRecordID;//游戏结果ＩＤ
    private String orderNumber;//单号
    private String tableID;//桌号
    private String stage;//局号
    private String inning;//靴数
    private String gameNameID;//请参考 遊戲代碼表.pdf
    private String gameBettingKind;//请参考 遊戲代碼表.pdf
    private String gameBettingContent;//下注内容文字表述
    private String resultType;//游戏结果类型：1表示输，2表示赢3表示和，4表示无效
    private String bettingAmount;//投注金额
    private String compensateRate;//赔率
    private String winLoseAmount;//输赢金额
    private String balance;//余额
    private String addTime;//交易时间
    private String platformID;//13 dios
    private String vendorId ;//顺序号
    private String validAmount;//有效投注额


}
