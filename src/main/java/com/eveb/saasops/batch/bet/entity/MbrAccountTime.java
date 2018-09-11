package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MbrAccountTime{

    private Long id;

    private String login;

    private String logout;

    private Integer accountId;

    private String loginName;

    private String createTime;

    private Long duration;

    private String schemaPrex;
}