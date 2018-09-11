package com.eveb.saasops.batch.game.Ibc.processor;

import com.eveb.saasops.batch.game.Ibc.domain.IbcBetLog;
import com.eveb.saasops.batch.game.Ibc.domain.TicketStatusEnum;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class IbcRptProcess {

    /***
     * 转换成统计类型
     * @param item
     * @return
     * @throws Exception
     */
    public static RptBetModel processRpt(IbcBetLog item) throws Exception {

        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getTransId().toString());
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_IBC.getValue());
        rpt.setGameType(item.getSportType());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_IBC.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getSportType()));
        rpt.setBetType(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_IBC.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getBetType().toString()));
        rpt.setUserName(item.getPlayerName());
        rpt.setCurrency(item.getCurrency());
        rpt.setBet(item.getStake());
        String tstatus = item.getTicketStatus().toLowerCase();
        if (TicketStatusEnum.Enum_HalfLose.getKey().equals(tstatus) || TicketStatusEnum.Enum_HalfWon.getKey().equals(tstatus)
                || TicketStatusEnum.Enum_Lose.getKey().equals(tstatus) || TicketStatusEnum.Enum_Won.getKey().equals(tstatus)) {
            rpt.setValidBet(item.getStake());
            rpt.setPayout(item.getWinLoseAmount());
        }
        if(TicketStatusEnum.Enum_Draw.getKey().equals(tstatus))
        {
            rpt.setValidBet(BigDecimal.ZERO);
            rpt.setPayout(item.getWinLoseAmount());
        }
                String status=ApplicationConstant.getCodeContent(PlatFromEnum.Enum_IBC.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), item.getTicketStatus().toLowerCase());
                if(GameCodeConstants.CONSTANT_ISPAID.equals(status)) {
                rpt.setResult(GameCodeConstants.getWinLoss(item.getWinLoseAmount()));
                }
                rpt.setStatus(status);
                rpt.setRoundNo(item.getMatchId()==null?"":item.getMatchId().toString());
                /**转成北京时间**/
                rpt.setOrderDate(DateConvert.convertAsiaDate(item.getWinLostDateTime()));
                rpt.setBetTime(DateConvert.convertAsiaDate(item.getTransactionTime()));
                rpt.setDownloadTime(DateUtil.orderDate(new Date()));
                return rpt;
                }
                }
