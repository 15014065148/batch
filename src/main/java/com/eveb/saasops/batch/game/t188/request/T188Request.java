package com.eveb.saasops.batch.game.t188.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.t188.domain.T188BetLog;
import com.eveb.saasops.batch.game.t188.domain.T188RequestParameter;
import com.eveb.saasops.batch.sys.util.AesUtil;
import com.eveb.saasops.batch.game.t188.util.Request;
import com.eveb.saasops.batch.sys.util.XmlUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class T188Request {

    private final String method = "GetWagerInfoByDateRange";
    @Autowired
    private OkHttpProxyUtils httpProxyUtils;

    public List<T188BetLog> request(T188RequestParameter parameter) throws Exception {
        String key = parameter.getApi().getMd5Key();
        String rurl = parameter.getApi().getPcUrl() + method;
        StringBuffer ps = new StringBuffer();
        ps.append(String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?> <Request Method=\"%s\">", method));
        ps.append(String.format("<Begin>%s</Begin><End>%s</End><MemberCode></MemberCode>", parameter.getStartTime(), parameter.getEndTime()));
        ps.append("</Request>");
        String encrypt = AesUtil.aesEncrypt(ps.toString(), key);
        String rs = Request.sendPost(rurl, encrypt);
        rs = AesUtil.aesDecrypt(rs, key);
        log.info("188请求url :" + ps.toString());
        log.info("188返回数据 :" + rs);
        Map rsmap = XmlUtil.xml2JSON(rs.getBytes());
        Object obj = ((Map) rsmap.get("Response")).get("Wagers");
        if (obj == null) {
            return new ArrayList<>();
        }
        rs = ((Map) ((List) obj).get(0)).get("TimeSpanBetWagerInfoDto").toString().replace("[", "").replace("]", "");
        List<T188BetLog> list = JSON.parseArray("[" + rs + "]", T188BetLog.class);
        return list;
    }
}
