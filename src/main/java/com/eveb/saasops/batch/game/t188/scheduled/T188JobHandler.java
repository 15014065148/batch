package com.eveb.saasops.batch.game.t188.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "t188JobHandler")
public class T188JobHandler extends IT188JobHandler {
}
