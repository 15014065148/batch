package com.eveb.saasops.batch.game.mg.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Service
public class MgElasticService {

    @Autowired
    private ElasticSearchService conn;

    public BigDecimal queryLog(String id)throws Exception {
        SearchRequestBuilder searchRequestBuilder = conn.client.prepareSearch(ElasticSearchConstant.MG_INDEX);
        searchRequestBuilder.setQuery(QueryBuilders.termsQuery("_id", id));
        Response response = conn.restClient.performRequest("POST", ElasticSearchConstant.REPORT_INDEX+"/"+ElasticSearchConstant.REPORT_TYPE+"/_search", Collections.singletonMap("_source", "true"), new NStringEntity(searchRequestBuilder.toString(), ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        JSONArray hits = ((JSONArray) (((Map) map.get("hits")).get("hits")));
        for (Object obj : hits) {
            Map objmap = (Map) obj;
            RptBetModel rpt = JSON.parseObject(objmap.get("_source").toString(), RptBetModel.class);
            return rpt.getPayout();
        }
        return BigDecimal.ZERO;
    }
}
