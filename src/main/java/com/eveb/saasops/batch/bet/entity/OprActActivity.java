package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OprActActivity{

    private Integer id;

    private Integer actTmplId;

    private String activityName;

    private String showStart;

    private String showEnd;

    private String useStart;

    private String useEnd;

    private Integer useState;

    private String rule;

    private String code5;

    private String schemaPrex;

    private Integer ruleId;

    private String siteCode;
}