package com.zige.robot.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.permission.PermissionChecker;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;


/**
 * Created by zhanghuan on 2016/3/22.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    protected boolean isDestroy;
    protected FragmentActivity mContext;
    protected App mApplication;
    protected Handler mHandler;
    private Toast toast = null;

    // 统一的加载对话框
    protected Dialog mLoadingDialog;

    protected PermissionChecker mPermissionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initPageLayoutID());
        ButterKnife.bind(this);
        App.getInstance().addActivity(this);
//        StatusBarUtils.setColor(this, Color.parseColor("#000000"));
        init();
    }

    public void setTitleName(String titleName) {
        ((TextView) findViewById(R.id.tv_title_name)).setText(titleName);
    }

    public void setBackListener(View.OnClickListener listener) {
        findViewById(R.id.rl_back_return).setOnClickListener(listener);
    }

    public void setIvActionbg(int resId) {
        ((ImageView) findViewById(R.id.iv_action)).setBackgroundResource(resId);
    }

    public void setIvActionListener(View.OnClickListener listener) {
        findViewById(R.id.rl_action).setOnClickListener(listener);
    }

    public void setTvActionText(String actionStr) {
        findViewById(R.id.tv_action).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_action)).setText(actionStr);
    }

    public void setTvActionListener(View.OnClickListener listener) {
        findViewById(R.id.tv_action).setOnClickListener(listener);
    }


    protected void init() {
        mContext = this;
        mApplication = App.getInstance();
        mHandler = new MyHandler(this);
        mPermissionChecker = new PermissionChecker(this);
    }

    /**
     * 返回主布局id
     */
    protected abstract int initPageLayoutID();

    protected <T> void startActivity(Class<T> tClass) {
        startActivity(new Intent(this, tClass));
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        App.getInstance().finishActivity(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        dismissProgressDialog();
        super.onDestroy();
    }

    /**
     * 接收处理mHandler的消息
     */
    protected void performHandleMessage(Message msg) {
    }

    public void toastShow(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(text);
        }
        toast.show();
    }


    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity baseActivity = (BaseActivity) reference.get();
            if (!baseActivity.isDestroy) {
                baseActivity.performHandleMessage(msg);
            }
        }
    }

    public void showProgressDialog(String tipMsg, boolean cancelable) {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = DialogUtils.createLoadingDialog(mContext, tipMsg, cancelable);
            }
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
