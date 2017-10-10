package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zige.robot.App;
import com.zige.robot.AppConfig;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.http.rxhttp.BaseSubscriber;
import com.zige.robot.http.rxhttp.Datacenter;
import com.zige.robot.http.rxhttp.query.QueryCraeateLeaveMsgBean;
import com.zige.robot.http.rxhttp.query.QueryHomeWorkBean;
import com.zige.robot.http.rxhttp.query.QueryUserBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.http.rxhttp.reponse.HomeWorkDetails;
import com.zige.robot.interf.SuccessVoiceListener;
import com.zige.robot.utils.DateFormatUtils;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.FileUtils;
import com.zige.robot.utils.MediaPlayerHelper;
import com.zige.robot.utils.QiniuUtil;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.view.WeiXinVoiceInputView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ldw on 2017/8/22.
 * 微信版作业详情
 */

public class HomeWorkDetailActivity extends BaseActivity {
    @BindView(R.id.tv_homework_time)
    TextView mTvHomeworkTime;
    @BindView(R.id.tv_homework_content)
    TextView mTvHomeworkContent;
    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.weixin_view)
    WeiXinVoiceInputView mWeixinView;

    Dialog homeWorkStateDialog; //确认完成状态dialog

    long homeworkId; //作业id

    HomeWorkDetails mHomeWorkDetails;  //作业详情

    QueryHomeWorkBean mQueryHomeWorkBean; //提交作业状态

    CommonLvAdapter<HomeWorkDetails.DataBean.LeaveMessagesBean> mLeaveMessagesBeanCommonLvAdapter;

    int volume_item_length_dip = 0; //单个语音条的长度
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    long subjectId;
    String subjectName;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_home_work_detail2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        volume_item_length_dip = ScreenUtils.getScreenW(mContext) / 10;
        homeworkId = getIntent().getLongExtra("homeworkId", 0);
        subjectId = getIntent().getLongExtra("subjectId", 0);
        subjectName = getIntent().getStringExtra("subjectName");
        setTitleName("详情");
        setIvActionbg(R.drawable.finish_btn);
        findViewById(R.id.iv_action).setVisibility(View.GONE);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(-1);
                finish();
            }
        });
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认完成作业
                homeWorkStateDialog.show();
            }
        });
        mWeixinView.setOnSuccessVoiceListener(new SuccessVoiceListener() {
            @Override
            public void getVoiceMsgPath(final String path, final long timeLength) {
                //新建一个录音文件
                QiniuUtil.getQiNiuToken(mContext, path, new QiniuUtil.UploadCallBack() {
                    @Override
                    public void isOk(String url, String localPath, String token) {
                        createLeaveMsg(timeLength, url, path);
                    }

                    @Override
                    public void isFail() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastShow("语音文件上传失败");
                            }
                        });
                    }
                });
            }
        });
        initAdapter();
        initDialog();
        getHomeWorkDetail();
        //作业修改
        if (null != subjectName) {
            llTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //添加作业
                    startActivityForResult(new Intent(mContext, HomeWorkEditActivity.class)
                            .putExtra("subjectId", subjectId)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("subjectName", subjectName)
                            .putExtra("content", getIntent().getStringExtra("content"))
                            .putExtra("data",mTvHomeworkTime.getText().toString().split("：")[1].trim())
                            .putExtra("type", 0),100); // 0新建作业
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayVoice();
//        MediaPlayerManager.release();
        MediaPlayerHelper.realese();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteAllFiles(new File(AppConfig.DEFAULT_VOICE_FILE_PATH)); //清除录音文件
    }


    AnimationDrawable voiceAnimation;
    ImageView selectImageView;

    private void startPlayVoice() {
        if (selectImageView.getTag().equals("1")) {//大人
            selectImageView.setImageResource(R.drawable.anim_voice_weixin_right);
        } else {//小孩
            selectImageView.setImageResource(R.drawable.anim_voice_weixin_left);
        }
        voiceAnimation = (AnimationDrawable) selectImageView.getDrawable();
        voiceAnimation.start();
    }

    private void stopPlayVoice() {
        if (selectImageView == null)
            return;
        if (voiceAnimation != null && voiceAnimation.isRunning()) {
            voiceAnimation.stop();
        }
        if (selectImageView.getTag().equals("1")) {//大人
            selectImageView.setImageResource(R.drawable.volume_r3_ico);

        } else {//小孩
            selectImageView.setImageResource(R.drawable.volume_l3_ico);

        }
    }

    /**
     * @param postion
     * @param leaveMessageId
     */
    private void setLeaveMsgReaded(final int postion, Long leaveMessageId) {
        Datacenter.get().leaveMsgReaded(new BaseSubscriber<BaseCode>(mContext) {
            @Override
            protected void onUserSuccess(BaseCode baseCode) {
                if (baseCode.getCode() == BaseCode.SUCCESS_CODE) {
                    mLeaveMessagesBeanCommonLvAdapter.getItem(postion).setIsRead(true);
                    mLeaveMessagesBeanCommonLvAdapter.notifyDataSetChanged();
                    Log.d("setLeaveMsgReaded", "onUserSuccess: ");
                } else {
                    Log.d("setLeaveMsgReaded", "onError: ");
                }
            }
        }, new QueryUserBean(mApplication.getPhone(), SystemUtils.getDeviceKey(), mApplication.getUserInfo().getDeviceid(), leaveMessageId));
    }

    /**
     * 留言消息adapter
     */
    private void initAdapter() {
        mLeaveMessagesBeanCommonLvAdapter = new CommonLvAdapter<HomeWorkDetails.DataBean.LeaveMessagesBean>(mContext,
                new ArrayList<HomeWorkDetails.DataBean.LeaveMessagesBean>(), R.layout.adapter_weixin_msg) {
            @Override
            public void convert(final CommonLvViewHolder holder, final HomeWorkDetails.DataBean.LeaveMessagesBean bean, final int position) {
                setMsgViewData(holder, bean);
                holder.setOnClickListener(R.id.rl_left_bubble, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.getOwner() == 2 && bean.getMessageType() == 2) { //小孩 语音
//                            //播放前重置。
//                            MediaPlayerManager.release();
//                           //开始实质播放
//                            MediaPlayerManager.getInstance(mContext).playSound(bean.getMessageContent(), new MediaPlayer.OnCompletionListener() {
//                                @Override
//                                public void onCompletion(MediaPlayer mp) {
//                                    //播放完成
//                                    stopPlayVoice();
//                                }
//                            }, true);
                            MediaPlayerHelper.playSoundThread(bean.getMessageContent(), new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            stopPlayVoice();
                                        }
                                    });
                                }
                            });
                            stopPlayVoice();
                            selectImageView = holder.getView(R.id.iv_left_volume);
                            selectImageView.setTag("2");//小孩
                            startPlayVoice();
                            if (!bean.isIsRead())
                                setLeaveMsgReaded(position, bean.getLeaveMessageId()); //标记语音留言为已读
                        }
                    }
                });

                holder.setOnClickListener(R.id.rl_right_bubble, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.getOwner() == 1 && bean.getMessageType() == 2) { //家长 语音
//                            //播放前重置。
//                            MediaPlayerManager.release();
//                           //开始实质播放
                            String voicePath;
                            if (!TextUtils.isEmpty(bean.getLocalVoicePath()) && new File(bean.getLocalVoicePath()).exists()) { //判断本地是否有语音文件
                                voicePath = bean.getLocalVoicePath();
                            } else {
                                voicePath = bean.getMessageContent();
                            }
//                            MediaPlayerManager.getInstance(mContext).playSound(voicePath, new MediaPlayer.OnCompletionListener() {
//                                @Override
//                                public void onCompletion(MediaPlayer mp) {
//                                    //播放完成
//                                    stopPlayVoice();
//                                }
//                            }, true);
                            MediaPlayerHelper.playSoundThread(voicePath, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            stopPlayVoice();
                                        }
                                    });

                                }
                            });
                            stopPlayVoice();
                            selectImageView = holder.getView(R.id.iv_right_volume);
                            selectImageView.setTag("1");//大人
                            startPlayVoice();
                        }
                    }
                });
            }
        };
        mListview.setAdapter(mLeaveMessagesBeanCommonLvAdapter);
    }


    /**
     * 消息赋值
     *
     * @param holder
     * @param bean
     */
    private void setMsgViewData(CommonLvViewHolder holder, HomeWorkDetails.DataBean.LeaveMessagesBean bean) {
        holder.setViewVisibility(R.id.rl_left_msg, View.GONE);
        holder.setViewVisibility(R.id.rl_right_msg, View.GONE);
        if (bean.getOwner() == 1) {    //家长
            holder.setViewVisibility(R.id.rl_right_msg, View.VISIBLE);
            if (bean.getMessageType() == 1) { //文本
                holder.setViewVisibility(R.id.ll_right_volume, View.GONE); //隐藏语音view
                holder.setViewVisibility(R.id.rl_right_voice_text, View.GONE); //隐藏语音翻译内容
                holder.setViewVisibility(R.id.tv_right_msg_content, View.VISIBLE); //显示作业内容
                holder.setText(R.id.tv_right_msg_content, bean.getMessageContent());
            } else if (bean.getMessageType() == 2) {  //语音
                holder.setViewVisibility(R.id.ll_right_volume, View.VISIBLE); //显示语音view
                if (bean.getDuration() >= 0 && bean.getDuration() <= 5) {
                    holder.getView(R.id.ll_right_volume).getLayoutParams().width = volume_item_length_dip * 2;
                } else if (bean.getDuration() > 5 && bean.getDuration() <= 10) {
                    holder.getView(R.id.ll_right_volume).getLayoutParams().width = volume_item_length_dip * 3;
                } else if (bean.getDuration() > 10 && bean.getDuration() <= 20) {
                    holder.getView(R.id.ll_right_volume).getLayoutParams().width = volume_item_length_dip * 4;
                } else {
                    holder.getView(R.id.ll_right_volume).getLayoutParams().width = volume_item_length_dip * 5;
                }
                holder.setViewVisibility(R.id.rl_right_voice_text, View.GONE); //隐藏语音翻译内容
                holder.setViewVisibility(R.id.tv_right_msg_content, View.GONE); //隐藏作业内容
                holder.setText(R.id.tv_right_volume_time, bean.getDuration() + "s");
            }
        } else if (bean.getOwner() == 2) {  //小孩
            holder.setViewVisibility(R.id.rl_left_msg, View.VISIBLE);
            holder.setViewVisibility(R.id.iv_unread_state, View.GONE); //语音未读状态
            if (bean.getMessageType() == 1) {  //文本
                holder.setViewVisibility(R.id.ll_left_volume, View.GONE); //隐藏语音view
                holder.setViewVisibility(R.id.rl_left_voice_text, View.GONE); //隐藏语音翻译内容
                holder.setViewVisibility(R.id.tv_left_msg_content, View.VISIBLE); //显示作业内容
                holder.setText(R.id.tv_left_msg_content, bean.getMessageContent());
            } else if (bean.getMessageType() == 2) {    //语音
                if (!bean.isIsRead())
                    holder.setViewVisibility(R.id.iv_unread_state, View.VISIBLE);
                holder.setViewVisibility(R.id.ll_left_volume, View.VISIBLE); //显示语音view
                if (bean.getDuration() > 0 && bean.getDuration() <= 5) {
                    holder.getView(R.id.ll_left_volume).getLayoutParams().width = volume_item_length_dip * 2;
                } else if (bean.getDuration() > 5 && bean.getDuration() <= 10) {
                    holder.getView(R.id.ll_left_volume).getLayoutParams().width = volume_item_length_dip * 3;
                } else if (bean.getDuration() > 10 && bean.getDuration() <= 20) {
                    holder.getView(R.id.ll_left_volume).getLayoutParams().width = volume_item_length_dip * 4;
                } else {
                    holder.getView(R.id.ll_left_volume).getLayoutParams().width = volume_item_length_dip * 5;
                }
                holder.setViewVisibility(R.id.rl_left_voice_text, View.GONE); //隐藏语音翻译内容
                holder.setViewVisibility(R.id.tv_left_msg_content, View.GONE); //隐藏作业内容
                holder.setText(R.id.tv_left_volume_time, bean.getDuration() + "s");
            }
        }
    }

    TextView tv_dialog_homework_content;

    private void initDialog() {
        homeWorkStateDialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_modify_work, 0, 0);
        tv_dialog_homework_content = homeWorkStateDialog.findViewById(R.id.tv_homework_content);
        homeWorkStateDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //取消
                homeWorkStateDialog.dismiss();
            }
        });
        homeWorkStateDialog.findViewById(R.id.btn_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //确认完成
                homeWorkStateDialog.dismiss();
                modifyHomeWork();
            }
        });

    }

    /**
     * 创建一条语音留言
     *
     * @param voice_time
     * @param messageContent
     */
    private void createLeaveMsg(final Long voice_time, final String messageContent, final String localVoicePath) {
        showProgressDialog("正在加载...", true);
        Datacenter.get().createLeaveMsg(new BaseSubscriber<BaseCode>(mContext) {
            @Override
            protected void onUserSuccess(BaseCode baseCode) {
                dismissProgressDialog();
                if (baseCode.getCode() == BaseCode.SUCCESS_CODE) {
                    HomeWorkDetails.DataBean.LeaveMessagesBean leaveMessagesBean = new HomeWorkDetails.DataBean.LeaveMessagesBean(voice_time, false, messageContent, 2, 1);
                    leaveMessagesBean.setLocalVoicePath(localVoicePath);
                    mHomeWorkDetails.getData().getLeaveMessages().add(leaveMessagesBean);//添加一条语音留言
                    mLeaveMessagesBeanCommonLvAdapter.setDatas(mHomeWorkDetails.getData().getLeaveMessages());
                } else {
                    toastShow(baseCode.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dismissProgressDialog();
            }
        }, new QueryCraeateLeaveMsgBean(App.getInstance().getPhone(), SystemUtils.getDeviceKey()
                , App.getInstance().getUserInfo().getDeviceid(), homeworkId, voice_time, 2, messageContent));
    }

    /**
     * 获取作业详情
     */
    private void getHomeWorkDetail() {
        showProgressDialog("正在加载", true);
        Datacenter.get().homeWorkDetail(new BaseSubscriber<HomeWorkDetails>(mContext) {
            @Override
            protected void onUserSuccess(HomeWorkDetails homeWorkDetails) {
                dismissProgressDialog();
                if (homeWorkDetails.getCode() == BaseCode.SUCCESS_CODE) {
                    mHomeWorkDetails = homeWorkDetails;
                    if (homeWorkDetails.getData().isIsDone()) {
                        findViewById(R.id.iv_action).setVisibility(View.GONE);
                        setTitleName("详情(已完成)");
                    } else {
                        findViewById(R.id.iv_action).setVisibility(View.VISIBLE);
                        setTitleName("详情(未完成)");
                    }
                    mTvHomeworkTime.setText("期限：" + DateFormatUtils.Stamp2DateFormat(homeWorkDetails.getData().getStartTime() * 1000, "yyyy.M.d") + "  -  "
                            + DateFormatUtils.Stamp2DateFormat(homeWorkDetails.getData().getEndTime() * 1000, "yyyy.M.d"));
                    mTvHomeworkContent.setText(homeWorkDetails.getData().getContent());
                    tv_dialog_homework_content.setText(homeWorkDetails.getData().getContent()); //dialog 作业内容
                    if (homeWorkDetails.getData().getLeaveMessages() != null && homeWorkDetails.getData().getLeaveMessages().size() > 0) {
                        mLeaveMessagesBeanCommonLvAdapter.setDatas(homeWorkDetails.getData().getLeaveMessages()); //留言listview
                        HomeWorkDetails.DataBean.LeaveMessagesBean msg = null;
                        for (int i = 0; i < homeWorkDetails.getData().getLeaveMessages().size(); i++) {
                            msg = homeWorkDetails.getData().getLeaveMessages().get(i);
                            if (msg.getMessageType() == 1 && !msg.isIsRead()) {
                                //文本留言并是未读设置为已读
                                setLeaveMsgReaded(i, msg.getLeaveMessageId());
                            }
                        }

                    }
                } else {
                    toastShow(homeWorkDetails.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG, "onError: " + e.getMessage());
                dismissProgressDialog();
            }
        }, homeworkId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getHomeWorkDetail();
    }

    /**
     * 修改作业 把作业状态改为true
     */
    private void modifyHomeWork() {
        mQueryHomeWorkBean = new QueryHomeWorkBean();
        mQueryHomeWorkBean.setUsername(mApplication.getPhone());
        mQueryHomeWorkBean.setDeviceId(SystemUtils.getDeviceKey());
        mQueryHomeWorkBean.setRobotDeviceId(mApplication.getUserInfo().getDeviceid());
        mQueryHomeWorkBean.setHomeworkId(mHomeWorkDetails.getData().getHomeworkId());
        mQueryHomeWorkBean.setSubjectId(mHomeWorkDetails.getData().getSubjectId());
        mQueryHomeWorkBean.setIsDone(true);
        mQueryHomeWorkBean.setContent(mHomeWorkDetails.getData().getContent());
        mQueryHomeWorkBean.setStartTime(mHomeWorkDetails.getData().getStartTime());
        mQueryHomeWorkBean.setEndTime(mHomeWorkDetails.getData().getEndTime());
        List<QueryHomeWorkBean.LeaveMessagesBean> msgList = new ArrayList<>(); //作业留言可以不上创。。。。
        mQueryHomeWorkBean.setLeaveMessages(msgList);
        Datacenter.get().chageHomeWork(new BaseSubscriber<BaseCode>(mContext) {
            @Override
            protected void onUserSuccess(BaseCode baseCode) {
                if (baseCode.getCode() == BaseCode.SUCCESS_CODE) {
                    toastShow("确认成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    toastShow(baseCode.getMessage());
                }
            }
        }, mQueryHomeWorkBean);
    }


}
