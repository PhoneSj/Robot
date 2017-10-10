package com.zige.robot.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zige.colorrecolibrary.DefResponse;
import com.zige.colorrecolibrary.FaceColorRec;
import com.zige.colorrecolibrary.RetrofitApi;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.utils.TagUtil;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.zige.robot.R;

import static com.zige.robot.utils.ToastUtils.showToast;

/*************************************
 功能： 人脸识别设置
 创建者：金征
 创建日期：${DATE}
 *************************************/
public class FaceRecogSetTwoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_action)
    public TextView tv_action;
    @BindView(R.id.btn_face_submit)
    public Button btn_face_submit;

    @BindView(R.id.tv_sex)
    public TextView tv_sex; //性别
    @BindView(R.id.et_title)
    public TextView et_title; //身份
    @BindView(R.id.et_age)
    public TextView et_age; //年龄
    @BindView(R.id.et_username)
    public TextView et_username; //名称

    String personId; //百度personId
    String qiniuUrl; //七牛URL
    String token;
    private Dialog sexDialog;


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_facerecog_set_two;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("提交人脸信息");

    }

    @Override
    protected void init() {
        super.init();
        personId = getIntent().getStringExtra(FaceRecogSetOneActivity.BAIDU_PERSONID);
        qiniuUrl = getIntent().getStringExtra(FaceRecogSetOneActivity.QINIU_URL);
        token = getIntent().getStringExtra("token");


        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_face_submit.setOnClickListener(this);
        findViewById(R.id.ll_sex_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.show();
            }
        });
        initDialog();

    }

    private void initDialog() {
        //性别dialog
        sexDialog = DialogUtils.createDialogForContentViewFromBottom(mContext, R.layout.dialog_picker_sex, 0, 0);
        final TextView tv_man = (TextView) sexDialog.findViewById(R.id.tv_man);
        final TextView tv_women = (TextView) sexDialog.findViewById(R.id.tv_women);
        sexDialog.findViewById(R.id.tv_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                tv_sex.setText("男");
                tv_man.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_women.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        sexDialog.findViewById(R.id.tv_women).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                tv_sex.setText("女");
                tv_women.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_man.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        sexDialog.findViewById(R.id.tv_cancel_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_face_submit: //提交

                submitRecogInfo();
                break;
            case R.id.rl_back_return: //返回
                finish();
                break;

        }

    }


    //获取性别代码   性别:1男 0女 未填写 3
    private String getSexCode() {
        String sex = tv_sex.getText().toString();
        switch (sex) {
            case "男":
                return "1";
            case "女":
                return "0";
        }
        return "3";
    }

    //提交用户信息
    private void submitRecogInfo() {
        String title = et_title.getText().toString();
        String age = et_age.getText().toString();
        String name = et_username.getText().toString();
        String sex = tv_sex.getText().toString();

        if (TextUtils.isEmpty(title)) {
            toastShow("身份信息未填写");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            toastShow("年龄未填写");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            toastShow("姓名未填写");
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            toastShow("性别未选择");
            return;
        }

//        if (TextUtils.isEmpty(personId)){
//            RetrofitApi.getInstance(HttpLink.BASE_URL).getApi().addUser(App.getInstance().getUserInfo().getDeviceid(), name,
//                    age + "", title, getSexCode() + "",
//                    personId + "", qiniuUrl,
//                    FaceColorRec.getServiceKey(token))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.Observer<DefResponse>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(DefResponse value) {
//                    if ("0000".equals(value.getCode())) {
//                        showToast("添加成功");
//                        finish();
//                        TagUtil.showLogDebug("提交成功" + value.getCode() + "\n" + value.getMessage());
//                    } else {
//                        showToast("提交失败" + value.getMessage());
//                    }
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    showToast(e.getMessage());
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//            return;
//        }

        RetrofitApi.getInstance(HttpLink.BASE_URL).getApi().addFace(name,
                age + "", title, getSexCode() + "",
                personId + "", qiniuUrl, FaceColorRec.getServiceKey(token), App.getInstance().getUserInfo().getUserId() + "",
                SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.Observer<DefResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DefResponse value) {
                if ("0000".equals(value.getCode())) {
                    showToast("添加成功");
                    finish();
                    TagUtil.showLogDebug("提交成功" + value.getCode() + "\n" + value.getMessage());
                } else {
                    showToast("提交失败" + value.getMessage());
                }

            }

            @Override
            public void onError(Throwable e) {
                showToast(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
