package com.eveb.saasops.batch.game.n2.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "n2JobHandler")
public class N2JobHandler extends IN2JobHandler{
}
