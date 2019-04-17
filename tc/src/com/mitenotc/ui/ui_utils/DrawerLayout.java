package com.mitenotc.ui.ui_utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.DensityUtil;
/**
 * 购彩界面的下拉界面的自定义控件
 * 使用方法: 设置middleY 和bottomY的值,之后和普通的linearlayout 用法相同,该view中已经添加了一个 ScrollView,同时当该scrollView滚动到顶部时,向下滚,会把该linearlayout 向下移动. 
 * 专门用于 购彩界面的2段式下拉
 * 实际使用的时候请把测试用的 textview给移除掉,调用 setTestFalse();
 * 注意,该view中的子view 不能直接在布局文件中添加, 必须掉该view 的addview()方法,
 * @author mitenotc
 */
public class DrawerLayout extends LinearLayout{

	protected ScrollView sv;//滑动的,主要是球界面
//	private boolean isDraged;//用于标记是否是ll 向下滚动(不是scrollview的滚动)
	protected LinearLayout ll_drawer;//用于添加其他的view的 linearlayout,该linearlayout必须只有唯一的一个子view,就是 sv,该view的addview必须重写,添加到ll_drawer中
	private MyScroller scroller;
	private int middleY = 150;
	private int bottomY = 300;
	private int screenWidth;
	public DrawerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DrawerLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		View.inflate(getContext(), R.layout.f_drawer_layout, this);
		sv = (ScrollView) findViewById(R.id.sv);
		ll_drawer = (LinearLayout) findViewById(R.id.ll_drawer);
		scroller = new MyScroller(getContext());
		middleY = DensityUtil.dip2px(getContext(), 150);
		bottomY = DensityUtil.dip2px(getContext(), 300);
		screenWidth = MyApp.screenWidth;
	}
 
	public void setTestFalse(){
		ll_drawer.removeAllViews();
	}
	/**
	 * 设置scrollerview的背景颜色
	 * @param resid
	 */
	public void setSecondLayerBackground(int resid){
		sv.setBackgroundResource(resid);
	}
	
	private int downY;
	private int preY;//linearlayout 之前滚到到的y值

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			////System.out.println("ll on down");
			downY = y;
			preY = y;
//			if(getScrollY()<-middleY)
			boolean touchFlag = super.dispatchTouchEvent(ev);
			if(touchFlag){//该事件已经被该view及他的子类处理
				return true;
			}else {//该事件没有被该view 及其子类处理
				if(x < screenWidth/3 || x > screenWidth*2/3){//点击在 屏幕的 两侧的 1/3 边缘
					return true;//只有 down的时候return true 才能触发 activion_move
				}else {//点击在屏幕的中间1/3
					return false;
				}
			}
//			break;
		case MotionEvent.ACTION_MOVE:
			if((sv.getScrollY() == 0 && y-downY>5) || getScrollY()<0){//在scrollview 滚动到 y值为0,即顶部的时候依然往下滚动的情况下.ll要往下滚
				
				if(getScrollY()+preY-y<=-bottomY){//将要滑动到最下面以下时,不进行滑动
//					scrollTo(0, -bottomY);
//					if(preY - y>0){//往上滑
//						scrollBy(0, preY-y);
//					}
					return true;
				}
				
				//或者当ll 已经下滚的时候,要阻止sv的滚动,使用ll的滚动
				scrollBy(0, preY-y);
				preY = y;
				return true;
			}else if (getScrollY()>0) {//当ll滚动到
				scrollTo(0, 0);
				return true;
			}
			
//			if(sv.getScrollY() == 0 && y-downY>5){//在scrollview 滚动到 y值为0,即顶部的时候依然往下滚动的情况下.ll要往下滚
//				scrollBy(0, preY-y);
//				preY = y;
//			}
			break;
		case MotionEvent.ACTION_UP:
			int scrollY = getScrollY();
			if(scrollY>-middleY/2){//在中间停止线的上半部分之上
				scroller.startScroll(0, scrollY, 0, 0-scrollY,200);
				scrollTo(0, 0);
//				startScrollAnim(-scrollY, 0);
			}else if(scrollY>-middleY){//在中间停止线的下半部分
				scrollTo(0, -middleY);
				scroller.startScroll(0, scrollY, 0, -middleY-scrollY,200);
//				startScrollAnim(-middleY-scrollY, 0);
			}else if(scrollY>-(middleY+(bottomY-middleY)/2)){//在底部停止线的上半部分
				scrollTo(0, -middleY);
				scroller.startScroll(0, scrollY, 0, -middleY-scrollY,200);
//				startScrollAnim(-middleY-scrollY, 0);
			}else if(scrollY>-bottomY){//在中间停止线的下半部分
				scrollTo(0, -bottomY);
				scroller.startScroll(0, scrollY, 0, -bottomY-scrollY,200);
//				startScrollAnim(middleY+scrollY, 0);
			}
			
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	/**
	 * 复写移动过程的 computeScroll方法
	 */
	@Override
	public void computeScroll() {
		if(scroller.computeScrollOffset()){
			scrollTo(0, scroller.getCurrentY());
			invalidate();
		}
		super.computeScroll();
	}
	
	
	@Override
	public void addView(View child) {
		ll_drawer.addView(child);
	}
	@Override
	public void addView(View child, int index) {
		ll_drawer.addView(child, index);
	}
	@Override
	public void removeView(View view) {
		ll_drawer.removeView(view);
	}
	@Override
	public void removeAllViews() {
		ll_drawer.removeAllViews();
	}
	
	public ScrollView getSv() {
		return sv;
	}

	public void setSv(ScrollView sv) {
		this.sv = sv;
	}

	public LinearLayout getLl_drawer() {
		return ll_drawer;
	}

	public void setLl_drawer(LinearLayout ll_drawer) {
		this.ll_drawer = ll_drawer;
	}

	public int getMiddleY() {
		return middleY;
	}

	public void setMiddleY(int middleY) {
		this.middleY = middleY;
	}

	public int getBottomY() {
		return bottomY;
	}

	public void setBottomY(int bottomY) {
		this.bottomY = bottomY;
	}

	private void startScrollAnim(int startY,int endY) {
		TranslateAnimation ta;
		ta = new TranslateAnimation(0, 0, startY, endY);
		ta.setDuration(300);
		startAnimation(ta);
	}
	
	public void resetScrollView(){
		if(sv != null){
			sv.scrollTo(0, 0);
		}
	}
	
	public void resetScroll(){
		scrollTo(0, 0);
	}
}
