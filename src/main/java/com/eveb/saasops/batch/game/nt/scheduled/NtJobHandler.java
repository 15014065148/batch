package com.eveb.saasops.batch.game.nt.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "ntJobHandler")
public class NtJobHandler extends INtJobHandler {
}
