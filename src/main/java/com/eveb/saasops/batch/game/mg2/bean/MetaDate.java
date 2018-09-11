package com.eveb.saasops.batch.game.mg2.bean;

import lombok.Data;

import java.util.Map;

@Data
public class MetaDate {
    private String round_seq_id;
    private Map<String,Object> context;
    /*{
        "os": "UNKNOWN",
                "browser_type": "NONE",
                "device_type": "UNKNOWN",
                "browser_version": -1,
                "platform": "FLASH"
    }*/
    private String round_id;
    private Map<String,Object>  mg;
   /* {
        "action_id": "45343174219",
                "session_id": "67844812",
                "server_id": "3403",
                "game_id": "84"
    },*/
    private String   ext_item_id;
    private String  item_id;
}
