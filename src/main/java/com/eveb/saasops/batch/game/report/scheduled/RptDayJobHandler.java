package com.eveb.saasops.batch.game.report.scheduled;


import com.eveb.saasops.batch.game.report.processor.RptBetDayProcessor;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.service.SysService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@JobHander(value = "rptDayJobHandler")
public class RptDayJobHandler extends JobHandler {

    @Autowired
    RptBetDayProcessor rptBetDayProcessors;
    @Autowired
    SysService sysService;

    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        Object[] objects = prepareArguments(params);
        List<String> sites=sysService.getSiteCodeList(objects[indexApi]==null?null:((ArrayList) objects[indexApi]).get(0).toString());

        for(String sitecode:sites)
        {
            rptBetDayProcessors.execute(sitecode,dateFormat.format(objects[indexDate]).toString().substring(0,10));
        }
        return ReturnT.SUCCESS;
    }
}
