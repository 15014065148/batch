package com.eveb.saasops.batch.game.n2.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eveb.saasops.batch.game.n2.domain.N2RequestParameter;
import com.eveb.saasops.batch.game.n2.jsonData.MessageBean;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import com.eveb.saasops.batch.sys.util.XmlOnMap;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class N2Request {

    @Autowired
    private OkHttpProxyUtils httpProxyUtils;

    public List<MessageBean> getBetList(N2RequestParameter parameter) throws Exception {
//        String requestUrl = "https://stgmerchanttalk.azuritebox.com/trading/GameInfo";
        String requestUrl = "https://merchanttalk.azuritebox.com/Trading/GameInfo";
        StringBuffer ps = new StringBuffer();
        ps.append(String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?> <request action=\"gameinfo\">"));
        ps.append(String.format("<element>\n" +
                "    <properties name=\"vendorid\">193</properties>\n" +
                "    <properties name=\"merchantpasscode\">4EC28BFC0106E0F9E3ECDEFDE10AD230</properties>\n" +
                "    <properties name=\"startdate\">%s</properties>\n" +
                "    <properties name=\"enddate\">%s</properties>\n" +
                "    <properties name=\"timezone\">480</properties>\n" +
                "  </element>", parameter.getStartdate(), parameter.getEnddate()));
        ps.append("</request>");
        log.info("N2请求url{}  N2请求ps{}:", requestUrl, ps.toString());
        String rs = httpProxyUtils.postXml(httpProxyUtils.proxyClient, requestUrl, ps.toString());
        log.info("N2返回数据{}:", rs);
        JSONObject jsonObject = XmlOnMap.xml2JSON(rs.replace("encoding=\"utf-16\"", ""));
        log.info("N2Request startime{} endTime{} data {}", parameter.getStartdate(), parameter.getEnddate(), rs);
        String string = new Gson().toJson(jsonObject);
        log.info("解析后数据{}:", string);
        List<MessageBean> list = new ArrayList<>();
        list.add(JSON.parseObject(string, MessageBean.class));
        List Gameinfolist = list.get(0).getMessage().getResult().get(0).getGameinfo();
        if (Gameinfolist != null && !Gameinfolist.isEmpty()) return list;
        return new ArrayList<>();
    }
}

