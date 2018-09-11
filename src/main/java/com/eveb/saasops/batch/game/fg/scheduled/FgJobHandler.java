package com.eveb.saasops.batch.game.fg.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.fg.domain.FgGameTypeUtil;
import com.eveb.saasops.batch.game.fg.domain.FgRequestParameter;
import com.eveb.saasops.batch.game.fg.process.FgProcessor;
import com.eveb.saasops.batch.game.hg.domain.HGRequestParameter;
import com.eveb.saasops.batch.game.hg.process.HGProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.util.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@JobHander(value = "fgJobHandler")
public class FgJobHandler extends JobHandler {

    @Autowired
    private FgProcessor processor;
    @Autowired
    private RptService rptService;

    SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.MATCH_TEMPLATE);

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<FgRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<FgRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<FgRequestParameter> parameterList) throws Exception {
        for (FgRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_FG.getValue());
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
    private List<FgRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_FG_INTEVAL);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_FG_INTEVAL);
        List<FgRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(startcal.getTime(), endcal.getTime(), ApplicationConstant.CONSTANT_FG_INTEVAL)) {
            FgRequestParameter parameter = new FgRequestParameter();
            //String startDate = "1532417400";
            //String endDate = "1532421600";
            String startDate = DateUtil.date2TimeStamp(strs[0],DateUtil.MATCH_TEMPLATE);
            String endDate = DateUtil.date2TimeStamp(strs[1],DateUtil.MATCH_TEMPLATE);
            parameter.setStartDate(startDate);
            parameter.setEndDate(endDate);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<FgRequestParameter> prepareApiLine(List<FgRequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<FgRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_FG.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (FgRequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                FgRequestParameter par = (FgRequestParameter) parameter.clone();
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
