package com.eveb.saasops.batch.game.report.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.game.report.domain.RptBetDayModel;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.service.ElasticSearchReadService;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.mapper.SysMapper;
import com.eveb.saasops.batch.sys.util.BigDecimalMath;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class RptElasticRestService {

    @Autowired
    private ElasticSearchService conn;
    @Autowired
    private ElasticSearchReadService conn_read;
    @Autowired
    private SysMapper sysMapper;

    /***
     * 批量添加
     * @param insstr
     * @throws Exception
     */
    public void insertList(String insstr) throws Exception {
        if(insstr.isEmpty()){return;}
        HttpEntity entity = new NStringEntity(insstr, ContentType.APPLICATION_JSON);
        conn.restClient.performRequest("POST", "/_bulk", Collections.singletonMap("pretty", "true"), entity);
    }

    /**
     * 先进行插入操作，如果已经存再则进行修改
     * @param list
     * @throws Exception
     */
    public void insertOrUpdateList(List<RptBetModel> list) throws Exception {
        StringBuffer string = new StringBuffer();
        for (RptBetModel object : list) {
            if (object == null) {
                continue;
            }
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.REPORT_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.REPORT_TYPE + "\", \"_id\" : \"" + object.getId() + "\" }}");
            string.append("\n");
            string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            string.append("\n");
        }
        if (list.size() > 0&& string.length()>0) {
            HttpEntity entity = new NStringEntity(string.toString(), ContentType.APPLICATION_JSON);
            conn.restClient.performRequest("POST", "/_bulk", Collections.singletonMap("pretty", "true"), entity);
        }
    }

    /****
     * 每五分钟生成统计数据
     * @param starttime
     * @param endtime
     * @param siteCode
     * @return
     * @throws Exception
     */
    public List<RptBetDayModel> aggsRpt(String starttime,String endtime,String siteCode)throws Exception {
        /***获取该站点下所有api前缀***/
        List<String> predixs = sysMapper.getApiPrefixBySiteCode(siteCode);
        List<RptBetDayModel> rptlist = new ArrayList<>();
        AggregatorFactories.Builder builder = new AggregatorFactories.Builder();
        builder.addAggregator(AggregationBuilders.sum("bet").field("bet"));
        builder.addAggregator(AggregationBuilders.sum("validBet").field("validBet"));
        builder.addAggregator(AggregationBuilders.sum("payout").field("payout"));
        builder.addAggregator(AggregationBuilders.sum("jackpotBet").field("jackpotBet"));
        builder.addAggregator(AggregationBuilders.sum("jackpotPayout").field("jackpotPayout"));
        TermsAggregationBuilder agg = AggregationBuilders.terms("userName").field("userName").size(ElasticSearchConstant.SEARCH_COUNT)
                .subAggregation(AggregationBuilders.terms("all_platform").field("platform").size(ElasticSearchConstant.SEARCH_COUNT)
                        .subAggregation(AggregationBuilders.terms("all_gamename").field("gameType").size(ElasticSearchConstant.SEARCH_COUNT).subAggregations(builder)));
        /**使用TransferClient 生成查询语句**/
        SearchRequestBuilder searchRequestBuilder = conn.client.prepareSearch("report");
        searchRequestBuilder.setTypes(ElasticSearchConstant.REPORT_TYPE);
        searchRequestBuilder.setSize(0);
        searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("orderDate").gte(starttime).lt(endtime)).must(QueryBuilders.termsQuery("sitePrefix", toLowerCase(predixs))));
        searchRequestBuilder.addAggregation(agg);
        String searchstr = searchRequestBuilder.toString().replace(",{\"_key\":\"asc\"}", "");
        log.info(String.format("****************%s站点统计语句********************", siteCode));
        log.info(searchstr);
        Response response = conn_read.restClient_Read.performRequest("POST", "/" +ElasticSearchConstant.REPORT_INDEX + "/" + ElasticSearchConstant.REPORT_TYPE + "/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(searchstr, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        for (Object json : ((JSONArray) ((Map) ((Map) map.get("aggregations")).get("userName")).get("buckets"))) {
            Map objmap = (Map) json;
            for (Object pfObj : (JSONArray) ((Map) objmap.get("all_platform")).get("buckets")) {
                Map pfmap = (Map) pfObj;
                for (Object gameObj : (JSONArray) (((Map) ((Map) pfmap.get("all_gamename"))).get("buckets"))) {
                    Map gamemap = (Map) gameObj;
                    RptBetDayModel rpt = new RptBetDayModel();
                    rpt.setUsername(objmap.get("key").toString());
                    /**存大写*/
                    rpt.setPlatform(pfmap.get("key").toString().toUpperCase());
                    rpt.setGametype((String) gamemap.get("key"));
                    rpt.setQuantity((Integer) gamemap.get("doc_count"));
                    /**向下取整，均为存储两位小数**/
                    rpt.setBet(BigDecimalMath.formatDownLimit((BigDecimal) ((Map) gamemap.get("bet")).get("value")));
                    rpt.setPayout(BigDecimalMath.formatDownLimit((BigDecimal) ((Map) gamemap.get("payout")).get("value")));
                    rpt.setValidbet(BigDecimalMath.formatDownLimit((BigDecimal) ((Map) gamemap.get("validBet")).get("value")));
                    rpt.setJackpotBet(BigDecimalMath.formatDownLimit((BigDecimal) ((Map) gamemap.get("jackpotBet")).get("value")));
                    rpt.setJackpotPayout(BigDecimalMath.formatDownLimit((BigDecimal) ((Map) gamemap.get("jackpotPayout")).get("value")));
                    rptlist.add(rpt);
                }
            }
        }
        return rptlist;
    }

    /****
     * 获取波音的小费统计
     * @param startDay
     * @param values
     * @return
     */
    public BigDecimal aggsTip(String startDay,List values)throws Exception
    {
        BigDecimal rs = new BigDecimal(0.00);
        TermsAggregationBuilder agg = AggregationBuilders.terms("userName").field("userName").
                subAggregation(AggregationBuilders.sum("betAmount").field("betAmount"));

        QueryBuilder quers = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("wagersDate").gte(startDay + "T00:00:00.000Z").lte(startDay + "T23:59:59.000Z"))
                .must(QueryBuilders.termsQuery("gameType", values));
        SearchRequestBuilder searchRequestBuilder = conn.client.prepareSearch("bbin");
        searchRequestBuilder.setTypes(ElasticSearchConstant.BBIN_INDEX_TIP);
        searchRequestBuilder.setSize(0);
        searchRequestBuilder.setQuery(quers);
        searchRequestBuilder.addAggregation(agg);
        log.info(searchRequestBuilder.toString());
        Response response = conn.restClient.performRequest("GET", "bbin/"+ElasticSearchConstant.BBIN_TYPE_TIP+"/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(searchRequestBuilder.toString(), ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        if (((JSONArray) ((Map) ((Map) map.get("aggregations")).get("userName")).get("buckets")).size() > 0) {
            return (BigDecimal) ((Map) ((Map) ((JSONArray) ((Map) ((Map) map.get("aggregations")).get("userName")).get("buckets")).get(0)).get("betAmount")).get("value");
        }
        return rs;
    }

    public List toLowerCase(List list)
    {
        List newList = new ArrayList();
        Iterator it = list.iterator();
        while(it.hasNext()){
            newList.add(String.valueOf(it.next()).toLowerCase());
        }
        return newList;
    }
//通过线路获取OG数据最大的vendorid来进行数据拉取
    public Map getMaxVendorid()throws Exception{
        TermsAggregationBuilder agg = AggregationBuilders.terms("apiPrefix").field("apiPrefix").
                subAggregation(AggregationBuilders.max("vendorId").field("vendorId"));
        SearchRequestBuilder searchRequestBuilder = conn.client.prepareSearch("report");
        searchRequestBuilder.addAggregation(agg);
        Response response = conn.restClient.performRequest("GET", "/" +ElasticSearchConstant.OG_INDEX + "/"+ElasticSearchConstant.OG_TYPE+"/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(searchRequestBuilder.toString(), ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        return map;
    }
}
