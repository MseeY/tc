package com.mitenotc.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 淡入淡出的效果实现
 * 
 * @author Administrator
 * 
 */
public class FadeAnimUtil {
	
	private static Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			View view=(View) msg.obj;
			// 删除第一个界面
			// 获取到当前执行动画的View 的老子ViewGroup
			ViewGroup parent = (ViewGroup) view.getParent();
			// ViewParent与ViewGroup的关系
			// 删除：removeView(view)
			parent.removeView(view);
			// 2.3的模拟器运行会报异常
			// 4.0无此异常
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 淡出
	 * 
	 * @param view
	 *            ：执行淡出的界面
	 * @param duration
	 *            ：淡出需要用到的时间
	 */
	public static void fadeOut(final View view, long duration) {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(duration);
		// 需要让View 停留在动画的最终的执行效果上
		// animation.setFillAfter(true);
		view.startAnimation(animation);

		// 必须：在第一个界面指定完动画的时候，将第一个界面清除
		// 监听：动画执行的监听
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// // 删除第一个界面
				// // 获取到当前执行动画的View 的老子ViewGroup
				// ViewGroup parent = (ViewGroup) view.getParent();
				// // ViewParent与ViewGroup的关系
				// // 删除：removeView(view)
				// parent.removeView(view);
				// // 2.3的模拟器运行会报异常
				// // 4.0无此异常
				
				Message msg=Message.obtain();
				msg.obj=view;
				handler.sendMessage(msg);
			}
		});

	}

	/**
	 * 淡入
	 * 
	 * @param view
	 *            ：执行淡出的界面
	 * @param duration
	 *            ：淡入需要用到的时间
	 * @param delay
	 *            ：延时多久执行（淡出界面执行的时间）
	 */
	public static void fadeIn(View view, long duration, long delay) {
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		// 设置延时时间
		animation.setStartOffset(delay);
		animation.setDuration(duration);

		view.startAnimation(animation);

	}
}
