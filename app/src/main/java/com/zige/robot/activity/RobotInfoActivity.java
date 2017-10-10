package com.zige.robot.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.fragment.RobotInfoFragment;
import com.zige.robot.fragment.RobotSwitchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 馒头机器人
 */
public class RobotInfoActivity extends BaseActivity {

    private String[] mTabString = {"添加馒头", "设备切换", "家庭列表", "朋友列表"};
    List<Fragment> fragmentList;
    RobotFragmentPagerAdapter robotFragmentPagerAdapter;
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_robot_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("馒头机器人");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        fragmentList = new ArrayList<>();
        fragmentList.add(new RobotInfoFragment()); //信息填写
        fragmentList.add(new RobotSwitchFragment()); //设备切换
//        fragmentList.add(new FamilysListFrgment()); //家庭列表
//        fragmentList.add(new SearchFriendsFragment()); //朋友列表
        robotFragmentPagerAdapter = new RobotFragmentPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(robotFragmentPagerAdapter);
//        viewpager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewpager);
    }

    TabLayout tablayout;
    ViewPager viewpager;

    public void setFragmentIndex(int index){
        viewpager.setCurrentItem(index);
    }

    class RobotFragmentPagerAdapter extends FragmentPagerAdapter {

        public RobotFragmentPagerAdapter(FragmentManager fm ) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabString[position];
        }
    }



}
