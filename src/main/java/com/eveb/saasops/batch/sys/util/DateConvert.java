package com.eveb.saasops.batch.sys.util;

import com.eveb.saasops.batch.sys.constants.ApplicationConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间转换
 */
public class DateConvert {

    /****
     * 北京时间转美东时间
     * @param asiaDate
     * @return
     * @throws ParseException
     */
    public static Date convertAmericaDate(Date asiaDate) throws ParseException {
        if(asiaDate==null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(asiaDate);
        cal.add(Calendar.HOUR, -Math.abs(ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL));
        return cal.getTime();
    }

    /****
     * 美东时间转北京时间
     * @param americaDate
     * @return
     * @throws ParseException
     */
    public static Date convertAsiaDate(Date americaDate) throws ParseException {
        if(americaDate==null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(americaDate);
        cal.add(Calendar.HOUR, Math.abs(ApplicationConstant.CONSTANT_TIMEZONE_INTEVAL));
        return cal.getTime();
    }

    /****
     * 任意时间转北京时间
     * @param americaDate
     * @return
     * @throws ParseException
     */
    public static Date convertAsiaDate(Date americaDate,int hour) {
        if(americaDate==null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(americaDate);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    /***
     * 取美东时间区间
     * @param asiaDate 北京时间
     * @return
     */
    public static String[] getAmericaInterval(Date asiaDate) {
        String[] strings = new String[2];
        /***使用北京时间***/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(asiaDate);
        cal.add(Calendar.HOUR, 24);
        strings[0] = (sdf.format(asiaDate) + "T12:00:00.000Z");
        strings[1] = (sdf.format(cal.getTime()) + "T12:00:00.000Z");
        return strings;
    }

    /***
     * 取北京时间区间
     * @param asiaDate 北京时间
     * @return
     */
    public static String[] getAsiaInterval(Date asiaDate) {
        String[] strings = new String[2];
        /***使用北京时间***/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strings[0] = (sdf.format(asiaDate) + "T00:00:00.000Z");
        strings[1] = (sdf.format(asiaDate) + "T23:59:59.000Z");
        return strings;
    }
}
