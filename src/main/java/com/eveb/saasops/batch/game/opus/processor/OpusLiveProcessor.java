package com.eveb.saasops.batch.game.opus.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.opus.domain.OpusLiveBetLog;
import com.eveb.saasops.batch.game.opus.domain.OpusRequestParameter;
import com.eveb.saasops.batch.game.opus.request.OpusRequest;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.processor.IProcessor;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OpusLiveProcessor extends IProcessor {

    @Autowired
    private OpusRequest request;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptElasticRestService rptElasticService;

    @Override
    public int processBet(String para) throws Exception {
        OpusRequestParameter parameter=JSON.parseObject(para,OpusRequestParameter.class);
        Integer counts=0;
        StringBuffer insstr = new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        Map map=(Map)JSON.parse(parameter.getApi().getSecureCode());
        List<RptBetModel> rptList = new ArrayList<>();
        for (OpusLiveBetLog model : request.requestLive(parameter)) {
            counts++;
            //小写 去掉OPUS自带的前缀
            model.setMember_code(model.getMember_code().replace(map.get("prefix").toString(),"").toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getMember_code().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setMember_code(model.getMember_code().substring(fore.length(), model.getMember_code().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptList.add(OpusLiveRptBetProcessor.process(model));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    @Override
    @Async("opusAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        executeJobs(jobmodel);
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    private String toInsertString(OpusLiveBetLog object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.OPUS_INDEX_LIVE + "\", \"_type\" : \"" + ElasticSearchConstant.OPUS_TYPE_LIVE + "\", \"_id\" : \"" + object.getTransaction_id() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
