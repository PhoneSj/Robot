package com.zige.robot.view.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zige.robot.R;


/**
 * Created by cracker on 2017/8/3.
 */

public class CalendarRvHolder extends RecyclerView.ViewHolder {
    public CalendarView gv;
    public TextView txtv_yearmonth;

    public CalendarRvHolder(View itemView) {
        super(itemView);
        gv = (CalendarView) itemView.findViewById(R.id.calendar_rv_item_gv);
        txtv_yearmonth = (TextView) itemView.findViewById(R.id.calendar_rv_item_txtv_year);
    }
}
