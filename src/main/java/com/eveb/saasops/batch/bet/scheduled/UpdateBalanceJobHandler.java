package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.service.UpdateDepotBalance;

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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;


@Slf4j
@Service
@JobHander(value = "updateBalanceJobHandler")
public class UpdateBalanceJobHandler extends JobHandler {

    @Autowired
    private UpdateDepotBalance updateDepotBalance;
    @Autowired
    private SysService sysService;

    private static final int THREAD_COUNT = 6;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("开始执行获取平台余额");
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
           int corePoolSize = siteCodes.size() > THREAD_COUNT ? THREAD_COUNT : siteCodes.size();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, siteCodes.size(),
                    5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5000));
            siteCodes.stream().forEach(st -> executor.execute(() -> updateDepotBalance.setDepotBalance(st)));
            executor.shutdown();
        }
        return ReturnT.SUCCESS;
    }

}
