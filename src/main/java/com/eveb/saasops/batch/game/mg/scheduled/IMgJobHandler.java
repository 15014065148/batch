package com.eveb.saasops.batch.game.mg.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg.domain.MgRequestParameter;
import com.eveb.saasops.batch.game.mg.processor.MgProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.SimpleDateFormat;
import java.util.*;

public class IMgJobHandler extends JobHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
    @Autowired
    private RptService rptService;
    @Autowired
    private MgProcessor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<MgRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<MgRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<MgRequestParameter> parameterList) throws Exception {
        for (MgRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_MG.getValue());
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
    private List<MgRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<MgRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_MG_INTEVAL, paramsStrs)) {
            MgRequestParameter parameter = new MgRequestParameter();
            parameter.setStartdate(strs[0]);
            parameter.setEnddate(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<MgRequestParameter> prepareApiLine(List<MgRequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<MgRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_MG.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (MgRequestParameter mg : parameters) {
            for (TGmApi api : apiList) {
                MgRequestParameter par =(MgRequestParameter)mg.clone();
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
