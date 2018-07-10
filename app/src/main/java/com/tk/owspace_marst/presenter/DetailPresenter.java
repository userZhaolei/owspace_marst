package com.tk.owspace_marst.presenter;

import com.tk.owspace_marst.model.api.ApiService;
import com.tk.owspace_marst.model.entity.DetailEntity;
import com.tk.owspace_marst.model.entity.Result;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Zhaolei
 * 时间:2018/6/29
 */

public class DetailPresenter implements DetailContract.Presenter {

    private ApiService apiService;
    private DetailContract.View view;

    @Inject
    public DetailPresenter(ApiService apiService, DetailContract.View view) {
        this.apiService = apiService;
        this.view = view;
    }


    @Override
    public void getDetail(String itemId) {
        apiService.getDetail("api", "getPost", itemId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result.Data<DetailEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showOnFailure();
                    }

                    @Override
                    public void onNext(Result.Data<DetailEntity> detailEntityData) {
                        view.updateListUI(detailEntityData.getDatas());
                    }
                });
    }
}
