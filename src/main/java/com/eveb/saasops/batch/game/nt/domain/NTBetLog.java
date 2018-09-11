package com.eveb.saasops.batch.game.nt.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class NTBetLog extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private Long id;
    private String partyId;
    private String userId;
    private String gameInfoId;
    private String gameTranId;
    private String platformCode;
    private String platformTranId;
    private String gameId;
    private String tranType;
    private Date dateTime;
    private BigDecimal amount;
    private String currency;
    private BigDecimal balance;
    private String rollbackTranId;
    private String rollbackTranType;

    public NTBetLog() {
    }

    public NTBetLog(Long id, String partyId, String userId, String gameInfoId, String gameTranId, String platformCode, String platformTranId, String gameId, String tranType, Date dateTime, BigDecimal amount, String currency, BigDecimal balance, String rollbackTranId, String rollbackTranType) {
        this.id = id;
        this.partyId = partyId;
        this.userId = userId;
        this.gameInfoId = gameInfoId;
        this.gameTranId = gameTranId;
        this.platformCode = platformCode;
        this.platformTranId = platformTranId;
        this.gameId = gameId;
        this.tranType = tranType;
        this.dateTime = dateTime;
        this.amount = amount;
        this.currency = currency;
        this.balance = balance;
        this.rollbackTranId = rollbackTranId;
        this.rollbackTranType = rollbackTranType;
    }
}
