package com.eveb.saasops.batch.game.og.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Data;

@Data
public class OGRequestParameter {
    private TGmApi gmApi;
    private Long maxVendorid;
}
