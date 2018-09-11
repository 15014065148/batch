package com.eveb.saasops.batch.bet.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuditRule {

    private List<AuditCat> ruleDtos;
    private Integer scope;
}
