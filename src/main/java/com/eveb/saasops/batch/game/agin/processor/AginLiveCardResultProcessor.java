package com.eveb.saasops.batch.game.agin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.agin.domain.AginLiveCardResultModel;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.agin.request.AginDownloadFile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AginLiveCardResultProcessor extends AginProcessor {

    @Override
    public Integer process(AginRequestParameterModel parameter) throws Exception {
        Integer counts = 0;
        StringBuffer insstr = new StringBuffer();
        List<String> dateList = AginDownloadFile.downFile(parameter);
        for (String dataStr : dateList) {
            counts++;
            /**将数据转换成MAP**/
            String[] strs = dataStr.toString().split("\" ");
            Map map = new HashMap();
            for (String s : strs) {
                String[] str = s.trim().split("=");
                if (str.length < 2) {
                    continue;
                }
                map.put(str[0], str[1].replace("\"", ""));
            }
            AginLiveCardResultModel model = JSON.parseObject(JSON.toJSONString(map), AginLiveCardResultModel.class);
            insstr.append(toInsertString(parameter.getIndexName(), parameter.getTypeName(), model));
        }
        rptElasticRestService.insertList(insstr.toString());
        return counts;
    }
}
