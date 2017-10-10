package com.zige.robot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidingwei on 2016/7/11.
 * 万能adapter基类
 */
public abstract class CommonLvAdapter<T> extends BaseAdapter
{

    private List<T> mDatas;
    private Context mContext;
    private int mLayoutId;

    public CommonLvAdapter(Context context, List<T> list, int layoutId)
    {
        mDatas = list;
        mContext = context;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount()
    {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CommonLvViewHolder holder = CommonLvViewHolder.getViewHoler(mContext, parent, mLayoutId, convertView);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    public abstract  void convert(CommonLvViewHolder holder, T bean, int position);

    public List<T> getDatas()
    {
        return mDatas;
    }

    /**
     *
     * @param list 数据源list
     */
    public void setDatas(List<T> list)
    {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    /**
     * 添加数据集合
     * @param list
     */
    public void addList(List<T> list)
    {
        if(mDatas==null)
        {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一个数据对象
     * @param t
     */
    public void addItem(T t)
    {
        if(mDatas==null)
        {
            mDatas = new ArrayList<>();
        }
        mDatas.add(t);
        notifyDataSetChanged();
    }

    /**
     * 删除一个数据对象
     * @param t
     */
    public void removeItem(T t)
    {
        if(mDatas==null)
        {
            mDatas = new ArrayList<>();
        }
        mDatas.remove(t);
        notifyDataSetChanged();
    }


    /**
     * 删除一个数据对象
     * @param position
     */
    public void removeItemFromPositon(int position)
    {
        if(mDatas==null)
        {
            mDatas = new ArrayList<>();
        }
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void clearAll()
    {
        if(mDatas!=null)
        {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }



}





















