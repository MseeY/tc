package com.mitenotc.ui.account.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyAutoCycleRollingListView extends ListView {
	private int smoothBy = 1;
	private Timer timer;
	private RollTimerTask task;
	private boolean isScrolling = false;

	private boolean isCanSroll = false;

	private long delay = 100;
	private long period = 100;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			smoothScrollBy(smoothBy, 0);
		};
	};

	public MyAutoCycleRollingListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyAutoCycleRollingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyAutoCycleRollingListView(Context context) {
		super(context);
	}

	public void startRoll() {
		if (task != null) {
			task.cancel();
			task = null;
		} 

		if (timer != null) {
			timer.cancel();
			timer =null;
		}
		
		timer = new Timer();
		task = new RollTimerTask();
		timer.schedule(task, delay, period);
		isScrolling = true;
	}

	public void pauseRoll() {
		// TODO Auto-generated method stub
		isCanSroll = false;
		isScrolling = false;
	}

	public void restartRoll() {
		isCanSroll = true;
		isScrolling = true;
	}

	public void stopRoll() {

	
			if (task != null) {
				task.cancel();
				task = null;
			}
			if (timer != null) {
				timer.cancel();
				timer =null;
				
			}

			isScrolling = false;
		

	}

	class RollTimerTask extends TimerTask {
		@Override
		public void run() {
			if (isCanSroll) {
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
			}

		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isScrolling() {
		return isScrolling;
	}

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		if (adapter != null) {
			isCanSroll = true;
		}
		super.setAdapter(adapter);
	}
}
