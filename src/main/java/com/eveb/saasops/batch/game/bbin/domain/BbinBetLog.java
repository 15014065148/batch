package com.eveb.saasops.batch.game.bbin.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class BbinBetLog extends BetLog implements Cloneable {

    /**API前缀**/
    private String apiPrefix;
    /**前缀**/
    private String sitePrefix;
    /**用户名**/
    private String userName;
    /**注单号**/
    private Long wagersID;
    /**下注时间**/
    private Date wagersDate;
    /**局号**/
    private String serialID;
    /**场次**/
    private String roundNo;
    /**游戏大类**/
    private String type;
    /**游戏种类**/
    private String gameType;
    /**玩法**/
    private String wagerDetail;
    /**桌号**/
    private String gameCode;
    /**开牌结果**/
    private String result;
    /**注单结果 ‐1：注销、0：未结算**/
    private String resultType;
    /**结果牌**/
    private String card;
    /**Y：已派彩、N：未派彩**/
    private String isPaid;
    /**退水**/
    private BigDecimal commission;
    /**下注金额**/
    private BigDecimal betAmount;
    /**派彩金额**/
    private BigDecimal payoff;
    /**币别**/
    private String currency;
    /**与人民币的汇率**/
    private BigDecimal exchangeRate;
    /**会员有效投注额**/
    private BigDecimal commissionable;
    /**1.行动装置下单：M 1‐1.ios手机：MI1 1‐2.ios平板：MI2 1‐3.Android手机：MA1 1‐4.Android平板：MA2 2.计算机下单：P'**/
    private String origin;
    /**注单变更时间**/
    private Date upTime;
    /**帐务时间**/
    private Date orderDate;
    /**注单变更时间**/
    private Date modifiedDate;
    /**Jackpot**/
    private String jp;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public BbinBetLog() {
    }
}
