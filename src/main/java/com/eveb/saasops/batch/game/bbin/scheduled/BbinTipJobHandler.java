package com.eveb.saasops.batch.game.bbin.scheduled;

import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
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
@JobHander(value = "bbinTipJobHandler")
public class BbinTipJobHandler extends IBbinJobHandler {

    @Override
    public List<BBINRequestParameter> prepareParameter(int[] range,Object[] params)
    {
        List<BBINRequestParameter> list=new ArrayList<>();
        range[0]= ApplicationConstant.Constant_Inteval_24;
        BBINRequestParameter parameter = new BBINRequestParameter();
        parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Tip.getKey()));
        parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_TIP);
        parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_TIP);
        parameter.setMethod("BetRecord");
        list.add(parameter);
        return list;
    }
}
