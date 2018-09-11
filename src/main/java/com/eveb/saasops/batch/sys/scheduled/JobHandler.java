package com.eveb.saasops.batch.sys.scheduled;

import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.Getopt;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JobHandler extends IJobHandler {

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static int indexPara = 0;
    public final static int indexApi = 1;
    public final static int indexDate = 2;
    public final static int indexInteval = 3;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        return null;
    }

    /**
     * 参数清洗，校验
     */
    protected Object[] prepareArguments(String params[]) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        //如果不传则初始化默认
        if (params == null) {
            return new Object[]{null, null, calendar.getTime(), ApplicationConstant.CONSTANT_INTEVAL};
        }
        Getopt g = new Getopt("IJobHandler", params, "p::a::d::n::");
        List<String> apiList = new ArrayList<>();
        String gamePara = null;
        Date endDate = new Date();
        int interval = ApplicationConstant.CONSTANT_INTEVAL;
        int c = 0;
        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 'p':
                    gamePara = g.getOptarg().trim();
                    break;
                case 'a':
                    if (g.getOptarg() == null)
                        apiList = null;
                    Collections.addAll(apiList, g.getOptarg().trim().split(" "));
                    break;
                case 'd':
                    endDate = g.getOptarg() == null ? calendar.getTime() : dateFormat.parse(g.getOptarg().trim());//如果传入的时间为空，则取当前时间
                    break;
                case 'n':
                    interval = g.getOptarg() == null ? ApplicationConstant.CONSTANT_INTEVAL : Integer.parseInt(g.getOptarg().trim());
                    break;
            }
        }
        return new Object[]{gamePara, apiList, endDate, interval};
    }

    /****
     * 切割时间
     * @param startDate
     * @param endDate
     * @param inteval 可以获取注单的时间长度
     * @return
     */
    public List<String[]> cutDate(Date startDate, Date endDate, int inteval) {
        List<String[]> dates = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        int between = (int) (Math.abs(startDate.getTime() - endDate.getTime()) / 1000 / 60);//除以1000/60是为了转换成分
        addDates(inteval, dates, startCal, endDate, between);
        return dates;
    }

    /****
     * 切割时间
     * @param intevallengs 获取注单的时间长度
     * @return
     */
    public List<String[]> cutDate(int intevallengs, Object paramsStrs[]) {
        List<String[]> dates = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();

        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        /**使用系统定义的五分钟延迟时间**/
        startcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_INTEVAL);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -ApplicationConstant.CONSTANT_INTEVAL);
        Date startDate = startcal.getTime();
        Date endDate = endcal.getTime();
        startCal.setTime(startDate);
        int between = (int) (Math.abs(startDate.getTime() - endDate.getTime()) / 1000 / 60);//除以1000/60是为了转换成分
        addDates(intevallengs, dates, startCal, endDate, between);
        return dates;
    }

    private void addDates(int intevallengs, List<String[]> dates, Calendar startCal, Date endDate, double between) {
        for (int i = 1; i <= Math.ceil(between / intevallengs); i++) {
            Date startdate = startCal.getTime();
            String start = dateFormat(startdate);
            startCal.add(Calendar.MINUTE, intevallengs);
            String end = dateFormat(startCal.getTime());
            if (i == Math.ceil(between / intevallengs)) {
                end = dateFormat(endDate);
            }
            dates.add(new String[]{start, end});
        }
    }

    /****
     * 切割时间
     * @param intevallengs 获取注单的时间长度
     * @param delay 获取注单的延迟时间,如果使用美东时间可加上12 * 60
     * @return
     */
    public List<String[]> cutDate(int intevallengs, int delay, Object paramsStrs[]) {
        List<String[]> dates = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();

        Date date = (Date) paramsStrs[indexDate];
        int inteval = (int) paramsStrs[indexInteval];
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date);
        /**使用自定义的延迟时间**/
        startcal.add(Calendar.MINUTE, -delay);
        startcal.add(Calendar.MINUTE, -inteval);
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        endcal.add(Calendar.MINUTE, -delay);
        Date startDate = startcal.getTime();
        Date endDate = endcal.getTime();
        startCal.setTime(startDate);
        int between = (int) (Math.abs(startDate.getTime() - endDate.getTime()) / 1000 / 60);//除以1000/60是为了转换成分
        addDates(intevallengs, dates, startCal, endDate, between);
        return dates;
    }

    public String dateFormat(Date date) {
        return dateFormat.format(date);
    }
}
