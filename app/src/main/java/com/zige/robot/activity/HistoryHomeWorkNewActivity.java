package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.fragment.HistoryHomeWorkFragment;
import com.zige.robot.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/20.
 * 作业历史记录
 */

public class HistoryHomeWorkNewActivity extends BaseActivity {

    private static final String TAG = "HistoryHomeWorkNewActiv";

    HomeFragmentPagerAdapter homeFragmentPagerAdapter;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    List<HistoryHomeWorkFragment> mFragments = new ArrayList<>();


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_history_home_work;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("历史记录");
        setIvActionbg(R.drawable.calendar);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //日历
                startActivityForResult(new Intent(mContext, HomeWorkDateActivity.class).putExtra("bPreDate", true), 1);
            }
        });
        mFragments.add(HistoryHomeWorkFragment.newInstance(-1)); //全部
        for (int i = 0; i < HomeWorkNewActivity.subjectList.size(); i++) { //科目类别
            HistoryHomeWorkFragment fragment = HistoryHomeWorkFragment.newInstance(HomeWorkNewActivity.subjectList.get(i).getSubjectId());
            mFragments.add(fragment);
        }
        homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(homeFragmentPagerAdapter);
        mViewpager.setOffscreenPageLimit(HomeWorkNewActivity.subjectList.size());
        if(HomeWorkNewActivity.subjectList.size() > 4){
            mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE); //可滑动的
        }else {
            mTablayout.setTabMode(TabLayout.MODE_FIXED); //固定的
        }
        mTablayout.setupWithViewPager(mViewpager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1&& resultCode == RESULT_OK){
            String startTime = data.getStringExtra("startTime"); //开始时间
            String endTime   = data.getStringExtra("endTime");   //截止时间
            setTitleName(startTime.split(" ")[0] +" - " +endTime.split(" ")[0]);
            Log.d(TAG, "onActivityResult: start1Time: "+ startTime + "  endTime: "+ endTime);
            if( mFragments.size() >0){
                for (HistoryHomeWorkFragment fragment : mFragments) {
                    Log.d(TAG, "refresh history data: ");
                     fragment.resetDateHistory(DateFormatUtils.DateFormat2Stamp(startTime,"yyyy-M-d HH:mm:ss")/1000, DateFormatUtils.DateFormat2Stamp(endTime,"yyyy-M-d HH:mm:ss")/1000);
                }
            }
        }
    }

   public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

        public HomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (position==0?"全部":HomeWorkNewActivity.subjectList.get(position-1).getSubjectName());
        }
    }


}
