package com.zige.robot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.http.rxhttp.reponse.HomeWorkListBean;

import java.util.List;

/**
 * Created by ldw on 2017/8/22.
 *
 */

public class HomeWorkListAdapter extends RecyclerView.Adapter<HomeWorkListAdapter.ViewHolder>{

    private Context mContext;
    private List<HomeWorkListBean.DataBean.ListBean> mList;

    public HomeWorkListAdapter(Context context, List<HomeWorkListBean.DataBean.ListBean> list){
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_work_list_2, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        HomeWorkListBean.DataBean.ListBean bean = mList.get(position);
//        holder.tv_create_time.setText(DateFormatUtils.Stamp2DateFormat(bean.getCreateTime()*1000,"yyyy-M-d"));
//        holder.tv_unread_count.setText("12");
//        holder.tv_homework_content.setText(bean.getContent());
//        holder.tv_child_msg.setText("宝宝有一条语言信息");
//        if(bean.isIsDone()){
//            //作业完成了
//            holder.iv_state.setImageResource(R.drawable.work_state_yes);
//        }else {
//            holder.iv_state.setImageResource(R.drawable.work_state_no);
//        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public  class  ViewHolder extends  RecyclerView.ViewHolder{

        public TextView  tv_create_time;
        public ImageView iv_parent_portrait;
        public TextView  tv_unread_count;
        public TextView tv_homework_content;
        public TextView tv_child_msg;
        public ImageView iv_state;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_create_time = itemView.findViewById(R.id.tv_create_time);
            iv_parent_portrait = itemView.findViewById(R.id.iv_parent_portrait);
            tv_unread_count = itemView.findViewById(R.id.tv_unread_count);
            tv_homework_content = itemView.findViewById(R.id.tv_homework_content);
            tv_child_msg = itemView.findViewById(R.id.tv_child_msg);
            iv_state = itemView.findViewById(R.id.iv_state);
        }
    }
}
