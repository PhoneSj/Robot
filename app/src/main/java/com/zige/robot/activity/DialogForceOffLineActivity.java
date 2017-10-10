package com.zige.robot.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.greendao.util.OrderDaoUtil;
import com.zige.robot.service.HXServiceLogin;
import com.zige.robot.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/26.
 * 帐号在其余地方登录
 */

public class DialogForceOffLineActivity extends BaseActivity {

    @BindView(R.id.tv_click_left)
    TextView mTvClickLeft;
    @BindView(R.id.tv_click_right)
    TextView mTvClickRight;
    @BindView(R.id.tv_tip)
    TextView mTvTip;

    @Override
    protected int initPageLayoutID() {
        return R.layout.dialog_tip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.drawable.drwable_black);
        setFinishOnTouchOutside(false);// 点击屏幕外边不消失
        ButterKnife.bind(this);
        mTvTip.setText("你的账号已在其他手机登录\n是否重新登录");
        mTvClickLeft.setText("确定");
        mTvClickRight.setText("取消");
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void initReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(HXServiceLogin.HX_LOGIN_SUC);
        filter.addAction(HXServiceLogin.HX_LOGIN_FAIL);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onBackPressed() {
        logOut2Login();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(HXServiceLogin.HX_LOGIN_SUC.equals(action)){
                toastShow("登录成功");
                finish();
            }else if(HXServiceLogin.HX_LOGIN_FAIL.equals(action)){
                toastShow("登录失败");
            }
        }
    };

    private void logOut2Login(){
        toastShow("登出成功");
        App.getInstance().setUserInfo(null);
        App.getInstance().setPhone("");
        SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.keep_login_password, "");
        SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.auto_login, "0");
        new Thread(new Runnable() {
            @Override
            public void run() {
             //删除数据库的记录
                  OrderDaoUtil.getInstance().deleteAllOrder();
            }}).start();
            App.getInstance().finishAllActivity();
            startActivity(LoginActivity.class);
    }

    @OnClick({R.id.tv_click_left, R.id.tv_click_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_click_left:
                //重新登录
                HXServiceLogin.getInstance().loginToHx();
                break;
            case R.id.tv_click_right:
                //退出登录跳到登录页面
                 logOut2Login();
                break;
        }
    }
}
