package com.eveb.saasops.batch.game.pt.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pt.domain.PTBetLog;
import com.eveb.saasops.batch.game.pt.domain.PtRequestParameter;
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

import java.util.*;
@Slf4j
@Component
public class PTGameProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private PTRequest ptRequest;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;

    @Async("ptAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), PtRequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(PtRequestParameter para)throws Exception {
        Integer counts=0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(para.getApi().getId());
        StringBuffer insstr=new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (PTBetLog item : ptRequest.getBetList(para)) {
            counts++;
            //小写
            item.setPlayername(item.getPlayername().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getPlayername().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setPlayername(item.getPlayername().substring(fore.length(), item.getPlayername().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(para.getApi().getAgyAcc());
//            PTBetLog obj = ptService.getBetLogRecord(item.getGamecode());
            rptList.add(PTRptBetProcessor.process(item));
//            /**如果不存在则进行插入操作*/
//            if (obj == null) {
                insstr.append(toInsertString(item));
//            } else {
//                /**如果已经存在且数据不一致则插入异常表记录*/
//                if (!item.equals(obj)) {
//                    ptService.insertMdfSingleton(item);
//                }
//            }
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
    private String toInsertString(PTBetLog object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.PT_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.PT_TYPE+"\", \"_id\" : \"" + object.getGamecode() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }

}
