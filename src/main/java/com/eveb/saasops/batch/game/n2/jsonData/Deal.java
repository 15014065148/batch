package com.eveb.saasops.batch.game.n2.jsonData;

import java.util.Date;
import java.util.List;

public class Deal {

    private Date enddate;
    private String code;
    private List<Dealdetails> dealdetails;
    private String id;
    private Date startdate;
    private List<Results> results;
    private String status;
    private List<Betinfo> betinfo;

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDealdetails(List<Dealdetails> dealdetails) {
        this.dealdetails = dealdetails;
    }

    public List<Dealdetails> getDealdetails() {
        return dealdetails;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setBetinfo(List<Betinfo> betinfo) {
        this.betinfo = betinfo;
    }

    public List<Betinfo> getBetinfo() {
        return betinfo;
    }

}