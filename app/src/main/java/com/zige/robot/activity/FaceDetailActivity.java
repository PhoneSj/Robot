package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zige.colorrecolibrary.DefResponse;
import com.zige.colorrecolibrary.DubePersonList;
import com.zige.colorrecolibrary.RetrofitApi;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.utils.TagUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.zige.robot.utils.ToastUtils.showToast;

public class FaceDetailActivity extends BaseActivity {

    @BindView(R.id.rl_back_return)
    RelativeLayout rlBackReturn;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.ll_chengyuan_click)
    LinearLayout llChengyuanClick;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ll_sex_click)
    LinearLayout llSexClick;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.imag_face1)
    ImageView imagFace1;
    @BindView(R.id.imag_face2)
    ImageView imagFace2;
    @BindView(R.id.imag_face3)
    ImageView imagFace3;
    @BindView(R.id.imag_del_face1)
    ImageView imagDelFace1;
    @BindView(R.id.imag_del_face2)
    ImageView imagDelFace2;
    @BindView(R.id.imag_del_face3)
    ImageView imagDelFace3;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    DubePersonList.UserInfoListBean userInfoListBean;

    private Dialog sexDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoListBean = getIntent().getExtras().getParcelable("userInfo");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String age = etAge.getText().toString();
                String name = etUsername.getText().toString();
                String sex = tvSex.getText().toString();

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
                showProgressDialog("保存中",false);
                RetrofitApi.getInstance(HttpLink.BASE_URL).getApi().addFace(etUsername.getText().toString(),
                        etAge.getText().toString(), etTitle.getText().toString(), "男".equals(tvSex.getText().toString()) ? "1":"0",
                        userInfoListBean.getPersonId()+"", "","", App.getInstance().getUserInfo().getUserId() + "",
                        SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DefResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DefResponse value) {
                        dismissProgressDialog();
                        if ("0000".equals(value.getCode())) {
                            showToast("修改成功");
                            finish();
                            TagUtil.showLogDebug("提交成功" + value.getCode() + "\n" + value.getMessage());
                        } else {
                            showToast("修改失败"+ value.getMessage());
                            finish();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });

        setTitleName("详情编辑");
        setTvActionText("删除");
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePerson();
            }
        });
        findViewById(R.id.ll_sex_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.show();
            }
        });
        initDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();

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
                tvSex.setText("男");
                tv_man.setTextColor(getResources().getColor(R.color.color_fe5265));
                tv_women.setTextColor(getResources().getColor(R.color.tv_777));

            }
        });
        sexDialog.findViewById(R.id.tv_women).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
                tvSex.setText("女");
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

    private void refreshData(){

        showProgressDialog("请稍后", false);
        String robotId = App.getInstance().getUserInfo().getDeviceid();
        String sign = "robotDeviceId" + robotId;
        String uid = App.getInstance().getUserInfo().getUserId()+"";
        String deviceKey = SystemUtils.getDeviceKey();
        RetrofitApi.getInstance(HttpLink.BASE_URL).getApi()
                .getAllUserByUserId(robotId, uid, deviceKey)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DubePersonList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DubePersonList dubePersonList) {
                        if ("0000".equals(dubePersonList.getCode())) {
                            if (dubePersonList.getUserInfoList() != null && dubePersonList.getUserInfoList().size() > 0) {
                                for (DubePersonList.UserInfoListBean faceUserListBean : dubePersonList.getUserInfoList()) {
                                    if ((faceUserListBean.getPersonId()+"").equals(userInfoListBean.getPersonId()+"")){
                                        userInfoListBean = faceUserListBean;
                                        break;
                                    }
                                }
                            }
                        }
                        dismissProgressDialog();
                        etUsername.setText(userInfoListBean.getUsername());
                        etAge.setText(userInfoListBean.getAge()+"");
                        etTitle.setText(userInfoListBean.getTitle());
                        String sex = "1".equals(userInfoListBean.getSex()+"")? "男" :("0".equals(userInfoListBean.getSex()+"") ? "女": "");
                        if (TextUtils.isEmpty(sex)){
                            tvSex.setText("请选择性别");
                        }else {
                            tvSex.setText(sex);
                        }


                        if (!TextUtils.isEmpty(userInfoListBean.getUrlFace1())){
                            imagDelFace1.setVisibility(View.VISIBLE);
                            Glide.with(FaceDetailActivity.this).load(userInfoListBean.getUrlFace1()).into(imagFace1);
                        }else {
                            imagDelFace1.setVisibility(View.INVISIBLE);
                            imagFace1.setImageBitmap(null);
                            imagFace1.setBackgroundResource(R.drawable.addbox);
                        }
                        if (!TextUtils.isEmpty(userInfoListBean.getUrlFace2())){
                            imagDelFace2.setVisibility(View.VISIBLE);
                            Glide.with(FaceDetailActivity.this).load(userInfoListBean.getUrlFace2()).into(imagFace2);
                        }else {
                            imagDelFace2.setVisibility(View.INVISIBLE);
                            imagFace2.setImageBitmap(null);
                            imagFace2.setBackgroundResource(R.drawable.addbox);
                        }
                        if (!TextUtils.isEmpty(userInfoListBean.getUrlFace3())){
                            imagDelFace3.setVisibility(View.VISIBLE);
                            Glide.with(FaceDetailActivity.this).load(userInfoListBean.getUrlFace3()).into(imagFace3);
                        }else {
                            imagDelFace3.setVisibility(View.INVISIBLE);
                            imagFace3.setImageBitmap(null);
                            imagFace3.setBackgroundResource(R.drawable.addbox);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_face_detail;
    }

    @OnClick({R.id.imag_face1, R.id.imag_face2, R.id.imag_face3, R.id.imag_del_face1, R.id.imag_del_face2, R.id.imag_del_face3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imag_face1:
                if (TextUtils.isEmpty(userInfoListBean.getUrlFace1())){
                    startActivity(new Intent(this, FaceRecogSetOneActivity.class).putExtra("addPerson", false).putExtra("userInfo", userInfoListBean));
                }
                break;
            case R.id.imag_face2:
                if (TextUtils.isEmpty(userInfoListBean.getUrlFace2())){
                    startActivity(new Intent(this, FaceRecogSetOneActivity.class).putExtra("addPerson", false).putExtra("userInfo", userInfoListBean));
                }
                break;
            case R.id.imag_face3:
                if (TextUtils.isEmpty(userInfoListBean.getUrlFace3())){
                    startActivity(new Intent(this, FaceRecogSetOneActivity.class).putExtra("addPerson", false).putExtra("userInfo", userInfoListBean));
                }
                break;
            case R.id.imag_del_face1:
                if (isLastFace()){
                    Toast.makeText(this, "不能再删除了,至少需要一张您的照片哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteFace(userInfoListBean.getFaceId1());
                break;
            case R.id.imag_del_face2:
                if (isLastFace()){
                    Toast.makeText(this, "不能再删除了,至少需要一张您的照片哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteFace(userInfoListBean.getFaceId2());
                break;
            case R.id.imag_del_face3:
                if (isLastFace()){
                    Toast.makeText(this, "不能再删除了,至少需要一张您的照片哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteFace(userInfoListBean.getFaceId3());
                break;
        }
    }

    private boolean isLastFace(){
        int i = 3;
        if (TextUtils.isEmpty(userInfoListBean.getUrlFace1())){
            i--;
        }if (TextUtils.isEmpty(userInfoListBean.getUrlFace2())){
            i--;
        }if (TextUtils.isEmpty(userInfoListBean.getUrlFace3())){
            i--;
        }
        return i == 1;
    }

    private void deletePerson(){
        showProgressDialog("请稍后", false);
        RetrofitApi.getInstance(HttpLink.BASE_URL).getApi().delFace(App.getInstance().getUserInfo().getDeviceid(),
                App.getInstance().getUserInfo().getUserId()+"", userInfoListBean.getPersonId()+"","",
                SystemUtils.getDeviceKey()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DefResponse value) {
                        dismissProgressDialog();
                        if ("0000".equals(value.getCode())){
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void deleteFace(String faceId){
        RetrofitApi.getInstance(HttpLink.BASE_URL).getApi().delFace(App.getInstance().getUserInfo().getDeviceid(),
                App.getInstance().getUserInfo().getUserId()+"", userInfoListBean.getPersonId()+"",faceId,
                SystemUtils.getDeviceKey()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DefResponse value) {
                if ("0000".equals(value.getCode())){
                    refreshData();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
