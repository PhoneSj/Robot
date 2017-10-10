package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zige.robot.base.BaseActivity;

import com.zige.robot.R;

/**
 * 添加馒头
 */
public class AddRobotActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_add_robot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("添加馒头");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_action://跳过
            case R.id.btn_add:
                startActivity(new Intent(mContext, OpenRobotActvity.class));
                break;
            case R.id.rl_back_return:
                finish();
            default:
                break;
        }
    }
}
