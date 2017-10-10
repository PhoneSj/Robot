package com.zige.robot.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zige.robot.adapter.SmartHomeAdapter;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.SmartHomeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.zige.robot.R;

/**
 * Created by Administrator on 2017/7/11.
 * 智能家居引导页
 */

public class SmartHomeActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    List<SmartHomeBean> mSmartHomeBeenList;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_smart_home;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTitleName("使用说明");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData(){
        mSmartHomeBeenList = new ArrayList<>();
        mSmartHomeBeenList.add(new SmartHomeBean(R.drawable.smart_home_01, "首次连接请按桥接器"));
        mSmartHomeBeenList.add(new SmartHomeBean(R.drawable.smart_home_02, "对馒头说“开灯”或“关灯”"));
        mSmartHomeBeenList.add(new SmartHomeBean(R.drawable.smart_home_03, "对选择灯的名字进行单个操作。例如：“打开客厅的灯”"));
        mSmartHomeBeenList.add(new SmartHomeBean(R.drawable.smart_home_04, "可对灯的亮度和颜色（灯必须有变色的功能）进行控制。例如“亮一点”“暗一点”或“红色” “绿色”等"));
        mSmartHomeBeenList.add(new SmartHomeBean("http://m.vrdube.com/IAmMento.mp4", "可点击视频观看"));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerview.setAdapter(new SmartHomeAdapter(mSmartHomeBeenList));
    }






}
