package com.eveb.saasops.batch.game.agin.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.game.OpenResultModel;
import com.eveb.saasops.batch.game.agin.domain.AginBetLogModel;
import com.eveb.saasops.batch.game.agin.domain.AginLiveCardResultModel;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.constants.GameTypeConstant;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.DateUtil;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AginLiveRptBetProcessor {
    @Autowired
    private ElasticSearchService conn;
    private String gameType = BBINGameTypeEnum.Enum_Live.getKey();//使用游戏分类代码

    public RptBetModel process(Object object) throws Exception {
        if (object == null) {
            return null;
        }
        AginBetLogModel model = (AginBetLogModel) object;
        RptBetModel rpt = new RptBetModel();
        rpt.setId(model.getBillNo().toString());
        rpt.setUserName(model.getPlayerName());
        rpt.setApiPrefix(model.getApiPrefix());
        rpt.setSitePrefix(model.getSitePrefix());
        rpt.setPlatform(PlatFromEnum.Enum_AGIN.getValue());
        rpt.setGameType(model.getGameType());
        String gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), gameType, model.getGameType());
        if (gamename.isEmpty()) {
            gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), model.getGameType());
        }
        if (model.getPlatformType().equalsIgnoreCase("SBTA")) {
            gamename = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Sport.getKey(), model.getGameType());
        }
        rpt.setGameName(gamename);
        rpt.setBet(model.getBetAmount());
        rpt.setBetType(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), model.getGameType(), model.getPlayType()).concat("$$$$$$").concat(model.getBetAmount() + "$$$").concat(model.getNetAmount() + ""));
        rpt.setSerialId(model.getGameCode());
        rpt.setTableNo(model.getTableCode());
        rpt.setValidBet(model.getValidBetAmount());
        rpt.setPayout(model.getNetAmount());
        //有效投注为零,和局
        if (model.getValidBetAmount().compareTo(BigDecimal.ZERO) == 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_TIE);
        } else if (model.getNetAmount().compareTo(BigDecimal.ZERO) > 0) {
            rpt.setResult(GameCodeConstants.CONSTANT_WIN);
        } else {
            rpt.setResult(GameCodeConstants.CONSTANT_LOST);
        }
        rpt.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rpt.setCurrency(model.getCurrency());
        rpt.setBetTime(DateConvert.convertAsiaDate(model.getBetTime()));
        rpt.setOrderDate(DateConvert.convertAsiaDate(model.getBetTime()));
        rpt.setDownloadTime(DateUtil.orderDate(new Date()));
        rpt.setOrigin(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AGIN.getValue(), CodeTypeEnum.Enum_Origin_Code.getKey(), GameCodeConstants.CONSTANT_CODE_DEVICETYPE, model.getDeviceType()));
        if (GameCodeConstants.CONSTANT_CODE_DATATYPE_LIVE.equals(model.getDataType())) {
            rpt.setBalanceBefore(model.getBeforeCredit());
            rpt.setBalanceAfter(model.getNetAmount().add(model.getBeforeCredit()));
        }
        setOpenResult(rpt, model);//设置注单详情
        return rpt;
    }

    //通过局号获取对应的牌面结果
    private void setOpenResult(RptBetModel rpt, AginBetLogModel model) {
        AginLiveCardResultModel aginLiveCardResultModel = new AginLiveCardResultModel();
        try {
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.termsQuery("gmcode", model.getGameCode().toLowerCase()));
            SearchRequestBuilder searchRequestBuilder = conn.client.prepareSearch("report");
            searchRequestBuilder.setQuery(builder);
            Response response = conn.restClient.performRequest("GET", "/" + ElasticSearchConstant.AGIN_INDEX_LIVECARDRESULT + "/" + ElasticSearchConstant.AGIN_TYPE_LIVECARDRESULT + "/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(searchRequestBuilder.toString(), ContentType.APPLICATION_JSON));
            Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
            JSONArray hits = ((JSONArray) (((Map) map.get("hits")).get("hits")));
            for (Object obj : hits) {
                Map objmap = (Map) obj;
                aginLiveCardResultModel = JSON.parseObject(objmap.get("_source").toString(), AginLiveCardResultModel.class);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("bankerPoint", (StringUtil.isNotEmpty(aginLiveCardResultModel.getBankerPoint())) ? aginLiveCardResultModel.getBankerPoint() : "");
        paraMap.put("playerPoint", (StringUtil.isNotEmpty(aginLiveCardResultModel.getPlayerPoint())) ? aginLiveCardResultModel.getPlayerPoint() : "");
        paraMap.put("dragonpoint", (StringUtil.isNotEmpty(aginLiveCardResultModel.getDragonpoint())) ? aginLiveCardResultModel.getDragonpoint() : "");
        paraMap.put("tigerpoint", (StringUtil.isNotEmpty(aginLiveCardResultModel.getTigerpoint())) ? aginLiveCardResultModel.getTigerpoint() : "");
        paraMap.put("openResult", (StringUtil.isNotEmpty(aginLiveCardResultModel.getCardlist())) ? aginLiveCardResultModel.getCardlist() : "");
        paraMap.put("gameType", (StringUtil.isNotEmpty(model.getGameType())) ? model.getGameType() : "");
        rpt.setOpenResultDetail(JSON.toJSONString(setResultDetail(paraMap)));
    }

    /****
     * 封装注单详情
     * @param paraMap
     * @return OpenResultModel
     */
    private static OpenResultModel setResultDetail(Map<String, String> paraMap) {
        switch (PlatFromEnum.Enum_AGIN.getValue().concat(paraMap.get("gameType")).toLowerCase()) {
            case GameTypeConstant.AGIN_BAC:
            case GameTypeConstant.AGIN_CBAC:
            case GameTypeConstant.AGIN_LINK:
            case GameTypeConstant.AGIN_LBAC:
            case GameTypeConstant.AGIN_SBAC:
                return AginOpenResultProcessor.analysisAGINBac(paraMap);
            case GameTypeConstant.AGIN_DT:
                return AginOpenResultProcessor.analysisAGINDt(paraMap);
            case GameTypeConstant.AGIN_SHB:
                return AginOpenResultProcessor.analysisAGINShb(paraMap);
            case GameTypeConstant.AGIN_ROU:
                return AginOpenResultProcessor.analysisAGINRou(paraMap);
            case GameTypeConstant.AGIN_NN:
                return AginOpenResultProcessor.analysisAGINNn(paraMap);
            case GameTypeConstant.AGIN_BJ:
                return AginOpenResultProcessor.analysisAGINBj(paraMap);
            case GameTypeConstant.AGIN_ZJH:
                return AginOpenResultProcessor.analysisAGINZjh(paraMap);
            default:
                return null;
        }
    }
}
