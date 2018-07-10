package com.tk.owspace_marst.di.modules;

import com.tk.owspace_marst.presenter.ArtContract;

import dagger.Module;
import dagger.Provides;

/**
 * Zhaolei
 * 时间:2018/7/4
 */
@Module
public class ArtModule {
    private ArtContract.View view;

    public ArtModule(ArtContract.View view) {
        this.view = view;
    }

    @Provides
    public ArtContract.View providesView() {
        return view;
    }
}
