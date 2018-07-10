package com.tk.owspace_marst.di.component;

import com.tk.owspace_marst.di.modules.NetModule;
import com.tk.owspace_marst.model.api.ApiService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Zhaolei
 * 时间:2018/6/27
 */
@Component(modules = NetModule.class)
@Singleton
public interface NetComponent {

    ApiService getApiService();

    OkHttpClient getOkHttp();

    Retrofit getRetrofit();
}
