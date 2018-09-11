package com.eveb.saasops.batch.game.ab.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.GameParameter;
import com.eveb.saasops.batch.game.Request;
import com.eveb.saasops.batch.game.ab.domain.AbBetLogModel;
import com.eveb.saasops.batch.game.ab.domain.AbResponse;
import com.eveb.saasops.batch.game.ab.domain.TripleDES;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.sys.util.JsonUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eveb.saasops.batch.game.ab.processor.AbProcessor.getTimett;

@Slf4j
@Component("AbRequest")
public class AbRequest implements Request<AbBetLogModel> {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    @Autowired
    private JsonUtil jsonUtil;
    private static final String BETLOG_PIECEOF_HISTORIES_IN30DAYS = "betlog_pieceof_histories_in30days";

    public List<AbBetLogModel> getBetList(GameParameter<Map<String, Object>> parameter) throws Exception {
        TGmApi api = parameter.getApi();
        Map<String, String> secureCode = (Map<String, String>) JSON.parse(api.getSecureCode());
        String queryString = String.format("agent=%s&random=%s&startTime=%s&endTime=%s", "",
                new SecureRandom().nextLong(), parameter.getStartTime(), parameter.getEndTime());
        Map<String, String> params = new HashMap<>();
        String data = TripleDES.encrypt(queryString, secureCode.get("DESKey"), null);
        params.put("data", URLEncoder.encode(data, "UTF8"));
        params.put("propertyId", secureCode.get("propertyId"));
        params.put("sign", URLEncoder.encode(Base64.encodeBase64String(DigestUtils.md5((data + secureCode.get("MD5Key")))), "UTF8"));
        String rs = null;
        log.info("abRequest startime{} endTime{} data {} propertyId {}  ", parameter.getStartTime(), parameter.getEndTime(), data, secureCode.get("propertyId"), params.get("sign"));
        try {
            rs = AbHttpClientGet(secureCode.get("apiUrl") + BETLOG_PIECEOF_HISTORIES_IN30DAYS, params);
            log.info("ab Result {}   ", rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AbResponse abResponse = jsonUtil.fromJson(rs, AbResponse.class);
        log.info("abResponse {}   " + JSON.toJSON(abResponse));
        List<AbBetLogModel> abBetLogModels = JSON.parseArray(JSON.toJSONString(((Map) JSON.parse(rs)).get("histories")), AbBetLogModel.class);
        abBetLogModels.stream().forEach(e -> {
            e.setBetTime(getTimett(e.getBetTime()));
            e.setGameRoundStartTime(getTimett(e.getGameRoundStartTime()));
            e.setGameRoundEndTime(getTimett(e.getGameRoundEndTime()));
        });
        return abBetLogModels;
    }

    public String AbHttpClientGet(String url, Map<String, String> queryMap) throws Exception {
        /**使用代理**/
        OkHttpClient.Builder builder = okHttpProxyUtils.setProxys(new OkHttpClient.Builder());
        return okHttpProxyUtils.get(builder.build(), url, queryMap);
    }
}
