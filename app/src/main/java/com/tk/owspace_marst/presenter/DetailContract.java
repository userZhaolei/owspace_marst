package com.tk.owspace_marst.presenter;

import com.tk.owspace_marst.model.entity.DetailEntity;
import com.tk.owspace_marst.model.entity.Result;

/**
 * Zhaolei
 * 时间:2018/6/29
 */

public interface DetailContract {

    interface View {
        void showLoading();
        void dismissLoading();
        void updateListUI(DetailEntity detailEntity);
        void showOnFailure();
    }

    interface Presenter {
        void getDetail(String itemId);
    }
}
