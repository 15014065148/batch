package com.eveb.saasops.batch.game.agin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.agin.domain.AGINHunterBetLogModel;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.agin.request.AginDownloadFile;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.util.DateConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 16:41 2017/12/5
 **/
@Component
public class AginHunterBetLogProcessor extends AginProcessor {

    @Autowired
    public RptService rptService;
    @Autowired
    private AginHunterRptBetProcessor rptBetProcessor;

    @Override
    public Integer process(AginRequestParameterModel parameter) throws Exception {
        Integer counts=0;
        StringBuffer insstr = new StringBuffer();
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<String> dateList = AginDownloadFile.downFile(parameter);
        List<RptBetModel> rptList = new ArrayList<>();
        for (String dataStr : dateList) {
            if (dataStr.contains("HSR"))//只有在捕鱼王场景下的投注记录才写入日志表
            {
                counts++;
                /**将数据转换成MAP**/
                String[] strs = dataStr.toString().split("\" ");
                Map map = new HashMap();
                for (String s : strs) {
                    String[] str = s.trim().split("=");
                    map.put(str[0], str[1].replace("\"", ""));
                }
                AGINHunterBetLogModel model = JSON.parseObject(JSON.toJSONString(map), AGINHunterBetLogModel.class);
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
                    insstr.append(toInsertString(parameter.getIndexName(),parameter.getTypeName(), model));
                    /***统计只写入投注注单**/
                    if (GameCodeConstants.CONSTANT_CODE_AGIN_TYPE.compareTo(model.getType())>0) {
                        continue;
                    }
                    rptList.add(rptBetProcessor.process(model));
                }
            }
        }
        rptElasticRestService.insertList(insstr.toString());
        rptElasticRestService.insertOrUpdateList(rptList);
        return counts;
    }
}
