package com.eveb.saasops.batch.game.bbin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.OpenResultModel;
import com.eveb.saasops.batch.game.bbin.domain.BbinBetLog;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.GameTypeConstant;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.DateUtil;
import com.xxl.job.core.log.XxlJobLogger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class BbinRptProcessor {

    /***
     * 转换成统计类型
     * @param item
     * @param gameType
     * @return
     * @throws Exception
     */
    public static RptBetModel processRpt(BbinBetLog item, String gameType) throws Exception {
        RptBetModel rpt = new RptBetModel();
        rpt.setId(item.getWagersID().toString());
        rpt.setApiPrefix(item.getApiPrefix());
        rpt.setSitePrefix(item.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_BBIN.getValue());
        rpt.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), gameType, item.getGameType()));
        rpt.setGameType(item.getGameType());
        rpt.setUserName(item.getUserName());
        rpt.setCurrency(item.getCurrency());
        rpt.setBetType(item.getWagerDetail() == null || item.getWagerDetail() == "" ? "" : getWagerDetails(item.getGameType(), item.getWagerDetail()));
        rpt.setTableNo(item.getGameCode());
        rpt.setRoundNo(item.getRoundNo());
        rpt.setSerialId(item.getSerialID());
        rpt.setResult(GameCodeConstants.getWinLoss(item.getPayoff()));
        rpt.setOpenResultDetail(JSON.toJSONString(setResultDetail(item)));//注单详情结果封装
        String status = GameCodeConstants.CONSTANT_ISPAID;
        /**转成北京时间**/
        rpt.setOrderDate(DateConvert.convertAsiaDate(item.getOrderDate() == null ? item.getWagersDate() : item.getOrderDate()));
        /**小费类型**/
        if (gameType.equals(BBINGameTypeEnum.Enum_Tip.getKey())) {
            rpt.setTip(item.getBetAmount());
        }
        /**当游戏类型为奖池类型时**/
        if (GameCodeConstants.CONSTANT_BBIN_GAMETYPE_JACKPOT.equals(item.getGameType())) {
            rpt.setJackpotBet(item.getBetAmount());
            rpt.setJackpotPayout(item.getPayoff());
        } else {
            rpt.setBet(item.getBetAmount());
            rpt.setPayout(item.getPayoff());
            rpt.setValidBet(item.getCommissionable());
        }

        /**电子类型**/
        if (gameType.equals(BBINGameTypeEnum.Enum_Slot.getKey())) {
            status = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), gameType, item.getResult());
            rpt.setStatus(status);
            if (GameCodeConstants.CONSTANT_NOPAID.equals(status)) {
                rpt.setResult(null);
            }
        }
        /**视讯类型**/
        if (BBINGameTypeEnum.Enum_Live.getKey().equals(gameType)) {
            if (item.getCommissionable().compareTo(BigDecimal.ZERO) == 0) {
                rpt.setResult(GameCodeConstants.CONSTANT_TIE);
            }
            status = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), gameType, item.getResultType());
            if (!status.isEmpty()) {
                rpt.setStatus(status);
                rpt.setResult(null);
            } else {
                rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
            }
        }
        /**体育类型**/
        if (gameType.equals(BBINGameTypeEnum.Enum_Sport.getKey())) {
            if (item.getOrderDate() != null) {
                rpt.setOrderDate(DateConvert.convertAsiaDate(item.getOrderDate()));
            }
            status = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), gameType, item.getResult());
            rpt.setStatus(status);
            if (GameCodeConstants.CONSTANT_NOPAID.equals(status)) {
                rpt.setResult(null);
            }
        }
        /**彩票 有效投注 如果派彩不为0，则取投注金额**/
        if (gameType.equals(BBINGameTypeEnum.Enum_Lottery.getKey())) {
            rpt.setValidBet(item.getBetAmount());
            if (item.getPayoff() == null || item.getPayoff().compareTo(BigDecimal.ZERO) == 0) {
                rpt.setValidBet(BigDecimal.ZERO);
            }
            if (item.getOrderDate() != null) {
                rpt.setOrderDate(DateConvert.convertAsiaDate(item.getOrderDate()));
            }
            status = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Status_Code.getKey(), gameType, item.getResult());
            rpt.setStatus(status);
            if (!GameCodeConstants.CONSTANT_ISPAID.equals(status)) {
                rpt.setResult(null);
            }
        }
        /**转成北京时间**/
        rpt.setBetTime(DateConvert.convertAsiaDate(item.getWagersDate()));
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, item.getOrigin()));
        return rpt;
    }

    /***
     * 根据游戏id获取游戏玩法，玩法说明："玩法,賠率,下注,派彩*玩法,賠率,下注,派彩*玩法,賠率,下注,派彩"
     * @param gameid
     * @param str
     * @return
     */
    private static String getWagerDetails(String gameid, String str) {
        StringBuffer rs = new StringBuffer();
        String[] strlist = str.split("\\*");
        try {
            for (String s : strlist) {
                String[] slist = s.split(",");
                rs.append(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_BBIN.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), gameid, slist[0]));
                rs.append("$$$");
                rs.append(slist[1]);
                rs.append("$$$");
                rs.append(slist[2]);
                rs.append("$$$");
                rs.append(slist[3]);
                rs.append("%%%");
            }
        } catch (Exception e) {
            XxlJobLogger.log(e.getMessage());
        }
        return rs.toString().substring(0, rs.length() - 3);
    }

    /****
     * 判断值是否存在
     * @param arr
     * @param result
     * @return
     */
    public static boolean checkResult(String[] arr, String result) {
        return Arrays.asList(arr).contains(result);
    }

    /****
     * 封装注单详情
     * @param bbinBetLog
     * @return OpenResultModel
     */
    private static OpenResultModel setResultDetail(BbinBetLog bbinBetLog) {
        switch (PlatFromEnum.Enum_BBIN.getValue().concat(bbinBetLog.getGameType()).toLowerCase()) {
            case GameTypeConstant.BBIN_3017:
            case GameTypeConstant.BBIN_3001:
                return BbinOpenResultProcessor.analysisBBIN3001(bbinBetLog);
            case GameTypeConstant.BBIN_3003:
                return BbinOpenResultProcessor.analysisBBIN3003(bbinBetLog);
            case GameTypeConstant.BBIN_3005:
                return BbinOpenResultProcessor.analysisBBIN3005(bbinBetLog);
            case GameTypeConstant.BBIN_3006:
                return BbinOpenResultProcessor.analysisBBIN3006(bbinBetLog);
            case GameTypeConstant.BBIN_3007:
                return BbinOpenResultProcessor.analysisBBIN3007(bbinBetLog);
            case GameTypeConstant.BBIN_3008:
                return BbinOpenResultProcessor.analysisBBIN3008(bbinBetLog);
            case GameTypeConstant.BBIN_3010:
                return BbinOpenResultProcessor.analysisBBIN3010(bbinBetLog);
            case GameTypeConstant.BBIN_3011:
                return BbinOpenResultProcessor.analysisBBIN3011(bbinBetLog);
            case GameTypeConstant.BBIN_3012:
                return BbinOpenResultProcessor.analysisBBIN3012(bbinBetLog);
            case GameTypeConstant.BBIN_3014:
                return BbinOpenResultProcessor.analysisBBIN3014(bbinBetLog);
            case GameTypeConstant.BBIN_3015:
                return BbinOpenResultProcessor.analysisBBIN3015(bbinBetLog);
            case GameTypeConstant.BBIN_3016:
                return BbinOpenResultProcessor.analysisBBIN3016(bbinBetLog);
            case GameTypeConstant.BBIN_3018:
                return BbinOpenResultProcessor.analysisBBIN3018(bbinBetLog);
        }
        return null;
    }
}
