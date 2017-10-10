package com.zige.robot.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.LoginCode;
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

import cn.jpush.android.api.JPushInterface;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HXServiceLogin";

    private RelativeLayout layoutRoot;
    private EditText et_phone;
    private EditText et_pwd;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_forget_pwd).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        et_phone.addTextChangedListener(textWatcher);
        String phone = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_userCount, "");
        String password = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_password, "");
        String auto_login = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.auto_login, "0");
        et_phone.setText(phone);
        et_pwd.setText(password);
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setSelection(phone.length());
        }
        if (!TextUtils.isEmpty(password)) {
            et_pwd.setSelection(password.length());
        }
        if ("1".equals(auto_login)) {
            //自动登录
            loginServer(phone, password);
        }
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                et_pwd.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_register: //注册
                startActivityForResult(new Intent(mContext, RegisterActivity.class), 1);
                break;

            case R.id.tv_forget_pwd: //忘记密码
                startActivityForResult(new Intent(mContext, ForgotPasswordActivity.class)
                        .putExtra("type", 0), 1);
                break;

            case R.id.btn_login:
                String phone = et_phone.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    toastShow("请输入手机号码");
                    return;
                }
                if (!SystemUtils.isMobileNO(phone)) {
                    toastShow("手机号码不正确");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    toastShow("请输入密码");
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 25) {
                    toastShow("请输入6-25位密码");
                    return;
                }
                loginServer(phone, pwd);
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra("bindState", 0) == 1) {
            loginServer(et_phone.getText().toString(), et_pwd.getText().toString()); //首次成功绑定返回
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //注册成功返回
            String phone = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_userCount, "");
            String pwd = (String) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.keep_login_password, "");
            et_phone.setText(phone);
            et_pwd.setText(pwd);
            loginServer(phone, pwd);
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(HXServiceLogin.HX_LOGIN_SUC);
        filter.addAction(HXServiceLogin.HX_LOGIN_FAIL);
        registerReceiver(mBroadcastReceiver, filter);
    }


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

    /**
     * 登录
     */
    private void loginServer(final String phone, final String pwd) {
        if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
            ToastUtils.showToast("网络连接不可用,请检查网络");
            return;
        }
        String mPushKey = JPushInterface.getRegistrationID(mContext);
        Log.d(TAG, "pushKey: " + mPushKey);
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
//                        TempUtil.saveRobotIdToSP(loginCode.robot.getDeviceid());
//                        TempUtil.saveHXAcount(phone);
//                        TempUtil.saveHXPassword(loginCode.robot.getHxPwd());
//                        TempUtil.saveConnectedHXContact("18923729010");
                        startService(new Intent(mContext, HXServiceLogin.class));
//                        CallUtil.logout(new EMCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                Log.d(TAG, "onSuccess: ");
//                            }
//
//                            @Override
//                            public void onError(int i, String s) {
//                                Log.d(TAG, "onError: ");
//                            }
//
//                            @Override
//                            public void onProgress(int i, String s) {
//                                Log.d(TAG, "onProgress: ");
//                            }
//                        });

//                        startService(new Intent(mContext, ServiceLogin.class) //后台登录腾讯云
//                                .putExtra("account", App.getInstance().getPhone())
//                                .putExtra("sig", App.getInstance().getUserInfo().getSig()));
//                        // TODO: 2017/9/14 将服务器返回的环信账号、密码存入sp中
//                        TempUtil.saveHXAcount(phone);
//                        TempUtil.saveHXPassword(loginCode.robot.getHxPwd());
//                        TempUtil.saveConnectedHXContact("r" + loginCode.robot.getDeviceid());
//                        LogUtil.w("acount:" + TempUtil.getHXAcount());
//                        LogUtil.w("pwd:" + TempUtil.getHXPassword());
//                        //登录环信
//                        CallUtil.login(new EMCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                Log.i(App.TAG, "sign in success");
//                                EMClient.getInstance().chatManager().loadAllConversations();
//                                try {
//                                    EMClient.getInstance()
//                                            .groupManager()
//                                            .getJoinedGroupsFromServer();
//                                } catch (HyphenateException e) {
//                                    e.printStackTrace();
//                                }
////                                //账号、密码存入sp中
////                                VMSPUtil.put(LoginActivity.this, "username", );
////                                VMSPUtil.put(LoginActivity.this, "password", password);
////
////
////                                Intent intent = new Intent(LoginActivity.this, MainCallActivity.class);
////                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onError(int i, String s) {
//
//                            }
//
//                            @Override
//                            public void onProgress(int i, String s) {
//
//                            }
//                        });

                    }
                } else {
                    toastShow(loginCode.message);
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
