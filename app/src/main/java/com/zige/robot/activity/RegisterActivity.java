package com.zige.robot.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.LoginCode;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/4/17.
 * 注册
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    EditText et_phone;
    EditText et_v_code;
    EditText et_pwd;
    TextView tv_get_code;

    int timeCount;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("注册");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_v_code = (EditText) findViewById(R.id.et_v_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        findViewById(R.id.tv_get_code).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.rl_back_return).setOnClickListener(this);
    }


    @Override
    protected void performHandleMessage(Message msg) {
        super.performHandleMessage(msg);
        if(msg.what == 1){
            timeCount--;
            if(timeCount >0){
                tv_get_code.setText(timeCount + "s后重发");
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }else {
                tv_get_code.setText("重新发送");
                tv_get_code.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_get_code){
            //获取验证码
            String phone = et_phone.getText().toString().trim();
            if (!TextUtils.isEmpty(phone)) {
                if (!SystemUtils.isMobileNO(phone)) {
                    toastShow("手机号码不正确！");
                    return;
                }
            } else {
                toastShow("请输入手机号码");
                return;
            }
         getMsgCode(phone);

        }else if(v.getId() == R.id.btn_register){
            //注册
            String phone = et_phone.getText().toString().trim();
            String verifyCode = et_v_code.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();
            if(TextUtils.isEmpty(phone)){
                toastShow("请输入手机号码");
                return;
            }
            if (!SystemUtils.isMobileNO(phone)) {
                toastShow("手机号码不正确");
                return;
            }
            if(TextUtils.isEmpty(verifyCode)){
                toastShow("请输入验证码");
                return;
            }
            if(TextUtils.isEmpty(pwd)){
                toastShow("请输入密码");
                return;
            }
            if(pwd.length() <6 ||pwd.length() >25){
                toastShow("请输入6-25位密码");
                return;
            }
            registerApp(phone, pwd, verifyCode);
        }else if(v.getId() == R.id.rl_back_return){
            finish();
        }
    }

        /**
     * 获取手机验证码
     */
    private void getMsgCode(String phone) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", String.valueOf(0)); //0 注册
        VRHttp.sendRequest(mContext, HttpLink.SmsGenerateLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(CBaseCode.getCode())){
                    timeCount = 60;
                    tv_get_code.setText(timeCount + "s后重发");
                    tv_get_code.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(1, 1000);
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

    /**
     * 注册
     */
    private void registerApp(final String phone, final String password, String verifyCode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("verifyCode", verifyCode);
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("pushKey", JPushInterface.getRegistrationID(mContext));
        VRHttp.sendRequest(mContext, HttpLink.RegisterLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                LoginCode loginCode = GsonUtils.getServerBean((String) response, LoginCode.class);
                if("0000".equals(loginCode.code)||"0".equals(loginCode.code)){
                    toastShow("注册成功");
                    setResult(RESULT_OK);
                    finish();
                }else {
                    toastShow(loginCode.message);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }
}
