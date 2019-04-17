package com.mitenotc.ui.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import b.m;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.NetUtil;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.account.LoginFragment;
import com.mitenotc.ui.account.MeFragment;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.base.BaseActivity.onBaseActivityResult;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.XListView;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Fragment 基类, 封装了与Activity交换的方法,简化了Fragment的使用
 * 
 * 如果用到了 listview, 数据从网络端获取 可以复写initAdapter 和 getMessageBean 两个方法类简化书写
 * @author mitenotc
 */
public class BaseFragment extends Fragment{
	
	private Map<String, Boolean> isCMDUseable;//true 代表可以请求状态,false 为不可请求状态//key 为 arg1_arg2 也就是 mywhat_cmd//用于保证 同一个请求 不能在同一时间内多次发送(解决重复点击事件)
	protected Map<String, BaseFragment> fragmentCaches;
	protected  BaseActivity mActivity;
	
	protected View mView;
	protected LayoutInflater mInflater;
	
	protected List<MessageBean> mList = new ArrayList<MessageBean>(); 
	protected XListView mListView;
	protected BaseAdapter mAdapter;
	
	protected final static int MSG_ERROR = 1000;
	protected final static int MSG_OK = 0;
	protected final static int MSG_NULL = 1;
	protected static  int is_logining=0;
	
	protected boolean network_notice_required;
	protected boolean isFragmentCacheEnable;//标记是否使用 fragmentcache
	protected Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_ERROR:
				if(NetUtil.checkNetWork(mActivity) && network_notice_required){
					mActivity.showNewworkRefresh();
				}
				if(msg.obj!=null){
					MyToast.showToast(mActivity, msg.obj.toString());
				}
				errorResult(msg);
				break;
			case MSG_NULL:
				System.err.println("BaseFragment: handleMessage msg is MSG_NULL !");
				nullResult();
				break;
			case MSG_OK:
				if(network_notice_required){
					mActivity.hideNetworkRefresh();
				}
				MessageBean bean = (MessageBean) msg.obj;////System.out.println("a ---------------= "+bean.getA());
				if("10045".equals(bean.getA()))
				{
					// 防止loginFtragment 被多次启动 is_logining=0 会在loginFtragment 
					//在销毁的时候被重新赋值为0  此处解决了登录下导航消失的bug
					if(BaseFragment.is_logining==0)
					{
							BaseFragment.is_logining = 1;
							//清空用户SESSION  2014-10-28 修改了 
							//protocol 中的SND  和　ＵＳＲ获取方式  使得用户只要SND存在未过期  USR不为空 前台就视为已经登录 不在需要登录(isLogin为true) 
							SPUtil.putString(R.string.SND, "");
							startLoginForResult();
					}
					break;
				}
//				BaseFragment.is_logining = 0;
//				mAdapter = initAdapter();
//				if(mAdapter != null)
//					mListView.setAdapter(mAdapter);
				 onMessageReceived(msg);
				break;

			default:
				break;
			}
//			////System.out.println("iscmd---------------------- before");
			isCMDUseable.put(msg.arg1+"_"+msg.arg2, true);//把该cmd 重新置为true
