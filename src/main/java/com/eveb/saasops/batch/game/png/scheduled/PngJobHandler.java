package com.eveb.saasops.batch.game.png.scheduled;

import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@JobHander(value = "pngJobHandler")
public class PngJobHandler extends IPngJobHandler {
}
