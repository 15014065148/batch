package com.eveb.saasops.batch.game.bbin.scheduled.modified;

import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
import com.eveb.saasops.batch.game.bbin.domain.MethodEnum;
import com.eveb.saasops.batch.game.bbin.scheduled.IBbinJobHandler;
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
@JobHander(value = "bbinSlotModifiedJobHandler")
public class BbinSlotModifiedJobHandler extends IBbinJobHandler {

    @Override
    public List<BBINRequestParameter> prepareParameter(int[] range,Object[] params) {
        List<BBINRequestParameter> list = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            range[0] = ApplicationConstant.CONSTANT_BBIN_MODIFIED_INTEVAL;
            BBINRequestParameter parameter = new BBINRequestParameter();
            if(i==4) {
                continue;
            }
            parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Slot.getKey()));
            parameter.setSubgamekind(i);//传入的第一个参数 (gamekind=5时，值:1、2、3、5，预设为1)
            parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_SLOT_MDF);
            parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_SLOT_MDF);
            parameter.setMethod(MethodEnum.Enum_Slot_Mdf.getValue());
            if (params[indexPara] == null || i == Integer.parseInt(params[indexPara].toString())) {
                list.add(parameter);
            }
        }
        return list;
    }
}
