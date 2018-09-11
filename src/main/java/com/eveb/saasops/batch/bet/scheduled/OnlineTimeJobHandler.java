package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.service.OnlineTimeService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "onlineTimeJobHandler")
public class OnlineTimeJobHandler extends IJobHandler {

    @Autowired
    private OnlineTimeService onlineTimeService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("开始统计玩家在线时长job");
        onlineTimeService.accountOnlineTime();
        return ReturnT.SUCCESS;
    }
}
