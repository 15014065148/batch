package com.eveb.saasops.batch.game.pb.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class PbRequestParameter implements Cloneable{

    private TGmApi api;
    /**1: settle
     0: unsettle
     -1: all (both settle and
     unsettle)
     (Default: -1)
     **/
    private Integer settle=-1;
    /**event_date wager_date settle_date**/
    private String filterBy;
    private String startTime;
    private String endTime;

    /****
     * 初始化调用参数
     */
    public PbRequestParameter() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
