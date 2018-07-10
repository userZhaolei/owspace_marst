package com.tk.owspace_marst.presenter;

import com.tk.owspace_marst.model.entity.Item;

import java.util.List;

/**
 * Zhaolei
 * 时间:2018/6/28
 */

public interface MainContract {

    interface View {
        void showLoading();

        void dismissLoading();

        void showNoData();

        void showNoMore();

        void updateListUI(List<Item> itemList);

        void showOnFailure();

        void showLunar(String content);
    }

    interface Presenter {
        void getListByPage(int page, int model, String pageId, String deviceId, String createTime);

        void getRecommend(String deviceId);
    }
}
