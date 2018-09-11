package com.eveb.saasops.batch.game.gns.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.gns.model.*;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GnsReuqets {

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    public List<GnsBetLogModel> getGnsBetList(IParameterModel parameter) {
        GnsParameterModel gns=JSON.parseObject(parameter.getT().toString(),GnsParameterModel.class);
        /**API线路**/
        TGmApi api = parameter.getApi();
        String rurl = String.format(GnsConstant.URL, api.getPcUrl2(), parameter.getStartTime(), parameter.getEndTime(), gns.getLimit(), gns.getStartIndex());
        try {
            String rsData = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(initHeand(api)), rurl);
            log.info("GNS请求url :" + rurl);
            log.info("GNS返回数据 :" + rsData);
            GnsResponseModel gnsrsp = JSON.parseObject(rsData, GnsResponseModel.class);
            if (gnsrsp.getTotal_query_size() <= ApplicationConstant.CONSTANT_GNS_LIMIT) {
                return gnsrsp.getData();
            }
            gns.setStartIndex(gns.getStartIndex() + ApplicationConstant.CONSTANT_GNS_LIMIT);
            parameter.setT(gns);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
        return getGnsBetList(parameter);
    }

    private Map initHeand(TGmApi api){
        Map map=new HashMap();
        map.put(GnsConstant.HEAD_PARTNER,api.getAgyAcc());
        map.put(GnsConstant.HEAD_CINTENT_TYPE_KEY,GnsConstant.HEAD_CINTENT_TYPE_VALUE);
        return map;
    }

}
