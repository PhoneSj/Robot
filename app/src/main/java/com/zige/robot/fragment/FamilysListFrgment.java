package com.zige.robot.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseLazyFragment;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.FamilyFriendListBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.interf.DialogListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zige.robot.R;

/**
 * Created by zhanghuan on 2016/12/31.
 * 朋友列表
 */

public class FamilysListFrgment extends BaseLazyFragment {

    ListView listview;
    CommonLvAdapter<FamilyFriendListBean.FamilyListBean> commonLvAdapter;
    Dialog delDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_friends, container, false);
        listview = (ListView) view.findViewById(R.id.listview);
        initData();
        initDialog();
        isPrepared = true;
        loadData();
        return view;
    }


    private void initDialog(){
        delDialog = DialogUtils.createDelDialog(mContext, new DialogListener() {
            @Override
            public void actionClick() {
//                if(commonLvAdapter.getItem(select_pos).getUserId()==App.getInstance().getUserInfo().getUserId()){
//                    showToast("不能删除自己");
//                    return;
//                }
                delFamily(commonLvAdapter.getItem(select_pos).getUserId()+"");
            }
        });
        ((TextView)delDialog.findViewById(R.id.tv_click)).setText("删除");
    }

    int select_pos = 0;
    private void initData(){
        commonLvAdapter = new CommonLvAdapter<FamilyFriendListBean.FamilyListBean>(mContext, new ArrayList<FamilyFriendListBean.FamilyListBean>(), R.layout.adapter_friend) {
            @Override
            public void convert(CommonLvViewHolder holder, FamilyFriendListBean.FamilyListBean bean, final int position) {
                holder.setText(R.id.tv_role, bean.getNickname());
                holder.setText(R.id.tv_phone, bean.getUsername());
            }
        };
        listview.setAdapter(commonLvAdapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                select_pos = position;
                delDialog.show();
                return true;
            }
        });
    }


    private void getDataForNet(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.FamilyList, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                FamilyFriendListBean friendListBean = GsonUtils.getServerBean((String) response, FamilyFriendListBean.class);
                if("0000".equals(friendListBean.getCode())){
                    if(friendListBean.getFamilyList()!=null && friendListBean.getFamilyList().size() >0){
                        commonLvAdapter.setDatas(friendListBean.getFamilyList());
                    }else {
                        commonLvAdapter.clearAll();
                    }
                }else {
                    showToast(friendListBean.getMessage());
                }
            }
        });
    }


    private void delFamily(String familyUserId){
        Map<String, String> map = new HashMap<String, String>();
        map.put("familyUserId", familyUserId);
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.DelFamily, map, new VRHttpListener() {
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

//    private void unBindRobot(final String userId){
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userId", userId);
//        map.put("deviceId", SystemUtils.getDeviceKey());
//        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
//        VRHttp.sendRequest(getActivity(), HttpLink.UnbindRobot, map, new VRHttpListener() {
//            @Override
//            public void onSuccess(Object response, boolean isCache) {
//                BaseCode baseCode  = GsonUtils.getServerBean((String) response, BaseCode.class);
////                if("0".equals(baseCode.code)){
//                    if(userId.equals(App.getInstance().getUserInfo().getUserId()+"") && "1".equals(App.getInstance().getUserInfo().getAdmin())){
//                        //如果自己是管理员解绑了，跳到登陆页面
//                        ServiceLogin.getInstance().sendTextMsg(App.getInstance().getUserInfo().getDeviceid(), "hostUnBind", new IMMsgListener() {
//                            @Override
//                            public void onSuccess(TIMMessage msg) {
//                                showToast("hostUnbind");
//                                startActivity(new Intent(mContext, MainActivity.class).putExtra("logOut",true));
//                            }
//
//                            @Override
//                            public void onError(int code, String desc) {
//
//                            }
//                        });
//                    }
//                    getDataForNet();
////                }
//
//            }
//
//            @Override
//            public void onError(String error) {
//                super.onError(error);
//            }
//        });
//    }

    @Override
    public void loadData() {
        if(isPrepared && isVisible){
            getDataForNet();
        }
    }
}
