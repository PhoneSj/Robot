package com.zige.robot.fsj.ui.call.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.zige.robot.R;
import com.zige.robot.fsj.model.bean.CallInfoBean;
import com.zige.robot.fsj.model.bean.CallRemainTimeBean;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.ui.call.base.CallManager;
import com.zige.robot.fsj.ui.call.base.VMBaseActivity;
import com.zige.robot.fsj.ui.pay.activity.PayActivity;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/9/7.
 */

public class MainCallActivity extends VMBaseActivity {

    public static final int REQUEST_CODE_CALL_MAIN = 0;

    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.iv_call_main_robot)
    ImageView ivCallMainRobot;
    @BindView(R.id.btn_call_main_vedio)
    Button btnCallMainVedio;
    @BindView(R.id.btn_call_main_more)
    Button btnCallMainMore;
    @BindView(R.id.tv_call_main_desc)
    TextView tvCallMainDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        checkPermissions();在主页面申请权限
        getRemainTime();
    }

    private void getRemainTime() {
        String robotId = SharedPreferencesUtils.getRobotIdFromSP();
        HttpConfig.getInstance()
                  .getApi()
                  .getCallRemainTime(robotId)
                  .compose(RxUtil.<CallHttpResponse<CallRemainTimeBean>>rxSchedulerHelper())
                  .compose(RxUtil.<CallRemainTimeBean>handleCallResult())
                  .subscribe(new Consumer<CallRemainTimeBean>() {
                      @Override
                      public void accept(CallRemainTimeBean callRemainTimeBean) throws Exception {
                          tvCallMainDesc.setText("可用通话剩余" + callRemainTimeBean.getRemain() + "分钟");
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
//                          ToastUtil.shortShow("获取剩余通话失败");
//                          LogUtil.showE("获取剩余通话失败");
                          stateError("获取剩余通话失败");
                      }
                  });
        stateContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_call_main;
    }

    @Override
    protected void initEventAndData() {
        setToolbar(toolBar, "视频通话");
    }

//    }

    @OnClick({R.id.btn_call_main_vedio, R.id.btn_call_main_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_call_main_vedio:
                // TODO: 2017/9/7 视屏通话前需要登录环信账号
                callVideo();
                break;
            case R.id.btn_call_main_more:
                // TODO: 2017/9/7 进入充值页面
                Intent intent = new Intent(this, PayActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void callVideo() {
        Intent vedioIntent = new Intent(MainCallActivity.this, VideoCallActivity.class);
        // TODO: 2017/9/7 会话id写死
//                String contact = "ldw004";
//                String contact = "r75a58e603b5d8c86";
        String contact = SharedPreferencesUtils.getConnectedHXContact();//机器人的环信账号
        CallManager.getInstance().setChatId(contact);
        CallManager.getInstance().setInComingCall(false);
        CallManager.getInstance().setCallType(CallManager.CallType.VIDEO);
        startActivityForResult(vedioIntent, REQUEST_CODE_CALL_MAIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int min = SharedPreferencesUtils.getConsumeTime();
        if (min > 0) {
            //视频通话正常结束
            // TODO: 2017/9/22
            Intent intent = new Intent(this, EndCallActivity.class);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallEvent(EMMessage msg) {
        int callTime = msg.getIntAttribute("callTime", 0);
        int min = (callTime + 59) / 60;//接通后有时长至少到未1s
        CallInfoBean bean = new CallInfoBean();
        bean.setCallerId(msg.getFrom());
        bean.setCalledId(msg.getTo().substring(1));//去除前缀r
        bean.setConsumeTime(min);
        bean.setId(msg.getMsgId());
        SharedPreferencesUtils.saveCallMessage(bean);//保存到sp中
    }
}
