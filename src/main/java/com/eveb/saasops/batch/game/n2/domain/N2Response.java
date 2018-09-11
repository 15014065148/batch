package com.eveb.saasops.batch.game.n2.domain;

import lombok.Data;

@Data
public class N2Response {
    private String message;
    private String status;
    private String action;
    private String startTime;
    private String endTime;
}
