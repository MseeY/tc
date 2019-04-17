package com.mitenotc.ui.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.MyReceiver;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.ReceiverDialog;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.DensityUtil;
import com.umeng.analytics.MobclickAgent;
/**
 * Activity 基类,继承该类似必须指定 目标Activity, targetCls
 * @author mitenotc
 */
public class BaseActivity extends FragmentActivity{
 
	protected FragmentManager fm;
//	protected Map<String, BaseFragment> fragmentCaches;
	public static Map<String, BaseFragment> fragmentCaches = new HashMap<String, BaseFragment>();
//	public static BottomFragment bottomFragment;//底部导航
	protected BaseFragment currentFragment;//当前中间容器的 fragment
	protected BaseFragment bottomFragment;//底部导航容器的 fragment
	
	protected TextView title_nav_refresh_network_tv;
	protected CheckBox title_nav_tv;
	protected ImageView title_nav_iv_left;
	protected ImageView title_nav_iv_right;// 定制按钮
	protected CheckBox title_nav_iv_right2;// 快三遗漏切换按钮
	protected CheckBox title_nav_iv_right3;// 竞彩过滤对阵按钮
	protected LinearLayout title_nav_ll;
	protected RelativeLayout title_layout_bg;
	protected Class<? extends BaseActivity> targetCls  = ThirdActivity.class;
	TCDialogs dialog =null;
	ReceiverDialog mDialog=null;
	private static SQLiteDatabase db;
	private Handler mTcHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				createDialog();
				break;
			}
		}
	};
	private void createDialog(){
		if(db==null){
			db= MyApp.datacache.getWritableDatabase();
		}
	    String[] cloumsStr={"B","info"}; 
        Cursor result=db.query("receiver_info", cloumsStr, null,null,null,null,null);
        if(result.getColumnCount() > 0){
        	mDialog=new ReceiverDialog(this);
            mDialog.showReceiverDialog();
        }else{
        	db.execSQL("update sqlite_sequence set seq=0 where name='receiver_info';");// 自增id归零
        }
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);////System.out.println("base activity oncreate");
		MyApp.activityList.add(this);
