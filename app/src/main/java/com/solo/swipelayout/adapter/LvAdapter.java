package com.solo.swipelayout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solo.library.SlideBaseAdapter;
import com.solo.library.SlideTouchView;
import com.solo.swipelayout.R;

import java.util.List;

import static com.solo.swipelayout.R.id.tv;

/**
 * Created by lingyiyong on 2017/8/21.
 */

public class LvAdapter extends SlideBaseAdapter {
    List<Integer> list;
    public LvAdapter(List<Integer> list) {
        this.list = list;
    }

    @Override
    public int[] getBindOnClickViewsIds() {
        return new int[]{R.id.btn_del};
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = new MyViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, null);
            holder.tv = (TextView) convertView.findViewById(tv);
            holder.mSlideTouchView = (SlideTouchView) convertView.findViewById(R.id.mSlideTouchView);
            convertView.setTag(holder);

            /**
             * must call
             */
            bindSlideState(holder.mSlideTouchView);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        /**
         * must call
         */
        bindSlidePosition(holder.mSlideTouchView, position);

        holder.tv.setText(String.valueOf(list.get(position)));
        return convertView;
    }

    class MyViewHolder {
        TextView tv;
        SlideTouchView mSlideTouchView;
    }
}
