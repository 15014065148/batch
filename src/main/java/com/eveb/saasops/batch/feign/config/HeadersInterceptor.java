package com.eveb.saasops.batch.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Map;

/**
 * 拦截器:拦截feign提交給cloud服務者的所有请求,暫時不用
 * Created by William on 2018/1/25.
 */
//@Configuration
public class HeadersInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Map<String, Collection<String>> collectionMap = template.headers();
        /*HttpServletRequest request =DispathServletInterceptor.getHttpServletRequest();
        if(request != null) {
            String cpSite = request.getHeader("sitePre");
            String token = request.getHeader("token");
            template.header("sitePre", cpSite);
            template.header("token",token);
        }*/
    }
}

