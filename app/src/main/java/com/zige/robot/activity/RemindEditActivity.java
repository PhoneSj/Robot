package com.zige.robot.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.ClockListModel;
import com.zige.robot.bean.Pickers;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.view.PickerScrollView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/6.
 * 编辑提醒
 */

public class RemindEditActivity extends BaseActivity {
    @BindView(R.id.rl_back_return)
    RelativeLayout rlBackReturn;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.view_mon)
    View viewMon;
    @BindView(R.id.ll_mon)
    LinearLayout llMon;
    @BindView(R.id.view_tue)
    View viewTue;
    @BindView(R.id.ll_tue)
    LinearLayout llTue;
    @BindView(R.id.view_wed)
    View viewWed;
    @BindView(R.id.ll_wed)
    LinearLayout llWed;
    @BindView(R.id.view_thu)
    View viewThu;
    @BindView(R.id.ll_thu)
    LinearLayout llThu;
    @BindView(R.id.view_fri)
    View viewFri;
    @BindView(R.id.ll_fri)
    LinearLayout llFri;
    @BindView(R.id.view_sat)
    View viewSat;
    @BindView(R.id.ll_sat)
    LinearLayout llSat;
    @BindView(R.id.view_sun)
    View viewSun;
    @BindView(R.id.ll_sun)
    LinearLayout llSun;
    @BindView(R.id.ll_chengyuan)
    LinearLayout llChengyuan;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.remind_house)
    PickerScrollView remindHouse;
    @BindView(R.id.remind_minute)
    PickerScrollView remindMinute;
    @BindView(R.id.tv_mon)
    TextView tvMon;
    @BindView(R.id.tv_tue)
    TextView tvTue;
    @BindView(R.id.tv_wed)
    TextView tvWed;
    @BindView(R.id.tv_thu)
    TextView tvThu;
    @BindView(R.id.tv_fri)
    TextView tvFri;
    @BindView(R.id.tv_sat)
    TextView tvSat;
    @BindView(R.id.tv_sun)
    TextView tvSun;
    @BindView(R.id.tv_chengyuan)
    TextView mTvChengyuan;

    String weekBean[] = {"1", "2", "3", "4", "5", "6", "7"};


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_remind_edit;
    }


    List<String> selectWeekList = new ArrayList<>();
    String select_hour = "00";
    String select_minute = "00";
    String json;
    ClockListModel.ClockListBean selectClockListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTvActionText("储存");
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        json = getIntent().getStringExtra("json");
        initTimeView();
    }


    private void initTimeView() {
        mTvChengyuan.setText(App.getInstance().getUserInfo().getNickname()); //成员
        remindHouse.setData(createHours());
        remindMinute.setData(createMinutes());
        remindHouse.setSelected(select_hour);
        remindMinute.setSelected(select_minute);
        remindHouse.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                select_hour = pickers.getShowConetnt();
            }
        });
        remindMinute.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                select_minute = pickers.getShowConetnt();
            }
        });
        if(TextUtils.isEmpty(json)){
            setTitleName("添加提醒");
            btnDelete.setVisibility(View.GONE);
            findViewById(R.id.tv_action).setVisibility(View.VISIBLE);
        }else {
            selectClockListBean = GsonUtils.getServerBean(json, ClockListModel.ClockListBean.class);
            setTitleName("提醒详情");
            btnDelete.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_action).setVisibility(View.GONE);
            etContent.setText(selectClockListBean.getContent());
            etContent.setSelection(selectClockListBean.getContent().length());
            remindHouse.setSelected(selectClockListBean.getClockTimeHour()<10?("0"+selectClockListBean.getClockTimeHour()):selectClockListBean.getClockTimeHour()+"");
            remindMinute.setSelected(selectClockListBean.getClockTimeMin()<10?("0"+selectClockListBean.getClockTimeMin()):selectClockListBean.getClockTimeMin()+"");
            numberToStr(selectClockListBean.getRepeat().split(","));
        }

    }


    /**
     * 删除闹钟
     * @param clockId
     */
    private void deleteClock(String clockId){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"" );
        map.put("clockId", clockId);
        map.put("deviceId", SystemUtils.getDeviceKey());
        VRHttp.sendRequest(mContext, HttpLink.DeleteClock, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode cBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(cBaseCode.getCode())){
                    toastShow("删除闹钟成功");
                    setResult(RESULT_OK);
                    finish();
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

    /**
     * 设置闹钟
     * @param week 时间
     * @param content 闹钟内容
     */
    private void setClock(String week, String content){
        Map<String, String> map = new HashMap<String, String>();
        map.put("robotDeviceId", App.getInstance().getUserInfo().getDeviceid());
        map.put("userId", App.getInstance().getUserInfo().getUserId()+"" );
        map.put("deviceId", SystemUtils.getDeviceKey());
        map.put("timeHour", select_hour);
        map.put("timeMin", select_minute);
        map.put("repeat", week);
        map.put("content", content);
        VRHttp.sendRequest(mContext, HttpLink.SetClock, map, new VRHttpListener() {
            @Override
            public void onSuccess(Object response, boolean isCache) {
                CBaseCode cBaseCode = GsonUtils.getServerBean((String) response, CBaseCode.class);
                if("0000".equals(cBaseCode.getCode())){
                    toastShow("设置闹钟成功");
                    setResult(RESULT_OK);
                    finish();
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

    //保存提醒
    private void save() {
        String content = etContent.getText().toString().trim();
        if (selectWeekList.size() == 0) {
            toastShow("请选择时间");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            toastShow("请选择内容");
            return;
        }
        Collections.sort(selectWeekList, Collator.getInstance(Locale.CHINA));
        String week = "";
        for (int i = 0; i < selectWeekList.size(); i++) {
            if (i == 0) {
                week = selectWeekList.get(i);
            } else {
                week += "," + selectWeekList.get(i);
            }
        }
        setClock(week, content);
    }

    /**
     * 周一 到 周日 view
     *
     * @param str
     */
    private void numberToStr(String str[]) {
        selectWeekList.clear();
        for (int i = 0; i < str.length; i++) {
            switch (str[i]) {
                case "1":
                    selectWeek(viewMon, tvMon, 0, true);
                    break;
                case "2":
                    selectWeek(viewTue, tvTue, 1, true);
                    break;
                case "3":
                    selectWeek(viewWed, tvWed, 2, true);
                    break;
                case "4":
                    selectWeek(viewThu, tvThu, 3, true);
                    break;
                case "5":
                    selectWeek(viewFri, tvFri, 4, true);
                    break;
                case "6":
                    selectWeek(viewSat, tvSat, 5, true);
                    break;
                case "7":
                    selectWeek(viewSun, tvSun, 6, true);
                    break;
            }
        }
    }

    /**
     * 小时数据
     *
     * @return
     */
    private ArrayList<Pickers> createHours() {
        ArrayList<Pickers> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add(new Pickers("0" + i, "" + i));
            } else {
                list.add(new Pickers("" + i, "" + i));
            }
        }
        return list;
    }

    /**
     * 分钟数据
     *
     * @return
     */
    private ArrayList<Pickers> createMinutes() {
        ArrayList<Pickers> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add(new Pickers("0" + i, "" + i));
            } else {
                list.add(new Pickers("" + i, "" + i));
            }
        }
        return list;
    }

    /**
     * 选中某天的效果
     *
     * @param view
     * @param textView
     * @param week
     * @param isSelected
     */
    private void selectWeek(View view, TextView textView, int week, boolean isSelected) {
        if (!isSelected) {
            view.setVisibility(View.INVISIBLE);
            textView.setTextColor(getResources().getColor(R.color.color_bfbfbf));
            selectWeekList.remove(weekBean[week]);
        } else {
            view.setVisibility(View.VISIBLE);
            textView.setTextColor(getResources().getColor(R.color.color_6c6c6c));
            if (!selectWeekList.contains(weekBean[week])) {
                selectWeekList.add(weekBean[week]);
            }
        }
    }

    @OnClick({R.id.ll_mon, R.id.ll_tue, R.id.ll_wed, R.id.ll_thu, R.id.ll_fri, R.id.ll_sat, R.id
            .ll_sun, R.id.ll_chengyuan, R.id.ll_content, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mon:
                selectWeek(viewMon, tvMon, 0, viewMon.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.ll_tue:
                selectWeek(viewTue, tvTue, 1, viewTue.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.ll_wed:
                selectWeek(viewWed, tvWed, 2, viewWed.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.ll_thu:
                selectWeek(viewThu, tvThu, 3, viewThu.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.ll_fri:
                selectWeek(viewFri, tvFri, 4, viewFri.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.ll_sat:
                selectWeek(viewSat, tvSat, 5, viewSat.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.ll_sun:
                selectWeek(viewSun, tvSun, 6, viewSun.getVisibility() == View.VISIBLE ? false : true);
                break;
            case R.id.btn_delete: //删除闹钟
                deleteClock(selectClockListBean.getId()+"");
                break;
        }
    }

}
