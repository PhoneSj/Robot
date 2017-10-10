package com.zige.robot.view.calendar;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by cracker on 2017/8/5.
 */

public class SelectedDate implements Comparable<SelectedDate> {
    private int year;
    private int month;
    private int day;
    private int x;
    private int y;
    Calendar calendar;

    public SelectedDate(int year, int month, int day, int x, int y, Calendar calendar) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.x = x;
        this.y = y;
        this.calendar = calendar;
    }


    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int compareTo(@NonNull SelectedDate o) {
        return calendar.before(o.calendar) ? -1 : 1;
    }
}
