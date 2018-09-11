package com.eveb.saasops.batch.sys.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
public class DatasourceManage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String siteprefix;

    private String dburl;

    private String dataSourceName;

    private Date createTime;

    private String merchantID;
}