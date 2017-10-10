package com.zige.robot.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lidingwei on 2016/7/11.
 */
public class CommonLvViewHolder
{
    private SparseArray<View> mViews;
    private View mConvertView;

    public  CommonLvViewHolder(Context context, ViewGroup parent,int layoutId)
    {
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static CommonLvViewHolder getViewHoler(Context context, ViewGroup parent, int layoutId, View convertView)
    {
        if(convertView == null)
        {
            return new CommonLvViewHolder(context, parent, layoutId);
        }
        return (CommonLvViewHolder) convertView.getTag();
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if(view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public CommonLvViewHolder setText(int viewId, String text)
    {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonLvViewHolder setTextColor(int viewId, int color)
    {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    public CommonLvViewHolder setImageResoure(int viewId, int resId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public CommonLvViewHolder setBackgroundResource(int viewId, int resId)
    {
        ImageView view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }


    public CommonLvViewHolder setRlBackgroundResource(int viewId, int resId)
    {
        RelativeLayout rlView = getView(viewId);
        rlView.setBackgroundResource(resId);
        return this;
    }
    public CommonLvViewHolder setLlBackgroundResource(int viewId, int resId)
    {
        LinearLayout rlView = getView(viewId);
        rlView.setBackgroundResource(resId);
        return this;
    }
    public CommonLvViewHolder setOnClickListener(int viewId, View.OnClickListener listener)
    {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonLvViewHolder setViewVisibility(int viewId, int visibility)
    {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

}