//		fragmentCaches = MyApp.fragmentCaches;
		setContentView(R.layout.activity_main);
		MobclickAgent.openActivityDurationTrack(false);// 友盟统计
		initView();
		setListener();
		initMiddle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApp.activityList.remove(this);
	}
	/**
	 * 当从一个Activity 中跳转到该Activity时,判断 intent 中是否含有 要加载的中间容器的 类名,如果有,就加载到中间容器中
	 */
	private void initMiddle() {
		Intent intent = getIntent();
		String fragmentName = intent.getStringExtra("fragmentName");
		if(!AppUtil.isEmpty(fragmentName)){//判断 fragment 是否为空
			try {
				Class<? extends BaseFragment> fragmentCls = (Class<? extends BaseFragment>) Class.forName(fragmentName);
				replaceMiddle(fragmentCls);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}else {
			initDefaultMiddle();
		}
	}
	/**
	 * Activity 被创建时, 或者 intent.getStringExtra("fragmentName") 为空时, 默认中间容器的创建
	 */
	protected void initDefaultMiddle() {
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}

	/**
	 * activity的跳转,带目标中间容器的 Fragment 过去
	 * @param cls
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseActivity> targetCls,Class<? extends BaseFragment> fragmentCls){
		Intent intent = new Intent(this, targetCls);
		intent.putExtra("fragmentName", fragmentCls.getName());
		startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}
	/**
	 * 跳转到目标 Activity
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseFragment> fragmentCls){
		if(targetCls!=null && fragmentCls!=null){
		start(targetCls,fragmentCls);
		this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
		}else {
			throw new RuntimeException("target Activity or target Fragment is null");
		}
	}
	
	/**
	 * activity的跳转,带目标中间容器的 Fragment 过去,携带bundle数据
	 * @param cls
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseActivity> targetCls,Class<? extends BaseFragment> fragmentCls,Bundle datas){
		Intent intent = new Intent(this, targetCls);
		intent.putExtra("fragmentName", fragmentCls.getName());
		if(datas != null)
			intent.putExtras(datas);
		startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}

	/**
	 * 跳转到目标 Activity,携带的数据存在bundle中
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseFragment> fragmentCls,Bundle datas){
		if(targetCls!=null && fragmentCls!=null){
			start(targetCls,fragmentCls,datas);
			this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
		}else {
			throw new RuntimeException("target Activity or target Fragment is null");
		}
	} 
	/**
	 * 拿到intent中的 bundle数据
	 * @return
	 */
	public Bundle getMyBundle(){
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			extras.remove("fragmentName");
			if(extras.keySet().size()>0)
				return extras;
		}
			return null;
	}
	
	private void initView() {
		fm = getSupportFragmentManager();
		
		title_nav_refresh_network_tv = (TextView) findViewById(R.id.title_nav_refresh_network_tv);
		title_nav_tv = (CheckBox) findViewById(R.id.title_nav_tv);
		title_nav_iv_left = (ImageView) findViewById(R.id.title_nav_iv_left);
		title_nav_iv_right = (ImageView) findViewById(R.id.title_nav_iv_right);
		title_nav_iv_right2 = (CheckBox) findViewById(R.id.title_nav_iv_right2);
		title_nav_iv_right3 = (CheckBox) findViewById(R.id.title_nav_iv_right3);
		title_nav_ll = (LinearLayout) findViewById(R.id.title_nav_ll);
		title_layout_bg = (RelativeLayout) findViewById(R.id.title_layout_bg);
	}
	/**
	 * 替换容器的Fragment,并把 Fragment 对象存入到 FragmentCache 中,Fragment 的类名存入 currentFragment 中
	 * @param containerId
	 * @param clazz
	 */
	public void replace(int containerId, Class<? extends BaseFragment> clazz){
		currentFragment = getFragment(clazz);//当前中间目标Fragment的simpleName
		if(fm == null){// ---努比亚手机调节字体大小之后应用会报此处NullpointerException
			fm = getSupportFragmentManager();
		}
		fm.beginTransaction().replace(containerId, currentFragment).commitAllowingStateLoss();//原本使用的是 commit,使用该方法是为了解决 哦你saveinstance方法 和 commit 方法之间的冲突
	}
	/**
	 * 顶部主题颜色修改
	 * @param resid
	 * @param resSelectid
	 * @param selcetid
	 */
	protected void replaceTitleBackgroundResource(int resid,int resSelectid){
		if(title_layout_bg!=null){
			title_layout_bg.setBackgroundResource(resid);
		}
		if(title_nav_tv!=null){
			title_nav_tv.setBackgroundResource(resSelectid);
		}
		if(title_nav_iv_right!=null){
			title_nav_iv_right.setBackgroundResource(resSelectid);
		}
	}
	
	/**
	 * 该方法设置了添加银行卡时添加按钮的背景
	 * @param title
	 * @param left_icon
	 * @param right_icon
	 * @param right_incon
	 */
	public void setTitleNav1(String title,int left_icon,int right_icon,int right_incon){
		setTitleNav(title,left_icon,right_icon);
		if(right_incon == 0){
			return;
		}
		if(title_nav_iv_right!=null){
			title_nav_iv_right.setBackgroundResource(right_incon);
		}
	}
	
	
	public   void replaceBottom(BaseFragment fragment){
		bottomFragment = fragment;
		if(fm == null){// ---努比亚手机调节字体大小之后应用会报此处NullpointerException  8-19 修改
			fm = getSupportFragmentManager();
		}
		fm.beginTransaction().replace(R.id.activity_main_bottom_nav, fragment).commit();
	}
	/**
	 * 替换该activity 中间容器的 Fragment
	 * @param key 当前目标Fragment的simpleName
	 * @param clazz 当前目标Fragment
	 */
	public void replaceMiddle(Class<? extends BaseFragment> clazz){
		replace(R.id.activity_main_middle, clazz);
	}
	
	public Map<String, BaseFragment> getFragmentCaches() {
		return fragmentCaches;
	}

	public void setFragmentCaches(Map<String, BaseFragment> fragmentCaches) {
		this.fragmentCaches = fragmentCaches;
	}
	
	/**
	 * 设置标题文字
	 * @param text
	 */
	public void setTitleText(String text){
		if(!AppUtil.isEmpty(text)){
			title_nav_tv.setText(text);
		}
	}
	/**
	 * 设置title 的checkbox 为默认的状态
	 */
	public void resetTitleState(){
		title_nav_tv.setChecked(false);
	}
	/**
	 * 头部的标题字符串
	 * @return
	 */
	public String getTitleText(){
		return title_nav_tv.getText().toString();
	}
	
	/**
	 * 设置头部导航的 图片 和 文字
	 * @param title
	 * @param left_icon
	 * @param right_icon
	 */
	public void setTitleNav(String title,int left_icon,int right_icon){
		if(title_nav_tv == null){
			return;
		}
		title_nav_tv.setText(title); 
		if(title == null && left_icon == 0 && right_icon == 0){
			title_nav_ll.setVisibility(View.GONE);
		}
//		title_nav_iv_left.setImageResource(left_icon);
//		title_nav_iv_right.setImageResource(right_icon);
		setImage(title_nav_iv_left, left_icon);
		setImage(title_nav_iv_right, right_icon);
	}
	/**
	 * 设置头部导航的 图片 和 文字
	 * @param title
	 * @param left_icon
	 * @param right_icon 定制按钮
	 * @param right_icon2 其它功能按钮
	 */
	public void setTitleNav(String title,int left_icon,int right_icon,int right_icon2){
		if(title_nav_tv == null){
			return;
		}
		title_nav_tv.setText(title); 
		if(title == null && left_icon == 0 && right_icon == 0){
			title_nav_ll.setVisibility(View.GONE);
		}
//		title_nav_iv_left.setImageResource(left_icon);
//		title_nav_iv_right.setImageResource(right_icon);
		setImage(title_nav_iv_left, left_icon);
		setImage(title_nav_iv_right, right_icon);
		setImage(title_nav_iv_right2, right_icon2);
	}
	public void setTitleNav(int titleId,int left_icon,int right_icon){
		if(title_nav_tv == null){
			return;
		}
		title_nav_tv.setText(titleId); 
		//如果 所有的资源都为空,则设置头部导航不可见
		if(titleId ==0 && left_icon ==0 && right_icon == 0){
			title_nav_ll.setVisibility(View.GONE);
		}
		
//		title_nav_iv_left.setImageResource(left_icon);
//		title_nav_iv_right.setImageResource(right_icon);
		setImage(title_nav_iv_left, left_icon);
		setImage(title_nav_iv_right, right_icon);
	}
	
	//设置图片,如果图片资源为空,则 控件 不可见
	public void setImage(ImageView iv,int resId){
		if(resId == 0){
			iv.setVisibility(View.GONE);
		}else {
			iv.setVisibility(View.VISIBLE);
			iv.setImageResource(resId);
		}
	}
	//设置图片,如果图片资源为空,则 控件 不可见
	public void setImage(CheckBox ch,int resId){
		if(resId == 0){
			ch.setVisibility(View.GONE);
		}else {
			ch.setVisibility(View.VISIBLE);
			Drawable  mDrawable=MyApp.res.getDrawable(resId);
			mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
			ch.setCompoundDrawables(mDrawable, null, null, null);
//			ch.setImageResource(resId);
		}
	}
	
	private void setListener() {
		title_nav_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTitleClicked();
			}
		});
		
		title_nav_iv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLeftIconClicked();
