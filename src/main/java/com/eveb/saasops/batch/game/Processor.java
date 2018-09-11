package com.eveb.saasops.batch.game;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.ab.domain.AbBetLogModel;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.JsonUtil;
import com.eveb.saasops.batch.sys.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.eveb.saasops.batch.common.DateUtil.formatEsDate;

/**
 * @param <D>
 * @param <BetLogModel>
 * @created william
 */
@Slf4j
@Service
public abstract class Processor<D, BetLogModel> {
    protected String index;
    protected String type;
    protected String id;
    /**
     * 注单编号
     */
    private Long betNum;
    @Autowired
    private RptService rptService;
    @Autowired
    private RptElasticRestService rptElasticRestService;
    @Autowired
    private SysService sysService;
    @Autowired
    private JsonUtil jsonUtil;

    protected abstract List<BetLogModel> getRequest(GameParameter<Map<String, Object>> parameter);

    /**
     * 请求
     *
     * @param parameter
     * @return
     */
    protected List<GameBetLogModel<BetLogModel>> request(GameParameter<Map<String, Object>> parameter) throws Exception {
        List<GameBetLogModel<BetLogModel>> gameBetLogModels = new ArrayList<>();
        Processor processor = ((Processor) SpringContextUtils.getBean(parameter.processMap.get(parameter.getHandlerName())));
        processor.getRequest(parameter).forEach(bet -> {
            GameBetLogModel<BetLogModel> model = new GameBetLogModel<>();
            model.setBetLogModel((BetLogModel) bet);
            gameBetLogModels.add(model);
        });
        return gameBetLogModels;
    }

    /**
     * 执行器
     *
     * @param parameter
     * @return
     */
    protected Integer progressor(GameParameter<Map<String, Object>> parameter, List<GameBetLogModel<BetLogModel>> models) throws Exception {
        Processor processor = ((Processor) SpringContextUtils.getBean(parameter.processMap.get(parameter.getHandlerName())));
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        StringBuffer insStr = new StringBuffer();
        models = request(parameter);
        List<RptBetModel> rptList = new LinkedList<>();
        models.forEach(model -> {
            model.setApiPrefix(parameter.getApi().getApiName());
            model = processor.betHandler(model, prefixList);
            if (model != null) {
                rptList.add(processor.rptbetHandler(model));
                insStr.append(processor.toInsertString(model));
            }
        });
        try {
            rptElasticRestService.insertList(insStr.toString());
            rptElasticRestService.insertOrUpdateList(rptList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models.size();
    }

    /**
     * 处理原始注单
     *
     * @param model
     * @param prefixList
     * @return
     */
    protected abstract GameBetLogModel<BetLogModel> betHandler(GameBetLogModel<BetLogModel> model, List<String> prefixList);

    /**
     * 处理清洗的数据
     *
     * @param model
     * @return
     */
    protected abstract RptBetModel rptbetHandler(GameBetLogModel<BetLogModel> model);

    /**
     * 任务执行
     *
     * @param jobmodel
     * @throws Exception
     */
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(progressor(JSON.parseObject(jobmodel.getParamater(), GameParameter.class), null));

        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    protected String toInsertString(GameBetLogModel<BetLogModel> object) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + index + "\", \"_type\" : \"" + type + "\", \"_id\" : \"" + ((AbBetLogModel) object.getBetLogModel()).getBetNum() + "\" }}");
        string.append("\n");
        Map map = jsonUtil.toMap(jsonUtil.toJson(object.getBetLogModel()));
        map.put("apiPrefix", object.getApiPrefix());
        map.put("sitePrefix", object.getSitePrefix());
        map.put("betTime", formatEsDate(map.get("betTime").toString()));
        map.put("gameRoundEndTime", formatEsDate(map.get("gameRoundEndTime").toString()));
        map.put("gameRoundStartTime", formatEsDate(map.get("gameRoundStartTime").toString()));
        string.append(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
