package com.tk.owspace_marst.app;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tk.owspace_marst.R;
import com.tk.owspace_marst.di.component .DaggerNetComponent;
import com.tk.owspace_marst.di.component.NetComponent;
import com.tk.owspace_marst.di.modules.NetModule;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Zhaolei
 * 时间:2018/6/27
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private NetComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initLogger();
        initDataBase();
        initTypeFace();
        initNet();
    }

    private void initNet() {
        component = DaggerNetComponent.builder().netModule(new NetModule())
                .build();
    }

    /**
     * 设置默认的字体
     */
    private void initTypeFace() {
        CalligraphyConfig calligraphyConfig = new CalligraphyConfig.Builder().setDefaultFontPath("fonts/PMingLiU.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(calligraphyConfig);

    }

    private void initDataBase() {
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public NetComponent getNetCompnent() {
        return component;
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

}
