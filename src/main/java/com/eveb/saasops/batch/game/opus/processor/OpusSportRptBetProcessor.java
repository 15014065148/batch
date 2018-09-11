package com.eveb.saasops.batch.game.opus.processor;

import com.eveb.saasops.batch.game.opus.domain.OpusSportBetLog;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.processor.IProcessor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpusSportRptBetProcessor{

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static RptBetModel process(OpusSportBetLog item) throws Exception {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setId(item.getTrans_id().toString());
        rptBet.setGameType(item.getSportname());
        //todo 没有游戏对照码
        rptBet.setGameName(item.getSportname());
        rptBet.setPlatform(PlatFromEnum.Enum_OPUSSB.getKey());
        rptBet.setUserName(item.getMember_id());
        rptBet.setBet(item.getStake());
        if (GameCodeConstants.CONSTANT_CODE_OPUS_SPORT_WINLOST_STATUS_C.equals(item.getWinlost_status())) {
            rptBet.setValidBet(BigDecimal.ZERO);
        } else {
            rptBet.setValidBet(item.getStake());
        }
        rptBet.setPayout(item.getWinlost_amount());
        rptBet.setCurrency(item.getCurrency());
        rptBet.setRoundNo(item.getAwayname());
        String status=ApplicationConstant.getCodeContent(PlatFromEnum.Enum_OPUSSB.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getWinlost_status());
        if(GameCodeConstants.CONSTANT_ISPAID.equals(status)) {
            rptBet.setResult(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_OPUSSB.getValue(), CodeTypeEnum.Enum_Result_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getWinlost_status()));
        }
        rptBet.setStatus(status);
        rptBet.setBetTime(sdf.parse(item.getMatch_datetime()));
        rptBet.setOrderDate(sdf.parse(item.getMatch_datetime()));
        rptBet.setDownloadTime(new Date());
        return rptBet;
    }
}
