package com.mitenotc.ui.ui_utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MGridView.BallAdapter;
import com.mitenotc.ui.ui_utils.MGridView.CustomItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PL120MGridView extends MGridView{
	private BaseAdapter mAdapter;
	private List<View> customItems;// 自定义item 的list
	private List<Integer> sampleNums;// 容器的数据样本//需要 具体事例 分别初始化的参数,默认初始化为 双色球的红球选号区 33 个球
	private int customItemViewID=0;
	
	public PL120MGridView(Context context) {
		super(context);
		init();
	}

	public PL120MGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PL120MGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {//向下传递
		return true;
	}
	
	private void init(){
		selectedBalls = new ArrayList<Integer>();
		sampleNums = new ArrayList<Integer>();
		mAdapter = new PL120MGVAdapter();
		
		setAdapter(mAdapter);
	}
	
	class PL120MGVAdapter extends BaseAdapter{
	
		@Override
		public int getCount() {
			if(sampleNums == null)
				return 0;
			return sampleNums.size();
		}
	
		@Override
		public Object getItem(int position) {
			return sampleNums.get(position);//返回选号数据
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			if(convertView == null){
				convertView = customItems.get(position);
			}
			if(parent.getChildCount() != position){
				return convertView;
			}
			CustomItem itemView=(CustomItem) convertView.findViewById(customItemViewID);
         // 设置球的显示
 		if (selectedBalls.contains(sampleNums.get(position))) {
 				itemView.selected();
 			}else{
 				itemView.unselected();
 			}
			return convertView;
		}
	
	}
	private int index;// 点击到的子控件的角标
	
	 @Override
	public boolean onTouchEvent(MotionEvent ev) {
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			View child=null;
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					index = pointToPosition(x, y);
//					child = getChildAt(index);
					break;
				case MotionEvent.ACTION_UP:
					if(customItems != null){
						////System.out.println("child instanceof CustomItem ======== "+(child instanceof CustomItem));
						
						if (child != null && index>=0 && child instanceof CustomItem) {
							CustomItem item = (CustomItem) child;
							if (selectedBalls.contains(sampleNums.get(index))) {
								item.unselected();
								selectedBalls.remove(sampleNums.get(index));
							} else {
								selectedBalls.add(sampleNums.get(index));
								if(maxNum > 0 && selectedBalls.size() >= maxNum){
									Toast.makeText(getContext(), "该选区最多只能选择"+maxNum+"个号", 0).show();
									return true;
								}
								item.selected();
							}
							
							Collections.sort(selectedBalls);
							// 球的有效点击 响应 接口
							if (actionUpListener != null) {
								actionUpListener.onActionUp();
							}
						}
					
					}
					mAdapter.notifyDataSetChanged();
					index = -1;
					break;
					
			}
				
		return true;
	}
	 
    /**
     * 初始化所需要的 item view
     * 
     * @param sampleNums
     * @param customItems
     * @param customItemViewID
     */
	 public void simpleInit_ListIV(List<Integer> sampleNums,List<View> customItems,int customItemViewID){
		 this.sampleNums=sampleNums;
		 this.customItems=customItems;
		 this.customItemViewID=customItemViewID;
	 }
	 
	 
}
