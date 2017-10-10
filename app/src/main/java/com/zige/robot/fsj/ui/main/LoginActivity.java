package com.zige.robot.fsj.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.activity.AddRobotActivity;
import com.zige.robot.activity.MainActivity;
import com.zige.robot.activity.RegisterActivity;
import com.zige.robot.bean.LoginCode;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.util.MyNetworkUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.service.HXServiceLogin;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by PhoneSj on 2017/10/9.
 */

public class LoginActivity extends StateActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.layout_root)
    RelativeLayout layoutRoot;

    //文本监听
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                etPwd.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 登录结果广播接收器
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (HXServiceLogin.HX_LOGIN_FAIL.equals(action)) {
                SnackbarUtil.showLong(layoutRoot, "登录失败");
            } else if (HXServiceLogin.HX_LOGIN_SUC.equals(action)) {
                SnackbarUtil.showLong(layoutRoot, "登录成功");
                if (TextUtils.isEmpty(App.getInstance().getUserInfo().getDeviceid())) {
                    //还没有“主人账号”，当前设置主人账号
                    startActivity(AddRobotActivity.class);
                } else {
                    //已经有“主人”账号，当前设置普通账号
                    startActivity(MainActivity.class);
                    finish();
                }
                SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.auto_login, "1");
            }
            dismissProgressDialog();
        }
    };


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra("bindState", 0) == 1) {
            loginServer(etPhone.getText().toString(), etPwd.getText().toString()); //首次成功绑定返回
        }
    }

    @Override
    protected void initEventAndData() {
        String phone = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_userCount, "");
        String password = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_password, "");
        String auto_login = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.auto_login, "0");//1表示自动登录
        etPhone.setText(phone);
        etPwd.setText(password);
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setSelection(phone.length());
        }
        if (!TextUtils.isEmpty(password)) {
            etPwd.setSelection(password.length());
        }
        if ("1".equals(auto_login)) {
            //自动登录
            loginServer(phone, password);
        }
        initReceiver();
        etPhone.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.tv_register, R.id.tv_forget_pwd, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivityForResult(new Intent(mContext, RegisterActivity.class), Constants.REQUEST_CODE_REGISTER);
                break;
            case R.id.tv_forget_pwd:
                break;
            case R.id.btn_login:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_REGISTER && resultCode == Constants.RESULT_CODE_REGISTER) {
            //注册成功返回
            String phone = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_userCount, "");
            String pwd = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_password, "");
//            String phone=SharedPreferencesUtils.getuser
            etPhone.setText(phone);
            etPwd.setText(pwd);
            loginServer(phone, pwd);
        }
    }

    /**
     * 初始化登录广播接收器
     */
    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(HXServiceLogin.HX_LOGIN_SUC);
        filter.addAction(HXServiceLogin.HX_LOGIN_FAIL);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void loginServer(final String phone, final String pwd) {
        if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
            ToastUtils.showToast("网络连接不可用,请检查网络");
            return;
        }
        String mPushKey = JPushInterface.getRegistrationID(mContext);
        Log.d(this.getClass().getSimpleName(), "pushKey: " + mPushKey);
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", phone);
        map.put("password", pwd);
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("pushKey", mPushKey);
        showProgressDialog("正在登录中...", true);
        VRHttp.sendRequestNoDilalog(mContext, HttpLink.LoginLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                LoginCode loginCode = GsonUtils.getServerBean((String) response, LoginCode.class);
                if ("0000".equals(loginCode.code) || "0".equals(loginCode.code)) {
                    if (loginCode.robot != null) {
                        SharedPreferencesUtils.saveValue(getApplicationContext(), SharedPreferencesUtils.keep_login_userCount, phone);
                        SharedPreferencesUtils.saveValue(getApplicationContext(), SharedPreferencesUtils.keep_login_password, pwd);
                        App.getInstance().setPhone(phone);
                        App.getInstance().setUserInfo(loginCode.robot);

                        //账号、密码存入sp中
                        SharedPreferencesUtils.saveRobotIdToSP(loginCode.robot.getDeviceid());
                        SharedPreferencesUtils.saveHXAcount(phone);
                        SharedPreferencesUtils.saveHXPassword(loginCode.robot.getHxPwd());
                        SharedPreferencesUtils.saveConnectedHXContact(loginCode.robot.getDeviceid());
                        startService(new Intent(mContext, HXServiceLogin.class));
                    }
                } else {
                    stateError("登录失败");
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                dismissProgressDialog();
            }

        });
    }
}
