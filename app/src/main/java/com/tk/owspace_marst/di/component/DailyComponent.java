package com.tk.owspace_marst.di.component;

import com.tk.owspace_marst.di.modules.DailyModule;
import com.tk.owspace_marst.di.scopes.UserScope;
import com.tk.owspace_marst.view.activity.DailyActivity;

import dagger.Component;

/**
 * Zhaolei
 * 时间:2018/7/6
 */
@UserScope
@Component(modules = DailyModule.class, dependencies = NetComponent.class)
public interface DailyComponent {
    void inject(DailyActivity activity);
}
