package com.zige.robot.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.service.HXServiceLogin;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by ldw on 2017/5/6 0006.
 * 我的信息填写
 */
public class MyQrCodeActivity extends BaseActivity {
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_my_qr_code;
    }

    ImageView iv_qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("我的二维码");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
//        if(ServiceLogin.isLogin){
        if(HXServiceLogin.isLogin){
            if("1".equals(App.getInstance().getUserInfo().getAdmin())){
                createQrCode();
            }else {
                toastShow("您不是主人哦");
            }
        }else {
            toastShow("账号未登录");
        }

    }

    String qrText;
    private void createQrCode(){
        String deviceid = mApplication.getUserInfo().getDeviceid();
        if(TextUtils.isEmpty(deviceid)){
            toastShow("未发现设备号，暂不提供二维码");
        }else {
            //  //zige_user:131********/fdsafweffdaswqwef
            qrText = "zige_user:" + mApplication.getPhone() + "/" + deviceid;
            new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(MyQrCodeActivity.this.getResources(), R.drawable.robot_on);
                return QRCodeEncoder.syncEncodeQRCode(qrText, BGAQRCodeUtil.dp2px(MyQrCodeActivity.this, 250), Color.BLACK, Color.WHITE, logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_qr_code.setImageBitmap(bitmap);
                } else {
                   toastShow("生成二维码失败");
                }
            }}.execute();
        }
    }

}