package com.eveb.saasops.batch.game.nt.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RevenueReport {

    private String loginname;

    private Integer partyId;

    private String uuid;

    private BigDecimal handle_total;

    private BigDecimal hold_total;

    private BigDecimal handle_real;

    private BigDecimal hold_real;

    private BigDecimal handle_released_bonus;

    private BigDecimal hold_released_bonus;

    private BigDecimal handle_playable_bonus;

    private BigDecimal hold_playable_bonus;

    public RevenueReport() {
    }

    public RevenueReport(String loginname, Integer partyId, String uuid, BigDecimal handle_total, BigDecimal hold_total, BigDecimal handle_real, BigDecimal hold_real, BigDecimal handle_released_bonus, BigDecimal hold_released_bonus, BigDecimal handle_playable_bonus, BigDecimal hold_playable_bonus) {
        this.loginname = loginname;
        this.partyId = partyId;
        this.uuid = uuid;
        this.handle_total = handle_total;
        this.hold_total = hold_total;
        this.handle_real = handle_real;
        this.hold_real = hold_real;
        this.handle_released_bonus = handle_released_bonus;
        this.hold_released_bonus = hold_released_bonus;
        this.handle_playable_bonus = handle_playable_bonus;
        this.hold_playable_bonus = hold_playable_bonus;
    }

    public RevenueReport(String dataStr)
    {
        Object[] data=dataStr.split(",");
        this.loginname = data[0].toString();
        this.partyId =Integer.parseInt(data[1].toString());
        this.uuid = data[2].toString();
        this.handle_total =BigDecimal.valueOf(Double.parseDouble(data[3].toString()));
        this.hold_total = BigDecimal.valueOf(Double.parseDouble(data[4].toString()));
        this.handle_real =BigDecimal.valueOf(Double.parseDouble(data[5].toString()));
        this.hold_real = BigDecimal.valueOf(Double.parseDouble(data[6].toString()));
        this.handle_released_bonus =BigDecimal.valueOf(Double.parseDouble(data[7].toString()));
        this.hold_released_bonus =BigDecimal.valueOf(Double.parseDouble(data[8].toString()));
        this.handle_playable_bonus = BigDecimal.valueOf(Double.parseDouble(data[9].toString()));
        this.hold_playable_bonus =BigDecimal.valueOf(Double.parseDouble(data[10].toString()));
    }

}