//			////System.out.println("iscmd---------------------- after");
		}
		
	};
	
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	};
	
	
	/**
	 * 当返回的错误码不为"0"时需要执行的
	 */
	protected void errorResult(Message msg) {
	}
	/**
	 * 当返回的错误码不为"0"时并且数据为空需要执行的
	 */
	protected void nullResult() {
	}
	/**
	 * 调用 startActivityFroResult 开启登录
	 */
	public void startLoginForResult() 
	{
		
		Intent intent = new Intent(mActivity, ThirdActivity.class);
		intent.putExtra("fragmentName", LoginFragment.class.getName());
		Bundle bundle=new Bundle();
		bundle.putInt(MyApp.res.getString(R.string.cmd), 1107);// 支付 充值 发现 其实都使用一个cmd 标识返回
		intent.putExtras(bundle);
		if(isAdded())
		startActivityForResult(intent, 1002);//专门用1002 与接口相应的cmd 号来标记登录
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1002){//1002 与接口 cmd 相应
			if(!UserBean.getInstance().isLogin() && this.getClass() != MeFragment.class){
				finish();
			}else {
				onReLogin();
			}
		}
	}
	/**
	 * 在 服务端返回 11045 提示用户未登录时, 用户重新登录成功的时候 重新 发送请求
	 */
	protected void onReLogin() {
		
	}
	
	/**
	 * 用于ListView 的adapter 的设置,同时给 mAdapter 赋值
	 * @return
	 */
	protected BaseAdapter initAdapter() {
		return null;
	}
	
	/**
	 * handler 中正常获取到数据的情况, 回调获取到的数据,如果不进行界面的操作可以直接在 getMessageBean() 方法中获取数据,不同点: getMessageBean() 是 在子线程中的操作
	 * msg.arg1 = myWhat
	 * msg.arg2 = cmd;
	 * 该方法与initDate();三个参数的方法对应
	 * @param msg
	 */
	protected void onMessageReceived(Message msg){
		
	}
	
	/**
	 * 子线程中获取数据,主要这里不可以进行有关界面的操作, MessageException 异常必须直接往上抛出, 如果该方法返回空则或 弹出Toast 显示 "没有获取到有效数据" 
	 * @return
	 * @throws MessageException
	 */
	protected MessageBean getMessageBean() throws MessageException {
		return new MessageBean();
	}
	final private MessageBean getMessageBean(int cmd, MessageJson jsonMsg) throws MessageException {
		MessageBean msg = BaseEngine.getCMD(cmd, jsonMsg);
		return msg;
	}
	
	/**银行卡添加按钮的背景
	 * @param title
	 * @param left_icon
	 * @param right_icon 定制 按钮
	 * @param right_icon2 其它功能按钮
	 */
	public void setTitleNav1(String title,int left_icon,int right_icon,int right_icon2){
		mActivity.setTitleNav1(title, left_icon, right_icon, right_icon2);
	}
	 
	@Deprecated
	protected void initData() {
//		new Thread(){
//			@Override
//			public void run() {
//				super.run();
//				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//				Message msg = Message.obtain();
//				try {
//					msg.obj = getMessageBean(); 
//					if(msg.obj != null){
//						msg.what = MSG_OK;
//					}else{
//						msg.what = MSG_NULL;
//					}
//					
//				} catch (MessageException e) {
//					msg.what = MSG_ERROR;
//					msg.obj = e.getMessage();
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);
//			}
//		}.start();
	}
	/**
	 * 专门用于获取网络数据
	 * @param myWhat
	 * @param cmd
	 * @param jsonMsg
	 */
	public void submitData(final int myWhat, final int cmd, final MessageJson jsonMsg) {////System.out.println("submit --------- cmd ===== "+cmd);
		if(isCMDUseable == null){//安全考虑. 防止 iscmduseable 为空
			isCMDUseable = new HashMap<String, Boolean>();
		}
		if(isCMDUseable.get(myWhat+"_"+cmd) == null || isCMDUseable.get(myWhat+"_"+cmd)){//当 该cmd 没有请求过,或者请求已经处理完成 时 才能进行下次请求,否则 直接返回该方法,不对请求进行处理.
			isCMDUseable.put(myWhat+"_"+cmd, false);//标记该cmd 正在请求,出于不可请求状态
		}else {
//			Toast.makeText(mActivity, "请求已发送,请稍后", 0).show();
			return;
		}
		new Thread(){
			@Override
			public void run() {
				super.run();
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				Message msg = Message.obtain();
				msg.arg1 = myWhat;
				msg.arg2 = cmd;
				try {
//					if(cmd >= 1200){
//						return ;
//					}
					msg.obj = getMessageBean(cmd,jsonMsg); 
					if(msg.obj != null){
						msg.what = MSG_OK;
					}else
					{
						msg.what = MSG_NULL;
					}
					
				} catch (MessageException e) {
					msg.what = MSG_ERROR;
					msg.obj = e.getMessage();
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(isCMDUseable == null){
			isCMDUseable = new HashMap<String, Boolean>();
		}else {
			isCMDUseable.clear();
		}
		mActivity = (BaseActivity) getActivity();
		mActivity.setActivityResult(new onBaseActivityResult() {
			@Override
			public void onUpayResult(int requestCode, int resultCode, Intent data) {
				onUpayResultF(requestCode, resultCode,data);
			}
		});
		fragmentCaches = mActivity.getFragmentCaches();
		network_notice_required = false;
		isFragmentCacheEnable = false;
		setListener();
		initListView();
//		initData();
		
//		if(mList == null || mList.size() == 0){
//			initData();
//		}else {
//			mAdapter = initAdapter();
//			if(mAdapter != null)
//				mListView.setAdapter(mAdapter);
//		}
	}
	protected void onUpayResultF(int requestCode, int resultCode, Intent data) {
		
	}
	private void initListView() {
		mListView = new XListView(mActivity); 
		mListView.setLayoutParams(new LayoutParams(-1, -1));
		mListView.setVerticalScrollBarEnabled(false);//不显示滚动条
		mListView.setFadingEdgeLength(0);//删除上线的黑边
		mListView.setBackgroundColor(Color.TRANSPARENT);
		mListView.setCacheColorHint(0);
		mListView.setDrawingCacheEnabled(false);
		mListView.setPullLoadEnable(false);//禁用上拉刷新 
		mListView.setSelector(new ColorDrawable(0x00000000)); //设置item的点击事件的背景
	}

	/**
	 * 默认设置 填充父窗体 的 view
	 * @param layoutResId
	 */
	public void setContentView(int layoutResId){
		mView = View.inflate(mActivity, layoutResId, null);
		LayoutParams layoutParams = new LayoutParams(-1,-1);
		mView.setLayoutParams(layoutParams);
	}
	public void setContentView(View view){
		this.mView = view;
	}
	public void setContentView(View view, ViewGroup.LayoutParams layoutParams){
		this.mView = view;
		view.setLayoutParams(layoutParams);
	}
	public View findViewById(int id){
		return mView.findViewById(id);
	}
	/**
	 * 隐藏底部导航
	 */
	public void hideBottom(){
		mActivity.hideBottomNav();
	}
	public void hideTitle(){
		mActivity.hideTitleNav();
	}
	/**
	 * 显示底部导航
	 */
	public void showBottom(){
		mActivity.showBottomNav();
	}
	/**
	 * 得到当前fragment的view对象
	 * @return
	 */
	public View getContentView(){
		return mView;
	}
	/**
	 *  复写了 onCreateView, 为保证该方法有效, 必须在 onCreate 中调用 setFragmentView() 方法, 以保证 view 不为空. 否则子类 必须自己 重新复写该方法
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		return mView;
	}
	/**切换中间的部分的 Fragment
	 * @param clazz 目标 Fragment
	 */
	public void replaceMiddle(Class <? extends BaseFragment> clazz){
		mActivity.replaceMiddle(clazz);
	}
	/**
	 * 替换activity 中的一个fragment
	 * @param containerId
	 * @param clazz
	 */
	public void replace(int containerId,Class <? extends BaseFragment> clazz){
		mActivity.replace(containerId,clazz);
	}
	/**
	 * 跳转到目标Activity,并指定 中间容器的Fragment. 目标 Activity 是该Fragment所在的Activity处理的.这里不用处理
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseFragment> fragmentCls){
		mActivity.start(fragmentCls);
	}
	/**
	 * 跳转到目标Activity,并指定 中间容器的Fragment. 目标 Activity 是该Fragment所在的Activity处理的.这里不用处理
	 * 携带的数据存到bundle中
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseFragment> fragmentCls,Bundle datas){
		mActivity.start(fragmentCls,datas);
	}
	/**
	 * 跳转到目标Activity,并指定 中间容器的Fragment. 目标 Activity 是该Fragment所在的Activity处理的.这里需要处理
	 * 携带的数据存到bundle中
	 * @param fragmentCls
	 */
	public void start(Class<? extends BaseActivity> targetCls,Class<? extends BaseFragment> fragmentCls,Bundle datas){
//		////System.out.println("start activity in basefragment");
		mActivity.start(targetCls,fragmentCls,datas);
	}
	/**
	 * 得到传递过类的 bundle数据
	 * @return
	 */
	public Bundle getMyBundle(){
		return mActivity.getMyBundle();
	}
	
	public void setTitleText(String text){
		if(!AppUtil.isEmpty(text)){
			mActivity.setTitleText(text);
		}
	}
	
	/**
	 * 设置标题的checked 状态发生变化的监听,使用默认的 drawable_right 图片
	 * @param listener
	 */
	public void setTitleCheckedChangedListener(OnCheckedChangeListener listener){
		mActivity.setTitleCheckedChangedListener(listener);
	}
	/**
	 * 设置标题的checked 状态发生变化的监听
	 * @param listener
	 */
	public void setTitleCheckedChangedListener(OnCheckedChangeListener listener,int drawableRightId){
		mActivity.setTitleCheckedChangedListener(listener);
	}
	
	/**
	 * 每个fragment必须调用该方法给 title_nav_bar 设置相应的信息
	 * @param title
	 * @param left_icon
	 * @param right_icon
	 */
	public void setTitleNav(String title,int left_icon,int right_icon){
		mActivity.setTitleNav(title, left_icon, right_icon);
	}
	/**重载
	 * @param title
	 * @param left_icon
	 * @param right_icon 定制 按钮
	 * @param right_icon2 其它功能按钮
	 */
	public void setTitleNav(String title,int left_icon,int right_icon,int right_icon2){
		mActivity.setTitleNav( title, left_icon, right_icon, right_icon2);
	}
	/**
	 * 每个fragment必须调用该方法给 title_nav_bar 设置相应的信息
	 * @param title
	 * @param left_icon
	 * @param right_icon
	 */
	public void setTitleNav(int titleId,int left_icon,int right_icon){
		mActivity.setTitleNav(titleId, left_icon, right_icon);
	}
	/**
	 * 设置title 的checkbox 为默认的状态
	 */
	public void resetTitleState(){
		mActivity.resetTitleState();
	}  
	 
	private void setListener(){
		if(mActivity.getTitle_nav_tv() == null){return;}
		mActivity.getTitle_nav_tv().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTitleClicked();
			} 
		});
		
		mActivity.getTitle_nav_iv_left().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLeftIconClicked();
			}
		});
		
		mActivity.getTitle_nav_iv_right().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightIconClicked();
			}
		});
		mActivity.getTitle_nav_iv_right2().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightIconClicked2();
			}
		});
		mActivity.getTitle_nav_iv_right3().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightIconClicked3();
			}
		});
	}
	public void hideRightIcon2(){
		mActivity.hideRightIcon2();
	}
	public void hideRightIcon(){
		mActivity.hideRightIcon();
	}
	public void showRightIcon2(){
		mActivity.showRightIcon2();
	}
	public void showRightIcon(){
		mActivity.showRightIcon();
	}
	
	/**
	 * 头部导航的 中间文字点击事件
	 */
	protected void onTitleClicked(){
		
	}
	/**
	 * 头部导航 左边图标的点击事件
	 */
	protected void onLeftIconClicked() {
		finish();
	}
	/**
	 * 头部导航 右边图标的点击事件
	 */
	protected void onRightIconClicked() {
	}
	/**
	 * 遗漏切换 图标的点击事件
	 */
	protected void onRightIconClicked2() {
	}
	/**
	 * 竞彩过滤 图标的点击事件
	 */
	protected void onRightIconClicked3() {
	}
	
	public class BaseListAdapter extends BaseAdapter{
		 
		@Override
		public int getCount() {////System.out.println("mlist = "+mList);
			if(mList == null){
				return 0;
			}
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return convertView;
		}
	}
	
	/**
	 * 将秒时间转换成日时分格式
	 * 
	 * @param lasttime
	 * @return
	 */
	public static String getLastTime(String lasttime) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNumericSpace(lasttime)) {
			int time = Integer.parseInt(lasttime);
			int day = time / (24 * 60 * 60);
			if (day > 0) {//以天为开始时间时 格式为 m天n时
				result.append(day).append("天");
				time = time - day * 24 * 60 * 60;
				int hour = time / 3600;
				result.append(hour).append("时");
				return result.toString();
			}
			int hour = time / 3600;
			if (hour > 0) {//以小时为开始时间时 格式为m时n分
				result.append(hour).append("时");
				time = time - hour * 60 * 60;
				int minute = time / 60;
//				result.append(minute).append("分");
//				result.append(minute).append(":");
				return result.toString();
			}
			int minute = time / 60;
			if(minute > 0){//时间以分为开始时 格式为 m分n秒,当不足一分钟时 格式为 m秒
//				result.append(minute).append("分");
				result.append(minute).append(":");
				time = time - minute*60;
			}
			result.append(time);
//			result.append(time).append("秒");
		}
		return result.toString();
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
//		mActivity.removeTitleCheckedChangedListener();
		MobclickAgent.onPageEnd("BaseFragment"); //统计页面

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mActivity.removeTitleCheckedChangedListener();
		mActivity.hideNetworkRefresh();
		mAdapter = null;
	}
	public void setMyBackPressedListener(MyBackPressedListener myBackPressedListener){
		mActivity.setMyBackPressedListener(myBackPressedListener);
	}
	/**
	 * 把当前的fragment所在的activity finish 掉
	 */
	public void finish(){
		if(mActivity != null){
			mActivity.finish();//监听返回的动画
			mActivity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		}
	}
	/**
	 * 获得 fragment,调用 BaseActivity的方法
	 * @param clazz
	 * @return
	 */
	public BaseFragment getFragment(Class<? extends BaseFragment> clazz){
		return mActivity.getFragment(clazz);
	}
	/**
	 * 设置控件的可见性.
	 * 判断控件当前的可见性和要设置的可见性是否相同. 仅当可见性不同时才设置
	 * @param view
	 * @param visibility
	 */
	public void setVisibility(View view,int visibility){
		if(view.getVisibility() != visibility){
			view.setVisibility(visibility);
		}
	}
	/**
	 * 隐藏控件,设置控件为 View.Gone
	 */
	public void hide(View view){
		setVisibility(view, View.GONE);
	}
	/**
	 * 显示控件
	 * @param view
	 */
	public void show(View view){
		setVisibility(view, View.VISIBLE);
	}
	
	/**
	 * 显示相关的控件
	 * @param views
	 */
	public void showViews(View...views){
		for (View view : views) {
			show(view);
		}
	}
	/**
	 * 隐藏相关控件
	 * @param views
	 */
	public void hideViews(View... views){
		for (View view : views) {
			hide(view);
		}
	}
	
	public Intent getIntent(){
		return mActivity.getIntent();
	}
	/**
	 * 专门用于需要添加到定制的 fragment 设置 title_nav
	 * @param item
	 * @param text
	 */
	public void setTitleNav(int id, String text,int iconLeftRes,int iconRightRes){////System.out.println("id = "+id);////System.out.println("BodyFragment1.isTagSaved(id) = "+BodyFragment1.isTagSaved(id));
		if(BodyFragment1.isTagSaved(id)){
			setTitleNav(text, iconLeftRes,0);
		}else {
			setTitleNav(text, iconLeftRes, iconRightRes);
		}
	}
	/**
	 * 专门用于需要添加到定制的 fragment 设置 title_nav
	 * @param item
	 * @param text
	 */
	public void setTitleNav(int id, int strRes){
		int iconLeftRes = R.drawable.title_nav_back;
		int iconRightRes = R.drawable.title_nav_menu;
		setTitleNav(id,strRes,iconLeftRes,iconRightRes);
	}
	/**
	 * 专门用于需要添加到定制的 fragment 设置 title_nav
	 * @param item
	 * @param text
	 */
	public void setTitleNav(int id, int strRes,int iconLeftRes,int iconRightRes){
		if(BodyFragment1.isTagSaved(id)){
			setTitleNav(strRes, iconLeftRes,0);
		}else {
			setTitleNav(strRes, iconLeftRes, iconRightRes);
		}
	}
	/**顶部主题颜色修改
	 * @param resid
	 * @param resSelcetid
	 * @param selcetid
	 */
	protected void replaceTitleBackgroundResource(int resid,int resSelcetid){
		mActivity.replaceTitleBackgroundResource(resid,resSelcetid);
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("BaseFragment"); //统计页面
	}
	
	/**
	 * 网络 重新连接时 用户点击刷新
	 */
	public void onNetworkRefresh() {
		
	}
	public boolean isFragmentCacheEnable() {
		return isFragmentCacheEnable;
	}
	public void setFragmentCacheEnable(boolean isFragmentCacheEnable) {
		this.isFragmentCacheEnable = isFragmentCacheEnable;
	}
}