package com.eveb.saasops.batch.game.pgcb.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.pgcb.domain.*;
import com.eveb.saasops.batch.game.pgcb.request.PgcbDownloadFile;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: Jeff
 * @Description: 盘古彩播
 * @Date: 11:55 2018/08/27
 **/
@Slf4j
@Component
public class PgcbProcessor {
    @Autowired
    private RptService rptService;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private SysService sysService;

    @Async("pgcbAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            Integer lottery = lotteryProcess(JSON.parseObject(jobmodel.getParamater(), PgcbRequestParameterModel.class));//彩票
            Integer gift = giftProcess(JSON.parseObject(jobmodel.getParamater(), PgcbRequestParameterModel.class));//送礼
            Integer casino = casinoProcess(JSON.parseObject(jobmodel.getParamater(), PgcbRequestParameterModel.class));//电子游戏
            jobmodel.setCounts(gift + lottery + casino);
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    /**
     * 彩票
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public Integer lotteryProcess(PgcbRequestParameterModel parameter) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());

        List<PgcbLotteryBetLogModel> lotteryList = new ArrayList<PgcbLotteryBetLogModel>();
        List<String> dateList = PgcbDownloadFile.downFileByDate(parameter);//获取文件数据
        for (String jsonStr : dateList) {
            List<PgcbLotteryBetLogModel> lottery = JSON.parseArray(jsonStr, PgcbLotteryBetLogModel.class);
            lotteryList.addAll(lottery);
        }

        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (PgcbLotteryBetLogModel item : lotteryList) {
            counts++;
            item.setUsername(item.getUsername().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getUsername().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setUsername(item.getUsername().substring(fore.length(), item.getUsername().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(parameter.getApi().getApiName());
            insstr.append(toInsertString(item));
            rptList.add(PgcbRptBetProcessor.process(item));
        }
        rptElasticService.insertList(insstr.toString());
        /**因不存在异动单，所以进行插入和更新**/
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    /**
     * 礼物
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public Integer giftProcess(PgcbRequestParameterModel parameter) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());

        parameter.setRemotePath("gift/");
        List<PgcbGiftBetLogModel> giftList = new ArrayList<PgcbGiftBetLogModel>();
        List<String> dateList = PgcbDownloadFile.downFileByDate(parameter);
        for (String jsonStr : dateList) {
            List<PgcbGiftBetLogModel> gift = JSON.parseArray(jsonStr, PgcbGiftBetLogModel.class);
            giftList.addAll(gift);
        }

        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (PgcbGiftBetLogModel item : giftList) {
            counts++;
            item.setUsername(item.getUsername().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                if (item.getUsername().startsWith(fore)) {//如果存在前缀则进行截取
                    item.setSitePrefix(fore);
                    item.setUsername(item.getUsername().substring(fore.length(), item.getUsername().length()).toLowerCase());
                    break;
                }
            }
            item.setDownloadTime(new Date());
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
    public Integer casinoProcess(PgcbRequestParameterModel parameter) throws Exception {
        Integer counts = 0;
        /**获取所有的前缀，不区分线路**/
        List<String> prefixList = rptService.getSiteForeByApiId(parameter.getApi().getId());

        parameter.setRemotePath("casino/");
        List<PgcbCasinoBetLogModel> casinoList = new ArrayList<PgcbCasinoBetLogModel>();
        List<String> dateList = PgcbDownloadFile.downFileByDate(parameter);
        for (String jsonStr : dateList) {
            List<PgcbCasinoBetLogModel> gift = JSON.parseArray(jsonStr, PgcbCasinoBetLogModel.class);
            casinoList.addAll(gift);
        }

        StringBuffer insstr = new StringBuffer();
        List<RptBetModel> rptList = new ArrayList<>();
        for (PgcbCasinoBetLogModel item : casinoList) {
            counts++;
            item.setUsername(item.getUsername().toLowerCase());
            for (String fore : prefixList) {
                fore = fore.toLowerCase();
                //如果存在前缀则进行截取
                if (item.getUsername().startsWith(fore)) {
                    item.setSitePrefix(fore);
                    item.setUsername(item.getUsername().substring(fore.length(), item.getUsername().length()).toLowerCase());
                    break;
                }
            }
            item.setApiPrefix(parameter.getApi().getApiName());
            insstr.append(toInsertString(item));
            rptList.add(PgcbRptBetProcessor.casinoProcess(item));
        }
        rptElasticService.insertList(insstr.toString());
        /**因不存在异动单，所以进行插入和更新**/
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }


    /***
     * 转换成插入语句
     * @param object
     * @return
     */
    public String toInsertString(Object object) {
        StringBuffer string = new StringBuffer();
        if (object instanceof PgcbLotteryBetLogModel) {
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.PGCB_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.PGCB_TYPE_LOTTERY + "\", \"_id\" : \"" + ((PgcbLotteryBetLogModel) object).getOrder_number() + "\" }}");
        } else if (object.getClass() == PgcbCasinoBetLogModel.class) {
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.PGCB_INDEX + "_casino" + "\", \"_type\" : \"" + ElasticSearchConstant.PGCB_TYPE_CASINO + "\", \"_id\" : \"" + ((PgcbCasinoBetLogModel) object).getOrder_number() + "\" }}");
        } else {
            string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.PGCB_INDEX + "_gift" + "\", \"_type\" : \"" + ElasticSearchConstant.PGCB_TYPE_GIFT + "\", \"_id\" : \"" + ((PgcbGiftBetLogModel) object).getOrder_number() + "\" }}");
        }
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
