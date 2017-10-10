package com.zige.robot.activity;


import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyphenate.EMCallBack;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.greendao.entity.Order;
import com.zige.robot.greendao.util.OrderDaoUtil;
import com.zige.robot.interf.DialogListener;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.HXManager;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.xmlresult.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 播音
 */
public class SpeakVoiceActivity extends BaseActivity implements View.OnClickListener{

    Dialog dialog;
    Dialog delDialog;
    ImageView iv_order_sound;
    ListView listview;
    CommonLvAdapter<Order> commonLvAdapter;
    Order select_order =null;
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("播音");
        setIvActionbg(R.drawable.english_add_on);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setIvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加指令
                et_import_content.setText("");
                dialog.show();
            }
        });
        iv_order_sound = (ImageView) findViewById(R.id.iv_order_sound);
        listview = (ListView) findViewById(R.id.listview);
        iv_order_sound.setOnTouchListener(recognizeListener);
        initDialog();
        initDelDialog();

        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        commonLvAdapter = new CommonLvAdapter<Order>(mContext, new ArrayList<Order>(), R.layout.adapter_order) {
            @Override
            public void convert(CommonLvViewHolder holder, final Order bean, final int position) {
                holder.setText(R.id.tv_content, bean.getOrderContent());
                holder.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendOrderMsg(bean.getOrderContent());
                    }
                });
                holder.getView(R.id.tv_content).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        select_order = commonLvAdapter.getItem(position);
                        delDialog.show();
                        return false;
                    }
                });
            }
        };
        listview.setAdapter(commonLvAdapter);
        getDataFromDb();

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
    private long begin;
    boolean recognizing = false;
    // 语音听写UI
    private SpeechRecognizer mIat;
    private RecognizerDialog mIatDialog;
    View.OnTouchListener recognizeListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                iv_order_sound.setBackgroundResource(R.drawable.voice_off);
                begin = System.currentTimeMillis();
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                if (!recognizing && (System.currentTimeMillis() - begin > 200)) {
                    recognizing = true;
                    setParam();
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                }
            } else if(event.getAction() == MotionEvent.ACTION_UP){
                iv_order_sound.setBackgroundResource(R.drawable.voice_on);
                if (recognizing && mIatDialog.isShowing())
                    mIatDialog.dismiss();
                recognizing = false;
            }
            return true;
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

    /**
     * 解析语音json
     * @param results
     */
    private void printResult(RecognizerResult results) {
        final String text = JsonParser.parseIatResult(results.getResultString());
        if (text != null && text.length() > 1) {
            sendOrderMsg(text);
            Log.i(TAG, "------------识别结果：  " + text);
        }
    }

    /**
     * 发送语音播报指令
     * @param text
     */
    private void sendOrderMsg(final String text){
//        //语音播报
//        ServiceLogin.getInstance().sendOnLineTextMsg(mApplication.getUserInfo().getDeviceid(), "speak_"/*"operate_"*/ + text, new IMMsgListener() {
//            @Override
//            public void onSuccess(TIMMessage msg) {
//                toastShow(text);
//            }
//
//            @Override
//            public void onError(int code, String desc) {
//            }
//        });

        HXManager.sendTxt(mApplication.getUserInfo().getDeviceid(), 3, text, new EMCallBack() {
            @Override
            public void onSuccess() {
                toastShow(text);
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 获取数据库的数据
     */
    private void getDataFromDb(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               final List<Order> list = OrderDaoUtil.getInstance().queryAllOrder();
                if(list!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commonLvAdapter.setDatas(list);
                        }
                    });
                }
            }
        }).start();
    }



    EditText et_import_content;
    /**
     * 添加指令dialog
     */
    private void initDialog(){
        dialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_add_order, ScreenUtils.dp2px(mContext, 20), ScreenUtils.dp2px(mContext, 20));
        et_import_content = (EditText) dialog.findViewById(R.id.et_import_content);
        dialog.findViewById(R.id.btn_back).setOnClickListener(this);
        dialog.findViewById(R.id.btn_add).setOnClickListener(this);
    }

    /**
     * 删除指令dialog
     */
    private void initDelDialog(){
        delDialog = DialogUtils.createDelDialog(mContext, new DialogListener() {
            @Override
            public void actionClick() {
                //删除指令
                OrderDaoUtil.getInstance().deleteOrder(select_order.getId());
                delDialog.dismiss();
                getDataFromDb();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                dialog.dismiss();
                break;
            case R.id.btn_add: //添加指令
                String orderContent = et_import_content.getText().toString().trim();
                if(TextUtils.isEmpty(orderContent)){
                    toastShow("请输入文字");
                    return;
                }
                dialog.dismiss();
                OrderDaoUtil.getInstance().insertOrder(System.currentTimeMillis(), orderContent);
                getDataFromDb();
                break;

        }
    }
}
