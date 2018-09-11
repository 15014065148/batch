package com.eveb.saasops.batch.game.bng.bean;

import com.eveb.saasops.batch.game.report.domain.BetLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public class BngItem extends BetLog {

    /**
     * "uid": "875dc949f3a34d7394d303a6ae63fd64",
     * "type": "transaction",
     * "bet": 200,
     * "round_started": true,
     * "rounds":[2105922],
     * "segment": "mobile",
     * "balance_after": 43627,
     * "freebet_id": null,
     * "c_at": "2018-04-07T09:50:55+00:00",
     * "win": 0,
     * "session": "5f401f8a9ede491ba8e2c0a09ee44df9",
     * "round_finished": true,
     * "player_id": "LBQIAN1983",
     * "game_name": "88_wild_dragon",
     * "currency": "CNY"
     * */
    private String uid;
    private String type;
    private Integer bet;
    private boolean round_started;
    private List<Integer> rounds;
    private String segment;
    private Integer balance_after;
    private String freebet_id;
    private String c_at;
    private Integer win;
    private String session;
    private String round_finished;
    private String player_id;
    private String game_name;
    private String currency;

    private String apiPrefix;
    /**前缀**/
    private String sitePrefix;

}
