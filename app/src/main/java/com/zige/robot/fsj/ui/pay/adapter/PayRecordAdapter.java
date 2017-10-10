package com.zige.robot.fsj.ui.pay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.model.bean.PayRecordBean;
import com.zige.robot.fsj.util.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/8.
 */

public class PayRecordAdapter extends RecyclerView.Adapter<PayRecordAdapter.MyViewHolder> {

    private Context context;
    private List<PayRecordBean.RowsBean> datas;

    public PayRecordAdapter(Context context, List<PayRecordBean.RowsBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pay_record, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PayRecordBean.RowsBean bean = datas.get(position);
//        if (bean.getStatus() == PayRecordBean.RowsBean.STATE_SUCCESS) {
//            holder.ivDot.setImageResource(R.drawable.pay_dot_green);
//            holder.tvState.setText("支付成功");
//        } else if (bean.getStatus() == PayRecordBean.RowsBean.STATE_FAILE) {
//            holder.ivDot.setImageResource(R.drawable.pay_dot_yellow);
//            holder.tvState.setText("支付失败");
//        } else {
//            holder.ivDot.setImageResource(R.drawable.pay_dot_pink);
//            holder.tvState.setText("未知");
//        }
        holder.ivDot.setImageResource(R.drawable.pay_dot_green);
        holder.tvState.setText("支付成功");
        holder.tvTime.setText(DateUtil.formatTime2String(bean.getCreateTime(), true));
        holder.tvMoney.setText(bean.getFee() + "元/" + bean.getChargeTime() + "分钟");
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_dot)
        ImageView ivDot;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_state)
        TextView tvState;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
