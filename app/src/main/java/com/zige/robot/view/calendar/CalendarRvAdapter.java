package com.zige.robot.view.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zige.robot.R;


/**
 * Created by cracker on 2017/8/3.
 */

public class CalendarRvAdapter extends RecyclerView.Adapter<CalendarRvHolder> {

    @Override
    public CalendarRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_rv_item, null);
        CalendarRvHolder holder = new CalendarRvHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CalendarRvHolder holder, int position) {
        holder.txtv_yearmonth.setText(DateUtils.getDate(position));
        holder.gv.setData(DateUtils.getList(position),this);
        holder.gv.setSelectListenner(mCalendarSelectListenner);
    }


    CalendarSelectListenner mCalendarSelectListenner;
    public void setCalendarSelectListenner(CalendarSelectListenner calendarSelectListenner) {
        mCalendarSelectListenner = calendarSelectListenner;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
