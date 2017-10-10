package com.zige.robot.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.http.rxhttp.BaseSubscriber;
import com.zige.robot.http.rxhttp.Datacenter;
import com.zige.robot.http.rxhttp.query.QueryUserBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.http.rxhttp.reponse.HomeWorkListBean;
import com.zige.robot.utils.MediaPlayerManager;
import com.zige.robot.utils.SystemUtils;

import java.util.List;

/**
 * Created by ldw on 2017/8/7.
 */

public class HomeWorkMsgAdapter  extends BaseAdapter{

    public static final int TYPE_TXT   = 0; //文本
    public static final int TYPE_VOICE = 1; //语音

    private Context mContext;
    private List<HomeWorkListBean.DataBean.ListBean.LeaveMessagesBean> mList;

    public HomeWorkMsgAdapter(Context context, List<HomeWorkListBean.DataBean.ListBean.LeaveMessagesBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getMessageType()-1 == TYPE_TXT){
            return TYPE_TXT;
        }else if(mList.get(position).getMessageType()-1 == TYPE_VOICE){
            return TYPE_VOICE;
        }else {
            return super.getItemViewType(position);
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TxtViewHolder txtViewHolder;
        VoiceViewHolder voiceViewHolder;
        if(getItemViewType(position) == TYPE_TXT){
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_txt_msg, null);
                txtViewHolder = new TxtViewHolder();
                txtViewHolder.tv_msg_txt = convertView.findViewById(R.id.tv_msg_txt);
                convertView.setTag(txtViewHolder);
            }else {
                txtViewHolder = (TxtViewHolder) convertView.getTag();
            }
            txtViewHolder.tv_msg_txt.setText(mList.get(position).getMessageContent()); //留言文本内容

        }else if(getItemViewType(position) == TYPE_VOICE){
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_voice_msg, null);
                voiceViewHolder = new VoiceViewHolder();
                voiceViewHolder.iv_voice = convertView.findViewById(R.id.iv_voice);
                voiceViewHolder.tv_voice_time = convertView.findViewById(R.id.tv_voice_time);
                voiceViewHolder.rl_msg_click = convertView.findViewById(R.id.rl_msg_click);
                voiceViewHolder.rl_msg_click.setBackgroundResource(R.drawable.voice_rect_bg);
                voiceViewHolder.tv_voice_time.setTextColor(mContext.getResources().getColor(R.color.white));
                voiceViewHolder.iv_voice.setImageResource(R.drawable.voice_white_3);
                convertView.setTag(voiceViewHolder);
            }else {
                voiceViewHolder = (VoiceViewHolder) convertView.getTag();
            }
            voiceViewHolder.rl_msg_click.setOnClickListener(new View.OnClickListener() { //语音播放
                @Override
                public void onClick(View view) {
                    //播放前重置。
                    MediaPlayerManager.release();
                    //开始实质播放
                    MediaPlayerManager.getInstance(mContext).playSound(mList.get(position).getMessageContent(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                        }
                    },true);
                    if(mList.get(position).getOwner() == 2)
                     setLeaveMsgReaded(mList.get(position).getLeaveMessageId());
                }
            });
            voiceViewHolder.tv_voice_time.setText(mList.get(position).getDuration()+"\"");
        }
        return convertView;
    }

    private void setLeaveMsgReaded(Long leaveMessageId){
        Datacenter.get().leaveMsgReaded(new BaseSubscriber<BaseCode>(mContext) {
            @Override
            protected void onUserSuccess(BaseCode baseCode) {
                if(baseCode.getCode() == BaseCode.SUCCESS_CODE){
                    Log.d("setLeaveMsgReaded", "onUserSuccess: ");
                }else {
                    Log.d("setLeaveMsgReaded", "onError: ");
                }
            }
        }, new QueryUserBean(App.getInstance().getPhone(), SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid(), leaveMessageId));
    }


    public  class TxtViewHolder{
        TextView tv_msg_txt;
    }

    public  class VoiceViewHolder{
        RelativeLayout rl_msg_click;
        ImageView iv_voice;
        TextView  tv_voice_time;

    }
}
