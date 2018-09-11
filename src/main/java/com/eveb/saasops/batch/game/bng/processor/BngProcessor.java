package com.eveb.saasops.batch.game.bng.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bng.bean.BngItem;
import com.eveb.saasops.batch.game.bng.bean.BngRequestParam;
import com.eveb.saasops.batch.game.bng.request.BngRequest;
import com.eveb.saasops.batch.game.mg2.bean.Mg2BetLogModel;
import com.eveb.saasops.batch.game.mg2.bean.Mg2RequestParameter;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class BngProcessor {
    @Autowired
    private SysService sysService;

    @Autowired
    private RptElasticRestService rptElasticRestService;

    @Autowired
    private BngRequest bngRequest;
    @Autowired
    private RptService rptService;

    public Integer process(BngRequestParam bngRequestParam) throws Exception {
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(bngRequestParam.getApi().getId());
        List<BngItem>  bngList=bngRequest.transactionList(bngRequestParam,null);
        StringBuffer insStr=new StringBuffer();
        List<RptBetModel> rptList = new LinkedList<>();
        TGmApi api =bngRequestParam.getApi();
        bngList.stream().forEach(bngItem -> {
            bngItem.setApiPrefix(api.getApiName());
            bngItem.setPlayer_id(bngItem.getPlayer_id().toLowerCase());
            for (String fore : prefixList) {
                if (bngItem.getPlayer_id().startsWith(fore)) {
                    bngItem.setSitePrefix(fore);
                    bngItem.setPlayer_id(bngItem.getPlayer_id().replace(fore,""));
                }
            }
            toInsertString(bngItem);
            rptList.add(BngRptProcess.processRpt(bngItem));
            insStr.append(toInsertString(bngItem));
        });
        rptElasticRestService.insertList(insStr.toString());
        rptElasticRestService.insertOrUpdateList(rptList);
        return rptList.size();
    }


    @Async(value = "bngProgressor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), BngRequestParam.class)));

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
    private String toInsertString(BngItem object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.BNG_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.BNG_TYPE+"\", \"_id\" : \"" + object.getUid() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
