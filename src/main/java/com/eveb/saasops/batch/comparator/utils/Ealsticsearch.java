package com.eveb.saasops.batch.comparator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Configuration
public class Ealsticsearch {

    public static RestClient restClient;
    public static TransportClient client;


    @Bean
    public RestClient restClient_1() throws Exception {
        Ealsticsearch e=new Ealsticsearch();
        KeyStore turestore = KeyStore.getInstance("jks");
        try (InputStream is = e.getClass().getResourceAsStream("/key/elastic.jks")) {
            turestore.load(is,"123456".toCharArray());
        }
        turestore.load(null, null);
        SSLContextBuilder sslBuilder= SSLContexts.custom().loadTrustMaterial(turestore,null);
        final SSLContext sslContext=sslBuilder.build();

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        /*credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "iequaiChou1chie3ailee"));*/
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "admin"));
        RestClientBuilder builder = RestClient.builder(new HttpHost("202.61.86.134", 9281, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        return builder.build();
    }

    private static void init() throws Exception {
        Ealsticsearch e=new Ealsticsearch();
        KeyStore turestore = KeyStore.getInstance("jks");
        try (InputStream is = e.getClass().getResourceAsStream("/key/elastic.jks")) {
            turestore.load(is,"123456".toCharArray());
        }
        turestore.load(null, null);
        SSLContextBuilder sslBuilder= SSLContexts.custom().loadTrustMaterial(turestore,null);
        final SSLContext sslContext=sslBuilder.build();

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "iequaiChou1chie3ailee"));

        RestClientBuilder builder = RestClient.builder(new HttpHost("161.202.230.40", 9231, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        restClient = builder.build();
        // 配置信息
        Settings esSetting = Settings.builder().build();
        client = new PreBuiltTransportClient(esSetting);//初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
        //此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
//        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(url), clientHost));
    }

    public static void main(String[] ars) throws Exception {
        init();
//        delByid();
//        del();
//        insertObj();
//        aggs();
        sysGroupbyUser();
//        sysGroupbyGame();
    }

    public static void insertObj()throws Exception
    {
        RptBetModel object=new RptBetModel();
        object.setId("666666666");
        object.setGameName("北京PK拾");
        object.setGameType("30001");
        object.setBet(new BigDecimal(3.3));
        StringBuffer string = new StringBuffer();
        string.append("{ \"create\" : { \"_index\" : \"" + "report" + "\", \"_type\" : \"" + "rpt_bet_rcd" + "\", \"_id\" : \"" + object.getId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        string.append("{ \"update\" : { \"_index\" : \"" + "report" + "\", \"_type\" : \"" + "rpt_bet_rcd" + "\", \"_id\" : \"" + object.getId() + "\" }}");
        string.append("\n");
        string.append("{ \"doc\" :"+JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")+"}");
        string.append("\n");
        HttpEntity entity = new NStringEntity(string.toString(), ContentType.APPLICATION_JSON);
        restClient.performRequest("POST", "_bulk", Collections.singletonMap("pretty", "true"), entity);
    }

    public static void aggs()throws Exception {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();//查询组合

        SumAggregationBuilder betaggs = AggregationBuilders.sum("bet").field("bet");
        SumAggregationBuilder validbetaggs = AggregationBuilders.sum("validbet").field("validbet");
        SumAggregationBuilder rewardaggs = AggregationBuilders.sum("reward").field("reward");
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("report");
        searchRequestBuilder.addAggregation(betaggs);
        searchRequestBuilder.addAggregation(validbetaggs);
        searchRequestBuilder.addAggregation(rewardaggs);
        searchRequestBuilder.setQuery(builder);
        Response response = restClient.performRequest("GET", "report/rpt_bet_rcd/_search", Collections.singletonMap("_source", "true"), new NStringEntity(searchRequestBuilder.toString(), ContentType.APPLICATION_JSON));

        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        Integer counts=(Integer)((Map) map.get("hits")).get("total");
        BigDecimal bet=(BigDecimal)((Map)((Map) map.get("aggregations")).get("bet")).get("value");
        BigDecimal reward=(BigDecimal)((Map)((Map) map.get("aggregations")).get("reward")).get("value");
        BigDecimal validbet=(BigDecimal)((Map)((Map) map.get("aggregations")).get("validbet")).get("value");
        JSONArray hits = ((JSONArray) (((Map) map.get("hits")).get("hits")));
    }

//    public static void transport()
//    {
//        Settings settings = Settings.builder()
//                .put("searchguard.ssl.transport.enabled", true)
//                .put("searchguard.ssl.transport.keystore_filepath", "D:\\William\\Projects\\searchGuardTest\\src\\main\\resources\\test-keystore.jks")
//                .put("searchguard.ssl.transport.truststore_filepath", "D:\\William\\Projects\\searchGuardTest\\src\\main\\resources\\truststore.jks")
//                .put("searchguard.ssl.transport.keystore_password", "12345678")
//                .put("searchguard.ssl.transport.truststore_password", "12345678")
//                .put("searchguard.ssl.transport.enforce_hostname_verification", false)
//                .put("client.transport.ignore_cluster_name", true)
//                .build();
//
//        //TransportClient client =new PreBuiltTransportClient(settings,new SearchGuardSSLPlugin(settings).getClass()).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
//        TransportClient client =new PreBuiltTransportClient(settings,SearchGuardSSLPlugin.class)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
//        client.admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet();
//
//        //搜索数据
//        GetResponse response = client.prepareGet("agin", "log_bet_rcd_agin_live", "171212226218993").execute().actionGet();
//        //输出结果
//        System.out.println(response.getSourceAsString());
//        //关闭client
//        client.close();
//
//    }

    public static void sysGroupbyUser()throws Exception
    {
        String  query="{\n" +
                "\"query\": {\n" +
                "\"bool\": {\n" +
                "\"must\": [\n" +
                "{\n" +
                "\"term\": {\n" +
                "\"platform\": \"PT\"\n" +
                "}\n" +
                "}\n" +
                ",\n" +
                "{\n" +
                "\"term\": {\n" +
                "\"sitePrefix\": \"caf\"\n" +
                "}\n" +
                "}\n" +
                ",\n" +
                "{\n" +
                "\"range\": {\n" +
                "\"bettime\": {\n" +
                "\"gte\": \"2018-02-11T12:00:00.000Z\",\n" +
                "\"lte\": \"2018-02-11T13:00:00.000Z\"\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "]\n" +
                "}\n" +
                "},\n" +
                "  \"from\": 0,\n" +
                "  \"size\": 0,\n" +
                "  \"sort\": [],\n" +
                "  \"aggs\": {\n" +
                "    \"username\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"username\",\n" +
                "        \"size\": 1000\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"bet\": {\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"betAmountBonus\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Response response = restClient.performRequest("GET", "report/rpt_bet_rcd/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(query, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));

        for(Object obj: (JSONArray)((Map) ((Map) map.get("aggregations")).get("username")).get("buckets"))
        {
            System.out.println(((JSONObject)obj).get("key")+" "+((Map)((JSONObject)obj).get("bet")).get("value"));
        }
    }


    public static void sysGroupbyGame()throws Exception
    {
        String  query="{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"sitePrefix\": \"df\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"terms\": {\n" +
                "            \"gametype\": [\n" +
                "              \"5005\",\n" +
                "              \"5006\",\n" +
                "              \"5007\",\n" +
                "              \"5008\",\n" +
                "              \"5009\",\n" +
                "              \"5010\",\n" +
                "              \"5013\",\n" +
                "              \"5014\",\n" +
                "              \"5015\",\n" +
                "              \"5017\",\n" +
                "              \"5029\",\n" +
                "              \"5030\",\n" +
                "              \"5034\",\n" +
                "              \"5035\",\n" +
                "              \"5039\",\n" +
                "              \"5040\",\n" +
                "              \"5041\",\n" +
                "              \"5042\",\n" +
                "              \"5043\",\n" +
                "              \"5044\",\n" +
                "              \"5048\",\n" +
                "              \"5049\",\n" +
                "              \"5054\",\n" +
                "              \"5058\",\n" +
                "              \"5060\",\n" +
                "              \"5061\",\n" +
                "              \"5062\",\n" +
                "              \"5063\",\n" +
                "              \"5064\",\n" +
                "              \"5065\",\n" +
                "              \"5066\",\n" +
                "              \"5067\",\n" +
                "              \"5070\",\n" +
                "              \"5073\",\n" +
                "              \"5076\",\n" +
                "              \"5077\",\n" +
                "              \"5078\",\n" +
                "              \"5079\",\n" +
                "              \"5080\",\n" +
                "              \"5084\",\n" +
                "              \"5088\",\n" +
                "              \"5089\",\n" +
                "              \"5090\",\n" +
                "              \"5091\",\n" +
                "              \"5092\",\n" +
                "              \"5093\",\n" +
                "              \"5094\",\n" +
                "              \"5095\",\n" +
                "              \"5096\",\n" +
                "              \"5105\",\n" +
                "              \"5107\",\n" +
                "              \"5108\",\n" +
                "              \"5109\",\n" +
                "              \"5115\",\n" +
                "              \"5116\",\n" +
                "              \"5117\",\n" +
                "              \"5118\",\n" +
                "              \"5131\",\n" +
                "              \"5202\",\n" +
                "              \"5402\",\n" +
                "              \"5406\",\n" +
                "              \"5407\",\n" +
                "              \"5601\",\n" +
                "              \"5701\",\n" +
                "              \"5703\",\n" +
                "              \"5704\",\n" +
                "              \"5705\",\n" +
                "              \"5706\",\n" +
                "              \"5707\",\n" +
                "              \"5801\",\n" +
                "              \"5802\",\n" +
                "              \"5804\",\n" +
                "              \"5805\",\n" +
                "              \"5806\",\n" +
                "              \"5808\",\n" +
                "              \"5809\",\n" +
                "              \"5810\",\n" +
                "              \"5811\",\n" +
                "              \"5821\",\n" +
                "              \"5823\",\n" +
                "              \"5824\",\n" +
                "              \"5825\",\n" +
                "              \"5826\",\n" +
                "              \"5827\",\n" +
                "              \"5831\",\n" +
                "              \"5832\",\n" +
                "              \"5833\",\n" +
                "              \"5835\",\n" +
                "              \"5836\",\n" +
                "              \"5837\",\n" +
                "              \"5901\",\n" +
                "              \"5902\",\n" +
                "              \"5903\",\n" +
                "              \"5904\",\n" +
                "              \"5905\",\n" +
                "              \"5907\",\n" +
                "              \"5908\",\n" +
                "              \"5888\",\n" +
                "              \"5909\",\n" +
                "              \"5027\",\n" +
                "              \"5910\"\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"orderDate\": {\n" +
                "              \"gte\": \"2018-01-13T00:00:00.000Z\",\n" +
                "              \"lte\": \"2018-01-14T00:00:00.000Z\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"gamename\": \"JACKPOT\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"username\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"username\",\n" +
                "        \"size\": 1000\n" +
                "      }\n" +
                "    },\n" +
                "    \"validbet\": {\n" +
                "      \"sum\": {\n" +
                "        \"field\": \"validbet\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"reward\": {\n" +
                "      \"sum\": {\n" +
                "        \"field\": \"reward\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Response response = restClient.performRequest("GET", "report/rpt_bet_rcd/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(query, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));

        for(Object obj: (JSONArray)((Map) ((Map) map.get("aggregations")).get("username")).get("buckets"))
        {
            System.out.println(((JSONObject)obj).get("key")+" "+((JSONObject)obj).get("doc_count"));
        }
    }

    public BigDecimal aggsTip(String startDay, List values)throws Exception
    {
        BigDecimal rs = new BigDecimal(0.00);
        TermsAggregationBuilder agg = AggregationBuilders.terms("userName").field("userName").
                subAggregation(AggregationBuilders.sum("betAmount").field("betAmount"));

        QueryBuilder quers = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("wagersDate").gte(startDay + "T00:00:00.000Z").lte(startDay + "T23:59:59.000Z"))
                .must(QueryBuilders.termsQuery("gameType", values));
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("bbin");
        searchRequestBuilder.setTypes("log_bet_rcd_bbin_tip");
        searchRequestBuilder.setSize(0);
        searchRequestBuilder.setQuery(quers);
        searchRequestBuilder.addAggregation(agg);
        System.out.println(searchRequestBuilder.toString());
        Response response = restClient.performRequest("GET", "bbin/log_bet_rcd_bbin_tip,log_bet_rcd_bbin_tip_mdf/_search", Collections.singletonMap("pretty", "true"), new NStringEntity(searchRequestBuilder.toString(), ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        if (((JSONArray) ((Map) ((Map) map.get("aggregations")).get("userName")).get("buckets")).size() > 0) {
            return (BigDecimal) ((Map) ((Map) ((JSONArray) ((Map) ((Map) map.get("aggregations")).get("userName")).get("buckets")).get(0)).get("betAmount")).get("value");
        }
        return rs;
    }

    public List<Map> getValidVet(String sitePrefix, List<String> userlist, String startTime, String endTime) {
        List<Map> rslist = new ArrayList<>();
        TermsAggregationBuilder agg = AggregationBuilders.terms("username").field("username").
                subAggregation(AggregationBuilders.sum("validbet").field("validbet"));
        agg.size(10000);
        BoolQueryBuilder query = QueryBuilders.boolQuery();//查询组合
//        query.must(QueryBuilders.termsQuery("username", userlist));
        query.must(QueryBuilders.rangeQuery("orderDate").gte("2018-01-10T00:00:00.000Z").lt("2018-01-11T00:00:00.000Z"));
        query.must(QueryBuilders.termsQuery("sitePrefix", sitePrefix));
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        query.must(builder);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("report");
        searchRequestBuilder.setQuery(query);
        searchRequestBuilder.addAggregation(agg);
        String str = searchRequestBuilder.toString();
        try {
            Response response = restClient.performRequest("GET", "report/rpt_bet_rcd/_search", Collections.singletonMap("_source", "true"), new NStringEntity(str, ContentType.APPLICATION_JSON));
            Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
            for (Object obj : (JSONArray) ((Map) ((Map) map.get("aggregations")).get("username")).get("buckets")) {
                Map rs = new HashMap();
                Map objmap = (Map) obj;
                rs.put(objmap.get("key"), ((BigDecimal) ((Map) objmap.get("validbet")).get("value")).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                rslist.add(rs);
            }
        } catch (Exception e) {
        }
        return rslist;
    }

    public static void del()throws Exception
    {
        List<String> list=new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("report");
        searchRequestBuilder.setQuery(QueryBuilders.rangeQuery("id").gte("0"));
        searchRequestBuilder.setSize(1000);
        String str = searchRequestBuilder.toString();
        Response response = restClient.performRequest("GET", "mg/log_bet_rcd_mg/_search", Collections.singletonMap("_source", "true"), new NStringEntity(str, ContentType.APPLICATION_JSON));
        Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
        for (Object obj : (JSONArray)((Map) map.get("hits")).get("hits")) {
            Map objmap=(Map)obj;
            list.add(objmap.get("_id").toString());
        }
        System.out.println(list);
        StringBuffer string = new StringBuffer();
        for(String id :list)
        {
            string.append("{ \"delete\" : { \"_index\" : \"mg\", \"_type\" : \"log_bet_rcd_mg\", \"_id\" : \"" +id + "\" }}");
            string.append("\n");
        }
        restClient.performRequest("POST", "_bulk", Collections.singletonMap("pretty", "true"), new NStringEntity(string.toString(), ContentType.APPLICATION_JSON));
    }

    public static void delByid()throws Exception
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"delete\" : { \"_index\" : \"report\", \"_type\" : \"rpt_bet_rcd\", \"_id\" : \"309671408657\" }}");
        string.append("\n");
        restClient.performRequest("POST", "_bulk", Collections.singletonMap("pretty", "true"), new NStringEntity(string.toString(), ContentType.APPLICATION_JSON));
    }

    public static List<Date> getBettimeList(String siteCode, String startTime, String endTime, List<String> userlist) {
        List<Date> rslist = new ArrayList<>();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termsQuery("username", userlist));
        /***根据下注时间查询***/
        query.must(QueryBuilders.rangeQuery("bettime").gte(startTime).lt(endTime));
        query.must(QueryBuilders.termsQuery("sitePrefix", "caf"));
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        query.must(builder);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("report");
        /****顺序排序**/
        searchRequestBuilder.addSort("bettime", SortOrder.ASC);
        searchRequestBuilder.setQuery(query);
        String str = searchRequestBuilder.toString();
        try {
            Response response = restClient.performRequest("GET", "report/rpt_bet_rcd/_search",
                    Collections.singletonMap("_source", "true"),
                    new NStringEntity(str, ContentType.APPLICATION_JSON));
            Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
            JSONArray hits = ((JSONArray) (((Map) map.get("hits")).get("hits")));
            for (Object obj : hits) {
                Map objmap = (Map) obj;
                RptBetModel rpt=JSON.parseObject(objmap.get("_source").toString(), RptBetModel.class);
                rslist.add(rpt.getBetTime());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rslist;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }
}
