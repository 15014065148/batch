package com.eveb.saasops.batch.bet.mapper;


import com.eveb.saasops.batch.bet.entity.FundDeposit;
import com.eveb.saasops.batch.bet.entity.SetBacicOnlinepay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface FundMapper {

    public List<FundDeposit> getDepositeNoPay(@Param("schemaCode") String schemaCode);

    void depositeNoPay(@Param("schemaCode") String schemaCode,@Param("id") Integer id);

    SetBacicOnlinepay queryOnlinePayOne(@Param("schemaCode") String schemaCode,@Param("id") Integer id);
}
