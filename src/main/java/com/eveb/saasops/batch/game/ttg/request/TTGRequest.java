package com.eveb.saasops.batch.game.ttg.request;

import com.eveb.saasops.batch.game.ttg.domain.TTGElectronicBetLog;
import com.eveb.saasops.batch.game.ttg.domain.TTGRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Configuration
public class TTGRequest {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    /**
     * 获取投注记录
     */
    public List<TTGElectronicBetLog> getBetList(TTGRequestParameter ttgRequestParameter) {
        //发起请求
        Document doc = sendRequest(ttgRequestParameter);
        //数据解析
        List<TTGElectronicBetLog> tTGElectronicBetLogs = xmlDataAnalysis(doc);
        log.info("=====>线路【  " + ttgRequestParameter.getApi().getAgyAcc() + " 】本次拉取的数据数量为：【 " + tTGElectronicBetLogs.size() + " 】条");
        log.info("=====>*************TTG平台*************");
        return tTGElectronicBetLogs;
    }

    /**
     * 发送请求
     */
    private Document sendRequest(TTGRequestParameter ttgRequestParameter) {
        try {
            String url = ttgRequestParameter.getApi().getPcUrl();
            //请求实体
            String param = getRequestBody(ttgRequestParameter);
            //设置请求头
            Map<String, String> head = getRequestHead(ttgRequestParameter);
            log.info("=====>线路 ：【 "+ttgRequestParameter.getApi().getAgyAcc()+" 】请求TTG拉取数据的URL为 : 【 " + url + " 】 ");
            log.info("=====>线路 ：【 "+ttgRequestParameter.getApi().getAgyAcc()+" 】请求TTG拉取数据的实体内容为 : 【 " + param + " 】 ");
            log.info("=====>线路 ：【 "+ttgRequestParameter.getApi().getAgyAcc()+" 】请求TTG拉取数据的请求头为 : 【 " + head + " 】 " );
            //发送请求
            String result = okHttpProxyUtils.postXml(okHttpProxyUtils.initHeadClient(head), url, param,"text/xml");
           if(StringUtils.isEmpty(result)){
                log.info("=====>线路 ：【 "+ttgRequestParameter.getApi().getAgyAcc()+" 】没有权限，请核实");
               result=getResult();
            }
            StringReader sr = new StringReader(result);
            InputSource is = new InputSource(sr);
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            return doc;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置请求实体
     */
    private String getRequestBody(TTGRequestParameter ttgRequestParameter) {
        String start = ttgRequestParameter.getStartdate();
        String end = ttgRequestParameter.getEnddate();
        //2018-07-03 17:00:00
        String startDate = start.substring(0, 10).replace("-", "");
        String startDateHour = start.substring(start.indexOf(" ") + 1, start.indexOf(":"));
        String startDateMinute = start.substring(start.indexOf(":") + 1, start.lastIndexOf(":"));
        String endDate = end.substring(0, 10).replace("-", "");
        String endDateHour = end.substring(start.indexOf(" ") + 1, start.indexOf(":"));
        String endDateMinute = end.substring(start.indexOf(":") + 1, start.lastIndexOf(":"));
        String body = " <searchdetail requestId=\"test2\">\n" +
                " <daterange startDate=\"%s\" startDateHour=\"%s\" startDateMinute=\"%s\" endDate=\"%s\" endDateHour=\"%s\" endDateMinute=\"%s\"/>\n" +
                " <account currency=\"CNY\" />\n" +
                " <transaction transactionType=\"Game\"/>\n" +
                " <partner includeSubPartner=\"Y\" />\n" +
                " </searchdetail>";
        String requestBody = String.format(body, startDate, startDateHour, startDateMinute, endDate, endDateHour, endDateMinute);
        return requestBody;
    }

    /**
     * xml数据解析
     */
    private List<TTGElectronicBetLog> xmlDataAnalysis(Document doc) {
        if(doc==null){
            return null;
        }
        List<TTGElectronicBetLog> tTGElectronicBetLogs = new ArrayList<>();
        Iterator<Element> it = doc.getRootElement().element("details").elementIterator("transaction");
        while (it.hasNext()) {
            TTGElectronicBetLog tTGElectronicBetLog = new TTGElectronicBetLog();
            Element element = it.next();
            String playerId = element.element("player").attributeValue("playerId").toLowerCase();
            String partnerId = element.element("player").attributeValue("partnerId").toLowerCase();
            String transactionId = element.element("detail").attributeValue("transactionId");
            String transactionDate = element.element("detail").attributeValue("transactionDate");
            String currency = element.element("detail").attributeValue("currency");
            String game = element.element("detail").attributeValue("game");
            String transactionSubType = element.element("detail").attributeValue("transactionSubType");
            String handId = element.element("detail").attributeValue("handId");
            String amount = element.element("detail").attributeValue("amount");
            tTGElectronicBetLog.setPlayerId(playerId);
            tTGElectronicBetLog.setPartnerId(partnerId);
            tTGElectronicBetLog.setTransactionId(Long.parseLong(transactionId));
            setTransactionDate(tTGElectronicBetLog, transactionDate);
            tTGElectronicBetLog.setCurrency(currency);
            tTGElectronicBetLog.setGame(game);
            tTGElectronicBetLog.setTransactionSubType(transactionSubType);
            tTGElectronicBetLog.setHandId(Long.parseLong(handId));
            tTGElectronicBetLog.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
            tTGElectronicBetLogs.add(tTGElectronicBetLog);
        }
        return tTGElectronicBetLogs;
    }

    /**
     * 设置日期字段
     */
    public void setTransactionDate(TTGElectronicBetLog tTGElectronicBetLog, String transactionDate) {
        try {
            StringBuilder sb = new StringBuilder(transactionDate);
            sb.insert(4, "/").insert(7, "/");
            tTGElectronicBetLog.setTransactionDate(dateFormat.parse(sb.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求头
     */
    private Map<String, String> getRequestHead(TTGRequestParameter ttgRequestParameter) {
        Map<String, String> head = new HashMap<>();
        head.put("T24-Affiliate-Id", ttgRequestParameter.getApi().getAgyAcc());
        head.put("T24-Affiliate-Login", ttgRequestParameter.getApi().getMd5Key());
        head.put("Content-Type", "text/xml");
        return head;
    }

    private String getResult(){
        String result="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<searchdetail totalRecords=\"0\" pageSize=\"0\" requestId=\"test2\">\n" +
                "    <details/>\n" +
                "</searchdetail>";
        return result;
    }
}
