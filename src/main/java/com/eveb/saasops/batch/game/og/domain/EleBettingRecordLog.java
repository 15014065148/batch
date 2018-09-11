package com.eveb.saasops.batch.game.og.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

/**
 *电子投注记录
 * */
@Getter
@Setter
public class EleBettingRecordLog extends BetLog {

    private String productID;//增ＩＤ，唯一性
    private String userName;//员名
    private String orderNumber;//号
    private String gameBettingKind;//注游戏类别
    private String resultType;//戏结果类型：1表示输，2表示赢3表示和，4表示无效
    private String bettingAmount;//注金额
    private String winLoseAmount;//赢金额
    private String balance;//额
    private String addTime;//易时间
    private String platformID;// ag,8 laxino, 14 habanero ,15 pt
    private String vendorId ;//序号
    private String validAmount;//效投注额


}
