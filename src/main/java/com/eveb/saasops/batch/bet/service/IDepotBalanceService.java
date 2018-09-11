package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.feign.feign.entity.R;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.math.BigDecimal;


@FeignClient(value = "saasops-v2-service")
public interface IDepotBalanceService {

    @Headers({"SToken: {siteCode}"})
    @RequestLine("GET /sysapi/depotBalance?siteCode={siteCode}&depotId={depotId}&accountId={accountId}")
    R depotBalance(@Param("siteCode") String siteCode, @Param("depotId") Integer depotId, @Param("accountId") Integer accountId);

    @Headers({"SToken: {siteCode}"})
    @RequestLine("GET /sysapi/validBetMsg?siteCode={siteCode}&accountId={accountId}&acvitityMoney={acvitityMoney}")
    void validBetMsg(@Param("siteCode") String siteCode, @Param("accountId") Integer accountId,
                     @Param("acvitityMoney") BigDecimal acvitityMoney);

    @Headers({"SToken: {siteCode}"})
    @RequestLine("GET /sysapi/updateMerchantPay?siteCode={siteCode}")
    void updateMerchantPay(@Param("siteCode") String siteCode);


    /**
     * 刪除站內信息
     * @param siteCode
     */
    @Headers({"SToken: {siteCode}"})
    @RequestLine("GET /sysapi/updateOprRecMbr?siteCode={siteCode}")
    void updateOprRecMbr(@Param("siteCode") String siteCode);

    @Headers({"SToken: {siteCode}"})
    @RequestLine("GET /sysapi/auditAccount?siteCode={siteCode}")
    void auditAccount(@Param("siteCode") String siteCode);

    @Headers({"SToken: {siteCode}"})
    @RequestLine("GET /sysapi/updateActivityState?siteCode={siteCode}")
    void updateActivityState(@Param("siteCode") String siteCode);
}
