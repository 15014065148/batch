package com.eveb.saasops.batch.game.fg.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class FgRequestParameter implements Cloneable {
    /**
     * 游戏code  分别为：fish/poker/slot/fruit/
     */
    //private String gameCode;
    /**
     * 开始时间 时间戳
     */
    private String startDate;
    /**
     * 结束时间 时间戳
     */
    private String endDate;

    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
