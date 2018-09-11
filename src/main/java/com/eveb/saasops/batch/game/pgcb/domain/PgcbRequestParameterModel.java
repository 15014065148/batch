package com.eveb.saasops.batch.game.pgcb.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Jeff
 * @Description:  盘古彩播
 * @Date: 10:44 2018/07/27
 **/
@Data
public class PgcbRequestParameterModel implements Cloneable {

    private String url;//FTP服务器hostname
    private String username;//FTP登录账号
    private String password;//FTP登录密码
    private String remotePath;//路径
    private List<String> fileName = new ArrayList<>();//文件名,默认为空
    private String indexName;
    private String typeName;
    private Date startDate;//开始更新时间
    private Date endDate;  //结束更新时间
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
