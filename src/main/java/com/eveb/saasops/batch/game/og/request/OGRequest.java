package com.eveb.saasops.batch.game.og.request;

import com.eveb.saasops.batch.game.og.domain.BettingRecordByVendorLog;
import com.eveb.saasops.batch.game.og.domain.OGRequestParameter;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.AesUtil;
import com.eveb.saasops.batch.sys.util.MD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@Slf4j
@Service
public class OGRequest {

    private Integer count = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    public List<BettingRecordByVendorLog> getBetList(OGRequestParameter parameter, List<BettingRecordByVendorLog> list) {
        String para = "agent=" + parameter.getGmApi().getApiName() + "$vendorid=" + parameter.getMaxVendorid() + "$method=gbrbv";
        String paraEncoder = AesUtil.base64Encode(para.getBytes());
        String key = MD5.getMD5(paraEncoder + parameter.getGmApi().getMd5Key());
        String requestUrl = String.format(parameter.getGmApi().getPcUrl(), paraEncoder, key);
        //发起请求
//        Document doc = HttpRequestUtil.httpGet(requestUrl);
        Document doc = sendRequest(okHttpProxyUtils, requestUrl);

        //解析数据
        List<BettingRecordByVendorLog> bettingRecordByVendorLogs = xmlDataAnalysis(doc);
        list.addAll(bettingRecordByVendorLogs);
        log.info("=====>第【  " + count + " 】次请求OG拉取数据的参数值vendorid为：【 " + parameter.getMaxVendorid() + " 】");
        log.info("=====>请求OG拉取数据的URL为 : 【 " + requestUrl + " 】");
        if (bettingRecordByVendorLogs.size() < ApplicationConstant.CONSTANT_OG_DATACOUNTS) {
            return list;
        }
        count++;
        //限制访问频率一分钟内最多只能请求6次数据
        //Thread.sleep(ApplicationConstant.CONSTANT_OG_CALLON);
        parameter.setMaxVendorid(bettingRecordByVendorLogs.get(bettingRecordByVendorLogs.size() - 1).getVendorId());
        //尾递归判发请求
        return getBetList(parameter, list);
    }

    /**
     * 解析xml数据封装成BettingRecordByVendorLog对象
     */
    public List<BettingRecordByVendorLog> xmlDataAnalysis(Document doc) {

        try {
//            SAXReader reader = new SAXReader();
//            Document doc1 = reader.read(new File("E:\\001.xml"));
            List<BettingRecordByVendorLog> list = new ArrayList<>();
            Iterator<Element> it1 = doc.getRootElement().elementIterator("Data");
            while (it1.hasNext()) {
                Element elem1 = it1.next();
                Iterator<Element> it2 = elem1.elementIterator("properties");
                BettingRecordByVendorLog bettingRecordByVendorLog = new BettingRecordByVendorLog();
                while (it2.hasNext()) {
                    Element elem2 = it2.next();
                    String str = elem2.attribute("name").getValue();
                    switch (str) {
                        case "UserName": {
                            bettingRecordByVendorLog.setUserName(elem2.getText());
                            break;
                        }
                        case "GameRecordID": {
                            bettingRecordByVendorLog.setGameRecordID(Long.parseLong(elem2.getText()));
                            break;
                        }
                        case "OrderNumber": {
                            bettingRecordByVendorLog.setOrderNumber(elem2.getText());
                            break;
                        }
                        case "TableID": {
                            bettingRecordByVendorLog.setTableID(Integer.parseInt(elem2.getText()));
                            break;
                        }
                        case "Stage": {
                            bettingRecordByVendorLog.setStage(Integer.parseInt(elem2.getText()));
                            break;
                        }
                        case "Inning": {
                            bettingRecordByVendorLog.setInning(Integer.parseInt(elem2.getText()));
                            break;
                        }
                        case "GameNameID": {
                            bettingRecordByVendorLog.setGameNameID(Integer.parseInt(elem2.getText()));
                            break;
                        }
                        case "GameBettingKind": {
                            bettingRecordByVendorLog.setGameBettingKind(Integer.parseInt(elem2.getText()));
                            break;
                        }
                        case "GameBettingContent": {
                            bettingRecordByVendorLog.setGameBettingContent(elem2.getText());
                            break;
                        }
                        case "ResultType": {
                            bettingRecordByVendorLog.setResultType(Byte.parseByte(elem2.getText()));
                            break;
                        }
                        case "BettingAmount": {
                            bettingRecordByVendorLog.setBettingAmount(BigDecimal.valueOf(Double.parseDouble(elem2.getText())));
                            break;
                        }
                        case "CompensateRate": {
                            bettingRecordByVendorLog.setCompensateRate(BigDecimal.valueOf(Double.parseDouble(elem2.getText())));
                            break;
                        }
                        case "WinLoseAmount": {
                            bettingRecordByVendorLog.setWinLoseAmount(BigDecimal.valueOf(Double.parseDouble(elem2.getText())));
                            break;
                        }
                        case "Balance": {
                            bettingRecordByVendorLog.setBalance(BigDecimal.valueOf(Double.parseDouble(elem2.getText())));
                            break;
                        }
                        case "AddTime": {
                            bettingRecordByVendorLog.setAddTime(dateFormat.parse(elem2.getText()));
                            break;
                        }
                        case "PlatformID": {
                            bettingRecordByVendorLog.setPlatformID(Integer.parseInt(elem2.getText()));
                            break;
                        }
                        case "VendorId": {
                            bettingRecordByVendorLog.setVendorId(Long.parseLong(elem2.getText()));
                            break;
                        }
                        case "ValidAmount": {
                            bettingRecordByVendorLog.setValidAmount(BigDecimal.valueOf(Double.parseDouble(elem2.getText())));
                            break;
                        }
                        case "GameResult": {
                            bettingRecordByVendorLog.setGameResult(elem2.getText());
                            break;
                        }
                    }
                }
                bettingRecordByVendorLog.setProductID(Long.parseLong(bettingRecordByVendorLog.getOrderNumber()));
                list.add(bettingRecordByVendorLog);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document sendRequest(OkHttpProxyUtils okHttpProxyUtils, String requestUrl) {

        try {
            String result = okHttpProxyUtils.get(new OkHttpClient(), requestUrl);
            StringReader sr = new StringReader(result);
            InputSource is = new InputSource(sr);
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
