package com.zige.robot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lidingwei on 2017/7/31.
 */

public class DateFormatUtils {
    /**
     * 时间格式 2012-3-4时间  转换成时间戳
     * @param ymd 年月日 时间
     * @param dateFormat  时间格式
     * @return
     */
    public static long DateFormat2Stamp(String ymd, String dateFormat){
        //Date或者String转化为时间戳
        SimpleDateFormat format =   new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = format.parse(ymd);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return date.getTime();
    }


    public static String Stamp2DateFormat(long timeStamp, String dateFormat){
        //Date或者String转化为时间戳
        SimpleDateFormat format =   new SimpleDateFormat(dateFormat);
        return format.format(timeStamp);
    }
}
