package com.eveb.saasops.batch.game.og.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

/**
 *捕鱼王投注记录
 * */
@Getter
@Setter
public class FishBettingRecordLog extends BetLog {

    private String productID;//自增ＩＤ，唯一性
    private String userName;//会员名
    private String orderNumber;//单号
    private String gameBettingKind;//投注游戏类别
    private String resultType;//游戏结果类型：1表示输，2表示赢3表示和，4表示无效
    private String bettingAmount;//投注金额
    private String winLoseAmount;//输赢金额
    private String compensateRate;//抽水
    private String balance;//余额
    private String addTime;//交易时间
    private String platformID;//2 ag,8 laxino, 14 habanero ,15 pt
    private String vendorId ;//顺序号
    private String validAmount;//有效投注额


}
