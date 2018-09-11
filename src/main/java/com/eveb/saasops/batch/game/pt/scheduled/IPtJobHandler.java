package com.eveb.saasops.batch.game.pt.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pt.domain.PtRequestParameter;
import com.eveb.saasops.batch.game.pt.process.PTGameProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class IPtJobHandler extends JobHandler {

    @Autowired
    private PTGameProcessor processor;
    @Autowired
    private RptService rptService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss");

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<PtRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<PtRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<PtRequestParameter> parameterList) throws Exception {
        for (PtRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_PT.getValue());
                model.setApiName(parameter.getApi().getAgyAcc());
                model.setParamater(JSON.toJSONString(parameter));
                processor.executeJob(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /***
     *  执行失败的任务
     * @throws Exception
     */
    protected ReturnT<String> doFailExecute(List<JobFailMessageModel> parameterList) throws Exception {
        for (JobFailMessageModel model : parameterList) {
            try {
                processor.executeJob(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /****
     * 切割时间，生成API调用
     * @param paramsStrs
     * @return
     */
    private List<PtRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        //获取十分钟之前的记录
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_PT_DELAY);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_PT_DELAY);
        List<PtRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(startcal.getTime(), endcal.getTime(), ApplicationConstant.CONSTANT_PT_INTEVAL)) {
            PtRequestParameter parameter = new PtRequestParameter();
            parameter.setStartdate(strs[0]);
            parameter.setEnddate(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<PtRequestParameter> prepareApiLine(List<PtRequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<PtRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_PT.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (PtRequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                PtRequestParameter par = (PtRequestParameter) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    public String dateFormat(Date date) {
        return dateFormat.format(date);
    }
}
