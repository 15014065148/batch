package com.eveb.saasops.batch.game.ttg.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.game.ttg.domain.TTGElectronicBetLog;
import com.eveb.saasops.batch.game.ttg.domain.TTGRequestParameter;
import com.eveb.saasops.batch.game.ttg.request.TTGRequest;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TTGGameProcessor {
    @Autowired
    private SysService sysService;
    @Autowired
    private RptService rptService;
    @Autowired
    private TTGRequest tTGRequest;
    @Autowired
    private RptElasticRestService rptElasticService;

    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        try {
            jobmodel.setTimes(jobmodel.getTimes() + 1);
            jobmodel.setFirstTime(new Date());
            jobmodel.setLastTime(new Date());
            jobmodel.setCounts(process(JSON.parseObject(jobmodel.getParamater(), TTGRequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(jobmodel);
            log.error(e.getMessage());
            throw e;
        }
        jobmodel.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(jobmodel);
    }

    public Integer process(TTGRequestParameter tTGRequestParameter) throws Exception {
        Integer counts = 0;
        StringBuffer insstr = new StringBuffer();
        List<TTGElectronicBetLog> tTGElectronicBetLogs = tTGRequest.getBetList(tTGRequestParameter);
        /**获取前缀api线路*/
        String apiPrefix = tTGRequestParameter.getApi().getAgyAcc().toLowerCase();
        /**获取会员前缀*/
        List<String> AccountPrefixList = getSiteForeByApiId(tTGRequestParameter);
        /**输赢报表实体封装*/
        List<RptBetModel> rptList = new ArrayList<>();
        /**截取会员前缀/线路前缀*/
        for (TTGElectronicBetLog item : tTGElectronicBetLogs) {
            counts++;
            /**分割会员前缀*/
            for (String prefix : AccountPrefixList) {
                String userName = item.getPlayerId().toLowerCase().trim();
                prefix = prefix.toLowerCase().trim();
                String userNamePrefix = userName.substring(0, prefix.length());
                if (userNamePrefix.equals(prefix)) {
                    item.setPlayerId(userName.replace(prefix, ""));
                    item.setSitePrefix(prefix);
                }
            }
            /**设置api线路前缀*/
            item.setApiPrefix(apiPrefix);
            /**输赢报表实体封装*/
            packageEntity(rptList, item);
            insstr.append(toInsertString(item));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        return counts;
    }

    /**
     * 获取会员前缀
     */
    public List<String> getSiteForeByApiId(TTGRequestParameter tTGRequestParameter) {
        List<String> List = rptService.getSiteForeByApiId(tTGRequestParameter.getApi().getId());
        List<String> prefixList = new ArrayList<>();
        List.forEach(e -> {
            prefixList.add(e.toLowerCase());
        });
        return prefixList;
    }

    /**
     * 输赢报表封装
     */
    private void packageEntity(List<RptBetModel> rptBetModels, TTGElectronicBetLog tTGElectronicBetLog) {
        RptBetModel rptBetModel = new RptBetModel();
        rptBetModel.setApiPrefix(tTGElectronicBetLog.getApiPrefix());//线路前缀
        rptBetModel.setSitePrefix(tTGElectronicBetLog.getSitePrefix());//会员前缀
        rptBetModel.setId(tTGElectronicBetLog.getTransactionId() + "");//注单唯一编号，平台方获取
        rptBetModel.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_TTG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), GameTypeEnum.Enum_SlotId.getKey(), tTGElectronicBetLog.getGame()));//游戏名称
        rptBetModel.setPlatform(PlatFromEnum.Enum_TTG.getValue());//游戏平台：PT、AG、BBIN TTG
        rptBetModel.setUserName(tTGElectronicBetLog.getPlayerId());//玩家用户名
        rptBetModel.setBet(tTGElectronicBetLog.getAmount());//投注
        rptBetModel.setValidBet(tTGElectronicBetLog.getAmount());//有效投注
        rptBetModel.setPayout(tTGElectronicBetLog.getAmount());//派彩
        setRptBetModelResult(rptBetModel, tTGElectronicBetLog.getTransactionSubType());//结果：投注标记为输、派彩标记为赢
        rptBetModel.setStatus("已结算");//状态：已结算、未结算
        rptBetModel.setBetTime(tTGElectronicBetLog.getTransactionDate());//投注时间
        rptBetModel.setPayoutTime(tTGElectronicBetLog.getTransactionDate());//派彩时间
        rptBetModel.setDownloadTime(DateUtil.orderDate(new Date()));//下载时间
        rptBetModel.setOrderDate(tTGElectronicBetLog.getTransactionDate());//账务时间
        rptBetModel.setCurrency(tTGElectronicBetLog.getCurrency());//币别
        rptBetModels.add(rptBetModel);
    }

    private void setRptBetModelResult(RptBetModel rptBetModel, String transactionSubType) {
        if (transactionSubType.equals("Wager")) {
            rptBetModel.setResult("输");
        } else {
            rptBetModel.setResult("赢");
        }
    }

    /***
     * 转换成插入或修改语句
     * @param tTGElectronicBetLog
     * @return
     */
    private String toInsertString(TTGElectronicBetLog tTGElectronicBetLog) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.TTG_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.TTG_TYPE + "\", \"_id\" : \"" + tTGElectronicBetLog.getTransactionId() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(tTGElectronicBetLog, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }
}
