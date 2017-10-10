package com.zige.robot.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.Toast;

import com.zige.robot.App;

/**
 * Created by Administrator on 2017/5/12.
 * fragment基类
 */

public class BaseFragment extends Fragment {

    protected  Context mContext;
    protected App mApp;
    protected  static Toast mToast;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = App.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mToast !=null){
            mToast.cancel();
        }
    }


    protected void toastShow(String message)
    {
        if (mContext == null )
        {
            return;
        }
        if(null==mToast)
        {
            mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }else
        {
            mToast.setText(message);
        }
        mToast.show();

    }

    protected  <T> void startActivity(Class<T> class1)
    {
        startActivity(new Intent(mContext, class1));
    }



}
