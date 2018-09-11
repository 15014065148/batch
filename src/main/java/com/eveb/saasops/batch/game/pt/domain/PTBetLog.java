package com.eveb.saasops.batch.game.pt.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PTBetLog extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private String playername;//用户名
    private String windowcode;//窗口号
    private String gameid;//游戏id
    private Long gamecode;//游戏局号,唯一ID
    private String gametype;//游戏类型
    private String gamename;//游戏名称
    private String sessionid;//期号
    private BigDecimal bet;//下注额
    private BigDecimal win;//赢
    private BigDecimal progressivebet;//奖池投注
    private BigDecimal progressivewin;//奖池赢得
    private BigDecimal balance;//余额
    private BigDecimal currentbet;//当局投注
    private Date gamedate;//游戏时间
    private String info;//详细信息
    private String livenetwork;//现场分类
    private String rnum;//类型
    private String cplatform;//投注设备


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PTBetLog ptBetLog = (PTBetLog) o;

        if (playername != null ? !playername.equals(ptBetLog.playername) : ptBetLog.playername != null) return false;
        if (windowcode != null ? !windowcode.equals(ptBetLog.windowcode) : ptBetLog.windowcode != null) return false;
        if (gameid != null ? !gameid.equals(ptBetLog.gameid) : ptBetLog.gameid != null) return false;
        if (gamecode != null ? !gamecode.equals(ptBetLog.gamecode) : ptBetLog.gamecode != null) return false;
        if (gametype != null ? !gametype.equals(ptBetLog.gametype) : ptBetLog.gametype != null) return false;
        if (gamename != null ? !gamename.equals(ptBetLog.gamename) : ptBetLog.gamename != null) return false;
        if (sessionid != null ? !sessionid.equals(ptBetLog.sessionid) : ptBetLog.sessionid != null) return false;
        if (bet != null ? bet.compareTo(ptBetLog.bet) != 0 : ptBetLog.bet != null) return false;
        if (win != null ? win.compareTo(ptBetLog.win) != 0 : ptBetLog.win != null) return false;
        if (progressivebet != null ? progressivebet.compareTo(ptBetLog.progressivebet) != 0 : ptBetLog.progressivebet != null)
            return false;
        if (progressivewin != null ? progressivewin.compareTo(ptBetLog.progressivewin) != 0 : ptBetLog.progressivewin != null)
            return false;
        if (balance != null ? balance.compareTo(ptBetLog.balance) != 0 : ptBetLog.balance != null) return false;
        if (currentbet != null ? currentbet.compareTo(ptBetLog.currentbet) != 0 : ptBetLog.currentbet != null)
            return false;
        if (gamedate != null ? !gamedate.equals(ptBetLog.gamedate) : ptBetLog.gamedate != null) return false;
        if (info != null ? !info.equals(ptBetLog.info) : ptBetLog.info != null) return false;
        if (livenetwork != null ? !livenetwork.equals(ptBetLog.livenetwork) : ptBetLog.livenetwork != null)
            return false;
        return rnum != null ? rnum.equals(ptBetLog.rnum) : ptBetLog.rnum == null;
    }

    @Override
    public int hashCode() {
        int result = playername != null ? playername.hashCode() : 0;
        result = 31 * result + (windowcode != null ? windowcode.hashCode() : 0);
        result = 31 * result + (gameid != null ? gameid.hashCode() : 0);
        result = 31 * result + (gamecode != null ? gamecode.hashCode() : 0);
        result = 31 * result + (gametype != null ? gametype.hashCode() : 0);
        result = 31 * result + (gamename != null ? gamename.hashCode() : 0);
        result = 31 * result + (sessionid != null ? sessionid.hashCode() : 0);
        result = 31 * result + (bet != null ? bet.hashCode() : 0);
        result = 31 * result + (win != null ? win.hashCode() : 0);
        result = 31 * result + (progressivebet != null ? progressivebet.hashCode() : 0);
        result = 31 * result + (progressivewin != null ? progressivewin.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (currentbet != null ? currentbet.hashCode() : 0);
        result = 31 * result + (gamedate != null ? gamedate.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (livenetwork != null ? livenetwork.hashCode() : 0);
        result = 31 * result + (rnum != null ? rnum.hashCode() : 0);
        return result;
    }

}
