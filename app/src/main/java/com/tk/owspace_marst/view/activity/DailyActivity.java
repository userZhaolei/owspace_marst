package com.tk.owspace_marst.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tk.owspace_marst.R;
import com.tk.owspace_marst.app.MyApplication;
import com.tk.owspace_marst.di.component.DaggerDailyComponent;
import com.tk.owspace_marst.di.modules.DailyModule;
import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.presenter.ArtContract;
import com.tk.owspace_marst.presenter.ArtPresenter;
import com.tk.owspace_marst.util.AppUtil;
import com.tk.owspace_marst.view.adapter.DailyViewPagerAdapter;
import com.tk.owspace_marst.view.widget.VerticalViewPager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyActivity extends BaseActivity implements ArtContract.View {
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;


    @Inject
    ArtPresenter presenter;


    private int page = 1;
    private static final int MODE = 4;
    private boolean isLoading = true;
    private String deviceId;
    private DailyViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.bind(this);
        initPresenter();
        initView();
        deviceId = AppUtil.getDeviceId(this);
        presenter.getListByPage(page, MODE, "0", deviceId, "0");
    }

    private void initPresenter() {
        DaggerDailyComponent.builder().netComponent(MyApplication.get(this).getNetCompnent()).dailyModule(new DailyModule(this)).build().inject(this);
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("");
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        viewPagerAdapter = new DailyViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPagerAdapter.getCount() <= position + 2 && !isLoading) { //获取条目数
                    Logger.e("page=" + page + ",getLastItemId=" + viewPagerAdapter.getLastItemId());
                    presenter.getListByPage(page, 0, viewPagerAdapter.getLastItemId(), deviceId, viewPagerAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        isLoading = false;
        viewPagerAdapter.setDataList(itemList);
        page++;
    }

    @Override
    public void showOnFailure() {
        if (viewPagerAdapter.getCount() == 0) {
            showNoData();
        } else {
            Toast.makeText(this, "加载数据失败，请检查您的网络", Toast.LENGTH_SHORT).show();
        }
    }
}
