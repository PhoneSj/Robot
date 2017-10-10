package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/1.
 * 添加课程
 */

public class AddSubjectActivity extends BaseActivity {
    @BindView(R.id.gridview)
    GridView mGridview;
    CommonLvAdapter<String> mCommonLvAdapter;
    List<String> mList = new ArrayList<>();

    int [] subjectIcon = {R.drawable.dance_icon, R.drawable.music_icon, R.drawable.drawing_icon,
    R.drawable.science_icon, R.drawable.exercise_icon, R.drawable.default_icon};

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_add_subject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("添加课程");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData(){
        mList.add("舞蹈");
        mList.add("音乐");
        mList.add("美术");
        mList.add("科学");
        mList.add("体育");
        mList.add("默认");
        mCommonLvAdapter = new CommonLvAdapter<String>(mContext, mList, R.layout.adapter_add_subject) {
            @Override
            public void convert(CommonLvViewHolder holder, final String bean, int position) {
                holder.setImageResoure(R.id.iv_subject, subjectIcon[position]);
                holder.setOnClickListener(R.id.iv_subject, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //选择某个科目类别
                         setResult(RESULT_OK, new Intent().putExtra("subjectName", bean));
                         finish();
                    }
                });
            }
        };
        mGridview.setAdapter(mCommonLvAdapter);
    }


}
