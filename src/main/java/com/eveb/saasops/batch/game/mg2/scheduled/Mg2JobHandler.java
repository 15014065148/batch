package com.eveb.saasops.batch.game.mg2.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg2.bean.Mg2RequestParameter;
import com.eveb.saasops.batch.game.mg2.processor.Mg2Processor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@JobHander(value = "Mg2JobHandler")
public class Mg2JobHandler extends JobHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    @Autowired
    Mg2Processor processor;
    @Autowired
    private RptService rptService;

    /**
     * 统计mg2 所有信息
     * @param strings
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<Mg2RequestParameter> mg2RequestParameters = prepareTimePeriod(paramsStrs);
        mg2RequestParameters =prepareApiLine(mg2RequestParameters,paramsStrs);
        return doExecute(mg2RequestParameters);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<Mg2RequestParameter> parameterList) throws Exception {
        for (Mg2RequestParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_MG2.getValue());
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

    /****
     * 切割时间，生成API调用
     * @param paramsStrs
     * @return
     */
    private List<Mg2RequestParameter> prepareTimePeriod(Object[] paramsStrs) {
        List<Mg2RequestParameter> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_MG2_INTEVAL, paramsStrs)) {
            Mg2RequestParameter parameter = new Mg2RequestParameter();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    public List<Mg2RequestParameter>  prepareApiLine(List<Mg2RequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<Mg2RequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_MG2.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (Mg2RequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                Mg2RequestParameter par = (Mg2RequestParameter) parameter.clone();
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
