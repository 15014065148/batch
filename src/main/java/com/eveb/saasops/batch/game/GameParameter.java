package com.eveb.saasops.batch.game;

import com.eveb.saasops.batch.game.report.domain.TGmApi;

import java.util.HashMap;
import java.util.Map;

public class GameParameter<T> implements Cloneable {
    private T t;
    private String handlerName;
    private TGmApi api;
    public final Map<String, String> processMap = new HashMap<>();

    public GameParameter() {
        processMap.put("AbJobHandler", "AbProcessor");
        processMap.put("AbEgameHanlder", "AbEgameProcessor");
        processMap.put("AbModifyJobHandler", "AbModifyProcessor");
    }

    private String startTime;//开始日期 "yyyy:MM:dd'T'HH:mm:ss"
    private String endTime;
    private Integer page = 1;//默认第一页
    private Integer pageSize = 1000;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public TGmApi getApi() {
        return api;
    }

    public void setApi(TGmApi api) {
        this.api = api;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
