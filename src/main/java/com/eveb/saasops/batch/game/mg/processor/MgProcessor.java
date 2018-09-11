package com.eveb.saasops.batch.game.mg.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg.domain.MgBetLog;
import com.eveb.saasops.batch.game.mg.domain.MgRequestParameter;
import com.eveb.saasops.batch.game.mg.request.MgRequest;
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
public class MgProcessor {

    @Autowired
    private MgRequest request;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private MgRptProcess rptProcess;

    public Integer process(MgRequestParameter parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr=new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
//        StringBuffer rptInsstr=new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        for(MgBetLog model:request.request(parameter))
        {
            counts++;
            /**无效注单不进行处理**/
            if(model.getType().equals(GameCodeConstants.CONSTANT_CODE_MG_REFUND))
                    continue;
            //小写
            model.setMbrUsername(model.getMbrUsername().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getMbrUsername().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setMbrUsername(model.getMbrUsername().substring(fore.length(), model.getMbrUsername().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptProcess.processRpt(model);
            rptList.add(rptProcess.processRpt(model));
//            rptInsstr.append(rptProcess.processRpt(model));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
//        rptElasticService.insertList(rptInsstr.toString());
        return counts;
    }

    @Async("mgAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), MgRequestParameter.class)));
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
    private String toInsertString(MgBetLog object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.MG_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.MG_TYPE+"\", \"_id\" : \"" + object.getColId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
