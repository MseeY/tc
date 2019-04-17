package com.mitenotc.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.account.MeFragment;
import com.mitenotc.ui.base.BaseFragment;

public class BodyFragment4 extends BaseFragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleNav(R.string.title_nav_user_center, 0, R.drawable.title_nav_menu);
//		start(ThirdActivity.class,TriggerFragment.class,null);
		replaceMiddle(MeFragment.class);
	}
	
	@Override
	protected void initData() {
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTitleCheckedChangedListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				////System.out.println("check changed");
			}
		});
	}
	
}
