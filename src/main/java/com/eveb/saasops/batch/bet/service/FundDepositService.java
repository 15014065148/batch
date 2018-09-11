package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.bet.entity.FundDeposit;
import com.eveb.saasops.batch.bet.entity.SetBacicOnlinepay;
import com.eveb.saasops.batch.bet.mapper.FundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by William on 2018/3/5.
 */
@Service
public class FundDepositService {

    @Autowired
    private FundMapper fundMapper;

    public List<FundDeposit> getDepositeNoPay(String schemaCode){
       return fundMapper.getDepositeNoPay(schemaCode);
    }
    public void depositeNoPay( String schemaCode,Integer id){
        fundMapper.depositeNoPay(schemaCode,id);
    }

    public SetBacicOnlinepay queryOnlinePayOne(String schemaCode,Integer id){
        return  fundMapper.queryOnlinePayOne(schemaCode,id);
    }
}
