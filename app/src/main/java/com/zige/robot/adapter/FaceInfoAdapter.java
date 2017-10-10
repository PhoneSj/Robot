package com.zige.robot.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zige.colorrecolibrary.DubePersonList;
import com.zige.robot.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zige on 2017/9/1.
 */

public class FaceInfoAdapter extends BaseAdapter {

    Context context;
    ArrayList<DubePersonList.UserInfoListBean> persons = new ArrayList<>();

    public FaceInfoAdapter(Context context, ArrayList<DubePersonList.UserInfoListBean> persons) {
        this.context = context;
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_faceinfo, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        String sex ="";
        if ("1".equals(persons.get(i).getSex()+"")){
            sex = "男";
        }else if ("0".equals(persons.get(i).getSex()+"")){
            sex = "女";
        }
        String age = "";
        if (!"0".equals(persons.get(i).getAge())){
            age = persons.get(i).getAge()+"岁";
        }
        holder.textName.setText(persons.get(i).getUsername());
        holder.textInfo.setText(persons.get(i).getTitle() + " " + sex + " " + age);

        if (!TextUtils.isEmpty(persons.get(i).getUrlFace1())){
            Glide.with(context).load(persons.get(i).getUrlFace1()).into(holder.imgFace1);
            holder.imgFace1.setVisibility(View.VISIBLE);
        }else {
            holder.imgFace1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(persons.get(i).getUrlFace2())){
            Glide.with(context).load(persons.get(i).getUrlFace2()).into(holder.imgFace2);
            holder.imgFace2.setVisibility(View.VISIBLE);
        }else {
            holder.imgFace2.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(persons.get(i).getUrlFace3())){
            Glide.with(context).load(persons.get(i).getUrlFace3()).into(holder.imgFace3);
            holder.imgFace3.setVisibility(View.VISIBLE);
        }else {
            holder.imgFace3.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_info)
        TextView textInfo;
        @BindView(R.id.img_face3)
        ImageView imgFace3;
        @BindView(R.id.img_face2)
        ImageView imgFace2;
        @BindView(R.id.img_face1)
        ImageView imgFace1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
