package com.zige.robot.fsj.ui.pay.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.PayRecordBean;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.ui.pay.adapter.PayRecordAdapter;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.fsj.widget.EmptyRecyclerView;
import com.zige.robot.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/9/8.
 */

public class PayRecordActivity extends StateActivity {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.view_main)
    EmptyRecyclerView viewMain;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    private int totalPage = 0;
    private int currentPage = 0;
    private static final int NUM_OF_PAGE = 20;

    private List<PayRecordBean.RowsBean> datas = new ArrayList<>();
    private PayRecordAdapter adapter;

    private boolean isLoadingMore = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_record;
    }

    @Override
    protected void initEventAndData() {
        //// TODO: 2017/9/15 robotId后续改为从presenter中获取
//        String robotId = mPresenter.getRobotId();
        adapter = new PayRecordAdapter(mContext, datas);
        viewMain.setLayoutManager(new LinearLayoutManager(mContext));
        viewMain.setEmptyView(layoutEmpty);
        viewMain.setAdapter(adapter);
        //下拉加载更多
        viewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) viewMain.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = viewMain.getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {  //还剩2个Item时加载更多
                    if (!isLoadingMore) {
                        isLoadingMore = true;
                        getMorePayRecordData();

                    }
                }
            }
        });

        setToolbar(toolBar, "充值记录");
        getPayRecordData();
        stateLoading("正在从网络中获取...");

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPayRecordData();
            }
        });
    }

    private void getMorePayRecordData() {
        if (currentPage == totalPage - 1) {
            //已经是最后一页
            stateContent("已经显示全部");
            return;
        }
        String robotId = SharedPreferencesUtils.getRobotIdFromSP();
        HttpConfig.getInstance()
                  .getApi()
                  .getPayRecordList(robotId, ++currentPage, NUM_OF_PAGE)
                  .compose(RxUtil.<CallHttpResponse<PayRecordBean>>rxSchedulerHelper())
                  .compose(RxUtil.<PayRecordBean>handleCallResult())
                  .map(new Function<PayRecordBean, List<PayRecordBean.RowsBean>>() {
                      @Override
                      public List<PayRecordBean.RowsBean> apply(@NonNull PayRecordBean payRecordBean) throws Exception {
                          totalPage = payRecordBean.getTotal();
                          return payRecordBean.getRows();
                      }
                  }).subscribe(new Consumer<List<PayRecordBean.RowsBean>>() {
            @Override
            public void accept(List<PayRecordBean.RowsBean> rowsBeen) throws Exception {
                datas.addAll(rowsBeen);
                adapter.notifyDataSetChanged();
                isLoadingMore = false;
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
//                ToastUtil.shortShow("获取更多充值记录失败");
//                LogUtil.showE("获取更多充值记录失败");
                stateError("获取更多充值记录失败");
            }
        });
    }

    private void getPayRecordData() {
        String robotId = SharedPreferencesUtils.getRobotIdFromSP();
        currentPage = 0;
        HttpConfig.getInstance()
                  .getApi()
                  .getPayRecordList(robotId, currentPage, NUM_OF_PAGE)
                  .compose(RxUtil.<CallHttpResponse<PayRecordBean>>rxSchedulerHelper())
                  .compose(RxUtil.<PayRecordBean>handleCallResult())
                  .map(new Function<PayRecordBean, List<PayRecordBean.RowsBean>>() {
                      @Override
                      public List<PayRecordBean.RowsBean> apply(@NonNull PayRecordBean payRecordBean) {
                          totalPage = payRecordBean.getTotal();
                          return payRecordBean.getRows();
                      }
                  }).subscribe(new Consumer<List<PayRecordBean.RowsBean>>() {
            @Override
            public void accept(List<PayRecordBean.RowsBean> rowsBeen) throws Exception {
                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
                stateContent();
                datas.clear();
                datas.addAll(rowsBeen);
                adapter.notifyDataSetChanged();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
//                ToastUtil.shortShow("获取充值记录失败");
//                LogUtil.showE("获取充值记录失败");s
                stateError("获取充值记录失败");
            }
        });
    }

    @Override
    protected void showContentMsg(String msg) {
        super.showContentMsg(msg);
        SnackbarUtil.showShort(layoutRoot, msg);
    }

    @Override
    protected void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        SnackbarUtil.showShort(layoutRoot, msg);
    }
}
