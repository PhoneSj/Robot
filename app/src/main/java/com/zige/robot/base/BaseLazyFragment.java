package com.zige.robot.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.Toast;

import com.zige.robot.utils.permission.PermissionChecker;

/**
 * @author Feel on 2017/4/13 09:50
 */

public abstract class BaseLazyFragment extends Fragment {

    public Activity mContext;
    private Toast mToast;

    protected PermissionChecker mPermissionChecker;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mPermissionChecker = new PermissionChecker(activity);
    }

    // fragment 是否初始化完成
    protected boolean isPrepared = false;
    //fragment是否可见
    protected boolean isVisible = false;
    //是否已经加载过网络数据
    protected boolean isFirstData = false;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        {
            if (getUserVisibleHint()){
                isVisible = true;
                loadData();
            }else {
                isVisible = false;
            }
        }
    }

    /**
     * 可见状态加载数据
     */
    public abstract void loadData();

    public void showToast(String message)
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

}
