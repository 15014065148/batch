package com.eveb.saasops.batch.game.agin.processor;

import com.eveb.saasops.batch.game.OpenResultModel;
import com.eveb.saasops.batch.sys.constants.GameTypeConstant;
import tk.mybatis.mapper.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class AginOpenResultProcessor {
    /**
     * AGIN 百家乐(bac) 结果解析
     *
     * @param paraMap H.7,C.4,C.7;S.9,D.5,H.8
     * @return
     */
    public static OpenResultModel analysisAGINBac(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        /**开奖结果*/
        map.put("zResult", paraMap.get("bankerPoint"));
        map.put("xResult", paraMap.get("playerPoint"));
        if (StringUtil.isNotEmpty(paraMap.get("openResult"))) {
            String[] openResultStrs = paraMap.get("openResult").split(";");
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
        return new OpenResultModel(GameTypeConstant.AGIN_BAC, map);
    }

    /**
     * AGIN 龙虎(dt)  结果解析
     *
     * @param paraMap C.13;S.1
     * @return
     */
    public static OpenResultModel analysisAGINDt(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        /**开奖结果*/
        map.put("dragonpointResult", paraMap.get("dragonpoint"));
        map.put("tigerpointResult", paraMap.get("tigerpoint"));
        if (StringUtil.isNotEmpty(paraMap.get("openResult"))) {
            String[] openResultStrs = paraMap.get("openResult").split(";");
            /**开奖详情*/
            map.put("dragonpoint", openResultStrs[0]);
            map.put("tigerpoint", openResultStrs[1]);
        }
        return new OpenResultModel(GameTypeConstant.AGIN_DT, map);
    }

    /**
     * AGIN 骰宝(shb)  结果解析
     *
     * @param paraMap C.13;S.1
     * @return
     */
    public static OpenResultModel analysisAGINShb(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isNotEmpty(paraMap.get("openResult"))) {
            String[] openResultStrs = paraMap.get("openResult").split(",");
            /**开奖详情*/
            for (int i = 0; i < openResultStrs.length; i++) {
                map.put("sResult" + (i + 1), openResultStrs[i]);
            }
        }
        return new OpenResultModel(GameTypeConstant.AGIN_SHB, map);
    }

    /**
     * AGIN 轮盘(rou)  结果解析
     *
     * @param paraMap C.13;S.1
     * @return
     */
    public static OpenResultModel analysisAGINRou(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("rouResult", paraMap.get("openResult"));
        return new OpenResultModel(GameTypeConstant.AGIN_ROU, map);
    }

    /**
     * AGIN 牛牛(nn)  结果解析
     *
     * @param paraMap H.2;H.4,H.10,D.3,S.11,C.4;H.3,H.13,D.8,S.13,C.11;S.12,C.2,H.9,D.2,H.11;S.2,C.1,S.8,S.6,S.4
     *                20;1|2;1|5;1
     *                0
     * @return
     */
    public static OpenResultModel analysisAGINNn(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        /**庄家开奖结果*/
        map.put("zResult", paraMap.get("bankerPoint"));
        /**闲家开奖结果*/
        if (StringUtil.isNotEmpty(paraMap.get("playerPoint"))) {
            String[] xLotteryResultStrs = paraMap.get("playerPoint").split("\\|");
            for (int i = 0; i < xLotteryResultStrs.length; i++) {
                String[] strs = xLotteryResultStrs[i].split(";");
                map.put("xResult" + i, strs[0]);
            }
        }
        if (StringUtil.isNotEmpty(paraMap.get("openResult"))) {
            String[] openResultStrs = paraMap.get("openResult").split(";");
            l:
            for (int i = 0; i < openResultStrs.length; i++) {
                String[] sts = openResultStrs[i].split(",");
                for (int j = 0; j < sts.length; j++) {
                    switch (i) {
                        /**头牌*/
                        case 0: {
                            map.put("headOpenResult", openResultStrs[i]);
                            continue l;
                        }
                        /**庄家开奖详情*/
                        case 1: {
                            map.put("zOpenResult" + (j + 1), sts[j]);
                            break;
                        }
                        /**闲家1开奖详情*/
                        case 2: {
                            map.put("xOpenResult1" + (j + 1), sts[j]);
                            break;
                        }
                        /**闲家2开奖详情*/
                        case 3: {
                            map.put("xOpenResult2" + (j + 1), sts[j]);
                            break;
                        }
                        /**闲家3开奖详情*/
                        case 4: {
                            map.put("xOpenResult3" + (j + 1), sts[j]);
                            break;
                        }
                    }
                }
            }
        }
        return new OpenResultModel(GameTypeConstant.AGIN_NN, map);
    }


    /**
     * AGIN 21点(bj)  结果解析
     *
     * @param paraMap B:D.4 S.12 S.13;P1:H.12 D.7;P2:C.7 C.11;P3:H.12 D.9;P4:D.3 C.12;P5:D.10 S.10;P6:D.13 C.13;P7:S.5 H.2 H.11
     *                0,0,24
     *                0,0,0,17,0,1,0|0,0,0,17,0,1,0|0,0,0,19,0,1,0|0,0,0,13,0,1,0|3,0,0,20,0,1,0|3,0,0,20,0,1,0|0,0,0,17,0,1,0
     *                B:H.8 H.9;P1:C.1 S.13;P2:H.5 H.12;P3:D.1 S.13;P4:D.3 C.3 C.2 H.12;P5:S.9 C.12;P7:H.4 S.11
     *                0,0,1,21,0,1,0|0,5,0,15,0,0,0|0,0,1,21,0,1,0|3,0,0,18,0,1,0|0,0,0,19,0,1,0|0|0,0,0,14,0,0,0
     * @return
     */
    public static OpenResultModel analysisAGINBj(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        /**庄家开奖结果*/
        if (StringUtil.isNotEmpty(paraMap.get("bankerPoint"))) {
            map.put("BResult", paraMap.get("bankerPoint").split(",")[2]);
        }
        /**闲家开奖结果*/
        if (StringUtil.isNotEmpty(paraMap.get("playerPoint"))) {
            String[] xlotteryResultStrs = paraMap.get("playerPoint").split("\\|");
            for (int i = 0; i < xlotteryResultStrs.length; i++) {
                if (xlotteryResultStrs[i].split(",").length == 1) {
                    continue;
                }
                map.put("P" + (i + 1) + "Result", xlotteryResultStrs[i].split(",")[3]);
            }
        }
        if (StringUtil.isNotEmpty(paraMap.get("openResult"))) {
            String[] openResultStrs = paraMap.get("openResult").split(";");
            for (int i = 0; i < openResultStrs.length; i++) {
                String var=(i==0)?openResultStrs[i].substring(0, 2):openResultStrs[i].substring(0, 3);
                switch (var) {
                    case "B:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            /**庄家开奖详情*/
                            map.put("BOpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P1:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P1OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P2:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P2OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P3:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P3OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P4:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P4OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P5:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P5OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P6:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P6OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                    case "P7:": {
                        String[] strs = openResultStrs[i].substring(openResultStrs[i].indexOf(":") + 1).split(" ");
                        for (int j = 0; j < strs.length; j++) {
                            map.put("P7OpenResult" + (j + 1), strs[j]);
                        }
                        continue;
                    }
                }
            }
        }
        return new OpenResultModel(GameTypeConstant.AGIN_BJ, map);
    }

    /**
     * AGIN 炸金花(zjh)  结果解析
     *
     * @param paraMap C.1,C.11,C.3;C.2,C.6,H.4
     * @return
     */
    public static OpenResultModel analysisAGINZjh(Map<String, String> paraMap) {
        Map<String, String> map = new LinkedHashMap<>();
        /**龙开奖结果*/
        map.put("dResult", paraMap.get("bankerPoint"));
        /**凤开奖结果*/
        map.put("pResult", paraMap.get("playerPoint"));
        if (StringUtil.isNotEmpty(paraMap.get("openResult"))) {
            String[] openResultStrs = paraMap.get("openResult").split(";");
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
        return new OpenResultModel(GameTypeConstant.AGIN_ZJH, map);
    }
}
