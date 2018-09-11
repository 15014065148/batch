package com.eveb.saasops.batch.game.agin.scheduled.modified;

import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.agin.scheduled.IAginJobHandler;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "aginModifiedJobHandler")
public class AginLiveModifiedJobHandler extends IAginJobHandler {

    @Override
    public AginRequestParameterModel prepareParameter() {
        AginRequestParameterModel parameter = new AginRequestParameterModel();
        parameter.setRemotePath("/AGIN/lostAndfound/");
        parameter.setIndexName(ElasticSearchConstant.AGIN_INDEX_MDF);
        parameter.setTypeName(ElasticSearchConstant.AGIN_TYPE_MDF);
        return parameter;
    }

}
