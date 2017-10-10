package com.zige.robot.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zige.robot.HttpLink;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.RolesNameBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.PopupWindowUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zige.robot.R;

/**
 * Created by lidingwei on 2017/5/6 0006.
 * 我的信息填写
 */
public class ChangeMyInfoActivity extends BaseActivity {

    @BindView(R.id.ll_chengyuan_click)
    LinearLayout llChengyuanClick;
    @BindView(R.id.et_nicheng)
    EditText etNicheng;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ll_sex_click)
    LinearLayout llSexClick;
    @BindView(R.id.et_years_old)
    EditText etYearsOld;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.et_chengyuan)
    EditText mEtChengyuan;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_change_my_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("信息编辑");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEtChengyuan.setText(mApplication.getUserInfo().getNickname());
        etNicheng.setText(mApplication.getUserInfo().getChild_nickname());
        tvSex.setText(mApplication.getUserInfo().getChild_sex());
        etYearsOld.setText(mApplication.getUserInfo().getChild_yearold());
        initDialog();
        initPopWindow();
    }

    ListView mListView;
    CommonLvAdapter<String> mStringCommonLvAdapter;
    PopupWindow mPopupWindow;
    private void initPopWindow() {
        mPopupWindow = PopupWindowUtils.createTopPopStyle(mContext, R.layout.pop_roles, R.id.listview);
        mListView = (ListView) mPopupWindow.getContentView().findViewById(R.id.listview);
        mStringCommonLvAdapter = new CommonLvAdapter<String>(mContext, new ArrayList<String>() ,R.layout.adapter_role_tv) {
            @Override
            public void convert(CommonLvViewHolder holder, final String bean, int position) {
                holder.setText(R.id.tv_role, bean);
                holder.setOnClickListener(R.id.ll_role_select, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEtChengyuan.setText(bean);
                        mPopupWindow.dismiss();
                    }
                });
            }
        };
        mListView.setAdapter(mStringCommonLvAdapter);
        getUsableRoles();

    }


    TextView tv_man;
    TextView tv_women;
    Dialog sexDialog;

    private void initDialog() {
        //性别dialog
        sexDialog = DialogUtils.createDialogForContentViewFromBottom(mContext, R.layout.dialog_picker_sex, 0, 0);
        tv_man = (TextView) sexDialog.findViewById(R.id.tv_man);
        tv_women = (TextView) sexDialog.findViewById(R.id.tv_women);
        sexDialog.findViewById(R.id.tv_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                tvSex.setText("男");
                tv_man.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_women.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        sexDialog.findViewById(R.id.tv_women).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                tvSex.setText("女");
                tv_women.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_man.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        if ("男".equals(tvSex.getText().toString())) {
            tv_man.setTextColor(getResources().getColor(R.color.color_fe5265));
        }
        if ("女".equals(tvSex.getText().toString())) {
            tv_women.setTextColor(getResources().getColor(R.color.color_fe5265));
        }
        sexDialog.findViewById(R.id.tv_cancel_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
            }
        });

    }

    /**
     * 获取可用角色列表
     */
    private void getUsableRoles(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", mApplication.getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", mApplication.getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.GetUsableRoles, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RolesNameBean rolesNameBean = GsonUtils.getServerBean((String) response, RolesNameBean.class);
                if("0000".equals(rolesNameBean.getCode())){
                    if(rolesNameBean.getRoleList()!=null && rolesNameBean.getRoleList().size() >0){
                        mStringCommonLvAdapter.setDatas(rolesNameBean.getRoleList());
                    }
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }


    /**
     * 获取可用角色列表
     */
    private void UpdateRobotInfoNet(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", mApplication.getUserInfo().getUserId()+"");
        map.put("nickname", chengYuan);
        map.put("childNickname",childNickname );
        map.put("childSex", childSex);
        map.put("childYearold", childYearold);
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", mApplication.getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.UpdateRobotInfo, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode CBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0".equals(CBaseCode.getCode())){
                    toastShow("信息修改成功");
                    mApplication.getUserInfo().setNickname(chengYuan);
                    mApplication.getUserInfo().setChild_nickname(childNickname);
                    mApplication.getUserInfo().setChild_sex(childSex);
                    mApplication.getUserInfo().setChild_yearold(childYearold);
                    setResult(RESULT_OK);
                    finish();
                }else {
                    toastShow(CBaseCode.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    String chengYuan;
    String childNickname;
    String childSex;
    String childYearold;
    @OnClick({R.id.ll_sex_click, R.id.btn_ok, R.id.rl_pull_roles})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sex_click:
                sexDialog.show();
                break;
            case R.id.rl_pull_roles: //下拉成员角色
                mPopupWindow.showAsDropDown(llChengyuanClick);
                break;
            case R.id.btn_ok:
                 chengYuan = mEtChengyuan.getText().toString();
                if (TextUtils.isEmpty(chengYuan)) {
                    toastShow("成员不能为空");
                    return;
                }
                 childNickname = etNicheng.getText().toString();
                if (TextUtils.isEmpty(childNickname)) {
                    toastShow("小孩名称不能为空");
                    return;
                }
                 childSex = tvSex.getText().toString();
                if (TextUtils.isEmpty(childSex)) {
                    toastShow("小孩性别不能为空");
                    return;
                }
                 childYearold = etYearsOld.getText().toString();
                if (TextUtils.isEmpty(childYearold)) {
                    toastShow("小孩年龄不能为空");
                    return;
                }
                UpdateRobotInfoNet();
                break;
        }
    }

}