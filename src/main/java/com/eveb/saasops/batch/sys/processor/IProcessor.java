package com.eveb.saasops.batch.sys.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
@Component
public abstract  class IProcessor {
    @Autowired
    private SysService sysService;

    @Autowired
    protected RptElasticRestService rptElasticService;

    public abstract void executeJob(JobFailMessageModel jobmodel)throws Exception;

    public void executeJobs(JobFailMessageModel jobmodel)throws Exception{
        jobmodel.setTimes(jobmodel.getTimes() + 1);
        jobmodel.setFirstTime(new Date());
        jobmodel.setLastTime(new Date());
        try {
            jobmodel.setCounts(processBet(jobmodel.getParamater()));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }
    public abstract int processBet(String para)throws Exception;


}
