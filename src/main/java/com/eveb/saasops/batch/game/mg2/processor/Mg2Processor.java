package com.eveb.saasops.batch.game.mg2.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.mg2.bean.Mg2BetLogModel;
import com.eveb.saasops.batch.game.mg2.bean.Mg2RequestParameter;
import com.eveb.saasops.batch.game.mg2.request.Mg2Request;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class Mg2Processor {

    @Autowired
    private SysService sysService;
    @Autowired
    private Mg2Request mg2Request;
    @Autowired
    private RptElasticRestService rptElasticRestService;

    private static final String CATEGROY_WAGER ="wager";
    private static final String CATEGROY_PAYOUT ="payout";

    @Autowired
    private RptService rptService;

    public Integer process(Mg2RequestParameter parameter){
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        StringBuffer insStr=new StringBuffer();
        List<Mg2BetLogModel> mg2BetLogModels = null;
        try {
            mg2BetLogModels = mg2Request.getTransactionFeed(parameter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //只处理投注和赢得的订单，去掉其他订单
        List<RptBetModel> rptList = new LinkedList<>();
        mg2BetLogModels.stream().forEach(mg2BetLogModel -> {
            if (mg2BetLogModel.getCategory().equalsIgnoreCase(CATEGROY_WAGER) || mg2BetLogModel.getCategory().equalsIgnoreCase(CATEGROY_PAYOUT)) {
                mg2BetLogModel.setAccount_ext_ref(mg2BetLogModel.getAccount_ext_ref().toLowerCase());
                for (String fore : prefixList) {
                    if (mg2BetLogModel.getAccount_ext_ref().startsWith(fore)) {
                        mg2BetLogModel.setSitePrefix(fore);
                        mg2BetLogModel.setAccount_ext_ref(mg2BetLogModel.getAccount_ext_ref().replace(fore,""));
                    }
                }
                mg2BetLogModel.setApiPrefix(parameter.getApi().getApiName());
                rptList.add(Mg2RptProcess.processRpt(mg2BetLogModel));
                insStr.append(toInsertString(mg2BetLogModel));
            }
        });
        try {
            rptElasticRestService.insertList(insStr.toString());
            rptElasticRestService.insertOrUpdateList(rptList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mg2BetLogModels.size();
    }


    @Async(value = "mg2Progressor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);//执行次数加一
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), Mg2RequestParameter.class)));

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
    private String toInsertString(Mg2BetLogModel object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.MG2_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.MG2_TYPE+"\", \"_id\" : \"" + object.getId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }

}
