package com.eveb.saasops.batch.game.nt.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NtRequestParameter implements Cloneable{

    private TGmApi api;
    private String startTime;//开始日期 "yyyy:MM:dd:HH:mm:ss"
    private String endTime;//结束日期 "yyyy:MM:dd:HH:mm:ss"
    private Integer timezone=+8;//The time zone offset format must have a+or – followed by a numbers.Example:+0,-0,+1.5,-1.5,-8,+8 etc.

    /****
     * 初始化调用参数
     */
    public NtRequestParameter() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
