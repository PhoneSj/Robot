package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.BaseStatusBean;
import com.zige.robot.bean.GetInfByTimeDescResultVO;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zige.robot.R;

import static com.zige.robot.R.id.btn_ok;

/**
 * Created by Administrator on 2017/5/5.
 * 养成
 */

public class MentoCultivateActivity extends BaseActivity {
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_mento_cultivate;
    }

    ListView listview;
    CommonLvAdapter<GetInfByTimeDescResultVO.GetInfoByTimeDescVO> commonLvAdapter;
    Dialog delDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("养成");
        setIvActionbg(R.drawable.refresh);
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新养成记录
                getDataFromNet();
            }
        });
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加任务
                startActivityForResult(new Intent(mContext, AddCultivateActivity.class), 1);
            }
        });
        listview = (ListView) findViewById(R.id.listview);
        initDelDialog();
        getDataFromNet();
        commonLvAdapter = new CommonLvAdapter<GetInfByTimeDescResultVO.GetInfoByTimeDescVO>(mContext, new ArrayList<GetInfByTimeDescResultVO.GetInfoByTimeDescVO>(), R.layout.adapter_cultivate) {
            @Override
            public void convert(CommonLvViewHolder holder, GetInfByTimeDescResultVO.GetInfoByTimeDescVO bean, final int position) {
                holder.setText(R.id.tv_task_num, "任务："+ String.valueOf(position+1));
                holder.setText(R.id.tv_task_content, "任务："+bean.getWordtext() );
                if("0".equals(bean.getRewardtype())){
                    //未完成
                    holder.setBackgroundResource(R.id.iv_complete_state, R.drawable.complete_off);
                }else {
                    holder.setBackgroundResource(R.id.iv_complete_state, R.drawable.complete_on);
                }
                if("自己洗澡".equals(bean.getWordtext())){
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.wash_self);
                }else if("整理房间".equals(bean.getWordtext())){
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.clean_room);
                }else if("整理玩具".equals(bean.getWordtext())){
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.tidy_toy);
                }else {
                    holder.setBackgroundResource(R.id.iv_icon, R.drawable.cultivate_custom);
                }
                holder.getView(R.id.rl_click).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        select_bean = commonLvAdapter.getItem(position);
                        delDialog.show();
                        return true;
                    }
                });
            }
        };
        listview.setAdapter(commonLvAdapter);
    }


    GetInfByTimeDescResultVO.GetInfoByTimeDescVO select_bean;
    /**
     * 删除任务dialog
     */
    private void initDelDialog(){
        delDialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_del, ScreenUtils.dp2px(mContext, 20), ScreenUtils.dp2px(mContext, 20));
        delDialog.findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除指令
                delDevelopeRecord(select_bean);
                delDialog.dismiss();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            getDataFromNet();
        }
    }

    /**
     * 获取养成记录
     */
    private void getDataFromNet(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", App.getInstance().getPhone());
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("sType", "1"); // 1 养成记录  2 作业记录
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        VRHttp.sendRequest(mContext, HttpLink.GetInfByTimeDesc, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                GetInfByTimeDescResultVO getInfByTimeDescResultVO = GsonUtils.getServerBean((String) response, GetInfByTimeDescResultVO.class);
                if("0000".equals(getInfByTimeDescResultVO.getCode())){
                    if(getInfByTimeDescResultVO.getData()!=null&&getInfByTimeDescResultVO.getData().size() >0){
                        commonLvAdapter.setDatas(getInfByTimeDescResultVO.getData());
                    }else {
                        commonLvAdapter.clearAll();
                    }
                }else {
                    toastShow(getInfByTimeDescResultVO.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });


    }

    /**
     * 删除养成记录
     * @param bean
     */
    private void delDevelopeRecord (final GetInfByTimeDescResultVO.GetInfoByTimeDescVO bean) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", App.getInstance().getPhone());
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("id", bean.getId()); //
        VRHttp.sendRequest(mContext, HttpLink.DelCmcatlg, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                BaseStatusBean baseStatusBean = GsonUtils.getServerBean((String) response, BaseStatusBean.class);
                toastShow(baseStatusBean.getMessage());
                if("0000".equals(baseStatusBean.getCode()) || "0".equals(baseStatusBean.getCode())){
                    commonLvAdapter.removeItem(bean);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });


    }



}
