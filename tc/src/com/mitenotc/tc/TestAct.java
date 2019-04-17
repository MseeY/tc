package com.mitenotc.tc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class TestAct extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(-1,-1));
		LinearLayout ll_bg = (LinearLayout) View.inflate(this, R.layout.f_item_award_info_ll, null);
		LinearLayout ll_content = (LinearLayout) ll_bg.findViewById(R.id.award_info_content);
//		ll.addView(ll_bg);
		for (int i = 0; i < 3; i++) {
			ll_content.addView(View.inflate(this, R.layout.f_award_info_item_dice, null));
		}
		setContentView(ll_bg);
	}
}
