package com.mitenotc.ui.register;


import android.os.Bundle;

import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;

/**
 * 注册主页
 * 
 * 管理其它注册页面的Fragment
 * 
 * @author 朱万利
 *
 */
public class Registermain extends BaseFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fg_register_main);
		setTitleNav("注册泰彩彩票", R.drawable.title_nav_back, 0);
	}
	
}
