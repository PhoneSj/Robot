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
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgotPasswordActivity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_v_code)
    EditText etMsgCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_pwd)
    EditText etPassword;
    int timeCount;
    int type;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_forgot_password;
    }
    @Override
    protected void performHandleMessage(Message msg) {
        super.performHandleMessage(msg);
        if(msg.what == 1){
            timeCount--;
            if(timeCount >0){
                tvGetCode.setText(timeCount + "s后重发");
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }else {
                tvGetCode.setText("重新发送");
                tvGetCode.setEnabled(true);
            }
        }
    }

    /**
     * 获取手机验证码
     */
    private void getMsgCode() {
        String phone = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            toastShow("手机号码不能为空！");
            return;
        }
        if (!SystemUtils.isMobileNO(phone)) {
            toastShow("手机号码不正确！");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", String.valueOf(1)); //1 找回或者重置;
        VRHttp.sendRequest(mContext, HttpLink.SmsGenerateLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(CBaseCode.getCode())){
                  timeCount = 60;
                  tvGetCode.setText(timeCount + "s后重发");
                  tvGetCode.setEnabled(false);
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
     * 重置密码
     */
    private void sendChangePsw() {
        final String phone = etPhone.getText().toString().trim();
        String verifyCode = etMsgCode.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            if (!SystemUtils.isMobileNO(phone)) {
                toastShow("手机号码不正确！");
                return;
            }
        } else {
            toastShow("手机号码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            toastShow("验证码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            toastShow("密码不能为空！");
            return;
        }
        if(password.length() <6 ||password.length() >25){
            toastShow("请输入6-25位密码");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("verifyCode", verifyCode);
        VRHttp.sendRequest(mContext, HttpLink.ResetParswordLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode cBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(cBaseCode.getCode()) || "0".equals(cBaseCode.getCode())){
                    toastShow("密码修改成功");
                    if(type == 1){
                        SharedPreferencesUtils.saveValue(getApplicationContext(), SharedPreferencesUtils.keep_login_password, password);
                        finish();
                        return;
                    }
                    setResult(RESULT_OK);
                    finish();
                }else {
                    toastShow(cBaseCode.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        if(type == 1){
            setTitleName("修改密码");
        }else {
            setTitleName("忘记密码");
        }
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @OnClick({ R.id.tv_get_code, R.id.btn_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code: //获取验证码
                getMsgCode();
                break;
            case R.id.btn_ensure:
                sendChangePsw();
                break;
        }
    }
}
