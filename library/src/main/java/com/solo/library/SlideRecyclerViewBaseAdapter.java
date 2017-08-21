package com.solo.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 滑动item的基类适配器
 * Created by lingyiyong on 2015/12/16.
 */
public abstract class SlideRecyclerViewBaseAdapter<T extends ISlide> extends RecyclerView.Adapter implements ISlideAdapter {
    private SlideAdapterImpl mSlideAdapterImpl;

    public SlideRecyclerViewBaseAdapter() {
        mSlideAdapterImpl = new SlideAdapterImpl(){
            @Override
            public int[] getBindOnClickViewsIds() {
                return SlideRecyclerViewBaseAdapter.this.getBindOnClickViewsIds();
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

    public void setupRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    closeAll();
                }
            });
        }
    }
}
