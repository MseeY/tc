package com.mitenotc.tc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.mitenotc.ui.base.BaseActivity;

/**
 * 第二层 Activity
 * @author mitenotc
 */
public class SecondActivity extends BaseActivity{

	public static PopupWindow pop;
	public static TextView pop_ball_tv;
	@Override
	protected void onCreate(Bundle arg0) { 
		super.onCreate(arg0);
		targetCls = ThirdActivity.class;
		initPop();
	}
	/**
	 * 初始化 专门用于 购彩界面的选号是显示泡泡的 popwindow
	 */
	private void initPop() {
		if(pop != null) //如果 popwindow已经创建,则不再重新创建
			return;
		View view = View.inflate(this, R.layout.f_pop_ball, null);
		pop_ball_tv = (TextView) view.findViewById(R.id.pop_ball_tv);
		
		int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(measureSpec, measureSpec);
		int popWidth = view.getMeasuredWidth();
		int popHeight = view.getMeasuredHeight();
		
		pop = new PopupWindow(view,popWidth,popHeight);
		pop.setAnimationStyle(0);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
