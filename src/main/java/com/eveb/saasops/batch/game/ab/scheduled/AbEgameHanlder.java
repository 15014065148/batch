package com.eveb.saasops.batch.game.ab.scheduled;

import com.eveb.saasops.batch.game.GameJobHandler;
import com.eveb.saasops.batch.game.ab.domain.AbEgameLogModel;
import com.eveb.saasops.batch.game.ab.domain.AbRequestParameter;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "AbEgameHanlder")
public class AbEgameHanlder extends GameJobHandler<AbRequestParameter, AbEgameLogModel> {

    public AbEgameHanlder() {
        super.constantInteval = ApplicationConstant.CONSTANT_AB_EGAME_INTEVAL;
        super.platFromValue = PlatFromEnum.Enum_AB.getValue();
    }

}
