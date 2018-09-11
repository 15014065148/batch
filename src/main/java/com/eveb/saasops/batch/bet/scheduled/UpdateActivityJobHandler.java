package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.service.IDepotBalanceService;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.Collections3;
import com.eveb.saasops.batch.sys.util.SiteCodeAesUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.util.Objects.isNull;


@Slf4j
@Service
@JobHander(value = "updateActivityJobHandler")
public class UpdateActivityJobHandler extends JobHandler {

    @Autowired
    private IDepotBalanceService depotBalanceService;
    @Autowired
    private SysService sysService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("开始执行修改活动状态JOB");
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
            siteCodes.stream().forEach(st -> {
                CompletableFuture.runAsync(() -> {
                    depotBalanceService.updateActivityState(SiteCodeAesUtil.encrypt(st));
                });
            });
        }
        return ReturnT.SUCCESS;
    }

}
