package com.eveb.saasops.batch.game.bng.bean;

import lombok.Data;

import java.util.List;

@Data
public class BngBetLogModel {

    private String fetch_state ;
    private List<BngItem> items;
}
