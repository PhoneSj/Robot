package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.fsj.util.CallUtil;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.greendao.util.OrderDaoUtil;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.interf.ActionClickListener;
import com.zige.robot.service.HXServiceLogin;
import com.zige.robot.service.ServiceLogin;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.HXManager;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import static com.zige.robot.R.id.ll_face_set;


public class SettingActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_setting;
    }

    private LinearLayout layoutRoot;
    //    ToggleButton msg_toggle;
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("设置");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layoutRoot = (LinearLayout) findViewById(R.id.layout_root);
//        msg_toggle = (ToggleButton) findViewById(msg_toggle); //通知开关
        tv_version = (TextView) findViewById(R.id.tv_version);
        findViewById(R.id.ll_my_click).setOnClickListener(this);
        findViewById(R.id.ll_course_push_click).setOnClickListener(this);
        findViewById(R.id.ll_smart_home_click).setOnClickListener(this);
        findViewById(R.id.ll_edit_pwd_click).setOnClickListener(this);
        findViewById(R.id.ll_function_click).setOnClickListener(this);
        findViewById(R.id.ll_agreement_click).setOnClickListener(this);
        findViewById(R.id.tv_logout_click).setOnClickListener(this);
        findViewById(R.id.ll_mento_log).setOnClickListener(this);
        findViewById(R.id.ll_mento_alarm).setOnClickListener(this);
        findViewById(R.id.ll_video_click).setOnClickListener(this);
        findViewById(R.id.ll_robot_click).setOnClickListener(this);
        findViewById(R.id.tv_unbind_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindRobot(mApplication.getUserInfo().getUserId() + "");
            }
        });
        findViewById(R.id.ll_face_set).setOnClickListener(this);


        boolean isMessageOpen = (boolean) SharedPreferencesUtils.getValue(mContext, SharedPreferencesUtils.message_isOpen, false);
        if (isMessageOpen) {
//            msg_toggle.toggleOn();
        } else {
//            msg_toggle.toggleOff();
        }
//        msg_toggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
//            @Override
//            public void onToggle(boolean on) {
//                SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.message_isOpen, on);
//            }
//        });
        tv_version.setText("mento Robot:" + SystemUtils.getVersionName(mContext));
    }


    private void unBindRobot(final String userId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.UnbindRobot, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if ("0000".equals(CBaseCode.getCode()) || "0".equals(CBaseCode.getCode())) {
                    if (userId.equals(App.getInstance()
                            .getUserInfo()
                            .getUserId() + "") && "1".equals(App.getInstance()
                            .getUserInfo()
                            .getAdmin())) {
                        //如果自己是管理员解绑了，跳到登陆页面
//                        ServiceLogin.getInstance()
//                                .sendTextMsg(App.getInstance()
//                                        .getUserInfo()
//                                        .getDeviceid(), "hostUnBind", new IMMsgListener() {
//                                    @Override
//                                    public void onSuccess(TIMMessage msg) {
//                                        //主人解绑成功需要登出避免重新登录新的帐号，旧的帐号没有登出
//                                        ILiveLoginManager.getInstance()
//                                                .iLiveLogout(new ILiveCallBack() {
//                                                    @Override
//                                                    public void onSuccess(Object data) {
//                                                        toastShow("解绑成功");
//                                                        SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.auto_login, "0");
//                                                        stopService(new Intent(mContext, ServiceLogin.class));//销毁后台服务
//                                                        App.getInstance().finishAllActivity();
//                                                        startActivity(LoginActivity.class);
//                                                    }
//
//                                                    @Override
//                                                    public void onError(String module, int errCode, String errMsg) {
//
//                                                    }
//                                                });
//                                    }
//
//                                    @Override
//                                    public void onError(int code, String desc) {
//
//                                    }
//                                });

                        String receive_robot = App.getInstance().getUserInfo().getDeviceid();
                        
                        HXManager.sendTxt(receive_robot, 4, "hostUnBind", new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                toastShow("解绑成功");
                                SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.auto_login, "0");
                                stopService(new Intent(mContext, ServiceLogin.class));//销毁后台服务
                                App.getInstance().finishAllActivity();
                                startActivity(LoginActivity.class);
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(int i, String s) {
                                Toast.makeText(mContext, "正在发送", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
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
     * 退出当前号码登陆
     */
    private void logout() {
        //清空相关保存的参数
        toastShow("退出成功");
        App.getInstance().setUserInfo(null);
        App.getInstance().setPhone("");
        SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.keep_login_password, "");
        SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.auto_login, "0");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //删除数据库的记录
                OrderDaoUtil.getInstance().deleteAllOrder();
            }
        }).start();
        App.getInstance().finishAllActivity();
        startActivity(LoginActivity.class);

//        stopService(new Intent(mContext, ServiceLogin.class));//销毁后台服务
//        ILiveLoginManager.getInstance().iLiveLogout(new ILiveCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//
//            }
//
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//                Log.d(TAG, "errCode: " + errCode + "   " + "errMsg:  " + errMsg);
//            }
//        });
        stopService(new Intent(mContext, HXServiceLogin.class));
        //退出环信登录
        CallUtil.logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtil.showI("logout hx success");
                SnackbarUtil.showShort(layoutRoot, "logout hx success");
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.showI("logout hx error");
                SnackbarUtil.showShort(layoutRoot, "logout hx error");
            }

            @Override
            public void onProgress(int i, String s) {
                LogUtil.showI("logout hx progress");
                SnackbarUtil.showShort(layoutRoot, "logout hx progress");
            }
        });
    }

    Dialog loginOutDialog;

    private void showLoginOutDialog() {
        loginOutDialog = DialogUtils.createTipDialog(mContext, "确认退出当前的帐号", "是", "否", new ActionClickListener() {
            @Override
            public void clickLeft() {
                logout();
                loginOutDialog.dismiss();
            }

            @Override
            public void clickRight() {
                loginOutDialog.dismiss();
            }
        });
        loginOutDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_click: //我的
                startActivity(MyInformationActivity.class);
                break;
            case ll_face_set: //人脸识别
                startActivity(FaceListActivity.class);
                break;
            case R.id.ll_smart_home_click: //智能家居
//                startActivity(SmartHomeActivity.class);
                break;
            case R.id.ll_course_push_click: //学习时间设置
                startActivity(StudyTimeSettingActivity.class);
                break;
            case R.id.ll_edit_pwd_click: //修改密码
                startActivity(new Intent(mContext, ForgotPasswordActivity.class).putExtra("type", 1));
                break;
            case R.id.ll_function_click: //功能介绍
                break;
            case R.id.ll_agreement_click: //用户协议
                startActivity(UserAgreementActivity.class);
                break;
            case R.id.tv_logout_click: //退出登录
                showLoginOutDialog();
                break;
            case R.id.ll_mento_log: //馒头日志
                startActivity(MentoLogActivity.class);
                break;
            case R.id.ll_mento_alarm: //闹钟
                startActivity(MentoRemindActivity.class);
                break;
            case R.id.ll_robot_click:
                startActivity(RobotInfoActivity.class);
                break;
            case R.id.ll_video_click:
                startActivity(SpeakVoiceActivity.class);
                break;

        }
    }
}
