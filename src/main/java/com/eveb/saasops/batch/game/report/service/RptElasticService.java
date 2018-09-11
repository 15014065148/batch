package com.eveb.saasops.batch.game.report.service;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.game.report.domain.RptBetDayModel;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class RptElasticService {

    @Autowired
    private ElasticSearchService conn;

    /***
     * 插入ElasticSearch
     * @param object
     * @throws Exception
     */
    public void insert(RptBetModel object)throws Exception
    {
        IndexResponse response=conn.client.prepareIndex("report","rpt_bet_rcd",object.getId().toString()).setSource((Map)JSON.toJSON(object)).execute().get();
    }

    public void insertList(List<RptBetModel> list) throws Exception {
        BulkRequestBuilder bulkRequest = conn.client.prepareBulk();
        int commintcount = list.size();
        int commintmax = ApplicationConstant.CONSTANT_COMMINTMAX;
        for (RptBetModel bet : list) {
            Map map = (Map) JSON.toJSON(bet);
            bulkRequest.add(conn.client.prepareIndex("report","rpt_bet_rcd",bet.getId()).setSource(map));
            commintcount--;
            commintmax--;
            // 每1000条提交一次
            if (commintmax == 0 || commintcount == 0) {
                bulkRequest.execute().actionGet();
                commintmax = ApplicationConstant.CONSTANT_COMMINTMAX;
            }
        }
//        bulkRequest.execute().actionGet();
    }

    public RptBetModel getRptBet(Long id)
    {
        try {
        GetResponse response =conn.client.prepareGet("report","rpt_bet_rcd",id.toString()).execute().actionGet();
            if(response.getSourceAsString()==null)return null;
            return JSON.parseObject(response.getSourceAsString(),RptBetModel.class);
        }catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    public RptBetModel getBetLog(String type,Long id)
    {
        try {
            GetResponse response =conn.client.prepareGet("saasops",type,id.toString()).execute().actionGet();
            if(response.getSourceAsString()==null)return null;
            return JSON.parseObject(response.getSourceAsString(),RptBetModel.class);
        }catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<RptBetDayModel> aggsRpt(String starttime,String endtime,String sitePrefix)
    {
        List<RptBetDayModel> rptlist =new ArrayList<>();

        AggregatorFactories.Builder builder=new AggregatorFactories.Builder();
        builder.addAggregator(AggregationBuilders.sum("bet").field("bet"));
        builder.addAggregator(AggregationBuilders.sum("validbet").field("validbet"));
        builder.addAggregator(AggregationBuilders.sum("reward").field("reward"));

        TermsAggregationBuilder agg= AggregationBuilders.terms("username").field("username")
                .subAggregation(AggregationBuilders.terms("all_platform").field("platform")
                        .subAggregation(AggregationBuilders.terms("all_gamename").field("gametype").subAggregations(builder))
                );
        SearchRequestBuilder searchRequestBuilder=conn.client.prepareSearch("report");
        searchRequestBuilder.setTypes("rpt_bet_rcd");
        searchRequestBuilder.setSize(0);
        searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("bettime").gte(starttime).lte(endtime)).must(QueryBuilders.termsQuery("sitePrefix",sitePrefix)));
        searchRequestBuilder.addAggregation(agg);
//        log.info(searchRequestBuilder.toString());
        SearchResponse searchResponse=searchRequestBuilder.execute().actionGet();
        Aggregations aggregations=searchResponse.getAggregations();
        List<Aggregation> agglist=  aggregations.asList();
        for (Aggregation aggterms:agglist) {
            Terms terms=(Terms)aggterms;
            for (Terms.Bucket bucket:terms.getBuckets()) {
                /**用户名**/
                String username=bucket.getKey().toString();
                List<Aggregation> list=bucket.getAggregations().asList();
                for (Aggregation aggregation:list) {
                    Terms childterms= (Terms) aggregation;
                    for (Terms.Bucket chilebucket:childterms.getBuckets()) {
                        /**游戏平台**/
                        String platform=chilebucket.getKey().toString();
                        List<Aggregation> childlist=chilebucket.getAggregations().asList();
                        for (Aggregation a:childlist)
                        {
                            Terms t=(Terms)a;
                            for (Terms.Bucket b:t.getBuckets()) {
                                /**游戏类型**/
                                RptBetDayModel dayModel=new RptBetDayModel();
                                dayModel.setUsername(username);
                                dayModel.setPlatform(platform);
                                dayModel.setGametype(b.getKey().toString());
                                Long quantity=b.getDocCount();
                                /**记录数**/
                                dayModel.setQuantity(quantity.intValue());
                                List<Aggregation> clist=b.getAggregations().asList();
                                for (Aggregation a1:clist)
                                {
                                    InternalSum t1=(InternalSum)a1;
                                    switch (t1.getName())
                                    {
                                        case "bet":
                                            dayModel.setBet(new BigDecimal(t1.getValue()));
                                            break;
                                        case "validbet":
                                            dayModel.setValidbet(new BigDecimal(t1.getValue()));
                                            break;
                                        case "reward":
                                            dayModel.setPayout(new BigDecimal(t1.getValue()));
                                            break;
                                    }
                                }
                                rptlist.add(dayModel);
                            }
                        }
                    }
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
    public BigDecimal aggsTip(String startDay,List values)
    {
        BigDecimal rs=new BigDecimal(0.00);
        TermsAggregationBuilder agg=AggregationBuilders.terms("userName").field("userName").
                subAggregation(AggregationBuilders.sum("betAmount").field("betAmount"));

        QueryBuilder quers=QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("wagersDate").gte(startDay+"T00:00:00.000Z").lte(startDay+"T23:59:59.000Z"))
                .must(QueryBuilders.termsQuery("gameType",values));

        SearchRequestBuilder searchRequestBuilder=conn.client.prepareSearch("bbin");
        searchRequestBuilder.setTypes("log_bet_rcd_bbin_tip");
        searchRequestBuilder.setSize(0);
        searchRequestBuilder.setQuery(quers);
        searchRequestBuilder.addAggregation(agg);
//        log.info(searchRequestBuilder.toString());
        SearchResponse response=searchRequestBuilder.execute().actionGet();
        Aggregations aggregations=response.getAggregations();
        List<Aggregation> agglist=aggregations.asList();
        for (Aggregation aggregation:agglist) {
            Terms terms=(Terms) aggregation;
            for (Terms.Bucket buket:terms.getBuckets()) {
                for (Aggregation s:buket.getAggregations().asList()) {
                    InternalSum sum=(InternalSum)s;
                    rs=new BigDecimal(sum.getValue());
                }
            }
        }
        return rs;
    }
}
