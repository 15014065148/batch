package com.eveb.saasops.batch.game.opus.scheduled;

import com.eveb.saasops.batch.game.opus.processor.OpusLiveProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@JobHander(value = "opusLiveJobHandler")
public class OpusLiveJobHandler extends IOpusJobHandler {

    @Autowired
    private OpusLiveProcessor liveProcessor;

    @Override
    public Object[] initParams(Object[] paramsStrs){
        this.processor=liveProcessor;
        paramsStrs[indexPara]= PlatFromEnum.Enum_OPUSCA.getValue();
        paramsStrs[indexApi]= Collections.singletonList(PlatFromEnum.Enum_OPUSCA.getValue().toString());
        return paramsStrs;
    }
}
