package com.zige.robot.fsj.ui.pay.activity;

import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.base.SimpleActivity;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.PayResultBean;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.fsj.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/9/9.
 */

public class PayResultActivity extends StateActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.iv_state_icon)
    ImageView ivStateIcon;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initEventAndData() {
        setToolbar(toolBar, "支付结果");
        getPayResultData();
        stateLoading("正在从网络获取支付结果");
    }

    private void getPayResultData() {
        // TODO: 2017/9/13
        String orderNo = "";
        HttpConfig.getInstance()
                  .getApi()
                  .getPayResult(orderNo)
                  .compose(RxUtil.<CallHttpResponse<PayResultBean>>rxSchedulerHelper())
                  .compose(RxUtil.<PayResultBean>handleCallResult())
                  .subscribe(new Consumer<PayResultBean>() {
                      @Override
                      public void accept(PayResultBean payResultBean) throws Exception {
                          stateContent();
                          if (payResultBean.getStatus() == PayResultBean.STATE_SUCCESS) {
                              tvMoney.setText("金额：" + payResultBean.getFee() + "元 支付成功");
                              tvTime.setText("正在为您充值视屏通话" + payResultBean.getChargeTime() + "分钟时长套餐\n充值时长将于5分钟后到账");
                              ivStateIcon.setImageResource(R.drawable.rec_success_ico);
                              btnOk.setTextColor(Resources.getSystem()
                                                          .getColor(R.color.vm_green_87));
                              btnOk.setText("确定");
                          } else {
                              tvMoney.setText("支付失败");
                              tvTime.setText("本次交易未完成\n如需充值请重新支付");
                              ivStateIcon.setImageResource(R.drawable.rec_error_ico);
                              btnOk.setTextColor(Resources.getSystem()
                                                          .getColor(R.color.vm_black_87));
                              btnOk.setText("返回");
                          }
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
//                          ToastUtil.shortShow("获取支付结果失败");
//                          LogUtil.showE("获取支付结果失败");
                          stateError("获取支付结果失败");
                      }
                  });
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void stateError(String msg) {
        super.stateError(msg);
        SnackbarUtil.showShort(layoutRoot, msg);
    }
}
