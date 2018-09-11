package com.eveb.saasops.batch.game.gg.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.fg.domain.FgBetLog;
import com.eveb.saasops.batch.game.fg.domain.FgGameTypeUtil;
import com.eveb.saasops.batch.game.fg.domain.FgRequestParameter;
import com.eveb.saasops.batch.game.fg.process.FgRptBetProcessor;
import com.eveb.saasops.batch.game.fg.request.FgRequest;
import com.eveb.saasops.batch.game.gg.domain.GgBetLog;
import com.eveb.saasops.batch.game.gg.domain.GgRequestParameter;
import com.eveb.saasops.batch.game.gg.request.GgRequest;
import com.eveb.saasops.batch.game.hg.domain.HGRequestParameter;
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
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GG平台，2018-08-02
 */
@Slf4j
@Component
public class GgProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private GgRequest ggRequest;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;


    @Async("ggcbAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            Date now = new Date();
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(now);
            jobmodel.setLastTime(now);
            GgRequestParameter parameter = JSON.parseObject(jobmodel.getParamater(), GgRequestParameter.class);

            Date afterTime = DateUtil.addDateDay(DateUtil.parse(parameter.getEndDate(), DateUtil.FORMAT_18_DATE_TIME), 3);//加3天后的数据
            if (now.before(afterTime)) {
                parameter.setMethod(ApplicationConstant.GG_METHOD_BET);
            } else {
                parameter.setMethod(ApplicationConstant.GG_METHOD_BET_HISTORY);
            }
            jobmodel.setCounts(process(parameter));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(GgRequestParameter para) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(para.getApi().getId());
        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (GgBetLog item : ggRequest.getBetList(para)) {
            counts++;
            //小写
            String accountNo = item.getAccountno();
            if (StringUtil.isNotEmpty(accountNo.split(para.getApi().getAgyAcc())[1])) {
                item.setAccountno(accountNo.split(para.getApi().getAgyAcc())[1].toLowerCase());
            } else {
                item.setAccountno(accountNo);
            }

            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getAccountno().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setAccountno(item.getAccountno().substring(fore.length(), item.getAccountno().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(para.getApi().getApiName());
            insstr.append(toInsertString(item));
            rptList.add(GgRptBetProcessor.process(item));
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
    private String toInsertString(GgBetLog object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.GG_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.GG_TYPE + "\", \"_id\" : \"" + object.getAutoid() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, DateUtil.FORMAT_38_DATE_TIME));
        string.append("\n");
        return string.toString();
    }

}
