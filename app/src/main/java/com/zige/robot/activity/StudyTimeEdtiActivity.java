package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.Pickers;
import com.zige.robot.bean.StudyTimeBean;
import com.zige.robot.utils.PopupWindowUtils;
import com.zige.robot.view.PickerTimeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zige.robot.R;

/**
 * Created by Administrator on 2017/5/18.
 */

public class StudyTimeEdtiActivity extends BaseActivity {


    @BindView(R.id.rl_back_return)
    RelativeLayout rlBackReturn;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.study_time_edit_add_btn)
    Button studyTimeEditAddBtn;
    @BindView(R.id.study_time_edit_add_layout)
    RelativeLayout studyTimeEditAddLayout;
    @BindView(R.id.study_time_edit_del_btn)
    Button studyTimeEditDelBtn;
    @BindView(R.id.study_time_edit_btn)
    Button studyTimeEditBtn;
    @BindView(R.id.study_time_edit_layout)
    LinearLayout studyTimeEditLayout;
    PopupWindow popupWindow;
    @BindView(R.id.study_time_edit_custom_time_text)
    TextView studyTimeEditCustomTimeText;
    @BindView(R.id.study_time_edit_custom_time)
    LinearLayout studyTimeEditCustomTime;
    @BindView(R.id.study_time_edit_standard_time_text)
    TextView studyTimeEditStandardTimeText;
    @BindView(R.id.study_time_edit_standard_time)
    LinearLayout studyTimeEditStandardTime;
    private List<Pickers> startHouseList; // 滚动选择器数据
    private List<Pickers> startMinuteList; // 滚动选择器数据
    private List<Pickers> endHouseList; // 滚动选择器数据
    private List<Pickers> endMinuteList; // 滚动选择器数据
    private String startHouse = "08";
    private String startMinute = "00";
    private String endHouse = "09";
    private String endMinute = "00";
    StudyTimeBean studyTimeBean;
    String subject;
    String time;

    int type = 0;//0添加，1 编辑；
    int position;

    void initPopStandardTime() {
        popupWindow = new PopupWindow(mContext);
        View view = View.inflate(mContext, R.layout.pop_study_standard_time, null);
        popupWindow.setContentView(view);
        PopupWindowUtils.setButtomShow(popupWindow, view, R.id.pop_standard_time_layout);
        PickerTimeView study_time_start_house = (PickerTimeView) view.findViewById(R.id.study_time_start_house);
        PickerTimeView study_time_start_minute = (PickerTimeView) view.findViewById(R.id.study_time_start_minute);
        PickerTimeView study_time_end_house = (PickerTimeView) view.findViewById(R.id.study_time_end_house);
        PickerTimeView study_time_end_minute = (PickerTimeView) view.findViewById(R.id.study_time_end_minute);
        view.findViewById(R.id.pop_standard_time_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.pop_standard_time_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(startHouse + startMinute) >= Integer.parseInt(endHouse + endMinute)) {
                    toastShow("结束时间要大于开始时间");
                    return;
                }
                studyTimeEditCustomTimeText.setText(startHouse + ":" + startMinute + " - " + endHouse + ":" + endMinute);
                popupWindow.dismiss();
            }
        });
        startHouseList = new ArrayList<>();
        endHouseList = new ArrayList<>();
        startMinuteList = new ArrayList<>();
        endMinuteList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                startHouseList.add(new Pickers("0" + i, "" + i));
                endHouseList.add(new Pickers("0" + i, "" + i));
            } else {
                startHouseList.add(new Pickers("" + i, "" + i));
                endHouseList.add(new Pickers("" + i, "" + i));
            }
        }
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                startMinuteList.add(new Pickers("0" + i, "" + i));
                endMinuteList.add(new Pickers("0" + i, "" + i));
            } else {
                startMinuteList.add(new Pickers("" + i, "" + i));
                endMinuteList.add(new Pickers("" + i, "" + i));
            }
        }
        study_time_start_house.setData(startHouseList);
        study_time_start_minute.setData(startMinuteList);
        study_time_end_house.setData(endHouseList);
        study_time_end_minute.setData(endMinuteList);

        study_time_start_house.setSelected(startHouse);
        study_time_start_minute.setSelected(startMinute);

        study_time_end_house.setSelected(endHouse);
        study_time_end_minute.setSelected(endMinute);
        study_time_start_house.setOnSelectListener(new PickerTimeView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                startHouse = pickers.getShowConetnt();
            }
        });
        study_time_start_minute.setOnSelectListener(new PickerTimeView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                startMinute = pickers.getShowConetnt();
            }
        });
        study_time_end_house.setOnSelectListener(new PickerTimeView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                endHouse = pickers.getShowConetnt();
            }
        });
        study_time_end_minute.setOnSelectListener(new PickerTimeView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                endMinute = pickers.getShowConetnt();
            }
        });
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_study_time_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 0);
        position = getIntent().getIntExtra("position", -1);

        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        studyTimeBean = (StudyTimeBean) getIntent().getSerializableExtra("StudyTimeBean");
        if (studyTimeBean != null) {
            editText.setText(studyTimeBean.getSubject());
            try {
                startHouse = studyTimeBean.getStartTime().split(":")[0];
                startMinute = studyTimeBean.getStartTime().split(":")[1];
                endHouse = studyTimeBean.getEndTime().split(":")[0];
                endMinute = studyTimeBean.getEndTime().split(":")[1];
                studyTimeEditCustomTimeText.setText(studyTimeBean.getStartTime() + " - " + studyTimeBean.getEndTime());
            } catch (Exception e) {

            }
            studyTimeEditLayout.setVisibility(View.VISIBLE);

        } else {
            if (type == 0) {
                setTitleName("添加学习时间");
                studyTimeEditAddLayout.setVisibility(View.VISIBLE);
            } else {
                setTitleName("编辑学习时间");
                studyTimeEditLayout.setVisibility(View.VISIBLE);
            }
        }
        initPopStandardTime();
    }


    @OnClick({R.id.study_time_edit_standard_time, R.id.study_time_edit_custom_time, R.id.study_time_edit_add_btn, R.id.study_time_edit_del_btn, R.id.study_time_edit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.study_time_edit_standard_time:
                popupWindow.showAtLocation(findViewById(R.id.tv_title_name), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.study_time_edit_custom_time:
                popupWindow.showAtLocation(findViewById(R.id.tv_title_name), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.study_time_edit_add_btn:
                subject = editText.getText().toString().trim();
                time = studyTimeEditCustomTimeText.getText().toString().trim();
                if (TextUtils.isEmpty(subject)) {
                    toastShow("请输入课程");
                    return;
                } else if (TextUtils.isEmpty(time)) {
                    toastShow("请选择时间");
                    return;
                }
                studyTimeBean = new StudyTimeBean(subject, startHouse + ":" + startMinute, endHouse + ":" + endMinute, 1);
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("StudyTimeBean", studyTimeBean);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.study_time_edit_del_btn:
                Intent intent2 = new Intent();
                intent2.putExtra("type", 1);
                intent2.putExtra("position", position);
                setResult(RESULT_OK, intent2);
                finish();
                break;
            case R.id.study_time_edit_btn:
                subject = editText.getText().toString().trim();
                time = studyTimeEditCustomTimeText.getText().toString().trim();
                if (TextUtils.isEmpty(subject)) {
                    toastShow("请输入课程");
                    return;
                } else if (TextUtils.isEmpty(time)) {
                    toastShow("请选择时间");
                    return;
                }
                studyTimeBean = new StudyTimeBean(subject, startHouse + ":" + startMinute, endHouse + ":" + endMinute, 1);
                Intent intent3 = new Intent();
                intent3.putExtra("type", 0);
                intent3.putExtra("position", position);
                intent3.putExtra("StudyTimeBean", studyTimeBean);
                setResult(RESULT_OK, intent3);
                finish();
                break;
        }
    }

}
