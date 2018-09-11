package com.eveb.saasops.batch.game.ab.scheduled;

import com.eveb.saasops.batch.game.GameJobHandler;
import com.eveb.saasops.batch.game.ab.domain.AbModifyBetLogModel;
import com.eveb.saasops.batch.game.ab.domain.AbRequestParameter;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@JobHander(value = "AbModifyJobHandler")
public class AbModifyJobHandler extends GameJobHandler<AbRequestParameter, AbModifyBetLogModel> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AbModifyJobHandler() {
        super.constantInteval = ApplicationConstant.CONSTANT_AB_MODIFIED_INTEVAL;
        super.platFromValue = PlatFromEnum.Enum_AB.getValue();
    }

    @Override
    public String dateFormat(Date date) {
        return dateFormat.format(date);
    }
}
