package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.view.calendar.CalendarRvAdapter;
import com.zige.robot.view.calendar.CalendarSelectListenner;
import com.zige.robot.view.calendar.CalendarView;

import butterknife.BindView;

import static com.zige.robot.R.id.calendar_rv;
import static com.zige.robot.view.calendar.DateUtils.curPos;


/**
 * Created by Administrator on 2017/7/31.
 * 日历选择
 */

public class HomeWorkDateActivity extends BaseActivity {


    @BindView(calendar_rv)
    RecyclerView mCalendarRv;

    boolean bPreDate= false; //false不能    true能当天之前的日期


    private CalendarRvAdapter mCalendarRvAdapter;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_home_work_date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("完成期限");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bPreDate = getIntent().getBooleanExtra("bPreDate", false);
        if(bPreDate){
            setTitleName("请选择时间");
        }
        initData();

    }


    private void initData() {
        //初始化Rv
        CalendarView.isSelectBefore = bPreDate;
        CalendarView.clickedList.clear();
        mCalendarRvAdapter = new CalendarRvAdapter();
        mCalendarRvAdapter.setCalendarSelectListenner(new CalendarSelectListenner() {
            @Override
            public void onSelected(int type, String[] time) {
                if (CalendarView.TYPE_START == type) {
                    Log.e("kk", "类型：" + type + "," + time[0]);
                } else {
                    Log.e("kk", "类型：" + type + "," + time[0] + "---->" + time[1]);
                    setResult(RESULT_OK, new Intent().putExtra("startTime", time[0]+" "+"00:00:00")
                    .putExtra("endTime", time[1]+" "+"23:59:59"));
                    finish();
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCalendarRv.setLayoutManager(manager);
        mCalendarRv.setAdapter(mCalendarRvAdapter);
        mCalendarRv.setWillNotDraw(false);
        mCalendarRv.scrollToPosition(curPos);
    }


}
