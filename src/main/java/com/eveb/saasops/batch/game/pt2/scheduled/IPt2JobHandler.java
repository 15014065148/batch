package com.eveb.saasops.batch.game.pt2.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pt2.domain.Pt2RequestParameter;
import com.eveb.saasops.batch.game.pt2.processor.Pt2Processor;
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

public class IPt2JobHandler extends JobHandler {

    @Autowired
    private RptService rptService;
    @Autowired
    private Pt2Processor processor;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs = prepareArguments(strings);
        List<Pt2RequestParameter> list = prepareTimePeriod(paramsStrs);
        List<Pt2RequestParameter> paraList = prepareApiLine(list, paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<Pt2RequestParameter> parameterList) throws Exception {
        for (Pt2RequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_PT2.getValue());
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
    private List<Pt2RequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<Pt2RequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_PT2_INTEVAL,ApplicationConstant.CONSTANT_PT2_DELAY,paramsStrs)) {
            Pt2RequestParameter parameter = new Pt2RequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<Pt2RequestParameter> prepareApiLine(List<Pt2RequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<Pt2RequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_PT2.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (Pt2RequestParameter pt : parameters) {
            for (TGmApi api : apiList) {
                Pt2RequestParameter par = (Pt2RequestParameter) pt.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    public String dateFormat(Date date) {
        return sdf.format(date);
    }
}
