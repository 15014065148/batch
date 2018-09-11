package com.eveb.saasops.batch.game.Ibc.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class IbcRequestParameter implements Cloneable{

    private String StartTime;
    private String EndTime;
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
