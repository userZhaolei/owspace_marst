package com.tk.owspace_marst.di.component;

import com.tk.owspace_marst.di.modules.DetailModule;
import com.tk.owspace_marst.di.scopes.UserScope;
import com.tk.owspace_marst.view.activity.AudioDetailActivity;
import com.tk.owspace_marst.view.activity.DetailActivity;
import com.tk.owspace_marst.view.activity.VideoDetailActivity;

import dagger.Component;

/**
 * Zhaolei
 * 时间:2018/6/29
 */
@UserScope
@Component(modules = DetailModule.class, dependencies = NetComponent.class)
public interface DetailCompoment {
     void inject(DetailActivity detailActivity);

    void inject(VideoDetailActivity activity);

    void inject(AudioDetailActivity activity);
}
