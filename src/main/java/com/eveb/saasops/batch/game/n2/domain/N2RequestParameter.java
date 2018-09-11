package com.eveb.saasops.batch.game.n2.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class N2RequestParameter implements Cloneable {
    /**
     * 商家身份ID
     */
    private String vendorid;
    /**
     * 节点
     */
    private String action;
    /**
     * 商家的验证密码
     */
    private String merchantpasscode;
    /**
     * 开始日期 "yyyy - mm - dd hh:mm:ss"
     */
    private String startdate;
    /**
     * 结束日期 "yyyy - mm - dd hh:mm:ss"
     */
    private String enddate;
    /**
     * 时区偏移
     */
    private String timezone;
    private Integer pagenum;
    private TGmApi api;

    public N2RequestParameter() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
