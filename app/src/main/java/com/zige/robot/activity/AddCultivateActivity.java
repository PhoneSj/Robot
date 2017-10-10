package com.zige.robot.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.tencent.TIMMessage;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.BaseStatusBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.interf.IMMsgListener;
import com.zige.robot.service.ServiceLogin;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 * 添加养成任务
 */

public class AddCultivateActivity extends BaseActivity {
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_add_cultivate;
    }

    GridView gridview;
    String[] taskStr = {"整理房间", "整理玩具","自己洗澡","自定义"};
    CommonLvAdapter<String> commonLvAdapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("添加任务");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridview = (GridView) findViewById(R.id.gridview);
        initDialog();

        commonLvAdapter = new CommonLvAdapter<String>(mContext, new ArrayList<String>(), R.layout.adapter_add_cultivate) {
            @Override
            public void convert(CommonLvViewHolder holder, String bean, int position) {
                holder.setText(R.id.tv_task_type2, bean);
                if(position ==0){
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.clean_room);
                }else if(position ==1){
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.tidy_toy);
                }else if(position ==2){
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.wash_self);
                }else{
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.cultivate_custom);
                }
            }
        };
        gridview.setAdapter(commonLvAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("自定义".equals(taskStr[position])){
                    et_import_content.setText("");
                    dialog.show();
                }else {
                    addDevelopeRecord(taskStr[position]);
                }
            }
        });
        List<String> data = new ArrayList<>();
        for (String s : taskStr) {
            data.add(s);
        }
        commonLvAdapter.setDatas(data);
    }

    EditText et_import_content;

    /**
     * 添加自定义养成任务dialog
     */
    private void initDialog(){
        dialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_add_cultivate, ScreenUtils.dp2px(mContext, 20), ScreenUtils.dp2px(mContext, 20));
        dialog.findViewById(R.id.rl_dissmiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加
                String etContent = et_import_content.getText().toString().trim();
                if(TextUtils.isEmpty(etContent)){
                    toastShow("请输入内容");
                    return;
                }
                addDevelopeRecord(etContent);
                dialog.dismiss();
            }
        });
        et_import_content = (EditText) dialog.findViewById(R.id.et_import_content);

    }



    /**
     *
     * @param content      自定义
     */
    private void addDevelopeRecord(String content){
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", App.getInstance().getPhone());
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("wordtext", content);
        map.put("deal", "0");
        map.put("rewardtype", "0");
        map.put("worktime", String.valueOf(System.currentTimeMillis()));
        VRHttp.sendRequest(mContext, HttpLink.AddCmcatlg, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                BaseStatusBean baseStatusBean = GsonUtils.getServerBean((String) response, BaseStatusBean.class);
                if("0000".equals(baseStatusBean.getCode()) || "0".equals(baseStatusBean.getCode())){
                    setResult(RESULT_OK);
                    ServiceLogin.getInstance().sendTextMsg(App.getInstance().getUserInfo().getDeviceid(), "notification_robot_cultivate", new IMMsgListener() {
                        @Override
                        public void onSuccess(TIMMessage msg) {
                            finish();
                        }

                        @Override
                        public void onError(int code, String desc) {
                            toastShow("发送养成消息失败");
                        }
                    });

                }else {
                    toastShow(baseStatusBean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }




}
