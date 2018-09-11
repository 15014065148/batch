package com.eveb.saasops.batch.game.hg.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.hg.domain.HGBetLog;
import com.eveb.saasops.batch.game.hg.domain.HGRequestParameter;
import com.eveb.saasops.batch.game.hg.request.HGRequest;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

/**
 * HG接口；2018-07-10
 */
@Slf4j
@Component
public class HGProcessor {
    @Autowired
    private RptService rptService;
    @Autowired
    private HGRequest hGRequest;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;

    @Async("hgAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), HGRequestParameter.class)));
        } catch (Exception e) {
             log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(HGRequestParameter parameter) throws Exception {
        Integer counts=0;
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        List<HGBetLog> hgBetLogList = hGRequest.getBetList(parameter);
        if(hgBetLogList != null && hgBetLogList.size() > 0){
            for (HGBetLog model :hgBetLogList ){
                counts++;

                model.setAccountId(model.getAccountId().toLowerCase());
                for (String fore : prefixList) {
                    fore=fore.toLowerCase();
                    //如果存在前缀则进行截取
                    if (model.getAccountId().startsWith(fore)) {
                        model.setSitePrefix(fore);
                        model.setAccountId(model.getAccountId().substring(fore.length(), model.getAccountId().length()).toLowerCase());
                        break;
                    }
                }

                model.setApiPrefix(parameter.getApi().getApiName());
                rptList.add(HGRptBetProcessor.process(model));
                model.setBetStartDate(DateUtil.format(DateUtil.parse(model.getBetStartDate(),DateUtil.FORMAT_28_DATE_TIME),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                model.setBetEndDate(DateUtil.format(DateUtil.parse(model.getBetEndDate(),DateUtil.FORMAT_28_DATE_TIME),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                insstr.append(toInsertString(model));
            }

            rptElasticService.insertList(insstr.toString());
            /**因不存在异动单，所以进行插入和更新**/
            rptElasticService.insertOrUpdateList(rptList);
        }
        return counts;
    }

    /**
     * 转换成插入或修改语句
     * @param hgBetLog
     * @return
     */
    private String toInsertString(HGBetLog hgBetLog) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.HG_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.HG_TYPE + "\", \"_id\" : \"" + hgBetLog.getBetId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(hgBetLog, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
