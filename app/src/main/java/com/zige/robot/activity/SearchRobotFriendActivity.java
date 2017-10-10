package com.zige.robot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.SearchRobotFriendBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/12.
 * 搜索朋友
 */

public class SearchRobotFriendActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.listview)
    ListView mListview;

    CommonLvAdapter<SearchRobotFriendBean.SearchListBean> mSearchListBeanCommonLvAdapter;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_search_robot_friend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("搜索朋友");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    String searchKey = mEtSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(searchKey)) {
                        toastShow("请输入手机号码");
                        return true;
                    }
                    if (!SystemUtils.isMobileNO(searchKey)) {
                        toastShow("手机号码不正确！");
                        return true;
                    }
                     getDataForNet(searchKey);

                    return true;
                }
                return false;
            }
        });

        mSearchListBeanCommonLvAdapter = new CommonLvAdapter<SearchRobotFriendBean.SearchListBean>(mContext, new ArrayList<SearchRobotFriendBean.SearchListBean>(), R.layout.adapter_robot_friend) {
            @Override
            public void convert(CommonLvViewHolder holder, SearchRobotFriendBean.SearchListBean bean, int position) {
                holder.setText(R.id.tv_name, bean.getFriendName());
                holder.setViewVisibility(R.id.tv_accout, View.GONE);
            }
        };
        mListview.setAdapter(mSearchListBeanCommonLvAdapter);
        mListview.setOnItemClickListener(this);

    }

    private void getDataForNet(String searchKey) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", mApplication.getUserInfo().getUserId() + "");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", mApplication.getUserInfo().getDeviceid());
        map.put("phone", searchKey);
        VRHttp.sendRequest(mContext, HttpLink.SearchFriend, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                SearchRobotFriendBean mSearchRobotFriendBean = GsonUtils.getServerBean((String) response, SearchRobotFriendBean.class);
                if ("0000".equals(mSearchRobotFriendBean.getCode())) {
                    if (mSearchRobotFriendBean.getSearchList() != null && mSearchRobotFriendBean.getSearchList().size() > 0) {
                        List<SearchRobotFriendBean.SearchListBean> searchListBeenList = new ArrayList<SearchRobotFriendBean.SearchListBean>();
                        for (SearchRobotFriendBean.SearchListBean searchListBean : mSearchRobotFriendBean.getSearchList()) {
                            if (searchListBean.getAddStatus() == 0) {
                                //未添加的
                                searchListBeenList.add(searchListBean);
                            }
                        }
                        mSearchListBeanCommonLvAdapter.setDatas(searchListBeenList);
                    } else {
                        mSearchListBeanCommonLvAdapter.clearAll();
                    }
                } else {
                    toastShow(mSearchRobotFriendBean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    /**
     * 添加机器人好友
     *
     * @param friendId
     */
    private void addRobotFriend(String friendId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", mApplication.getUserInfo().getUserId() + "");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", mApplication.getUserInfo().getDeviceid());
        map.put("friendId", friendId);
        VRHttp.sendRequest(mContext, HttpLink.AddFriend, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode mCBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                toastShow(mCBaseCode.getMessage());
                if ("0000".equals(mCBaseCode.getCode())) {
                    finish();
                } else {

                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1){
            String search_phone = data.getStringExtra("search_phone");
            getDataForNet(search_phone);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //添加机器好友
        addRobotFriend(mSearchListBeanCommonLvAdapter.getItem(position).getFriendId() + "");
    }

    @OnClick(R.id.iv_qr_click)
    public void onViewClicked() {
        //扫一扫添加
         startActivityForResult(new Intent(mContext, CaptureActivity.class)
                .putExtra("type", 1), 1);
    }
}
