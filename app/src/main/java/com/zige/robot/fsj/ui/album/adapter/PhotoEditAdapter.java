package com.zige.robot.fsj.ui.album.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.component.ImageLoader;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.util.AlbumUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class PhotoEditAdapter extends RecyclerView.Adapter<PhotoEditAdapter.MyViewHolder> {

    public static final int SPAN_COUNT = 5;
    private Context context;
    private List<PhotoListBean.ListBean> datas;
    private ArrayList<Integer> selectPosList;
    private ArrayList<Long> selectPhotoIds;
    private ArrayList<String> selectPaths;
    private int spanCount;
    private int scaleSize;
    private OnItemClickListener onItemClickListener;
    private Type type;

    public enum Type {
        local, remote
    }

    public PhotoEditAdapter(Context context, List<PhotoListBean.ListBean> datas, Type type) {
        this.context = context;
        this.datas = datas;
        this.type = type;
        this.spanCount = SPAN_COUNT;
        buildScaleSize();
        selectPosList = new ArrayList<>();
        selectPhotoIds = new ArrayList<>();
        selectPaths = new ArrayList<>();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                                              .inflate(R.layout.item_album_photo_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PhotoListBean.ListBean bean = datas.get(position);
        String url = bean.getPhotoUrl();
        if (type == Type.remote) {
            url = AlbumUtil.getThumbnailUrl(bean.getPhotoUrl(), scaleSize, scaleSize);
        }
        ImageLoader.load(context, url, holder.ivPhoto);
        if (bean.isSelected()) {
            holder.select();
        } else {
            holder.unSelect();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bean.isSelected()) {
                    holder.select();
                    datas.get(position).setSelected(true);
                    if (!selectPosList.contains(Integer.valueOf(position))) {
                        selectPosList.add(Integer.valueOf(position));
                        selectPhotoIds.add(datas.get(position).getPhotoId());
                        selectPaths.add(datas.get(position).getPhotoUrl());
                    }
                } else {
                    holder.unSelect();
                    datas.get(position).setSelected(false);
                    if (selectPosList.contains(Integer.valueOf(position))) {
                        selectPosList.remove(Integer.valueOf(position));
                        selectPhotoIds.remove(datas.get(position).getPhotoId());
                        selectPaths.remove(datas.get(position).getPhotoUrl());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.fl_container)
        FrameLayout flContainer;
        @BindView(R.id.layout_root)
        RelativeLayout layoutRoot;

        private ImageView weekImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
            params.width = scaleSize;
            params.height = scaleSize;

            final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(ivPhoto);
            weekImage = imageViewWeakReference.get();
        }

        void select() {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.choice_btn_act);
//            drawable.clearColorFilter();
//            drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ivSelect.setBackground(ContextCompat.getDrawable(context, R.drawable.choice_btn_act));
            } else {
                //noinspection deprecation
                ivSelect.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.choice_btn_act));
            }
        }

        void unSelect() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ivSelect.setBackground(ContextCompat.getDrawable(context, R.drawable.choice_btn_nor));
            } else {
                //noinspection deprecation
                ivSelect.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.choice_btn_nor));
            }
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

    public ArrayList<Integer> getSelectPosList() {
        return selectPosList;
    }

    public ArrayList<Long> getSelectPhotoIds() {
        return selectPhotoIds;
    }

    public ArrayList<String> getSelectPaths() {
        return selectPaths;
    }

    /**
     * 清空选中数据及更新ui
     */
    public void clearSelected() {
        for (int i = 0; i < selectPosList.size(); i++) {
            datas.get(selectPosList.get(i)).setSelected(false);
            notifyItemChanged(selectPosList.get(i));
        }
        selectPosList.clear();
        selectPhotoIds.clear();
        selectPaths.clear();
    }
}
