package com.eveb.saasops.batch.game.ab.processor;

import com.eveb.saasops.batch.game.GameBetLogModel;
import com.eveb.saasops.batch.game.GameParameter;
import com.eveb.saasops.batch.game.Ibc.domain.TicketStatusEnum;
import com.eveb.saasops.batch.game.Processor;
import com.eveb.saasops.batch.game.ab.domain.AbModifyBetLogModel;
import com.eveb.saasops.batch.game.ab.domain.AbRequestParameter;
import com.eveb.saasops.batch.game.ab.request.AbModifyRequest;
import com.eveb.saasops.batch.game.report.constants.BBINGameTypeEnum;
import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.GameCodeConstants;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AbModifyProcessor extends Processor<AbRequestParameter, AbModifyBetLogModel> {

    @Autowired
    public AbModifyProcessor() {
        super.index = ElasticSearchConstant.AB_INDEX;
        super.type = ElasticSearchConstant.AB_TYPE;
    }

    protected List<AbModifyBetLogModel> getRequest(GameParameter<Map<String, Object>> parameter) {
        AbModifyRequest request = (AbModifyRequest) SpringContextUtils.getBean("AbModifyRequest");
        List<AbModifyBetLogModel> list = new LinkedList<>();
        try {
            list = request.getBetList(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected GameBetLogModel<AbModifyBetLogModel> betHandler(GameBetLogModel<AbModifyBetLogModel> model, List<String> prefixList) {
        AbModifyBetLogModel abBetLogModel = model.getBetLogModel();
        if (abBetLogModel.getState() == 0) {
            return null;
        }
        for (String prefix : prefixList) {
            if (abBetLogModel.getClient().startsWith(prefix)) {
                model.setSitePrefix(prefix);
                abBetLogModel.setClient(abBetLogModel.getClient().replace(prefix, ""));
                model.setBetLogModel(abBetLogModel);
                super.id = String.valueOf(abBetLogModel.getBetNum());
            }
        }
        return model;
    }

    @Override
    protected RptBetModel rptbetHandler(GameBetLogModel<AbModifyBetLogModel> model) {
        AbModifyBetLogModel abBetLogModel = model.getBetLogModel();
        if (abBetLogModel.getState() == 0) {
            return null;
        }
        String gameName = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AB.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), String.valueOf(abBetLogModel.getGameType()));
        String setBetType = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AB.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), BBINGameTypeEnum.Enum_Slot.getKey(), String.valueOf(abBetLogModel.getBetType()));
        if (gameName.equals("")) {
            gameName = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AB.getValue(), CodeTypeEnum.Enum_Game_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), String.valueOf(abBetLogModel.getGameType()));
            setBetType = ApplicationConstant.getCodeContent(PlatFromEnum.Enum_AB.getValue(), CodeTypeEnum.Enum_Wager_Code.getKey(), BBINGameTypeEnum.Enum_Live.getKey(), String.valueOf(abBetLogModel.getBetType()));
        }
        RptBetModel rptBetModel = new RptBetModel();
        rptBetModel.setId(String.valueOf(abBetLogModel.getBetNum()));
        rptBetModel.setApiPrefix(model.getApiPrefix());
        rptBetModel.setSitePrefix(model.getSitePrefix());
        rptBetModel.setWebsite(model.getApiPrefix());
        rptBetModel.setRoundNo(abBetLogModel.getGameRoundId());
        rptBetModel.setPlatform(PlatFromEnum.Enum_AB.getValue());
        rptBetModel.setGameName(gameName);
        rptBetModel.setCurrency(abBetLogModel.getCurrency());
        rptBetModel.setBetTime(getTime(abBetLogModel.getBetTime()));
        rptBetModel.setBet(abBetLogModel.getBetAmount());
        rptBetModel.setValidBet(abBetLogModel.getBetAmount());
        rptBetModel.setStatus(GameCodeConstants.CONSTANT_ISPAID);
        rptBetModel.setBetType(setBetType);
        rptBetModel.setPayoutTime(getTime(abBetLogModel.getBetTime()));
        rptBetModel.setStartTime(getTime(abBetLogModel.getGameRoundStartTime()));
        rptBetModel.setTableNo(abBetLogModel.getTableName());
        rptBetModel.setPayout(abBetLogModel.getWinOrLoss());
        rptBetModel.setResult(abBetLogModel.getWinOrLoss().compareTo(BigDecimal.ZERO) > 0 ? TicketStatusEnum.Enum_Won.getValue() : TicketStatusEnum.Enum_Lose.getValue());
        rptBetModel.setDownloadTime(new Date());
        return rptBetModel;
    }

    /**
     * 子类重写异步运行
     *
     * @param jobmodel
     * @throws Exception
     */
    @Async(value = "abProgressor")
    public void executeJob(JobFailMessageModel jobmodel) throws Exception {
        super.executeJob(jobmodel);
    }

    public static String getTime(Date time, String parten) {
        SimpleDateFormat sdf = new SimpleDateFormat(parten);
        return sdf.format(time);
    }

    public static Date getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            log.debug("时间格式化错误 time{}" + time);
            e.printStackTrace();
        }
        return null;
    }
}
