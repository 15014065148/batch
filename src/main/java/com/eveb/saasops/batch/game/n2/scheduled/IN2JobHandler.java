package com.eveb.saasops.batch.game.n2.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.n2.domain.N2RequestParameter;
import com.eveb.saasops.batch.game.n2.process.N2Processor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IN2JobHandler extends JobHandler {
    @Autowired
    private RptService rptService;
    @Autowired
    private N2Processor n2GameProcessor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs = prepareArguments(strings);
        List<N2RequestParameter> list = prepareTimePeriod(paramsStrs);
        List<N2RequestParameter> paraList = prepareApiLine(list, paramsStrs);
        return doExecute(paraList);
    }

    /**
     * 执行
     *
     * @param parameterList
     * @return
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<N2RequestParameter> parameterList) {
        for (N2RequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_N2.getValue());
                model.setApiName(parameter.getApi().getAgyAcc());
                model.setParamater(JSON.toJSONString(parameter));
                n2GameProcessor.executeJob(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 切割时间，生成API调用
     *
     * @param paramsStrs
     * @return
     */
    private List<N2RequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_N2_EGAME_DELAY);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_N2_EGAME_DELAY);
        List<N2RequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(startcal.getTime(), endcal.getTime(), ApplicationConstant.CONSTANT_N2_EGAME_INTEVAL)) {
            N2RequestParameter parameter = new N2RequestParameter();
            parameter.setStartdate(strs[0]);
            parameter.setEnddate(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     *
     * @param parameterList
     * @param paramsStrs
     * @return
     * @throws Exception
     */
    protected List<N2RequestParameter> prepareApiLine(List<N2RequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<N2RequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_N2.getValue(),
                paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (N2RequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                N2RequestParameter par = (N2RequestParameter) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }
}

