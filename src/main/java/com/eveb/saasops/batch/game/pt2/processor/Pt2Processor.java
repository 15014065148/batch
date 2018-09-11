package com.eveb.saasops.batch.game.pt2.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pt2.domain.Pt2BetLog;
import com.eveb.saasops.batch.game.pt2.domain.Pt2RequestParameter;
import com.eveb.saasops.batch.game.pt2.request.Pt2Request;
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
public class Pt2Processor {

    @Autowired
    private Pt2Request request;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptService rptService;
    @Autowired
    public RptElasticRestService rptElasticService;

    @Async("pt2AsyncExecutor")
    public void executeJob(JobFailMessageModel model)throws Exception
    {
        try {
            model.setTimes(model.getTimes() + 1);
            model.setFirstTime(new Date());
            model.setLastTime(new Date());
            model.setCounts(process(JSON.parseObject(model.getParamater(), Pt2RequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(model);
            log.error(e.getMessage());
            throw e;
        }
        model.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(model);

    }

    public Integer process(Pt2RequestParameter parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr=new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for(Pt2BetLog model:request.request(parameter))
        {
            counts++;
            //小写
            model.setPlayerCode(model.getPlayerCode().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getPlayerCode().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setPlayerCode(model.getPlayerCode().substring(fore.length(), model.getPlayerCode().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptList.add(Pt2RptBetProcessor.process(model));
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
    private String toInsertString(Pt2BetLog object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.PT2_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.PT2_TYPE+"\", \"_id\" : \"" + object.getRoundId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
