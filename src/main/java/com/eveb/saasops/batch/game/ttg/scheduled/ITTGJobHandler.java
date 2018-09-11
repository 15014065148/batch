package com.eveb.saasops.batch.game.ttg.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.game.ttg.domain.TTGRequestParameter;
import com.eveb.saasops.batch.game.ttg.process.TTGGameProcessor;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class ITTGJobHandler extends JobHandler {
    @Autowired
    private RptService rptService;

    @Autowired
    private TTGGameProcessor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception{
        Object[] paramsStrs=prepareArguments(strings);
        List<TTGRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<TTGRequestParameter> TTGRequestParameters=prepareApiLine(list,paramsStrs);
        return doExecute(TTGRequestParameters);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<TTGRequestParameter> TTGRequestParameters) {
        for (TTGRequestParameter parameter : TTGRequestParameters) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_TTG.getValue());
                model.setApiName(parameter.getApi().getApiName());
                model.setParamater(JSON.toJSONString(parameter));
                processor.executeJob(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 获取Api线路前缀
     */
    protected List<TTGRequestParameter> prepareApiLine(List<TTGRequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<TTGRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_TTG.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (TTGRequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                TTGRequestParameter par = (TTGRequestParameter) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }
    /****
     * 切割时间，生成API调用
     * @param paramsStrs
     * @return
     */
    private List<TTGRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        //获取十分钟之前的记录
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_TTG_DELAY);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_TTG_DELAY);
        List<TTGRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(startcal.getTime(), endcal.getTime(), ApplicationConstant.CONSTANT_TTG_INTEVAL)) {
            TTGRequestParameter parameter = new TTGRequestParameter();
            parameter.setStartdate(strs[0]);
            parameter.setEnddate(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }
}
