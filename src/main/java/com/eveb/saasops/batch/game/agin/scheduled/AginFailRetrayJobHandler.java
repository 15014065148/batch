package com.eveb.saasops.batch.game.agin.scheduled;

import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 9:45 2017/12/30
 **/
@Slf4j
@Service
@JobHander(value = "aginFailRetryJobHandler")
public class AginFailRetrayJobHandler extends IAginJobHandler {

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {

        Object[] objects = prepareArguments(strings);
        Integer limit = objects[indexInteval] == null ? 1 : Integer.parseInt(objects[indexInteval].toString());//每次需要执行的条数
        Integer jobID= objects[indexPara] == null ? 0 : Integer.parseInt(objects[indexPara].toString());//需要执行的ID
        List<JobFailMessageModel> faillist = sysService.getExecuteFailMessages(PlatFromEnum.Enum_AGIN.getValue(),jobID,limit);
        return doFailExecute(faillist);
    }
}
