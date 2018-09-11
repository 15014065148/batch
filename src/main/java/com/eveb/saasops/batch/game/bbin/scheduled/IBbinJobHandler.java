package com.eveb.saasops.batch.game.bbin.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
import com.eveb.saasops.batch.game.bbin.processor.BbinBetLogProcessors;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.eveb.saasops.batch.sys.service.SysService;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 13:44 2017/12/11
 **/

@Slf4j
@Service
public  class IBbinJobHandler extends JobHandler {

    @Autowired
    public SysService sysService;
    @Autowired
    private RptService rptService;
    @Autowired
    public BbinBetLogProcessors processors;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<BBINRequestParameter> prepareParameter(int[] range, Object[] params) {
        return new ArrayList<>();
    }

    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        List<BBINRequestParameter> parameterList = new ArrayList<>();
        int[] range = {10};//默认时间范围，拉取数据最大获取的时间段
        Object[] paramsStrs;
        //分解参数
        paramsStrs = prepareArguments(params);
        //初始化请求参数
        List<BBINRequestParameter> parameter = prepareParameter(range, paramsStrs);
        //处理时间
        parameterList = prepareTimePeriod(parameter, paramsStrs, range[0]);
        //处理Api前缀
        parameterList = prepareApiLine(parameterList, paramsStrs);
        //执行
        return doExecute(parameterList);
    }


    /**
     * 处理时间周期，需要增加或者减少
     * 拆分n分钟以上的（比如n=5，根据不同的接口定义）
     * 拆分跨天的情况
     */
    protected List<BBINRequestParameter> prepareTimePeriod(List<BBINRequestParameter> parameters, Object[] paramsStrs, int range) throws Exception {
        List<BBINRequestParameter> parameterList = new ArrayList<>();
        List<String[]> dateList = new ArrayList<>();
        /***初始化时间***/
        //时间处理，超过5分钟或者跨天的，拆成若干个
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_DELAY);
        /***获取六分钟的数据**/
        startcal.add(Calendar.MINUTE, -inteval);
        //美东时间差
        startcal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String str = dateFormat.format(startcal.getTime());
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_DELAY);
        endcal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String endstr = dateFormat.format(endcal.getTime());
        dateList.addAll(cutDate(startcal.getTime(), endcal.getTime(), range));
        //判断是否跨天
        if (!str.substring(0, 10).equals(endstr.substring(0, 10))) {
            dateList.clear();
            dateList.addAll(cutDate(startcal.getTime(), dateFormat.parse(str.substring(0, 10) + " 23:59:59"), range));//前一天
            dateList.addAll(cutDate(dateFormat.parse(endstr.substring(0, 10) + " 00:00:00"), endcal.getTime(), range));//后一天
        }
        for (BBINRequestParameter parameter : parameters) {
            for (String[] dateStr : dateList) {
                BBINRequestParameter par = (BBINRequestParameter) parameter.clone();
                par.setRounddate(dateStr[0].substring(0, 10));
                par.setStarttime(dateStr[0].substring(11));
                par.setEndtime(dateStr[1].substring(11));
                par.setDate(dateStr[0].substring(0, 10));
                parameterList.add(par);
            }
        }
        return parameterList;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<BBINRequestParameter> prepareApiLine(List<BBINRequestParameter> parameterList, Object[] paramsStrs) throws Exception {
        List<BBINRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_BBIN.getValue(), paramsStrs[indexApi] != null ? (List<String>) paramsStrs[indexApi] : null);
        for (BBINRequestParameter parameter : parameterList) {
            for (TGmApi api : apiList) {
                BBINRequestParameter par = (BBINRequestParameter) parameter.clone();
                par.setApi(api);
                list.add(par);
            }
        }
        return list;
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<BBINRequestParameter> parameterList) throws Exception {
        for (BBINRequestParameter par : parameterList) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setApiName(par.getApi().getAgyAcc());
                model.setPlatform(PlatFromEnum.Enum_BBIN.getValue());
                model.setParamater(JSON.toJSONString(par));
                processors.execute(model);
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
                processors.execute(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }
}
