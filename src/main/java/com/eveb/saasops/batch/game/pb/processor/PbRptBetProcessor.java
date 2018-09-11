package com.eveb.saasops.batch.game.pb.processor;

import com.eveb.saasops.batch.game.pb.domain.PbBetLog;
import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.math.BigDecimal;
import java.util.Date;

public class PbRptBetProcessor {

    public static RptBetModel process(PbBetLog item) throws Exception {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setId(item.getWagerId());
        rptBet.setGameType(item.getSport());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PB.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getSport()));
        rptBet.setBetType(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PB.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getBetType().toString()));
        rptBet.setPlatform(PlatFromEnum.Enum_PB.getKey());
        rptBet.setUserName(item.getUserCode());
        rptBet.setBet(item.getStake());
        rptBet.setValidBet(item.getTurnover());
        rptBet.setPayout(item.getWinLoss());
        rptBet.setCurrency(CurrencyEnum.getValue(item.getCurrencyCode()));
        String status = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PB.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getStatus());
        if (GameCodeConstants.CONSTANT_ISPAID.equals(status)) {
            rptBet.setResult(GameCodeConstants.getWinLoss(item.getWinLoss()));
        }
        rptBet.setStatus(status);
        rptBet.setBetTime(item.getWagerDateFm());
        rptBet.setOrderDate(item.getEventDateFm());
        rptBet.setDownloadTime(new Date());
        return rptBet;
    }
}
