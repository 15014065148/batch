package com.eveb.saasops.batch.game.gns.model;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import java.util.Date;

@Data
public class GnsBetLogModel extends BetLog {

    private String partner_data;
    private String user_id;
    private String game_id;
    private String causality;
    private String currency;
    private Long total_bet;
    private Long Balance;
    private Long total_won;
    private Date timestamp;
    private String merchantcode;
    private String device;
    private String user_type;
    private String roundid;
    private String jp_id;
    private Long jpcontrib;
}
