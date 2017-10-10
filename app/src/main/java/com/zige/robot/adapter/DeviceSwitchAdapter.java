package com.zige.robot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.bean.RobotDeviceListBean;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.view.GalleryRecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */

public class DeviceSwitchAdapter extends RecyclerView.Adapter<DeviceSwitchAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
    Context mContext;
    List<RobotDeviceListBean.RobotListBean> mItems;
    GalleryRecyclerView mGalleryRecyclerView;
    int width;
    DeviceOnClickListener mDeviceOnClickListener;
    DeviceOnUnbindListener mDeviceOnUnbindListener;
    public DeviceSwitchAdapter(Context mContext, List<RobotDeviceListBean.RobotListBean> mItems, GalleryRecyclerView mGalleryRecyclerView) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mGalleryRecyclerView = mGalleryRecyclerView;
        width= ScreenUtils.getScreenW(mContext);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_robot_device_card,null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.item_robot_device_layout.setLayoutParams(new RelativeLayout.LayoutParams((int)(width*0.7), ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.item_robot_device_img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(width*0.65)));
        holder.tv_device_name.setText( "归属："+mItems.get(position).getDeviceName());
        holder.tv_chengyuan.setText( "权限："+mItems.get(position).getNickname());
        holder.iv_device_state.setBackgroundResource(R.drawable.english_off);
        holder.tv_click_unbind.setVisibility(View.INVISIBLE);
        if(String.valueOf(mItems.get(position).getRobotDeviceId()).equals(App.getInstance().getUserInfo().getDeviceid())){ //当前绑定的robot
            holder.iv_device_state.setBackgroundResource(R.drawable.english_on);
            if(mItems.size() == 1){
                holder.tv_click_unbind.setVisibility(View.VISIBLE);
            }
        }else {
            holder.tv_click_unbind.setVisibility(View.VISIBLE);
        }
        holder.iv_device_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDeviceOnClickListener!=null)
                mDeviceOnClickListener.onItemClickListener(position);
            }
        });
        holder.tv_click_unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDeviceOnUnbindListener!=null)
                    mDeviceOnUnbindListener.onItemClickUnbindListener(position);
            }
        });
//        if (position%2==0){
//            holder.item_robot_device_layout.setBackgroundColor(Color.BLUE);
//        }else {
//            holder.item_robot_device_layout.setBackgroundColor(Color.GREEN);
//        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        if (position==mItems.size()){
            mGalleryRecyclerView.smoothScrollBy(500,0);
        }else if (position==0){
            mGalleryRecyclerView.setSelectPosition(position);
            mGalleryRecyclerView.smoothScrollBy(-500,0);
        }else {
            mGalleryRecyclerView.setSelectPosition(position);
            mGalleryRecyclerView.smoothScrollBy(-500,0);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_robot_device_layout;
        ImageView item_robot_device_img;
        TextView tv_device_name;
        TextView tv_chengyuan;
        TextView tv_click_unbind;
        ImageView iv_device_state;
        public ItemViewHolder(View itemView) {
            super(itemView);
            item_robot_device_layout= (RelativeLayout) itemView.findViewById(R.id.item_robot_device_layout);
            item_robot_device_img= (ImageView) itemView.findViewById(R.id.item_robot_device_img);
            tv_device_name= (TextView) itemView.findViewById(R.id.tv_device_name);
            tv_chengyuan= (TextView) itemView.findViewById(R.id.tv_chengyuan);
            tv_click_unbind= (TextView) itemView.findViewById(R.id.tv_click_unbind);
            iv_device_state= (ImageView) itemView.findViewById(R.id.iv_device_state);
        }
    }

    public void setOnItemClickListener(DeviceOnClickListener listener){
          this.mDeviceOnClickListener = listener;
    }

    public void setOnItemUnbindClickListener(DeviceOnUnbindListener listener){
         this.mDeviceOnUnbindListener = listener;
    }

    public interface DeviceOnClickListener{

        void onItemClickListener(int position);

    }

    public interface DeviceOnUnbindListener{

        void onItemClickUnbindListener(int position);

    }

}
