package com.eveb.saasops.batch.game.report.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameCode {

    private Integer id;
    private String platform;//平台
    private String codeType;//代码类型
    private String codeGroup;//分组
    private String codeId;//分类
    private String codeName;

    public GameCode() {
    }
}
