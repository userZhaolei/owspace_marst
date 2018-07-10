package com.tk.owspace_marst.di.modules;

import com.tk.owspace_marst.BuildConfig;
import com.tk.owspace_marst.model.api.ApiService;
import com.tk.owspace_marst.model.api.StringConverterFactory;
import com.tk.owspace_marst.model.utils.EntityUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Zhaolei
 * 时间:2018/6/27
 */
@Module
public class NetModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);//设置日志的级别，如果为debug则不开启日志
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)   //连接超时
                .writeTimeout(30, TimeUnit.SECONDS)    //写入超时
                .readTimeout(30, TimeUnit.SECONDS)     //读取超时
                .addInterceptor(loggingInterceptor)             //添加logger拦截器
                .build();
        return okhttpClient;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okhttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okhttpClient)
                .baseUrl("http://static.owspace.com/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(EntityUtils.gson))//
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit) {

        return retrofit.create(ApiService.class);
    }


}
