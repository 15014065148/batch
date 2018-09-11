package com.eveb.saasops.batch.game.vr.scheduled;

import com.eveb.saasops.batch.game.IJobhandler;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.vr.domain.VrRequestParameter;
import com.eveb.saasops.batch.game.vr.process.VrGameProcessor;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@Order(2)
@JobHander(value = "vrJobHandler")
public class VrJobHandler extends IJobhandler<VrRequestParameter> {

    @Autowired
    public VrGameProcessor vrGameProcessor;

    public VrJobHandler() {
        super.platFromValue = PlatFromEnum.Enum_VR.getKey();
        super.constantInteval = ApplicationConstant.CONSTANT_VR_INTEVAL;
        super.processor=vrGameProcessor;
    }

    @Override
    public String dateFormat(Date date) {
        return ApplicationConstant.DateFormat.SDF_YYYYMMddTHHmmssSSSZ.format(date);
    }

}
