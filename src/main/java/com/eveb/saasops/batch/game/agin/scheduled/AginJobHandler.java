package com.eveb.saasops.batch.game.agin.scheduled;

import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@JobHander(value = "aginJobHandler")
public class AginJobHandler extends IAginJobHandler {

    @Override
    public AginRequestParameterModel prepareParameter() {
        AginRequestParameterModel parameter = new AginRequestParameterModel();
        parameter.setRemotePath("/AGIN/");
        parameter.setIndexName(ElasticSearchConstant.AGIN_INDEX);
        parameter.setTypeName(ElasticSearchConstant.AGIN_TYPE);
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
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_AGIN_LIVE_DELAY);
        startcal.add(Calendar.MINUTE, -inteval);
        //美东时间差
        startcal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String str = simpleFormat.format(startcal.getTime());
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_AGIN_LIVE_DELAY);
        endcal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String endstr = simpleFormat.format(endcal.getTime());
        //判断是否跨天
        if (!str.substring(0, 8).equals(endstr.substring(0, 8))) {
            AginRequestParameterModel model = (AginRequestParameterModel) parameter.clone();
            model.setRemotePath(model.getRemotePath() + str.substring(0, 8));
            model.setFileName(agCutDate(startcal.getTime(), simpleFormat.parse(str.substring(0, 8) + " 23:59:59"), range));//前一天
            AginRequestParameterModel modelAfter = (AginRequestParameterModel) parameter.clone();
            modelAfter.setRemotePath(modelAfter.getRemotePath() + endstr.substring(0, 8));
            modelAfter.setFileName(agCutDate(simpleFormat.parse(endstr.substring(0, 8) + " 00:00:00"), endcal.getTime(), range));//后一天
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

}
