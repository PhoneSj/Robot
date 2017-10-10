package com.zige.robot.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.HXManager;
import com.zige.robot.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/26.
 * 主人获取绑定信息
 */

public class DialogBindActivity extends BaseActivity {


    @BindView(R.id.tv_click_left)
    TextView mTvClickLeft;
    @BindView(R.id.tv_click_right)
    TextView mTvClickRight;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.ll_btn)
    LinearLayout mLlBtn;


    String userId;
    String guestId;
    String robotDeviceId;
    String nickname;
    String childNickname;
    String childSex;
    String childYearold;



    @Override
    protected int initPageLayoutID() {
        return R.layout.dialog_tip;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);// 点击屏幕外边不消失
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.drawable.drwable_black);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        nickname = getIntent().getStringExtra("nickname");
        childNickname = getIntent().getStringExtra("childNickname");
        childSex = getIntent().getStringExtra("childSex");
        childYearold = getIntent().getStringExtra("childYearold");
        guestId = getIntent().getStringExtra("guestId");
        robotDeviceId = getIntent().getStringExtra("robotDeviceId");
        mTvTip.setText("【"+ guestId+"】" +"\n想绑定您的机器人\n是否同意？");
        mTvClickLeft.setText("接受");
        mTvClickRight.setText("拒绝");
    }

    /**
     * 绑定机器人
     */
    private void bindRobotQuest() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("nickname", nickname);
        map.put("childNickname", childNickname);
        map.put("childSex", childSex);
        map.put("childYearold", childYearold);
        map.put("admin", "0");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", robotDeviceId); //机器人id
        VRHttp.sendRequest(mContext, HttpLink.BindRobotLink, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if ("0000".equals(CBaseCode.getCode()) || "0".equals(CBaseCode.getCode())) {
                    sendMsgHostState("bindOk", robotDeviceId, guestId); //告诉客人我作为主人身份同意绑定了
                } else {
                    toastShow(CBaseCode.getMessage());
                    finish();
                }

            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    /**
     * 发送消息给客人
     * @param msgContent
     * @param guestId
     */
    private void sendMsgHostState(String msgContent, String robotDeviceId , final String guestId) {
        if (TextUtils.isEmpty(msgContent)) {
            return;
        }
        if (TextUtils.isEmpty(robotDeviceId)) {
            return;
        }
//        ServiceLogin.getInstance().sendTextMsg(guestId, msgContent, new IMMsgListener() {
//            @Override
//            public void onSuccess(TIMMessage msg) {
//                finish();
//            }
//
//            @Override
//            public void onError(int code, String desc) {
//                toastShow(desc);
//                finish();
//            }
//        });
        Map<String ,String> map = new HashMap<>();
        map.put("action",msgContent);
        map.put("account",robotDeviceId);
        HXManager.sendToHost(guestId, 4, msgContent,map, new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onError(int i, String s) {
                toastShow(s);
                finish();
            }

            @Override
            public void onProgress(int i, String s) {
                Toast.makeText(mContext, "正在发送", Toast.LENGTH_SHORT).show();
            }
        });
        
    }


    @OnClick({R.id.tv_click_left, R.id.tv_click_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_click_left: //同意
                bindRobotQuest();
                break;
            case R.id.tv_click_right: //拒绝
                sendMsgHostState("bindNo:", robotDeviceId, guestId);  //告诉客人我作为主人身份拒绝绑定
                break;
        }
    }
}
