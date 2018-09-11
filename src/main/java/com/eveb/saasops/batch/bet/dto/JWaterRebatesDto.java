package com.eveb.saasops.batch.bet.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JWaterRebatesDto {

    //活动范围
    private ActivityScopeDto scopeDto;

    //有效投注流水范围 0输 1赢 2全部
    private Integer scope;

    //是否审核 true是 false 否
    private Boolean isAudit;

    //已填写真实姓名 true是 false 否
    private Boolean isName;

    //已绑定银行卡 true是 false 否
    private Boolean isBank;

    //已验证手机 true是 false 否
    private Boolean isMobile;

    //已验证邮箱 true是 false 否
    private Boolean isMail;

    //可领取类型 0每日 1每周 2近7日 3自定义
   // private Integer drawType;

    //可领取次数
    //private Integer drawNumber;

    //平台活动规则范围
    private List<AuditCat> ruleDtos;

    //活动规则
    private List<WaterRebatesRuleListDto> ruleListDtos;
}
