package com.eveb.saasops.batch.game.fg.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.fg.domain.FgBetLog;
import com.eveb.saasops.batch.game.fg.domain.FgGameTypeUtil;
import com.eveb.saasops.batch.game.fg.domain.FgRequestParameter;
import com.eveb.saasops.batch.game.fg.request.FgRequest;
import com.eveb.saasops.batch.game.pb.domain.PbBetLog;
import com.eveb.saasops.batch.game.pb.domain.PbRequestParameter;
import com.eveb.saasops.batch.game.pb.processor.PbRptBetProcessor;
import com.eveb.saasops.batch.game.pb.request.PbRequest;
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
 * FG平台，2018-07-18
 */
@Slf4j
@Component
public class FgProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private FgRequest fgRequest;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;


    @Async("fgAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            for (String type : FgGameTypeUtil.fgTypes()) {//每条线路的游戏类型数据
                FgRequestParameter parameter = JSON.parseObject(jobmodel.getParamater(), FgRequestParameter.class);
                parameter.getApi().setApiUrl(parameter.getApi().getApiUrl() + FgGameTypeUtil.FG_SERVER_URL + type);
                jobmodel.setCounts(jobmodel.getCounts() + process(parameter));
            }

        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(FgRequestParameter para) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(para.getApi().getId());
        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (FgBetLog item : fgRequest.getBetList(para)) {
            counts++;
            //小写
            item.setNickname(item.getNickname().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getNickname().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setNickname(item.getNickname().substring(fore.length(), item.getNickname().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(para.getApi().getApiName());
            if (item.getStart_time() != null) { //时间戳转
                item.setStart_time(DateUtil.timeStamp2Date(item.getStart_time(), DateUtil.FORMAT_38_DATE_TIME));
            }
            if (item.getEnd_time() != null) {
                item.setEnd_time(DateUtil.timeStamp2Date(item.getEnd_time(), DateUtil.FORMAT_38_DATE_TIME));
            }
            if (item.getBuy_time() != null) {
                item.setBuy_time(DateUtil.timeStamp2Date(item.getBuy_time(), DateUtil.FORMAT_38_DATE_TIME));
            }
            insstr.append(toInsertString(item));
            rptList.add(FgRptBetProcessor.process(item));
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
    private String toInsertString(FgBetLog object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.FG_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.FG_TYPE + "\", \"_id\" : \"" + object.getId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, DateUtil.FORMAT_38_DATE_TIME));
        string.append("\n");
        return string.toString();
    }

}
