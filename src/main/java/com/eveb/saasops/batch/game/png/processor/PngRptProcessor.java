package com.eveb.saasops.batch.game.png.processor;

import com.eveb.saasops.batch.game.png.domain.PngBetLong;
import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class PngRptProcessor {

    public static RptBetModel process(PngBetLong pngBetLong) throws Exception {
        RptBetModel rptBet=new RptBetModel();
        rptBet.setApiPrefix(pngBetLong.getApiPrefix());
        rptBet.setSitePrefix(pngBetLong.getSitePrefix());
        rptBet.setId(pngBetLong.getTransactionId().toString());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PNG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(),String.valueOf(pngBetLong.getGameId())));
        rptBet.setGameType(pngBetLong.getGameId().toString());
        rptBet.setPlatform(PlatFromEnum.Enum_PNG.getKey());
        rptBet.setUserName(pngBetLong.getExternalUserId());
        rptBet.setBet(pngBetLong.getRoundLoss());
        rptBet.setValidBet(pngBetLong.getRoundLoss());
        //添加到用户账户的金额。没有减去投注金额
        rptBet.setPayout(pngBetLong.getAmount().subtract(pngBetLong.getRoundLoss()));
        //玩家获得/从任何彩池中赢得的金额
        rptBet.setJackpotPayout(pngBetLong.getJackpotGain());
        rptBet.setCurrency(pngBetLong.getCurrency());
        rptBet.setResult(GameCodeConstants.getWinLoss(pngBetLong.getAmount().subtract(pngBetLong.getRoundLoss())));
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBet.setBetTime(pngBetLong.getTime());
        rptBet.setOrderDate(pngBetLong.getTime());
        rptBet.setDownloadTime(DateUtil.orderDate(new Date()));
        /**游戏记录无法区分，但是电脑投注时gameId是五位数字，如100247，以100开通，手机投注是三位，如247**/
        rptBet.setOrigin(pngBetLong.getGameId().toString().length()>3?GameCodeConstants.CONSTANT_CODE_ORIGIN_PHONE:GameCodeConstants.CONSTANT_CODE_ORIGIN_PC);
        rptBet.setBalanceBefore(pngBetLong.getBalance().subtract(pngBetLong.getAmount()).add(pngBetLong.getRoundLoss()));
        rptBet.setBalanceAfter(pngBetLong.getBalance());
        return rptBet;
    }
}
