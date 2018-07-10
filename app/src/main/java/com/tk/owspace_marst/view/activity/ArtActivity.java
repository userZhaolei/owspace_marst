package com.tk.owspace_marst.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tk.owspace_marst.R;
import com.tk.owspace_marst.app.MyApplication;
import com.tk.owspace_marst.di.component.DaggerArtComponent;
import com.tk.owspace_marst.di.modules.ArtModule;
import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.presenter.ArtContract;
import com.tk.owspace_marst.presenter.ArtPresenter;
import com.tk.owspace_marst.util.AppUtil;
import com.tk.owspace_marst.view.adapter.ArtRecyclerAdapter;
import com.tk.owspace_marst.view.widget.CustomPrtHandler;
import com.tk.owspace_marst.view.widget.DividerItemDecoratio;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ArtActivity extends BaseActivity implements ArtContract.View {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout mPtrFrame;
    @Inject
    ArtPresenter presenter;
    private ArtRecyclerAdapter recycleViewAdapter;
    private int page = 1;
    private int mode = 1;
    private boolean isRefresh;
    private boolean hasMore = true;
    private String deviceId;
    private int lastVisibleItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);
        ButterKnife.bind(this);
        mode = getIntent().getIntExtra("mode", 1);
        initPresenter();
        initView();
    }

    private void initPresenter() {
        DaggerArtComponent.builder().artModule(new ArtModule(this)).netComponent(MyApplication.get(this).getNetCompnent()).build().inject(this);

    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        String tt = getIntent().getStringExtra("title");
        title.setText(tt);
        deviceId = AppUtil.getDeviceId(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recycleViewAdapter = new ArtRecyclerAdapter(this);
        recycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycleView.addItemDecoration(new DividerItemDecoratio(this));
        recycleView.setAdapter(recycleViewAdapter);


        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                hasMore = true;
                loadData(page, mode, "0", deviceId, "0");
            }
        });
        mPtrFrame.setOffsetToRefresh(200);
        mPtrFrame.autoRefresh(true);
        CustomPrtHandler header = new CustomPrtHandler(this, mode);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isRefresh && hasMore && (lastVisibleItem + 1 == recycleViewAdapter.getItemCount())) {
                    loadData(page, mode, recycleViewAdapter.getLastItemId(), deviceId, recycleViewAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
    }

    private void loadData(int page, int mode, String pageId, String deviceId, String createTime) {
        presenter.getListByPage(page, mode, pageId, deviceId, createTime);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNoData() {
    }

    @Override
    public void showNoMore() {
        hasMore = false;
        if (!isRefresh) {
            //显示没有更多
            recycleViewAdapter.setHasMore(false);
            recycleViewAdapter.notifyItemChanged(recycleViewAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        mPtrFrame.refreshComplete();
        page++;
        if (isRefresh) {
            recycleViewAdapter.setHasMore(true);
            recycleViewAdapter.setError(false);
            isRefresh = false;
            recycleViewAdapter.replaceAllData(itemList);
        } else {
            recycleViewAdapter.setArtList(itemList);
        }
    }

    @Override
    public void showOnFailure() {
        if (!isRefresh) {
            //显示失败
            recycleViewAdapter.setError(true);
            recycleViewAdapter.notifyItemChanged(recycleViewAdapter.getItemCount() - 1);
        } else {
            Toast.makeText(this, "~~~~(>_<)~~~~刷新失败", Toast.LENGTH_SHORT).show();
        }
    }
}
