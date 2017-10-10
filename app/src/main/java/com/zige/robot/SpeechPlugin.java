package com.zige.robot;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.zige.robot.interf.IRecognizeListener;
import com.zige.robot.interf.ISpeakListener;
import com.zige.robot.interf.ITextUnderStandListener;
import com.zige.robot.utils.StringUtils;
import com.zige.robot.xmlresult.JsonParser;


/**
 * Created by admin on 2016/7/13.
 */
public class SpeechPlugin {

    private static final String TAG = "SpeechPlugin";

    private static SpeechPlugin mInstance;

    private Context mContext;

     //语义听写
    private SpeechRecognizer mIat;

    // 文本理解
    private TextUnderstander mTextUnderstander;

    // 语音合成对象
    private SpeechSynthesizer mSpeechSynthesizer;

    // 默认发音人
    private static final String voicer = "jiajia";

    private ISpeakListener mSpeakListener;

    private int emptyTimes;

    public SpeechPlugin(Context context) {
        mContext = context;

        mIat = SpeechRecognizer.createRecognizer(context,
                mInitListener);

        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(context,
                mSynthersizerInit);

        mTextUnderstander = TextUnderstander.createTextUnderstander(
                context, mTextUdrInitListener);

        uploadWords();

    }

    public static SpeechPlugin CreateInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SpeechPlugin(context);
        }
        return mInstance;
    }

    public static SpeechPlugin getInstance() {
        if (mInstance == null) {
            synchronized (SpeechPlugin.class) {
                if (mInstance == null) {
                    mInstance = new SpeechPlugin(App.getInstance());
                }
            }
        }
        return mInstance;
    }

    //上传热词
    private void uploadWords() {
    }

    /**
     * 开启识别
     */
    public void startRecognize() {
        stopRecognize();
        if (mIat == null) {
            Log.e(TAG, "startRecognize:  not init yet" );
            return;
        }
        setParam();
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != 0) {
            Log.e(TAG, "start recognize fail" + ret);
        }

    }

    /**
     * 停止识别
     */
    public void stopRecognize() {
        if (mIat != null && mIat.isListening()) {
            mIat.stopListening();
            mIat.cancel();
        }
    }

    /**
     * 是否正在识别
     */
    public boolean isRecognizing() {
        return mIat != null && mIat.isListening();
    }

    /**
     * 是否在播放TTS
     */
    public boolean isSpeaking() {
        return mSpeechSynthesizer != null && mSpeechSynthesizer.isSpeaking();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
                Log.e(TAG, "InitListener fail");
                return;
            }
            setParam();
//            startRecognize();
        }
    };

    //ifly
    public void startSpeak(String text, ISpeakListener listener) {

        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "synthesizer not init");
            return;
        }
        this.mSpeakListener = listener;
        mSpeechSynthesizer.startSpeaking(text, mSynthesizerListener);
    }

    public void startSpeak(String text) {
        startSpeak(text, null);
    }

    /**
     * 停止tts
     */
    public void stopSpeak() {
        if (mSpeechSynthesizer != null && mSpeechSynthesizer.isSpeaking()) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    private void setParam() {
        if(mIat==null){
            mIat = SpeechRecognizer.createRecognizer(mContext,
                    mInitListener);
        }
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        //        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = "mandarin";
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //        mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, lag);

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");

        mIat.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT,
                "60000"); // 语音超时 -1为无限制
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        mIat.setParameter(SpeechConstant.SAMPLE_RATE, "16000");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        // mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/listen_"+ System.currentTimeMillis()+".wav");

    }


    /**
     * 设置语音合成参数
     */
    private void setSpeakParams() {

            mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE,
                    SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voicer);
            // 设置合成语速
            mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
            // 设置合成音调
            mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
            // 设置合成音量
            mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
            // 设置播放器音频流类型
            mSpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");
            // 设置播放合成音频打断音乐播放，默认为true
            mSpeechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS,
                    "false");
            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            // mSpeechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT,
            // "wav");
