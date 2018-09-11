package com.eveb.saasops.batch.game.t188.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.game.t188.domain.T188BetLog;
import com.eveb.saasops.batch.game.t188.domain.T188RequestParameter;
import com.eveb.saasops.batch.game.t188.request.T188Request;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class T188Processor {

    @Autowired
    private T188Request request;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptElasticRestService rptElasticService;

    public Integer process(T188RequestParameter parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr = new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for (T188BetLog model : request.request(parameter)) {
            counts++;
            //小写
            model.setUserCode(model.getUserCode().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getUserCode().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setUserCode(model.getUserCode().substring(fore.length(), model.getUserCode().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptList.add(T188RptProcessor.process(model));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    @Async("t188AsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), T188RequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    private String toInsertString(T188BetLog object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.T188_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.T188_TYPE + "\", \"_id\" : \"" + object.getWagerNo() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
