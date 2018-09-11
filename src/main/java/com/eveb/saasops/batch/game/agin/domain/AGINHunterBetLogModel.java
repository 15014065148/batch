package com.eveb.saasops.batch.game.agin.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 16:16 2017/12/5
 **/
@Data
@EqualsAndHashCode(callSuper=true)
public class AGINHunterBetLogModel extends BetLog {

    private String dataType;//捕鱼王場景记录详情
    private String id;//项目编号
    private String tradeNo;//交易编号
    private String platformType;//平台类型
    private Long sceneId;//場景號
    private String apiPrefix;//API前缀
    private String sitePrefix;//前缀
    private String playerName;//玩家账户
    private Integer type;//转账类别 (1=場景捕魚, 2=抽獎, 7= 捕魚王獎勵)
    private Date seneStartTime;//場景開始時間
    private Date seneEndTime;//場景結束時間
    private String roomid;//房間號
    private BigDecimal roombet;//房間倍率
    private BigDecimal cost;//投注額度(注單類型type=1 適用)
    private BigDecimal earn;//派彩(注單類型type=1 適用)
    private BigDecimal jackpotcomm;//場景彩池投注
    private BigDecimal transferAmount;//转账额度
    private BigDecimal previousAmount;//转账前额度
    private BigDecimal currentAmount;//当前额度
    private String currency;//货币类型
    private BigDecimal exchangeRate;//汇率
    private String ip="0.0.0.0";//玩家IP
    private Integer flag;//結算状态  (0=成功)
    private Date creationTime;//纪录时间
    private String gameCode;//游戏局号(通常为空)

    public void setIp(String ip) {
        if(!ip.equals("")) {
            this.ip = ip;
        }
    }
}
