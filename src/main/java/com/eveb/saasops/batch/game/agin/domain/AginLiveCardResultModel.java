package com.eveb.saasops.batch.game.agin.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class AginLiveCardResultModel extends BetLog {
    private String dataType;
    private String gmcode;//局号
    private String tablecode;//桌号
    private Date begintime;//开始时间
    private Date closetime;//结束时间
    private String dealer;//dealer
    private String shoecode;
    private String flag;
    private String bankerPoint;
    private String playerPoint;
    private String cardnum;
    private String pair;
    private String gametype;
    private String dragonpoint;
    private String tigerpoint;
    private String cardlist;//牌面结果
    private String vid;
    private String platformtype;

}
