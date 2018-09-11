package com.eveb.saasops.batch.game.nt.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.nt.domain.*;
import com.eveb.saasops.batch.sys.util.HttpRequestUtil;
import com.eveb.saasops.batch.sys.util.XMLHttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.*;

@Slf4j
@Configuration
@ConfigurationProperties(prefix="nt")
public class NTRequests {


    @Value("${nt.url}")
    private String url;

    /****
     * 获取NT游戏统计
     * @return
     */
    public GetGameStatisticsResponse getGameStatistics(NTRequestParam param)
    {
        String url="https://lsl.omegasys.eu/connect/cxf/SecondaryWallet?wsdl";
        GetGameStatisticsResponse response=new GetGameStatisticsResponse();
        /**发送XML组装参数**/
        param.setFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        param.setFunction("getGameStatistics");
        String rs=XMLHttpRequestUtil.send(url,param.toString());
        try {
            JAXBContext context = JAXBContext.newInstance(GetGameStatisticsResponse.class);
            //反序列化
            Unmarshaller unmarshaller = context.createUnmarshaller();
            /**去掉不需要的元素**/
            rs=rs.substring(rs.indexOf("<ns2:getGameStatisticsResponse"),rs.indexOf("</soap:Body>")).replace("ns2:","");
            StringReader reader = new StringReader(rs);
            response =(GetGameStatisticsResponse)unmarshaller.unmarshal(reader);

        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    /****
     * 获取NT游戏汇总
     * @return
     */
    public PlayerGamePlaySummary getPlayerGamePlaySummary(NTRequestParam param)
    {
        String url="https://lsl.omegasys.eu/connect/cxf/SecondaryWallet?wsdl";
        PlayerGamePlaySummary response=new PlayerGamePlaySummary();
        /**发送XML组装参数**/
        param.setFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        param.setFunction("getPlayerGamePlaySummaryReq");
        String rs=XMLHttpRequestUtil.send(url,param.toString());
        try {
            JAXBContext context = JAXBContext.newInstance(PlayerGamePlaySummary.class);
            //反序列化
            Unmarshaller unmarshaller = context.createUnmarshaller();
            /**去掉不需要的元素**/
            rs=rs.substring(rs.indexOf("<ns2:getPlayerGamePlaySummaryResponse"),rs.indexOf("</soap:Body>")).replace("ns2:","");
            StringReader reader = new StringReader(rs);
            response =(PlayerGamePlaySummary)unmarshaller.unmarshal(reader);

        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    /****
     *获取收入统计报表
     * @return
     */
    public List<RevenueReport> generateRevenueReport(NTRequestParam param)
    {
        String url="https://lsl.omegasys.eu/connect/cxf/SecondaryWallet?wsdl";
        List<RevenueReport> list=new ArrayList<>();
        /**发送XML组装参数**/
        param.setFormat("yyyy-MM-dd");
        param.setFunction("generateRevenueReportReq");
        String rs=XMLHttpRequestUtil.send(url,param.toString());
        try {
            /**去掉不需要的元素**/
            rs=rs.substring(rs.indexOf("<csvData>")+1,rs.indexOf("</csvData>"));
            String[] str=rs.split("\n");
            for (int i=1;i<str.length;i++) {
                list.add(new RevenueReport(str[i]));
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return list;
    }

    /***
     * 获取所有的交易记录，包含注单
     * @param lastTransactionId
     * @return
     */
    public List<NTBetLog> getNTBetLog(Long lastTransactionId) {
        /**传入lastTransactionId，获取该id之后的数据**/
//        try {
//            log.info(url + lastTransactionId);
//            String rs = HttpRequestUtil.httpsPost(url + lastTransactionId);
//            log.info(rs);
//            Map rsmap = (Map) JSON.parse(rs);
//            List<NTBetLog> list = JSON.parseArray(rsmap.get("transactions").toString(), NTBetLog.class);
//            return list;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
        return new ArrayList<>();
    }
}
