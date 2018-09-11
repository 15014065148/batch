package com.eveb.saasops.batch.game.t188.processor;

import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.t188.domain.T188BetLog;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.math.BigDecimal;
import java.util.Date;

public class T188RptProcessor {

    /***
     * 转换成统计类型
     * @param item
     * @return
     * @throws Exception
     */
    public static RptBetModel process(T188BetLog item) throws Exception {
        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getWagerNo().toString());
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_T188.getValue());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_T188.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), GameTypeEnum.Enum_Sport.getKey(), item.getSportName()));
        rpt.setGameType(item.getSportName());
        rpt.setUserName(item.getUserCode());
        /**现只使用RMB**/
        rpt.setCurrency(CurrencyEnum.RMB_ENUM.getValue());
        rpt.setBet(item.getTotalStakeF());
        rpt.setPayout(BigDecimal.ZERO);
        if (GameCodeConstants.CONSTANT__T188_CHECK.equals(item.getBetStatus())) {
            rpt.setValidBet(item.getTotalStakeF());
            rpt.setPayout(item.getWinLossAmount());
            //Todo 先以 赢得减去下注
            if (item.getTotalStakeF().compareTo(item.getWinLossAmount()) > 0) {
                rpt.setResult(GameCodeConstants.CONSTANT_LOST);
            }else
            {
                rpt.setResult(GameCodeConstants.CONSTANT_WIN);
            }
        }
        rpt.setStatus(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_T188.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), GameTypeEnum.Enum_Sport.getKey(), item.getSettlementStatus().toString()));
        rpt.setRoundNo(item.getEventId().toString());
        rpt.setOrderDate(item.getDateEvent());
        rpt.setBetTime(item.getDateCreated());
        rpt.setDownloadTime(new Date());
        //Todo 目前无法区分
//        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), "deviceType", item.getGamePlatformType()));
        return rpt;
    }
}
