package com.zige.robot.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tencent.TIMMessage;
import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.interf.IMMsgListener;
import com.zige.robot.service.ServiceLogin;
import com.zige.robot.utils.HXManager;
import com.zige.robot.utils.QuickClick;
import com.zige.robot.xmlresult.JsonParser;

import java.util.List;

/**
 * 遥控
 */
public class ControlActivity extends BaseActivity implements View.OnClickListener,  View.OnTouchListener {


    ImageView iv_control_up;
    ImageView iv_control_down;
    ImageView iv_control_left;
    ImageView iv_control_right;
    ImageView iv_voice;

    // 语音听写UI
    private SpeechRecognizer mIat;
    private RecognizerDialog mIatDialog;
    private static final String TAG = "ControlActivity";

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_control;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.rl_back_return).setOnClickListener(this);
        findViewById(R.id.iv_voice).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title_name)).setText("遥控");
        iv_control_up = (ImageView) findViewById(R.id.iv_control_up);
        iv_control_down = (ImageView) findViewById(R.id.iv_control_down);
        iv_control_left = (ImageView) findViewById(R.id.iv_control_left);
        iv_control_right = (ImageView) findViewById(R.id.iv_control_right);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        iv_control_up.setOnTouchListener(this);
        iv_control_down.setOnTouchListener(this);
        iv_control_left.setOnTouchListener(this);
        iv_control_right.setOnTouchListener(this);
        iv_voice.setOnTouchListener(recognizeListener);

        mIat = SpeechRecognizer.createRecognizer(ControlActivity.this, mInitListener);
        mIatDialog = new RecognizerDialog(ControlActivity.this, mInitListener);

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
//        initSenor();
        /**
         * 6.0检测录音权限
         */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP_MR1){
                String[] mPermissionList = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                };
                ActivityCompat.requestPermissions(this,mPermissionList,1);
            }
        }

        findViewById(R.id.tv_action).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_action)).setText("重置");
        findViewById(R.id.tv_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ServiceLogin.getInstance().sendOnLineTextMsg(mApplication.getUserInfo().getDeviceid(), "reset", new IMMsgListener() {
//                    @Override
//                    public void onSuccess(TIMMessage msg) {
//                    }
//
//                    @Override
//                    public void onError(int code, String desc) {
//
//                    }
//                });
                HXManager.sendTxt(mApplication.getUserInfo().getDeviceid(), 1, "reset", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.d(TAG, "onMessageReceived: ");
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.d(TAG, "onCmdMessageReceived: ");
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.d(TAG, "onMessageRead: ");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            Log.d(TAG, "onMessageDelivered: ");
        }
        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
            Log.d(TAG, "onMessageRecalled: ");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.d(TAG, "onMessageChanged: ");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        sendOrder(STOP_STATE);
    }

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onResume() {
        super.onResume();
//        initSenor();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mSensorManager.unregisterListener(listener);

    }



    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor == null) {
                return;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                int x = (int) event.values[0];
                int y = (int) event.values[1];
                int z = (int) event.values[2];

                if (x > 4) {
                    if(MOVE_STATE ==3){
                        return;
                    }
                    toastShow("左");
                    sendOrder(3);
                    MOVE_STATE = 3;
                } else if (x < -4) {
                    if(MOVE_STATE ==4){
                        return;
                    }
                    toastShow("右");
                    sendOrder(4);
                    MOVE_STATE = 4;
                } else if (y < -3) {
                    if(MOVE_STATE ==1){
                        return;
                    }
                    toastShow("前");
                    sendOrder(1);
                    MOVE_STATE = 1;
                } else if (y > 4) {
                    if(MOVE_STATE ==2){
                        return;
                    }
                    toastShow("后");
                    sendOrder(2);
                    MOVE_STATE = 2;
                } else {
                    if(MOVE_STATE ==5){
                        return;
                    }
                    toastShow("停");
                    sendOrder(5);
                    MOVE_STATE = 5;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void initSenor(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
            toastShow("手机不支持重力感应");
            return;
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(listener, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
            }
        }
    };

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = "mandarin";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "2000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "700");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    private void printResult(RecognizerResult results) {
        final String text = JsonParser.parseIatResult(results.getResultString());
        if (text != null && text.length() > 1) {
            //指令命令
            
            HXManager.sendTxt(App.getInstance().getUserInfo().getDeviceid(), 2, text, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: ");
                    toastShow(text);
                }

                @Override
                public void onError(int i, String s) {
                    Log.d(TAG, "onError: ");
                }

                @Override
                public void onProgress(int i, String s) {
                    Log.d(TAG, "onProgress: ");
                }
            });
            
        }
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
//            if (recognizing && mIatDialog.isShowing())
//                mIatDialog.dismiss();
//                recognizing = false;
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
        }

    };

    private long begin;
    boolean recognizing = false;

    View.OnTouchListener recognizeListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                iv_voice.setBackgroundResource(R.drawable.voice_off);
                begin = System.currentTimeMillis();
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                if (!recognizing && (System.currentTimeMillis() - begin > 200)) {
                    recognizing = true;
                    setParam();
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                }
            } else if(event.getAction() == MotionEvent.ACTION_UP){
                iv_voice.setBackgroundResource(R.drawable.voice_on);
                if (recognizing && mIatDialog.isShowing())
                    mIatDialog.dismiss();
                    recognizing = false;
            }
            return true;
        }
    };
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_back_return) {
            finish();
        }
    }

    public static int MOVE_STATE = 0;
    public static final int MOVE_STATE_UP = 1;
    public static final int MOVE_STATE_DOWN = 2;
    public static final int MOVE_STATE_LEFT = 3;
    public static final int MOVE_STATE_RIGHT = 4;
    public static final int STOP_STATE = 5;
    private long start = 0;
    private long stop = 0; // 计算点击的时间

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (QuickClick.oneClick()) {
                    // 点击过快不响应
                    return false;
                }
                start = System.currentTimeMillis();
                switch (v.getId()) {
                    case R.id.iv_control_up:
                        MOVE_STATE = MOVE_STATE_UP;
                        sendOrder(MOVE_STATE);
                        Log.i("main", "=============长前进");
                        break;
                    case R.id.iv_control_down:
                        MOVE_STATE = MOVE_STATE_DOWN;
                        sendOrder(MOVE_STATE);
                        Log.i("main", "=============长后退");
                        break;
                    case R.id.iv_control_left:
                        MOVE_STATE = MOVE_STATE_LEFT;
                        sendOrder(MOVE_STATE);
                        Log.i("main", "=============长向左");
                        break;
                    case R.id.iv_control_right:
                        MOVE_STATE = MOVE_STATE_RIGHT;
                        sendOrder(MOVE_STATE);
                        Log.i("main", "=============长向右");
                        break;
                    default:
                        break;
                }
                changeButton(MOVE_STATE);
                break;

            case MotionEvent.ACTION_UP:
                iv_control_up.setBackgroundResource(R.drawable.up_off);
                iv_control_down.setBackgroundResource(R.drawable.under_off);
                iv_control_left.setBackgroundResource(R.drawable.left_off);
                iv_control_right.setBackgroundResource(R.drawable.right_off);
                MOVE_STATE = 0;
                stop = System.currentTimeMillis();
                if (stop - start < 300) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                sendOrder(STOP_STATE);
                                Log.i("call", "短按停止=====");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Log.i("call", "长按停止=====");
                    sendOrder(STOP_STATE);
                }
                break;
        }
        return true;
    }

    private void changeButton(int state){
        if(state == 1){
            iv_control_up.setBackgroundResource(R.drawable.up_on);
        }else if(state ==2){
            iv_control_down.setBackgroundResource(R.drawable.under_on);
        }else  if(state ==3){
            iv_control_left.setBackgroundResource(R.drawable.left_on);
        }else if(state ==4){
            iv_control_right.setBackgroundResource(R.drawable.right_on);
        }
    }

    String msgContent;

    private void sendOrder(int order) {

        switch (order) {
            case 1:
                msgContent = "front";
                break;
            case 2:
                msgContent = "back";
                break;
            case 3:
                msgContent = "turn_left";
                break;
            case 4:
                msgContent = "turn_right";
                break;
            case 5:
                msgContent = "move_stop";
                break;
        }
        

        String receive_robot = App.getInstance().getUserInfo().getDeviceid();
        HXManager.sendTxt(receive_robot, 1, msgContent, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d(TAG, "onProgress: ");
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}







