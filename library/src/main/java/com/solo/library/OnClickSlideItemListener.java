package com.solo.library;

import android.view.View;

/**
 * Created by lingyiyong on 2017/8/18.
 */

public interface OnClickSlideItemListener {
    void onItemClick(ISlide iSlideView, View v, int position);
    void onClick(ISlide iSlideView, View v, int position);
}
