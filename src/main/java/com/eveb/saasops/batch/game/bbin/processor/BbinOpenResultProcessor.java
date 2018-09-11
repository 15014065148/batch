package com.eveb.saasops.batch.game.bbin.processor;

import com.eveb.saasops.batch.game.OpenResultModel;
import com.eveb.saasops.batch.game.bbin.domain.BbinBetLog;
import com.eveb.saasops.batch.sys.constants.GameTypeConstant;
import tk.mybatis.mapper.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class BbinOpenResultProcessor {
    /**
     * BBIN 百家乐(3001) 结果解析
     *
     * @param bbinBetLog C.10,D.9*D.11,C.4 | 6,7
     * @return
     */
    public static OpenResultModel analysisBBIN3001(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] lotteryResultStrs = bbinBetLog.getResult().split(",");
            /**开奖结果*/
            map.put("zResult", lotteryResultStrs[0]);
            map.put("xResult", lotteryResultStrs[1]);
        }
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split("\\*");
            /**开奖详情*/
            for (int i = 0; i < openResultStrs.length; i++) {
                String[] strs = openResultStrs[i].split(",");
                for (int j = 0; j < strs.length; j++) {
                    switch (i) {
                        case 0: {
                            map.put("zOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                        case 1: {
                            map.put("xOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                    }
                }
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3001, map);
    }


    /**
     * BBIN 龙虎斗(3003) 结果解析
     *
     * @param bbinBetLog S.7*H.10 | 13,13
     * @return
     */
    public static OpenResultModel analysisBBIN3003(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] lotteryResultStrs = bbinBetLog.getResult().split(",");
            /**开奖结果*/
            map.put("dragonpointResult", lotteryResultStrs[0]);
            map.put("tigerpointResult", lotteryResultStrs[1]);
        }
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split("\\*");
            /**开奖详情*/
            map.put("dragonpoint", openResultStrs[0]);
            map.put("tigerpoint", openResultStrs[1]);
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3003, map);
    }

    /**
     * BBIN 温州牌九(3006) 结果解析
     *
     * @param bbinBetLog S.6,H.9*D.8,H.8*D.5,S.1*C.10,H.5 | P1L, P2L, P3W
     * @return
     */
    public static OpenResultModel analysisBBIN3006(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] lotteryResultStrs = bbinBetLog.getResult().split(",");
            /**开奖结果*/
            for (int i = 0; i < lotteryResultStrs.length; i++) {
                map.put("result" + (i + 1), lotteryResultStrs[i]);
            }
        }
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultDetialStrs = bbinBetLog.getCard().split("\\*");
            /**开奖详情*/
            for (int i = 0; i < openResultDetialStrs.length; i++) {
                String[] strs = openResultDetialStrs[i].split(",");
                for (int j = 0; j < strs.length; j++) {
                    switch (i) {
                        /**庄家牌*/
                        case 0: {
                            map.put("zOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                        /**顺门牌*/
                        case 1: {
                            map.put("sOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                        /**出门牌*/
                        case 2: {
                            map.put("cOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                        /**到门牌*/
                        case 3: {
                            map.put("dOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                    }
                }
            }
        }

        return new OpenResultModel(GameTypeConstant.BBIN_3006, map);
    }

    /**
     * BBIN 轮盘(3007) 结果解析
     *
     * @param bbinBetLog 26 | 26
     * @return
     */
    public static OpenResultModel analysisBBIN3007(BbinBetLog bbinBetLog) {

        Map<String, String> map = new LinkedHashMap<>();
        map.put("rouResult", bbinBetLog.getCard());
        return new OpenResultModel(GameTypeConstant.BBIN_3007, map);
    }

    /**
     * BBIN 骰子(3008) 结果解析
     *
     * @param bbinBetLog 2,3,5
     * @return
     */
    public static OpenResultModel analysisBBIN3008(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split(",");
            for (int i = 0; i < openResultStrs.length; i++) {
                map.put("sResult" + (i + 1), openResultStrs[i]);
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3008, map);
    }

    /**
     * BBIN 德州扑克(3010) 结果解析
     *
     * @param bbinBetLog H.11,S.9*D.3,C.7*C.13,S.4,C.9,H.4,S.11 | D.1,S.1,D.12,S.10,H.8(OnePair)
     * @return
     */
    public static OpenResultModel analysisBBIN3010(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        /**开牌结果*/
        map.put("result", (StringUtil.isNotEmpty(bbinBetLog.getResult()) && bbinBetLog.getResult().contains("(")) ? bbinBetLog.getResult().substring(bbinBetLog.getResult().indexOf("(") + 1, bbinBetLog.getResult().indexOf(")")) : "");
        /**开牌详情*/
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split("\\*");
            for (int i = 0; i < openResultStrs.length; i++) {
                String[] strs = openResultStrs[i].split(",");
                for (int j = 0; j < strs.length; j++) {
                    switch (i) {
                        case 0: {
                            map.put("zOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                        case 1: {
                            map.put("xOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                        case 2: {
                            map.put("gOpenResult" + (j + 1), strs[j]);
                            break;
                        }
                    }
                }
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3010, map);
    }

    /**
     * BBIN 色碟(3011) 结果解析
     *
     * @param bbinBetLog 2 White 2 Red
     * @return
     */
    public static OpenResultModel analysisBBIN3011(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            if (bbinBetLog.getResult().contains("W")) {
                map.put("whiteCounts", bbinBetLog.getResult().charAt(0) + "");
            }
            if (bbinBetLog.getResult().contains("R")) {
                map.put("redCounts", bbinBetLog.getResult().charAt(0) + "");
            }
            if (bbinBetLog.getResult().contains("W") && bbinBetLog.getResult().contains("R")) {
                map.put("redCounts", bbinBetLog.getResult().charAt(6) + "");
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3011, map);
    }

    /**
     * BBIN 牛牛(3012) 结果解析
     *
     * @param bbinBetLog D.4,D.7,H.6,S.4,H.1*C.1,C.10,H.3,D.8,D.9*D.12,D.11,H.5,C.13,C.4*S.1,D.10,C.11,D.3,S.8 | Bull2,No Bull,Bull5,No Bull
     * @return
     */
    public static OpenResultModel analysisBBIN3012(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split("\\*");
            /**开奖详情*/
            for (int i = 0; i < openResultStrs.length; i++) {
                String[] strs = openResultStrs[i].split(",");
                for (int j = 0; j < strs.length; j++) {
                    if (i == 0) {
                        map.put("zOpenResult" + (j + 1), strs[j]);
                        continue;
                    }
                    map.put("xOpenResult" + (i + 1) + (j + 1), strs[j]);
                }
            }
        }
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] lotteryResultStrs = bbinBetLog.getResult().split(",");
            /**开奖结果*/
            for (int i = 0; i < lotteryResultStrs.length; i++) {
                if (i == 0) {
                    map.put("zResult" + (i + 1), lotteryResultStrs[i]);
                    continue;
                }
                map.put("xResult" + (i + 1), lotteryResultStrs[i]);
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3012, map);
    }

    /**
     * BBIN 无限21点(3014) 结果解析
     *
     * @param bbinBetLog 23,19,
     * @return
     */
    //TODO: 2018/7/24 没有数据样板
    public static OpenResultModel analysisBBIN3014(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] resultStrs = bbinBetLog.getResult().split(",");
            for (int i = 0; i < resultStrs.length; i++) {
                map.put("result" + (i + 1), resultStrs[i]);
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3014, map);
    }

    /**
     * BBIN 番摊(3015) 结果解析
     *
     * @param bbinBetLog 6
     * @return
     */
    public static OpenResultModel analysisBBIN3015(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("buttonCounts", bbinBetLog.getResult());
        return new OpenResultModel(GameTypeConstant.BBIN_3015, map);
    }

    /**
     * BBIN 三公(3005) 结果解析
     *
     * @param bbinBetLog S.7,S.5,D.2*S.4,C.3,S.10*D.3,C.4,D.12*H.7,C.6,H.5 | P7,P9,7,P9
     * @return
     */
    public static OpenResultModel analysisBBIN3005(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] lotteryResultStrs = bbinBetLog.getResult().split(",");
            for (int i = 0; i < lotteryResultStrs.length; i++) {
                if (i == 0) {
                    map.put("zResult", lotteryResultStrs[i]);
                    continue;
                }
                map.put("xResult" + i, lotteryResultStrs[i]);
            }
        }
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split("\\*");
            for (int i = 0; i < openResultStrs.length; i++) {
                String[] strs = openResultStrs[i].split(",");
                for (int j = 0; j < strs.length; j++) {
                    if (i == 0) {
                        map.put("zOpenResult" + (j + 1), strs[j]);
                        continue;
                    }
                    map.put("xOpenResult" + (i) + (j + 1), strs[j]);
                }
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3005, map);
    }

    /**
     * BBIN 鱼虾蟹(3016) 结果解析
     *
     * @param bbinBetLog 3,4,6
     * @return
     */
    public static OpenResultModel analysisBBIN3016(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] resultStrs = bbinBetLog.getResult().split(",");
            for (int i = 0; i < resultStrs.length; i++) {
                map.put("result" + (i + 1), resultStrs[i]);
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3016, map);
    }

    /**
     * BBIN 炸金花(3018) 结果解析
     *
     * @param bbinBetLog D.5,C.3,S.10*S.7,C.12,S.3
     *                   HighCard-10,HighCard-Q
     * @return
     */
    public static OpenResultModel analysisBBIN3018(BbinBetLog bbinBetLog) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(bbinBetLog.getResult())) {
            String[] resultStrs = bbinBetLog.getResult().split(",");
            map.put("dResult", resultStrs[0]);
            map.put("pResult", resultStrs[1]);
        }
        if (StringUtil.isNotEmpty(bbinBetLog.getCard())) {
            String[] openResultStrs = bbinBetLog.getCard().split("\\*");
            String[] dragonOpenResultStr = openResultStrs[0].split(",");
            String[] phoenixOpenResultStr = openResultStrs[1].split(",");
            /**龙开奖详情*/
            for (int i = 0; i < dragonOpenResultStr.length; i++) {
                map.put("dOpenResult" + i, dragonOpenResultStr[i]);
            }
            /**凤开奖详情*/
            for (int i = 0; i < phoenixOpenResultStr.length; i++) {
                map.put("pOpenResult" + i, phoenixOpenResultStr[i]);
            }
        }
        return new OpenResultModel(GameTypeConstant.BBIN_3018, map);
    }
}
