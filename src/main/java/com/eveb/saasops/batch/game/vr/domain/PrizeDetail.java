package com.eveb.saasops.batch.game.vr.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * VR彩票游戏投注记录
 */
@Getter
@Setter
public class PrizeDetail {
    private Integer count;//中奖注数
    private String number;//中奖数字
    private String awardName;//中奖名称

}
