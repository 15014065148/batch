package com.eveb.saasops.batch.game.fg.domain;


import java.util.ArrayList;
import java.util.List;

/**
 * Fg游戏接口及类型
 */
public class FgGameTypeUtil {


    public static final String FG_SERVER_URL = "v2/agent/log_by_page/gt/";

    public static final String FG_TYPE_FISH = "fish";
    public static final String FG_SERVER_POKER = "poker";
    public static final String FG_SERVER_SLOT = "slot";
    public static final String FG_SERVER_SRUIT = "fruit";

    public static List<String> fgTypes() {
        List<String> results = new ArrayList<String>();
        results.add(FG_TYPE_FISH);
        results.add(FG_SERVER_POKER);
        results.add(FG_SERVER_SLOT);
        results.add(FG_SERVER_SRUIT);
        return results;
    }
}
