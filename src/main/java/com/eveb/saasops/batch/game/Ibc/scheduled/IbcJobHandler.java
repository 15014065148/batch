package com.eveb.saasops.batch.game.Ibc.scheduled;

import com.eveb.saasops.batch.game.Ibc.processor.IbcProcessor;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "ibcJobHandler")
public class IbcJobHandler extends IibcJobHandler {

    @Autowired
    private IbcProcessor ibcProcessor;

    @Override
    public void initProcessor()
    {
        this.processor=ibcProcessor;
    }
}
