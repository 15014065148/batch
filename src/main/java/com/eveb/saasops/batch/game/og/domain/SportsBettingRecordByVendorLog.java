package com.eveb.saasops.batch.game.og.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

/**
 *体育投注记录
 * */
@Getter
@Setter
public class SportsBettingRecordByVendorLog extends BetLog {

    private String ballid;//场次id号
    private String balltime;//结算时间
    private String curpl;//赔率
    private String ds;//单双
    private String dxc;//大小个数
    private String isbk;//4体育,6电子游戏,7彩票遊戲
    private String iscancel;//是否取消
    private String isdanger;//是否危险球
    private String isjs;//是否结算,1表示结算,0表示未结算
    private String lose;//输的钱
    private String matchid;//联赛id号
    private String moneyrate;//会员使用的货币比率
    private String orderid;//注单号
    private String recard;//红牌
    private String result;//赛事结果
    private String rowguid;//唯一性id号，自动生成，orderid为每个平台的id号，多平台可能不唯一
    private String rqc;//让球个数
    private String rqteam;//让球队伍,比如主队让球H,客对让球A
    private String sportid;//下注球类id号
    private String tballtime;//走地时间
    private String thisdate;//结算日期
    private String truewin;//赢半，赢，输半，输，和那些标志
    private String tzip;//投注ip,IBC不提供，为空就行
    private String tzmoney;//下注金额
    private String tzteam;//下注队伍，比如主队H,客队A
    private String tztype;//下注类别
    private String updatetime;//下注时间
    private String username;//会员名
    private String win;//赢的钱
    private String zdbf;//走地比分
    private String content;//下注中文简体内容,需要用UrlDecode解码
    private String vendorid;//注单更新顺序号
    private String validamount;//有效投注额


}
