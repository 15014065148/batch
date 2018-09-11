package com.eveb.saasops.batch.game.Ibc.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.Ibc.domain.IbcBetLog;
import com.eveb.saasops.batch.game.Ibc.domain.IbcRequestParameter;
import com.eveb.saasops.batch.game.Ibc.request.IbcRequest;
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

import java.util.Date;
@Slf4j
public class IibcProcessor extends IProcessor {

    @Autowired
    protected IbcRequest request;
    @Autowired
    protected RptService rptService;
    @Autowired
    protected SysService sysService;
    @Autowired
    protected RptElasticRestService rptElasticService;


    @Override
    @Async("ibcAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        executeJobs(jobmodel);
    }


    @Override
    public int processBet(String para) throws Exception{return 0;}

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    public String toInsertString(IbcBetLog object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.IBC_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.IBC_TYPE+"\", \"_id\" : \"" + object.getTransId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
