package com.eveb.saasops.batch.feign.feign.sercice.saasopsV2Service;

import com.eveb.saasops.batch.feign.feign.entity.R;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by William on 2018/1/27.
 */
@FeignClient(value="saasops-v2-service")
public interface SaasopsV2Service {

    //通过SpringMVC的注解来配置所綁定的服务下的具体实现
    @Headers({"SToken: {siteCode}","token: {token}"})
    @RequestLine("GET /bkapi/onlinepay/setbaciconlinepay/onlinePayList")
    String onlinePayList(@Param("siteCode") String siteCode, @Param("token") String token);


    @Headers({"SToken: {siteCode}"})
    @RequestLine("POST /sysapi/paySuccess?ids={ids}")
    public R paySuccess(@Param("siteCode") String siteCode,@Param("ids") List<Integer> ids);

    @Headers({"SToken: {siteCode}"})
    @RequestLine("POST /sysapi/onlinepayInfo?id={id}")
    public R onlinepayInfo(@Param("siteCode") String siteCode,@Param("id") Integer id);
}
