package com.mitenotc.ui.account;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MemoryManager;
import com.mitenotc.utils.AnimationController;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 启动界面 2014-3-31 11:29:35
 * 
 * @author ymx
 * 
 */
public class SplashFragment extends BaseFragment {

	private ImageView acc_splash;
	private ViewPager pager;
	private List<View> views;
	private PagerAdapter pagerAdapter;
	private Button experience;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_splash);
		hideTitle();
		initView();
		
//		MessageJson msg4 = new MessageJson();
//		msg4.put("A", "6");
//		submitData(1, 1302, msg4);
//		
//		MessageJson msg3 = new MessageJson();
//		msg3.put("A", "3");
//		submitData(3, 1302, msg3);
	}

	private void setSplashLisener() {
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				showViewPager();
			}
		});
	}
	private AlphaAnimation aa;
	private void initView() {
		acc_splash = (ImageView) findViewById(R.id.v_acc_splash);

		if(MyApp.splashbitmap != null)
		{
			BitmapDrawable bd = new BitmapDrawable(MyApp.splashbitmap);
			acc_splash.setBackgroundDrawable(bd);
			aa = new AlphaAnimation(1f, 1f);
			aa.setDuration(2000);
			acc_splash.startAnimation(aa);
			setSplashLisener();
		}else{
			showViewPager();
		}
	}
	
	private void showViewPager(){

		if ("".equals(SPUtil.getString(MyApp.res
				.getString(R.string.SPLASH)))) {
			SPUtil.putString(MyApp.res.getString(R.string.SPLASH),"HAS");
			createTCShortCut();// 生成快捷图标在手机桌面 ---TODO 小米的等某些机型可能只会有一个图标
			
			pager = (ViewPager) findViewById(R.id.ii_splash_viewpager);
			AnimationController.fadeIn(pager, 500, 500);
			views = new ArrayList<View>();
			View v1 = View.inflate(mActivity, R.layout.m_acc_splash_welcome1, null);
			views.add(v1);
			View v2 = View.inflate(mActivity, R.layout.m_acc_splash_welcome2, null);
			views.add(v2);
			View v3 = View.inflate(mActivity, R.layout.m_acc_splash_welcome3, null);
			experience = (Button) v3.findViewById(R.id.bt_acc_wel_experience);
			experience.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					closeSplashPage();
				}
			});
			views.add(v3);

			pagerAdapter = new MyViewPagerAdapter();
			pager.setAdapter(pagerAdapter);

			return;
		}
		closeSplashPage();
	}
	
	
	private void closeSplashPage(){
		finish();
//		MessageJson msg = new MessageJson();
//		submitData(0, 1355, msg);
	}
	
	/**
	 * 创建桌面快捷图标
	 * @param tag
	 */
	public void createTCShortCut(){
		Intent homeIntent = new Intent(mActivity,TCActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		homeIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
				intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, MyApp.res.getString(R.string.app_name));
//				intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, Intent.ShortcutIconResource.fromContext(MyApp.context,R.drawable.ic_launcher));
				intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(MyApp.context,R.drawable.ic_launcher));
				intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, homeIntent);
				intent.putExtra("duplicate", false);// 是否允许重复创建
				
		mActivity.sendBroadcast(intent);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		GloableParams.isLoading = false;
	}
	
	
/*	*//** 操作网络数据 *//*
	@Override
	protected void onMessageReceived(Message msg) {
		final MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1355:
			if ("0".equals(messageBean.getA())) {
				TCDialogs t = new TCDialogs(MyApp.activityList.get(0));
				t.setCancelable(false);
				t.popHadNewVersion(new OkClickedListener() {
					@Override
					public void okClicked() {
						
						if(!MemoryManager.externalMemoryAvailable()||MemoryManager.getAvailableExternalMemorySize() < 3 * 1024 * 1024){
							Toast.makeText(mActivity, "无SD卡或SD卡容量不足！", Toast.LENGTH_SHORT).show();
							return;
						}
						//这里不能用当前的context。因为创建dialog需要依附context
						DownLoadUtil dlu = new DownLoadUtil(MyApp.activityList.get(0));
						dlu.createDialog(messageBean.getD());
						dlu.createNotification();
						
						}
					}, messageBean.getE());
			}
			break;
		}
	}*/
	
	private class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = views.get(position);
			container.removeView(view);
		}
	}

	/** 当界面打开的时候 把资源文件下的db数据库文件拷贝到files目录下 */
}
