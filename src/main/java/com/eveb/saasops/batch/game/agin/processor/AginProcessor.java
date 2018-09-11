package com.eveb.saasops.batch.game.agin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.agin.domain.AGINHunterBetLogModel;
import com.eveb.saasops.batch.game.agin.domain.AginBetLogModel;
import com.eveb.saasops.batch.game.agin.domain.AginLiveCardResultModel;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @Author: Miracle
 * @Description:
 * @Date: 11:55 2017/12/28
 **/
@Slf4j
@Component
public class AginProcessor {

    @Autowired
    public SysService sysService;
    @Autowired
    public RptElasticRestService rptElasticRestService;

    public Integer process(AginRequestParameterModel parameter) throws Exception {
        return 0;
    }

    @Async("agAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), AginRequestParameterModel.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    /***
     * 转换成插入语句
     * @param object
     * @return
     */
    public String toInsertString(String index, String type, Object object) {
        StringBuffer string = new StringBuffer();
        if (object instanceof AGINHunterBetLogModel) {
            string.append("{ \"index\" : { \"_index\" : \"" + index + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + ((AGINHunterBetLogModel) object).getTradeNo() + "\" }}");
        } else if (object.getClass()==AginLiveCardResultModel.class) {
            string.append("{ \"index\" : { \"_index\" : \"" + index + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + ((AginLiveCardResultModel) object).getGmcode() + "\" }}");
        } else {
            string.append("{ \"index\" : { \"_index\" : \"" + index + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + ((AginBetLogModel) object).getBillNo() + "\" }}");
        }
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
