package com.eveb.saasops.batch.sys.scheduled;

import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 17:43 2017/12/26
 **/
@Slf4j
@Service
@JobHander(value = "initOKHttpProxy")
public class InitOKHttpProxysJobHandler extends IJobHandler {
    @Autowired
    OkHttpProxyUtils okHttpProxy;
    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        okHttpProxy.init();
        return null;
    }
}
