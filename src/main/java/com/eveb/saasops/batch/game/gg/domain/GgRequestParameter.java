package com.eveb.saasops.batch.game.gg.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

/**
 * 2018-08-02 Jeff
 */
@Data
public class GgRequestParameter implements Cloneable {

    /**
     * 开始时间 yyyy-MM-dd HH:mm:ss
     */
    private String startDate;
    /**
     * 结束时间 yyyy-MM-dd HH:mm:ss
     */
    private String endDate;
    /**
     * 游戏编码
     */
    //private String gameId;
    /**
     * 数值 ,是一個常數
     */
    private String method;

    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
