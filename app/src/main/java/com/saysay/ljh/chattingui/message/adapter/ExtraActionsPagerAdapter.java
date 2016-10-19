package com.saysay.ljh.chattingui.message.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.message.actions.BaseAction;

import java.util.List;

/**
 * Created by ljh on 2016/10/19.
 */

public class ExtraActionsPagerAdapter extends PagerAdapter {

    private List<BaseAction> actions;
    private final int ITEM_COUNT_GRID_VIEW = 8;
    private Context mContext;
    private int pagers;
    private ViewPager mViewPager;


    public ExtraActionsPagerAdapter(Context context, ViewPager viewPager,List<BaseAction> baseActions) {
        actions=baseActions;
        this.mContext = context;
        this.mViewPager = viewPager;
        this.pagers = (actions.size() + ITEM_COUNT_GRID_VIEW - 1) / ITEM_COUNT_GRID_VIEW;
    }


    @Override
    public int getCount() {
        return pagers;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int end = (position + 1) * ITEM_COUNT_GRID_VIEW > actions.size() ? actions
                .size() : (position + 1) * ITEM_COUNT_GRID_VIEW;
        List<BaseAction> subBaseActions = actions.subList(position
                * ITEM_COUNT_GRID_VIEW, end);

        GridView gridView = new GridView(mContext);
        gridView.setAdapter(new ExtarActionsGridAdapter(mContext, subBaseActions));
        gridView.setNumColumns(4);
        gridView.setSelector(R.color.transparent);
        gridView.setHorizontalSpacing(0);
        gridView.setVerticalSpacing(0);
        gridView.setGravity(Gravity.CENTER);
        gridView.setTag(Integer.valueOf(position));
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = ((Integer) parent.getTag()) * ITEM_COUNT_GRID_VIEW + position;
                actions.get(index).onClick();
            }
        });

        container.addView(gridView);
        return gridView;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
