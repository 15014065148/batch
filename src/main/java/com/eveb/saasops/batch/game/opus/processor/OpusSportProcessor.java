package com.eveb.saasops.batch.game.opus.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.opus.domain.OpusRequestParameter;
import com.eveb.saasops.batch.game.opus.domain.OpusSportBetLog;
import com.eveb.saasops.batch.game.opus.request.OpusRequest;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class OpusSportProcessor extends IProcessor {

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
        Boolean isfinish=false;
        StringBuffer insstr = new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for (OpusSportBetLog model : request.requestSport(parameter)) {
            counts++;
            //小写
            model.setMember_id(model.getMember_id().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getMember_id().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setMember_id(model.getMember_id().substring(fore.length(), model.getMember_id().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            model.setDatetime(format(model.getDatetime()));
            model.setMatch_datetime(format(model.getMatch_datetime()));
            model.setTransaction_time(format(model.getTransaction_time()));
            model.setWinlost_datetime(format(model.getWinlost_datetime()));
            model.setLast_update(format(model.getLast_update()));
            insstr.append(toInsertString(model));
            rptList.add(OpusSportRptBetProcessor.process(model));
            if (GameCodeConstants.CONSTANT_CODE_OPUS_SPORT_WINLOST_STATUS_P.equals(model.getWinlost_status())) {
                isfinish=true;
            }
        }
        /**当拉取回来的数据中有未完成的注单时，需要记录，进行重试，直到所有的注单都有结果**/
        if(isfinish)
        {
            throw new Exception(ApplicationConstant.CONSTANT_ERR_MSG);
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

    public String format(String times)throws Exception
    {
        SimpleDateFormat sdf_english = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss aa", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        if (times==null||times.isEmpty())
        {
            return null;
        }
        return  sdf.format(sdf_english.parse(times));
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    private String toInsertString(OpusSportBetLog object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.OPUS_INDEX_SPORT + "\", \"_type\" : \"" + ElasticSearchConstant.OPUS_TYPE_SPORT + "\", \"_id\" : \"" + object.getTrans_id() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
