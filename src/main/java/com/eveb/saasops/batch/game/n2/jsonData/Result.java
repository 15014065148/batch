package com.eveb.saasops.batch.game.n2.jsonData;

import java.util.List;

public class Result {

    private List<Gameinfo> gameinfo;

    public void setGameinfo(List<Gameinfo> gameinfo) {
        this.gameinfo = gameinfo;
    }

    public List<Gameinfo> getGameinfo() {
        return gameinfo;
    }

}