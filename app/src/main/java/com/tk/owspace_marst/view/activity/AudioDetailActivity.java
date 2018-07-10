package com.tk.owspace_marst.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.orhanobut.logger.Logger;
import com.tk.owspace_marst.R;
import com.tk.owspace_marst.app.GlideApp;
import com.tk.owspace_marst.app.MyApplication;
import com.tk.owspace_marst.di.component.DaggerDetailCompoment;
import com.tk.owspace_marst.di.modules.DetailModule;
import com.tk.owspace_marst.model.entity.DetailEntity;
import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.player.IPlayback;
import com.tk.owspace_marst.player.PlayBackService;
import com.tk.owspace_marst.player.PlayState;
import com.tk.owspace_marst.presenter.DetailContract;
import com.tk.owspace_marst.presenter.DetailPresenter;
import com.tk.owspace_marst.util.AppUtil;
import com.tk.owspace_marst.util.TimeUtils;
import com.tk.owspace_marst.util.tool.AnalysisHTML;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioDetailActivity extends BaseActivity implements DetailContract.View, ObservableScrollViewCallbacks, IPlayback.Callback {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.news_top_img_under_line)
    View newsTopImgUnderLine;
    @BindView(R.id.news_top_type)
    TextView newsTopType;
    @BindView(R.id.news_top_date)
    TextView newsTopDate;
    @BindView(R.id.news_top_title)
    TextView newsTopTitle;
    @BindView(R.id.news_top_author)
    TextView newsTopAuthor;
    @BindView(R.id.news_top_lead)
    TextView newsTopLead;
    @BindView(R.id.news_top_lead_line)
    View newsTopLeadLine;
    @BindView(R.id.news_top)
    LinearLayout newsTop;
    @BindView(R.id.news_parse_web)
    LinearLayout newsParseWeb;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.favorite)
    ImageView favorite;
    @BindView(R.id.write)
    ImageView write;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @Inject
    DetailPresenter presenter;
    @BindView(R.id.button_play_last)
    AppCompatImageView buttonPlayLast;
    @BindView(R.id.button_play_toggle)
    AppCompatImageView buttonPlayToggle;
    @BindView(R.id.button_play_next)
    AppCompatImageView buttonPlayNext;
    @BindView(R.id.text_view_progress)
    TextView textViewProgress;
    @BindView(R.id.seek_bar)
    AppCompatSeekBar seekBar;
    @BindView(R.id.text_view_duration)
    TextView textViewDuration;
    private int mParallaxImageHeight;

    private PlayBackService mPlaybackService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mPlaybackService = ((PlayBackService.LocalBinder) service).getService();
            register();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            unRegister();
            mPlaybackService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);
        ButterKnife.bind(this);
        initView();
        initPresenter();
        bindPlaybackService();
    }

    public void bindPlaybackService() {
        this.bindService(new Intent(this, PlayBackService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        Item item = bundle.getParcelable("item");
        if (item != null) {
            GlideApp.with(this).load(item.getThumbnail()).centerCrop().into(image);
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopType.setText("音 频");
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            newsTopLead.setLineSpacing(1.5f, 1.8f);
            presenter.getDetail(item.getId());
        }

    }

    private void register() {
        mPlaybackService.registerCallback(this);
    }

    private void unRegister() {
        if (mPlaybackService != null) {
            mPlaybackService.unregisterCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        unRegister();
        handleProgress.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void initPresenter() {
        DaggerDetailCompoment.builder().netComponent(MyApplication.get(this).getNetCompnent()).detailModule(new DetailModule(this)).build().inject(this);
    }

    private void initView() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlaybackService.seekTo(getSeekDuration(seekBar.getProgress()));
                playTimer();
            }
        });
    }

    private int getSeekDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / seekBar.getMax()));
    }

    private int getCurrentSongDuration() {
        int duration = 0;
        if (mPlaybackService != null) {
            duration = mPlaybackService.getDuration();
        }
        return duration;
    }

    private void initWebViewSetting() {
        WebSettings localWebSettings = this.webView.getSettings();
        localWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setLoadWithOverviewMode(true);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    String song;

    @Override
    public void updateListUI(DetailEntity detailEntity) {
        song = detailEntity.getFm();
        if (detailEntity.getParseXML() == 1) {
            int i = detailEntity.getLead().trim().length();
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), analysisHTML.HTML_STRING, newsParseWeb, i);
            newsTopType.setText("音 频");
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParams2WezeitUrl(detailEntity.getHtml5(), false));
        }

    }

    @Override
    public void showOnFailure() {

    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) i / mParallaxImageHeight);
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    public String addParams2WezeitUrl(String url, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(url);
        localStringBuffer.append("?client=android");
        localStringBuffer.append("&device_id=" + AppUtil.getDeviceId(this));
        localStringBuffer.append("&version=" + "1.3.0");
        if (paramBoolean) localStringBuffer.append("&show_video=0");
        else {
            localStringBuffer.append("&show_video=1");
        }
        return localStringBuffer.toString();
    }

    @OnClick(R.id.button_play_toggle)
    public void onClick() {
        if (mPlaybackService == null || song == null) {
            Logger.d("mPlaybackService == null");
            return;
        }
        if (mPlaybackService.isPlaying()) {
            if (song.equals(mPlaybackService.getSong())) {
                mPlaybackService.pause();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
            } else {
                mPlaybackService.play(song);
                buttonPlayToggle.setImageResource(R.drawable.ic_pause);
            }
        } else {
            if (song.equals(mPlaybackService.getSong())) {
                mPlaybackService.play(song);
            } else {
                mPlaybackService.play(song);
            }
            buttonPlayToggle.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void onComplete(PlayState status) {
        Logger.d("onComplete.......");
        cancelTimer();
    }

    @Override
    public void onPlayStatusChanged(PlayState status) {
        Logger.d("onPlayStatusChanged.......status=" + status);
        switch (status) {
            case INIT:
                break;
            case PREPARE:
                break;
            case PLAYING:
                updateDuration();
                playTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                Logger.d(mPlaybackService.getDuration());
                break;
            case PAUSE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
                break;
            case ERROR:

                break;
            case COMPLETE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
                seekBar.setProgress(0);
                textViewDuration.setText("00:00");
                textViewProgress.setText("00:00");
                break;
        }
    }

    @Override
    public void onPosition(int position) {
        Logger.d("onPosition.......=" + position);
    }

    Timer timer = null;

    private void playTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mPlaybackService == null) return;
                if (mPlaybackService.isPlaying()) {
                    handleProgress.post(runnable);
                }
            }
        }, 0, 1000);
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mPlaybackService.isPlaying()) {
                if (isFinishing()) {
                    return;
                }
                int progress = (int) (seekBar.getMax() * ((float) mPlaybackService.getProgress() / (float) mPlaybackService.getDuration()));
                updateProgressTextWithProgress(mPlaybackService.getProgress());
                if (progress >= 0 && progress <= seekBar.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(progress, true);
                    } else {
                        seekBar.setProgress(progress);
                    }
                }
            }
        }
    };
    Handler handleProgress = new Handler();

    private void updateProgressTextWithProgress(int progress) {
        textViewProgress.setText(TimeUtils.formatDuration(progress));
    }

    private void updateDuration() {
        textViewDuration.setText(TimeUtils.formatDuration(mPlaybackService.getDuration()));
    }
}
