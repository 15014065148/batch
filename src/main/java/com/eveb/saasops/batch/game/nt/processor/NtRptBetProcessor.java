package com.eveb.saasops.batch.game.nt.processor;

import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.nt.domain.NTBetLog;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.math.BigDecimal;
import java.util.Date;

public class NtRptBetProcessor {


    public static RptBetModel process(NTBetLog ntBetLog) throws Exception {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setApiPrefix(ntBetLog.getApiPrefix());
        rptBet.setSitePrefix(ntBetLog.getSitePrefix());
        rptBet.setId(ntBetLog.getId().toString());
        rptBet.setGameType(ntBetLog.getGameId());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_NT.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), ntBetLog.getGameId()));
        rptBet.setPlatform(PlatFromEnum.Enum_NT.getKey());
        rptBet.setUserName(ntBetLog.getUserId());
        rptBet.setPayout(ntBetLog.getAmount());
        rptBet.setCurrency(ntBetLog.getCurrency());
        /***当返回的注单类型为赢时注单为结果注单，当为输时是下注类型***/
        if (ntBetLog.getTranType().contains(GameCodeConstants.CONSTANT_CODE_NT_TRANTYPE_WIN)) {
            rptBet.setBet(BigDecimal.ZERO);
            rptBet.setValidBet(BigDecimal.ZERO);
            rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
            rptBet.setBalanceBefore(ntBetLog.getBalance().subtract(ntBetLog.getAmount()));
        } else {
            //绝对值
            rptBet.setBet(ntBetLog.getAmount().abs());
            rptBet.setValidBet(ntBetLog.getAmount().abs());
            rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
            rptBet.setBalanceBefore(ntBetLog.getBalance().add(ntBetLog.getAmount().abs()));
        }
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBet.setBetTime(ntBetLog.getDateTime());
        rptBet.setOrderDate(ntBetLog.getDateTime());
        rptBet.setDownloadTime(new Date());
        rptBet.setBalanceAfter(ntBetLog.getBalance());
        return rptBet;
    }
}
