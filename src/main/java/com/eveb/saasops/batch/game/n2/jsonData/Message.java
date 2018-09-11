package com.eveb.saasops.batch.game.n2.jsonData;

import java.util.List;

public class Message {

    private List<Result> result;
    private List<String> status;

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<String> getStatus() {
        return status;
    }

}