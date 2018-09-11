package com.eveb.saasops.batch.game.pgcb.domain;

import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

public class PgcbRptBetProcessor {

    public static RptBetModel process(PgcbLotteryBetLogModel item) {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setId(item.getOrder_number());
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setGameName(item.getLottery_name());
        rptBet.setGameType(item.getLottery_id() + "");
        rptBet.setPlatform(PlatFromEnum.Enum_PGCB.getKey());
        rptBet.setUserName(item.getUsername());

        rptBet.setBetType(item.getNumber());//期号
        rptBet.setRoundNo(item.getExpect());

        //投注金额
        BigDecimal amount = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(item.getAmount())) {
            rptBet.setBet(new BigDecimal(item.getAmount()));
        }

        //中奖金额
        BigDecimal amountWin = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(item.getAmount_win())) {
            amountWin = new BigDecimal(item.getAmount_win());
        }

        switch (item.getStatus()) {//订单状态
            case 1: //1:未开奖
                rptBet.setBet(amount);
                rptBet.setResult(GameCodeConstants.CONSTANT_NOPAID);
                break;
            case 2://2:未中奖
                rptBet.setBet(amount);
                rptBet.setValidBet(amount);
                rptBet.setPayout(BigDecimal.ZERO);
                rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
                break;
            case 3://3:中奖
                rptBet.setValidBet(amount);
                rptBet.setBet(amount);
                rptBet.setPayout(amountWin);
                rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
                break;
            default://4:撤单
                rptBet.setBet(amount);
                rptBet.setResult(GameCodeConstants.CONSTANT_CANCELID);
                break;
        }

        if (StringUtil.isNotEmpty(item.getUpdated_at())) {
            rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        } else {
            rptBet.setStatus(GameCodeConstants.CONSTANT_NOPAID);
        }

        rptBet.setBetTime(DateUtil.parse(item.getCreated_at(), DateUtil.FORMAT_18_DATE_TIME));
        rptBet.setStartTime(DateUtil.parse(item.getCreated_at(), DateUtil.FORMAT_18_DATE_TIME));
        if (StringUtil.isNotEmpty(item.getUpdated_at())) {
            rptBet.setPayoutTime(DateUtil.parse(item.getUpdated_at(), DateUtil.FORMAT_18_DATE_TIME));
            rptBet.setOrderDate(DateUtil.parse(item.getUpdated_at(), DateUtil.FORMAT_18_DATE_TIME));
        }
        rptBet.setDownloadTime(new Date());
        rptBet.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_PGCB.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getDevice_type() + ""));//下注设备（1PC,2H5,3安卓,4苹果）
        rptBet.setCurrency(ApplicationConstant.CONSTANT_FGCB_CURRENCY);
        return rptBet;
    }


    public static RptBetModel casinoProcess(PgcbCasinoBetLogModel item) {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setId(item.getOrder_number());
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setGameName(item.getGame_name());
        rptBet.setGameType(item.getCasino_id());
        rptBet.setPlatform(PlatFromEnum.Enum_PGCB.getKey());
        rptBet.setUserName(item.getUsername());

        BigDecimal amount = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(item.getAmount())) {
            amount = new BigDecimal(item.getAmount());
        }
        BigDecimal amountWin = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(item.getAmount())) {
            amountWin = new BigDecimal(item.getAmount_win());

        }
        rptBet.setBet(amount);
        rptBet.setBetType(item.getNumber());
        rptBet.setValidBet(amount);
        rptBet.setPayout(amountWin);
        if (amountWin.compareTo(BigDecimal.ZERO) == 1) {//大于0,结果为赢
            rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
        } else {
            rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
        }
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBet.setBetTime(DateUtil.parse(item.getCreated_at(), DateUtil.FORMAT_18_DATE_TIME));
        rptBet.setStartTime(DateUtil.parse(item.getCreated_at(), DateUtil.FORMAT_18_DATE_TIME));
        rptBet.setPayoutTime(DateUtil.parse(item.getCreated_at(), DateUtil.FORMAT_18_DATE_TIME));
        rptBet.setDownloadTime(new Date());
        rptBet.setOrderDate(DateUtil.parse(item.getCreated_at(), DateUtil.FORMAT_18_DATE_TIME));
        rptBet.setCurrency(ApplicationConstant.CONSTANT_FGCB_CURRENCY);


        return rptBet;
    }
}
