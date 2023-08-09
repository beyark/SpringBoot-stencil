package com.it.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static ch.qos.logback.core.util.OptionHelper.isEmpty;

/**
 * @create 2023-03-21
 */
public class DateUtil {

    /**
     * @description: 字符串格式转日期格式 format为null 默认是年月日格式
     * @date: 2023/6/26 15:07
     * @param: dateString 日期字符串,format 日期格式
     * @return: java.lang.String
     **/
    public static Date forDate(String dateString, String format) throws ParseException {
        if (isEmpty(format)) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateString);
        return date;
    }

    /**
     * @description: 日期格式转字符串格式 format为null 默认是年月日格式
     * @date: 2023/6/26 15:07
     * @param: date 日期,format 日期格式
     * @return: java.lang.String
     **/
    public static String forString(Date date, String format) {
        if (date == null) {
            return "";
        } else {
            if (isEmpty(format)) {
                format = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
    }

    /**
     * @description: 计算两个日期相差天数
     * @date: 2023/6/26 15:09
     * @param: bdate - smdate
     * @return: int
     **/
    public static int daysBetween(Date smdate, Date bdate) {
        long betweenDays = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            betweenDays = (time2 - time1) / (1000 * 3600 * 24);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * @description: 向前后推几天日期
     * @date: 2023/6/26 15:11
     * @param: qh: 1前，2后,num 天数,format 日期格式
     * @return: java.util.List<java.lang.String>
     **/
    public static String getDate(int qh, String num, String format) {
        if ((qh == 1 || qh == 2) && !isEmpty(num)) {
            if (isEmpty(format)) {
                format = "yyyy-MM-dd";
            }
            //定义规则
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            //创建Calendar实列
            Calendar calendar = Calendar.getInstance();
            //前
            if (qh == 1) {
                calendar.add(Calendar.DATE, -Integer.parseInt(num));
            }
            //后
            if (qh == 2) {
                calendar.add(Calendar.DATE, Integer.parseInt(num));
            }
            //获取到日期
            String futureTime = sdf.format(calendar.getTime());
            return futureTime;
        }
        return null;
    }
}


