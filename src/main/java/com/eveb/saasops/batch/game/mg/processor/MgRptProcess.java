package com.eveb.saasops.batch.game.mg.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg.domain.MgBetLog;
import com.eveb.saasops.batch.game.mg.service.MgElasticService;
import com.eveb.saasops.batch.game.report.constants.*;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class MgRptProcess {

    @Autowired
    private MgElasticService service;
    @Autowired
    private RptElasticRestService rptElasticService;


    public RptBetModel processRpt(MgBetLog item) throws Exception
    {
        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getColId().toString());
        String gameid = item.getGameKey().replace("Game:", "");
        if (GameCodeConstants.CONSTANT_CODE_MG_BETTYPE.equals(item.getType())) {
            rpt.setBet(item.getAmount());
            rpt.setValidBet(item.getAmount());
            rpt.setJackpotBet(item.getClrngAmnt());
            rpt.setPayout(item.getAmount().negate());
            /***预设输，只有赢得才有返回注单***/
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
            /** 当下注时 转账后的金额加上下注的金额**/
            rpt.setBalanceBefore(item.getAfterTxWalletAmount().add(item.getAmount()));
        } else {
            rpt.setBet(BigDecimal.ZERO);
            rpt.setValidBet(BigDecimal.ZERO);
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
            rpt.setPayout(item.getAmount());
            rpt.setJackpotPayout(item.getClrngAmnt());
            /** 当赢得时 转账后的金额减去派彩的金额**/
            rpt.setBalanceBefore(item.getAfterTxWalletAmount().subtract(item.getAmount()));
            rpt.setBalanceAfter(item.getAfterTxWalletAmount());
        }
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_MG.getValue());
        String gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), gameid);
        if (gamename.isEmpty()) {
            gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), gameid);
        }
        rpt.setGameName(gamename);
        rpt.setGameType(gameid);
        rpt.setUserName(item.getMbrUsername());
        rpt.setCurrency(CurrencyEnum.RMB_ENUM.getValue());
        rpt.setRoundNo(item.getMgsGameId().toString());
        /**转成北京时间**/
        rpt.setOrderDate(item.getTransactionTimestampDate());
        rpt.setBetTime(item.getTransactionTimestampDate());
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getGamePlatformType()));
        rpt.setBalanceAfter(item.getAfterTxWalletAmount());
        return rpt;
    }

    /***
     * 转换成统计类型
     * @param item
     * @return
     * @throws Exception
     */
    public void processRpt_bak(MgBetLog item) throws Exception {
        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getColId().toString());
        // "gameKey": "Game:28350"
        String gameid = item.getGameKey().replace("Game:", "");
        String id = gameid + item.getMgsGameId() + item.getMbrUsername();
        if (GameCodeConstants.CONSTANT_CODE_MG_BETTYPE.equals(item.getType())) {
            rpt.setBet(item.getAmount());
            rpt.setValidBet(item.getAmount());
            rpt.setJackpotBet(item.getClrngAmnt());
            rpt.setPayout(item.getAmount().negate());
            /***预设输，只有赢得才有返回注单***/
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
            /** 当下注时 转账后的金额加上下注的金额**/
            rpt.setBalanceBefore(item.getAfterTxWalletAmount().add(item.getAmount()));
        } else {
            rpt.setBet(BigDecimal.ZERO);
            rpt.setValidBet(BigDecimal.ZERO);
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
            BigDecimal payout=item.getAmount().add(service.queryLog(id));
            rpt.setPayout(payout);
            rpt.setJackpotPayout(item.getClrngAmnt());
            /** 当赢得时 转账后的金额减去派彩的金额**/
            rpt.setBalanceBefore(item.getAfterTxWalletAmount().subtract(payout));
            rpt.setBalanceAfter(item.getAfterTxWalletAmount());
             toUpdate(id, rpt);
             return;
        }
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_MG.getValue());
        String gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), gameid);
        if (gamename.isEmpty()) {
            gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), gameid);
        }
        rpt.setGameName(gamename);
        rpt.setGameType(gameid);
        rpt.setUserName(item.getMbrUsername());
        rpt.setCurrency(CurrencyEnum.RMB_ENUM.getValue());
        rpt.setRoundNo(item.getMgsGameId().toString());
        /**转成北京时间**/
        rpt.setOrderDate(item.getTransactionTimestampDate());
        rpt.setBetTime(item.getTransactionTimestampDate());
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_MG.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getGamePlatformType()));
        rpt.setBalanceAfter(item.getAfterTxWalletAmount());
        toInser(id, rpt);
    }

    public void toInser(String id, RptBetModel object) throws Exception {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.REPORT_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.REPORT_TYPE + "\", \"_id\" : \"" + id + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        rptElasticService.insertList(string.toString());
    }

    public void toUpdate(String id, RptBetModel object) throws Exception {
        StringBuffer string = new StringBuffer();
        string.append("{ \"update\" : { \"_index\" : \"" + ElasticSearchConstant.REPORT_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.REPORT_TYPE + "\", \"_id\" : \"" + id + "\" }}");
        string.append("\n");
        string.append("{ \"doc\" :" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") + "}");
        string.append("\n");
        rptElasticService.insertList(string.toString());
    }
}
