package com.tk.owspace_marst.presenter;

import com.tk.owspace_marst.model.entity.Item;

import java.util.List;

/**
 * Zhaolei
 * 时间:2018/7/4
 */

public interface ArtContract {
    interface Presenter {
        void getListByPage(int page, int model, String pageId, String deviceId, String createTime);
    }

    interface View {
        void showLoading();

        void dismissLoading();

        void showNoData();

        void showNoMore();

        void updateListUI(List<Item> itemList);

        void showOnFailure();
    }
}
