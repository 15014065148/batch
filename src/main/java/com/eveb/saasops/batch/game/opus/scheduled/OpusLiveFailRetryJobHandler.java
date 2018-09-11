package com.eveb.saasops.batch.game.opus.scheduled;

import com.eveb.saasops.batch.game.opus.processor.OpusLiveProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 17:51 2017/12/21
 **/
@Slf4j
@Service
@JobHander(value = "opusLiveFailRetryJobHandler")
public class OpusLiveFailRetryJobHandler extends IOpusJobHandler {

    @Autowired
    private SysService sysService;
    @Autowired
    private OpusLiveProcessor liveProcessor;
    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        this.processor=liveProcessor;
        Object[] objects = prepareArguments(strings);
        Integer limit = objects[indexInteval] == null ? 1 : Integer.parseInt(objects[indexInteval].toString());//每次需要执行的条数
        Integer jobID= objects[indexPara] == null ? 0 : Integer.parseInt(objects[indexPara].toString());//需要执行的ID
        List<JobFailMessageModel> faillist = sysService.getExecuteFailMessages(PlatFromEnum.Enum_OPUSCA.getValue(),jobID,limit);
        return doFailExecute(faillist);
    }
}
