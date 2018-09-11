package com.eveb.saasops.batch.feign.feign.controller;

import com.eveb.saasops.batch.feign.feign.sercice.saasopsV2Service.SaasopsV2Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by William on 2018/1/25.
 */
@RestController
@RequestMapping("/saasops/feign")
public class ConsumerController  {
    @Resource
    private SaasopsV2Service saasopsV2;


    @GetMapping("/query")
    String query(String token){
        return saasopsV2.onlinePayList("test",token);
    }
}
