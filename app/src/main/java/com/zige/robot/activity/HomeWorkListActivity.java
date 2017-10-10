package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.http.rxhttp.BaseSubscriber;
import com.zige.robot.http.rxhttp.Datacenter;
import com.zige.robot.http.rxhttp.query.QueryUserBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.http.rxhttp.reponse.HomeWorkListBean;
import com.zige.robot.interf.DialogListener;
import com.zige.robot.utils.DateFormatUtils;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lidingwei on 2017/7/29.
 * 作业列表
 */

public class HomeWorkListActivity extends BaseActivity {
    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.rl_empty_view)
    RelativeLayout mRlEmptyView;

    public static final int RQ_CODE_EDIT = 1; //编辑
    public static final int RQ_CODE_DETAIL = 2; //详情

    CommonLvAdapter<HomeWorkListBean.DataBean.ListBean> mCommonLvAdapter;


    private long subjectId; //当前列表的科目id
    private String subjectName; //当前列表的科目名称
    private Dialog delDialog;
    private HomeWorkListBean.DataBean.ListBean selected_bean; //选中的作业bean


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_home_work_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setIvActionbg(R.drawable.add_little);
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加作业
                startActivityForResult(new Intent(mContext, HomeWorkEditActivity.class)
                        .putExtra("subjectId", subjectId)
                        .putExtra("subjectName", subjectName)
                        .putExtra("type", 0), RQ_CODE_EDIT); // 0新建作业
            }
        });
        subjectId = getIntent().getLongExtra("subjectId", 0);
        subjectName = getIntent().getStringExtra("subjectName");
        setTitleName(subjectName);
        initAdapter();
        httpGetHomeWorkList(0, Integer.MAX_VALUE, null, null, subjectId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RQ_CODE_EDIT) { //新建作业，返回刷新数据
                httpGetHomeWorkList(1, Integer.MAX_VALUE, null, null, subjectId);
            } else if (requestCode == RQ_CODE_DETAIL) {  //确认完成作业，更新完成状态
                selected_bean.setIsDone(true);
                mCommonLvAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initAdapter() {
        initDelDialog();
        mCommonLvAdapter = new CommonLvAdapter<HomeWorkListBean.DataBean.ListBean>(mContext, new ArrayList<HomeWorkListBean.DataBean.ListBean>(), R.layout.adapter_work_list_2) {
            @Override
            public void convert(final CommonLvViewHolder holder, final HomeWorkListBean.DataBean.ListBean bean, int position) {
                holder.setViewVisibility(R.id.ll_date, View.VISIBLE);
                holder.setText(R.id.tv_create_time, DateFormatUtils.Stamp2DateFormat(bean.getCreateTime() * 1000, "yyyy-M-d"));
                if (position > 0) {
                    if (DateFormatUtils.Stamp2DateFormat(bean.getCreateTime() * 1000, "yyyy-M-d").equals(DateFormatUtils.Stamp2DateFormat(mCommonLvAdapter.getItem(position - 1).getCreateTime() * 1000, "yyyy-M-d"))) {
                        //创建日期相同则隐藏日期时间栏
                        holder.setViewVisibility(R.id.ll_date, View.GONE);
                    }
                }
                if (bean.getUnReadCount() > 0) {
                    holder.setViewVisibility(R.id.tv_unread_count, View.VISIBLE);
                    holder.setText(R.id.tv_unread_count, bean.getUnReadCount() + "");
                } else {
                    holder.setViewVisibility(R.id.tv_unread_count, View.GONE);
                }
                holder.setText(R.id.tv_homework_content, bean.getContent());
                if (bean.getLastChildMessageType() == 1) { //文本
                    holder.setViewVisibility(R.id.tv_child_msg, View.VISIBLE);
                    holder.setText(R.id.tv_child_msg, bean.getLastChildMessageContent());
                } else if (bean.getLastChildMessageType() == 2) {
                    holder.setViewVisibility(R.id.tv_child_msg, View.VISIBLE);
                    holder.setText(R.id.tv_child_msg, "宝宝回复了语音消息");
                } else {
                    holder.setViewVisibility(R.id.tv_child_msg, View.GONE);
                }
                if (bean.isIsDone()) {
                    //作业完成了
                    holder.setImageResoure(R.id.iv_state, R.drawable.work_state_yes);
                } else {
                    holder.setImageResoure(R.id.iv_state, R.drawable.work_state_no);
                }
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_bean = bean;
                        startActivityForResult(new Intent(mContext, HomeWorkDetailActivity.class)
                                .putExtra("homeworkId", bean.getHomeworkId())
                                .putExtra("subjectId", subjectId)
                                .putExtra("content", bean.getContent())
                                .putExtra("data",DateFormatUtils.Stamp2DateFormat(bean.getCreateTime() * 1000, "yyyy-M-d"))
                                .putExtra("subjectName", subjectName), RQ_CODE_EDIT); //详情
                    }
                });
                holder.getView(R.id.ll_root).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selected_bean = bean;
                        delDialog.show();
                        return true;
                    }
                });
            }
        };
        mListview.setAdapter(mCommonLvAdapter);
    }


    /**
     * 删除指令dialog
     */
    private void initDelDialog() {
        delDialog = DialogUtils.createDelDialog(mContext, new DialogListener() {
            @Override
            public void actionClick() {
                //删除指令
                delDialog.dismiss();
                Datacenter.get().deleteHomeWork(new BaseSubscriber<BaseCode>(mContext) {
                                                    @Override
                                                    protected void onUserSuccess(BaseCode baseCode) {
                                                        if (baseCode.getCode() == BaseCode.SUCCESS_CODE) {
                                                            mCommonLvAdapter.removeItem(selected_bean);
                                                            toastShow("删除成功");
                                                        } else {
                                                            toastShow(baseCode.getMessage());
                                                        }
                                                    }
                                                }, selected_bean.getHomeworkId(),
                        new QueryUserBean(mApplication.getPhone(), SystemUtils.getDeviceKey(), mApplication.getUserInfo().getDeviceid()));
            }
        });
    }

    /**
     * 家庭作业列表
     *
     * @param page      页数
     * @param size      每页数量
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param subjectId 科目id
     */
    private void httpGetHomeWorkList(int page, int size, Long startTime, Long endTime, Long subjectId) {
        showProgressDialog("正在加载", true);
        Datacenter.get().homeWorkList(new BaseSubscriber<HomeWorkListBean>(mContext) {
            @Override
            protected void onUserSuccess(HomeWorkListBean homeWorkListBean) {
                dismissProgressDialog();
                if (homeWorkListBean.getCode() == BaseCode.SUCCESS_CODE) {
                    if(homeWorkListBean.getData().getList()!=null && homeWorkListBean.getData().getList().size() >0){
                        mCommonLvAdapter.setDatas(homeWorkListBean.getData().getList());
                        mRlEmptyView.setVisibility(View.GONE);
                    }else {
                        mRlEmptyView.setVisibility(View.VISIBLE); //空提示
                    }
                } else {
                    toastShow(homeWorkListBean.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                super.onError(e);
            }
        }, page, size, mApplication.getPhone(), SystemUtils.getDeviceKey(), mApplication.getUserInfo().getDeviceid(), startTime, endTime, subjectId);
    }


}
