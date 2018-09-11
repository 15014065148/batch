package com.eveb.saasops.batch.game.bng.request;

import com.eveb.saasops.batch.game.bng.bean.BngBetLogModel;
import com.eveb.saasops.batch.game.bng.bean.BngItem;
import com.eveb.saasops.batch.game.bng.bean.BngRequestParam;
import com.eveb.saasops.batch.sys.util.JsonUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BngRequest {

    public static final String GAME_SERVER_URL="https://box9.betsrv.com/eveb/";
    public static final String API_TOKEN="GWvGg8aWmQ9yUNY6MdbnYPyCa";
    public static final String GAME_SERVER_URL_GamePlayer="https://box7-stage.betsrv.com/eveb-stage/lobby.html?profile=profilename&wl=transfer&token=%s&lang=en&sound=1&theme=dark";
    public static final String GAME_SERVER_URL_FreeBet="https://box7-stage.betsrv.com/eveb-stage/api/v4/freebet/%s/";
    private static final Integer FETCH_SIZE = 1000;
    /**Transaction.list (交易清单)  包含免费游戏*/
    private static final String URL ="api/v4/transaction/list/";
    /**等待时常，避免任务发送太快**/
    private final int waitMillis=1000;
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;
    @Autowired
    private JsonUtil jsonUtil;

    /**
     *
     * @param bngRequestParam
     * @param fetchSate
     * @return
     */
    public List<BngItem> transactionList(BngRequestParam bngRequestParam, String fetchSate){
        Map<String,Object> map = new HashMap<>();
        map.put("api_token",bngRequestParam.getApi().getMd5Key());
        map.put("fetch_state",fetchSate == null?"":fetchSate);
        map.put("fetch_size",bngRequestParam.getPageSize());
        map.put("start_date",bngRequestParam.getStartDate());
        map.put("end_date",bngRequestParam.getEndDate());
        log.info("即将执行bng数据拉去 bng request {}", map.toString());
        String rs ="";
        try {
           rs=okHttpProxyUtils.postJson(initBngProcessor(),GAME_SERVER_URL+URL,map);
        } catch (Exception e) {
            log.info("url{} , params {} ", GAME_SERVER_URL+URL + map);
            e.printStackTrace();
        }
        BngBetLogModel bngBetLogModel =jsonUtil.fromJson(rs,BngBetLogModel.class);
        List<BngItem> items = bngBetLogModel.getItems();
        /**下一页*/
        if(bngBetLogModel.getItems().size() == bngRequestParam.getPageSize()){
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            items.addAll(transactionList(bngRequestParam,bngBetLogModel.getFetch_state()));
        }
        log.info("BNG 数据拉去结果  items{}", items.toString());
        return items;
    }



    public OkHttpClient initBngProcessor(){
        return okHttpProxyUtils.setProxys(new OkHttpClient.Builder()).build();
    }
}
