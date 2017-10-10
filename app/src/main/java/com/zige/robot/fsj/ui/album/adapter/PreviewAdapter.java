package com.zige.robot.fsj.ui.album.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
import com.zige.robot.R;
import com.zige.robot.fsj.component.ImageLoader;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.ui.album.PreviewActivity;

import java.io.File;
import java.util.List;

/**
 * Created by PhoneSj on 2017/9/27.
 */

public class PreviewAdapter extends PagerAdapter {

    private Context context;
    private List<PhotoListBean.ListBean> datas;

    public PreviewAdapter(Context context, List<PhotoListBean.ListBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override

    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_preview, null, false);
        PhotoView photoView = view.findViewById(R.id.pv_preview);
        photoView.setEnabled(true);//启用缩放功能
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PreviewActivity) context).hideOrShowToolbar();
            }
        });
        photoView.enable();//启用缩放功能
        ImageLoader.load(context, datas.get(position).getPhotoUrl(), photoView);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
