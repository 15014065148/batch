package com.eveb.saasops.batch.game.pb.scheduled;

import com.eveb.saasops.batch.game.opus.processor.OpusLiveProcessor;
import com.eveb.saasops.batch.game.opus.scheduled.IOpusJobHandler;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@JobHander(value = "pbJobHandler")
public class PbJobHandler extends IPbJobHandler {

}
