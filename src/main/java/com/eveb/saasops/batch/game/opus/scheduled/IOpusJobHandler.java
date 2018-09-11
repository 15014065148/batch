package com.eveb.saasops.batch.game.opus.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.opus.domain.OpusRequestParameter;
import com.eveb.saasops.batch.sys.processor.IProcessor;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IOpusJobHandler extends JobHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss");
    @Autowired
    private RptService rptService;
    public IProcessor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        paramsStrs=initParams(paramsStrs);
        List<OpusRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<OpusRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    public Object[] initParams(Object[] paramsStrs){
        return paramsStrs;
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<OpusRequestParameter> parameterList) throws Exception {
        for (OpusRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(parameter.getPlatformName());
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
    private List<OpusRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<OpusRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_OPUS_INTEVAL,ApplicationConstant.CONSTANT_OPUS_DELAY, paramsStrs)) {
            OpusRequestParameter parameter = new OpusRequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<OpusRequestParameter> prepareApiLine(List<OpusRequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<OpusRequestParameter> list = new ArrayList<>();
        String platformName=paramsStrs[indexPara].toString();
        List<TGmApi> apiList = rptService.getGameApiByApiName(platformName , paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (OpusRequestParameter opus : parameters) {
            for (TGmApi api : apiList) {
                OpusRequestParameter par =(OpusRequestParameter)opus.clone();
                par.setPlatformName(platformName);
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
