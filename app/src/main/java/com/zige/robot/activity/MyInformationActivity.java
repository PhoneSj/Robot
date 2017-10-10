package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lidingwei on 2017/5/6 0006.
 * 我的
 */
public class MyInformationActivity extends BaseActivity {
    @BindView(R.id.rl_back_return)
    RelativeLayout rlBackReturn;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_info_click)
    RelativeLayout rlInfoClick;
    @BindView(R.id.rl_qr_code)
    RelativeLayout rlQrCode;
    @BindView(R.id.tv_connect_robot)
    TextView tvConnectRobot;
    @BindView(R.id.rl_tu_cao)
    RelativeLayout rlTuCao;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_my_infotmation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTitleName("我的");
        initDialog();
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPhone.setText("账号：" + mApplication.getPhone());
        if ("1".equals(mApplication.getUserInfo().getAdmin())) {
            tvNick.setText("主人：" + mApplication.getUserInfo().getNickname());
            rlQrCode.setVisibility(View.VISIBLE);
        } else {
            tvNick.setText("家人：" + mApplication.getUserInfo().getNickname());
            rlQrCode.setVisibility(View.GONE);
        }
        tvConnectRobot.setText(mApplication.getUserInfo().getChild_nickname());

    }

    Dialog dialog;
    TextView tv_dialog_title;
    EditText et_import_content;
    Button btn_back;
    Button btn_add;

    private void initDialog() {
        dialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_add_order, ScreenUtils.dp2px(mContext, 20), ScreenUtils.dp2px(mContext, 20));
        et_import_content = (EditText) dialog.findViewById(R.id.et_import_content);
        tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        btn_back = (Button) dialog.findViewById(R.id.btn_back);
        btn_add = (Button) dialog.findViewById(R.id.btn_add);
        tv_dialog_title.setText("吐槽");
        et_import_content.setHint("");
        btn_back.setText("取消");
        btn_add.setText("发送");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.dismiss();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_import_content.getText().toString();
                //发送
                if (TextUtils.isEmpty(str)) {
                    toastShow("请输入文字内容");
                    return;
                }
                dialog.dismiss();
                sendFeedBack(str);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            tvNick.setText("主人：" + mApplication.getUserInfo().getNickname());
        }
    }

    private void sendFeedBack(String content) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userPhone", App.getInstance().getPhone());
        map.put("content", content);
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.Feedback, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode cBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if ("0000".equals(cBaseCode.code) || "0".equals(cBaseCode.code)) {
                    toastShow("发送成功");
                } else {
                    toastShow(cBaseCode.message);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }


    @OnClick({R.id.rl_info_click, R.id.rl_qr_code, R.id.rl_tu_cao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_info_click: //更改个人信息
                startActivityForResult(new Intent(mContext, ChangeMyInfoActivity.class), 1);
                break;
            case R.id.rl_qr_code: //生成二维码
                startActivity(MyQrCodeActivity.class);
                break;
            case R.id.rl_tu_cao: //吐槽
                dialog.show();
                et_import_content.setText("");
                break;
        }
    }
}