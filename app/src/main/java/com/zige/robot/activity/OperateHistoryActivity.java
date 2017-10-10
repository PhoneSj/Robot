package com.zige.robot.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.RunRecordListBean;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/6.
 * 运行历史
 */

public class OperateHistoryActivity extends BaseActivity {
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_operate_history;
    }


    CommonLvAdapter<RunRecordListBean.RecordListBean> commonLvAdapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("运行历史");
        setIvActionbg(R.drawable.refresh);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新历史
                getDataFromNet();
            }
        });
        listview = (ListView) findViewById(R.id.listview);

        commonLvAdapter = new CommonLvAdapter<RunRecordListBean.RecordListBean>(mContext, new ArrayList<RunRecordListBean.RecordListBean>(), R.layout.adapter_operate_history) {
            @Override
            public void convert(CommonLvViewHolder holder, RunRecordListBean.RecordListBean bean, int position) {
                holder.setText(R.id.tv_run_name, bean.getAppName());
                holder.setText(R.id.tv_start_time, timeStr2Str(bean.getStartTime()));
                holder.setText(R.id.tv_end_time, timeStr2Str(bean.getEndTime()));
            }
        };
        listview.setAdapter(commonLvAdapter);
        getDataFromNet();

    }


    private void getDataFromNet(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"");
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.RunRecordList, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                RunRecordListBean runRecordListBean = GsonUtils.getServerBean((String) response, RunRecordListBean.class);
                if("0000".equals(runRecordListBean.getCode())){
                    if(runRecordListBean.getRecordList() !=null && runRecordListBean.getRecordList().size() >0){
                        commonLvAdapter.setDatas(runRecordListBean.getRecordList());
                    }else {
                        commonLvAdapter.clearAll();
                    }
                }else {
                    toastShow(runRecordListBean.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    private   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private   SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 时间格式转换
     * @param time
     * @return
     */
    private  String timeStr2Str(String time){
        String resultStr="";
        try {
            Date date = sdf.parse(time);
            resultStr = sdf1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultStr;
    }
}
