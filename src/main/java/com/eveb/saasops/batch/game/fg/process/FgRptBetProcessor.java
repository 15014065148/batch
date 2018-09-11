package com.eveb.saasops.batch.game.fg.process;

import com.eveb.saasops.batch.game.fg.domain.FgBetLog;
import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

public class FgRptBetProcessor {

    public static RptBetModel process(FgBetLog item) {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setId(item.getId());
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_FG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), item.getGame_id() + ""));
        rptBet.setGameType(item.getType() + "");
        rptBet.setPlatform(PlatFromEnum.Enum_FG.getKey());
        rptBet.setUserName(item.getNickname());

        BigDecimal blance = item.getStart_chips().subtract(item.getEnd_chips());//计算投注
        rptBet.setBet(blance);
        rptBet.setPayout(blance);
        rptBet.setValidBet(BigDecimal.ZERO);
        if (blance.compareTo(BigDecimal.ZERO) == 0) {
            rptBet.setResult(GameCodeConstants.CONSTANT_TIE);
        } else if (blance.compareTo(BigDecimal.ZERO) > 0) {
            rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
        } else {
            rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
        }

        rptBet.setCurrency(ApplicationConstant.CONSTANT_FG_CURRENCY);
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBet.setBetTime(DateUtil.parse(item.getStart_time(),DateUtil.FORMAT_38_DATE_TIME));
        rptBet.setOrderDate(DateUtil.parse(item.getEnd_time(),DateUtil.FORMAT_38_DATE_TIME));
        rptBet.setDownloadTime(new Date());
        rptBet.setOrigin(item.getGame_terminal() + "");
        return rptBet;
    }
}
