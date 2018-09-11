package com.eveb.saasops.batch.game.bbin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.game.bbin.domain.BbinBetLog;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 17:25 2017/12/22
 **/
@Slf4j
@Service
public class BBINElasticRestService {

    @Autowired
    private ElasticSearchService conn;

//    public void insert(String index,String type, BbinBetLog object) throws Exception {
//        HttpEntity entity=new NStringEntity(JSON.toJSONStringWithDateFormat(object,ApplicationConstant.CONSTANT_DATEFORMAT), ContentType.APPLICATION_JSON);
//        conn.restClient.performRequest("POST","/"+index.toLowerCase()+"/"+type+"/"+object.getWagersID(), Collections.singletonMap("pretty", "true"),entity);
//    }

//    public void insertList(String index,String type, List<BbinBetLog> list) throws Exception {
//        StringBuffer string = new StringBuffer();
//        for (BbinBetLog object : list) {
//            string.append("{ \"create\" : { \"_index\" : \"" + index.toLowerCase() + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + object.getWagersID() + "\" }}");
//            string.append("\n");
//            string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
//            string.append("\n");
////            string.append("{ \"update\" : { \"_index\" : \"" + index.toLowerCase() + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + object.getWagersID() + "\" }}");
////            string.append("\n");
////            string.append("{ \"doc\" :"+JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")+"}");
////            string.append("\n");
//        }
//        if (list.size() > 0) {
//            HttpEntity entity = new NStringEntity(string.toString(), ContentType.APPLICATION_JSON);
//            conn.restClient.performRequest("POST", "/_bulk", Collections.singletonMap("pretty", "true"), entity);
//        }
//    }

    /**
     * 根据ID和类型获取记录
     *
     * @param type
     * @param id
     * @return
     */
    public BbinBetLog getBetLogRecord(String index, String type, Long id) throws Exception{
        SearchRequestBuilder searchRequestBuilder=conn.client.prepareSearch("report");
        searchRequestBuilder.setQuery(QueryBuilders.termsQuery("_id",id.toString()));
        searchRequestBuilder.setSize(1);
        Response response = conn.restClient.performRequest("GET", index.toLowerCase()+"/"+type+"/_search", Collections.singletonMap("_source", "true"),new NStringEntity(searchRequestBuilder.toString(),ContentType.APPLICATION_JSON));
        Map map=(Map)JSON.parse(EntityUtils.toString(response.getEntity()));
        JSONArray hits=((JSONArray)(((Map)map.get("hits")).get("hits")));
        if(hits.size()>0) {
            return JSON.parseObject(((Map)hits.get(0)).get("_source").toString(), BbinBetLog.class);
        }
        return null;
    }
}
