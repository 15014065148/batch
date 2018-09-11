package com.eveb.saasops.batch.game.mg2.processor;

import com.eveb.saasops.batch.game.mg2.bean.Mg2BetLogModel;
import com.eveb.saasops.batch.game.mg2.request.Mg2Request;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.math.BigDecimal;
import java.util.Date;

public class Mg2RptProcess {

    private static final String CATEGROY_WAGER = "wager";
    private static final String CATEGROY_PAYOUT = "payout";

    public static RptBetModel processRpt(Mg2BetLogModel mg2BetLogModel) {
        RptBetModel rptBetModel = new RptBetModel();
        String gameName = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG2.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), mg2BetLogModel.getMeta_data().getItem_id());
        if (gameName.equals("")) {
            gameName = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG2.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), mg2BetLogModel.getMeta_data().getItem_id());
        }
        rptBetModel.setGameName(gameName);
        rptBetModel.setPlatform(PlatFromEnum.Enum_MG2.getValue());
        rptBetModel.setApiPrefix(mg2BetLogModel.getApiPrefix());
        rptBetModel.setSitePrefix(mg2BetLogModel.getSitePrefix());
        rptBetModel.setUserName(mg2BetLogModel.getAccount_ext_ref());
        rptBetModel.setCurrency(mg2BetLogModel.getCurrency_unit());
        rptBetModel.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBetModel.setBetTime(mg2BetLogModel.getCreated());
        rptBetModel.setPayoutTime(mg2BetLogModel.getCreated());
        rptBetModel.setDownloadTime(new Date());
        rptBetModel.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG2.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, mg2BetLogModel.getApplication_id()));
        if (mg2BetLogModel.getCategory().equalsIgnoreCase(CATEGROY_WAGER)) {
            rptBetModel.setBet(mg2BetLogModel.getAmount());
            rptBetModel.setId(mg2BetLogModel.getId());
            rptBetModel.setValidBet(mg2BetLogModel.getAmount());
            rptBetModel.setResult(GameCodeConstants.CONSTANT_LOST);
            rptBetModel.setBalanceBefore(mg2BetLogModel.getBalance().add(mg2BetLogModel.getAmount()));
            rptBetModel.setBalanceAfter(mg2BetLogModel.getBalance());
            rptBetModel.setPayout(mg2BetLogModel.getAmount().negate());
        }
        if (mg2BetLogModel.getCategory().equalsIgnoreCase(CATEGROY_PAYOUT)) {
            rptBetModel.setBet(new BigDecimal(0));
            rptBetModel.setValidBet(new BigDecimal(0));
            rptBetModel.setPayout(mg2BetLogModel.getAmount());
            rptBetModel.setId(mg2BetLogModel.getParent_transaction_id());
            rptBetModel.setResult(GameCodeConstants.CONSTANT_WIN);
            rptBetModel.setBalanceAfter(mg2BetLogModel.getBalance());
            rptBetModel.setBalanceBefore(mg2BetLogModel.getBalance().subtract(mg2BetLogModel.getAmount()));
        }
        return rptBetModel;
    }
}