//             mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");

    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.e(TAG, "====================RecognizerListener  onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            int code = error.getErrorCode();
            Log.e(TAG, "=============RecognizerListener:recognize  error 　code:　" + code);
            if (code == 10118) {
                emptyTimes++;
//                Logr.w(TAG,"语音识别错误10118，累计次数： "+emptyTimes);
                if (emptyTimes > 2) {
                    emptyTimes = 0;
                }
                startRecognize();
            } else if (code == 20006) {
//                startSpeak("打开录音失败");
                startRecognize();
            } else if (code == 20001) {
//                startSpeak("网络未连接");
                Toast.makeText(mContext, "网络未连接", Toast.LENGTH_SHORT).show();
            } else {
                startRecognize();
            }
        }

        @Override
        public void onEndOfSpeech() {
            Log.e(TAG, "==============RecognizerListener  onEndOfSpeech");
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            emptyTimes = 0;
            if (isLast) {
                startRecognize();
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            Log.e(TAG, "==============onEvent  eventType: "+ eventType);
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
             if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                 String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                 Log.e(TAG, "===========session id =" + sid);
             }
        }
    };

    /**
     * 讯飞在线识别 解释返回结果
     */
    private void printResult(RecognizerResult results) {
        String str = JsonParser.parseIatResult(results.getResultString());
        String text = StringUtils.removeSentenceMark(str);
         Log.d(TAG, "==================RecognizerListener printResult "+ text);
        if (text.length() >= 1) {
             if(mIRecognizeListener!=null){
                 mIRecognizeListener.onRecognizeOver(text);
             }
        }
    }

    IRecognizeListener mIRecognizeListener;
    /**
     * 设置识别监听器
     * @param IRecognizeListener
     */
    public void setIRecognizeListener(IRecognizeListener IRecognizeListener) {
        mIRecognizeListener = IRecognizeListener;
    }

    /**
     * 初始化语音合成
     */
    private InitListener mSynthersizerInit = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                 setSpeakParams();
            } else {
                Log.e(TAG, "speech Synthersizer init fail ,the error code is :" + code);
            }
        }
    };


    /**
     * 语音合成回调监听
     */
    private SynthesizerListener mSynthesizerListener = new SynthesizerListener() {

        @Override
        public void onSpeakResumed() {
            // 继续播放
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

        @Override
        public void onCompleted(SpeechError errorCode) {
            // 播放完成
            if (mSpeakListener != null) {
                mSpeakListener.onSpeakOver(errorCode);
            }

        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }
    };
    /**
     * 初始化监听器（文本到语义）。
     */
    private InitListener mTextUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Log.e(TAG, "mTextUdrInitListener fail");
            }
        }
    };

    /**
     * 讯飞理解
     * @param text
     */
    public void onTextUnderstand(String text) {
        if (mTextUnderstander.isUnderstanding()) {
            mTextUnderstander.cancel();
        }
        int ret = mTextUnderstander.understandText(text, mTextUnderstanderListener);
        if (ret != 0) {
            Log.e(TAG, "onTextUnderstand fail");
        }

    }

    ITextUnderStandListener mITextUnderStandListener;
    /**
     * 设置语义理解监听
     * @param ITextUnderStandListener
     */
    public void setITextUnderStandListener(ITextUnderStandListener ITextUnderStandListener) {
        mITextUnderStandListener = ITextUnderStandListener;
    }

    /**
     * 讯飞理解回调监听
     */
    private TextUnderstanderListener mTextUnderstanderListener = new TextUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                String text = result.getResultString();
                Log.d(TAG, "===================onResult: " + text);
                if(mITextUnderStandListener !=null){
                    mITextUnderStandListener.onUnderStandOver(text);
                }
            }
        }

        @Override
        public void onError(SpeechError error) {
            // 文本语义不能使用回调错误码14002，请确认您下载sdk时是否勾选语义场景和私有语义的发布
            Log.e(TAG, "onError: " + error.getErrorCode() + "  " +error.getErrorDescription());
        }
    };

    public void onDestroy() {

        if (mIat != null) {
            if (mIat.isListening())
                mIat.stopListening();
            mIat.destroy();
            mIat = null;
        }

        if (mSpeechSynthesizer != null) {
            if (mSpeechSynthesizer.isSpeaking())
                mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer.destroy();
            mSpeechSynthesizer = null;
        }

        if (mTextUnderstander != null) {
            if (mTextUnderstander.isUnderstanding())
                mTextUnderstander.cancel();
            mTextUnderstander.destroy();
            mTextUnderstander = null;
        }
        mInstance = null;

    }


}