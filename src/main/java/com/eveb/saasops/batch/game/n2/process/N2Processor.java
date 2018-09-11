package com.eveb.saasops.batch.game.n2.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.n2.domain.N2BetLog;
import com.eveb.saasops.batch.game.n2.domain.N2RequestParameter;
import com.eveb.saasops.batch.game.n2.jsonData.Clientbet;
import com.eveb.saasops.batch.game.n2.jsonData.MessageBean;
import com.eveb.saasops.batch.game.n2.request.N2Request;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class N2Processor {
    @Autowired
    private RptService rptService;
    @Autowired
    private N2Request n2Request;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;

    @Async("n2AsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), N2RequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(N2RequestParameter parameter) throws Exception {
        Integer counts = 0;
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        StringBuffer insertBuffer = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (MessageBean model : n2Request.getBetList(parameter)) {
            N2BetLog n2BetLog = new N2BetLog();
            n2BetLog.setStatus(model.getMessage().getStatus().toString().replace("[", "").replace("]", ""));
            model.getMessage().getResult().get(0).getGameinfo().get(0).getGame().stream().forEach(game -> {
                n2BetLog.setGameCode(game.getCode());
                game.getDeal().forEach(deal -> {
                    Clientbet clientbet = deal.getBetinfo().get(0).getClientbet().get(0);
                    n2BetLog.setApiPrefix(parameter.getApi().getApiName());
                    n2BetLog.setDealId(deal.getId());
                    n2BetLog.setStartdate(deal.getStartdate());
                    n2BetLog.setEnddate(deal.getEnddate());
                    n2BetLog.setDealStatus(deal.getStatus());
                    n2BetLog.setDealCode(deal.getCode());
                    n2BetLog.setPalyLogin(clientbet.getLogin().toLowerCase());
                    n2BetLog.setBetAmount(clientbet.getBet_amount());
                    n2BetLog.setPayoutAmount(clientbet.getPayout_amount());
                    n2BetLog.setHandle(clientbet.getHandle());
                    n2BetLog.setHold(clientbet.getHold());
                    n2BetLog.setDealdetails(new Gson().toJson(deal.getDealdetails()));
                    n2BetLog.setBetdetail(new Gson().toJson(deal.getBetinfo().get(0).getClientbet().get(0).getBetdetail()));
                    deal.getResults().stream().forEach(result -> {
                        n2BetLog.setResult(result.getResult().toString().replace("[", "").replace("]", ""));
                    });
                    prefixList.stream().forEach(es -> {
                        es = es.toLowerCase();
                        if (n2BetLog.getPalyLogin().startsWith(es)) {
                            n2BetLog.setSitePrefix(es);
                            n2BetLog.setPalyLogin(n2BetLog.getPalyLogin().substring(es.length(), n2BetLog.getPalyLogin().length()));
                        }
                    });
                    insertBuffer.append(toInsertString(n2BetLog));
                    rptList.add(N2RptBetProcessor.process(n2BetLog));
                });
            });
        }
        rptElasticService.insertList(insertBuffer.toString());
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    /**
     * 转换成插入或修改语句
     *
     * @param n2BetLog
     * @return
     */
    private String toInsertString(N2BetLog n2BetLog) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.N2_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.N2_TYPE + "\", \"_id\" : \"" + n2BetLog.getDealId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(n2BetLog, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }

}
