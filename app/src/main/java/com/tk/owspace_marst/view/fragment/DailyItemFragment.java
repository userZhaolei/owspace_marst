package com.tk.owspace_marst.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tk.owspace_marst.R;
import com.tk.owspace_marst.app.GlideApp;
import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Zhaolei
 * 时间:2018/7/6
 */

public class DailyItemFragment extends Fragment {
    @BindView(R.id.month_tv)
    TextView monthTv;
    @BindView(R.id.year_tv)
    TextView yearTv;
    @BindView(R.id.date_rl)
    RelativeLayout dateRl;
    @BindView(R.id.calendar_iv)
    ImageView calendarIv;

    public static Fragment getInstance(Item item) {
        Fragment fragment = new DailyItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_daily, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Item item = getArguments().getParcelable("item");
        GlideApp.with(getActivity()).load(item.getThumbnail()).centerCrop().into(calendarIv);
        String[] arrayOfString = TimeUtil.getCalendarShowTime(item.getUpdate_time());
        if ((arrayOfString != null) && (arrayOfString.length == 3)) {
            monthTv.setText(arrayOfString[1] + " , " + arrayOfString[2]);
            yearTv.setText(arrayOfString[0]);
        }
    }
}
