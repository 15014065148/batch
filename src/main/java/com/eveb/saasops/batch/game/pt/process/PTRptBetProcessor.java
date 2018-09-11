package com.eveb.saasops.batch.game.pt.process;

import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.pt.domain.PTBetLog;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class PTRptBetProcessor {

    public static RptBetModel process(PTBetLog item) {
        RptBetModel rpt = new RptBetModel();
        if (item.getWin().compareTo(BigDecimal.ZERO) == 0 && item.getBet().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setId(item.getGamecode().toString());
        rpt.setPlatform(PlatFromEnum.Enum_PT.getValue());
        rpt.setUserName(item.getPlayername());
        String gameType = item.getGamename().substring(item.getGamename().indexOf("(") + 1, item.getGamename().indexOf(")"));
        rpt.setGameType(gameType);
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PT.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), gameType));
        rpt.setBet(item.getBet());
        rpt.setJackpotBet(item.getProgressivebet());
        rpt.setJackpotPayout(item.getProgressivewin());
        rpt.setValidBet(item.getBet());
        /**投注比赢得大时，状态为输**/
        if (item.getBet().compareTo(item.getWin()) > 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
        } else {
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
        }
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setCurrency(CurrencyEnum.RMB_ENUM.getValue());
        /***平台派彩均大于0，没有减去投注***/
        rpt.setPayout(item.getWin().subtract(item.getBet()));
        rpt.setBetTime(item.getGamedate());
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrderDate(item.getGamedate());
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PT.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getCplatform()));
        rpt.setBalanceBefore(item.getBalance().add(item.getBet()).subtract(item.getWin()).subtract(item.getProgressivewin()));
        rpt.setBalanceAfter(item.getBalance());
        return rpt;
    }
}
