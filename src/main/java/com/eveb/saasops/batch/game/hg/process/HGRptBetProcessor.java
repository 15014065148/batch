package com.eveb.saasops.batch.game.hg.process;

import com.eveb.saasops.batch.game.hg.domain.HGBetLog;
import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;


import java.math.BigDecimal;
import java.util.Date;

public class HGRptBetProcessor {

    public static RptBetModel process(HGBetLog item) {
        String gameName;
        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getBetNo());
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        if (item.getPayout().compareTo(BigDecimal.ZERO) > 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
        }
        if(item.getPayout().compareTo(BigDecimal.ZERO) < 0){
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
        }
        rpt.setValidBet(item.getBetAmount());
        //电子
        gameName=ApplicationConstant.getCodeContent(PlatFromEnum.Enum_HG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), item.getTableId());
        if(gameName.isEmpty()){
            //真人
            gameName=ApplicationConstant.getCodeContent(PlatFromEnum.Enum_HG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), item.getTableId());
            if (item.getPayout().compareTo(BigDecimal.ZERO) == 0) {//有效投注为零,和局
                rpt.setResult(GameCodeConstants.CONSTANT_TIE);
                rpt.setValidBet(BigDecimal.ZERO);
            }
            rpt.setBetType(item.getBetSpot());
        }
        rpt.setGameName(gameName);
        rpt.setGameType(item.getGameType());
        rpt.setPlatform(PlatFromEnum.Enum_HG.getValue());
        rpt.setUserName(item.getAccountId());
        rpt.setBet(item.getBetAmount());
        rpt.setPayout(item.getPayout());
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setBetTime(DateUtil.parse(item.getBetStartDate(),DateUtil.FORMAT_28_DATE_TIME));
        rpt.setPayoutTime(DateUtil.parse(item.getBetEndDate(),DateUtil.FORMAT_28_DATE_TIME));
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrderDate(DateUtil.parse(item.getBetStartDate(),DateUtil.FORMAT_28_DATE_TIME));
        rpt.setCurrency(item.getCurrency());
        return rpt;
    }
}
