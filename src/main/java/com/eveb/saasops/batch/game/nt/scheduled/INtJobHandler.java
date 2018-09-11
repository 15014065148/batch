package com.eveb.saasops.batch.game.nt.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.nt.domain.NtRequestParameter;
import com.eveb.saasops.batch.game.nt.processor.NtProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
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

public class INtJobHandler extends JobHandler {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    @Autowired
    private RptService rptService;
    @Autowired
    private NtProcessor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<NtRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<NtRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<NtRequestParameter> parameterList){
        for (NtRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_NT.getValue());
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
    private List<NtRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<NtRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_NT_INTEVAL, paramsStrs)) {
            NtRequestParameter parameter = new NtRequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<NtRequestParameter> prepareApiLine(List<NtRequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<NtRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_NT.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (NtRequestParameter mg : parameters) {
            for (TGmApi api : apiList) {
                NtRequestParameter par =(NtRequestParameter)mg.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    public String dateFormat(Date date)
    {
        return dateFormat.format(date);
    }
}
