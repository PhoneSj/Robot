package com.zige.robot.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.zige.robot.base.CommonBaseAdapter;
import com.zige.robot.utils.ImageLoaderUtils;
import com.zige.robot.view.ViewHolder;

import java.util.LinkedList;
import java.util.List;

import com.zige.robot.R;

/**
 * 类描述：本地图片适配器
 * 创建人：Shirley
 */
public class LocalPhotoSelectorAdapter extends CommonBaseAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<>();

    private boolean mIsMulti;//是否多选

    private OnLocalPhotoSelectListener mListener;

    public void setAlbumSelectListener(OnLocalPhotoSelectListener mListener) {
        this.mListener = mListener;
    }

    public LocalPhotoSelectorAdapter(Activity context, List<String> mDataList, boolean isMulti) {
        super(context, mDataList);
        this.mIsMulti = isMulti;
    }

    public static List<String> getSelectedImage() {
        return mSelectedImage;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_photo_selector;
    }

    @Override
    public void conner(ViewHolder holder, final String entity) {
        //设置no_pic
        holder.setImageResource(R.id.id_item_image, R.drawable.loading_wait);
        final ImageView iv_select = holder.getView(R.id.id_item_select);
        final ImageView mImageView = holder.getView(R.id.id_item_image);
        if (!mIsMulti) {
            iv_select.setVisibility(View.GONE);
        } else {
            //设置选中状态
            if (mSelectedImage.contains(entity)) {
                iv_select.setImageResource(R.drawable.mine_wallet_saveway_selected);
            } else {
                iv_select.setImageResource(0);
            }
            /**
             * 已经选择过的图片，显示出选择过的效果
             */
            if (mSelectedImage.contains(entity)) {
                iv_select.setImageResource(R.drawable.mine_wallet_saveway_selected);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
        }


        mImageView.setTag(iv_select);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelectOperation = true;
                if (mIsMulti) {
                    if (mSelectedImage.contains(entity)) {
                        isSelectOperation = false;
                    }
                    if (mListener == null ||
                            mListener.isInterceptSelect(mSelectedImage.size(), isSelectOperation)) {
                        if (!mSelectedImage.contains(entity)) {
                            mSelectedImage.add(entity);
                        } else {
                            mSelectedImage.remove(entity);
                        }
                        ImageView iv_state = (ImageView) v.getTag();
                        //设置选中状态
                        if (isSelectOperation) {
                            iv_state.setImageResource(R.drawable.mine_wallet_saveway_selected);
                        } else {
                            iv_state.setImageResource(R.drawable.loading_wait);
                        }
                    }
                } else {
                    mSelectedImage.clear();
                    mSelectedImage.add(entity);
                }
                if (mListener != null) {
                    mListener.afterSelect(mSelectedImage.size(), isSelectOperation);
                }
            }
        });

        //设置图片
        ImageLoaderUtils.displayImage(mContext, mImageView, entity);

    }

    /**
     * 相片选择事件
     */
    public interface OnLocalPhotoSelectListener {

        /**
         * 是否拦截选择图片时间
         *
         * @param countBeforeSelected 当前已选择的图片数量,不包括正要选择的图片
         * @param isCheck             是否是选中图片，false,则表示是取消选中
         * @return 返回true 表示拦截该时间,false表示不拦截该事件
         */
        boolean isInterceptSelect(int countBeforeSelected, boolean isCheck);

        /**
         * 选择完相片后触发事件
         *
         * @param isCheck 是否是选中图片，false,则表示是取消选中
         */
        void afterSelect(int countAfaterSelected, boolean isCheck);

    }
}
