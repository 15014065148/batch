package com.eveb.saasops.batch.game.mg.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MgRequestParameter implements Cloneable{

    private TGmApi api;
    private String startdate;//开始日期 "yyyy:MM:dd:HH:mm:ss"
    private String enddate;//结束日期 "yyyy:MM:dd:HH:mm:ss"
    private String timezone="Asia/Shanghai";// Timezone, eg, "UTC" or  "Asia/Shanghai"

    /****
     * 初始化调用参数
     */
    public MgRequestParameter() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
