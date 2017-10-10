//package com.zige.robot.fsj.ui.album.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.zige.robot.R;
//import com.zige.robot.fsj.component.ImageLoader;
//import com.zige.robot.fsj.model.bean.AlbumUploadBean;
//import com.zige.robot.fsj.model.bean.PhotoListBean;
//import com.zige.robot.fsj.ui.album.AlbumUploadActivity;
//import com.zige.robot.fsj.ui.album.util.PickConfig;
//import com.zige.robot.fsj.ui.album.util.PickUtils;
//import com.zige.robot.fsj.util.AlbumUtil;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by PhoneSj on 2017/10/10.
// */
//
//public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    public static final int SPAN_COUNT = 5;
//
//    private Context context;
//    private List<PhotoListBean.ListBean> datas;
//    private FileAdapter.OnItemClickListener onItemClickListener;
//    private int spanCount;
//    private int scaleSize;
//
//    public PhotoAdapter(Context context, List<PhotoListBean.ListBean> datas, FileAdapter.OnItemClickListener onItemClickListener, int spanCount) {
//        this.context = context;
//        this.datas = datas;
//        this.spanCount = spanCount;
//        buildScaleSize();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return datas.get(position)
//                    .getViewType() == PhotoListBean.ListBean.VIEW_TYPE_NORMAL ? PhotoListBean.ListBean.VIEW_TYPE_NORMAL : PhotoListBean.ListBean.VIEW_TYPE_UPLOAD;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == PhotoListBean.ListBean.VIEW_TYPE_NORMAL) {
//            return new NormalViewHolder(LayoutInflater.from(context)
//                                                      .inflate(R.layout.item_album_file, parent, false));
//        } else {
//            return new UploadViewHolder(LayoutInflater.from(context)
//                                                      .inflate(R.layout.item_album_upload, parent, false));
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        if (datas.get(position).getViewType() == PhotoListBean.ListBean.VIEW_TYPE_NORMAL) {
//            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
//            String url = AlbumUtil.getThumbnailUrl(datas.get(position)
//                                                        .getPhotoUrl(), scaleSize, scaleSize);
//            ImageLoader.load(context, url, normalViewHolder.iv);
////            holder.itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    if (onItemClickListener != null) {
////                        onItemClickListener.onItemClick(position);
////                    }
////                }
////            });
//        } else {
//            UploadViewHolder uploadViewHolder = (UploadViewHolder) holder;
//
//            String path = datas.get(position).getPhotoUrl();//本地路径
//            ImageLoader.load(context, path, uploadViewHolder.ivPhoto);
//
//            PhotoListBean.ListBean.UploadState state = datas.get(position).getState();
//            int progress = (int) Math.round(datas.get(position).getPercent() * 100);
//            if (state == PhotoListBean.ListBean.UploadState.FAIL) {
//                uploadViewHolder.llUplaod.setVisibility(View.VISIBLE);
//                uploadViewHolder.tvUpload.setText("上传失败");
//                uploadViewHolder.pbUpload.setProgress(0);
//            } else if (state == PhotoListBean.ListBean.UploadState.UPLOADING) {
//                uploadViewHolder.llUplaod.setVisibility(View.VISIBLE);
//                uploadViewHolder.tvUpload.setText("正在上传");
//                uploadViewHolder.pbUpload.setProgress(progress);
//            } else if (state == PhotoListBean.ListBean.UploadState.FINISH) {
//                uploadViewHolder.llUplaod.setVisibility(View.VISIBLE);
//                uploadViewHolder.tvUpload.setText("上传完成");
//                uploadViewHolder.pbUpload.setProgress(100);
//            } else {
//                uploadViewHolder.llUplaod.setVisibility(View.VISIBLE);
//                uploadViewHolder.tvUpload.setText("等待上传");
//                uploadViewHolder.pbUpload.setProgress(0);
//                ((AlbumUploadActivity) context).uploadPhoto(position);
//            }
//
////            holder.itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    if (onItemClickListener != null) {
////                        onItemClickListener.onItemClick(position);
////                    }
////                }
////            });
//
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return datas == null ? 0 : datas.size();
//    }
//
//    private void buildScaleSize() {
//        int screenWidth = PickUtils.getInstance(context).getWidthPixels();
//        int space = PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE);
//        scaleSize = (screenWidth - (spanCount + 1) * space) / spanCount;
//    }
//
//    class NormalViewHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.iv)
//        ImageView iv;
//
//        public NormalViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
//            params.width = scaleSize;
//            params.height = scaleSize;
//        }
//    }
//
//    class UploadViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView ivPhoto;
//        public RelativeLayout llUplaod;
//        public TextView tvUpload;
//        public ProgressBar pbUpload;
//
//        public UploadViewHolder(View itemView) {
//            super(itemView);
//            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
//            llUplaod = (RelativeLayout) itemView.findViewById(R.id.ll_upload);
//            tvUpload = (TextView) itemView.findViewById(R.id.tv_upload);
//            pbUpload = (ProgressBar) itemView.findViewById(R.id.pb_upload);
//
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
//            params.width = scaleSize;
//            params.height = scaleSize;
//
//            llUplaod.getLayoutParams().width = scaleSize;
//            llUplaod.getLayoutParams().height = scaleSize;
//
//            pbUpload.getLayoutParams().height = scaleSize / 10;
//        }
//
//    }
//}
