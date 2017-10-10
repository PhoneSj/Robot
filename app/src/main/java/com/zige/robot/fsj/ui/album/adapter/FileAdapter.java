package com.zige.robot.fsj.ui.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.component.ImageLoader;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.util.AlbumUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PhoneSj on 2017/9/27.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    public static final int SPAN_COUNT = 5;
    private Context context;
    private List<PhotoListBean.ListBean> datas;
    private OnItemClickListener onItemClickListener;
    private int spanCount;
    private int scaleSize;

    public FileAdapter(Context context, List<PhotoListBean.ListBean> datas) {
        this.context = context;
        this.datas = datas;
        this.spanCount = SPAN_COUNT;
        buildScaleSize();
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                                            .inflate(R.layout.item_album_file, parent, false));
    }

    @Override
    public void onBindViewHolder(FileAdapter.ViewHolder holder, final int position) {
        String url = AlbumUtil.getThumbnailUrl(datas.get(position)
                                                    .getPhotoUrl(), scaleSize, scaleSize);
        ImageLoader.load(context, url, holder.iv);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
            params.width = scaleSize;
            params.height = scaleSize;
        }
    }

    private void buildScaleSize() {
        int screenWidth = PickUtils.getInstance(context).getWidthPixels();
        int space = PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE);
        scaleSize = (screenWidth - (spanCount + 1) * space) / spanCount;
    }

    public interface OnItemClickListener {
        void onItemClick(int positon);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
