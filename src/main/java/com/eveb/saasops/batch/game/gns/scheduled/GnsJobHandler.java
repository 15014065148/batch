package com.eveb.saasops.batch.game.gns.scheduled;

import com.eveb.saasops.batch.game.IJobhandler;
import com.eveb.saasops.batch.game.gns.model.GnsParameterModel;
import com.eveb.saasops.batch.game.gns.processor.GnsProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
@JobHander(value = "gnsJobHandler")
public class GnsJobHandler extends IJobhandler<GnsParameterModel> {
    @Autowired
    private GnsProcessor gnsProcessor;
    public GnsJobHandler() {
        super.platFromValue=PlatFromEnum.Enum_GNS.getKey();
        super.constantInteval=ApplicationConstant.CONSTANT_GNS_INTEVAL;
        super.constantDelay=ApplicationConstant.CONSTANT_GNS_DELAY;
    }

    @Override
    public String dateFormat(Date date) {
        return ApplicationConstant.DateFormat.SDF_YYYYMMddTHHmmssSSSZ.format(date);
    }

    @Override
    public GnsParameterModel setParameter(){
        super.processor=gnsProcessor;
        GnsParameterModel gns=new GnsParameterModel();
        gns.setStartIndex(ApplicationConstant.CONSTANT_GNS_START_INDEX);
        gns.setLimit(ApplicationConstant.CONSTANT_GNS_LIMIT);
        return gns;
    }
}
