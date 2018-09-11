package com.eveb.saasops.batch.game.hg.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

import java.util.Date;

@Data
public class HGRequestParameter implements Cloneable {
//    private String username; //
//    private String password; //密码
//    private String casinoId; //赌场id
//    private String userId; //用户id
    private String dateval; //日期  yyyy/MM/dd
    private String timeRange;//日期时间，0-24小时
//    private String status; //状态
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
