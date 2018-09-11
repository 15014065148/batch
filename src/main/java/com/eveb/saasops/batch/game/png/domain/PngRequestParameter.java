package com.eveb.saasops.batch.game.png.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PngRequestParameter implements Cloneable{

    private TGmApi api;
    private String startTime;//开始日期 "yyyy:MM:dd:HH:mm:ss"
    private String endTime;//结束日期 "yyyy:MM:dd:HH:mm:ss"

    /****
     * 初始化调用参数
     */
    public PngRequestParameter() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
