package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.bet.entity.SetBacicOnlinepay;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有支付查询接口的父类,各种支付都要实现这个方法
 * Created by William on 2018/3/6.
 */
@Data
public abstract class ThirdPayService {

    private Map<String,List<Integer>> paySuccessMap;

    public Map<String,List<Integer>> getPaySuccessMap(){
        return paySuccessMap;
    }

    private int totalCount = 0 ;

    public synchronized void plusCount(){
        totalCount ++;
    }

    public void  addPaySuccessId( String siteCode,Integer id){
        if(paySuccessMap == null){
            paySuccessMap  = new ConcurrentHashMap<>();
        }
        List<Integer> paySuccessId= paySuccessMap.get(siteCode);
        synchronized (this){
            if(paySuccessId == null){
                paySuccessId =new ArrayList<>();
                paySuccessMap.put(siteCode,paySuccessId);
            }
            paySuccessId.add(id);
        }
    }

    public void clearpaySuccessMap(){
        if(paySuccessMap != null) {
            paySuccessMap.clear();
        }
    }

    /**
     * 获取支付结果
     * @param orderNo
     * @param id
     * @param siteCode
     * @param onlinepay
     * @return
     * @throws Exception
     */
    public CompletableFuture<String> getPayResult(Long orderNo, Integer id, String siteCode, SetBacicOnlinepay onlinepay) throws Exception{
        return null;
    }
}
