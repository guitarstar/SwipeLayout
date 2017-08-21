package com.solo.library;

import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 滑动item的基类适配器
 * Created by lingyiyong on 2015/12/16.
 */
public abstract class SlideBaseAdapter<T extends ISlide> extends BaseAdapter implements ISlideAdapter {
    private SlideAdapterImpl mSlideAdapterImpl;

    public SlideBaseAdapter() {
        mSlideAdapterImpl = new SlideAdapterImpl(){
            @Override
            public int[] getBindOnClickViewsIds() {
                return SlideBaseAdapter.this.getBindOnClickViewsIds();
            }
        };
    }

    @Override
    public void bindSlideState(View slideTouchView) {
        mSlideAdapterImpl.bindSlideState(slideTouchView);
    }

    @Override
    public void bindSlidePosition(View slideTouchView, int pos) {
        mSlideAdapterImpl.bindSlidePosition(slideTouchView, pos);
    }

    public void setOnClickSlideItemListener(OnClickSlideItemListener listener) {
        mSlideAdapterImpl.setOnClickSlideItemListener(listener);
    }

    public void closeAll() {
        mSlideAdapterImpl.closeAll();
    }

    public abstract int[] getBindOnClickViewsIds();

    public void setupListView(ListView listView) {
        if (listView != null) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    closeAll();
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }
}
