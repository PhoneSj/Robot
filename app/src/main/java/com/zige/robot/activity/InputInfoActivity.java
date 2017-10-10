package com.zige.robot.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.RobotDeviceListBean;
import com.zige.robot.bean.RolesNameBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.HXManager;
import com.zige.robot.utils.PopupWindowUtils;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zige.robot.R.id.et_chengyuan;
import static com.zige.robot.R.id.et_nicheng;
import static com.zige.robot.R.id.et_years_old;
import static com.zige.robot.R.id.tv_sex;

/**
 * 领养小曼信息填写
 */
public class InputInfoActivity extends BaseActivity {

    @BindView(et_chengyuan)
    EditText mEtChengyuan;
    @BindView(R.id.rl_pull_roles)
    RelativeLayout mRlPullRoles;
    @BindView(R.id.ll_chengyuan_click)
    LinearLayout mLlChengyuanClick;
    @BindView(et_nicheng)
    EditText mEtNicheng;
    @BindView(tv_sex)
    TextView mTvSex;
    @BindView(R.id.ll_sex_click)
    LinearLayout mLlSexClick;
    @BindView(et_years_old)
    EditText mEtYearsOld;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_ok)
    Button mBtnOk;
    @BindView(R.id.activity_input_info)
    RelativeLayout mActivityInputInfo;

    public static final String AGREE_BIND = "agree_bind"; //同意绑定
    public static final String REFUSE_BIND = "refuse_bind"; //拒绝绑定

    private String qrCode;
    private boolean bQrCodeOk = false;


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_input_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("填写信息");
        setTvActionText("继续扫描");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionChecker.isLackPermissions(new String[]{Manifest.permission.CAMERA})) {
                    mPermissionChecker.requestPermissions();
                } else {
                    startActivity(CaptureActivity.class);
                    finish();
                }
            }
        });
        mEtYearsOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(s.toString()) && "0".equals(s.toString())) {
                    mEtYearsOld.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initReceiver();
        initPopWindow();
        initDialog();
        getUsableRoles();
        qrCode = getIntent().getStringExtra("QrCode");
        if (TextUtils.isEmpty(qrCode)) {
            toastShow("二维码没有包含所需要信息哦");
            finish();
            return;
        } else if (qrCode.contains("zige")) {
            bQrCodeOk = true;
        } else {
            toastShow("这个二维码不是我们的哦");
            finish();
            return;
        }
        getRobotDeviceList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionChecker.hasAllPermissionsGranted(grantResults)) {
            startActivity(CaptureActivity.class);
            finish();
        } else {
            mPermissionChecker.setMessage("没有开启相机权限,请在设置中开启权限后再使用");
            mPermissionChecker.showDialog();
        }
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AGREE_BIND);
        intentFilter.addAction(REFUSE_BIND);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void getRobotDeviceList() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(App.getInstance().getUserInfo().getUserId()));
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.RobotDeviceList, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RobotDeviceListBean bean = GsonUtils.getServerBean((String) response, RobotDeviceListBean.class);
                if ("0000".equals(bean.getCode())) {
                    if (bean.getRobotList() != null && bean.getRobotList().size() > 0) {
                        for (RobotDeviceListBean.RobotListBean robotListBean : bean.getRobotList()) {
                            if (qrCode.contains(robotListBean.getRobotDeviceId())) {
                                toastShow("亲，你已经绑定了这个机器人哦！");
                                finish();
                                return;
                            }
                        }
                    }
                } else {
                    toastShow(bean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }


    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String robotDeviceId = intent.getStringExtra("robotDeviceId");
            if (AGREE_BIND.equals(action)) {
                //主人同意绑定
                sendMsgToRobot("bindOk", robotDeviceId); //通知机器作为客人身份的自己绑定成功
                toastShow("绑定成功啦，快来体验一下吧！");
                startActivity(new Intent(mContext, LoginActivity.class).putExtra("bindState", 1));
                // TODO: 2017/9/13 存入robotId
                SharedPreferencesUtils.saveRobotIdToSP(robotDeviceId);
                finish();
            }
            if (REFUSE_BIND.equals(action)) {
                //主人不同意绑定
                toastShow("主人不同意绑定");
            }
        }
    };


    /**
     * 客人扫描发送消息给主人
     *
     * @param msgContent
     * @param robotId
     */
    private void sendMsgToRobot(String msgContent, final String robotId) {
        if (TextUtils.isEmpty(msgContent)) {
            return;
        }
        if (TextUtils.isEmpty(robotId)) {
            return;
        }

        HXManager.sendTxt(robotId, 4, msgContent, new EMCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                toastShow(s);
            }

            @Override
            public void onProgress(int i, String s) {
                Toast.makeText(mContext, "正在发送", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 客人扫描发送消息给主人
     *
     * @param content
     * @param childNickname
     * @param childSex
     * @param childYearold
     * @param userId
     * @param chengYuan
     * @param msgContent
     * @param hostId
     */
    private void sendMsgToHost(String content, String childNickname, String childSex, String childYearold, String userId, String chengYuan, String msgContent, String deviceKey, final String hostId) {
        if (TextUtils.isEmpty(msgContent)) {
            return;
        }
        if (TextUtils.isEmpty(hostId)) {
            return;
        }

        Map map = new HashMap();
        map.put("action", content);
        map.put("childNickname", childNickname);
        map.put("childSex", childSex);
        map.put("childYearold", childYearold);
        map.put("userId", userId);
        map.put("nickname", chengYuan);
        map.put("deviceId", deviceKey);
        map.put("robotDeviceId", msgContent);
        HXManager.sendToHost(hostId, 4, content, map, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        toastShow("请求主人同意");
                        finish();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                toastShow(s);
            }

            @Override
            public void onProgress(int i, String s) {
                Toast.makeText(mContext, "正在发送", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 管理员绑定
    private void sendMsgToRobotAdmin(final String robotDeviceId, String hostId) {
        if (TextUtils.isEmpty(hostId)) {
            return;
        }
        if (TextUtils.isEmpty(robotDeviceId)) {
            toastShow("您还没有绑定设备呢，先绑定设备吧！");
            return;
        }

        String deviceid = App.getInstance().getUserInfo().getDeviceid();
        HXManager.sendTxt(deviceid, 4, "bindOk", hostId, new EMCallBack() {
            @Override
            public void onSuccess() {
                App.getInstance().getUserInfo().setDeviceid(robotDeviceId);
                App.getInstance().getUserInfo().setAdmin("family");
                runOnUiThread(new Runnable() {
                    public void run() {
                        toastShow("绑定成功啦，快来体验一下吧！");
                        startActivity(new Intent(mContext, LoginActivity.class).putExtra("bindState", 1));
                        finish();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                toastShow(s);
            }

            @Override
            public void onProgress(int i, String s) {
                Toast.makeText(mContext, "正在发送", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 绑定机器人
     *
     * @param robotDeviceId 机器人id
     * @param admin         1 管理员  0 非管理员
     */
    private void bindAdminRobotQuest(String nickname, String child_name, String child_sex, String child_age, final String robotDeviceId, final int admin) {
        int userId = App.getInstance().getUserInfo().getUserId();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        map.put("nickname", nickname);
        map.put("childNickname", child_name);
        map.put("childSex", child_sex);
        map.put("childYearold", child_age);
        map.put("admin", String.valueOf(admin)); // 1管理员
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", robotDeviceId); //机器人id
        VRHttp.getSmsCode(HttpLink.BindRobotLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                sendMsgToRobotAdmin(robotDeviceId, App.getInstance().getPhone());
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    Dialog sexDialog;
    TextView tv_man;
    TextView tv_women;

    private void initDialog() {
        //性别dialog
        sexDialog = DialogUtils.createDialogForContentViewFromBottom(mContext, R.layout.dialog_picker_sex, 0, 0);
        tv_man = (TextView) sexDialog.findViewById(R.id.tv_man);
        tv_women = (TextView) sexDialog.findViewById(R.id.tv_women);
        sexDialog.findViewById(R.id.tv_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                mTvSex.setText("男");
                tv_man.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_women.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        sexDialog.findViewById(R.id.tv_women).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                mTvSex.setText("女");
                tv_women.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_man.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        sexDialog.findViewById(R.id.tv_cancel_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
            }
        });

    }

    ListView mListView;
    CommonLvAdapter<String> mStringCommonLvAdapter;
    PopupWindow mPopupWindow;

    //成员选择
    private void initPopWindow() {
        mPopupWindow = PopupWindowUtils.createTopPopStyle(mContext, R.layout.pop_roles, R.id.listview);
        mListView = (ListView) mPopupWindow.getContentView().findViewById(R.id.listview);
        mStringCommonLvAdapter = new CommonLvAdapter<String>(mContext, new ArrayList<String>(), R.layout.adapter_role_tv) {
            @Override
            public void convert(CommonLvViewHolder holder, final String bean, int position) {
                holder.setText(R.id.tv_role, bean);
                holder.setOnClickListener(R.id.ll_role_select, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEtChengyuan.setText(bean);
                        mEtChengyuan.setSelection(bean.length());
                        mPopupWindow.dismiss();
                    }
                });
            }
        };
        mListView.setAdapter(mStringCommonLvAdapter);

    }


    /**
     * 获取可用角色列表
     */
    private void getUsableRoles() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", App.getInstance().getUserInfo().getUserId() + "");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.GetUsableRoles, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RolesNameBean rolesNameBean = GsonUtils.getServerBean((String) response, RolesNameBean.class);
                if ("0000".equals(rolesNameBean.getCode())) {
                    if (rolesNameBean.getRoleList() != null && rolesNameBean.getRoleList()
                                                                            .size() > 0) {
                        mStringCommonLvAdapter.setDatas(rolesNameBean.getRoleList());
                    }
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    /**
     * 开始领养
     */
    private void startAdopt() {
        String chengYuan = mEtChengyuan.getText().toString();
        if (TextUtils.isEmpty(chengYuan)) {
            toastShow("成员不能为空");
            return;
        }
        String childNickname = mEtNicheng.getText().toString();
        if (TextUtils.isEmpty(childNickname)) {
            toastShow("小孩名称不能为空");
            return;
        }
        String childSex = mTvSex.getText().toString();
        if (TextUtils.isEmpty(childSex)) {
            toastShow("小孩性别不能为空");
            return;
        }
        String childYearold = mEtYearsOld.getText().toString();
        if (TextUtils.isEmpty(childYearold)) {
            toastShow("小孩年龄不能为空");
            return;
        }
        if (qrCode.contains("zige:")) { // 管理员
            // zige:cb3e347e851b2e74
            bindAdminRobotQuest(chengYuan, childNickname, childSex, childYearold, qrCode.substring(5), 1);
        } else if (qrCode.contains("zige_user:")) { // 新用户
            //zige_user:131********/fdsafweffdaswqwef
            String hostId = qrCode.substring(qrCode.indexOf(":") + 1, qrCode.indexOf("/")); //管理员id
            String robotDeviceId = qrCode.substring(qrCode.indexOf("/") + 1); //机器人id
            String userId = App.getInstance().getUserInfo().getUserId() + "";
            String deviceKey = SystemUtils.getDeviceKey();
            String jsonData
                    = "{\"childNickname\": \"" + childNickname + "\",\"childSex\": \"" + childSex + "\",\"childYearold\": \"" + childYearold + "\",\"userId\": \"" + userId + "\",\"nickname\": \"" + chengYuan + "\",\"robotDeviceId\": \"" + robotDeviceId + "\"}";
            sendMsgToHost("guestMsg", childNickname, childSex, childYearold, userId, chengYuan, robotDeviceId, deviceKey, hostId);
            Log.v("jsonData  ", jsonData);
        } else {

        }

    }


    @OnClick({R.id.rl_pull_roles, R.id.ll_sex_click, R.id.btn_cancel, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_pull_roles:
                mPopupWindow.showAsDropDown(mLlChengyuanClick);
                break;
            case R.id.ll_sex_click:
                sexDialog.show();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_ok:
                if (!bQrCodeOk) {
                    toastShow("二维码不正确");
                    return;
                }
                startAdopt();
                break;
        }
    }
}
