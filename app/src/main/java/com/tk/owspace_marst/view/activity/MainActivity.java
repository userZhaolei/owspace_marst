package com.tk.owspace_marst.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;
import com.tk.owspace_marst.R;
import com.tk.owspace_marst.app.MyApplication;
import com.tk.owspace_marst.di.component.DaggerMainComponent;
import com.tk.owspace_marst.di.modules.MainModule;
import com.tk.owspace_marst.model.entity.Event;
import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.presenter.MainContract;
import com.tk.owspace_marst.presenter.MainPresenter;
import com.tk.owspace_marst.util.AppUtil;
import com.tk.owspace_marst.util.PreferenceUtils;
import com.tk.owspace_marst.util.TimeUtil;
import com.tk.owspace_marst.util.tool.RxBus;
import com.tk.owspace_marst.view.adapter.VerticalPagerAdapter;
import com.tk.owspace_marst.view.fragment.LeftMenuFragment;
import com.tk.owspace_marst.view.fragment.RightMenuFragment;
import com.tk.owspace_marst.view.widget.LunarDialog;
import com.tk.owspace_marst.view.widget.VerticalViewPager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements MainContract.View {
    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;
    private SlidingMenu slidingMenu;

    @Inject
    MainPresenter presenter;

    private boolean isLoading = true;
    private int page = 1;
    private String deviceId;
    private long mLastClickTime;

    private LeftMenuFragment leftMenu;
    private Subscription subscription;
    private VerticalPagerAdapter pagerAdapter;
    private RightMenuFragment rightMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initMenu();
        initPage();
        deviceId = AppUtil.getDeviceId(this);


        String getLunar = PreferenceUtils.getPrefString(this, "getLunar", null);
        if (!TimeUtil.getDate("yyyyMMdd").equals(getLunar)) {  //如果当前时间的话跟存入的时间不同就去这个图片
            loadRecommend();
        }

        loadData(1, 0, "0", "0");
    }

    private void loadRecommend() {
        presenter.getRecommend(deviceId);
    }

    private void initPage() {
        pagerAdapter = new VerticalPagerAdapter(getSupportFragmentManager());
        DaggerMainComponent.builder().
                mainModule(new MainModule(this))
                .netComponent(MyApplication.get(this).getNetCompnent())
                .build()
                .inject(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (pagerAdapter.getCount() <= position + 2 && !isLoading) {
                    if (isLoading) {
                        Toast.makeText(MainActivity.this, "正在努力加载...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Logger.i("page=" + page + ",getLastItemId=" + pagerAdapter.getLastItemId());
                    loadData(page, 0, pagerAdapter.getLastItemId(), pagerAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadData(int page, int mode, String pageId, String createTime) {
        isLoading = true;
        presenter.getListByPage(page, mode, pageId, deviceId, createTime);
    }

    private void initMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        slidingMenu.setMenu(R.layout.left_menu);
        leftMenu = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.left_menu, leftMenu).commit();//左边滑出

        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        rightMenu = new RightMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.right_menu, rightMenu).commit(); //右边滑出

        subscription = RxBus.getInstance().toObservable(Event.class)
                .subscribe(new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        slidingMenu.showContent();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
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
        Toast.makeText(this, "加载数据失败，请检查您的网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        isLoading = false;
        pagerAdapter.setDataList(itemList);
        page++;
    }

    @Override
    public void showOnFailure() {
    }

    @Override
    public void showLunar(String content) {
        Logger.e("showLunar:" + content);
        PreferenceUtils.setPrefString(this, "getLunar", TimeUtil.getDate("yyyyMMdd")); //
        LunarDialog lunarDialog = new LunarDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_lunar, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_iv);
        Glide.with(this).load(content).into(imageView);
        lunarDialog.setContentView(view);
        lunarDialog.show();
    }

    @OnClick({R.id.left_slide, R.id.right_slide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_slide:
                slidingMenu.showMenu();
                leftMenu.startAnim();
                break;
            case R.id.right_slide:
                slidingMenu.showSecondaryMenu();
                rightMenu.startAnim();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing() || slidingMenu.isSecondaryMenuShowing()) {
            slidingMenu.showContent();
        } else {
            if (System.currentTimeMillis() - mLastClickTime <= 2000L) {
                super.onBackPressed();
            } else {
                mLastClickTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
