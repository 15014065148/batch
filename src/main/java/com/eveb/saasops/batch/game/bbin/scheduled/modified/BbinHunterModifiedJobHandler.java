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
@JobHander(value = "bbinHunterModifiedJobHandler")
public class BbinHunterModifiedJobHandler extends IBbinJobHandler {

    @Override
    public List<BBINRequestParameter> prepareParameter(int[] range, Object[] params) {
        List<BBINRequestParameter> list = new ArrayList<>();
        range[0] = ApplicationConstant.CONSTANT_BBIN_MODIFIED_INTEVAL;
        BBINRequestParameter parameter = new BBINRequestParameter();
        parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Slot.getKey()));
        parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_HUNTER_MDF);
        parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_HUNTER_MDF);
        parameter.setMethod(MethodEnum.Enum_Hunter_Mdf.getValue());
        parameter.setAction("ModifiedTime");//时间捞取依据 (BetTime:使用下注时间查询资讯 / ModifiedTime: 使用异动时间查询资讯)
        parameter.setGametype("30599");
        list.add(parameter);
        /**捕鱼大师**/
        BBINRequestParameter mparameter = new BBINRequestParameter();
        mparameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Slot.getKey()));
        mparameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_HUNTER_MDF);
        mparameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_HUNTER_MDF);
        mparameter.setMethod(MethodEnum.Enum_Hunter_Master_Mdf.getValue());
        mparameter.setAction("ModifiedTime");//时间捞取依据 (BetTime:使用下注时间查询资讯 / ModifiedTime: 使用异动时间查询资讯)
        mparameter.setGametype("38001");
        list.add(mparameter);
        return list;
    }
}
