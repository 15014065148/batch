package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.bet.entity.MbrBillDetail;
import com.eveb.saasops.batch.bet.entity.MbrBillDetailGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by William on 2018/1/15.
 */
@Slf4j
@Data
public class FinancialCountService{

    Map<String,List<String>> errorData;
    Map<String,List<MbrBillDetail>>  MbrBillDetailMap =new HashMap<>();
    public FinancialCountService() {
        this.errorData = new HashMap<>();
        this.MbrBillDetailMap =new HashMap<>();
    }

    public List<MbrBillDetailGroup> getAllMbrBillDetail(List<MbrBillDetail> mbrBillDetails, String sitePrefix, Map<String,List<String>> errorData,Map<String,List<MbrBillDetail>>  MbrBillDetailMap){
        log.info("当前数据库++++++++++++++++"+sitePrefix);
       /* List<MbrBillDetail> mbrBillDetails =dataSource.findMbrBillDetail();*/
        List<MbrBillDetail> mbrBillDetailName =new ArrayList<>();
        List<String> error =new ArrayList<>();
        for(MbrBillDetail mbrBillDetail : mbrBillDetails) {
            int i = mbrBillDetailName.size();
            if (i != 0) {
                if (mbrBillDetail.getAccountId().equals(mbrBillDetailName.get(0).getAccountId())) {
                    mbrBillDetailName.add(mbrBillDetail);
                } else {
                    List<MbrBillDetail> mbrBillDetailNameClone = new ArrayList<>();
                    mbrBillDetailNameClone.addAll(mbrBillDetailName);
                    MbrBillDetailMap.put(mbrBillDetailName.get(0).getLoginName(), mbrBillDetailNameClone);
                    mbrBillDetailName.clear();
                    mbrBillDetailName.add(mbrBillDetail);
                }
            } else {
                mbrBillDetailName.add(mbrBillDetail);
            }
            //比较当前行内的数据
            if (mbrBillDetail.getOpType() == 0) {
                //支出
                BigDecimal balance = mbrBillDetail.getBeforeBalance().subtract(mbrBillDetail.getAmount());
                if (!balance.equals(mbrBillDetail.getAfterBalance())) {
                    log.info("[数据库" + sitePrefix + "]单笔错误 支出----" + mbrBillDetail.toString());
                    error.add("[数据库" + sitePrefix + "]单笔错误 收入" + mbrBillDetail.toString());
                }
            } else {
                //收入
                BigDecimal balance = mbrBillDetail.getBeforeBalance().add(mbrBillDetail.getAmount());
                if (!balance.equals(mbrBillDetail.getAfterBalance())) {
                    log.info("[数据库" + sitePrefix + "]单笔错误 收入" + mbrBillDetail.toString());
                    error.add("[数据库" + sitePrefix + "]单笔错误 收入" + mbrBillDetail.toString());
                }
            }
        }
        errorData.put("[数据库"+sitePrefix+"]单笔错误",error);
        return null;
    }

}
