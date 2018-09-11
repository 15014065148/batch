package com.eveb.saasops.batch.game;

import lombok.Data;

import java.util.Map;

@Data
public class OpenResultModel {
    private String type;
    private Map<String,String> resultMap;

    public OpenResultModel(){

    }
    public OpenResultModel(String type,Map<String,String> map){
        this.type=type;
        this.resultMap=map;
    }
}
