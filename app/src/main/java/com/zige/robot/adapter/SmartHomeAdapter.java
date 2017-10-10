package com.zige.robot.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.bean.SmartHomeBean;
import com.zige.robot.view.MyVideoView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 * 智能家居引导图适配器
 */

public class SmartHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int IMG   = 1;
    public static final int VIDEO = 2;

    private List<SmartHomeBean> mList;

    public SmartHomeAdapter(List<SmartHomeBean> list){
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == IMG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_smart_home, parent, false);
            return new SmartHomeAdapter.ViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_smart_home_video, parent, false);
            return new SmartHomeAdapter.ViewHolderViedo(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SmartHomeBean bean = mList.get(position);
        if(!TextUtils.isEmpty(bean.getPath())){
             //视频
            ((ViewHolderViedo)holder).tv_doc.setText(bean.getDocText());
            ((ViewHolderViedo)holder).iv_thumb.setImageResource(R.drawable.logo);
            ((ViewHolderViedo)holder).iv_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ((ViewHolderViedo)holder).iv_thumb.setVisibility(View.GONE);
//                    ((ViewHolderViedo)holder).video_view.setVideoURI(Uri.parse(bean.getPath()));
//                    ((ViewHolderViedo)holder).video_view.start();
                }
            });
        }else {
            //图片
            ((ViewHolder)holder).tv_doc.setText(bean.getDocText());
            ((ViewHolder)holder).iv_img.setImageResource(bean.getResId());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!TextUtils.isEmpty(mList.get(position).getPath())){
            return VIDEO;
        }else {
            return IMG;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv_img;
        public TextView  tv_doc;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_doc = (TextView) itemView.findViewById(R.id.tv_doc);
        }
    }

    public static class ViewHolderViedo extends RecyclerView.ViewHolder{

        public MyVideoView video_view;
        public TextView  tv_doc;
        public ImageView  iv_thumb;

        public ViewHolderViedo(View itemView) {
            super(itemView);
            video_view = (MyVideoView) itemView.findViewById(R.id.video_view);
            tv_doc = (TextView) itemView.findViewById(R.id.tv_doc);
            iv_thumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }


}
