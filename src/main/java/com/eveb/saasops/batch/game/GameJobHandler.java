package com.eveb.saasops.batch.game;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class GameJobHandler<D, BetLogModel> extends JobHandler {
    public int constantInteval;
    public String platFromValue = "";
    @Autowired
    protected Processor<D, BetLogModel> processor;
    @Autowired
    private RptService rptService;

    /**
     * 统计AB所有信息
     *
     * @param strings
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Object[] paramsStrs = prepareArguments(strings);
        List<GameParameter<D>> requestParameters = prepareTimePeriod(paramsStrs);
        requestParameters = prepareApiLine(requestParameters, paramsStrs);
        return doExecute(requestParameters);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<GameParameter<D>> parameterList) {
        for (GameParameter parameter : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(platFromValue);
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
    private List<GameParameter<D>> prepareTimePeriod(Object[] paramsStrs) {
        List<GameParameter<D>> parameters = new ArrayList<>();
        for (String[] strs : cutDate(constantInteval, paramsStrs)) {
            GameParameter<D> parameter = new GameParameter<>();
            parameter.setStartTime(strs[0]);
            parameter.setEndTime(strs[1]);
            parameters.add(parameter);
        }
        return parameters;
    }

    public List<GameParameter<D>> prepareApiLine(List<GameParameter<D>> parameterList, Object[] paramsStrs) throws Exception {
        List<GameParameter<D>> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(platFromValue, paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (GameParameter<D> parameter : parameterList) {
            for (TGmApi api : apiList) {
                GameParameter par = (GameParameter) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        list.stream().forEach(e -> {
            e.setHandlerName(paramsStrs[indexPara].toString());
        });
        return list;
    }
}
