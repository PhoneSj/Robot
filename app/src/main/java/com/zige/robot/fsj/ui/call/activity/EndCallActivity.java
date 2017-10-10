package com.zige.robot.fsj.ui.call.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.base.SimpleActivity;
import com.zige.robot.fsj.model.bean.CallInfoBean;
import com.zige.robot.fsj.model.bean.CallRemainTimeBean;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.fsj.util.ToastUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * Created by Administrator on 2017/9/8.
 */

public class EndCallActivity extends SimpleActivity {


    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView_consume_time)
    TextView textViewConsumeTime;
    @BindView(R.id.textView_remain_time)
    TextView textViewRemainTime;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    private String callerId;
    private String calledId;
    private String callId;
    private int consumeTime = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_call_end;
    }

    @Override
    protected void initEventAndData() {
        CallInfoBean message = SharedPreferencesUtils.getCallMessage();
        callerId = message.getCallerId();
        calledId = message.getCalledId();
        callId = message.getId();
        consumeTime = SharedPreferencesUtils.getConsumeTime();
        textViewConsumeTime.setText("本次通话时长" + consumeTime + "分钟");
//        mPresenter.commitConsumeTime(callerId, calledId, consumeTime, callId);
        CallInfoBean bean = new CallInfoBean(callerId, calledId, consumeTime, callId);
        HttpConfig.getInstance()
                  .getApi()
                  .commitCallComsumeTime(bean)
                  .compose(RxUtil.<CallHttpResponse<CallRemainTimeBean>>rxSchedulerHelper())
                  .compose(RxUtil.<CallRemainTimeBean>handleCallResult())
                  .subscribe(new Consumer<CallRemainTimeBean>() {
                      @Override
                      public void accept(CallRemainTimeBean callRemainTimeBean) throws Exception {
                          textViewRemainTime.setText("可用通话剩余" + callRemainTimeBean.getRemain() + "分钟");
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          ToastUtil.shortShow("获取通话剩余失败");
                          LogUtil.showE("获取通话剩余失败");
                      }
                  });
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                Intent intent = new Intent(EndCallActivity.this, MainCallActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

//    @Override
//    public void showRemainTime(CallRemainTimeBean bean) {
//        textViewRemainTime.setText("可用通话剩余" + bean.getRemain() + "分钟");
//    }
//
//    @Override
//    public void showPerssionResult(boolean isGranted) {
//        //不用实现
//    }

}
