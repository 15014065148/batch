package com.eveb.saasops.batch.game.og.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.game.og.domain.OGRequestParameter;
import com.eveb.saasops.batch.game.og.process.OGGameProcessor;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.scheduled.JobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class IOGJobHandler extends JobHandler {

    @Autowired
    private RptService rptService;

    @Autowired
    private OGGameProcessor oGGameProcessor;

    @Override
    public ReturnT<String> execute(String... strings) {
        List<OGRequestParameter> oGRequestParameters = prepareApiLine();
        //获取每条线路的maxVendorid
        for (OGRequestParameter o : oGRequestParameters) {
            //从ES中取出根据线路取出对应线路最大的vendorId作为查询的条件
            Map map = oGGameProcessor.getMaxVendorid();
            for (Object obj : (JSONArray) ((Map) ((Map) map.get("aggregations")).get("apiPrefix")).get("buckets")) {
                Map objMap = (Map) obj;
                String apiPrefix = (String) objMap.get("key");
                BigDecimal bd = new BigDecimal((((Map) objMap.get("vendorId")).get("value")) + "");
                String maxVendoridStr = bd.toPlainString();
                if (o.getGmApi().getApiName().equals(apiPrefix)) {
                    log.info("maxVendorid:" + maxVendoridStr);
                    o.setMaxVendorid(Long.parseLong(maxVendoridStr));
                }
            }
            if(o.getMaxVendorid()==null ){
                o.setMaxVendorid(1415366207591L);
            }
        }
        return doExecute(oGRequestParameters);
    }

    /***
     *  执行
     * @throws Exception
     */
    protected ReturnT<String> doExecute(List<OGRequestParameter> oGRequestParameters) {
        for (OGRequestParameter oGRequestParameter : oGRequestParameters) {
            try {
                JobFailMessageModel model = new JobFailMessageModel();
                model.setPlatform(PlatFromEnum.Enum_OG.getValue());
                model.setApiName(oGRequestParameter.getGmApi().getApiName());
                model.setParamater(JSON.toJSONString(oGRequestParameter));
                oGGameProcessor.executeJob(model);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 获取Api线路前缀
     */
    protected List<OGRequestParameter> prepareApiLine() {
        List<OGRequestParameter> list = new ArrayList<>();
        List<TGmApi> apiList = rptService.getGameApiByApiName(PlatFromEnum.Enum_OG.getValue(), null);
        for (TGmApi tGmApi : apiList) {
            OGRequestParameter ogRequestParameter = new OGRequestParameter();
            ogRequestParameter.setGmApi(tGmApi);
            list.add(ogRequestParameter);
        }
        return list;
    }


}
