package com.eveb.saasops.batch.game.gns.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.gns.model.GnsBetLogModel;
import com.eveb.saasops.batch.game.gns.model.GnsParameterModel;
import com.eveb.saasops.batch.game.gns.model.IParameterModel;
import com.eveb.saasops.batch.game.gns.request.GnsReuqets;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.processor.IProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GnsProcessor extends IProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private GnsReuqets gnsReuqets;
    @Autowired
    private RptElasticRestService rptElasticService;


    @Override
    @Async("gnsAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        executeJobs(jobmodel);
    }

    @Override
    public int processBet(String para)throws Exception {
        IParameterModel gnsPara=JSON.parseObject(para,IParameterModel.class);
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(gnsPara.getApi().getId());
        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (GnsBetLogModel item : gnsReuqets.getGnsBetList(gnsPara)) {
            counts++;
            //小写
            item.setUser_id(item.getUser_id().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getUser_id().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setUser_id(item.getUser_id().substring(fore.length(), item.getUser_id().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(gnsPara.getApi().getWebName());
            rptList.add(GnsRptProcessor.processRpt(item));
            insstr.append(toInsertString(item));
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
    private String toInsertString(GnsBetLogModel object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.GNS_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.GNS_TYPE+"\", \"_id\" : \"" + object.getCausality() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
