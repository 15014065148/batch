package com.eveb.saasops.batch.game.agin.processor;

import com.eveb.saasops.batch.game.agin.domain.AGINHunterBetLogModel;
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
public class AginHunterRptBetProcessor {

    private String gameType = BBINGameTypeEnum.Enum_Slot.getKey();//使用游戏分类代码

    public RptBetModel process(Object object) throws Exception {
        if (object == null) {
            return null;
        }
        AGINHunterBetLogModel model = (AGINHunterBetLogModel) object;
        RptBetModel rpt = new RptBetModel();
        /**使用场景号+ 用户名 为唯一值，不同用户可以进入同一场景**/
        rpt.setId(model.getTradeNo());
        rpt.setApiPrefix(model.getApiPrefix());
        rpt.setSitePrefix(model.getSitePrefix());
        rpt.setUserName(model.getPlayerName());
        rpt.setPlatform(PlatFromEnum.Enum_AGIN.getValue());
        rpt.setGameType(model.getPlatformType());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), gameType, model.getPlatformType()));
        rpt.setRoundNo(model.getGameCode());
        rpt.setBet(model.getCost());
        rpt.setValidBet(model.getCost());
        rpt.setPayout(BigDecimal.ZERO);
        /**jackpot**/
        rpt.setJackpotBet(model.getJackpotcomm());
        //类型为捕鱼投注时
        if (GameCodeConstants.CONSTANT_CODE_AGIN_TYPE.equals(model.getType())) {
            rpt.setPayout(model.getTransferAmount());
        } else {
            /**不是下注类型注单均为奖池赢得**/
            rpt.setJackpotPayout(model.getTransferAmount());
        }
        //有效投注为零,和局
        if (model.getTransferAmount().compareTo(BigDecimal.ZERO) == 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_TIE);
        } else if (model.getTransferAmount().compareTo(BigDecimal.ZERO) > 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
        } else//输
        {
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
        }
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setCurrency(model.getCurrency());
        /**将时间转换成北京时间**/
        rpt.setBetTime(DateConvert.convertAsiaDate(model.getCreationTime()));
        rpt.setOrderDate(DateConvert.convertAsiaDate(model.getCreationTime()));
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        /**额度**/
        rpt.setBalanceBefore(model.getPreviousAmount());
        rpt.setBalanceAfter(model.getCurrentAmount());
        return rpt;
    }
}
