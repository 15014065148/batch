package com.eveb.saasops.batch.game.report.service;
import com.eveb.saasops.batch.game.report.domain.GameCode;
import com.eveb.saasops.batch.game.report.domain.RptBetDayModel;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import com.eveb.saasops.batch.game.report.mapper.RptBetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RptService {

    @Autowired
    private RptBetMapper rptBetMapper;

    public RptBetModel getRptBetRcdByID(Long id)
    {
        return rptBetMapper.getRptBetRcdByID(id);
    }

    public List<GameCode> getGameCodeByCodegroup(Integer codegroup)
    {
        return rptBetMapper.getGameCodeByCodegroup(codegroup);
    }

    public GameCode getGameCodeByCodegroupAndCodeID(Integer codegroup,String codeid)
    {
        return rptBetMapper.getGameCodeByCodegroupAndCodeID(codegroup,codeid);
    }

    public List<GameCode> getGameCodeList()
    {
        return rptBetMapper.getGameCodeList();
    }

    public Map getGameCodeMap(String codetype)
    {
        return rptBetMapper.getGameCodeMap(codetype);
    }

    public List<GameCode> getGameListByPlatForm(String platform,String catId)
    {
        return rptBetMapper.getGameListByPlatForm(platform,catId);
    }

    public int delRptBetDayByDate(String schemaPrex,String startday)
    {
        return rptBetMapper.delRptBetDayByDate(schemaPrex,startday);
    }

    public void insertBBINDayRptBet(String schemaPrex,RptBetDayModel rptBetModel) {
        rptBetMapper.insertBBINDayRptBet(schemaPrex, rptBetModel);
    }

    public void insertBbinDayRptBetList(String schemaPrex,List<RptBetDayModel> bets){ rptBetMapper.insertBBINDayRptBetList(schemaPrex,bets);}

    public List<TGmApi> getGameApiByApiName(String apiName,List<String> agyAcc){ return rptBetMapper.getGameApiByApiName(apiName,agyAcc);}

    public List<String> getSiteForeByApiId(Integer apiId){return rptBetMapper.getSiteForeByApiId(apiId);}

}
