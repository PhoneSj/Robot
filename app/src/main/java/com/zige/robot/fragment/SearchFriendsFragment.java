package com.zige.robot.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.activity.SearchRobotFriendActivity;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseLazyFragment;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.RobotFriendListBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.interf.DialogListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.zige.robot.R;

/**
 * Created by Administrator on 2017/5/12.
 * 搜索朋友
 */

public class SearchFriendsFragment extends BaseLazyFragment {


    @BindView(R.id.listview)
    ListView mListview;
    Unbinder unbinder;
     CommonLvAdapter<RobotFriendListBean.FriendListBean> mFriendListBeanCommonLvAdapter;
    Dialog mDialog;
    int select_pos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friend, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        isPrepared = true;
        initDialog();
        loadData();
        return view;
    }


    private void delFriend(String friendId){
        Map<String, String> map = new HashMap<String, String>();
        map.put("friendId", friendId);
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.DelFriend, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                getDataForNet();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                showToast(error);
            }
        });
    }

    private void initDialog(){
        mDialog = DialogUtils.createDelDialog(mContext, new DialogListener() {
            @Override
            public void actionClick() {
                mDialog.dismiss();
                delFriend(mFriendListBeanCommonLvAdapter.getItem(select_pos).getFriendId()+"");
            }
        });
    }

    private void initData() {
        mFriendListBeanCommonLvAdapter = new CommonLvAdapter<RobotFriendListBean.FriendListBean>(mContext, new ArrayList<RobotFriendListBean.FriendListBean>(), R.layout.adapter_robot_friend) {
            @Override
            public void convert(CommonLvViewHolder holder, RobotFriendListBean.FriendListBean bean, int position) {
                holder.setText(R.id.tv_name, bean.getFriendName());
                holder.setText(R.id.tv_accout, bean.getFriendPhone());
            }
        };
        mListview.setAdapter(mFriendListBeanCommonLvAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_pos = position;
                mDialog.show();
            }
        });
    }


    private void getDataForNet() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", App.getInstance().getUserInfo().getUserId() + "");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.FriendList, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RobotFriendListBean mRobotFriendListBean = GsonUtils.getServerBean((String) response, RobotFriendListBean.class);
                if ("0000".equals(mRobotFriendListBean.getCode())) {
                     if(mRobotFriendListBean.getFriendList()!=null && mRobotFriendListBean.getFriendList().size() >0){
                         mFriendListBeanCommonLvAdapter.setDatas(mRobotFriendListBean.getFriendList());
                     }else {
                         mFriendListBeanCommonLvAdapter.clearAll();
                     }
                } else {
                    showToast(mRobotFriendListBean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                showToast(error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.tv_search_click, R.id.iv_qr_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search_click:
                startActivity(new Intent(mContext, SearchRobotFriendActivity.class));
                break;
        }
    }

    @Override
    public void loadData() {
        if(isPrepared && isVisible){
            getDataForNet();
        }
    }
}
