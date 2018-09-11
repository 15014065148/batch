package com.eveb.saasops.batch.game.report.processor;

import com.eveb.saasops.batch.game.bbin.request.BbinRequest;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.game.report.constants.GameTypeEnum;
import com.eveb.saasops.batch.game.report.domain.*;
import com.eveb.saasops.batch.game.report.service.RptElasticRestService;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.DateConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 10:58 2017/12/16
 **/
@Slf4j
@Component
public class RptBetDayProcessor {
    @Autowired
    private BbinRequest bbinRequest;
    @Autowired
    private RptElasticRestService rptElasticService;
    @Autowired
    private RptService rptService;
    @Autowired
    private SysService sysService;

    @Async("rptAsyncExecutor")
    public void execute(String sitePrefix,String startDay) throws Exception {
        List<RptBetDayModel> dataList = getBBINBetDayRptList(startDay, sitePrefix);
        List<RptBetDayModel> addList=new ArrayList<>();
        String schemaPrex = new SchemaCode(sysService.getSchemaName(sitePrefix)).getSchemaCode();
        /**当天存在数据则先进行删除旧数据**/
        rptService.delRptBetDayByDate(schemaPrex, startDay);
        for (RptBetDayModel rpt : dataList) {
            rpt.setStartday(startDay);
            List gameidList = new ArrayList();
            /***只有视讯类型下才有小费***/
            if (GameTypeEnum.Enum_Live.getValue().equals(rpt.getGametype())) {
                //根据游戏分类获取该分类下的游戏集合
//                List<Map> haspList = (ArrayList) ((Map) ((Map) ApplicationConstant.Constant_GameCode_Map.get(PlatFromEnum.Enum_BBIN.getKey())).get(CodeTypeEnum.Enum_Game_Code.getKey())).get(BBINGameTypeEnum.Enum_Live.getKey());
//                for (Map map : haspList) {
//                    Map.Entry object = (Map.Entry) map.entrySet().iterator().next();
//                    gameidList.add(object.getKey());
//                }
                gameidList.add(rpt.getGametype());
                rpt.setTip(rptElasticService.aggsTip(rpt.getStartday(), gameidList));
            }
            addList.add(rpt);
        }
        if(!addList.isEmpty()&&addList.size()>0) {
            rptService.insertBbinDayRptBetList(schemaPrex, addList);
        }
    }

    /*****
     * 根据时间获取当天区间的统计
     * @param startDay("yyyy-MM-dd")
     * @return
     * @throws ParseException
     */
    public List<RptBetDayModel> getBBINBetDayRptList(String startDay, String sitePrefix) throws Exception {
        /***使用北京时间***/
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String[] dates= DateConvert.getAsiaInterval(sdf.parse(startDay));
        return rptElasticService.aggsRpt(dates[0], dates[1], sitePrefix);
    }
}
