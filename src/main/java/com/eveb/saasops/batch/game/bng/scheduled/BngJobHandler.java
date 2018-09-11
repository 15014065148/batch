package com.eveb.saasops.batch.game.bng.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bng.bean.BngRequestParam;
import com.eveb.saasops.batch.game.bng.processor.BngProcessor;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@JobHander(value = "BngJobHandler")
public class BngJobHandler extends JobHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
    @Autowired
    private RptService rptService;
    @Autowired
    BngProcessor processor;
    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        log.info("BNG 数据开始拉去");
        Object[] paramsStrs=prepareArguments(strings);
        List<BngRequestParam> parameters = prepareTimePeriod(paramsStrs);
        parameters =prepareApiLine(parameters,paramsStrs);
        return doExecute(parameters);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<BngRequestParam> parameterList) throws Exception {
        for (BngRequestParam parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_BNG.getValue());
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
    private List<BngRequestParam> prepareTimePeriod(Object[] paramsStrs) {
        List<BngRequestParam> parameters = new ArrayList<>();
        for (String[] strs : cutDate(ApplicationConstant.CONSTANT_BNG_INTEVAL,ApplicationConstant.CONSTANT_BNG_DELAY, paramsStrs)) {
            BngRequestParam parameter = new BngRequestParam();
            parameter.setStartDate(strs[0]);
            parameter.setEndDate(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    public List<BngRequestParam>  prepareApiLine(List<BngRequestParam> parameterList, Object[] paramsStrs) throws Exception {
        List<BngRequestParam> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_BNG.getValue(),
                paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (BngRequestParam parameter : parameterList) {
            for (TGmApi api : apiList) {
                BngRequestParam par = (BngRequestParam) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    /**东八区转化为0时区*/
    @Override
    public String dateFormat(Date date)
    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.HOUR_OF_DAY,-8);
//        date =calendar.getTime();
        return dateFormat.format(date);
    }
}
