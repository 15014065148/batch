package com.eveb.saasops.batch.game.hg.scheduled;

import com.alibaba.fastjson.JSON;
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
@JobHander(value = "hgJobHandler")
public class HgJobHandler extends JobHandler {

    @Autowired
    private HGProcessor processor;
    @Autowired
    private RptService rptService;

    SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.MATCH_TEMPLATE);

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<HGRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<HGRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<HGRequestParameter> parameterList) throws Exception {
        for (HGRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_HG.getValue());
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
    private List<HGRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        List<HGRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(startcal.getTime(), endcal.getTime(), ApplicationConstant.CONSTANT_HG_INTEVAL)) {
            HGRequestParameter parameter = new HGRequestParameter();
            parameter.setDateval(strs[0].substring(0,10));
            parameter.setTimeRange(DateUtil.parse(strs[0],DateUtil.MATCH_TEMPLATE).getHours()+"");
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<HGRequestParameter> prepareApiLine(List<HGRequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<HGRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_HG.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (HGRequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                HGRequestParameter par = (HGRequestParameter) parameter.clone();
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
