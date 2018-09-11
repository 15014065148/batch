package com.eveb.saasops.batch.bet.mapper;

import com.eveb.saasops.batch.bet.entity.MbrAccount;
import com.eveb.saasops.batch.bet.entity.MbrBillDetail;
import com.eveb.saasops.batch.bet.entity.MbrBillDetailGroup;
import com.eveb.saasops.batch.bet.entity.MbrWallet;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by William on 2018/1/15.
 */
@Mapper
@Component
public interface MbrBillDetailMapper  {

    List<MbrBillDetail> findMbrBillDetail(@Param("id") Integer id,@Param("schemaCode") String schemaCode);

    Integer findMbrBillDetaiIndex(@Param("schemaCode") String schemaCode);

    Integer findMbrBillDetaiCount(@Param("beginId") Integer beginId,@Param("schemaCode") String schemaCode);

    List<MbrBillDetail> findMbrBillDetailPage(@Param("id") Integer id,@Param("beginNo") Integer beginNo ,@Param("size") Integer size,@Param("schemaCode") String schemaCode);

    List<MbrAccount> findMbrAccountList(@Param("schemaCode") String schemaCode);

    List<MbrBillDetail> findMbrBillDetailMbrAccount(@Param("id") Integer id,@Param("mbrAccountId") Integer mbrAccountId,@Param("schemaCode") String schemaCode);

    List<MbrWallet> findMbrWalletById(@Param("accountId") Integer accountId,@Param("schemaCode") String schemaCode);

    List<Map<String,Object>> findMbrOp(@Param("accountId") Integer accountId,@Param("schemaCode") String schemaCode);
}
