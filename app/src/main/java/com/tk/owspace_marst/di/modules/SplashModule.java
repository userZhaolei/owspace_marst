package com.tk.owspace_marst.di.modules;

import com.tk.owspace_marst.presenter.SplashContract;

import dagger.Module;
import dagger.Provides;

/**
 * Zhaolei
 * 时间:2018/6/27
 */

@Module
public class SplashModule {

    private SplashContract.View view;

    public SplashModule(SplashContract.View view) {
        this.view = view;
    }

    @Provides
    public SplashContract.View provideView(){
        return view;
    }
}
