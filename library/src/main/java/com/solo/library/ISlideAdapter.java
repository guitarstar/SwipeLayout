package com.solo.library;

import android.view.View;

/**
 * Created by lingyiyong on 2017/8/18.
 */

public interface ISlideAdapter {
    void bindSlideState(View slideTouchView);
    void bindSlidePosition(View slideTouchView , int pos);
    void setOnClickSlideItemListener(OnClickSlideItemListener listener);
    int[] getBindOnClickViewsIds();
    void closeAll();
}
