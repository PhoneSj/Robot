package com.zige.robot.view.calendar;

/**
 * Created by cracker on 2017/8/4.
 */

public class CustomBeam {
    public int monthCount;
    public int spaceCount;
    public int nextCount;
    public String yearMonth;
    public String curYearMonth;

    @Override
    public String toString() {
        return "CustomBeam{" +
                "monthCount=" + monthCount +
                ", spaceCount=" + spaceCount +
                ", nextCount=" + nextCount +
                ", yearMonth='" + yearMonth + '\'' +
                ", curYearMonth='" + curYearMonth + '\'' +
                '}';
    }
}
