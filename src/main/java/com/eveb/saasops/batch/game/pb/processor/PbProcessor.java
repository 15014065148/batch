package com.eveb.saasops.batch.game.pb.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pb.domain.PbBetLog;
import com.eveb.saasops.batch.game.pb.domain.PbRequestParameter;
import com.eveb.saasops.batch.game.pb.request.PbRequest;
import com.eveb.saasops.batch.game.pt.domain.PTBetLog;
import com.eveb.saasops.batch.game.pt.domain.PtRequestParameter;
import com.eveb.saasops.batch.game.pt.process.PTRptBetProcessor;
import com.eveb.saasops.batch.game.pt.request.PTRequest;
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
public class PbProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private PbRequest request;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;

    @Async("pbAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), PbRequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(PbRequestParameter para)throws Exception {
        Integer counts=0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(para.getApi().getId());
        StringBuffer insstr=new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (PbBetLog item : request.request(para)) {
            counts++;
            //小写
            item.setUserCode(item.getUserCode().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getUserCode().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setUserCode(item.getUserCode().substring(fore.length(), item.getUserCode().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(para.getApi().getAgyAcc());
            insstr.append(toInsertString(item));
            rptList.add(PbRptBetProcessor.process(item));
        }
        rptElasticService.insertList(insstr.toString());
        /**因不存在异动单，所以进行插入和更新**/
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    private String toInsertString(PbBetLog object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.PB_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.PB_TYPE+"\", \"_id\" : \"" + object.getWagerId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }

}
