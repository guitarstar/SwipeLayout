# SwipeLayout
![image](https://github.com/guitarstar/SwipeLayout/blob/master/screenshot/swipelayout.gif?raw=true)
[源码解析博客](http://blog.csdn.net/guitarstudio/article/details/48525339/ "源码解析博客-- 一分钟轻松打造左滑删除+下拉刷新Listview控件")

步骤 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			maven { url "https://jitpack.io" }
		}
	}
步骤 2. Add the dependency

	dependencies {
	        compile 'com.github.guitarstar:SwipeLayout:v1.0'
	}
  
步骤 3. 列表的布局layout编写
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.solo.library.SlideTouchView
        android:id="@+id/mSlideTouchView"
        android:layout_width="match_parent"
        android:layout_height="60dp">
        <!-- 下层布局 -->
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="horizontal">
            <Button
                android:id="@+id/btn_del"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:background="@android:color/holo_red_light"
                android:text="删除"/>
        </LinearLayout>
        <!-- 上层布局 -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#fff"><!-- 这里设个背景颜色将下层布局遮掩 -->
            <TextView
                android:id="@+id/tv"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:gravity="center"/>
        </LinearLayout>
    </com.solo.library.SlideTouchView>
</LinearLayout>


步骤 4. ListView的adapter继承SlideBaseAdapter（使用recyclerview的adapter继承SlideRecyclerViewBaseAdapter）

如果使用ListView的情况：

public class LvAdapter extends SlideBaseAdapter {
    List<Integer> list;
    public LvAdapter(List<Integer> list) {
        this.list = list;
    }


    @Override
    public int[] getBindOnClickViewsIds() {
        return new int[]{R.id.btn_del};  //必须调用, 删除按钮或者其他你想监听点击事件的View的id
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

            bindSlideState(holder.mSlideTouchView); //必须调用
            
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        
        bindSlidePosition(holder.mSlideTouchView, position);//必须调用

        holder.tv.setText(String.valueOf(list.get(position)));
        return convertView;
    }
    
    如果使用recyclerview的情况：
    public class RcyAdapter extends SlideRecyclerViewBaseAdapter {
        List<Integer> list;

        public RcyAdapter(List<Integer> list) {
            this.list = list;
        }

        @Override
        public int[] getBindOnClickViewsIds() {
            return new int[]{R.id.btn_del};  //必须调用, 删除按钮或者其他你想监听点击事件的View的id
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, null);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.tv.setText(String.valueOf(list.get(position)));

            bindSlidePosition(viewHolder.mSlideTouchView, position);//必须调用
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            SlideTouchView mSlideTouchView;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
                mSlideTouchView = (SlideTouchView) itemView.findViewById(R.id.mSlideTouchView);

                bindSlideState(mSlideTouchView);//必须调用
            }
        }
    }

步骤 5. 点击事件监听
    adapter.setupRecyclerView(mRecyclerView); //里面的逻辑是监听滚动关闭按钮显示 （ListView中调用adapter.setupListView(mListView);）
    adapter.setOnClickSlideItemListener(new OnClickSlideItemListener() {
        @Override
        public void onItemClick(ISlide iSlideView, View v, int position) {
            //点击item时会回调此方法（onClick中也会回调）
            Toast.makeText(v.getContext(), "click item position:" + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(ISlide iSlideView, View v, int position) {
            //控件的所有子控件的点击回调都会回调此方法
            if (v.getId() == R.id.btn_del) { //对删除按钮的监听（上面adapter的getBindOnClickViewsIds()中设置了R.id.btn_del）
                iSlideView.close(); //关闭当前的按钮
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    });
