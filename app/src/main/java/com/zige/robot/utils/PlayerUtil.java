package com.zige.robot.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;


public class PlayerUtil {

	Context context;
	private MediaPlayer mPlayer;
	private AssetManager mAssetManager;

	public PlayerUtil(Context context) {
		this.context = context;
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
	}


	public void playAssetsFile(String file, boolean repeat) {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		try {
			mAssetManager = context.getAssets();
			AssetFileDescriptor fd = mAssetManager.openFd(file);
			if (mPlayer != null && mPlayer.isPlaying()) {
				mPlayer.pause();
			}
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.reset();
			mPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
			fd.close();
			mPlayer.prepare();
			mPlayer.setLooping(repeat);
			mPlayer.start();
			if (!repeat) {
				mPlayer.setOnCompletionListener(myComPlistener);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			if (mPlayer == null) {
				return;
			}
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
			}
			mPlayer.release();
			mPlayer = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mPlayer = null;
		}
	}

	OnCompletionListener myComPlistener = new OnCompletionListener() {

		public void onCompletion(MediaPlayer mp) {
			listener.playOver();
			mPlayer.pause();
			mPlayer.stop();
			mPlayer.release();
		}
	};

	public void setPlayerListener(PlayerListener listener) {
		this.listener = listener;
	}

	PlayerListener listener;

	public interface PlayerListener {
		/** 播放完毕回调 */
		void playOver();
	}

}
