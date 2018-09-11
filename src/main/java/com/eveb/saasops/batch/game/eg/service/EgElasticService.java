package com.eveb.saasops.batch.game.eg.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class EgElasticService {

    @Autowired
    private ElasticSearchService conn;

    /***
     * 返回EV当前最大的VendorId
     * @return
     * @throws IOException
     */
    public String getEvBetLogMax(String apiPrefix) throws IOException {
        SearchRequestBuilder searchRequestBuilder = conn.client.prepareSearch("report");
        searchRequestBuilder.addSort("addTime", SortOrder.DESC);
        searchRequestBuilder.setSize(1);
        searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("apiPrefix", apiPrefix)));
        String str = searchRequestBuilder.toString();
        Response response = conn.restClient.performRequest("POST", ElasticSearchConstant.EG_INDEX+"/"+ElasticSearchConstant.EG_TYPE+"/_search", Collections.singletonMap("_source", "true"), new NStringEntity(str, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        if(((JSONArray)((Map)map.get("hits")).get("hits")).size()>0)
        {
            return ((Map)((JSONArray)((Map)map.get("hits")).get("hits")).get(0)).get("_id").toString();
        }
      return "0";
    }
}
