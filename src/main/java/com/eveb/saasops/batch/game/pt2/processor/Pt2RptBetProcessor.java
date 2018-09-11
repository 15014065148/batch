package com.eveb.saasops.batch.game.pt2.processor;

import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.pt2.domain.Pt2BetLog;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Pt2RptBetProcessor {

    public static RptBetModel process(Pt2BetLog item) {
        RptBetModel rpt = new RptBetModel();
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setId(item.getRoundId());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PT2.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), item.getGameCode()));
        rpt.setGameType(item.getGameCode());
        rpt.setPlatform(PlatFromEnum.Enum_PT2.getValue());
        rpt.setUserName(item.getPlayerCode());
        rpt.setBet(item.getBet());
        rpt.setValidBet(item.getBet());
        /***取公司收入为派彩,并进行取反操作***/
        rpt.setPayout(item.getRevenue().negate());
        rpt.setCurrency(item.getCurrency());
        /***收入大于零时为公司赢得，玩家输***/
        rpt.setResult(GameCodeConstants.getWinLoss(item.getRevenue().negate()));
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setBetTime(item.getFirstTs());
        rpt.setPayoutTime(item.getTs());
        rpt.setOrderDate(item.getFirstTs());
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PT2.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getDevice()));
        rpt.setBalanceBefore(item.getBalanceBefore());
        rpt.setBalanceAfter(item.getBalanceAfter());
        return rpt;
    }
}
