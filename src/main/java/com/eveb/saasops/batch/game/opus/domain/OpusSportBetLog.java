package com.eveb.saasops.batch.game.opus.domain;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class OpusSportBetLog extends BetLog {

   private String sitePrefix;//前缀
   private String apiPrefix;
   private String status_code;
   private String status_text;
   private String trans_count;
   private String datetime;
   private Long trans_id;
   private String member_id;
   private String operator_id;
   private String site_code;
   private Long league_id;
   private Long home_id;
   private Long away_id;
   private String match_datetime;
   private Integer bet_type;
   private Integer parlay_ref_no;
   private BigDecimal odds;
   private String currency;
   private BigDecimal stake;
   private BigDecimal winlost_amount;
   private String transaction_time;
   private String ticket_status;
   private Integer version_key;
   private String winlost_datetime;
   private Integer odds_type;
   private Integer sports_type;
   private String bet_team;
   private BigDecimal home_hdp;
   private BigDecimal away_hdp;
   private BigDecimal match_id;
   private Integer is_live;
   private Integer home_score;
   private Integer away_score;
   private String choicecode;
   private String choicename;
   private String txn_type;
   private String last_update;
   private String leaguename;
   private String homename;
   private String awayname;
   private String sportname;
   private String oddsname;
   private String bettypename;
   private String winlost_status;
}
