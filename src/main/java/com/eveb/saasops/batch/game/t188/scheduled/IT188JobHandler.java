package com.eveb.saasops.batch.game.t188.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.game.t188.domain.T188RequestParameter;
import com.eveb.saasops.batch.game.t188.processor.T188Processor;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IT188JobHandler extends JobHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    @Autowired
    private RptService rptService;
    @Autowired
    private T188Processor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<T188RequestParameter> list=prepareTimePeriod(paramsStrs);
        List<T188RequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<T188RequestParameter> parameterList) throws Exception {
        for (T188RequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_T188.getValue());
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
    private List<T188RequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<T188RequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_T188_INTEVAL, paramsStrs)) {
            T188RequestParameter parameter = new T188RequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<T188RequestParameter> prepareApiLine(List<T188RequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<T188RequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_T188.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (T188RequestParameter mg : parameters) {
            for (TGmApi api : apiList) {
                T188RequestParameter par =(T188RequestParameter)mg.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    @Override
    public String dateFormat(Date date)
    {
        return dateFormat.format(date);
    }
}
