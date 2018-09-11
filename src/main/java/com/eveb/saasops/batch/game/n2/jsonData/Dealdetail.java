package com.eveb.saasops.batch.game.n2.jsonData;

import java.util.List;

public class Dealdetail {

    private List<String> side;
    private List<String> type;
    private List<String> value;

    public void setSide(List<String> side) {
        this.side = side;
    }

    public List<String> getSide() {
        return side;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getType() {
        return type;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public List<String> getValue() {
        return value;
    }

}