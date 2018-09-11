package com.eveb.saasops.batch.bet.mapper;

import com.eveb.saasops.batch.bet.entity.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface DepotBalanceMapper {

    List<MbrDepotWallet> findMbrDepotWalletList(@Param("schemaPrex") String schemaPrex);

    int updateMbrDepotWalletList(MbrDepotWallet wallet);

    BigDecimal findAccountAmount(MbrBillDetail mbrBillDetail);

}
