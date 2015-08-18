package cn.bingoogolapple.refreshlayout.demo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.demo.R;
import cn.bingoogolapple.refreshlayout.demo.adapter.NormalRecyclerViewAdapter;
import cn.bingoogolapple.refreshlayout.demo.diaoyu.RVHttpTaskObserver;
import cn.bingoogolapple.refreshlayout.demo.model.CateModel;
import cn.bingoogolapple.refreshlayout.demo.widget.Divider;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class NormalRecyclerViewFragment extends BaseFragment implements
        BGARefreshLayout.BGARefreshLayoutDelegate,
        BGAOnRVItemClickListener,
        BGAOnRVItemLongClickListener,
        BGAOnItemChildClickListener,
        BGAOnItemChildLongClickListener{
    private static final String TAG = NormalRecyclerViewFragment.class.getSimpleName();
    private NormalRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;

    Map<String, String> params = new HashMap<>();
    int nextPage = 0;

    private RvModel rvModel ;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_recyclerview);
        mRefreshLayout = getViewById(R.id.rl_recyclerview_refresh);
        mDataRv = getViewById(R.id.rv_recyclerview_data);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);

        mAdapter = new NormalRecyclerViewAdapter(mApp);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        // 使用addOnScrollListener，而不是setOnScrollListener();
//        mDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                Log.i(TAG, "测试自定义onScrollStateChanged被调用");
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                Log.i(TAG, "测试自定义onScrolled被调用");
//            }
//        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        View headerView = View.inflate(mApp, R.layout.view_custom_header2, null);

        // 测试自定义header中控件的点击事件
//        headerView.findViewById(R.id.btn_custom_header2_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.show("点击了测试按钮");
//            }
//        });

//        mRefreshLayout.setCustomHeaderView(headerView, true);

//        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(mApp, true);
//        stickinessRefreshViewHolder.setStickinessColor(Color.parseColor("#11cd6e"));
//        stickinessRefreshViewHolder.setRotateDrawable(getResources().getDrawable(R.mipmap.custom_stickiness_roate));
//        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

        mRefreshLayout.setRefreshViewHolder(new BGAMyNormalRefreshViewHolder(mApp, true));

        mDataRv.addItemDecoration(new Divider(mApp));

        mDataRv.setLayoutManager(new LinearLayoutManager(mApp, LinearLayoutManager.VERTICAL, false));

        mDataRv.setAdapter(mAdapter);

    }

    @Override
    protected void onUserVisible() {

        params.put("type", "1");
        params.put("cate_id", "5207");

        rvModel = RvModel.build(getActivity(),"skill/getlist").params(params);

        refresh();

    }

    public void refresh(){
        rvModel.params.put("page", "" + nextPage);
        rvModel.refreshData();
        RVHttpTaskObserver.getInstance().addTaskObserver(rvModel, "skill/getlist",
                RvModel.HttpTaskStatusStarted
                        | RvModel.HttpTaskStatusSucceeded
                        | RvModel.HttpTaskStatusFailed
                        | RvModel.HttpTaskStatusFinish,
                new RVHttpTaskObserver.RVHttpTaskBlock() {
                    @Override
                    public void block(RvModel task) {
                        switch (task.status) {
                            case RvModel.HttpTaskStatusStarted:

//                                ToastUtil.show("start");

                                break;
                            case RvModel.HttpTaskStatusSucceeded:

                                mRefreshLayout.endRefreshing();
                                mRefreshLayout.endLoadingMore();

//                                ToastUtil.show("success");

                                JsonObject object = task.httpTaskResult.data;
                                JsonArray array = object.getAsJsonArray("cates");
                                List<CateModel> refreshModels = new GsonBuilder().create().fromJson(array.toString(), new TypeToken<ArrayList<CateModel>>() {
                                }.getType());
                                if (nextPage == 0) {
                                    mAdapter.setDatas(refreshModels);
                                } else {
                                    mAdapter.addMoreDatas(refreshModels);
                                }
                                nextPage = object.get("nextPage").getAsInt();

                                break;
                            case RvModel.HttpTaskStatusFailed:

//                                ToastUtil.show("failed");

                                mRefreshLayout.endRefreshing();
                                mRefreshLayout.endLoadingMore();

                                break;
                            case RvModel.HttpTaskStatusFinish:

//                                ToastUtil.show("finish");

                                break;

                        }
                    }
                });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        nextPage = 0;
        refresh();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(nextPage == 0)
                return false;
        refresh();
        return true;
    }

    @Override
    public void onRVItemClick(View v, int position) {
        showToast("点击了条目 " + mAdapter.getItem(position).getName());
    }

    @Override
    public boolean onRVItemLongClick(View v, int position) {
        showToast("长按了条目 " + mAdapter.getItem(position).getName());
        return true;
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if (v.getId() == R.id.tv_item_normal_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(View v, int position) {
        if (v.getId() == R.id.tv_item_normal_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).getName());
            return true;
        }
        return false;
    }

//    @Override
//    public void beforRequest() {
//
//    }
//
//    @Override
//    public void onSuccess(HttpTaskResult result) {
//
//        Log.i(TAG, result.toString());
//        JsonObject object = result.data;
//        JsonArray array = object.getAsJsonArray("cates");
//        List<CateModel> refreshModels = new GsonBuilder().create().fromJson(array.toString(), new TypeToken<ArrayList<CateModel>>() {
//        }.getType());
//        if(nextPage == 0) {
//            mRefreshLayout.endRefreshing();
//            mAdapter.setDatas(refreshModels);
//        }else{
//            mRefreshLayout.endLoadingMore();
//            mAdapter.addMoreDatas(refreshModels);
//        }
//
//        nextPage = object.get("nextPage").getAsInt();
//
//    }
//
//    @Override
//    public void onFailure(Exception error) {
//
//        mRefreshLayout.endRefreshing();
//
//    }
//
//    @Override
//    public void finishRequest() {
////        mRefreshLayout.endRefreshing();
//    }
}