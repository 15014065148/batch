package com.eveb.saasops.batch.game.mg2.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class Mg2Response {

    private Map<String,Object> meta ;
    private List<Mg2BetLogModel> data;

}
