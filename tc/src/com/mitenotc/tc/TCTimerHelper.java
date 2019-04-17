package com.mitenotc.tc;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;


public class TCTimerHelper {

	private TimerObserver observer;
	private long interval = 1000;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(observer != null){
				observer.onUpdate();
			}
		}
	};
	private Timer timer;
	private TimerTask task;
	class TimerTaskHelper extends TimerTask{
		@Override
		public void run() {
			handler.sendEmptyMessage(0);
		}
	}
	
	public TCTimerHelper(TimerObserver observer) {
		super();
		this.observer = observer;
	}
	public TCTimerHelper(TimerObserver observer, long interval) {
		super();
		this.observer = observer;
		this.interval = interval;
	}
	public interface TimerObserver{
		void onUpdate();
	}
	public void start(){
		timer = new Timer();
		task = new TimerTaskHelper();
		timer.schedule(task, 0,interval);
	}
	public void stop(){
		if(timer != null)
			timer.cancel();
		timer = null;
		task = null;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
}
