package com.mitenotc.ui.pay;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MWebView;
/**
 *   此类已经被TCWebFragment  里面的js-android互相调用机制取代
 *   
 * @author mitenotc
 *
 */
public class Ali_WAP_Fragment extends BaseFragment{

	private WebView ali_wap_wv; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("支付宝Wap支付（免手续费）");
		setContentView(R.layout.ali_wap);
		ali_wap_wv = (MWebView) findViewById(R.id.ali_wap_wv);
		ali_wap_wv.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
		});
		String url = getMyBundle().getString("url");
//		for (String obj : getMyBundle().keySet()) {
//			////System.out.println("key : value = "+obj+" : "+getMyBundle().getString(obj));
//		}
//		////System.out.println("url = "+ url);
		ali_wap_wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		ali_wap_wv.getSettings().setJavaScriptEnabled(true);
		CookieManager.getInstance().setAcceptCookie(true);
		
		ali_wap_wv.loadUrl(url);
	}
} 

//{"GOTOURL":"跳转地址","WIDseller_email":"卖家支付宝账号","WIDout_trade_no":"订单编号","WIDsubject":"订单名称","WIDtotal_fee":"付款金额"}
