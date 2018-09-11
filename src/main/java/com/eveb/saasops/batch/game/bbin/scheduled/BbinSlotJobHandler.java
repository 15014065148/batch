package com.eveb.saasops.batch.game.bbin.scheduled;

import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
import com.eveb.saasops.batch.game.bbin.domain.MethodEnum;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@JobHander(value = "bbinSlotJobHandler")
public class BbinSlotJobHandler extends IBbinJobHandler {

    @Override
    public List<BBINRequestParameter> prepareParameter(int[] range,Object[] params)
    {
        List<BBINRequestParameter> list=new ArrayList<>();
        for(int i=1;i<6;i++) {
            range[0] = ApplicationConstant.Constant_Inteval_24;
            BBINRequestParameter parameter = new BBINRequestParameter();
            if(i==4) {
                continue;
            }
            parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Slot.getKey()));
            //传入的第一个参数 (gamekind=5时，值:1、2、3、5，预设为1)
            parameter.setSubgamekind(i);
            parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_SLOT);
            parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_SLOT);
            parameter.setMethod(MethodEnum.Enum_Slot.getValue());
            if(params[indexPara] == null || i==Integer.parseInt(params[indexPara].toString())) {
                list.add(parameter);
            }
        }
        return list;
    }
}
