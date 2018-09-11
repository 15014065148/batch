package com.eveb.saasops.batch.game.gns.model;

import lombok.Data;

@Data
public class GnsParameterModel {

    private String startDate;
    private String endDate;
    private Integer limit;
    private Integer startIndex;

}
