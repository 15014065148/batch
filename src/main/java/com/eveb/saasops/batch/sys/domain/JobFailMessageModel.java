package com.eveb.saasops.batch.sys.domain;

import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 13:22 2017/12/22
 **/
@Data
public class JobFailMessageModel {

    private int id;
    private String platform;
    private String paramater;
    private String apiName;
    private Integer executeStatus= ApplicationConstant.CONSTANT_JOBEXECUTE_FAIL;
    private Date firstTime=new Date();
    private Integer counts=0;
    private Integer times=0;
    private Date lastTime;
}
