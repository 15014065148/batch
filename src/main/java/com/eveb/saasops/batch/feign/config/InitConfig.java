package com.eveb.saasops.batch.feign.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign 默認配置,如果沒有無法使用feign默認註解
 * Created by William on 2018/1/26.
 */
@Configuration
public class InitConfig {
    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

}
