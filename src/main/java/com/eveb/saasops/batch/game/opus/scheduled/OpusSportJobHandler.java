package com.eveb.saasops.batch.game.opus.scheduled;

import com.eveb.saasops.batch.game.opus.processor.OpusSportProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@JobHander(value = "opusSportJobHandler")
public class OpusSportJobHandler extends IOpusJobHandler {

    @Autowired
    private OpusSportProcessor sportProcessor;

    @Override
    public Object[] initParams(Object[] paramsStrs){
        this.processor=sportProcessor;
        paramsStrs[indexPara]= PlatFromEnum.Enum_OPUSSB.getValue();
        paramsStrs[indexApi]= Collections.singletonList(PlatFromEnum.Enum_OPUSSB.getValue());
        return paramsStrs;
    }
}
