package com.eveb.saasops.batch.game.Ibc.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.Ibc.domain.IbcRequestParameter;
import com.eveb.saasops.batch.game.Ibc.processor.IbcProcessor;
import com.eveb.saasops.batch.game.Ibc.processor.IibcProcessor;
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

public class IibcJobHandler extends JobHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3a'mm'%3a'ss");
    public IibcProcessor processor;
    @Autowired
    private RptService rptService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        initProcessor();
        Object[] paramsStrs=prepareArguments(strings);
        List<IbcRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<IbcRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     * 初始化处理类
     */
    public void initProcessor(){}

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<IbcRequestParameter> parameterList) throws Exception {
        for (IbcRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_IBC.getValue());
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
    private List<IbcRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<IbcRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_IBC_INTEVAL,ApplicationConstant.CONSTANT_IBC_DELAY, paramsStrs)) {
            IbcRequestParameter parameter = new IbcRequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<IbcRequestParameter> prepareApiLine(List<IbcRequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<IbcRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_IBC.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (IbcRequestParameter ibc : parameters) {
            for (TGmApi api : apiList) {
                IbcRequestParameter par =(IbcRequestParameter)ibc.clone();
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
