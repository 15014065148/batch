package com.eveb.saasops.batch.bet.processor;

import com.eveb.saasops.batch.bet.entity.MbrBillDetail;
import com.eveb.saasops.batch.bet.entity.MbrWallet;
import com.eveb.saasops.batch.bet.mapper.MbrBillDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by William on 2018/1/17.
 */
@Slf4j
@Component
public class FinancialCountProcess {

    /**
     * 单笔查询
     * @param mbrBillDetailMapper
     * @param schemaCode
     * @param id
     * @param beginNo
     * @param size
     */
    @Async("financialCountAsyncExecutor")
    public void financialCount(MbrBillDetailMapper mbrBillDetailMapper , String schemaCode , int id , int beginNo, int size){
        try {
            List<MbrBillDetail> mbrBillDetails = mbrBillDetailMapper.findMbrBillDetailPage(id, beginNo, size,schemaCode);
            for(MbrBillDetail mbrBillDetail : mbrBillDetails) {
                //比较当前行内的数据
                if (mbrBillDetail.getOpType() == 0) {
                    //支出
                    BigDecimal balance = mbrBillDetail.getBeforeBalance().subtract(mbrBillDetail.getAmount());
                    if (!balance.equals(mbrBillDetail.getAfterBalance())) {
                        log.info("[数据库" + schemaCode + "]单笔错误 支出----" + mbrBillDetail.toString());
                    }
                } else {
                    //收入
                    BigDecimal balance = mbrBillDetail.getBeforeBalance().add(mbrBillDetail.getAmount());
                    if (!balance.equals(mbrBillDetail.getAfterBalance())) {
                        log.info("[数据库" + schemaCode + "]单笔错误 收入" + mbrBillDetail.toString());
                    }
                }
            }
        }catch (Exception e){
            log.error("error +++++++++++++++++++++[数据库" + schemaCode + "] -----id:"+id+"-----beginNo:" + beginNo+ "-----size:" +size);
            e.printStackTrace();
        }
    }

    /**
     * 验证同一账户的前一次操作前余额是上一次的操作后余额
     * @param mbrAccountId
     * @param id
     */
    @Async("financialCountAsyncExecutor")
    public void financialCountMemberStepOneprocess(Integer mbrAccountId,Integer id,MbrBillDetailMapper mbrBillDetailMapper,String schemaCode){
        List<MbrBillDetail> mbrBillDetailList = new ArrayList<>();
        int size =0;
        try {
            synchronized (this){
                mbrBillDetailList = mbrBillDetailMapper.findMbrBillDetailMbrAccount(id, mbrAccountId,schemaCode);
            }
            size =mbrBillDetailList.size();
        }catch (Exception e){
            log.error("error +++++++++++++++++++++[数据库" + schemaCode + "] -----id:"+id+"-----mbrAccountId:" + mbrAccountId);
            e.printStackTrace();
        }
        if(size == 0){
            return;
        }
        for (int i = 0; i < size; i++) {
            BigDecimal beforeBalance=mbrBillDetailList.get(i).getAfterBalance(); //前一次的操作后
            BigDecimal afterBalacne= null ;
            try {
                afterBalacne=mbrBillDetailList.get(i+1).getBeforeBalance(); //后一次操作前
            }catch (IndexOutOfBoundsException e){
                return;
            }
            if(!beforeBalance.equals(afterBalacne)){
                log.info("[数据库" + schemaCode + "]前后两次操作余额不-------前一次操作 :" + mbrBillDetailList.get(i).toString() +",后一次操作:"+mbrBillDetailList.get(i+1).toString());
            }
        }
    }

    /**
     * 验证该用户余额 = 所有收入 - 所有支出
     * @param mbrAccountId
     * @param mbrBillDetailMapperThread
     * @param schemaCode
     */
    @Async("financialCountAsyncExecutor")
    public void financialCountMenberStepTwo(Integer mbrAccountId, MbrBillDetailMapper mbrBillDetailMapperThread, String schemaCode){
        MbrWallet wallet ;
        List<MbrWallet> mbrWallets;
        List<Map<String, Object>> mbr;
        synchronized (this){
            mbrWallets =mbrBillDetailMapperThread.findMbrWalletById(mbrAccountId,schemaCode);
            mbr = mbrBillDetailMapperThread.findMbrOp(mbrAccountId,schemaCode);
        }
        if(mbrWallets.size() == 0){
            log.info("[数据库" + schemaCode + "] 会员id :"+mbrAccountId +"无钱包");
            return;
        }else {
            wallet = mbrWallets.size() == 0 ? null : mbrBillDetailMapperThread.findMbrWalletById(mbrAccountId,schemaCode).get(0);
        }
        //当前余额
        BigDecimal balance =wallet.getBalance();
        BigDecimal caculate =new BigDecimal("0.00");
        mbr.removeAll(Collections.singleton(null));
        if(mbr.size() != 0 ){
           BigDecimal receiveAmout =new BigDecimal(mbr.get(0).get("receiveAmout") == null?"0":mbr.get(0).get("receiveAmout").toString());
           BigDecimal payAmout =new BigDecimal(mbr.get(0).get("payAmout")== null?"0":mbr.get(0).get("payAmout").toString());
           caculate = receiveAmout.subtract(payAmout);
        }
        if(!balance.equals(caculate)){
            log.info("[数据库" + schemaCode + "] 会员id :"+mbrAccountId +"余额与所有收入 - 所有支出不符,钱包余额为"+balance+"汇总计算余额为"+caculate);
        }
    }
}
