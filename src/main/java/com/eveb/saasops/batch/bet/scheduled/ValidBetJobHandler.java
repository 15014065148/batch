package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.service.ValidBetService;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.Collections3;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.eveb.saasops.batch.sys.util.DateUtil.FORMAT_18_DATE_TIME;
import static com.eveb.saasops.batch.sys.util.DateUtil.getCurrentDate;
import static java.util.Objects.isNull;


@Slf4j
@Service
@JobHander(value = "validBetJobHandler")
public class ValidBetJobHandler extends JobHandler {

    @Autowired
    private ValidBetService validBetService;
    @Autowired
    private SysService sysService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("开始执行获取有效有效投注");
        String startTime = getCurrentDate(FORMAT_18_DATE_TIME);
        Object[] objects = prepareArguments(strings);
        List<String> apis = isNull(objects[indexApi]) ? null : (List<String>) objects[indexApi];
        List<String> siteCodes = new ArrayList<>();
        if (isNull(apis)) {
            siteCodes = sysService.getSiteCodeList();
        } else {
            for (String api : apis) {
                siteCodes.addAll(sysService.getSiteCodeList(api));
            }
        }
        if (Collections3.isNotEmpty(siteCodes)) {
            siteCodes.stream().forEach(st -> validBetService.castMbrValidbet(st));
        }
        log.info("有效投注额结束开始时间【" + startTime + "】,结束时间【" + getCurrentDate(FORMAT_18_DATE_TIME) + "】");
        return ReturnT.SUCCESS;
    }

}
