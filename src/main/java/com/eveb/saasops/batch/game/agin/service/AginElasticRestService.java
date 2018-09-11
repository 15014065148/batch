package com.eveb.saasops.batch.game.agin.service;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.agin.domain.AginBetLogModel;
import com.eveb.saasops.batch.game.agin.domain.AGINHunterBetLogModel;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 15:00 2017/12/28
 **/
@Service
public class AginElasticRestService {
    @Autowired
    private ElasticSearchService conn;

    public void insertList(String type, List<Object> list) throws Exception {
        StringBuffer string = new StringBuffer();
        for (Object object : list) {
            if (object instanceof AGINHunterBetLogModel) {
                string.append("{ \"index\" : { \"_index\" : \"" + PlatFromEnum.Enum_AGIN.getKey().toLowerCase() + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + ((AGINHunterBetLogModel) object).getTradeNo() + "\" }}");
            } else {
                string.append("{ \"index\" : { \"_index\" : \"" + PlatFromEnum.Enum_AGIN.getKey().toLowerCase() + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + ((AginBetLogModel) object).getBillNo() + "\" }}");
            }
            string.append("\n");
            string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            string.append("\n");
        }
        if (list.size() > 0) {
            HttpEntity entity = new NStringEntity(string.toString(), ContentType.APPLICATION_JSON);
            conn.restClient.performRequest("POST", "_bulk", Collections.singletonMap("pretty", "true"), entity);
        }
    }
}
