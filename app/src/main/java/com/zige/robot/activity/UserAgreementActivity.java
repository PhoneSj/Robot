package com.zige.robot.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;

public class UserAgreementActivity extends BaseActivity {
    private TextView userAgreementContent;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAgreementContent = (TextView) findViewById(R.id.tv_userAgreement_Content);
        setTitleName("用户协议");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        InputStream inputStream = getResources().openRawResource(R.raw.littlezebraagreement);
//        String agreementContent = TxtReaderUtils.getString(inputStream);
//        userAgreementContent.setText(agreementContent);
        userAgreementContent.setText("暂无");
    }


}
