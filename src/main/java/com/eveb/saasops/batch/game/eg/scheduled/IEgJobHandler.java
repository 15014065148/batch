package com.eveb.saasops.batch.game.eg.scheduled;

import com.eveb.saasops.batch.game.eg.domain.EgRequestParameter;
import com.eveb.saasops.batch.game.eg.processor.EgProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IEgJobHandler extends JobHandler {

    @Autowired
    private RptService rptService;
    @Autowired
    private EgProcessor processor;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs=prepareArguments(strings);
        List<EgRequestParameter> paraList=prepareApiLine(paramsStrs);
        return doExecute(paraList);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<EgRequestParameter> parameterList) throws Exception {
        for (EgRequestParameter parameter : parameterList) {
            try {
                processor.process(parameter);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<EgRequestParameter> prepareApiLine(Object[] paramsStrs) throws Exception {
        List<EgRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_EG.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (TGmApi api : apiList) {
            EgRequestParameter par =new EgRequestParameter() ;
            par.setApi(api);
            list.add(par);
        }
        return list;
    }

    @Override
    public String dateFormat(Date date)
    {
        return dateFormat.format(date);
    }
}
