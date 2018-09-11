package com.eveb.saasops.batch.bet.mapper;

import com.eveb.saasops.batch.bet.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ValidBetMapper {

    List<OprActActivity> findActivityList(OprActActivity activity);

    MbrAccount findMbrAccountOne(
            @Param("loginName") String loginName,
            @Param("schemaPrex") String schemaPrex);

    int findValidbetCount(
            @Param("activityId") int activityId,
            @Param("time") String time,
            @Param("schemaPrex") String schemaPrex);

    int selectBankcardCount(MbrBankcard mbrBankcard);

    MbrWallet selectMbrWalletOne(MbrWallet wallet);

    int updateMbrWalletAdd(MbrWallet wallet);

    int updateOprActBonus(OprActBonus waterBonus);

    int insertMbrBillDetail(MbrBillDetail billDetail);

    void insertOprActBonus(OprActBonus waterBonus);

    void insertValidBet(MbrValidbet validbet);

    List<MbrAccount> findMbrAccountList(
            @Param("schemaPrex") String schemaPrex);

    String findMbrAccountTimeByAccountId(
            @Param("schemaPrex") String schemaPrex,
            @Param("accountId") Integer accountId);

    int insertMbrAccountTime(MbrAccountTime accountTime);
}
