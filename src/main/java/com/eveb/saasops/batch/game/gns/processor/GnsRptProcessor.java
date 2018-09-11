package com.eveb.saasops.batch.game.gns.processor;

import com.eveb.saasops.batch.game.gns.model.GnsBetLogModel;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.DateUtil;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

public class GnsRptProcessor {

    /***
     * 转换成统计类型
     * @param item
     * @return RptBetModel
     */
    public static RptBetModel processRpt(GnsBetLogModel item){

        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getCausality());
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_GNS.getValue());
        String gameId=item.getGame_id();
        rpt.setGameType(gameId);
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_GNS.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), gameId));
        rpt.setUserName(item.getUser_id());
        rpt.setCurrency(item.getCurrency());
        /**金額移轉 以分为单位**/
        BigDecimal payout=BigDecimal.valueOf(item.getTotal_won()).subtract(BigDecimal.valueOf(item.getTotal_bet()));
        rpt.setBet(BigDecimal.valueOf(item.getTotal_bet()).divide(ApplicationConstant.CONSTANT_GNS_AMOUNT_UNIT));
        rpt.setValidBet(BigDecimal.valueOf(item.getTotal_bet()).divide(ApplicationConstant.CONSTANT_GNS_AMOUNT_UNIT));
        rpt.setPayout(payout.divide(ApplicationConstant.CONSTANT_GNS_AMOUNT_UNIT));
        rpt.setResult(payout.compareTo(BigDecimal.ZERO)>0?GameCodeConstants.CONSTANT_WIN:GameCodeConstants.CONSTANT_LOST);
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        /**转成北京时间**/
        rpt.setOrderDate(item.getTimestamp());
        rpt.setBetTime(item.getTimestamp());
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        return rpt;
    }
}
