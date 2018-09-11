package com.eveb.saasops.batch.bet.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ActivityScopeDto {

    //会员组是否全部 true是 false否
    private Boolean isAccAll;

    //会员组ID
    private List<Integer> accIds;

    //总代是否全部 true是 false否
    private Boolean isAgyTopAll;

    //总代ID
    private List<Integer> agyTopIds;

    //代理是否全部 true是 false否
    private Boolean isAgyAll;

    //代理ID
    private List<Integer> agyIds;
}
