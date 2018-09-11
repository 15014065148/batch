package com.eveb.saasops.batch.game.og.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import org.springframework.stereotype.Service;

@Service
@JobHander(value = "ogJobHandler")
public class OGJobHandler extends IOGJobHandler {
}

