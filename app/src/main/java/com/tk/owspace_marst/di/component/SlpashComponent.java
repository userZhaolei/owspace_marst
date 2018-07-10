package com.tk.owspace_marst.di.component;

import com.tk.owspace_marst.di.modules.SplashModule;
import com.tk.owspace_marst.di.scopes.UserScope;
import com.tk.owspace_marst.view.activity.SplashActivity;

import dagger.Component;

/**
 * Zhaolei
 * 时间:2018/6/27
 */
@UserScope
@Component(modules = SplashModule.class, dependencies = NetComponent.class)
public interface SlpashComponent {
    void inject(SplashActivity splashActivity);
}
