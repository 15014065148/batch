package com.eveb.saasops.batch.game.bbin.scheduled;

import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
import com.eveb.saasops.batch.game.bbin.domain.MethodEnum;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.domain.GameCode;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@JobHander(value = "bbinLotteryJobHandler")
public class BbinLotteryJobHandler extends IBbinJobHandler {

    @Autowired
    private RptService rptService;
    @Override
    public List<BBINRequestParameter> prepareParameter(int[] range,Object[] params) {

        List<BBINRequestParameter> list = new ArrayList<>();
        if (params[indexPara] == null || ApplicationConstant.CONSTANT_BBIN_LOTTERY_LT.equals(params[indexPara].toString())) {
            range[0] = ApplicationConstant.Constant_Inteval_24;
            BBINRequestParameter lt_parameter = new BBINRequestParameter();
            lt_parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Lottery.getKey()));
            //(gamekind=12时，需强制带入)
            lt_parameter.setGametype(ApplicationConstant.CONSTANT_BBIN_LOTTERY_LT);
            lt_parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_LOTTERY);
            lt_parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_LOTTERY);
            lt_parameter.setMethod(MethodEnum.Enum_Lottery.getValue());
            list.add(lt_parameter);
        }
        if (params[indexPara] == null || ApplicationConstant.CONSTANT_BBIN_LOTTERY_OTHER.equals(params[indexPara].toString())) {
            BBINRequestParameter other_parameter = new BBINRequestParameter();
            other_parameter.setGamekind(Integer.parseInt(BBINGameTypeEnum.Enum_Lottery.getKey()));
            //(gamekind=12时，需强制带入)
            other_parameter.setGametype(ApplicationConstant.CONSTANT_BBIN_LOTTERY_OTHER);
            other_parameter.setIndexName(ElasticSearchConstant.BBIN_INDEX_LOTTERY);
            other_parameter.setTypeName(ElasticSearchConstant.BBIN_TYPE_LOTTERY);
            other_parameter.setMethod(MethodEnum.Enum_Lottery.getValue());
            list.add(other_parameter);
        }
        return list;
    }
}
