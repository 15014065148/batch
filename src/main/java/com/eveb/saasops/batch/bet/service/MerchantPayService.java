package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.sys.util.SiteCodeAesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MerchantPayService {

    @Autowired
    private IDepotBalanceService depotBalanceService;

    @Async("merchantPaymentAsyncExecutor")
    public void updateMerchantPay(String siteCode) {
        depotBalanceService.updateMerchantPay(SiteCodeAesUtil.encrypt(siteCode));
    }

}