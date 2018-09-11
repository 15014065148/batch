package com.eveb.saasops.batch.bet.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AuditCat {

    private Boolean isAll;

    private Integer catId;

    private List<AuditDepot> depots;
}
