package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.service.UpdateOprRecMbrService;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.Collections3;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


import static com.eveb.saasops.batch.sys.util.DateUtil.FORMAT_18_DATE_TIME;
import static com.eveb.saasops.batch.sys.util.DateUtil.getCurrentDate;
import static java.util.Objects.isNull;


@Slf4j
@Service
@JobHander(value = "UpdateOprRecMbrJobHandler")
public class UpdateOprRecMbrJobHandler extends JobHandler {

    @Autowired
    private UpdateOprRecMbrService updateOprRecMbrService;
    @Autowired
    private SysService sysService;


    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("开始执行删除已读站内信息");
        String startTime = getCurrentDate(FORMAT_18_DATE_TIME);
        Object[] objects = prepareArguments(strings);
        List<String> apis = isNull(objects[indexApi]) ? null : (List<String>) objects[indexApi];
        List<String> siteCodes = Lists.newArrayList();
        if (isNull(apis)) {
            siteCodes = sysService.getSiteCodeList();
        } else {
            for (String api : apis) {
                siteCodes.addAll(sysService.getSiteCodeList(api));
            }
        }
        if (Collections3.isNotEmpty(siteCodes)) {
            siteCodes.stream().forEach(st -> updateOprRecMbrService.updateOprRecMbr(st));
        }
        log.info("删除已读站内信息结束开始时间【" + startTime + "】,结束时间【" + getCurrentDate(FORMAT_18_DATE_TIME) + "】");
        return ReturnT.SUCCESS;
    }

}