//				MyApp.saveProbeMsg("顶部按钮事件", "左边返回-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			}
		});
		
		title_nav_iv_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightIconClicked();
//				MyApp.saveProbeMsg("顶部按钮事件", "右边定制-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			}
		});
		title_nav_iv_right2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightIconClicked2();
//				MyApp.saveProbeMsg("顶部按钮事件", "右边遗漏-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			}
		});
		title_nav_iv_right3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightIconClicked3();
//				MyApp.saveProbeMsg("顶部按钮事件", "右边遗漏-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			}
		});
		title_nav_refresh_network_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onNetworkRefresh();
			}
		});
	}
	/**
	 * 网络 重新连接时 用户点击刷新
	 */
	public void onNetworkRefresh() {
		currentFragment.onNetworkRefresh();
	}
	public void hideNetworkRefresh(){
		title_nav_refresh_network_tv.setVisibility(View.GONE);
	}
	public void showNewworkRefresh(){
		title_nav_refresh_network_tv.setVisibility(View.VISIBLE);
	}
	/**
	 * 头部导航的 中间文字点击事件
	 */
	public void onTitleClicked(){
		
	}
	/**
	 * 头部导航 左边图标的点击事件
	 */
	private void onLeftIconClicked() {
		
	}
	/**
	 * 定制图标 的点击事件
	 */
	private void onRightIconClicked() {
		
	}
	/**
	 * 遗漏切换 图标的点击事件
	 */
	private void onRightIconClicked2() {
		
	}
	/**
	 * 竞彩过滤对阵 图标的点击事件
	 */
	private void onRightIconClicked3() {
		
	}
	public void hideRightIcon2(){
		if(title_nav_iv_right2 != null)
		title_nav_iv_right2.setVisibility(View.GONE);
	}
	public void showRightIcon2(){
		if(title_nav_iv_right2!=null)
			title_nav_iv_right2.setVisibility(View.VISIBLE);
	}
	public void hideRightIcon3(){
		if(title_nav_iv_right3 != null)
			title_nav_iv_right3.setVisibility(View.GONE);
	}
	public void showRightIcon3(){
		if(title_nav_iv_right3!=null)
			title_nav_iv_right3.setVisibility(View.VISIBLE);
	}
	public void hideRightIcon(){
		if(title_nav_iv_right != null)
			title_nav_iv_right.setVisibility(View.GONE);
	}
