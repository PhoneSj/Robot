package com.zige.robot.fsj.ui.pay.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.CallRemainTimeBean;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/9/9.
 */

public class PayActivity extends StateActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_available_time)
    TextView tvAvailableTime;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.view_main)
    LinearLayout viewMain;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    @Override
    protected int getLayout() {
        return R.layout.activity_pay;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pay_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pay_rescord:
                //调到充值记录界面
                Intent intent = new Intent(this, PayRecordActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initEventAndData() {
        setToolbar(toolBar, "充值");
        //// TODO: 2017/9/15 robotId后续改为从presenter中获取
//        String robotId = mPresenter.getRobotId();
        String robotId = SharedPreferencesUtils.getRobotIdFromSP();
        HttpConfig.getInstance()
                  .getApi()
                  .getCallRemainTime(robotId)
                  .compose(RxUtil.<CallHttpResponse<CallRemainTimeBean>>rxSchedulerHelper())
                  .compose(RxUtil.<CallRemainTimeBean>handleCallResult())
                  .subscribe(new Consumer<CallRemainTimeBean>() {
                      @Override
                      public void accept(CallRemainTimeBean callRemainTimeBean) throws Exception {
                          tvAvailableTime.setText("可用通话剩余" + callRemainTimeBean.getRemain() + "分钟");
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

    @OnClick(R.id.btn_scan)
    public void onViewClicked() {
        // TODO: 2017/9/9 调用微信支付宝借口支付
    }

    @Override
    protected void stateError(String msg) {
        super.stateError(msg);
        SnackbarUtil.showShort(layoutRoot, "获取剩余通话失败");
    }
}
