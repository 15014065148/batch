package com.eveb.saasops.batch.game.bg.process;

import com.eveb.saasops.batch.game.bg.domian.BgFishingBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgVideoBetLog;
import com.eveb.saasops.batch.game.bg.util.BgParamerUtil;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.math.BigDecimal;
import java.util.Date;


/**
 * BG投注转输赢报表
 * 2018-08-08 Jeff
 */
public class BgRptBetProcessor {

    public static RptBetModel videoProcess(BgVideoBetLog item) {
        RptBetModel rptBet = new RptBetModel();

        rptBet.setId(item.getOrderId());
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), item.getGameId()));
        rptBet.setGameType(item.getGameId());
        rptBet.setPlatform(PlatFromEnum.Enum_BG.getKey());
        rptBet.setUserName(item.getLoginId());
        rptBet.setRoundNo(item.getIssueId());

        rptBet.setBet(item.getBAmount());
        rptBet.setValidBet(item.getValidBet());
        rptBet.setPayout(item.getPayment());

        if (item.getOrderStatus() == 1) {
            rptBet.setStatus(GameCodeConstants.CONSTANT_NOPAID);
            rptBet.setResult(GameCodeConstants.CONSTANT_NOPAID);
        } else {
            rptBet.setBetTime(item.getOrderTime());
            rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
            if (item.getPayment().compareTo(BigDecimal.ZERO) == 0) {
                rptBet.setResult(GameCodeConstants.CONSTANT_TIE);
            } else if (item.getPayment().compareTo(BigDecimal.ZERO) > 0) {
                rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
            } else {
                rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
            }
        }
        rptBet.setPayoutTime(item.getLastUpdateTime());
        rptBet.setDownloadTime(new Date());
        rptBet.setOrderDate(item.getLastUpdateTime());
        rptBet.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BG.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getOrderFrom() + ""));
        rptBet.setCurrency(BgParamerUtil.CONSTANT_BG_CURRENCY);
        rptBet.setOpenResultDetail(item.getPlayName());

        return rptBet;
    }


    public static RptBetModel fishProcess(BgFishingBetLog item) {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setId(item.getBetId());
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), BgParamerUtil.CONSTANT_BG_GAMEID));
        rptBet.setGameType(BgParamerUtil.CONSTANT_BG_GAMEID);
        rptBet.setPlatform(PlatFromEnum.Enum_BG.getKey());
        rptBet.setUserName(item.getLoginId());
        rptBet.setBet(item.getBetAmount());
        rptBet.setSerialId(item.getIssueId());
        rptBet.setValidBet(item.getValidAmount());
        rptBet.setPayout(item.getPayout());
        rptBet.setJackpotPayout(item.getJackpot());
        rptBet.setBetTime(item.getOrderTime());
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        if (item.getPayout().compareTo(BigDecimal.ZERO) == 0) {
            rptBet.setResult(GameCodeConstants.CONSTANT_TIE);
        } else if (item.getPayout().compareTo(BigDecimal.ZERO) > 0) {
            rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
        } else {
            rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
        }
        rptBet.setStartTime(item.getOrderTime());
        rptBet.setPayoutTime(item.getOrderTime());
        rptBet.setDownloadTime(new Date());
        rptBet.setOrderDate(item.getOrderTime());
        rptBet.setCurrency(BgParamerUtil.CONSTANT_BG_CURRENCY);
        return rptBet;
    }
}
