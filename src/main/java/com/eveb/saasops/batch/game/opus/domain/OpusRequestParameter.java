package com.eveb.saasops.batch.game.opus.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OpusRequestParameter implements Cloneable{

    private TGmApi api;
    private String platformName;
    private String startTime;//开始日期 "yyyy:MM:dd HH:mm:ss"
    private String endTime;//结束日期 "yyyy:MM:dd HH:mm:ss"

    /****
     * 初始化调用参数
     */
    public OpusRequestParameter() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
