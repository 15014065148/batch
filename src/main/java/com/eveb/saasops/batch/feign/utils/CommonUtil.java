package com.eveb.saasops.batch.feign.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by William on 2018/1/26.
 */
@Slf4j
public class CommonUtil {
    /**
     * 获取当前请求的站点前缀
     * @return
     */
    public static String getSitePre(){
        if(RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (request != null) {
                String cpSite = request.getHeader("sitePre");
                if(StringUtils.isEmpty(cpSite)){
                    log.info("无法获取cpSite");
                    cpSite="test";
                    System.out.println("Header未设置,测试期设默认到test环璄下!");
                    //throw new RRException("invalid sitePre");
                }
                return cpSite;
            }
        }
        return null;
    }
}
