package com.eveb.saasops.batch.game.vr.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * VR彩票游戏投注记录
 */
@Getter
@Setter
public class VrLotteryBetLog {
    private Integer recordCountPerPage;
    private Integer recordPage;
    private Integer totalRecords;
    private List<BetRecord> betRecords;
}
