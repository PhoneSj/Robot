package com.zige.robot.fsj.base;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.utils.DialogUtils;

/**
 * Created by PhoneSj on 2017/9/28.
 */

public abstract class StateActivity extends SimpleActivity {

    private Dialog dialog;

    protected void stateContent() {
        stateContent(null);
    }

    protected void stateContent(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showContentMsg(msg);
            }
        });
    }

    protected void stateLoading(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoadingMsg(msg);
            }
        });
    }

    protected void stateError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorMsg(msg);
            }
        });
    }

    protected void showContentMsg(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showLoadingMsg(String msg) {
        dialog = DialogUtils.createLoadingDialog(this, msg, false);
        dialog.show();
    }

    protected void showErrorMsg(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
