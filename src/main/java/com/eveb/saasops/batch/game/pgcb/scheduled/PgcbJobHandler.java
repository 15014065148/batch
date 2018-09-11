package com.eveb.saasops.batch.game.pgcb.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pgcb.domain.PgcbRequestParameterModel;
import com.eveb.saasops.batch.game.pgcb.processor.PgcbProcessor;
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
@JobHander(value = "pgcbJobHandler")
public class PgcbJobHandler extends JobHandler {

    @Autowired
    private PgcbProcessor processor;
    @Autowired
    private RptService rptService;

    SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.FORMAT_18_DATE_TIME);

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs = prepareArguments(strings);
        List<PgcbRequestParameterModel> list = prepareTimePeriod(paramsStrs);
        List<PgcbRequestParameterModel> paraList = prepareApiLine(list, paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<PgcbRequestParameterModel> parameterList) throws Exception {
        for (PgcbRequestParameterModel parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_PGCB.getValue());
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
    private List<PgcbRequestParameterModel> prepareTimePeriod(Object[] paramsStrs) {
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_PGCB_INTEVAL);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_PGCB_INTEVAL);
        List<PgcbRequestParameterModel> parameters = new ArrayList<>();
        for (String[] strs : cutDate(startcal.getTime(), endcal.getTime(), ApplicationConstant.CONSTANT_PGCB_INTEVAL)) {
            PgcbRequestParameterModel parameter = new PgcbRequestParameterModel();
            parameter.setStartDate(DateUtil.parse(strs[0], DateUtil.FORMAT_18_DATE_TIME));
            parameter.setEndDate(DateUtil.parse(strs[1], DateUtil.FORMAT_18_DATE_TIME));
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<PgcbRequestParameterModel> prepareApiLine(List<PgcbRequestParameterModel> parameterList, Object[] paramsStrs) throws Exception {
        List<PgcbRequestParameterModel> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_PGCB.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (PgcbRequestParameterModel parameter : parameterList) {
            for (TGmApi api : apiList) {
                PgcbRequestParameterModel par = (PgcbRequestParameterModel) parameter.clone();
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
