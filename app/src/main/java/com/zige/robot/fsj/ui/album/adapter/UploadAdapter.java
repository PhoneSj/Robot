package com.zige.robot.fsj.ui.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.component.ImageLoader;
import com.zige.robot.fsj.model.bean.AlbumUploadBean;
import com.zige.robot.fsj.ui.album.AlbumUploadActivity;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickUtils;

import java.util.List;

/**
 * Created by PhoneSj on 2017/9/25.
 */

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.MyViewHolder> {

    public static final int SPAN_COUNT = 5;
    private Context context;
    private List<AlbumUploadBean> datas;
    private int spanCount;
    private int scaleSize;
    private OnItemClickListener onItemClickListener;

    public UploadAdapter(Context context, List<AlbumUploadBean> datas) {
        this.context = context;
        this.datas = datas;
        this.spanCount = SPAN_COUNT;
        buildScaleSize();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                                              .inflate(R.layout.item_album_upload, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String path = datas.get(position).getPath();
        ImageLoader.load(context, path, holder.ivPhoto);

        AlbumUploadBean.UploadState state = datas.get(position).getState();
        int progress = (int) Math.round(datas.get(position).getPercent() * 100);
        if (state == AlbumUploadBean.UploadState.FAIL) {
            holder.llUplaod.setVisibility(View.VISIBLE);
            holder.tvUpload.setText("上传失败");
            holder.pbUpload.setProgress(0);
        } else if (state == AlbumUploadBean.UploadState.UPLOADING) {
            holder.llUplaod.setVisibility(View.VISIBLE);
            holder.tvUpload.setText("正在上传");
            holder.pbUpload.setProgress(progress);
        } else if (state == AlbumUploadBean.UploadState.FINISH) {
            holder.llUplaod.setVisibility(View.VISIBLE);
            holder.tvUpload.setText("上传完成");
            holder.pbUpload.setProgress(100);
        } else {
            holder.llUplaod.setVisibility(View.VISIBLE);
            holder.tvUpload.setText("等待上传");
            holder.pbUpload.setProgress(0);
            ((AlbumUploadActivity) context).uploadPhoto(position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPhoto;
        public RelativeLayout llUplaod;
        public TextView tvUpload;
        public ProgressBar pbUpload;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            llUplaod = (RelativeLayout) itemView.findViewById(R.id.ll_upload);
            tvUpload = (TextView) itemView.findViewById(R.id.tv_upload);
            pbUpload = (ProgressBar) itemView.findViewById(R.id.pb_upload);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
            params.width = scaleSize;
            params.height = scaleSize;

            llUplaod.getLayoutParams().width = scaleSize;
            llUplaod.getLayoutParams().height = scaleSize;

            pbUpload.getLayoutParams().height = scaleSize / 10;
        }

    }

    private void buildScaleSize() {
        int screenWidth = PickUtils.getInstance(context).getWidthPixels();
        int space = PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE);
        scaleSize = (screenWidth - (spanCount + 1) * space) / spanCount;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
