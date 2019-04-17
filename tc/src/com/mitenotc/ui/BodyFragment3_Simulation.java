package com.mitenotc.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.play.PL122;
import com.mitenotc.ui.play.PL130;

public class BodyFragment3_Simulation extends BaseFragment{

	private ImageView simulation_bg_iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleNav("模拟投注", 0, 0);
		isFragmentCacheEnable = true;
		network_notice_required = false;
		handler.sendEmptyMessage(1000);
		setContentView(R.layout.fragment_simulation_ll);
//		start(ThirdActivity.class,TriggerFragment.class,null);
//		simulation_bg_iv = (ImageView) findViewById(R.id.simulation_bg_iv);
//		
//		simulation_bg_iv.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(ConstantValue.LOTTERY_TYPE){
//					start(PL130.class);//体彩
//				}else{
//					start(PL122.class);//泰彩
//				}
//			}
//		});
		/*simulation_bg_iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					float y = event.getY();
					int height = mActivity.getWindowManager().getDefaultDisplay().getHeight();
					if(y < height/2){
						start(MGridView_test.class);
					}else {
						start(PL118.class);
					}
				}
				return true;
			}
		});*/
	}
	
//	@Override
//	public void onStart() {
//		super.onStart();
//		setTitleText("模拟投注");
//	}
}
