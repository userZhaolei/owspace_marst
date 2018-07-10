package com.tk.owspace_marst.di.component;

import com.tk.owspace_marst.di.modules.MainModule;
import com.tk.owspace_marst.di.scopes.UserScope;
import com.tk.owspace_marst.view.activity.MainActivity;

import dagger.Component;

/**
 * Zhaolei
 * 时间:2018/6/28
 */
@UserScope
@Component(modules = MainModule.class, dependencies = NetComponent.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
