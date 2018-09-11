package com.eveb.saasops.batch.game.agin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.agin.domain.AginBetLogModel;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.agin.request.AginDownloadFile;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
public class AginLiveBetLogProcessor extends AginProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private AginLiveRptBetProcessor rptBetProcessor;

    @Override
    public Integer process(AginRequestParameterModel parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr=new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<String> dateList = AginDownloadFile.downFile(parameter);
        List<RptBetModel> rptList = new ArrayList<>();
        for (String dataStr : dateList) {
            if (dataStr.toString().contains("BR"))//可能存在有转账记录
            {
                counts++;
                /**将数据转换成MAP**/
                String[] strs = dataStr.toString().split("\" ");
                Map map = new HashMap();
                for (String s : strs) {
                    String[] str = s.trim().split("=");
                    if (str.length < 2) {
                        continue;
                    }
                    /**当未派彩时该派彩时间为空，无法进行转换***/
                    if("recalcuTime=\"null".equals(s)){
                        continue;
                    }
                    map.put(str[0], str[1].replace("\"", ""));
                }
                AginBetLogModel model = JSON.parseObject(JSON.toJSONString(map), AginBetLogModel.class);
                if (model != null) {
                    model.setApiPrefix(parameter.getApi().getAgyAcc());
                    model.setPlayerName(model.getPlayerName().toLowerCase());
                    for (String fore : prefixList) {
                        fore=fore.toLowerCase();
                        //如果存在前缀则进行截取
                        if (model.getPlayerName().startsWith(fore)) {
                            model.setSitePrefix(fore);
                            model.setPlayerName(model.getPlayerName().substring(fore.length(), model.getPlayerName().length()));
                            break;
                        }
                    }
                    insstr.append(toInsertString(parameter.getIndexName(),parameter.getTypeName(),model));
                    rptList.add(rptBetProcessor.process(model));
                }
            }
        }
        log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+JSON.toJSONString(rptList));
        rptElasticRestService.insertList(insstr.toString());
        rptElasticRestService.insertOrUpdateList(rptList);
        return counts;
    }
}
