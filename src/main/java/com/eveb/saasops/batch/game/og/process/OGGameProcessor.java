package com.eveb.saasops.batch.game.og.process;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.og.domain.BettingRecordByVendorLog;
import com.eveb.saasops.batch.game.og.domain.OGRequestParameter;
import com.eveb.saasops.batch.game.og.request.OGRequest;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OGGameProcessor {
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private SysService sysService;

    @Autowired
    private OGRequest ogRequest;

    @Autowired
    private RptElasticRestService rptElasticService;

    @Autowired
    private RptService rptService;

    public void executeJob(JobFailMessageModel model) {
        try {
            model.setTimes(model.getTimes() + 1);
            model.setFirstTime(new Date());
            model.setLastTime(new Date());
            model.setCounts(process(JSON.parseObject(model.getParamater(), OGRequestParameter.class)));
        } catch (Exception e) {
            sysService.saveOrUpdate(model);
            e.printStackTrace();
            log.error(e.getMessage());
        }
        model.setExecuteStatus(ApplicationConstant.CONSTANT_JOBEXCUTE_SUCCEED);
        sysService.saveOrUpdate(model);
    }

    public Integer process(OGRequestParameter oGRequestParameter) throws Exception {
        StringBuffer insstr=new StringBuffer();
        /**获取前缀api线路*/
        String apiPrefix = oGRequestParameter.getGmApi().getApiName();
        /**获取会员前缀*/
        List<String> AccountPrefixList = getSiteForeByApiId(oGRequestParameter);
        Integer counts = 0;
        /**输赢报表实体封装*/
        List<RptBetModel> rptList = new ArrayList<>();
        ogRequest.setCount(1);
        List<BettingRecordByVendorLog> list =new ArrayList<>();
        ogRequest.getBetList(oGRequestParameter,list);
        /**截取会员前缀/线路前缀*/
        for (BettingRecordByVendorLog item : list) {
            counts++;
            /**分割会员前缀*/
            for (String prefix : AccountPrefixList) {
                String userName = item.getUserName().toLowerCase().trim();
                prefix = prefix.toLowerCase().trim();
                String userNamePrefix=userName.substring(0,prefix.length());
                if (userNamePrefix.equals(prefix)) {
                    item.setUserName(userName.replace(prefix, ""));
                    item.setSitePrefix(prefix);
                }
            }
            /**设置api线路前缀*/
            item.setApiPrefix(apiPrefix);
            /**输赢报表实体封装*/
            packageEntity(rptList,item);
            insstr.append(toInsertString(item));
        }
        rptElasticService.insertList(insstr.toString());
        rptElasticService.insertOrUpdateList(rptList);
        log.info("=====>本次执行，线路：【 "+apiPrefix+" 】共拉取数据：【  "+list.size()+" 】条");
       // list.clear();
        return counts;
    }

    /**
     * 获取会员前缀
     */
    public List<String> getSiteForeByApiId(OGRequestParameter oGRequestParameter) {
        List<String> prefixList = rptService.getSiteForeByApiId(oGRequestParameter.getGmApi().getId());
        return prefixList;
    }

    /**
     * 输赢报表实体封装
     */
    public void packageEntity(List<RptBetModel> list, BettingRecordByVendorLog bettingRecordByVendorLog) throws Exception {
        RptBetModel rptBetModel = new RptBetModel();
        rptBetModel.setApiPrefix(bettingRecordByVendorLog.getApiPrefix());//线路前缀
        rptBetModel.setSitePrefix(bettingRecordByVendorLog.getSitePrefix());//会员前缀
        rptBetModel.setId(bettingRecordByVendorLog.getOrderNumber());//注单唯一编号，平台方获取
        rptBetModel.setGameName(ApplicationConstant.getCodeContent(PlatFromEnum.Enum_OG.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), GameTypeEnum.Enum_SportId.getKey(), bettingRecordByVendorLog.getGameNameID()+""));//游戏名称
        rptBetModel.setGameType(bettingRecordByVendorLog.getGameNameID()+"");//游戏类型：真人、老虎机
        rptBetModel.setPlatform(PlatFromEnum.Enum_OG.getValue());//游戏平台：PT、AG、BBIN
        rptBetModel.setUserName(bettingRecordByVendorLog.getUserName());//玩家用户名
        rptBetModel.setBet(bettingRecordByVendorLog.getBettingAmount());//投注
        rptBetModel.setBetType(bettingRecordByVendorLog.getGameBettingKind()+"");//投注类型
        rptBetModel.setTableNo(bettingRecordByVendorLog.getTableID()+"");//桌号
        rptBetModel.setSerialId(bettingRecordByVendorLog.getInning()+"");//局号
        rptBetModel.setValidBet(bettingRecordByVendorLog.getValidAmount());//有效投注
        rptBetModel.setPayout(bettingRecordByVendorLog.getWinLoseAmount());//派彩
        setRptBetModelResult(rptBetModel,bettingRecordByVendorLog.getResultType());//结果：输、赢
        rptBetModel.setStatus("已结算");//状态：已结算、未结算
        rptBetModel.setBetTime(bettingRecordByVendorLog.getAddTime());//投注时间
        rptBetModel.setPayoutTime(bettingRecordByVendorLog.getAddTime());//派彩时间
        rptBetModel.setDownloadTime(DateUtil.orderDate(new Date()));//下载时间
        rptBetModel.setOrderDate(bettingRecordByVendorLog.getAddTime());//账务时间
        //rptBetModel.setOrigin();//'1.行动装置下单：M 1‐1.ios手机：MI1 1‐2.ios平板：MI2 1‐3.Android手机：MA1 1‐4.Android平板：MA2 2.计算机下单：P'
        rptBetModel.setCurrency("RMB");//币别
        rptBetModel.setBalanceBefore(bettingRecordByVendorLog.getBalance().add(bettingRecordByVendorLog.getWinLoseAmount()));//下注前余额 有些平台无此值
        rptBetModel.setBalanceAfter(bettingRecordByVendorLog.getBalance());//派彩后余额 有些平台无此值
        list.add(rptBetModel);
    }

    /***
     * 转换成插入或修改语句
     * @param bettingRecordByVendorLog
     * @return
     */
    private String toInsertString(BettingRecordByVendorLog bettingRecordByVendorLog)
    {
        StringBuffer string = new StringBuffer();
        string.append("{ \"index\" : { \"_index\" : \""+ ElasticSearchConstant.OG_INDEX +"\", \"_type\" : \""+ElasticSearchConstant.OG_TYPE+"\", \"_id\" : \"" + bettingRecordByVendorLog.getProductID() + "\" }}");
        string.append("\n");
        string.append(JSON.toJSONStringWithDateFormat(bettingRecordByVendorLog, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        string.append("\n");
        return string.toString();
    }

    public void setRptBetModelResult(RptBetModel rptBetModel,Byte resultType){
        //游戏结果类型：1表示输，2表示赢3表示和，4表示无效
        switch (resultType){
            case 1:{
                rptBetModel.setResult("输");
                break;
            }
            case 2:{
                rptBetModel.setResult("赢");
                break;
            }
            case 3:{
                rptBetModel.setResult("和");
                break;
            }
            case 4:{
                rptBetModel.setResult("无效");
                break;
            }
        }
    }

    public Map getMaxVendorid(){
        try {
            return rptElasticService.getMaxVendorid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
