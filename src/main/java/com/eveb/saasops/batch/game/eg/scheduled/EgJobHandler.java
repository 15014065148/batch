package com.eveb.saasops.batch.game.eg.scheduled;


import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "egJobHandler")
public class EgJobHandler extends IEgJobHandler {
}
