package com.eveb.saasops.batch.game.hg.request;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eveb.saasops.batch.game.hg.domain.HGBetLog;
import com.eveb.saasops.batch.game.hg.domain.HGRequestParameter;
import com.eveb.saasops.batch.sys.util.XmlUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口请求及解析；2018-07-10
 */
@Slf4j
@Configuration
public class HGRequest {
    /**
     * xml请求接口
     * @param parameter
     * @return
     * @throws Exception
     */
    public List<HGBetLog> getBetList(HGRequestParameter parameter) {
        List<HGBetLog> list = new ArrayList<HGBetLog>();
        String username=""; //
        String password=""; //密码
        String casinoId=""; //赌场id
        String userId=""; //用户id
        String dateval=""; //日期  yyyy/MM/dd
        String timeRange="";//日期时间，0-24小时
        String status=""; //状态

        username=parameter.getApi().getAgyAcc();
        password=parameter.getApi().getMd5Key();
        casinoId=parameter.getApi().getSecureCodes().get("casinoId");
        if(StringUtil.isNotEmpty(parameter.getDateval()))
            dateval=parameter.getDateval();
        if(StringUtil.isNotEmpty(parameter.getTimeRange()))
            timeRange=parameter.getTimeRange();
        HttpClient httpclient = new HttpClient();
        String requestUrl = parameter.getApi().getApiUrl()+"betapi.asmx/GetAllbetdetails";
        PostMethod httpPost = new PostMethod(requestUrl);
        StringBuffer xmlresponse = new StringBuffer();
        xmlresponse.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        xmlresponse.append("<GetBetdetails>");
        xmlresponse.append("<Username>"+username+"</Username>");
        xmlresponse.append("<Password>"+password+"</Password>");
        xmlresponse.append("<CasinoId>"+casinoId+"</CasinoId>");
        xmlresponse.append("<UserId>"+userId+"</UserId>");
        xmlresponse.append("<Dateval>"+ dateval+"</Dateval>");
        xmlresponse.append("<TimeRange>"+timeRange+"</TimeRange>");
        xmlresponse.append("<Status>"+status+"</Status>");
        xmlresponse.append("</GetBetdetails>");
        httpPost.addRequestHeader("Content-Type", "text/xml");

        log.info("HG请求url  hg请求ps:", requestUrl, xmlresponse.toString());
        try {
            httpPost.setRequestBody(xmlresponse.toString());
            httpclient.executeMethod(httpPost);
            String res = httpPost.getResponseBodyAsString();
            log.info("HG返回数据 :", res);
            res = res.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
            res = res.replace("<string xmlns=\"http://service.hogaming.info/\">", "");
            res = res.replace("</string>", "");
            res = res.replace("&lt;", "<");
            res = res.replace("&gt;", ">");
            res = res.replace("\r\n", "");
            JSONObject jsonObject = XmlUtil.xml2JSON(res.getBytes());
            JSONObject betObject = jsonObject.getJSONObject("BetInfos");
            log.info("Betinfo返回数据 :", betObject.get("Betinfo").toString());
            String betInfoList = betObject.get("Betinfo").toString();
            betInfoList = betInfoList.replace("[", "");
            betInfoList = betInfoList.replace("]", "");
            list = JSON.parseArray("[" + betInfoList + "]", HGBetLog.class);
        }catch (Exception e){
            log.info("HGRequest not BetInfos:"+e.getMessage());
            list = null;
        }
        return list;
    }

}
