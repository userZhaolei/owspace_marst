package com.tk.owspace_marst.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.view.fragment.DailyItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Zhaolei
 * 时间:2018/7/6
 */

public class DailyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Item> artList = new ArrayList<>();
    private Context context;

    public DailyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return DailyItemFragment.getInstance(artList.get(position));
    }

    @Override
    public int getCount() {
        return artList.size();
    }

    public void setDataList(List<Item> data) {
        artList.addAll(data);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        if (artList.size() == 0) {
            return "0";
        }
        Item item = artList.get(artList.size() - 1);
        return item.getId();
    }

    public String getLastItemCreateTime() {
        if (artList.size() == 0) {
            return "0";
        }
        Item item = artList.get(artList.size() - 1);
        return item.getCreate_time();
    }
}
