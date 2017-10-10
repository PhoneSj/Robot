package com.zige.robot.fsj.ui.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.component.ImageLoader;
import com.zige.robot.fsj.model.bean.AlbumBean;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.ui.album.widget.SwipeLayout;
import com.zige.robot.fsj.util.AlbumUtil;

import java.util.List;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class RemoteAdapter extends BaseAdapter {

    private static final int SPAN_COUNT = 4;
    private Context context;
    private List<AlbumBean.ListBean> datas;
    private int spanCount;
    private int scaleSize;
    private int imgSize;
    private OnItemClickListener onItemClickListener;
    private OnItemDeleteListener onItemDeleteListener;
    private OnItemRenameListener onItemRenameListener;

    public RemoteAdapter(Context context, List<AlbumBean.ListBean> datas) {
        this.context = context;
        this.datas = datas;
        this.spanCount = SPAN_COUNT;
        buildScaleSize();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context)
                                        .inflate(R.layout.item_album_remote, viewGroup, false);
            holder.tvName = convertView.findViewById(R.id.tv_name);
            holder.tvCount = convertView.findViewById(R.id.tv_count);
            holder.llContainer = convertView.findViewById(R.id.ll_container);
            holder.layoutRoot = convertView.findViewById(R.id.layout_root);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete);
            holder.btnRename = convertView.findViewById(R.id.btn_rename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //显示里面的四张图片
        List<String> intorImages = datas.get(position).getIntroImages();
        for (int i = 0; i < spanCount; i++) {
            holder.llContainer.getLayoutParams().width = scaleSize;
//                              holder.llContainer.getLayoutParams().height = imgSize + dp2px(8 * 2);
            ImageView imageView = (ImageView) holder.llContainer.getChildAt(i);
            if (i < intorImages.size()) {
                imageView.setVisibility(View.VISIBLE);
                //拼接缩略图rul
                String url = AlbumUtil.getThumbnailUrl(intorImages.get(i), scaleSize, scaleSize);
                ImageLoader.load(context, url, imageView);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
            imageView.getLayoutParams().width = imgSize;
            imageView.getLayoutParams().height = imgSize;
        }
        holder.tvName.setText(datas.get(position).getPhotoAlbumName());
        holder.tvCount.setText(datas.get(position).getCount() + "张");
        //删除回调
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemDeleteListener != null) {
                    onItemDeleteListener.onItemDelete(position);
                }
            }
        });
        //重命名回调
        holder.btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemRenameListener != null) {
                    onItemRenameListener.onItemRename(position);
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tvName;
        TextView tvCount;
        LinearLayout llContainer;
        SwipeLayout layoutRoot;

        Button btnDelete;
        Button btnRename;
    }

    private void buildScaleSize() {
        int screenWidth = PickUtils.getInstance(context).getWidthPixels();
        scaleSize = screenWidth - dp2px(8 * 2);//xml中设置的padding值
        imgSize = (scaleSize - dp2px((spanCount + 1) * 8)) / spanCount;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(int position);
    }

    public interface OnItemRenameListener {
        void onItemRename(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setOnItemRenameListener(OnItemRenameListener onItemRenameListener) {
        this.onItemRenameListener = onItemRenameListener;
    }

    public int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
