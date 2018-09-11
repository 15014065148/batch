package com.eveb.saasops.batch.sys.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期时间工具类
 */
@Slf4j
public class DateUtil {

    private static final int step = 3;
    public static final String MATCH_TEMPLATE = "yyyy/MM/dd HH:mm:ss:SSS";
    public static final String FORMAT_25_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    private static final String PATTERN_TEMPLATE = "0000/00/00 00:00:00:000";
    public static final String FORMAT_22_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_28_DATE_TIME = "MM/dd/yyyy HH:mm:ss";
    public static final String FORMAT_18_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_12_DATE_TIME = "yyyy-MM-dd HH";
    public static final String FORMAT_10_DATE = "yyyy-MM-dd";
    public static final String FORMAT_8_DATE = "yyyyMMdd";

    public static final String FORMAT_38_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static String suffix = ".000Z";
    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };
    /**
     * 上次订单时间
     */
    private static Long lastOrderNumber = new Date().getTime();
    /**
     * 当前订单时间
     */
    private static Long currentOrderNum;

    /**
     * 获取SimpleDateFormat实例
     *
     * @param pattern 模式串
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat format = local.get();
        format.applyPattern(pattern);
        return format;
    }

    /**
     * 获取本周 周一的日期
     *
     * @param pattern
     * @return
     */
    public static String getMonday(String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern, Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sf.format(calendar.getTime());
    }

    /**
     * 获取本周 周日的日期
     *
     * @param pattern
     * @return
     */
    public static String getWeek(String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern, Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sf.format(calendar.getTime());
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(today);
    }

    /**
     * 将时间戳转换为时间
     *
     * @param lt      时间戳
     * @param pattern 模式串
     */
    public static String stampToDate(Long lt, String pattern) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取表示当前时间的字符串
     *
     * @param pattern 模式串
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取表示当前美东时间
     *
     * @return
     */
    public static String getAmericaDate(String pattern, Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        sf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return sf.format(date);
    }

    /**
     * 日期时间格式化, 自动匹配格式化模式串
     *
     * @param date Date
     * @return
     */
    public static String format(Date date) {
        SimpleDateFormat format = getSimpleDateFormat(MATCH_TEMPLATE);
        String dateStr = format.format(date);
        String ms = dateStr.substring(dateStr.length() - step);
        if (Short.parseShort(ms) == 0) {
            dateStr = dateStr.substring(0, dateStr.length() - step - 1);
        } else {
            return dateStr;
        }
        String time = dateStr.substring(dateStr.length() - step * 3 + 1);
        time = time.replace(":", "");
        if (Integer.parseInt(time) == 0) {
            dateStr = dateStr.substring(0, dateStr.length() - step * 3);
        } else {
            return dateStr;
        }
        return dateStr;
    }

    /**
     * 日期时间格式化
     *
     * @param date    Date
     * @param pattern 模式串
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = getSimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 日期时间格式化 特定获取有效投注
     *
     * @param date
     * @return
     */
    public static String formatEsDate(String date) {
        SimpleDateFormat format = getSimpleDateFormat(FORMAT_18_DATE_TIME);
        String da = format.format(parse(date, FORMAT_18_DATE_TIME));
        return da.replace(" ", "T") + suffix;
    }

    public static Date parse(String date, String pattern) {
        try {
            SimpleDateFormat format = getSimpleDateFormat(pattern);
            return format.parse(date);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取唯一不重复的订单号
     *
     * @param date
     * @return
     */
    public static synchronized Date orderDate(Date date) {
        currentOrderNum = date.getTime();
        while (currentOrderNum <= lastOrderNumber) {
            currentOrderNum = new Date().getTime();
        }
        lastOrderNumber = currentOrderNum;
        //log.info("订单下载时间戳 {}",currentOrderNum);
        return new Date(currentOrderNum);
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = FORMAT_18_DATE_TIME;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取期间日期 (天)
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> listDate = new ArrayList<Date>();
        listDate.add(beginDate);// 把开始时间加入集合
        if (!format(beginDate, FORMAT_8_DATE).equals(format(endDate, FORMAT_8_DATE))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);// 使用给定的 Date 设置此 Calendar 的时间
            boolean continueBoolean = true;
            while (continueBoolean) {
                cal.add(Calendar.DAY_OF_MONTH, 1);// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                // 测试此日期是否在指定日期之后
                if (endDate.after(cal.getTime())) {
                    listDate.add(cal.getTime());
                } else {
                    break;
                }
            }
            listDate.add(endDate);// 把结束时间加入集合
        }
        return listDate;
    }

    /**
     * 添加小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addDateHour(Date date, int hour) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        return date;

    }

    /**
     * 添加天
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDateDay(Date date, int day) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, day);// 24小时制
        date = cal.getTime();
        return date;
    }

    public static void main(String[] args) throws Exception {
        Date dBegin = parse("2018-08-07 00:00:00", FORMAT_18_DATE_TIME);

        System.out.println(format(DateConvert.convertAsiaDate(dBegin),FORMAT_18_DATE_TIME));

    }
}