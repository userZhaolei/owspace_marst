package com.tk.owspace_marst.di.modules;

import com.tk.owspace_marst.presenter.DetailContract;

import dagger.Module;
import dagger.Provides;

/**
 * Zhaolei
 * 时间:2018/6/29
 */
@Module
public class DetailModule {
    private DetailContract.View view;

    public DetailModule(DetailContract.View view) {
        this.view = view;
    }

    @Provides
    public DetailContract.View providesView() {
        return view;
    }
}
