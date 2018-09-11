package com.eveb.saasops.batch.game.agin.processor;

import com.eveb.saasops.batch.game.agin.domain.AGINSlotBetLogModel;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.*;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.DateUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
@Component
public class AginSlotRptBetProcessor {

    private String gameType= BBINGameTypeEnum.Enum_Slot.getKey();


    public RptBetModel process(Object object) throws Exception {
        AGINSlotBetLogModel model = (AGINSlotBetLogModel) object;
        RptBetModel rpt = new RptBetModel();
        rpt.setId(model.getBillNo().toString());
        rpt.setUserName(model.getPlayerName());
        rpt.setApiPrefix(model.getApiPrefix());
        rpt.setSitePrefix(model.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_AGIN.getValue());
        rpt.setGameType(model.getGameType());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), gameType, model.getGameType()));
        rpt.setBet(model.getBetAmount());
        rpt.setBetType(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), gameType, model.getPlayType()));
        rpt.setRoundNo(model.getGameCode());
        rpt.setValidBet(model.getValidBetAmount());
        rpt.setPayout(model.getNetAmount());
        rpt.setJackpotBet(model.getBetAmountBonus());
        rpt.setJackpotPayout(model.getNetAmountBonus());
        //有效投注为零,和局
        /*if (model.getValidBetAmount().compareTo(BigDecimal.ZERO) == 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_TIE);
        } else if (model.getNetAmount().compareTo(BigDecimal.ZERO) > 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
        } else {
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
        }*/
        rpt.setResult(GameCodeConstants.getWinLoss(model.getNetAmount()));
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setCurrency(model.getCurrency());
        rpt.setBetTime(DateConvert.convertAsiaDate((model.getBetTime())));
        rpt.setOrderDate(DateConvert.convertAsiaDate((model.getBetTime())));
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, model.getDeviceType()));
        if (GameCodeConstants.CONSTANT_CODE_PLATFORMTYPE_YOPLAY.equals(model.getPlatformType())) {
            rpt.setBalanceBefore(model.getBeforeCredit());
            /**投注前额度+ 派彩 +奖池赢得**/
            rpt.setBalanceAfter(model.getBeforeCredit().add(model.getNetAmountBase()).add(model.getNetAmountBonus()));
        }
        return rpt;
    }
}
