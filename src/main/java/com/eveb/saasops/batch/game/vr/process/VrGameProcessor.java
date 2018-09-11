package com.eveb.saasops.batch.game.vr.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.gns.model.IParameterModel;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.game.vr.domain.BetRecord;
import com.eveb.saasops.batch.game.vr.domain.VrLotteryBetLog;
import com.eveb.saasops.batch.game.vr.request.VrRequest;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.processor.IProcessor;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Order(1)
public class VrGameProcessor extends IProcessor {

    @Autowired
    private VrRequest vrRequest;
    @Autowired
    private RptService rptService;

    @Override
    @Async("vrAsyncExecutor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        executeJobs(jobmodel);
    }

    @Override
    public int processBet(String para) throws Exception {
        Integer counts = 0;
        StringBuffer insstr = new StringBuffer();
        IParameterModel iParameterModel = JSON.parseObject(para, IParameterModel.class);
        VrLotteryBetLog vrLotteryBetLog = new VrLotteryBetLog();
        vrRequest.getBetList(iParameterModel, vrLotteryBetLog);

        List<BetRecord> betRecords = vrLotteryBetLog.getBetRecords();
        /**获取前缀api线路*/
        String apiPrefix = iParameterModel.getApi().getWebName().toLowerCase();
        /**获取会员前缀*/
        List<String> AccountPrefixList = getSiteForeByApiId(iParameterModel);
        /**输赢报表实体封装*/
        List<RptBetModel> rptList = new ArrayList<>();
        /**截取会员前缀/线路前缀*/
        for (BetRecord item : betRecords) {
            counts++;
            /**分割会员前缀*/
            for (String prefix : AccountPrefixList) {
                String userName = item.getPlayerName().toLowerCase().trim();
                prefix = prefix.toLowerCase().trim();
                String userNamePrefix = userName.substring(0, prefix.length());
                if (userNamePrefix.equals(prefix)) {
                    item.setPlayerName(userName.replace(prefix, ""));
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
    public List<String> getSiteForeByApiId(IParameterModel iParameterModel) {
        List<String> List = rptService.getSiteForeByApiId(iParameterModel.getApi().getId());
        List<String> prefixList = new ArrayList<>();
        List.forEach(e -> {
            prefixList.add(e.toLowerCase());
        });
        return prefixList;
    }

    /**
     * 输赢报表实体封装
     */
    private void packageEntity(List<RptBetModel> rptList, BetRecord item) throws Exception {
        RptBetModel rptBetModel = new RptBetModel();
        rptBetModel.setId(item.getSerialNumber());//投注订单号
        rptBetModel.setApiPrefix(item.getApiPrefix());//线路名
        rptBetModel.setSitePrefix(item.getSitePrefix());//站点/会员名前缀
        rptBetModel.setGameName(item.getChannelName());//游戏名称
        rptBetModel.setPlatform(PlatFromEnum.Enum_VR.getKey());//平台名称
        rptBetModel.setUserName(item.getPlayerName());//玩家名称
        rptBetModel.setBet(item.getCost());//投注金额
        rptBetModel.setBetType(item.getBetTypeName());//投注种类名称
        rptBetModel.setValidBet(item.getCost());//投注金额
        rptBetModel.setPayout(item.getPlayerPrize());//玩家中奖金额
        rptBetModel.setResult(getResult(item.getState()));//投注结果:(0: 未颁奖 , 1: 撤单 , 2: 未中奖 , 3: 中奖 )
        rptBetModel.setStatus((item.getState() == 0) ? "未结算" : "已结算");//状态
        rptBetModel.setBetTime(item.getCreateTime());//下注时间
        rptBetModel.setStartTime(item.getUpdateTime());//更新时间
        rptBetModel.setPayoutTime(item.getUpdateTime());//更新时间
        rptBetModel.setDownloadTime(DateUtil.orderDate(new Date()));//下载时间
        rptBetModel.setCurrency("CNY");//币种
        rptBetModel.setRoundNo(item.getIssueNumber());//期号
        rptList.add(rptBetModel);
    }

    public String toInsertString(BetRecord betRecord) {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \"" + ElasticSearchConstant.VR_INDEX + "\", \"_type\" : \"" + ElasticSearchConstant.VR_TYPE + "\", \"_id\" : \"" + betRecord.getSerialNumber() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(betRecord, ApplicationConstant.CONSTANT_DATEFORMAT));
        string.append("\n");
        return string.toString();
    }

    private String getResult(Integer state) {
        switch (state) {
            case 1:
                return "未颁奖";
            case 2:
                return "撤单";
            case 3:
                return "输";
            case 4:
                return "赢";
        }
        return null;
    }

}
