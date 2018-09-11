package com.eveb.saasops.batch.game.fg.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 2018/07/19 Jeff
 */
@Data
public class FgBetLog extends BetLog {
    /**
     * 全局唯一 key
     */
    private String id;
    /**
     * 类型 1->场景捕鱼账单.2->jp 奖账单.6->买鱼注单.14->激光炮.15->boss 赛功能.
     */
    private Integer type;
    /**
     * 场景 id
     */
    private Integer scene_id;
    /**
     * 总投注(包括子弹价值和买鱼消耗单位元)
     */
    private BigDecimal bullet_chips;
    /**
     * 子弹个数
     */
    private Integer bullet_count;
    /**
     * 总奖金(场景死掉的鱼的总价值和买鱼收益,
     * {包括幸运金)——}  括号中，文档已弃用
     * （单位元）
     */
    private BigDecimal fish_dead_chips;
    /**
     * {幸运金（打鱼场景单位元）}括号中，文档已弃用
     */
    private String fish_price;
    /**
     * 游戏 id
     */
    private Integer game_id;
    /**
     * 1->PC 2-> IOS 横 3->IOS 竖 4-> android 横，5->android 竖，6->其他横,7->其他竖
     */
    private Integer game_terminal;
    /**
     * 开始筹码(单位元)
     */
    private BigDecimal start_chips;
    /**
     * 结束筹码(单位元)
     */
    private BigDecimal end_chips;
    /**
     * 玩家赢得 jackpot 奖金(该奖金由 FG 出,不算入结算)
     */
    private BigDecimal trans_chips;
    /**
     * 开始时间
     */
    private String start_time;
    /**
     * 结束时间
     */
    private String end_time;
    /**
     * 用户名
     */
    private String nickname;
    /**
     * 代理商 id,
     */
    private Integer agent_uid;
    /**
     * 总社 id
     */
    private Integer top_agent_uid;
    /**
     * 总代理 id
     */
    private Integer total_agent_uid;
    /**
     * jackpot 贡献保留 4 位小数(当前记录下注的额度抽取进入
     * jackpot,一般抽取比例是千分三,单位元)
     */
    private BigDecimal jp_add;
    /**
     *(被捕获鱼的编号对应 FG 后台投资买鱼报表中的鱼编号) ,只有投资买鱼才有(type=6)字段
     */
    private Integer order_id;
    /**
     * (买鱼的时间) ,只有投资买鱼才有(type=6)字段
     */
    private String buy_time;
    /**
     *投资买鱼报表房间 id,只有投资买鱼才有(type=6)字段
     */
    private Integer room_id;
    /**
     * 投资买鱼房间倍率,只有投资买鱼才有(type=6)字段
     */
    private BigDecimal room_type;

}
