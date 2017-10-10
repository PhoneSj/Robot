package com.zige.robot.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.RunRecordStatBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.view.LogArcView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 * 馒头日志
 */

public class MentoLogActivity  extends BaseActivity implements View.OnClickListener{
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_mento_log;
    }

    LogArcView log_view;
    TextView tv_game_time;
    TextView tv_battery;
    TextView tv_current_app;

    TextView tv_rate_idle;
    TextView tv_rate_story;
    TextView tv_rate_music;
    TextView tv_rate_learn;
    TextView tv_rate_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("馒头日志");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        log_view = (LogArcView) findViewById(R.id.log_view);
        tv_game_time = (TextView) findViewById(R.id.tv_game_time);
        tv_battery = (TextView) findViewById(R.id.tv_battery);
        tv_current_app = (TextView) findViewById(R.id.tv_current_app);
        tv_rate_idle = (TextView) findViewById(R.id.tv_rate_idle);
        tv_rate_story = (TextView) findViewById(R.id.tv_rate_story);
        tv_rate_music = (TextView) findViewById(R.id.tv_rate_music);
        tv_rate_learn = (TextView) findViewById(R.id.tv_rate_learn);
        tv_rate_other = (TextView) findViewById(R.id.tv_rate_other);
        findViewById(R.id.ll_game_time).setOnClickListener(this);
        findViewById(R.id.ll_operate_history).setOnClickListener(this);
        getDataFromNet();
    }


    private void getDataFromNet(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.RunRecordStat, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RunRecordStatBean runRecordStatBean = GsonUtils.getServerBean((String) response, RunRecordStatBean.class);
                if("0000".equals(runRecordStatBean.getCode())){
                    RunRecordStatBean.RobotRunStatBean robotRunStatBean = runRecordStatBean.getRobotRunStat();
                    if(robotRunStatBean ==null){
                        return;
                    }
                    List<String> data = new ArrayList<>();
//                    data.add(String.valueOf(robotRunStatBean.getRateIdle() / 100));
//                    data.add(String.valueOf(robotRunStatBean.getRateStory() / 100));
//                    data.add(String.valueOf(robotRunStatBean.getRateMusic() / 100));
//                    data.add(String.valueOf(robotRunStatBean.getRateLearn() / 100));
//                    data.add(String.valueOf(robotRunStatBean.getRateOther() / 100));

                    data.add("0.2");
                    data.add("0.1");
                    data.add("0.3");
                    data.add("0.1");
                    data.add("0.3");

                    log_view.setArcList(data);
                    tv_game_time.setText(robotRunStatBean.getGameTotalTime()+"分钟");
                    tv_battery.setText(robotRunStatBean.getBattery()+"%");
                    tv_current_app.setText(robotRunStatBean.getCurAppName());
                }else {
                    toastShow(runRecordStatBean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_game_time: //游戏时间
                startActivity(GameTimeSettingActivity.class);
                break;

            case R.id.ll_operate_history://运行历史
                startActivity(OperateHistoryActivity.class);
                break;

            default:
                break;
        }
    }
}
