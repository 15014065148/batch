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
@JobHander(value = "bbinLiveModifiedJobHandler")
public class BbinLiveModifiedJobHandler extends IBbinJobHandler {

    @Override
    public List<BBINRequestParameter> prepareParameter(int[] range,Object[] params)
    {
        List<BBINRequestParameter> list=new ArrayList<>();
        range[0]= ApplicationConstant.CONSTANT_BBIN_MODIFIED_INTEVAL;
        BBINRequestParameter parameter = new BBINRequestParameter();
        parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Live.getKey()));
        parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_LIVE_MDF);
        parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_LIVE_MDF);
        parameter.setMethod(MethodEnum.Enum_Live_Mdf.getValue());
        list.add(parameter);
        return list;
    }
}
