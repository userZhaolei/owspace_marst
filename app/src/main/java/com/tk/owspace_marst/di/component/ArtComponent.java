package com.tk.owspace_marst.di.component;

import com.tk.owspace_marst.di.modules.ArtModule;
import com.tk.owspace_marst.di.scopes.UserScope;
import com.tk.owspace_marst.view.activity.ArtActivity;

import dagger.Component;

/**
 * Zhaolei
 * 时间:2018/7/4
 */
@UserScope
@Component(modules = ArtModule.class, dependencies = NetComponent.class)
public interface ArtComponent {
     void inject(ArtActivity artActivity);
}

