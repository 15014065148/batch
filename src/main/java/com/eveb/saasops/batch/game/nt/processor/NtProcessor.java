package com.eveb.saasops.batch.game.nt.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.nt.domain.NTBetLog;
import com.eveb.saasops.batch.game.nt.domain.NtRequestParameter;
import com.eveb.saasops.batch.game.nt.request.NtRequest;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
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
public class NtProcessor {

    @Autowired
    private NtRequest request;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptElasticRestService rptElasticService;

    public Integer process(NtRequestParameter parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr = new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for (NTBetLog model : request.request(parameter)) {
            /**转账注单不进行处理**/
            if (GameCodeConstants.CONSTANT_CODE_NT_TRANSFTYPE_IN.equals(model.getTranType())||GameCodeConstants.CONSTANT_CODE_NT_TRANSFTYPE_OUT.equals(model.getTranType()))
                continue;
            //小写
            counts++;
            model.setUserId(model.getUserId().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getUserId().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setUserId(model.getUserId().substring(fore.length(), model.getUserId().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptList.add(NtRptBetProcessor.process(model));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    @Async("ntAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), NtRequestParameter.class)));
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
    private String toInsertString(NTBetLog object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.NT_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.NT_TYPE + "\", \"_id\" : \"" + object.getId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
