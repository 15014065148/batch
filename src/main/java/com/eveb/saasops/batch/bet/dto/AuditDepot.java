package com.eveb.saasops.batch.bet.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AuditDepot {
    private Integer depotId;
    private List<Integer> games;

    public AuditDepot() {
        //默认选择全部
        depotId = 0;
        games = new ArrayList<>();
        games.add(0);
    }
}
