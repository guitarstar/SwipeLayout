package com.solo.library;

import android.view.View;

/**
 * 滑动显示控件接口
 * Created by lingyiyong on 2015/12/17.
 * @author lingyiyong
 * @date 2015-12-17
 * @version 1.0
 */
public interface ISlide {
    /**
     * 打开
     * @param showAnim 是否以动画过度
     */
    void open(boolean... showAnim);

    /**
     * 关闭
     * @param showAnim 是否以动画过度
     */
    void close(boolean... showAnim);

    /**
     * 获取前景布局
     * @return
     */
    View getForegroundView();

    /**
     * 获取背景布局
     * @return
     */
    View getBackgroundView();

    /**
     * 是否打开
     * @return
     */
    boolean isOpen();

    /**
     * 设置slide状态变化监听器
     * @param listener
     */
    void setOnSlideStateListener(SlideStateListener listener);

    /**
     * 设置位置
     * @param pos
     */
    void setPostion(int pos);

    /**
     * 获取位置
     * @return
     */
    int getPostion();
}
