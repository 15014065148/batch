package com.eveb.saasops.batch.game;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.gns.model.IParameterModel;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.processor.IProcessor;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class IJobhandler<D> extends JobHandler {
    public int constantInteval=ApplicationConstant.CONSTANT_INTEVAL;
    public int constantDelay=ApplicationConstant.CONSTANT_DELAY;
    public String platFromValue;
    @Autowired
    private RptService rptService;
    public IProcessor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs = prepareArguments(strings);
        List<IParameterModel<D>> requestParameters = prepareTimePeriod(paramsStrs);
        requestParameters = prepareApiLine(requestParameters, paramsStrs);
        return doExecute(requestParameters);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<IParameterModel<D>> parameterList) {
        for (IParameterModel parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(platFromValue);
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

    /****
     * 切割时间，生成API调用
     * @param paramsStrs
     * @return
     */
    private List<IParameterModel<D>> prepareTimePeriod(Object[] paramsStrs) {
        List<IParameterModel<D>> parameters = new ArrayList<>();
        for (String[] strs : cutDate(constantInteval,constantDelay, paramsStrs)) {
            IParameterModel<D> parameter = new IParameterModel<>();
            parameter.setT(setParameter());
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    public List<IParameterModel<D>> prepareApiLine(List<IParameterModel<D>> parameterList, Object[] paramsStrs) throws Exception {
        List<IParameterModel<D>> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(platFromValue, paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (IParameterModel<D> parameter : parameterList) {
            for (TGmApi api : apiList) {
                IParameterModel par = (IParameterModel) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    /***
     *  执行失败的任务
     */
    protected ReturnT<String> doFailExecute(List<JobFailMessageModel> parameterList) {
        for (JobFailMessageModel model : parameterList) {
            try {
                processor.executeJob(model);
            } catch (Exception e) {
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    public <T> T setParameter(){
        return null;
    }
}
