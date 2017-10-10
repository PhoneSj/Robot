package com.zige.robot.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.zige.robot.base.BaseActivity;
import com.zige.robot.view.MyVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.zige.robot.R;

import static com.zige.robot.R.id.video_view;

/**
 * Created by Administrator on 2017/5/5.
 */

public class SplashActivity extends BaseActivity {
    @BindView(video_view)
    MyVideoView mVideoView;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_splash;
    }
    //开机帧动画
    int[] imageList = {R.drawable.kjdh1, R.drawable.kjdh2, R.drawable.kjdh3, R.drawable.kjdh4, R.drawable.kjdh5, R.drawable.kjdh6, R.drawable.kjdh7, R.drawable.kjdh8, R.drawable.kjdh9, R.drawable.kjdh10,
            R.drawable.kjdh11, R.drawable.kjdh12, R.drawable.kjdh13, R.drawable.kjdh14, R.drawable.kjdh15, R.drawable.kjdh16, R.drawable.kjdh17, R.drawable.kjdh18, R.drawable.kjdh19, R.drawable.kjdh20,
            R.drawable.kjdh21, R.drawable.kjdh22, R.drawable.kjdh23, R.drawable.kjdh24, R.drawable.kjdh25, R.drawable.kjdh26, R.drawable.kjdh27, R.drawable.kjdh28, R.drawable.kjdh29, R.drawable.kjdh30,
            R.drawable.kjdh31, R.drawable.kjdh32, R.drawable.kjdh33, R.drawable.kjdh34, R.drawable.kjdh35, R.drawable.kjdh36, R.drawable.kjdh37, R.drawable.kjdh38, R.drawable.kjdh39, R.drawable.kjdh40,
            R.drawable.kjdh41, R.drawable.kjdh42, R.drawable.kjdh43, R.drawable.kjdh44, R.drawable.kjdh45, R.drawable.kjdh46, R.drawable.kjdh47, R.drawable.kjdh48, R.drawable.kjdh49, R.drawable.kjdh50,
            R.drawable.kjdh51, R.drawable.kjdh52, R.drawable.kjdh53, R.drawable.kjdh54, R.drawable.kjdh55, R.drawable.kjdh56, R.drawable.kjdh57, R.drawable.kjdh58, R.drawable.kjdh59, R.drawable.kjdh60,
            R.drawable.kjdh61, R.drawable.kjdh62, R.drawable.kjdh63, R.drawable.kjdh64, R.drawable.kjdh65, R.drawable.kjdh66, R.drawable.kjdh67, R.drawable.kjdh68, R.drawable.kjdh69, R.drawable.kjdh70,
            R.drawable.kjdh71, R.drawable.kjdh72, R.drawable.kjdh73, R.drawable.kjdh74, R.drawable.kjdh75, R.drawable.kjdh76, R.drawable.kjdh77, R.drawable.kjdh78, R.drawable.kjdh79, R.drawable.kjdh80,
            R.drawable.kjdh81, R.drawable.kjdh82, R.drawable.kjdh83, R.drawable.kjdh84, R.drawable.kjdh85, R.drawable.kjdh86, R.drawable.kjdh87, R.drawable.kjdh88, R.drawable.kjdh89, R.drawable.kjdh90,
            R.drawable.kjdh91, R.drawable.kjdh92, R.drawable.kjdh93, R.drawable.kjdh94, R.drawable.kjdh95, R.drawable.kjdh96, R.drawable.kjdh97, R.drawable.kjdh98, R.drawable.kjdh99, R.drawable.kjdh100,
            R.drawable.kjdh101, R.drawable.kjdh102, R.drawable.kjdh103, R.drawable.kjdh104, R.drawable.kjdh105, R.drawable.kjdh106, R.drawable.kjdh107, R.drawable.kjdh108, R.drawable.kjdh109, R.drawable.kjdh110,
            R.drawable.kjdh111, R.drawable.kjdh112, R.drawable.kjdh113, R.drawable.kjdh114, R.drawable.kjdh115, R.drawable.kjdh116, R.drawable.kjdh117, R.drawable.kjdh118, R.drawable.kjdh119, R.drawable.kjdh120,
            R.drawable.kjdh121, R.drawable.kjdh122, R.drawable.kjdh123, R.drawable.kjdh124, R.drawable.kjdh125, R.drawable.kjdh126, R.drawable.kjdh127, R.drawable.kjdh128, R.drawable.kjdh129, R.drawable.kjdh130,
            R.drawable.kjdh131, R.drawable.kjdh132, R.drawable.kjdh133};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playBootVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.stopPlayback();
    }

    private void playBootVideo(){
        //播放完成回调
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startActivity(LoginActivity.class);
                finish();
            }
        });
        //设置视频路径
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.boot));
        mVideoView.start();
    }


}
