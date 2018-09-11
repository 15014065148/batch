package com.eveb.saasops.batch.game.agin.scheduled;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.agin.processor.*;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
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
 * @Date: 9:59 2017/12/28
 **/
@Slf4j
@Service
public class IAginJobHandler extends JobHandler {

    @Autowired
    public SysService sysService;
    @Autowired
    private RptService rptService;
    @Autowired
    private AginLiveBetLogProcessor liveBetLogProcessor;
    @Autowired
    private AginHunterBetLogProcessor hunterBetLogProcessor;
    @Autowired
    private AginSlotBetLogProcessor slotBetLogProcessor;
    @Autowired
    private AginLiveCardResultProcessor aginLiveCardResultProcessor;

    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMddHHmm");

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        List<AginRequestParameterModel> list = new ArrayList<>();
        AginRequestParameterModel parameter = prepareParameter();
        Object[] objects = prepareArguments(strings);
        list = prepareTimePeriod(parameter, objects);
        list = prepareApiLine(list, objects);
        return doExecute(list);
    }

    public AginRequestParameterModel prepareParameter() {
        AginRequestParameterModel parameter = new AginRequestParameterModel();
        return parameter;
    }

    /**
     * 处理时间周期，需要增加或者减少
     * 拆分1分钟
     * 拆分跨天的情况
     */
    protected List<AginRequestParameterModel> prepareTimePeriod(AginRequestParameterModel parameter, Object[] paramsStrs) throws Exception {
        /***初始化时间***/
        List<AginRequestParameterModel> list = new ArrayList<>();
        //时间处理，生成每1分钟一个的时间文件名
        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        int range = 1;//间隔一分钟
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_DELAY);
        startcal.add(Calendar.MINUTE, -inteval);
        //美东时间差
        startcal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String str = simpleFormat.format(startcal.getTime());
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_DELAY);
        endcal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String endstr = simpleFormat.format(endcal.getTime());
        //判断是否跨天
        if (!str.substring(0, 8).equals(endstr.substring(0, 8))) {
            List<String> fileList=new ArrayList<>();
            fileList.addAll(agCutDate(startcal.getTime(), endcal.getTime(), range));
            AginRequestParameterModel model=(AginRequestParameterModel)parameter.clone();
            model.setRemotePath(model.getRemotePath()+str.substring(0, 8));
            model.setFileName(fileList);//前一天
            AginRequestParameterModel modelAfter=(AginRequestParameterModel)parameter.clone();
            modelAfter.setRemotePath(modelAfter.getRemotePath()+endstr.substring(0, 8));
            modelAfter.setFileName(fileList);//后一天
            list.add(model);
            list.add(modelAfter);
        } else {
            AginRequestParameterModel model = (AginRequestParameterModel) parameter.clone();
            model.setRemotePath(model.getRemotePath() + str.substring(0, 8));
            model.setFileName(agCutDate(startcal.getTime(), endcal.getTime(), range));
            list.add(model);
        }
        return list;
    }

    /**
     * 处理Api线路前缀
     */
    protected List<AginRequestParameterModel> prepareApiLine(List<AginRequestParameterModel> parameters, Object[] objects) throws Exception {
        List<AginRequestParameterModel> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_AGIN.getValue(), objects[indexApi] != null ? (List<String>) objects[indexApi] : null);
        for (AginRequestParameterModel model : parameters) {
            for (TGmApi api : apiList) {
                Map apimap = (Map) JSON.parse(api.getSecureCode());
                AginRequestParameterModel apimodel = (AginRequestParameterModel) model.clone();
                apimodel.setUrl(apimap.get("url").toString());
                apimodel.setUsername(apimap.get("username").toString());
                apimodel.setPassword(apimap.get("password").toString());
                apimodel.setApi(api);
                list.add(apimodel);
            }
        }
        return list;
    }

    /***
     * 初始化处理类型
     * @param jobModel
     * @return
     */
    public void initBetProcessor(JobFailMessageModel jobModel) throws Exception {
        AginRequestParameterModel parameter = JSON.parseObject(jobModel.getParamater(), AginRequestParameterModel.class);
        if (parameter.getIndexName().equals(ElasticSearchConstant.AGIN_INDEX_LIVECARDRESULT)) {
            aginLiveCardResultProcessor.executeJob(jobModel);
        } else if (parameter.getRemotePath().contains("AGIN") || parameter.getRemotePath().contains("SBTA")) {
            liveBetLogProcessor.executeJob(jobModel);
            return;
        } else if (parameter.getRemotePath().contains("HUNTER")) {
            hunterBetLogProcessor.executeJob(jobModel);
            return;
        } else if (parameter.getRemotePath().contains("XIN")) {
            slotBetLogProcessor.executeJob(jobModel);
            return;
        }


    }

    public ReturnT<String> doExecute(List<AginRequestParameterModel> list) throws Exception {
        for (AginRequestParameterModel parameter : list) {
            JobFailMessageModel model = new JobFailMessageModel();
            model.setApiName(parameter.getApi().getAgyAcc());
            model.setPlatform(PlatFromEnum.Enum_AGIN.getValue());
            model.setParamater(JSON.toJSONString(parameter));
            try {
                initBetProcessor(model);
            } catch (Exception e) {
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    public ReturnT<String> doFailExecute(List<JobFailMessageModel> list) throws Exception {
        for (JobFailMessageModel model : list) {
            try {
                initBetProcessor(model);
            } catch (Exception e) {
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /****
     * 切割时间
     * @param startDate
     * @param endDate
     * @param inteval
     * @return
     */
    public List<String> agCutDate(Date startDate, Date endDate, int inteval) {
        List<String> dates = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        int between = (int) (Math.abs(startDate.getTime() - endDate.getTime()) / 1000 / 60);//除以1000/60是为了转换成分
        for (int i = 1; i <= Math.ceil((double) between / inteval); i++) {
            Date startdate = startCal.getTime();
            String start = simpleFormat.format(startdate);
            startCal.add(Calendar.MINUTE, inteval);
            dates.add(start + ".xml");
        }
        return dates;
    }
}
