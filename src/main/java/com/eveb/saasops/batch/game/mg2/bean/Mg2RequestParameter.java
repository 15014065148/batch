package com.eveb.saasops.batch.game.mg2.bean;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class Mg2RequestParameter implements Cloneable {

    private String startTime;//开始日期 "yyyy:MM:dd'T'HH:mm:ss"
    private String endTime;//结束日期 "yyyy:MM:dd'T'HH:mm:ss"
    private Integer page=1;//默认第一页
    private Integer pageSize = 10000;
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
