package com.tk.owspace_marst.di.modules;

import com.tk.owspace_marst.presenter.MainContract;

import dagger.Module;
import dagger.Provides;

/**
 * Zhaolei
 * 时间:2018/6/28
 */

@Module
public class MainModule {

    private MainContract.View view;

    public MainModule(MainContract.View view) {
        this.view = view;
    }

    @Provides
    public MainContract.View provideView() {
        return view;
    }

}
