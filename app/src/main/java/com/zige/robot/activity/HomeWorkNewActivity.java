package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.http.rxhttp.BaseSubscriber;
import com.zige.robot.http.rxhttp.Datacenter;
import com.zige.robot.http.rxhttp.query.QuerySubjectBean;
import com.zige.robot.http.rxhttp.query.QueryUserBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.http.rxhttp.reponse.SubjectIdListBean;
import com.zige.robot.interf.DialogListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lidingwei on 2017/7/29.
 * 作业
 */

public class HomeWorkNewActivity extends BaseActivity {

    public static final String LANGUAGE ="语文";
    public static final String MATHEMATICS ="数学";
    public static final String ENGLISH ="英语";
    public static final String DANCE ="舞蹈";
    public static final String DRAWING ="美术";
    public static final String EXERCISE ="体育";
    public static final String MUSIC ="音乐";
    public static final String SCIENCE ="科学";

    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.iv_add_type)
    ImageView mIvAddType;

    Dialog mDialog;
    Dialog delDialog;
    CommonLvAdapter<SubjectIdListBean.DataBean.ListBean> mCommonLvAdapter;

    ArrayList<SubjectIdListBean.DataBean.ListBean> list = new ArrayList<>();

    public static List<SubjectIdListBean.DataBean.ListBean> subjectList;

    long selected_subjectId = 0;

    SubjectIdListBean.DataBean.ListBean selected_bean;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_homework_new;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("作业");
        setTvActionText("历史");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //历史作业记录
                if(HomeWorkNewActivity.subjectList ==null)
                    return;
                startActivity(HistoryHomeWorkNewActivity.class);

            }
        });
        initData();
        getNetData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            String subjectName = data.getStringExtra("subjectName");
            if("默认".equals(subjectName)){
                //默认的还需要填写科目标题
                et_add_type.setText("");
                mDialog.show();
            }else {
               //创建科目
                if(subjectIsRepeat(subjectName)){
                    toastShow("你已经添加了" + subjectName);
                    return;
                }
                changeSubject(new QuerySubjectBean(mApplication.getPhone(), SystemUtils.getDeviceKey(),
                        mApplication.getUserInfo().getDeviceid(), null, subjectName));
            }
        }
    }


    /**
     * 删除指令dialog
     */
    private void initDelDialog(){
        delDialog = DialogUtils.createDelDialog(mContext, new DialogListener() {
            @Override
            public void actionClick() {
                //删除指令
                delDialog.dismiss();
                Datacenter.get().deleteSubject(new BaseSubscriber<BaseCode>(mContext) {
                   @Override
                      protected void onUserSuccess(BaseCode baseCode) {
                         if(baseCode.getCode() == BaseCode.SUCCESS_CODE){
                             toastShow("删除成功");
                             mCommonLvAdapter.removeItem(selected_bean);
                         }else {
                             toastShow(baseCode.getMessage());
                         }
                        }}, selected_subjectId,
                        new QueryUserBean(mApplication.getPhone(), SystemUtils.getDeviceKey(), mApplication.getUserInfo().getDeviceid()));
            }
        });
    }

    /**
     * 创建和修改科目
     * @param bean
     */
    private void changeSubject(QuerySubjectBean bean){
        showProgressDialog("正在加载",true);
        Datacenter.get().changeSubject(new BaseSubscriber<BaseCode>(mContext) {
            @Override
            protected void onUserSuccess(BaseCode baseCode) {
                dismissProgressDialog();
                if(baseCode.getCode() == BaseCode.SUCCESS_CODE){
                    getNetData(); //重新获取科目列表
                }else {
                    toastShow(baseCode.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dismissProgressDialog();
            }
        }, bean);
    }

    /**
     * 判断科目是否重复
     * @param subjectName
     * @return
     */
    private boolean  subjectIsRepeat(String subjectName){
        if(subjectList!=null && subjectList.size() >0){
            for (SubjectIdListBean.DataBean.ListBean listBean : subjectList) {
                if(subjectName.equals(listBean.getSubjectName())){ //科目重复了
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取科目列表
     */
    private void getNetData(){
        showProgressDialog("正在加载",false);
        Datacenter.get().getSubjectList(new BaseSubscriber<SubjectIdListBean>(mContext) {
            @Override
            protected void onUserSuccess(SubjectIdListBean subjectIdListBean) {
                dismissProgressDialog();
                if(subjectIdListBean.getCode() == BaseCode.SUCCESS_CODE){
                    subjectList = subjectIdListBean.getData().getList();
                    mCommonLvAdapter.setDatas(subjectList);
                }else {
                    subjectList = new ArrayList<SubjectIdListBean.DataBean.ListBean>();
                    toastShow(subjectIdListBean.getMessage());
                }
            }
            @Override
            protected void onUserError(Throwable ex) {
                super.onUserError(ex);
                dismissProgressDialog();
                subjectList = new ArrayList<SubjectIdListBean.DataBean.ListBean>();
            }
        }, 0, Integer.MAX_VALUE);
    }

    private void initData() {
        initAddTypeDialog();
        initDelDialog();
        mCommonLvAdapter = new CommonLvAdapter<SubjectIdListBean.DataBean.ListBean>(mContext, list, R.layout.adapter_work_type) {
            @Override
            public void convert(CommonLvViewHolder holder, final SubjectIdListBean.DataBean.ListBean bean, int position) {
                if (!TextUtils.isEmpty(bean.getSubjectName())) {
                    if (LANGUAGE.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.work_language);
                    } else if (MATHEMATICS.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.work_mathematics);
                    } else if (ENGLISH.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.work_english);
                    } else if (DANCE.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.work_dance);
                    } else if (DRAWING.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.drawing);
                    } else if (EXERCISE.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.exercise);
                    } else if (MUSIC.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.music);
                    } else if (SCIENCE.equals(bean.getSubjectName())) {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.science);
                    }else {
                        holder.setImageResoure(R.id.iv_subject, R.drawable.work_default);
                    }
                    holder.setText(R.id.tv_work_type, bean.getSubjectName());
                    holder.setOnClickListener(R.id.rl_bg, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //作业列表
                            startActivity(new Intent(mContext, HomeWorkListActivity.class)
                                    .putExtra("subjectId",bean.getSubjectId())
                                    .putExtra("subjectName",bean.getSubjectName()));
                        }
                    });
                    holder.getView(R.id.rl_bg).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            selected_subjectId = bean.getSubjectId();
                            selected_bean = bean;
                            delDialog.show();
                            return true;
                        }
                    });
                }
            }
        };
        mListview.setAdapter(mCommonLvAdapter);
    }


    private EditText et_add_type;
    private void initAddTypeDialog(){
        mDialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_add_work_type, ScreenUtils.dp2px(mContext, 20), ScreenUtils.dp2px(mContext, 20));
        mDialog.findViewById(R.id.tv_click_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.tv_click_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = et_add_type.getText().toString().trim();
                if(TextUtils.isEmpty(subjectName)){
                    toastShow("请填写科目！");
                    return;
                }
                if(subjectIsRepeat(subjectName)){
                    toastShow("你已经添加了" + subjectName);
                    return;
                }
                changeSubject(new QuerySubjectBean(mApplication.getPhone(), SystemUtils.getDeviceKey(),
                        mApplication.getUserInfo().getDeviceid(), null, subjectName));
                mDialog.dismiss();
            }
        });
        et_add_type = mDialog.findViewById(R.id.et_add_type);

    }

    @OnClick(R.id.iv_add_type)
    public void onViewClicked() {
        //添加分类
        startActivityForResult(new Intent(mContext, AddSubjectActivity.class), 1);
    }

}
