package com.eveb.saasops.batch.game.fg.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.fg.domain.FgBetLog;
import com.eveb.saasops.batch.game.fg.domain.FgRequestParameter;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FG取玩家下注日志接口；2018-07-19
 */
@Slf4j
@Configuration
public class FgRequest {
    //public static final String HG_SERVER_URL = "https://api.ppro.98078.net/v2/agent/log_by_page/gt/";
    public static final String HG_SERVER_URL_HEAD_ACCEPT = "application/json";
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    public List<FgBetLog> getBetList(FgRequestParameter parameter) {
        List<FgBetLog> resultList = new ArrayList<FgBetLog>();
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", HG_SERVER_URL_HEAD_ACCEPT);
        mapHead.put("merchantname", parameter.getApi().getAgyAcc());
        mapHead.put("merchantcode", parameter.getApi().getMd5Key());
        String hgGetUrl = parameter.getApi().getApiUrl() + "/start_time/" + parameter.getStartDate() + "/end_time/" + parameter.getEndDate();
        try {
            String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), hgGetUrl);
            if (StringUtil.isNotEmpty(result)) {
                Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
                String page_key = jsonMap.get("page_key").toString();
                resultList = JSON.parseArray(jsonMap.get("data").toString(), FgBetLog.class);
                while (page_key != null && !page_key.equals("none")) { //page_key 为none 无下一页
                    String resultPage = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), hgGetUrl + "/page_key/" + page_key);
                    if (StringUtil.isNotEmpty(resultPage)) {
                        Map<String, Object> jsonMapPage = (Map<String, Object>) JSON.parse(resultPage);
                        page_key = jsonMapPage.get("page_key").toString();
                        resultList.addAll(JSON.parseArray(jsonMapPage.get("data").toString(), FgBetLog.class));
                    }else {
                        page_key = null;
                    }
                }
            }
        } catch (Exception e) {
            log.error("FG请求错误：", e);
            return null;
        }
        return resultList;
    }
}
