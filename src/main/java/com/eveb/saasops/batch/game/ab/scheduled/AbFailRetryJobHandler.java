package com.eveb.saasops.batch.game.ab.scheduled;

import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@JobHander(value = "AbFailRetryJobHandler")
public class AbFailRetryJobHandler extends AbJobHandler {

    @Autowired
    public SysService sysService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] objects = prepareArguments(strings);
        Integer limit = objects[indexInteval] == null ? 1 : Integer.parseInt(objects[indexInteval].toString());//每次需要执行的条数
        Integer jobID = objects[indexPara] == null ? 0 : Integer.parseInt(objects[indexPara].toString());//需要执行的ID
        List<JobFailMessageModel> faillist = sysService.getExecuteFailMessages(PlatFromEnum.Enum_AB.getValue(), jobID, limit);
        return doFailExecute(faillist);
    }

    /***
     *  执行失败的任务
     * @throws Exception
     */
    protected ReturnT<String> doFailExecute(List<JobFailMessageModel> parameterList) throws Exception {
        for (JobFailMessageModel model : parameterList) {
            try {
                super.processor.executeJob(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }
}
