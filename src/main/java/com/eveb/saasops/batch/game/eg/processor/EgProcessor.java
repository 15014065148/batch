package com.eveb.saasops.batch.game.eg.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.eg.domain.EgBetLog;
import com.eveb.saasops.batch.game.eg.domain.EgRequestParameter;
import com.eveb.saasops.batch.game.eg.request.EgRequest;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EgProcessor {

    @Autowired
    private EgRequest request;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RptElasticRestService rptElasticService;

    @Async("egAsyncExecutor")
    public void process(EgRequestParameter parameter) throws Exception {
        StringBuffer insstr=new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for(EgBetLog model:request.request(parameter))
        {
            //小写
            model.setUserName(model.getUserName().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getUserName().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setUserName(model.getUserName().substring(fore.length(), model.getUserName().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptList.add(EgRptProcess.processRpt(model));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    private String toInsertString(EgBetLog object)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.EG_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.EG_TYPE+"\", \"_id\" : \"" + object.getVendorId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
