package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.StudyTimeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/18.
 */

public class StudyTimeSettingActivity extends BaseActivity {
    @BindView(R.id.rl_back_return)
    RelativeLayout rlBackReturn;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.study_time_add)
    TextView studyTimeAdd;
    @BindView(R.id.study_time_listview)
    ListView studyTimeListview;
    @BindView(R.id.no_list_toast_layout)
    LinearLayout noListToastLayout;


    CommonLvAdapter<StudyTimeBean> commonLvAdapterVoice;
    List<StudyTimeBean> mList = new ArrayList<>();
    int ADD_TIME = 1;
    int CHANGE_TIME = 2;


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_study_time_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("学习时间设置");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        commonLvAdapterVoice = new CommonLvAdapter<StudyTimeBean>(mContext, mList, R.layout.item_study_time) {

            @Override
            public void convert(CommonLvViewHolder holder, StudyTimeBean bean, int position) {
                holder.setText(R.id.item_study_subject_name, bean.getSubject());
                holder.setText(R.id.item_study_subject_time, bean.getStartTime() + " - " + bean.getEndTime());

                if (bean.getIsOpen() == 1) {
                    holder.setTextColor(R.id.item_study_subject_name, getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.item_study_subject_time, getResources().getColor(R.color.white));
                    holder.setLlBackgroundResource(R.id.item_study_layout, R.drawable.learning_box_on);
                    holder.setBackgroundResource(R.id.item_study_subject_swich, R.drawable.learning_on);
                } else {
                    holder.setTextColor(R.id.item_study_subject_name, getResources().getColor(R.color.color_777777));
                    holder.setTextColor(R.id.item_study_subject_time, getResources().getColor(R.color.color_777777));
                    holder.setLlBackgroundResource(R.id.item_study_layout, R.drawable.learning_box_off);
                    holder.setBackgroundResource(R.id.item_study_subject_swich, R.drawable.learning_off);
                }
                holder.getView(R.id.item_study_subject_swich).setTag(position);
                holder.setOnClickListener(R.id.item_study_subject_swich, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag();
                        ImageView imageView = (ImageView) v;

                        if (mList.get(pos).getIsOpen() == 1) {
                            mList.get(pos).setIsOpen(0);
//                            imageView.setBackgroundResource(R.drawable.learning_on);
                        } else {
                            mList.get(pos).setIsOpen(1);
//                            imageView.setBackgroundResource(R.drawable.learning_off);
                        }
                        commonLvAdapterVoice.notifyDataSetChanged();
                    }
                });
            }
        };
        studyTimeListview.setAdapter(commonLvAdapterVoice);
        studyTimeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudyTimeBean studyTimeBean = (StudyTimeBean) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, StudyTimeEdtiActivity.class);
                intent.putExtra("StudyTimeBean",studyTimeBean);
                intent.putExtra("position",position);
                startActivityForResult(intent,CHANGE_TIME);
            }
        });
    }


    @OnClick({R.id.study_time_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.study_time_add://添加
                Intent intent2 = new Intent(mContext, StudyTimeEdtiActivity.class);
                intent2.putExtra("type", 0);
                startActivityForResult(intent2, ADD_TIME);
//                write();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == ADD_TIME) {
            mList.add(0, (StudyTimeBean) data.getSerializableExtra("StudyTimeBean"));
            commonLvAdapterVoice.notifyDataSetChanged();
            if (studyTimeListview.getAdapter().getCount() != 0) {
                noListToastLayout.setVisibility(View.GONE);
            }
        }else if (requestCode==CHANGE_TIME){
            int position =data.getIntExtra("position",-1);
            if (position!=-1){
                if (data.getIntExtra("type",0)==0){//修改
                    mList.remove(position);
                    mList.add(position,(StudyTimeBean) data.getSerializableExtra("StudyTimeBean"));
                }else if (data.getIntExtra("type",0)==1){//删除
                    mList.remove(position);
                }
                commonLvAdapterVoice.notifyDataSetChanged();
                if (studyTimeListview.getAdapter().getCount() != 0) {
                    noListToastLayout.setVisibility(View.GONE);
                }else {
                    noListToastLayout.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
