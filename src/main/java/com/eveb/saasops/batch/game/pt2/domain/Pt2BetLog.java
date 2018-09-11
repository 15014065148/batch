package com.eveb.saasops.batch.game.pt2.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class Pt2BetLog extends BetLog implements Serializable{

    private String sitePrefix;//前缀
    private String apiPrefix;
    private String roundId;
    private String brandId;
    private String playerCode;
    private String gameId;
    private String gameCode;
    private String device;
    private String currency;
    private BigDecimal win;
    private BigDecimal bet;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal totalEvents;
    private Date firstTs;
    private Date ts;
    private Boolean isTest;
    private Boolean finished;
    private BigDecimal revenue;
}
