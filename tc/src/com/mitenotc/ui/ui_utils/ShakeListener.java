package com.mitenotc.ui.ui_utils;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.SPUtil;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public class ShakeListener implements SensorEventListener{
	
	private Vibrator vibrate;
	private SensorManager manager;
	private OnShakeListener onShakeListener;
	private long currTime;
	private boolean isRegisted;//安全性考虑,避免重复注册
	private boolean isEnable = true;//只有在 isEnable = true; 的时候才会回调 onShake;
	
	public OnShakeListener getOnShakeListener() {
		return onShakeListener;
	}

	public void setOnShakeListener(OnShakeListener onShakeListener) {
		this.onShakeListener = onShakeListener;
	}

	public ShakeListener(Context context) {
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
//		boolean flag = false;
		float[] values = event.values;
		if(Sensor.TYPE_ACCELEROMETER == event.sensor.getType()){////System.out.println("Math.abs(values[0]) ==== "+Math.abs(values[0])+" [1] = "+Math.abs(values[1])+" [2] = "+Math.abs(values[2]));
			/*String str = "";//注释掉的代码为检测 手机没有摇晃时 出现的 values的值大于15的情况
			for (int i = 0; i < values.length; i++) {
				str += values[i] + " :---: ";
				if(values[i]>15){
					flag = true;
				}
			}
			if(flag){
				////System.out.println(str);
				flag = false;
			}*/
			if(Math.pow(values[0],2) + Math.pow(values[1],2) + Math.pow(values[2],2)>Math.pow(13,2)){
				if(onShakeListener!=null && isEnable){
					onShakeListener.onShake();
					vibrate(100);
				}
			}
			
			/*if(Math.abs(values[0])>14.6||Math.abs(values[1])>14.6||Math.abs(values[2])>14.6){
				if(onShakeListener!=null && isEnable){
					onShakeListener.onShake();
					vibrate(100);
				}
			}*/
			
		}
	}
	
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	/**
	 * 开始摇晃监听
	 */
	public void start(){
		if(!isRegisted){
			manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
			isRegisted = true;
		}
	}
	
	/**
	 * 结束摇晃监听
	 */
	public void stop(){
		if(isRegisted){
			manager.unregisterListener(this);
			isRegisted = false;
		}
	}
	
	public interface OnShakeListener{
		/**
		 * 发生摇晃事件
		 */
		public void onShake();
	}
	
	/**
	 * 默认震动100毫秒
	 */
	public void vibrate(){
		vibrate(100);
	}
	public void vibrate(long time){
		if(SPUtil.getBoolean(MyApp.res.getString(R.string.SHAKE), true)){
			vibrate.vibrate(time);
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
