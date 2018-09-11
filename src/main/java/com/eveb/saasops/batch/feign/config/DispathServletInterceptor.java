package com.eveb.saasops.batch.feign.config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截所有controller请求获取HttpservletRequest,暫時不用
 * Created by William on 2018/1/26.
 */
//@Configuration
public class DispathServletInterceptor extends HandlerInterceptorAdapter implements Cloneable {


    public static HttpServletRequest request;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle 执行！！！");
        this.request =request;
        return true; // 为了说明效果，全部拦截下来

    }

    public static HttpServletRequest getHttpServletRequest(){
        return request;
    }

}
