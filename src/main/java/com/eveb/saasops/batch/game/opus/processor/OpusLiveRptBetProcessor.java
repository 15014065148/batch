package com.eveb.saasops.batch.game.opus.processor;

import com.eveb.saasops.batch.game.opus.domain.OpusLiveBetLog;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;

import java.math.BigDecimal;
import java.util.Date;

public class OpusLiveRptBetProcessor {

    public static RptBetModel process(OpusLiveBetLog item) throws Exception
    {
        RptBetModel rptBet = new RptBetModel();
        rptBet.setApiPrefix(item.getApiPrefix());
        rptBet.setSitePrefix(item.getSitePrefix());
        rptBet.setId(item.getTransaction_id().toString());
        rptBet.setGameType(item.getGame_code());
        rptBet.setGameName(item.getGame_detail());
        rptBet.setPlatform(PlatFromEnum.Enum_OPUSCA.getKey());
        rptBet.setUserName(item.getMember_code());
        rptBet.setBet(item.getBet());
        /**和局时有效投注为零**/
        if(GameCodeConstants.CONSTANT_CODE_OPUS_LIVE_STATUS_TIP.equals(item.getBet_status()))
        {
            rptBet.setValidBet(BigDecimal.ZERO);
        }else {
            rptBet.setValidBet(item.getBet());
        }
        /**赢得减去下注**/
        rptBet.setPayout(item.getWin().subtract(item.getBet()));
        rptBet.setCurrency(item.getCurrency());
        if (GameCodeConstants.CONSTANT_CODE_OPUS_LIVE_STATUS_WIN.equals(item.getBet_status())) {
            rptBet.setResult(GameCodeConstants.CONSTANT_WIN);
        } else if((GameCodeConstants.CONSTANT_CODE_OPUS_LIVE_STATUS_LOSS.equals(item.getBet_status()))){
            rptBet.setResult(GameCodeConstants.CONSTANT_LOST);
        }else
        {
            rptBet.setResult(GameCodeConstants.CONSTANT_TIE);
        }
        rptBet.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBet.setBalanceBefore(item.getBalance_start());
        rptBet.setBalanceAfter(item.getBalance_end());
        rptBet.setBetTime(item.getTransaction_date_time());
        rptBet.setOrderDate(item.getTransaction_date_time());
        rptBet.setDownloadTime(new Date());
        return rptBet;
    }
}
