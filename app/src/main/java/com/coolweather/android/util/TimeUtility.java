package com.coolweather.android.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ld-1 on 2018/2/19.
 */

public class TimeUtility {

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     *获取距现在某一小时的时刻
     * @param hour hour=-1为上一个小时，hour=1为下一个小时
     * @return
     */
    public static String getLongTime(int hour){
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int h; // 需要更改的小时
        h = c.get(Calendar.HOUR_OF_DAY) - hour;
        c.set(Calendar.HOUR_OF_DAY, h);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.v("time",df.format(c.getTime()));
        return df.format(c.getTime());
    }
    /**
     * 比较时间大小
     * @param str1：要比较的时间
     * @param str2：要比较的时间
     * @return
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     *获取日期对应的星期
     * @param date 要解析的日期
     * @return
     */
    public static String getWeekday(Date date){
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        c.setTime(date);
        String weekdayString;
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                weekdayString = "星期日";
                break;
            case 2:
                weekdayString = "星期一";
                break;
            case 3:
                weekdayString = "星期二";
                break;
            case 4:
                weekdayString = "星期三";
                break;
            case 5:
                weekdayString = "星期四";
                break;
            case 6:
                weekdayString = "星期五";
                break;
            case 7:
                weekdayString = "星期六";
                break;
            default:
                weekdayString = null;
                break;
        }

        return weekdayString;
    }

    public static int getDaysBetween(Date startData, Date endData) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startData);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endData);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int)((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (24 * 3600 * 1000));
    }
}
