package com.eveb.saasops.batch.game.ttg.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import org.springframework.stereotype.Service;

@Service
@JobHander(value = "ttgJobHandler")
public class TTGJobHandler extends ITTGJobHandler{
}
