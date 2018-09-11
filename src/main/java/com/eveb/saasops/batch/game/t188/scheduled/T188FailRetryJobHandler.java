package com.eveb.saasops.batch.game.t188.scheduled;


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
@JobHander(value = "t188FailRetryJobHandler")
public class T188FailRetryJobHandler extends IT188JobHandler {

    @Autowired
    private SysService sysService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {

        Object[] objects = prepareArguments(strings);
        Integer limit = objects[indexInteval] == null ? 1 : Integer.parseInt(objects[indexInteval].toString());//每次需要执行的条数
        Integer jobID= objects[indexPara] == null ? 0 : Integer.parseInt(objects[indexPara].toString());//需要执行的ID
        List<JobFailMessageModel> faillist = sysService.getExecuteFailMessages(PlatFromEnum.Enum_T188.getValue(),jobID,limit);
        return doFailExecute(faillist);
    }
}
