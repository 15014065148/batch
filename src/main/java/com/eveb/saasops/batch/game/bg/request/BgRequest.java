package com.eveb.saasops.batch.game.bg.request;


import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bg.domian.BgFishingBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgRequestParameter;
import com.eveb.saasops.batch.game.bg.domian.BgVideoBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgVideoTipBetLog;
import com.eveb.saasops.batch.game.bg.util.BgParamerUtil;
import com.eveb.saasops.batch.sys.util.DateUtil;
import com.eveb.saasops.batch.sys.util.MD5;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;

/**
 * BG取玩家下注日志接口；
 * 2018-08-08 Jeff
 */
@Slf4j
@Configuration
public class BgRequest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    /**
     * 获取视讯注单
     *
     * @param parameter
     * @return
     */
    public List<BgVideoBetLog> getVideoBetList(BgRequestParameter parameter) {
        List<BgVideoBetLog> resultList = new ArrayList<>();
        parameter.setMethod(BgParamerUtil.BG_METHOD_URL_VIDEO);
        String items = resultJson(parameter);
        if (StringUtil.isNotEmpty(items) && JSON.parseArray(items).size() > 0) {
            resultList = JSON.parseArray(items, BgVideoBetLog.class);
        }
        return resultList;
    }

    /**
     * 获取视讯小费注单
     *
     * @param parameter
     * @return
     */
    public List<BgVideoTipBetLog> getVideoTipBetList(BgRequestParameter parameter) {
        List<BgVideoTipBetLog> resultList = new ArrayList<>();
        parameter.setMethod(BgParamerUtil.BG_METHOD_URL_TIP);
        String items = resultJson(parameter);
        if (StringUtil.isNotEmpty(items) && JSON.parseArray(items).size() > 0) {
            resultList = JSON.parseArray(items, BgVideoTipBetLog.class);
        }
        return resultList;
    }

    /**
     * 获取捕鱼注单
     *
     * @param parameter
     * @return
     */
    public List<BgFishingBetLog> getFishBetList(BgRequestParameter parameter) {
        List<BgFishingBetLog> resultList = new ArrayList<>();
        parameter.setMethod(BgParamerUtil.BG_METHOD_URL_FISH);
        String items = resultJson(parameter);
        if (StringUtil.isNotEmpty(items) && JSON.parseArray(items).size() > 0) {
            resultList = JSON.parseArray(items, BgFishingBetLog.class);
        }
        return resultList;
    }


    /**
     * 请求
     *
     * @param paramer
     * @return
     */
    private String resultJson(BgRequestParameter paramer) {
        String itemsList = null;
        String url = paramer.getApi().getApiUrl();
        String sn = paramer.getApi().getAgyAcc();//厅代码
        String secretKey = paramer.getApi().getMd5Key();//秘钥
        String method = paramer.getMethod();//请求路径

        String id = UUID.randomUUID().toString();
        String random = id; //随机字符串，建议使用UUID
        String sign = MD5.getMD5(random + sn + secretKey); //签名摘要digest=md5(random+sn+secretKey)
        String startTime = DateUtil.format(paramer.getStartDate(), DateUtil.FORMAT_18_DATE_TIME);
        String endTime = DateUtil.format(paramer.getEndDate(), DateUtil.FORMAT_18_DATE_TIME);

        String agentId = null;//代理ID
        String agentLoginId = null;//登录ID
        if (StringUtil.isNotEmpty(paramer.getApi().getSecureCode())) {
            agentId = paramer.getApi().getSecureCodes().get("agentId");
            agentLoginId = paramer.getApi().getSecureCodes().get("agentLoginId");
        }

        Map<String, String> postDate = new HashMap<>();
        postDate.put("id", id);
        postDate.put("method", method);
        postDate.put("jsonrpc", BgParamerUtil.BG_URL_JSONRPC);

        postDate.put("random", random);
        postDate.put("sign", sign);
        postDate.put("sn", sn);
        postDate.put("startTime", startTime);
        postDate.put("endTime", endTime);
        if (StringUtil.isNotEmpty(agentId)) {
            postDate.put("agentId", agentId);
        }
        if (StringUtil.isNotEmpty(agentLoginId)) {
            postDate.put("agentLoginId", agentLoginId);
        }
        try {
            String result = okHttpProxyUtils.postJson(okHttpProxyUtils.proxyClient, url, postDate);
            Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
            Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(jsonMap.get("result").toString());
            if (resultMap.get("items") != null) {
                itemsList = resultMap.get("items").toString();
            }
        } catch (Exception e) {
            log.error("BG请求错误：", e);
            return null;
        }
        return itemsList;
    }

}
