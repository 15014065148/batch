package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.sys.util.SiteCodeAesUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@Transactional
public class UpdateOprRecMbrService {

    @Autowired
    private IDepotBalanceService depotBalanceService;

    public void updateOprRecMbr(String siteCode) {
        CompletableFuture.runAsync(() -> {
            depotBalanceService.updateOprRecMbr(SiteCodeAesUtil.encrypt(siteCode));
        });
    }
}