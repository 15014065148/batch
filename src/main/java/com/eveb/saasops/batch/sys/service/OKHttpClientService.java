package com.eveb.saasops.batch.sys.service;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.sys.config.RedisSwitch;
import com.eveb.saasops.batch.sys.mapper.SysMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OKHttpClientService {

    @Autowired
    private SysMapper sysMapper;

    public List<String> getProxys(String groups){return sysMapper.selectProxys(groups);}
}
