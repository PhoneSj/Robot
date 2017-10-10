package com.zige.robot.fsj.ui.album.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.model.bean.AlbumBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PhoneSj on 2017/9/25.
 */

public class DirAdapter extends RecyclerView.Adapter<DirAdapter.MyViewHolder> {

    private Context context;
    private List<AlbumBean.ListBean> datas;
    private int current;

    public DirAdapter(Context context, List<AlbumBean.ListBean> datas, int current) {
        this.context = context;
        this.datas = datas;
        this.current = current;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                                              .inflate(R.layout.item_album_dir, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (datas.get(position).isSelected() || current == position) {
            holder.rbCheck.setChecked(true);
            current = position;
        } else {
            holder.rbCheck.setChecked(false);
        }
        holder.tvDir.setText(datas.get(position).getPhotoAlbumName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current != position) {
                    datas.get(current).setSelected(false);
                    notifyItemChanged(current);
                    current = position;
                    datas.get(current).setSelected(true);
                    notifyItemChanged(current);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setSelectedAlbumId(long selectedAlubmId) {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getPhotoAlbumId() == selectedAlubmId) {
                current = i;
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_dir)
        TextView tvDir;
        @BindView(R.id.rb_check)
        RadioButton rbCheck;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public int getCurrent() {
        return current;
    }

}
