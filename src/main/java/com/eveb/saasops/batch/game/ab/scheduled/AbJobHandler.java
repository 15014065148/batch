package com.eveb.saasops.batch.game.ab.scheduled;

import com.eveb.saasops.batch.game.GameJobHandler;
import com.eveb.saasops.batch.game.ab.domain.AbBetLogModel;
import com.eveb.saasops.batch.game.ab.domain.AbRequestParameter;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@JobHander(value = "AbJobHandler")
public class AbJobHandler extends GameJobHandler<AbRequestParameter, AbBetLogModel> {

    public AbJobHandler() {
        super.constantInteval = ApplicationConstant.CONSTANT_AB_INTEVAL;
        super.platFromValue = PlatFromEnum.Enum_AB.getValue();
    }

}
