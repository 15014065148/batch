package com.eveb.saasops.batch.game.bbin.domain;

import com.eveb.saasops.batch.game.report.domain.TGmApi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BBINRequestParameter implements Cloneable{

    private String rounddate;//日期ex：2012/03/21、2012‐03‐21 (gamekind=5，只可取7日内 的资料)
    private String startdate;//开始日期 ex：2012/03 / 21,2012-03-21（action = BetTime时 start_date与end_date需同一天）
    private String enddate;//结束日期 ex：2012/03 / 21,2012-03-21（action = BetTime时 start_date与end_date需同一天）
    private String date;//捕鱼action：BetTime、ModifiedTime 統一為不可跨日撈取
    private String starttime;//开始时间ex：00:00:00(BB体育无效)
    private String endtime;//结束时间ex：23:59:59(BB体育无效)
    private Integer gamekind;//游戏种类(1：BB体育、3：BB视讯、5：BB电子、12：BB彩票、 15：3D电子、99：BB小费
    private Integer subgamekind;//请详查附件五(gamekind=5时，值:1、2、3、5，预设为1)
    private String gametype="";//请详查附件二(gamekind=12时，需强制带入)
    private String action;//时间捞取依据 (BetTime:使用下注时间查询资讯 / ModifiedTime: 使用异动时间查询资讯)
    /**
     * BetRecord：下注记录
     * BetRecordByModifiedDate3：下注记录(注单变更时间)(不分体系、限定5分钟)(无法捞取7天前，被异动的资料)
     * WagersRecordBy30：BB捕鱼达人下注纪录（action = ModifiedTime时，捞取区间限定5分钟且无法捞取7天前，被异动的 资料）
     * WagersRecordBy38：捕鱼大师下注纪录(action=ModifiedTime时，捞取区间限定5分钟且无法捞取7天前，被异动的资料)
     * **/
    private String method;
    private String indexName;
    private String typeName;
    private String key;
    private int page=1;//查询页数
    private int pagelimit=500;//每页数量,若API为BetRecordByModifiedDate3、gamekind = 3或5、格式为JSON，查询资料时最大每页笔数全面限制为「10000」 笔
    private TGmApi api;

    /****
     * 初始化调用参数
     */
    public BBINRequestParameter() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
