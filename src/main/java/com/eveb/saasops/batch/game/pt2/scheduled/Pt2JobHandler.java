package com.eveb.saasops.batch.game.pt2.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "pt2JobHandler")
public class Pt2JobHandler extends IPt2JobHandler {
}
