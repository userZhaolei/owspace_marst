package com.tk.owspace_marst.di.modules;

import com.tk.owspace_marst.presenter.ArtContract;

import dagger.Module;
import dagger.Provides;

/**
 * Zhaolei
 * 时间:2018/7/6
 */
@Module
public class DailyModule {

    private ArtContract.View mView;

    public DailyModule(ArtContract.View mView) {
        this.mView = mView;
    }
    @Provides
    public ArtContract.View provideView(){
        return mView;
    }
}
