package com.zige.robot.activity;

import android.os.Bundle;
import android.view.View;

import com.zige.robot.base.BaseActivity;

import com.zige.robot.R;

/**
 * 互动
 */
public class InteractionActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_interaction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("互动");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.rl_mento_log).setOnClickListener(this);
        findViewById(R.id.rl_mento_cultivate).setOnClickListener(this);
        findViewById(R.id.rl_mento_remind).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mento_log: //日志
                startActivity(MentoLogActivity.class);
                break;
            case R.id.rl_mento_cultivate: //养成
                startActivity(MentoCultivateActivity.class);
                break;
            case R.id.rl_mento_remind://提醒
                startActivity(MentoRemindActivity.class);
                break;
            default:
                break;
        }
    }



}