//	 万利添加 6-16
	public void showRightIcon(){
		if(title_nav_iv_right!=null)
		title_nav_iv_right.setVisibility(View.VISIBLE);
	}
	/**
	 * 设置标题的checked 状态发生变化的监听
	 * @param listener
	 */
	public void setTitleCheckedChangedListener(OnCheckedChangeListener listener){
		setTitleCheckedChangedListener(listener, R.drawable.title_nav_cb_drawable_right);
	}
	/**
	 * 设置标题 checked 状态 变化的监听,并动态的设置 selector图片
	 * @param listener
	 * @param drawableRightId
	 */
	public void setTitleCheckedChangedListener(OnCheckedChangeListener listener,int drawableRightId){
		if(listener != null){
			title_nav_tv.setEnabled(true);
			Drawable drawable_right = getResources().getDrawable(drawableRightId);
			drawable_right.setBounds(0, 0, drawable_right.getMinimumWidth(), drawable_right.getMinimumHeight());
			title_nav_tv.setCompoundDrawables(null, null, drawable_right, null);
			title_nav_tv.setOnCheckedChangeListener(listener);
		}
	}
	
	public void removeTitleCheckedChangedListener(){
		if(title_nav_tv.isChecked()){
			title_nav_tv.setChecked(false);
		}
		title_nav_tv.setEnabled(false);
		title_nav_tv.setCompoundDrawables(null, null, null, null);
	}

	public TextView getTitle_nav_tv() {
		return title_nav_tv;
	}

	public ImageView getTitle_nav_iv_left() {
		return title_nav_iv_left;
	}

	public ImageView getTitle_nav_iv_right() {
		return title_nav_iv_right;
	}
	public CheckBox getTitle_nav_iv_right2() {
		return title_nav_iv_right2;
	}
	public CheckBox getTitle_nav_iv_right3() {
		return title_nav_iv_right3;
	}
	
	public BaseFragment getFragment(Class <? extends BaseFragment> clazz){
		String key = clazz.getName();
		BaseFragment fragment = fragmentCaches.get(key);
		if(null == fragment || !fragment.isFragmentCacheEnable()){
			try {
				fragment = (BaseFragment) clazz.newInstance();
				fragmentCaches.put(key, fragment);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fragment;
	}
	public BaseFragment getCurrentFragment() {
		return currentFragment;
	}
	public BaseFragment getBottomFragment() {
		return bottomFragment;
	}
	/**
	 * 隐藏底部导航
	 */
	public void hideBottomNav(){
		if(bottomFragment!=null && bottomFragment.mView.getVisibility() == View.VISIBLE)
			bottomFragment.mView.setVisibility(View.GONE);
	}
	/**
	 * 显示底部导航
	 */
	public void showBottomNav(){
		//System.out.println("tc------3---------"+bottomFragment.mView.getVisibility() +"=??="+View.VISIBLE);
		//System.out.println("tc------4---------"+bottomFragment);
		if(bottomFragment!=null && bottomFragment.mView.getVisibility() != View.VISIBLE)
			bottomFragment.mView.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		if(myBackPressedListener != null ){
			myBackPressedListener.onMyBackPressed();
		}else{
			super.onBackPressed();
			//监听返回键的动画
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		}
	}
	
	protected MyBackPressedListener myBackPressedListener;

	public interface MyBackPressedListener{
		/**
		 * @return 是否关闭当前Activity
		 */
		void onMyBackPressed();
	}
	public MyBackPressedListener getMyBackPressedListener() {
		return myBackPressedListener;
	}
	public void setMyBackPressedListener(MyBackPressedListener myBackPressedListener) {
		this.myBackPressedListener = myBackPressedListener;
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		currentFragment.onActivityResult(requestCode, resultCode, data);
//	}
	/**
	 * 隐藏头部
	 */
	public void hideTitleNav(){
		title_nav_ll.setVisibility(View.GONE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		title_nav_iv_right.setVisibility(View.GONE);
		title_nav_iv_right2.setVisibility(View.GONE);
		title_nav_iv_right2.setChecked(false);
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
//		mTcHandler.sendEmptyMessage(1);// 推送消息
	}
	//银联品质,支持高额支付.
		//安全快捷
		
		/**
		 * 在Activity中的接口下放到了Fragment的子类中，实现在BaseFragment子类中回调 U付返回的结果
		 * @author ymx
		 *
		 */
		
		interface onBaseActivityResult{
			void onUpayResult(int requestCode, int resultCode, Intent data);
		}
		
		private onBaseActivityResult activityResult; 


		public void setActivityResult(onBaseActivityResult activityResult) {
			this.activityResult = activityResult;
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			activityResult.onUpayResult(requestCode, resultCode, data);
		}

}

