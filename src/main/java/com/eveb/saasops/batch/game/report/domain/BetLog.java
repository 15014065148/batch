package com.eveb.saasops.batch.game.report.domain;

import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BetLog {
    protected String sitePrefix;//前缀
    protected String apiPrefix;//api前缀
    /**下载时间**/
    private Date downloadTime=DateUtil.orderDate(new Date());
}
