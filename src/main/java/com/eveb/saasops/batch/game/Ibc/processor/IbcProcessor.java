package com.eveb.saasops.batch.game.Ibc.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.Ibc.domain.IbcBetLog;
import com.eveb.saasops.batch.game.Ibc.domain.IbcRequestParameter;
import com.eveb.saasops.batch.game.Ibc.domain.TicketStatusEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class IbcProcessor extends IibcProcessor{

    @Override
    public int processBet(String para) throws Exception {
        IbcRequestParameter parameter=JSON.parseObject(para,IbcRequestParameter.class);
        Integer counts=0;
        StringBuffer insstr=new StringBuffer();
        Boolean isfinish=false;
        /**获取前缀**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<RptBetModel> rptList = new ArrayList<>();
        for(IbcBetLog model:request.request(parameter))
        {
            counts++;
            //小写
            model.setPlayerName(model.getPlayerName().toLowerCase());
            for (String fore : prefixList) {
                fore=fore.toLowerCase();
                //如果存在前缀则进行截取
                if (model.getPlayerName().startsWith(fore)) {
                    model.setSitePrefix(fore);
                    model.setPlayerName(model.getPlayerName().substring(fore.length(), model.getPlayerName().length()));
                }
            }
            model.setApiPrefix(parameter.getApi().getAgyAcc());
            insstr.append(toInsertString(model));
            rptList.add(IbcRptProcess.processRpt(model));
            String tstatus = model.getTicketStatus().toLowerCase();
            if (TicketStatusEnum.Enum_Waiting.getKey().equals(tstatus) || TicketStatusEnum.Enum_Running.getKey().equals(tstatus)) {
                isfinish=true;
            }
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        /**当拉取回来的数据中有未完成的注单时，需要记录，进行重试，直到所有的注单都有结果**/
        if(isfinish)
        {
            throw new Exception(ApplicationConstant.CONSTANT_ERR_MSG);
        }
        return counts;
    }


}
