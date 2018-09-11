package com.eveb.saasops.batch.game.gg.request;


import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.gg.domain.GgBetLog;
import com.eveb.saasops.batch.game.gg.domain.GgRequestParameter;
import com.eveb.saasops.batch.sys.util.MD5;
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
 * GG取玩家下注日志接口；2018-08-02
 */
@Slf4j
@Configuration
public class GgRequest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    public List<GgBetLog> getBetList(GgRequestParameter parameter) {
        List<GgBetLog> resultList = new ArrayList<>();
        String cagent = parameter.getApi().getAgyAcc(); //代理编码
        String desKey = parameter.getApi().getSecureCodes().get("des_key");
        String md5Key = parameter.getApi().getMd5Key();

        String startdate = parameter.getStartDate();//开始时间，格式 yyyy-MM-ddHH:mm:ss
        String enddate = parameter.getEndDate();//游戏账号的密码
        //String gameId = parameter.getGameId();//游戏编码  + "/\\\\/gameId=" + gameId
        String method = parameter.getMethod();//数值 ,是一個常數
        try {
            //加密
            GgDESEncrypt des = new GgDESEncrypt(desKey);
            String params = des.encrypt("cagent=" + cagent + "/\\\\/startdate=" + startdate + "/\\\\/enddate=" + enddate  + "/\\\\/method=" + method);
            String key = MD5.getMD5(params + md5Key);
            //添加参数
            Map<String, String> mapParam = new HashMap<>();
            mapParam.put("params", params);
            mapParam.put("key", key);
            //设置请求头
            Map<String, String> mapHead = new HashMap<>();
            mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
            String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), parameter.getApi().getApiUrl(), mapParam);
            if (StringUtil.isNotEmpty(result)) {
                Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
                Integer code = Integer.parseInt(jsonMap.get("code").toString());
                if (code != null && code == 0) {
                    resultList = JSON.parseArray(jsonMap.get("recordlist").toString(), GgBetLog.class);
                }
            }
        } catch (Exception e) {
            log.error("GG请求错误：", e);
            return null;
        }
        return resultList;
    }

    public static void main(String[] args) {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("betlist", "123");
        String recordlist = mapParam.get("recordlist");
        System.out.println(recordlist);

    }
}
