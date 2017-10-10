package com.zige.robot.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import java.io.IOException;


/**
 * 语音播放类
 */
public class MediaPlayerManager {

    private static final String TAG = "MediaPlayerManager";
    private static MediaPlayer mPlayer;
    private static boolean isPause;
    private static MediaPlayerManager sManager;
    private static AudioManager audioManager;


    public static MediaPlayerManager getInstance(Context context){
        if(sManager == null){
            sManager = new MediaPlayerManager(context);
        }
        return sManager;
    }

    public MediaPlayerManager(Context context){
         audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }



    public void playSound( String filePathString,  OnCompletionListener onCompletionListener, boolean isSpeak) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayer.reset();
                    return false;
                }
            });
        } else {
            mPlayer.reset();//就重置
        }
        try {
            if (isSpeak)
            {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(true);
                mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            } else
            {
                audioManager.setSpeakerphoneOn(false);// 关闭扬声器
                // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            }
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setDataSource(filePathString);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //停止函数
    public static void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    //继续
    public static void resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
            isPause = false;
        }
    }


    public static void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


}
