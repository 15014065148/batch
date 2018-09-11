package com.eveb.saasops.batch.game.pt.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import org.springframework.stereotype.Service;

@Service
@JobHander(value = "ptJobHandler")
public class PtJobHandler extends IPtJobHandler {
}
