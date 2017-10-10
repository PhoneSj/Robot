package com.zige.robot.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zige.robot.AppConfig;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.http.rxhttp.BaseSubscriber;
import com.zige.robot.http.rxhttp.Datacenter;
import com.zige.robot.http.rxhttp.query.QueryHomeWorkBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.interf.SuccessVoiceListener;
import com.zige.robot.utils.DateFormatUtils;
import com.zige.robot.utils.FileUtils;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.MediaPlayerHelper;
import com.zige.robot.utils.QiniuUtil;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.view.VoiceRecordBtn;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lidingwei on 2017/4/7 16:04
 *         作业编辑
 */

public class HomeWorkEditActivity extends BaseActivity {

    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_completion_time)
    TextView mTvCompletionTime;
    @BindView(R.id.ll_complete_time_click)
    LinearLayout mLlCompleteTimeClick;
    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.ll_msg)
    LinearLayout mLlMsg;
    @BindView(R.id.record_btn)
    VoiceRecordBtn mRecordBtn;

    CommonLvAdapter<QueryHomeWorkBean.LeaveMessagesBean> mCommonLvAdapter;
    QueryHomeWorkBean mQueryHomeWorkBean = new QueryHomeWorkBean();
    ;
    long subjectId, homeworkId; //科目id
    String subjectName; //科目名称
    int type; //0新建作业
    int volume_item_length_dip = 0; //单个语音条的长度


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_home_work_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        volume_item_length_dip = ScreenUtils.getScreenW(mContext) / 10;
        subjectId = getIntent().getLongExtra("subjectId", 0);
        subjectName = getIntent().getStringExtra("subjectName");
        homeworkId = getIntent().getLongExtra("homeworkId", 0);
        String content = getIntent().getStringExtra("content");
        String data = getIntent().getStringExtra("data");
        if (null != content) {
            mEtInput.setText(content);
            mEtInput.setSelection(mEtInput.getText().length());
            mQueryHomeWorkBean.setContent(content);
        }
        if (null != data) {
            String startTime = data.split("-")[0].trim() + " 00:00:00";
            String endTime = data.split("-")[1].trim() + " 23:59:59";
            mTvCompletionTime.setText(data);
            mQueryHomeWorkBean.setStartTime(DateFormatUtils.DateFormat2Stamp(startTime, "yyyy-M-d HH:mm:ss") / 1000); //秒级时间
            mQueryHomeWorkBean.setEndTime(DateFormatUtils.DateFormat2Stamp(endTime, "yyyy-M-d HH:mm") / 1000);
        }
        type = getIntent().getIntExtra("type", 0);
        setTitleName(subjectName);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTvActionText("确定");
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交作业
                if (mQueryHomeWorkBean.getStartTime() == 0 || mQueryHomeWorkBean.getEndTime() == 0) {
                    toastShow("请选择完成日期");
                    return;
                }
                if (TextUtils.isEmpty(mQueryHomeWorkBean.getContent()) && (mQueryHomeWorkBean.getLeaveMessages() == null || mQueryHomeWorkBean.getLeaveMessages().size() == 0)) {
                    toastShow("作业内容不能为空");
                    return;
                }
                httpCreateHomeWork();
            }
        });
        mRecordBtn.setOnSuccessVoiceListener(new SuccessVoiceListener() {
            @Override
            public void getVoiceMsgPath(final String path, final long timeLength) {
                Log.d(TAG, "getVoiceMsgPath: " + path);
                QiniuUtil.getQiNiuToken(mContext, path, new QiniuUtil.UploadCallBack() {
                    @Override
                    public void isOk(final String url, final String localPath, String token) {
                        Log.d(TAG, "isOk: qiniu upload  token: " + token);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mLlMsg.getVisibility() == View.GONE) {
                                    mLlMsg.setVisibility(View.VISIBLE);   //创建了语音留言显示view
                                }
                                QueryHomeWorkBean.LeaveMessagesBean leaveMessagesBean = new QueryHomeWorkBean.LeaveMessagesBean((int) timeLength, url, 2);
                                leaveMessagesBean.setLocalVoicePath(path); //本地语音文件路径
                                mQueryHomeWorkBean.getLeaveMessages().add(leaveMessagesBean); //增加1条语音作业
                                mCommonLvAdapter.setDatas(mQueryHomeWorkBean.getLeaveMessages());
                                mListview.setSelection(mCommonLvAdapter.getCount() - 1);
                            }
                        });

                    }

                    @Override
                    public void isFail() {
                        Log.d(TAG, "isFail: qiniu upload");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastShow("网络不好！");
                            }
                        });
                    }
                });
            }
        });
        //作业内容输入框
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mQueryHomeWorkBean.setContent(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initData();
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
        FileUtils.deleteAllFiles(new File(AppConfig.DEFAULT_VOICE_FILE_PATH)); //清除媒体文件
    }

    /**
     * 创建作业
     */
    private void httpCreateHomeWork() {
        showProgressDialog("正在布置作业...", true);
        Log.d(TAG, "httpCreateHomeWork: " + GsonUtils.getObjectToJson(mQueryHomeWorkBean));
        Datacenter.get().chageHomeWork(new BaseSubscriber<BaseCode>(mContext) {
            @Override
            protected void onUserSuccess(BaseCode baseCode) {
                dismissProgressDialog();
                if (baseCode.getCode() == BaseCode.SUCCESS_CODE) {
                    toastShow("布置作业成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    toastShow(baseCode.getMessage());
                }
            }

            @Override
            protected void onUserError(Throwable ex) {
                super.onUserError(ex);
                dismissProgressDialog();
                Log.d(TAG, "onUserError: " + ex.getMessage());
            }
        }, mQueryHomeWorkBean);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mCommonLvAdapter = new CommonLvAdapter<QueryHomeWorkBean.LeaveMessagesBean>(mContext, new ArrayList<QueryHomeWorkBean.LeaveMessagesBean>(), R.layout.adapter_voice_msg) {
            @Override
            public void convert(final CommonLvViewHolder holder, final QueryHomeWorkBean.LeaveMessagesBean bean, int position) {
                holder.setText(R.id.tv_voice_time, bean.getDuration() + "\"");
                if (bean.getDuration() >= 0 && bean.getDuration() <= 5) {
                    holder.getView(R.id.rl_msg_click).getLayoutParams().width = volume_item_length_dip * 2;
                } else if (bean.getDuration() > 5 && bean.getDuration() <= 10) {
                    holder.getView(R.id.rl_msg_click).getLayoutParams().width = volume_item_length_dip * 3;
                } else if (bean.getDuration() > 10 && bean.getDuration() <= 20) {
                    holder.getView(R.id.rl_msg_click).getLayoutParams().width = volume_item_length_dip * 4;
                } else {
                    holder.getView(R.id.rl_msg_click).getLayoutParams().width = volume_item_length_dip * 5;
                }
                holder.setOnClickListener(R.id.rl_msg_click, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //播放前重置。
//                        MediaPlayerManager.release();
                        //开始实质播放
                        String voicePath;
                        if (!TextUtils.isEmpty(bean.getLocalVoicePath()) && new File(bean.getLocalVoicePath()).exists()) { //判断本地是否有语音文件
                            voicePath = bean.getLocalVoicePath();
                        } else {
                            voicePath = bean.getMessageContent();
                        }
//                        MediaPlayerManager.getInstance(mContext).playSound(voicePath, new MediaPlayer.OnCompletionListener() {
//                                    @Override
//                                    public void onCompletion(MediaPlayer mp) {
//                                        //播放完成
//                                        stopPlayVoice();
//                                    }
//                                }, true);
                        MediaPlayerHelper.playSoundThread(voicePath, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //播放完成
                                        stopPlayVoice();
                                    }
                                });
                            }
                        });
                        stopPlayVoice(); //先停掉原来的播放动画
                        selectImageView = holder.getView(R.id.iv_voice);
                        playVoice();

                    }
                });
            }
        };
        mListview.setAdapter(mCommonLvAdapter);
        if (type == 0) { //新建1个作业
            mQueryHomeWorkBean.setLeaveMessages(new ArrayList<QueryHomeWorkBean.LeaveMessagesBean>()); //新建消息list
            mQueryHomeWorkBean.setUsername(mApplication.getPhone());
            mQueryHomeWorkBean.setDeviceId(SystemUtils.getDeviceKey());
            mQueryHomeWorkBean.setRobotDeviceId(mApplication.getUserInfo().getDeviceid());
            mQueryHomeWorkBean.setSubjectId(subjectId);
            mQueryHomeWorkBean.setIsDone(false);
            if (0 != homeworkId) {
                mQueryHomeWorkBean.setHomeworkId(homeworkId);
            }
        }
    }

    AnimationDrawable voiceAnimation;
    ImageView selectImageView;

    private void playVoice() {
        selectImageView.setImageResource(R.drawable.anim_voice_orange);
        voiceAnimation = (AnimationDrawable) selectImageView.getDrawable();
        voiceAnimation.start();
    }

    private void stopPlayVoice() {
        if (selectImageView == null)
            return;
        if (voiceAnimation != null && voiceAnimation.isRunning()) {
            voiceAnimation.stop();
        }
        selectImageView.setImageResource(R.drawable.voice_orange_3);
    }


    @OnClick(R.id.ll_complete_time_click)
    public void onViewClicked() {
        //日历
        startActivityForResult(new Intent(mContext, HomeWorkDateActivity.class).putExtra("bPreDate", false), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String startTime = data.getStringExtra("startTime"); //开始时间
            String endTime = data.getStringExtra("endTime");   //截止时间
            mTvCompletionTime.setText(startTime.split(" ")[0] + "  -  " + endTime.split(" ")[0]);
            Log.d(TAG, "onActivityResult: startTime: " + startTime + "  endTime: " + endTime);
            mQueryHomeWorkBean.setStartTime(DateFormatUtils.DateFormat2Stamp(startTime, "yyyy-M-d HH:mm:ss") / 1000); //秒级时间
            mQueryHomeWorkBean.setEndTime(DateFormatUtils.DateFormat2Stamp(endTime, "yyyy-M-d HH:mm") / 1000);
        }
    }
}
