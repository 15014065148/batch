package com.eveb.saasops.batch.comparator.querySql.report;


import org.springframework.stereotype.Component;

/**
 * Created by William on 2018/2/14.
 */
@Component
public class ReportQuery {

    /**
     * 按用户名汇总,查询明细
     * @param dateFrom
     * @param dateTo
     * @param platform
     * @param apiPrefix
     * @return
     */
    public String getReportOrderByUserName(String dateFrom, String dateTo,String platform,String apiPrefix){
        String sql ="{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"platform\": \""+platform+"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"apiPrefix\": \""+apiPrefix+"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"bettime\": {\n" +
                "              \"gte\": \""+dateFrom+"T00:00:00.000Z\",\n" +
                "              \"lte\": \""+dateTo+"T00:00:00.000Z\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [],\n" +
                "      \"should\": []\n" +
                "    }\n" +
                "  },\n" +
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
                "        \"bets\": {\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"bet\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Active Players\":{\n" +
                "          \"cardinality\": {\n" +
                "            \"field\": \"username\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Progressive Share\":{\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"betAmountBonus\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Progressive Wins\":{\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"rewardamount\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Wins\":{\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"reward\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
            return sql;
    }

    /**
     * 按apiPrefix汇总信息,查询明细
     * @param dateFrom
     * @param dateTo
     * @param platform
     * @return
     */
    public String getReportOrderByKioks(String dateFrom, String dateTo,String platform){
        return "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"platform\": \""+platform+"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"bettime\": {\n" +
                "              \"gte\": \""+dateFrom+"T00:00:00.000Z\",\n" +
                "              \"lte\": \""+dateTo+"T00:00:00.000Z\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [],\n" +
                "      \"should\": []\n" +
                "    }\n" +
                "  },\n" +
                "  \"from\": 0,\n" +
                "  \"size\": 0,\n" +
                "  \"sort\": [],\n" +
                "  \"aggs\": {\n" +
                "    \"Kiosk\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"apiPrefix\",\n" +
                "        \"size\": 1000\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"bets\": {\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"bet\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Active Players\": {\n" +
                "          \"cardinality\": {\n" +
                "            \"field\": \"username\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Progressive Share\": {\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"betAmountBonus\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Progressive Wins\": {\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"rewardamount\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Wins\": {\n" +
                "          \"sum\": {\n" +
                "            \"field\": \"reward\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
