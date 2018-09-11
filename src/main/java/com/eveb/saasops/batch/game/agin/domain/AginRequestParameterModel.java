package com.eveb.saasops.batch.game.agin.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 10:44 2017/12/28
 **/
@Data
public class AginRequestParameterModel implements Cloneable{

    private String url;//FTP服务器hostname
    private String username;//FTP登录账号
    private String password;//FTP登录密码
    private String remotePath;//路径
    private List<String> fileName=new ArrayList<>();//文件名,默认为空
    private String indexName;
    private String typeName;
    private TGmApi api;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
