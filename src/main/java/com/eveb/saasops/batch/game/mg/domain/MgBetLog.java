package com.eveb.saasops.batch.game.mg.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class MgBetLog extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private String key;//The key of the HOR Transactions report. (Internal usage only)
    private Long colId;//The unique key of the record. (Internal ID)
    private String horNeKey;//The HOR network ID
    private String mbrNeKey;//The member network ID.
    private String betTransKey;// Mg2BetLogModel unique ID in our system
    private String gameKey;//The unique ID of the game in our system
    private String type;// Mg2BetLogModel type. [Bet -> "mgsaspibet", Win -> "mgsapiwin", Refund -> "mgsapirefund"]
    private String product;// Product type. Always equal "casino"
    private Date transactionTimestampDate;//Mg2BetLogModel occur time, always based on UTC timezone j
    private BigDecimal amount;// Mg2BetLogModel amount. (Always a positive figures)
    private String mbrCode;// Member network code.
    private String mbrUsername;// Member login name.
    private String mbrAlias;//Exist only if Poker product type. Not applicable here
    private String gameCasinoId;// MG external game name
    private String gamePokerId;// Exist only if Poker product type. Not applicable here
    private String gamePokerType;//Exist only if Poker product type. Not applicable here.
    private String refKey;// Refund transaction key. Exist only if a refund case, this shows which transaction (map by betTransKey) target to be refund
    private String refType;//The type of the transaction target to be refund
    private BigDecimal afterTxWalletAmount;// Credit balance after the transaction
    private Long mgsGameId;// MG given game id. One game can have multiple action.
    private Long mgsActionId;//MG given action id. Each bet/win/refund treat as an individual action
    private BigDecimal clrngAmnt;// The progressive contribution/payout amount
    private String gamePlatformType;
}
