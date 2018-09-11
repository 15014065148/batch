package com.eveb.saasops.batch.game.bg.domian;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

import java.util.Date;

/**
 * 请求参数
 * 2018-08-08 Jeff
 */
@Data
public class BgRequestParameter implements Cloneable {
    /**
     * 接口地址
     */
    private String method;
    /**
     * 开始时间 时间戳
     */
    private Date startDate;
    /**
     * 结束时间 时间戳
     */
    private Date endDate;

    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
