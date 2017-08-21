package com.solo.library;

import android.view.View;

/**
 * Created by lingyiyong on 2015/12/17.
 */
public interface SlideStateListener<T extends ISlide> {
    void onOpened(T v);
    void onClosed(T v);
    void beforeOpen(T v);
    void clamping(T v);
    void onForegroundViewClick(T slideView, View v);
}
