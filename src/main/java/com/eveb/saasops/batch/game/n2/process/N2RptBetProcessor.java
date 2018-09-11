package com.eveb.saasops.batch.game.n2.process;

import com.eveb.saasops.batch.game.n2.domain.N2BetLog;
import com.eveb.saasops.batch.game.n2.domain.N2BetTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


public class N2RptBetProcessor {

    public static RptBetModel process(N2BetLog betLog) {
        RptBetModel rpt = new RptBetModel();
        rpt.setApiPrefix(betLog.getApiPrefix());
        rpt.setSitePrefix(betLog.getSitePrefix());
        rpt.setId(betLog.getDealId());
        rpt.setPlatform(PlatFromEnum.Enum_N2.getValue());
        rpt.setUserName(betLog.getPalyLogin());
        rpt.setGameType(betLog.getGameCode());
        rpt.setBet(betLog.getBetAmount());
        rpt.setPayout(betLog.getHold());
        rpt.setBetTime(betLog.getStartdate());
        rpt.setValidBet(betLog.getHold().compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:betLog.getBetAmount());
        rpt.setResult(GameCodeConstants.getWinLoss(betLog.getHold()));
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        setAllBetType(betLog, rpt);
        setAllGameCode(betLog, rpt);
        rpt.setOrderDate(betLog.getStartdate());
        rpt.setCurrency("RMB");
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setSerialId(betLog.getDealId());
        return rpt;
    }

    private static void setAllGameCode(N2BetLog betLog, RptBetModel rpt) {
        String gameCode = betLog.getGameCode();
        if (gameCode.equals("50002")) {
            rpt.setGameName(N2BetTypeEnum.Enum_50002.getValue());
        }
        if (gameCode.equals("51002")) {
            rpt.setGameName(N2BetTypeEnum.Enum_51002.getValue());
        }
        if (gameCode.equals("52002")) {
            rpt.setGameName(N2BetTypeEnum.Enum_52002.getValue());
        }
        if (gameCode.equals("60001")) {
            rpt.setGameName(N2BetTypeEnum.Enum_60001.getValue());
        }
        if (gameCode.equals("61001")) {
            rpt.setGameName(N2BetTypeEnum.Enum_61001.getValue());
        }
        if (gameCode.equals("62001")) {
            rpt.setGameName(N2BetTypeEnum.Enum_62001.getValue());
        }
        if (gameCode.equals("90091")) {
            rpt.setGameName(N2BetTypeEnum.Enum_90091.getValue());
        }
        if (gameCode.equals("91091")) {
            rpt.setGameName(N2BetTypeEnum.Enum_91091.getValue());
        }
        if (gameCode.equals("90092")) {
            rpt.setGameName(N2BetTypeEnum.Enum_90092.getValue());
        }
        if (gameCode.equals("91092")) {
            rpt.setGameName(N2BetTypeEnum.Enum_91092.getValue());
        }
    }

    private static void setAllBetType(N2BetLog betLog, RptBetModel rpt) {
        StringBuffer betTypeBJL = new StringBuffer();
        StringBuffer betTypeChiplP = new StringBuffer();
        StringBuffer betTypeChipTB = new StringBuffer();
        JSONArray jsonArray = JSONArray.fromObject(betLog.getBetdetail());
        jsonArray.stream().forEach(obj -> {
            Map<String, String> objMap = (Map) obj;
            objMap.entrySet().forEach(entry -> {
                String bjlStr = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_N2.getValue(),
                        CodeTypeEnum.Enum_Wager_Code.getKey(), N2BetTypeEnum.Enum_90091.getKey(), entry.getKey());
                String tbStr = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_N2.getValue(),
                        CodeTypeEnum.Enum_Wager_Code.getKey(), N2BetTypeEnum.Enum_60001.getKey(), entry.getKey());
                String lpStr = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_N2.getValue(),
                        CodeTypeEnum.Enum_Wager_Code.getKey(), N2BetTypeEnum.Enum_50002.getKey(), entry.getKey());
                if (StringUtils.isNotEmpty(bjlStr)) rpt.setBetType(betTypeBJL.append(bjlStr).toString());
                if (StringUtils.isNotEmpty(tbStr)) rpt.setBetType(betTypeChipTB.append(tbStr).toString());
                if (StringUtils.isNotEmpty(lpStr)) rpt.setBetType(betTypeChiplP.append(lpStr).toString());
            });
        });
    }
}

