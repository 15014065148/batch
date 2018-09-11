package com.eveb.saasops.batch.game.bbin.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bbin.domain.BbinBetLog;
import com.eveb.saasops.batch.game.bbin.domain.BBINRequestParameter;
import com.eveb.saasops.batch.game.report.domain.RptBetDayModel;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.EvebMD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class BbinRequest {


    @Autowired
    private OkHttpProxyUtils clientService;

    public List<BbinBetLog> getBBINBetLogList(BBINRequestParameter parameter) throws Exception {
        /**API线路**/
        TGmApi api = parameter.getApi();
        String keys[] = ((Map) JSON.parse(api.getSecureCode())).get(parameter.getMethod()).toString().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL);
        String valid = sdf.format(cal.getTime());
        Map paramap = new HashMap();
        paramap.put("website", api.getWebName());
        paramap.put("uppername", api.getAgyAcc());
        paramap.put("action", parameter.getAction());
        paramap.put("starttime", parameter.getStarttime());
        paramap.put("endtime", parameter.getEndtime());
        paramap.put("rounddate", parameter.getRounddate());
        paramap.put("date", parameter.getDate());
        paramap.put("start_date", parameter.getRounddate());//api限定不可跨天，传入统一的时间
        paramap.put("end_date", parameter.getRounddate());//api限定不可跨天，传入统一的时间
        paramap.put("gamekind", parameter.getGamekind());
        paramap.put("subgamekind", parameter.getSubgamekind());
        paramap.put("gametype", parameter.getGametype());
        paramap.put("page", parameter.getPage());
        paramap.put("pagelimit", parameter.getPagelimit());
        paramap.put("key", keys[0] + EvebMD5.getMD5(api.getWebName() + "" + keys[1] + valid) + keys[2]);
        String rsstr = clientService.postForm(clientService.bbinclient, api.getPcUrl() + parameter.getMethod(), paramap);
        log.info("BBIN请求url :" + api.getPcUrl() + parameter.getMethod() + JSON.toJSONString(paramap));
        log.info("BBIN返回数据 :" + rsstr);
        Map rsmap = (Map) JSON.parse(rsstr);
        /**抛出异常，让XXL捕获，进行异常重试**/
        String resultStr = rsmap.get("result").toString();
        if (resultStr.equals("true")) {
            List<BbinBetLog> list = JSON.parseArray(rsmap.get("data").toString(), BbinBetLog.class);
            Map pagemap = JSON.parseObject(rsmap.get("pagination").toString());
            int page = Integer.parseInt(pagemap.get("Page").toString());
            int TotalPage = Integer.parseInt(pagemap.get("TotalPage").toString());
            /**直到获取到最终页数**/
            if (page < TotalPage) {
                parameter.setPage(parameter.getPage() + 1);
                list.addAll(getBBINBetLogList(parameter));
            }
            return list;
        } else {
            XxlJobLogger.log("Result:" + rsmap.toString());
            throw new Exception(rsmap.toString());
        }
    }
}
