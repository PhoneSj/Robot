package com.zige.robot.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.activity.HomeWorkDetailActivity;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.http.rxhttp.BaseSubscriber;
import com.zige.robot.http.rxhttp.Datacenter;
import com.zige.robot.http.rxhttp.query.QueryUserBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.http.rxhttp.reponse.HomeWorkListBean;
import com.zige.robot.interf.DialogListener;
import com.zige.robot.utils.DateFormatUtils;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.utils.ToastUtils;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/7/31.
 */

public class HistoryHomeWorkFragment extends BaseNewFragment {

    private static final String TAG = "HistoryHomeWorkFragment";

    long subjectId;
    Dialog delDialog;

    ListView mListView;
    CommonLvAdapter<HomeWorkListBean.DataBean.ListBean>  mCommonLvAdapter;
    HomeWorkListBean.DataBean.ListBean selected_bean;

    public static final int RQ_CODE_DETAIL = 1; //详情

    public static HistoryHomeWorkFragment newInstance(long subjectId) {
        HistoryHomeWorkFragment fragment = new HistoryHomeWorkFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("data", subjectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_history_home_work,container, false);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        subjectId = getArguments().getLong("data", -1);
        Log.d(TAG, "lazyLoad: " + subjectId);
        httpGetHomeWorkList(0, Integer.MAX_VALUE, null, null, subjectId);
    }

    @Override
    protected void bindViews(View view) {
        mListView = view.findViewById(R.id.listview);
        initAdaptView();
        initDelDialog();
    }

    @Override
    protected void processLogic() {
//        subjectId = getArguments().getLong("data", -1);
        Log.d(TAG, "processLogic: " );
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode   +"  "+ resultCode );
        if(requestCode == RQ_CODE_DETAIL && resultCode == mContext.RESULT_OK){
            selected_bean.setIsDone(true);
            mCommonLvAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据日历刷新数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public void resetDateHistory(Long startTime, Long endTime){
        httpGetHomeWorkList(0, Integer.MAX_VALUE, startTime, endTime, subjectId);
    }

    private void initAdaptView(){
        mCommonLvAdapter = new CommonLvAdapter<HomeWorkListBean.DataBean.ListBean>(mContext, new ArrayList<HomeWorkListBean.DataBean.ListBean>(),
                R.layout.adapter_history_home_work_new) {
            @Override
            public void convert(CommonLvViewHolder holder, final HomeWorkListBean.DataBean.ListBean bean, int position) {
                 holder.setText(R.id.tv_class, bean.getSubjectName()); //科目
                 holder.setText(R.id.tv_content, bean.getContent()); //作业内容
                 holder.setText(R.id.tv_time, DateFormatUtils.Stamp2DateFormat(bean.getCreateTime()*1000, "yyyy-MM-dd"));
                if(bean.isIsDone()){
                    holder.setText(R.id.tv_state, "已完成");
                    holder.setTextColor(R.id.tv_state, getResources().getColor(R.color.color_22b4ff));
                }else {
                    holder.setText(R.id.tv_state, "未完成");
                    holder.setTextColor(R.id.tv_state, getResources().getColor(R.color.tv_777));
                }
                if(bean.getUnReadCount()==0){
                    holder.setViewVisibility(R.id.tv_unread_msg, View.GONE); //未读消息先隐藏？？？？？
                }else {
                    holder.setViewVisibility(R.id.tv_unread_msg, View.VISIBLE);
                    holder.setText(R.id.tv_unread_msg, bean.getUnReadCount()+"");
                }
                if("语文".equals(bean.getSubjectName())){
                    holder.setRlBackgroundResource(R.id.rl_left, R.drawable.language_circle);
                    holder.setTextColor(R.id.tv_class, getResources().getColor(R.color.color_fe5265));
                }else if("数学".equals(bean.getSubjectName())){
                    holder.setRlBackgroundResource(R.id.rl_left, R.drawable.mathematics_circle);
                    holder.setTextColor(R.id.tv_class, getResources().getColor(R.color.color_22b4ff));
                }else if("英语".equals(bean.getSubjectName())){
                    holder.setRlBackgroundResource(R.id.rl_left, R.drawable.english_circle);
                    holder.setTextColor(R.id.tv_class, getResources().getColor(R.color.color_ffa001));
                }else {
                    holder.setRlBackgroundResource(R.id.rl_left, R.drawable.other_circle);
                    holder.setTextColor(R.id.tv_class, getResources().getColor(R.color.color_0ac1c5));
                }
                holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_bean = bean;
                        startActivityForResult(new Intent(mContext, HomeWorkDetailActivity.class)
                                .putExtra("homeworkId", bean.getHomeworkId()), RQ_CODE_DETAIL); //详情
                    }
                });

                holder.getView(R.id.rl_root).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selected_bean = bean;
                        delDialog.show();
                        return true;
                    }
                });

            }
        };
        mListView.setAdapter(mCommonLvAdapter);
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
                Datacenter.get().deleteHomeWork(new BaseSubscriber<BaseCode>(mContext) {
                                                    @Override
                                                    protected void onUserSuccess(BaseCode baseCode) {
                                                        if(baseCode.getCode() == BaseCode.SUCCESS_CODE){
                                                            mCommonLvAdapter.removeItem(selected_bean);
                                                            showToast("删除成功");
                                                        }else {
                                                            showToast(baseCode.getMessage());
                                                        }
                                                    }}, selected_bean.getHomeworkId(),
                        new QueryUserBean(App.getInstance().getPhone(), SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid()));
            }
        });
    }
    /**
     * 家庭作业列表
     * @param page   页数
     * @param size  每页数量
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @param subjectId 科目id
     */
    private void httpGetHomeWorkList(int page, int size, Long startTime, Long endTime, Long subjectId){
        Datacenter.get().homeWorkList(new BaseSubscriber<HomeWorkListBean>(mContext) {
            @Override
            protected void onUserSuccess(HomeWorkListBean homeWorkListBean) {
                if(homeWorkListBean.getCode() == BaseCode.SUCCESS_CODE){
                    mCommonLvAdapter.setDatas(homeWorkListBean.getData().getList());
                }else {
                    ToastUtils.showToastShort(homeWorkListBean.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        },page, size, App.getInstance().getPhone(), SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid(), startTime, endTime, (subjectId==-1?null:subjectId));
    }

}
