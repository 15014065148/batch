package com.eveb.saasops.batch.game.opus.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class OpusLiveBetLog extends BetLog {

    private String sitePrefix;//前缀
    private String apiPrefix;
    private Long transaction_id;
    private Date transaction_date_time;
    private String member_code;
    private String member_type;
    private String currency;
    private BigDecimal balance_start;
    private BigDecimal balance_end;
    private BigDecimal bet;
    private BigDecimal win;
    private String game_code;
    private String game_detail;
    private String bet_status;
    private String player_hand;
    private String game_result;
    private String game_category;
    private String vendor;
    private Integer DrawNumber;
    private String m88_studio;
    private Date stamp_date;
    private String bet_record_id;

}
