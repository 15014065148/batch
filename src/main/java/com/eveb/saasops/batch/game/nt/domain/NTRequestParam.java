package com.eveb.saasops.batch.game.nt.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Configuration
@ConfigurationProperties(prefix = "nt")
public class NTRequestParam {

    @Value("${nt.brandid}")
    private Integer brandid;
    @Value("${nt.brandPassword}")
    private String brandPassword;
    @Value("${nt.uuid}")
    private String uuid;
    private Calendar startDate;
    private Calendar endDate;
    private String function;
    private String format;


    public NTRequestParam() {
    }

    @Override
    public String toString() {

        SimpleDateFormat data_sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        String rs = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://secondarywallet.connect.omega.com/\">  <soapenv:Header/>  <soapenv:Body>";
        rs += "<sec:" + function + "> ";
        if (brandid != null) {
            rs += "<brandId>" + brandid + "</brandId>";
        }
        if (brandPassword != null) {
            rs += "<brandPassword>" + brandPassword + "</brandPassword>";
        }
        if (uuid != null) {
            rs += "<uuid>" + uuid + "</uuid>";
        }
        if (startDate != null) {
            if (format != null && format.equals("yyyy-MM-dd")) {
                rs += "<startDate>" + data_sdf.format(startDate.getTime()) + "</startDate>";
            } else {
                rs += "<startTime>" + time_sdf.format(startDate.getTime()) + "</startTime>";
            }

        }
        if (endDate != null) {
            if (format != null && format.equals("yyyy-MM-dd")) {
                rs += "<endDate>" + data_sdf.format(endDate.getTime()) + "</endDate>";
            } else {
                rs += "<endTime>" + time_sdf.format(endDate.getTime()) + "</endTime>";
            }

        }
        rs += "</sec:" + function + "> ";
        rs += "</soapenv:Body> </soapenv:Evelope>";
        return rs;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public String getBrandPassword() {
        return brandPassword;
    }

    public void setBrandPassword(String brandPassword) {
        this.brandPassword = brandPassword;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
