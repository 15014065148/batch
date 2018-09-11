package com.eveb.saasops.batch.game.eg.processor;

import com.eveb.saasops.batch.game.eg.domain.EgBetLog;
import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.util.Date;

public class EgRptProcess {

    /***
     * 转换成统计类型
     * @param item
     * @return
     * @throws Exception
     */
    public static RptBetModel processRpt(EgBetLog item) throws Exception {
        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getVendorId().toString());
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_EG.getValue());
        rpt.setGameType(item.getGameNameId().toString());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_EG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), item.getGameNameId().toString()));
        rpt.setBetType(item.getGameBettingContent());
        rpt.setUserName(item.getUserName());
        rpt.setCurrency(CurrencyEnum.RMB_ENUM.getValue());
        rpt.setBet(item.getBettingAmount());
        rpt.setValidBet(item.getValidAmount());
        rpt.setPayout(item.getWinLoseAmount());
        rpt.setResult(GameCodeConstants.getWinLoss(item.getWinLoseAmount()));
        rpt.setStatus(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_EG.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), item.getResultType().toString()));
        rpt.setRoundNo(item.getInning().toString());
        rpt.setOrderDate(item.getAddTime());
        rpt.setBetTime(item.getAddTime());
        rpt.setDownloadTime(new Date());
        rpt.setBalanceBefore(item.getBalance().subtract(item.getWinLoseAmount()));
        rpt.setBalanceAfter(item.getBalance());
        return rpt;
    }
}
