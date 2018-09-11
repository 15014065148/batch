package com.eveb.saasops.batch.comparator.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eveb.saasops.batch.comparator.comparemodel.PtcrawlerModel;
import com.eveb.saasops.batch.comparator.querySql.ptcrawler.PtcrawlerQuery;
import com.eveb.saasops.batch.comparator.querySql.report.ReportQuery;
import com.eveb.saasops.batch.sys.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by William on 2018/2/14.
 */
@Service
@Slf4j
public class ComparatorService {

    public Map<String,PtcrawlerModel> mapOpsiteUserName =new HashMap<>();

    public Map<String,PtcrawlerModel> mapOpsiteKioks =new HashMap<>();

    List<String> apiPrefixs =new LinkedList<>();

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private ReportQuery reportQuery;

    @Resource(name = "restClient_1")
    private RestClient restClient ;

    @Autowired
    private PtcrawlerQuery ptcrawlerQuery;

    public void getReportOrderByUserName(String dateFrom, String dateTo, String platform, String apiPrefix) throws IOException {
        String query= reportQuery.getReportOrderByUserName(dateFrom,dateTo,platform,apiPrefix);
        Response response = restClient.performRequest("POST", "report/rpt_bet_rcd/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(query, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        mapOpsiteUserName.clear();
        for(Object obj: (JSONArray)((Map) ((Map) map.get("aggregations")).get("username")).get("buckets"))
        {
            setMaps(mapOpsiteUserName,obj);
        }
    }



    public void getReportOrderByKioks(String dateFrom, String dateTo,String platform) throws IOException {
        String query= reportQuery.getReportOrderByKioks(dateFrom,dateTo,platform);
        Response response = restClient.performRequest("POST", "report/rpt_bet_rcd/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(query, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        mapOpsiteKioks.clear();
        for(Object obj: (JSONArray)((Map) ((Map) map.get("aggregations")).get("Kiosk")).get("buckets"))
        {
            setMaps(mapOpsiteKioks,obj);
        }
    }

    public String getPTDataByTime (String date) throws IOException {
        String query = ptcrawlerQuery.getPTDataByTime(date);
        Response response = restClient.performRequest("POST", "ptcrawler/pt_data/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(query, ContentType.APPLICATION_JSON));
        Map<String,PtcrawlerModel> mapKioks =new HashMap<>();
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        List<HashMap<String,Object>> list=((List)(((Map)(map.get("hits"))).get("hits")));
        for (HashMap mapObj : list){
            Object data = mapObj.get("_source");
        }
        return null;
    }



    private void setMaps(Map<String,PtcrawlerModel> mapOpsite,Object obj){
        PtcrawlerModel ptcrawlerModel =new PtcrawlerModel();
        ptcrawlerModel.setBets(new BigDecimal(((Map)((JSONObject)obj).get("bets")).get("value").toString()));
        ptcrawlerModel.setActivePlayers(Integer.valueOf(((Map)((JSONObject)obj).get("Active Players")).get("value").toString()));
        ptcrawlerModel.setProgressiveShare(new BigDecimal(((Map)((JSONObject)obj).get("Progressive Share")).get("value").toString()));
        ptcrawlerModel.setProgressiveWins(new BigDecimal(((Map)((JSONObject)obj).get("Progressive Wins")).get("value").toString()));
        ptcrawlerModel.setWins(new BigDecimal(((Map)((JSONObject)obj).get("Wins")).get("value").toString()));
        mapOpsite.put (((JSONObject)obj).get("key").toString(), ptcrawlerModel);
    }

}
