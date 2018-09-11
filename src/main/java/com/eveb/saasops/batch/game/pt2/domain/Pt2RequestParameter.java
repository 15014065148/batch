package com.eveb.saasops.batch.game.pt2.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class Pt2RequestParameter implements Cloneable{

    private String startTime;
    private String endTime;
    private Integer offset=0;
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
