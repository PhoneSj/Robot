package com.zige.robot.view.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cracker on 2017/8/4.
 */

public class DateUtils {

    public  static int curPos = (getCurYear() - 1970) * 12 + getMonth();

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy年MM月");

    public static String getDate(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position - curPos);
        return mSimpleDateFormat.format(calendar.getTime());
    }

    public static int getCurYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
//        Log.e("kk", "________." + calendar.get(Calendar.MONTH));
        return calendar.get(Calendar.MONTH);
    }

    public static CustomBeam getList(int position) {
//        Log.e("kk", position + "");
        CustomBeam beam = new CustomBeam();
        Calendar calendar = Calendar.getInstance();
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
//        Log.e("kk", "---->" + curDay);
        beam.curYearMonth = calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH);
        int i = position - curPos;
        calendar.add(Calendar.MONTH, i);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;//星期天是1，星期六是7
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int max = calendar.get(Calendar.DAY_OF_MONTH);
        beam.spaceCount = week;
        beam.monthCount = max;
        if (position == curPos) {
            beam.nextCount = curDay - 1;//显示灰色的天数
        } else if (position > curPos) {
            beam.nextCount = 0;
        } else {
            beam.nextCount = max;
        }
        beam.yearMonth = calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH);
        return beam;
    }
}
