package com.eveb.saasops.batch.game.bng.bean;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class BngRequestParam implements Cloneable {

    private String startDate;//开始日期 "yyyy:MM:dd'T'HH:mm:ss"
    private String endDate;//结束日期 "yyyy:MM:dd'T'HH:mm:ss"
    private Integer page=1;//默认第一页
    private Integer pageSize = 3000;
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
