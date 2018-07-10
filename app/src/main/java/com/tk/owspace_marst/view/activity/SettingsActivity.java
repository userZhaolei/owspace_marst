package com.tk.owspace_marst.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tk.owspace_marst.R;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.push_toggle)
    Switch pushToggle;
    @BindView(R.id.cacheSize)
    TextView cacheSize;
    @BindView(R.id.cacheLayout)
    RelativeLayout cacheLayout;
    @BindView(R.id.about)
    RelativeLayout about;
    @BindView(R.id.feedback)
    RelativeLayout feedback;
    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.checkUpgrade)
    RelativeLayout checkUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        File file = Glide.getPhotoCacheDir(this);  //计算Glide 缓存大小
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(getDirSize(file));
        cacheSize.setText(dd + "M");
    }

    private float getDirSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                float size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {
                float size = (float) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0.0f;
        }
    }

    @OnClick(R.id.cacheLayout)
    public void onClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplication()).clearDiskCache();//清除Glide缓存
            }
        }).start();
        cacheSize.setText("0.00M");
    }

    @OnClick({R.id.push_toggle, R.id.about, R.id.feedback, R.id.checkUpgrade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.push_toggle:
                break;
            case R.id.about:
                break;
            case R.id.feedback:
                break;
            case R.id.checkUpgrade:
                break;
        }
    }
}
