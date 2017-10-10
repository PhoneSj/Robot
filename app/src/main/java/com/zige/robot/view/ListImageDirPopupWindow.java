package com.zige.robot.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.zige.robot.base.CommonBaseAdapter;
import com.zige.robot.bean.AlbumPhotoEntity;
import com.zige.robot.utils.ImageLoaderUtils;

import java.util.List;

import com.zige.robot.R;


public class ListImageDirPopupWindow extends BasePopupWindowForListView<AlbumPhotoEntity> {

    private ListView mListDir;

    private String mSelectCurrentPath = "所有图片";

    public ListImageDirPopupWindow(int width, int height, List<AlbumPhotoEntity> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(new CommonBaseAdapter<AlbumPhotoEntity>(context, mDatas) {
            @Override
            public int getLayoutId() {
                return R.layout.list_dir_item;
            }

            @Override
            public void conner(ViewHolder holder, AlbumPhotoEntity entity) {
                holder.setText(R.id.id_dir_item_name, entity.getName().replace("/",""));
                ImageLoaderUtils.displayImage(context, (ImageView) holder.getView(R.id.id_dir_item_image), entity.getFirstImagePath());
                holder.setText(R.id.id_dir_item_count, entity.getCount() + "张");
                ImageView iv_selected = holder.getView(R.id.iv_selected);
                if (entity.getName().equals(mSelectCurrentPath)) {
                    iv_selected.setVisibility(View.VISIBLE);
                } else {
                    iv_selected.setVisibility(View.GONE);
                }
            }
        });
    }

    public interface
    OnImageDirSelected {
        void selected(AlbumPhotoEntity floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectCurrentPath = mDatas.get(position).getName();
                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDatas.get(position));
                }
            }
        });
    }

    @Override
    public void init() {
    }

}
