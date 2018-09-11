package com.eveb.saasops.batch.game.pb.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.opus.domain.OpusRequestParameter;
import com.eveb.saasops.batch.game.pb.domain.PbRequestParameter;
import com.eveb.saasops.batch.game.pb.processor.PbProcessor;
import com.eveb.saasops.batch.game.pt.domain.PtRequestParameter;
import com.eveb.saasops.batch.game.pt.process.PTGameProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
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
public class IPbJobHandler extends JobHandler {

    @Autowired
    private PbProcessor processor;
    @Autowired
    private RptService rptService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss");

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<PbRequestParameter> list=prepareTimePeriod(paramsStrs);
        List<PbRequestParameter> paraList=prepareApiLine(list,paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<PbRequestParameter> parameterList) throws Exception {
        for (PbRequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_PB.getValue());
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
    private List<PbRequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<PbRequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_PB_INTEVAL,ApplicationConstant.CONSTANT_PB_DELAY, paramsStrs)) {
            PbRequestParameter parameter = new PbRequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<PbRequestParameter> prepareApiLine(List<PbRequestParameter> parameters, Object[] paramsStrs) throws Exception {
        List<PbRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_PB.getValue() , paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (PbRequestParameter pb : parameters) {
            for (TGmApi api : apiList) {
                PbRequestParameter par =(PbRequestParameter)pb.clone();
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
