package com.solo.library;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 滑动显示控件
 * @author guitarstar
 * @date 2015-12-17
 * @version 2.0
 */
public class SlideTouchView extends RelativeLayout implements View.OnClickListener , ISlide{
    private final String TAG = "SlideTouchView";
    private View fgView , bgView;
    private ViewDragHelper mDrager;
    private SlideStateListener mStateListener;
    private final int DRAG_LEFT = -1 , DRAG_RIGHT = 1;
    private int dragMode = DRAG_LEFT;
    private float minX , maxX;
    private int xOffset; //记录偏移值

    public SlideTouchView(Context context) {
        super(context);
    }

    public SlideTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrager = ViewDragHelper.create(this, 5f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return isEnabled() && child == fgView;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return bgView.getMeasuredWidth();
            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if(mStateListener != null){
                    mStateListener.clamping(SlideTouchView.this);
                }
                return getPositionX(left);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if(Math.abs(fgView.getLeft()) != 0 || Math.abs(fgView.getLeft()) != bgView.getMeasuredWidth()){
                    float x = fgView.getLeft() + 0.1f * xvel;
                    mDrager.smoothSlideViewTo(fgView,
                            Math.abs(getPositionX(x)) > bgView.getMeasuredWidth() / 2 ? bgView.getMeasuredWidth() * dragMode : 0, 0);
                    postInvalidate();
                }

            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if(changedView == fgView && getParent() != null)
                    getParent().requestDisallowInterceptTouchEvent(fgView.getLeft() != 0 ? true : false);
                if(mStateListener != null){
                    if(left == 0){
                        mStateListener.onClosed(SlideTouchView.this);
                    }else if(Math.abs(left) == bgView.getMeasuredWidth()){
                        mStateListener.onOpened(SlideTouchView.this);
                    }
                }
            }
        });
    }

    public View getForegroundView() {
        return fgView;
    }

    public View getBackgroundView() {
        return bgView;
    }

    @Override
    public void open(boolean...showAnim){
        if(mStateListener != null){
            mStateListener.beforeOpen(this);
        }
        if(showAnim != null && showAnim.length > 0 && showAnim[0]){
            mDrager.smoothSlideViewTo(fgView, bgView.getMeasuredWidth() * dragMode, 0);
        } else {
            fgView.offsetLeftAndRight(dragMode * (bgView.getMeasuredWidth() - fgView.getLeft()));
        }
        postInvalidate();
    }

    @Override
    public void close(boolean...showAnim){
        if (!isOpen()) return;
        if(showAnim != null && showAnim.length > 0 && showAnim[0]){
            mDrager.smoothSlideViewTo(fgView, 0, 0);
        } else {
            if(mDrager.continueSettling(true)){
                mDrager.abort();
            }
            fgView.offsetLeftAndRight(-fgView.getLeft());
        }
        postInvalidate();
    }

    @Override
    public boolean isOpen(){
        return Math.abs(fgView.getLeft()) > bgView.getMeasuredWidth() / 2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //drag range
        if(dragMode == DRAG_LEFT){
            minX = -bgView.getMeasuredWidth();
            maxX = 0;
        }else{
            minX = 0;
            maxX = bgView.getMeasuredWidth();
        }
    }
    private int getPositionX(float x){
        if(x < minX) x = minX;
        if(x > maxX) x = maxX;
        return (int) x;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() != 2)
            throw new IllegalArgumentException("must contain only two child view");
        fgView = getChildAt(1);
        bgView = getChildAt(0);
        if(!(fgView instanceof ViewGroup && bgView instanceof ViewGroup))
            throw new IllegalArgumentException("ForegroundView and BackgoundView must be a subClass of ViewGroup");
        LayoutParams param = (LayoutParams)bgView.getLayoutParams();
        param.addRule(dragMode == DRAG_LEFT ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        param.width = LayoutParams.WRAP_CONTENT;

        //bind onClick Event
        fgView.setOnClickListener(this);

        idleLeft = fgView.getLeft();
    }

    @Override
    public void computeScroll() {
        if(mDrager.continueSettling(true)){
            postInvalidate();
        } else {
            xOffset = fgView.getLeft();
        }
    }

    @Override
    public void setOnSlideStateListener(SlideStateListener listener){
        mStateListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(!isEnabled()) return;
        if(v == fgView && isOpen()) {
            close(true);
            return;
        }
        if(mStateListener != null && v == fgView) {
            mStateListener.onForegroundViewClick(SlideTouchView.this , v);
        }
    }
    @Override
    public void setPostion(int pos) {
        bgView.setTag(pos);
    }

    @Override
    public int getPostion() {
        Object tag = bgView.getTag();
        if(tag != null && tag instanceof Integer){
            return (Integer) tag;
        }
        return -1;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDrager.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDrager.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!changed) {
            fgView.offsetLeftAndRight(xOffset); //防止onlayout时将拖动的位置重置
        }
    }

    private int idleLeft;
    public boolean isIdleState() {
        if (fgView != null) {
            return fgView.getLeft() == idleLeft;
        }
        return true;
    }
}