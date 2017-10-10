package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.colorrecolibrary.DubePersonList;
import com.zige.colorrecolibrary.RetrofitApi;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.adapter.FaceInfoAdapter;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.SystemUtils;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FaceListActivity extends BaseActivity {

    @BindView(R.id.rl_back_return)
    RelativeLayout rlBackReturn;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.list)
    ListView list;

    FaceInfoAdapter adapter;
    ArrayList<DubePersonList.UserInfoListBean> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setTitleName("人脸信息");
        setTvActionText("添加");
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FaceRecogSetOneActivity.class);
            }
        });

        adapter = new FaceInfoAdapter(this, persons);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DubePersonList.UserInfoListBean userInfoListBean = persons.get(i);
                startActivity(new Intent(FaceListActivity.this, FaceDetailActivity.class).putExtra("userInfo", userInfoListBean));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();

    }

    private void refreshData() {
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
                persons.clear();
                if ("0000".equals(dubePersonList.getCode())) {
                    if (dubePersonList.getUserInfoList() != null && dubePersonList.getUserInfoList().size() > 0) {
                        for (DubePersonList.UserInfoListBean faceUserListBean : dubePersonList.getUserInfoList()) {
                            persons.add(faceUserListBean);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                dismissProgressDialog();
                if (persons.size() == 0){
                    toastShow("还没有添加人脸,点击右上角添加一个试试吧");
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
        return R.layout.activity_face_list;
    }
}
