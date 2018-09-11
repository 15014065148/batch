package com.eveb.saasops.batch.game.png.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.png.domain.PngBetLong;
import com.eveb.saasops.batch.game.png.domain.PngRequestParameter;
import com.eveb.saasops.batch.game.png.request.PngRequest;
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
public class PngBetLogProcessor {

    @Autowired
    private PngRequest request;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptElasticRestService rptElasticService;

    @Async("pngAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), PngRequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(PngRequestParameter parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr = new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for (PngBetLong model : request.request(parameter)) {
            counts++;
            //小写
            model.setExternalUserId(model.getExternalUserId().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getExternalUserId().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setExternalUserId(model.getExternalUserId().substring(fore.length(), model.getExternalUserId().length()));
                }
            }
            /***PNG的账号存在webname字段**/
            model.setApiPrefix(parameter.getApi().getWebName());
            insstr.append(toInsertString(model));
            rptList.add(PngRptProcessor.process(model));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    private String toInsertString(PngBetLong object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.PNG_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.PNG_TYPE+"\", \"_id\" : \"" + object.getTransactionId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
