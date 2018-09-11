package com.eveb.saasops.batch.game.bbin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bbin.domain.BbinBetLog;
import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
import com.eveb.saasops.batch.game.bbin.request.BbinRequest;
import com.eveb.saasops.batch.game.bbin.service.BBINElasticRestService;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
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

/**
 * @Author: Miracle
 * @Description:
 * @Date: 14:54 2017/12/16
 **/
@Slf4j
@Component
public class BbinBetLogProcessors {

    @Autowired
    SysService sysService;
    @Autowired
    public BBINElasticRestService ealsticService;
    @Autowired
    private BbinRequest bbinRequest;
    @Autowired
    private RptService rptService;
    @Autowired
    private RptElasticRestService rptElasticService;

    @Async("bbinAsyncExecutor")
    public void execute(JobFailMessageModel model) throws Exception {
        //执行次数加一
        model.setTimes(model.getTimes() + 1);
        model.setFirstTime(new Date());
        model.setLastTime(new Date());
        BBINRequestParameter parameter = JSON.parseObject(model.getParamater(), BBINRequestParameter.class);
        try {
            model.setCounts(prodessBet(parameter));
        } catch (Exception e) {
            sysService.saveOrUpdate(model);
            log.error(e.getMessage());
            throw e;
        }
        model.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(model);
    }

    /**
     * 处理注单数据
     * @param parameter
     * @throws Exception
     */
    private Integer prodessBet(BBINRequestParameter parameter) throws Exception {
        /**根据线路获取所有的前缀**/
        List<String> prefixList;
        if (!(parameter.getIndexName().indexOf("mdf") > 0)) {
            prefixList= rptService.getSiteForeByApiId(parameter.getApi().getId());
        }else{
            /**异动单,获取所有前缀**/
            prefixList= rptService.getSiteForeByApiId(null);
        }
        Integer counts=0;
        StringBuffer betList=new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        List<RptBetModel> mdfRptList = new ArrayList<>();
        for (BbinBetLog item : bbinRequest.getBBINBetLogList(parameter)) {
            counts++;
            item.setUserName(item.getUserName().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getUserName().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setUserName(item.getUserName().substring(fore.length(), item.getUserName().length()));
                    break;
                }
            }
            item.setApiPrefix(parameter.getApi().getAgyAcc());

            /**当调用异动单接口时返回的注单无需判断，某一个注单可能有多次异动**/
            if (!(parameter.getIndexName().indexOf("mdf") > 0)) {
                //不是异动单的情况下写入前缀
                item.setApiPrefix(parameter.getApi().getAgyAcc());
                /**普通注单,报表进行插入**/
                rptList.add(BbinRptProcessor.processRpt(item, parameter.getGamekind().toString()));
            } else {
                /**异动单,报表进行更新**/
                BbinBetLog mdfbet=(BbinBetLog)item.clone();
                mdfbet.setSitePrefix(null);
                mdfbet.setUserName(null);
                mdfbet.setApiPrefix(null);
                mdfRptList.add(BbinRptProcessor.processRpt(mdfbet, parameter.getGamekind().toString()));
            }
            /**暂时不实现校验功能**/
            betList.append(toInsertString(parameter.getIndexName(), parameter.getTypeName(),item));
        }
        rptElasticService.insertList(betList.toString());
        rptElasticService.insertList(insertList(rptList));
        rptElasticService.insertList(updateList(mdfRptList));
        return counts;
    }

    public String toInsertString(String index,String type, BbinBetLog betLog) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + index.toLowerCase() + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + betLog.getWagersID() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(betLog, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }

    /**
     *  针对波音异动注单处理
     * @param list
     * @throws Exception
     */
    public String insertList(List<RptBetModel> list) {
        StringBuffer string = new StringBuffer();
        for (RptBetModel object : list) {
            RptBetModel rbm = new RptBetModel();
            rbm.setApiPrefix(object.getApiPrefix());
            rbm.setSitePrefix(object.getSitePrefix());
            rbm.setUserName(object.getUserName());
            string.append("{ \"update\" : { \"_index\" : \"" + ElasticSearchConstant.REPORT_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.REPORT_TYPE + "\", \"_id\" : \"" + object.getId() + "\" }}");
            string.append("\n");
            string.append("{ \"doc\" :" + JSON.toJSONString(rbm) + "}");
            string.append("\n");
            string.append("{ \"create\" : { \"_index\" : \"" + ElasticSearchConstant.REPORT_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.REPORT_TYPE + "\", \"_id\" : \"" + object.getId() + "\" }}");
            string.append("\n");
            string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            string.append("\n");
        }
        return string.toString();
    }

    /**
     * 先进行插入操作，如果已经存再则进行修改
     * @param list
     * @throws Exception
     */
    public String updateList(List<RptBetModel> list){
        StringBuffer string = new StringBuffer();
        for (RptBetModel object : list) {
            string.append("{ \"create\" : { \"_index\" : \""+ElasticSearchConstant.REPORT_INDEX+"\", \"_type\" : \""+ElasticSearchConstant.REPORT_TYPE+"\", \"_id\" : \"" + object.getId() + "\" }}");
            string.append("\n");
            string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            string.append("\n");
            string.append("{ \"update\" : { \"_index\" : \""+ElasticSearchConstant.REPORT_INDEX+"\", \"_type\" : \""+ElasticSearchConstant.REPORT_TYPE+"\", \"_id\" : \"" + object.getId() + "\" }}");
            string.append("\n");
            string.append("{ \"doc\" :"+JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")+"}");
            string.append("\n");
        }
        return string.toString();
    }
}
