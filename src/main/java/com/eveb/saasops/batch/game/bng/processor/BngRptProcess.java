package com.eveb.saasops.batch.game.bng.processor;

import com.eveb.saasops.batch.game.bng.bean.BngItem;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class BngRptProcess {

    public static RptBetModel processRpt(BngItem bngBetLogModel) {
        BigDecimal fee =new BigDecimal(100);
        RptBetModel rpt =new RptBetModel();
        rpt.setId(bngBetLogModel.getUid());
        BigDecimal balanceAfter=new BigDecimal(bngBetLogModel.getBalance_after() == null ? 0 : bngBetLogModel.getBalance_after());
        BigDecimal win=new BigDecimal(bngBetLogModel.getWin()== null ? 0 : bngBetLogModel.getWin());
        BigDecimal bet=new BigDecimal(bngBetLogModel.getBet() == null ? 0 : bngBetLogModel.getBet());
        rpt.setBet(bet.divide(fee));
        rpt.setApiPrefix(bngBetLogModel.getApiPrefix());
        rpt.setSitePrefix(bngBetLogModel.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_BNG.getValue());
        rpt.setUserName(bngBetLogModel.getPlayer_id());
        rpt.setValidBet(bet.divide(fee));
        rpt.setPayout(win.divide(fee));
        rpt.setBetTime(getDate(bngBetLogModel.getC_at()));
        rpt.setPayoutTime(getDate(bngBetLogModel.getC_at()));
        rpt.setDownloadTime(getDate(bngBetLogModel.getC_at()));
        rpt.setBalanceAfter(balanceAfter.divide(fee));
        rpt.setBalanceBefore((balanceAfter.subtract(win).add(bet)).divide(fee));
        rpt.setOrderDate(getDate(bngBetLogModel.getC_at()));
        rpt.setCurrency(bngBetLogModel.getCurrency());
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BNG.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, bngBetLogModel.getSegment()));
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BNG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), bngBetLogModel.getGame_name()));
        rpt.setRoundNo(bngBetLogModel.getRounds().get(0).toString());
        return rpt;
    }
    private static Date getDate(String timeString){
        //ISO-8601 ("YYYY-MM-DDThh:mm:ss±hh:mm"), 余额转移交易的创建时间
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
        Date date = new Date();
        try {
            date=sdf.parse(timeString);
        } catch (ParseException e) {
            log.info("要转换的时间 {}  ",timeString);
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,8);
        return calendar.getTime();
    }
}
