package com.eveb.saasops.batch.game.bg.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.bg.domian.BgFishingBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgRequestParameter;
import com.eveb.saasops.batch.game.bg.domian.BgVideoBetLog;
import com.eveb.saasops.batch.game.bg.domian.BgVideoTipBetLog;
import com.eveb.saasops.batch.game.bg.request.BgRequest;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.DateConvert;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BG平台
 * 2018-08-08  Jeff
 */
@Slf4j
@Component
public class BgProcessor {

    @Autowired
    private RptService rptService;
    @Autowired
    private BgRequest bgRequest;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;


    @Async("bgAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            Integer video = videoProcess(JSON.parseObject(jobmodel.getParamater(), BgRequestParameter.class));//视讯
            Integer videoTip = tipProcess(JSON.parseObject(jobmodel.getParamater(), BgRequestParameter.class));//小费
            Integer fish = fishProcess(JSON.parseObject(jobmodel.getParamater(), BgRequestParameter.class));//捕鱼
            jobmodel.setCounts(video + videoTip + fish);
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    /**
     * 视讯
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public Integer videoProcess(BgRequestParameter parameter) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<BgVideoBetLog> videoList = bgRequest.getVideoBetList(parameter);

        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (BgVideoBetLog item : videoList) {
            counts++;
            item.setLoginId(item.getLoginId().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getLoginId().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setLoginId(item.getLoginId().substring(fore.length(), item.getLoginId().length()).toLowerCase());
                    break;
                }
            }
            item.setOrderTime(DateConvert.convertAsiaDate(item.getOrderTime()));//美东转北京时间
            item.setLastUpdateTime(DateConvert.convertAsiaDate(item.getLastUpdateTime()));//美东转北京时间
            item.setApiPrefix(parameter.getApi().getApiName());
            insstr.append(toInsertString(item));
            rptList.add(BgRptBetProcessor.videoProcess(item));
        }
        rptElasticService.insertList(insstr.toString());
        /**因不存在异动单，所以进行插入和更新**/
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    /**
     * 小费
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public Integer tipProcess(BgRequestParameter parameter) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<BgVideoTipBetLog> tipList = bgRequest.getVideoTipBetList(parameter);


        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (BgVideoTipBetLog item : tipList) {
            counts++;
            item.setLoginId(item.getLoginId().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                if (item.getLoginId().startsWith(fore)) {//如果存在前缀则进行截取
                    item.setSitePrefix(fore);
                    item.setLoginId(item.getLoginId().substring(fore.length(), item.getLoginId().length()).toLowerCase());
                    break;
                }
            }

            item.setCreateTime(DateConvert.convertAsiaDate(item.getCreateTime()));//美东转北京时间
            item.setDownloadTime(new Date());
            item.setApiPrefix(parameter.getApi().getApiName());
            insstr.append(toInsertString(item));
        }
        rptElasticService.insertList(insstr.toString());
        return counts;
    }

    /**
     * 电子游戏
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public Integer fishProcess(BgRequestParameter parameter) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());
        List<BgFishingBetLog> fishList = bgRequest.getFishBetList(parameter);

        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (BgFishingBetLog item : fishList) {
            counts++;
            item.setLoginId(item.getLoginId().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getLoginId().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setLoginId(item.getLoginId().substring(fore.length(), item.getLoginId().length()).toLowerCase());
                    break;
                }
            }
            item.setOrderTime(DateConvert.convertAsiaDate(item.getOrderTime()));
            insstr.append(toInsertString(item));
            item.setApiPrefix(parameter.getApi().getApiName());
            rptList.add(BgRptBetProcessor.fishProcess(item));
        }
        rptElasticService.insertList(insstr.toString());
        /**因不存在异动单，所以进行插入和更新**/
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }


    /***
     * 转换成插入或修改语句
     * @param object
     * @return
     */
    public String toInsertString(Object object) {
        StringBuffer string = new StringBuffer();
        if (object instanceof BgVideoBetLog) {
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.BG_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.BG_TYPE_VIDEO + "\", \"_id\" : \"" + ((BgVideoBetLog) object).getOrderId() + "\" }}");
        } else if (object.getClass() == BgVideoTipBetLog.class) {
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.BG_INDEX + "_tip" + "\", \"_type\" : \"" + ElasticSearchConstant.BG_TYPE_TIP + "\", \"_id\" : \"" + ((BgVideoTipBetLog) object).getTipId() + "\" }}");
        } else {
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.BG_INDEX + "_fish" + "\", \"_type\" : \"" + ElasticSearchConstant.BG_TYPE_FISH + "\", \"_id\" : \"" + ((BgFishingBetLog) object).getBetId() + "\" }}");
        }
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, DateUtil.FORMAT_38_DATE_TIME));
        string.append("\n");
        return string.toString();
    }

}
