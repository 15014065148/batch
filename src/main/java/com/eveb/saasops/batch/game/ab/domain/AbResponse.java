package com.eveb.saasops.batch.game.ab.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AbResponse {
    private String error_code;
    private String message;
    private List<Map> histories;
    private String startTime;
    private String endTime;
}
