package com.zige.robot.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.activity.LoginActivity;
import com.zige.robot.adapter.DeviceSwitchAdapter;
import com.zige.robot.base.BaseLazyFragment;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.LoginCode;
import com.zige.robot.bean.RobotDeviceListBean;
import com.zige.robot.fsj.Constants;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.interf.ActionClickListener;
import com.zige.robot.service.ServiceLogin;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.view.GalleryRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghuan on 2016/12/31.
 * 设备切换
 */

public class RobotSwitchFragment extends BaseLazyFragment {
    private GalleryRecyclerView mGalleryRecyclerView;
    private List<RobotDeviceListBean.RobotListBean> mlist = new ArrayList<>();
    private ImageView[] imageViews;
    private DeviceSwitchAdapter deviceSwitchAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        mGalleryRecyclerView = (GalleryRecyclerView) view.findViewById(R.id.viewPager);
        mGalleryRecyclerView.setCanAlpha(true);
        mGalleryRecyclerView.setCanScale(true);
        mGalleryRecyclerView.setBaseScale(0.85f);
        mGalleryRecyclerView.setBaseAlpha(0.85f);
        isPrepared = true;
        deviceSwitchAdapter = new DeviceSwitchAdapter(mContext, mlist, mGalleryRecyclerView);
        mGalleryRecyclerView.setAdapter(deviceSwitchAdapter);
        deviceSwitchAdapter.setOnItemClickListener(new DeviceSwitchAdapter.DeviceOnClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //切换绑定
                changeDevice(mlist.get(position).getRobotDeviceId());
            }
        });
        deviceSwitchAdapter.setOnItemUnbindClickListener(new DeviceSwitchAdapter.DeviceOnUnbindListener() {
            @Override
            public void onItemClickUnbindListener(int position) {
                showUnbindRobotDialog(position);
            }
        });
        return view;
    }

    Dialog unbindRobotDialog;

    private void showUnbindRobotDialog(final int position) {
        unbindRobotDialog = DialogUtils.createTipDialog(mContext, "确认解绑机器人", "是", "否", new ActionClickListener() {
            @Override
            public void clickLeft() {
                unbindRobotDialog.dismiss();
                unBindRobot(position);
            }

            @Override
            public void clickRight() {
                unbindRobotDialog.dismiss();
            }
        });
        unbindRobotDialog.show();
    }


    /**
     * 获取机器人列表
     */
    private void getRobotDeviceList() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(App.getInstance().getUserInfo().getUserId()));
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.RobotDeviceList, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RobotDeviceListBean bean = GsonUtils.getServerBean((String) response, RobotDeviceListBean.class);
                if ("0000".equals(bean.getCode())) {
                    if (bean.getRobotList() != null && bean.getRobotList().size() > 0) {
                        mlist.clear();
                        mlist.addAll(bean.getRobotList());
                        deviceSwitchAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(bean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    /**
     * 切换绑定
     */
    private void changeDevice(final String robotDeviceId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(App.getInstance().getUserInfo().getUserId()));
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", robotDeviceId);
        VRHttp.sendRequest(mContext, HttpLink.ChangeDevice, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                LoginCode loginCode = GsonUtils.getServerBean((String) response, LoginCode.class);
                if ("0000".equals(loginCode.code) || "0".equals(loginCode.code)) {
                    if (loginCode.robot != null) {
                        showToast("切换设备成功！");
                        deviceSwitchAdapter.notifyDataSetChanged();
                        App.getInstance().setUserInfo(loginCode.robot);
                        // TODO: 2017/9/15  将绑定的robotId和机器人账号存入sp中
                        SharedPreferencesUtils.saveRobotIdToSP(robotDeviceId);
                        SharedPreferencesUtils.saveConnectedHXContact("r" + robotDeviceId);
                    }

                } else {
                    showToast(loginCode.message);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    private void unBindRobot(final int postion) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(App.getInstance().getUserInfo().getUserId()));
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(getActivity(), HttpLink.UnbindRobot, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if ("0000".equals(CBaseCode.getCode()) || "0".equals(CBaseCode.getCode())) {
                    showToast("解绑成功！");
                    if (mlist.size() == 1) {
                        SharedPreferencesUtils.saveValue(mContext, SharedPreferencesUtils.auto_login, "0");
                        mContext.stopService(new Intent(mContext, ServiceLogin.class));//销毁后台服务
                        App.getInstance().finishAllActivity();
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        // TODO: 2017/9/13 删除robotId
                        SharedPreferences mSP = App.getInstance()
                                .getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
                        mSP.edit().putString(Constants.SP_ROBOT_ID, null).apply();
                    } else {
                        mlist.remove(postion);
                        deviceSwitchAdapter.notifyDataSetChanged();

                    }
                } else {
                    showToast(CBaseCode.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    @Override
    public void loadData() {
        if (isPrepared && isVisible) {
            getRobotDeviceList();
        }
    }


}
