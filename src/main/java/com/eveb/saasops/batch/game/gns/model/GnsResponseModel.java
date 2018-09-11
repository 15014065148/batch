package com.eveb.saasops.batch.game.gns.model;

import lombok.Data;

import java.util.List;

@Data
public class GnsResponseModel {

    private Integer total_query_size;
    private Integer start_index;
    private Integer fetch_size;
    private List<GnsBetLogModel> data;
}
