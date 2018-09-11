package com.eveb.saasops.batch.game.ttg.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class TTGRequestParameter implements Cloneable {

    private String startdate;//开始日期 "yyyy:MM:dd:HH:mm:ss"
    private String enddate;//结束日期 "yyyy:MM:dd:HH:mm:ss"
    private Integer pagenum=1;//默认第一页
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}