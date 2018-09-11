package com.eveb.saasops.batch.game.gg.process;

import com.eveb.saasops.batch.game.gg.domain.GgBetLog;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 20178-08-02  Jeff
 */
public class GgRptBetProcessor {

    public static RptBetModel process(GgBetLog item) {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setId(item.getAutoid());
        item.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_GG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), item.getGameId()));
        rptBet.setGameType(item.getGameId());
        rptBet.setPlatform(PlatFromEnum.Enum_GG.getKey());
        rptBet.setUserName(item.getAccountno());
        rptBet.setBet(item.getBet());
        rptBet.setSerialId(item.getLinkId());

        Date betDate = DateUtil.parse(item.getBettimeStr(), DateUtil.FORMAT_18_DATE_TIME);
        BigDecimal balidBetAmount = item.getProfit();
        rptBet.setPayout(balidBetAmount);
        if (balidBetAmount.compareTo(BigDecimal.ZERO) == 0) {
            rptBet.setResult(GameCodeConstants.CONSTANT_TIE);
        } else if (balidBetAmount.compareTo(BigDecimal.ZERO) > 0) {
            rptBet.setValidBet(balidBetAmount);
            rptBet.setBetTime(betDate);
            rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
        } else {
            rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
        }
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBet.setStartTime(betDate);
        rptBet.setPayoutTime(betDate);
        rptBet.setDownloadTime(new Date());

        //标识设备类型 0=PC Web  1=Android  2=iOS  3=Android Web  4=iOS Web
        rptBet.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_GG.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getOrigin() + ""));
        rptBet.setCurrency(item.getCuuency());
        return rptBet;
    }
}
