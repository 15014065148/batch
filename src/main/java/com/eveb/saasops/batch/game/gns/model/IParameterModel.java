package com.eveb.saasops.batch.game.gns.model;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class IParameterModel<T> implements Cloneable{

    private TGmApi api;
    private T t;
    private String startTime;
    private String endTime;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
