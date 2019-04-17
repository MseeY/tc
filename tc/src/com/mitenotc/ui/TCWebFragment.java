package com.mitenotc.ui;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.Des2;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.ui_utils.MWebView;
import com.mitenotc.utils.AppUtil;

public class TCWebFragment extends BaseFragment {

	private MWebView webview; 
	private ProgressBar progressBar; 
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTitleNav(R.string.title_nav_user_center, 0, R.drawable.title_nav_menu);
		//replaceMiddle(MeFragment.class);
		String title = getMyBundle().getString("title");
		if(AppUtil.isEmpty(title)){
			
//			setTitleText(this.getResources().getString(R.string.app_name));
			setTitleNav(getString(R.string.app_name), R.drawable.title_nav_back, 0);//设置手机返回键  监听
		}else {
//			setTitleText(title);
			setTitleNav(title, R.drawable.title_nav_back, 0);//设置手机返回键  监听
		}
		//CookieManager
		View mView=View.inflate(mActivity, R.layout.tcweb, null);
//		webview = new MWebView(mActivity);
		webview=(MWebView) mView.findViewById(R.id.tc_web_id);
		progressBar=(ProgressBar) mView.findViewById(R.id.progressBar);
		progressBar.setMax(100);
		progressBar.setProgress(0);
		progressBar.setIndeterminate(true);//指有明确进度任务
		progressBar.setSoundEffectsEnabled(false);

		webview.getSettings().setDomStorageEnabled(true);   
		webview.getSettings().setAppCacheMaxSize(1024*1024*8);  
		 String appCachePath = mActivity.getCacheDir().getAbsolutePath();  
		 webview.getSettings().setAppCachePath(appCachePath);  
		 webview.getSettings().setAllowFileAccess(true);  
		 webview.getSettings().setAppCacheEnabled(true); 
		 webview.addJavascriptInterface(this, "tcapp");		 

		setContentView(mView);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) 
			{
				//支付宝  如果需要在地址栏地址跳转劫持 这里依然可以做到 如果不采用原来的机制 此处就可以放弃掉(注释掉)
				if(url.contains("tc://")){//跳转到应用内部的处理
					MyApp.backToTCActivity();
					int id = Integer.parseInt(StringUtils.substringBetween(url, "tc://", "?"));
					if(id == 1001 || id == 1002 || id == 1003){
						CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.getItemById(id));
					}
				}
					view.loadUrl(url);
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);				
//				webview.loadUrl("javascript:LoadDeviceInfo("+Protocol.getInstance().getProtocol().toString()+")");
			}
			
			});
		webview.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
				
				if(title.length() > 8 ){
					String s=title.substring(0, 8)+"..";
					setTitleText(s);
				}else{
					setTitleText(title);
				}
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				//			setTitleText("数据加载中，请稍后.....");
				if(newProgress< 100){
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress((newProgress / 100 ) * 100);
				}else{
					progressBar.setVisibility(View.GONE);
				}
				
			}	
		});
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.getSettings().setJavaScriptEnabled(true);
		String url = getMyBundle().getString("url");
		synCookies(url);
		webview.loadUrl(url);
		
		//增加手机返回键  监听
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				finish();//关闭
				mActivity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});
	}
	@Override
	public void onResume() 
	{
		super.onResume();
		
	}	
	public void synCookies(String url) 
	{
		String auth = Protocol.getInstance().getProtocol().toString();
		Des2 des = new Des2("f81d7c99");
		try {
			auth = des.encrypt(auth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			auth = "";
		}
		CookieSyncManager.createInstance(MyApp.context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);
	    cookieManager.removeSessionCookie();        //移除	
	    cookieManager.setCookie(url, "auth="+auth);//cookies是在HttpClient中获得的cookie
	    CookieSyncManager.getInstance().sync();

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(UserBean.getInstance().isLogin()){
			webview.loadUrl("javascript:LoadDeviceInfo("+Protocol.getInstance().getProtocol().toString()+")");
		}
	}
	public void openWindow(String id,String data)
	{
		// 登录成功之后
		//要特别处理一下登陆，如果要打开的是登陆的接口，则需要回调一下
		if("10045".equals(id)){
			if(UserBean.getInstance().isLogin()){
//				webview.loadUrl("javascript:LoadDeviceInfo("+Protocol.getInstance().getProtocol().toString()+")");
				return;
			}else{
				startLoginForResult(); 
			}
		}else if(AppUtil.isEmpty(id)){
			Bundle data1=new Bundle();
			data1.putInt("window_id", Integer.parseInt(id));
			CustomTagEnum.startActivity(MyApp.activityList.get(0), data1, CustomTagEnum.getItemById(Integer.parseInt(id)));
		}
	}
	public String loadDeviceInfo(){
		return Protocol.getInstance().getProtocol().toString();
	}
}
