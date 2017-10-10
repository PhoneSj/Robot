package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.ClockListModel;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 * 提醒
 */

public class MentoRemindActivity extends BaseActivity {
    private int ADD_REMIND = 1;//添加
    private int EDIT_REMIND = 2;//编辑

    CommonLvAdapter<ClockListModel.ClockListBean> commonLvAdapterVoice;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_mento_remind;
    }

    ListView listview;
    List<ClockListModel.ClockListBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("闹钟");
        setIvActionbg(R.drawable.english_add_on);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, RemindEditActivity.class), ADD_REMIND); //添加提醒
            }
        });
        listview = (ListView) findViewById(R.id.listview);

        commonLvAdapterVoice = new CommonLvAdapter<ClockListModel.ClockListBean>(mContext, mList, R.layout.item_robot_remind) {

            @Override
            public void convert(CommonLvViewHolder holder, final ClockListModel.ClockListBean bean, final int position) {
                String hour = bean.getClockTimeHour()<10?("0"+bean.getClockTimeHour()):bean.getClockTimeHour()+"";
                String min = bean.getClockTimeMin()<10?("0"+bean.getClockTimeMin()):bean.getClockTimeMin()+"";
                holder.setText(R.id.item_robot_remind_time,hour+":"+min);
                holder.setText(R.id.item_robot_remind_week, weekToStr(bean.getRepeat()));
                holder.setText(R.id.item_robot_remind_member, "成员：" + App.getInstance().getUserInfo().getNickname());
                holder.setText(R.id.item_robot_remind_content, "内容：" + bean.getContent());
                holder.getView(R.id.item_robot_remind_img).setTag(position);
                //闹钟状态
                if (bean.getStatus() == 0) {
                    holder.setTextColor(R.id.item_robot_remind_time, getResources().getColor(R.color.color_fe5365));
                    holder.setImageResoure(R.id.item_robot_remind_img, R.drawable.english_on);
                } else {
                    holder.setTextColor(R.id.item_robot_remind_time, getResources().getColor(R.color.tv_777));
                    holder.setImageResoure(R.id.item_robot_remind_img, R.drawable.english_off);
                }
                //闹钟操作按钮
                holder.setOnClickListener(R.id.item_robot_remind_img, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getStatus()==0){//关闭
                            closeClock(position, "1");
                        }else {//打开
                            closeClock(position, "0");
                        }
                    }
                });
            }
        };
        listview.setAdapter(commonLvAdapterVoice);
        listview.setOnItemClickListener(onItemClickListener);
        getClock();
    }

    /**
     * 获取闹钟
     */
    private void getClock(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"" );
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.GetClock, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                ClockListModel clockListModel = GsonUtils.getServerBean((String) response, ClockListModel.class);
                if("0000".equals(clockListModel.getCode())){
                    if(clockListModel.getClockList()!=null && clockListModel.getClockList().size() >0){
                        commonLvAdapterVoice.setDatas(clockListModel.getClockList());
                    }
                }else {
                    toastShow(clockListModel.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    /**
     * 获取闹钟
     */
    private void closeClock(final int position, final String close){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"" );
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("clockId", commonLvAdapterVoice.getItem(position).getId()+"");
        map.put("close",close); //0 开启 1 关闭
        VRHttp.sendRequest(mContext, HttpLink.CloseClock, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode cBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(cBaseCode.getCode())){
                    if("0".equals(close)){
                        toastShow("打开闹钟成功");
                    }else {
                        toastShow("关闭闹钟成功");
                    }
                    commonLvAdapterVoice.getItem(position).setStatus(Integer.parseInt(close));
                    commonLvAdapterVoice.notifyDataSetChanged();
                }else {
                    toastShow(cBaseCode.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }


    private String weekToStr(String str) {
        if (str.contains(",")) {
            String day[] = str.split(",");
            List<String> selectWeekList = new ArrayList<>();
            for (String s : day) {
                selectWeekList.add(s);
            }
            Collections.sort(selectWeekList, Collator.getInstance(Locale.CHINA));
            if (selectWeekList.size() == 7) {
                return "每天";
            } else {
                String dayStr = "";
                for (int i = 0; i < selectWeekList.size(); i++) {
                    dayStr += " " + numberToStr(selectWeekList.get(i));
                }
                if (dayStr.equals(" 一 二 三 四 五")) {
                    return "工作日";
                } else if (dayStr.equals(" 六 日")) {
                    return "周末";
                }
                return "周" + dayStr;
            }
        } else {
            return "周 " + numberToStr(str);
        }
    }

    private String numberToStr(String str) {
        switch (str) {
            case "1":
                return "一";
            case "2":
                return "二";
            case "3":
                return "三";
            case "4":
                return "四";
            case "5":
                return "五";
            case "6":
                return "六";
            case "7":
                return "日";
        }
        return "";
    }

    int select_position;
    ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            select_position = position;
            startActivityForResult(new Intent(mContext, RemindEditActivity.class)
                    .putExtra("clockId",commonLvAdapterVoice.getItem(position).getId())
                    .putExtra("json",GsonUtils.getObjectToJson(commonLvAdapterVoice.getItem(position))), EDIT_REMIND); //添加提醒
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == ADD_REMIND) { //增加提醒
            getClock();
        } else if(requestCode == EDIT_REMIND){ //编辑提醒
            commonLvAdapterVoice.removeItemFromPositon(select_position);
        }
    }

}
