package com.eveb.saasops.batch.game.report.scheduled;

import com.eveb.saasops.batch.game.report.constants.CodeTypeEnum;
import com.eveb.saasops.batch.game.report.constants.PlatFromEnum;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.game.report.domain.GameCode;
import com.eveb.saasops.batch.game.report.service.RptService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@JobHander(value="initGameCodeJobHander")
public class InitGameCode extends IJobHandler {

    @Autowired
    private RptService rptService;


    public ReturnT<String> execute(String... params) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        log.info("gameCodeMap Execution Start:" + sdf.format(new Date()));

        /**初始化游戏代码**/
        ApplicationConstant.CONSTANT_GAMECODE_MAP.clear();
        List<GameCode> list = rptService.getGameCodeList();
        for (PlatFromEnum e : PlatFromEnum.values()) {
            Map pfMap = new HashMap();
            for (CodeTypeEnum code : CodeTypeEnum.values()) {
                Map ctMap = new HashMap();
                Map groupMap = new HashMap();
                for (GameCode gc : list) {
                    if (code.getKey().equals(gc.getCodeType()) && e.getValue().equals(gc.getPlatform())) {
                        groupMap.put(gc.getCodeGroup(), null);
                    }
                }
                Iterator iterator = groupMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    Object key = entry.getKey();
                    List gclist = new ArrayList();
                    for (GameCode g : list) {
                        if (e.getValue().equals(g.getPlatform()) && code.getKey().equals(g.getCodeType()) && key.toString().equals(g.getCodeGroup())) {
                            Map gcMap = new HashMap();
                            /***去掉空格***/
                            gcMap.put(g.getCodeId()==null?g.getCodeId():g.getCodeId(), g.getCodeName());
                            gclist.add(gcMap);
                        }
                    }
                    ctMap.put(key, gclist);
                }
                pfMap.put(code.getKey(), ctMap);
            }
            ApplicationConstant.CONSTANT_GAMECODE_MAP.put(e.getValue(), pfMap);
        }
        XxlJobLogger.log(ApplicationConstant.CONSTANT_GAMECODE_MAP.toString());
//        log.debug(ApplicationConstant.Constant_GameCode_Map.toString());
        return ReturnT.SUCCESS;
    }
}
