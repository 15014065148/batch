package com.eveb.saasops.batch.bet.entity;

import lombok.Data;

import java.util.List;

/**
 * 按用户名分组帐变表
 * Created by William on 2018/1/15.
 */
@Data
public class MbrBillDetailGroup {
    private Integer accountId;

    private String loginName;

    private List<MbrBillDetail> mbrBillDetail;
}
