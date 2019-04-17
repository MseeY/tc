package com.mitenotc.tc;

import android.os.Bundle;
import android.view.WindowManager;

import cn.jpush.android.api.JPushInterface;

import com.mitenotc.ui.base.BaseActivity;

/**
 * 第三层 Activity
 * @author mitenotc
 *
 */
public class ThirdActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		
//		----------------------------------万利 添加
//		 方法一：在你的activity中的oncreate中setContentView之前写上这个代码getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		 方法二：在项目的AndroidManifest.xml文件中界面对应的<activity>里加入android:windowSoftInputMode="stateVisible|adjustResize"，这样会让屏幕整体上移。如果加上的是
//		                 android:windowSoftInputMode="adjustPan"这样键盘就会覆盖屏幕。
//		 方法三：把顶级的layout替换成ScrollView，或者说在顶级的Layout上面再加一层ScrollView的封装。这样就会把软键盘和输入框一起滚动了，软键盘会一直处于底部。
//		 
//		注：方法一和二应该是同一种方法，不过一个是在代码里实现一个是在xml文件里实现。可能会应为1.5和之前的版本不支持该方法。但是方法三是肯定没有问题的。
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//为了智能追号页面涉及过多的输入框
		super.onCreate(arg0);
	}

}
