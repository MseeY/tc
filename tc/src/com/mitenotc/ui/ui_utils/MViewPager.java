package com.mitenotc.ui.ui_utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
//自定义的viewpager用于传递gallery的 touch 事件.gallery之下的部分 中断水平方向的 touch事件的分发
public class MViewPager extends ViewPager{

	private View[] picViews;//用于获取当前的viewPager中的gallery的位置,以确定y的值
	private int x;
	private int y;
	public MViewPager(Context context,View[] picViews) {
		super(context);
		this.picViews = picViews;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = (int) event.getX();
			y = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			View picView = picViews[getCurrentItem()];
			
			//获取相对窗口的坐标
			int[] location = new int[2];
			picView.getLocationInWindow(location);
			////System.out.println("location y = "+location[1]);
			int picHeight = picView.getHeight()+location[1];
			////System.out.println("picHeight = "+picHeight);
			if(event.getY()<picHeight){
				return false;
			}else {
				int dx = (int) Math.abs(event.getX()-x);
				int dy = (int) Math.abs(event.getY()-y);
				if(dx>dy && dx >10){
					return true;
				}else {
					return false;
				}
			}
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(event);
	}
}
