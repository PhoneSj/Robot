package com.zige.robot.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.Pickers;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.view.PickerTimeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/20.
 * 游戏时间
 */

public class GameTimeSettingActivity extends BaseActivity {


    @BindView(R.id.tv_total_time)
    TextView mTvTotalTime;
    @BindView(R.id.tv_gap_time)
    TextView mTvGapTime;
    @BindView(R.id.tv_game_time)
    TextView mTvGameTime;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_game_time_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("游戏时间");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTotalTime.setText(totalTime);
        mTvGameTime.setText(gameTime);
        mTvGapTime.setText(gapTime);
        initTimeDialog();
    }

    Dialog timeDialog;
    PickerTimeView mPickerTimeView;
    List<Pickers> mPickersList;

    int timeFlag =0;
    String totalTime ="120分钟";
    String gapTime ="5分钟";
    String gameTime ="30分钟";

    private void initTimeDialog() {
        timeDialog = DialogUtils.createDialogForContentViewFromBottom(mContext, R.layout.dialog_picker_roles, ScreenUtils.dp2px(mContext,20), ScreenUtils.dp2px(mContext,20));
        timeDialog.findViewById(R.id.tv_cancel_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.dismiss();
            }
        });
        mPickerTimeView = (PickerTimeView) timeDialog.findViewById(R.id.pick_time_view);
        mPickerTimeView.setColorText(0x464646);
        mPickerTimeView.setColorTextSelect(0xfe5265); //字体颜色
        mPickerTimeView.setMutilTextSize(8,16); //字体大小
        mPickerTimeView.setOnSelectListener(new PickerTimeView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
              if(timeFlag ==0){
                  //总时间
                  totalTime = pickers.getShowConetnt();
                  mTvTotalTime.setText(totalTime);
              }else if(timeFlag ==1){
                  //间隔时间
                  gapTime = pickers.getShowConetnt();
                  mTvGapTime.setText(gapTime);
              }else {
                  //游戏时间
                  gameTime = pickers.getShowConetnt();
                  mTvGameTime.setText(gameTime);
              }
            }
        });

    }


    @OnClick({R.id.ll_total_game_time, R.id.ll_interval_game_time, R.id.ll_game_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_total_game_time:
                timeFlag = 0;
                mPickersList = new ArrayList<>();
                for (int i=1; i<=40; i++){
                    mPickersList.add(new Pickers((i*10)+"分钟",""));
                }
                mPickerTimeView.setData(mPickersList);
                mPickerTimeView.setSelected(totalTime);
                timeDialog.show();
                break;
            case R.id.ll_interval_game_time:
                timeFlag = 1;
                mPickersList = new ArrayList<>();
                for (int i=1; i<=40; i++){
                    mPickersList.add(new Pickers((i*5)+"分钟",""));
                }
                mPickerTimeView.setData(mPickersList);
                mPickerTimeView.setSelected(gameTime);
                timeDialog.show();
                break;
            case R.id.ll_game_time:
                timeFlag = 2;
                mPickersList = new ArrayList<>();
                for (int i=1; i<=20; i++){
                    mPickersList.add(new Pickers((i*10)+"分钟",""));
                }
                mPickerTimeView.setData(mPickersList);
                mPickerTimeView.setSelected(gameTime);
                timeDialog.show();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setGameTimeNet();
    }

    /**
     * 设置游戏时间
     */
    private void setGameTimeNet(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", mApplication.getUserInfo().getDeviceid());
        map.put("userId", mApplication.getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("totalTime", mTvTotalTime.getText().toString().replace("分钟",""));
        map.put("perTime", mTvGameTime.getText().toString().replace("分钟",""));
        map.put("intervalTime", mTvGapTime.getText().toString().replace("分钟",""));
        VRHttp.sendRequest(mContext, HttpLink.SetGameTime, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(CBaseCode.getCode())){
                    toastShow("时间设置成功");
                }else {
                    toastShow(CBaseCode.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

}
