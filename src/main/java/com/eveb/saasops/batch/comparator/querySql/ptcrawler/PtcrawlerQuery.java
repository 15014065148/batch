package com.eveb.saasops.batch.comparator.querySql.ptcrawler;

import org.springframework.stereotype.Component;

/**
 * Created by William on 2018/2/19.
 */
@Component
public class PtcrawlerQuery {

    /**
     * 根据时间获取按平台汇总的数据
     * @param date YYYY-MM-dd
     * @return
     */
    public String getPTDataByTime(String date){
        return "{\n" +
                "\"query\": {\n" +
                "\"bool\": {\n" +
                "\"must\": [\n" +
                "{\n" +
                "\"term\": {\n" +
                "\"cTime\": \""+date+"\"\n" +
                "}\n" +
                "}\n" +
                "],\n" +
                "\"must_not\": [ ],\n" +
                "\"should\": [ ]\n" +
                "}\n" +
                "},\n" +
                "\"from\": 0,\n" +
                "\"sort\": [ ],\n" +
                "\"aggs\": { }\n" +
                "}";
    }
}
