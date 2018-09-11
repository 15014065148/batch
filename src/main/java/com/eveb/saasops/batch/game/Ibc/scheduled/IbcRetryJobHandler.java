package com.eveb.saasops.batch.game.Ibc.scheduled;

import com.eveb.saasops.batch.game.Ibc.processor.IbcProcessor;
import com.eveb.saasops.batch.game.Ibc.processor.IbcRetryProcessor;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "ibcRetryJobHandler")
public class IbcRetryJobHandler extends IibcJobHandler {

    @Autowired
    private IbcRetryProcessor ibcRetryProcessor;

    @Override
    public void initProcessor()
    {
        this.processor=ibcRetryProcessor;
    }
}
