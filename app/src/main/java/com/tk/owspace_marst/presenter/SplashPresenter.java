package com.tk.owspace_marst.presenter;

import com.orhanobut.logger.Logger;
import com.tk.owspace_marst.app.MyApplication;
import com.tk.owspace_marst.model.api.ApiService;
import com.tk.owspace_marst.model.entity.SplashEntity;
import com.tk.owspace_marst.util.NetUtil;
import com.tk.owspace_marst.util.OkHttpImageDownloader;
import com.tk.owspace_marst.util.TimeUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Zhaolei
 * 时间:2018/6/27
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;
    private ApiService apiService;
    @Inject
    public SplashPresenter(SplashContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
        Logger.d("apppp:"+apiService);
    }
    @Override
    public void getSplash(String deviceId ) {
        String client = "android";
        String version = "1.3.0";
        Long time = TimeUtil.getCurrentSeconds();
        apiService.getSplash(client,version,time,deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<SplashEntity>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("load splash onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e,"load splash failed:");
                    }
                    @Override
                    public void onNext(SplashEntity splashEntity) {
                        if (NetUtil.isWifi(MyApplication.getInstance().getApplicationContext())){
                            if (splashEntity != null){
                                List<String> imgs = splashEntity.getImages();
                                for (String url:imgs) {
                                    OkHttpImageDownloader.download(url);
                                }
                            }
                        }else{
                            Logger.i("不是WIFI环境,就不去下载图片了");
                        }
                    }
                });
    }

}
