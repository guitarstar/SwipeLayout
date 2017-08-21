package com.solo.library;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingyiyong on 2017/8/18.
 */

public class SlideAdapterImpl<T extends ISlide> implements ISlideAdapter {
    private final String TAG = "SwipeView-BaseAdapter";

    List<ISlide> mSlideViews = new ArrayList<ISlide>();

    OnClickSlideItemListener mItemClickListener;

    /**
     * 子类必须调用，绑定状态监听
     * @param slideTouchView
     */
    @Override
    public void bindSlideState(View slideTouchView){
        if(slideTouchView instanceof ISlide){
            T mSlideView = ((T)slideTouchView);
            mSlideView.setOnSlideStateListener(new SlideStateListener(){
                @Override
                public void onOpened(ISlide v) {
                    if(!mSlideViews.contains(v))
                        mSlideViews.add(v);
                }

                @Override
                public void onClosed(ISlide v) {
                    mSlideViews.remove(v);
                }

                @Override
                public void beforeOpen(ISlide v) {
                    closeAll();
                }

                @Override
                public void clamping(ISlide v) {
                    closeAllExcept(v);
                }

                @Override
                public void onForegroundViewClick(ISlide slideView, View v) {
                    if(mItemClickListener != null){
                        mItemClickListener.onClick(slideView , v , slideView.getPostion());
                        mItemClickListener.onItemClick(slideView, v, slideView.getPostion());
                    }
                }
            });

            if(getBindOnClickViewsIds() != null && getBindOnClickViewsIds().length > 0) {
                for (int i = 0 ; i < getBindOnClickViewsIds().length ; i++){
                    slideTouchView.findViewById(getBindOnClickViewsIds()[i]).setOnClickListener(new MyClickListener(mSlideView));
                }
            }
        }
    }

    /**
     * 子类必须调用，针对点击事件中可以从BackgroundView中获取点击position
     * @param slideTouchView
     * @param pos
     */
    @Override
    public void bindSlidePosition(View slideTouchView , int pos){
        if(slideTouchView instanceof ISlide) {
            T slideView = ((T)slideTouchView);
            slideView.setPostion(pos);
            closeAll();
        }
    }

    /**
     * 关闭所有
     */
    public void closeAll(){
        try {
            for(ISlide i : mSlideViews) {
                i.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭所有的，除了v
     * @param v
     */
    public void closeAllExcept(ISlide v){
        try {
            for(ISlide i : mSlideViews) {
                if (!i.equals(v)) {
                    i.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void setOnClickSlideItemListener(OnClickSlideItemListener listener){
        mItemClickListener = listener;
    }

    @Override
    public int[] getBindOnClickViewsIds() {
        return new int[0];
    }

    class MyClickListener implements View.OnClickListener{
        ISlide mISlide;

        public MyClickListener(final ISlide v){
            mISlide = v;
        }
        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onClick(mISlide , v , mISlide.getPostion());
            }
        }
    }
}
