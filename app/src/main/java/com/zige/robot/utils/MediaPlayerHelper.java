package com.zige.robot.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by ldw on 2017/8/25.
 * 媒体播放工具类
 */
public class MediaPlayerHelper {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = false;

    public static void playSound(String filePath) {
        playSound(filePath, null);
    }

    public static void playSoundThread(final String filePath, final MediaPlayer.OnCompletionListener onCompletionListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                playSound(filePath, onCompletionListener);
            }
        }).start();
    }

    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (onCompletionListener != null) {
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
            }
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    public static void realese() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            isPause = true;
        }
    }

}
