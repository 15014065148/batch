package com.eveb.saasops.batch.bet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnlineTimeDto {

    /**
     * 1登入 2登出 3即登入，登出 加15分钟  4不处理 即进行中
     */
    private int isLogin;

    /**
     * 时间戳
     */
    private Long time;
}
