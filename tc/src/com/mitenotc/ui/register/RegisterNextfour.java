package com.mitenotc.ui.register;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment2;
import com.mitenotc.ui.account.PhoneVerify;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.SPUtil;

public class RegisterNextfour extends BaseFragment implements OnClickListener {
	
	private TextView felicitate_tv;//恭喜通知
	private Button goon_btn;//继续我的订单
	private Button immediately_btn;//立刻验证
	private Button affirm_btn;//确认注册
	private Button goto_btn;//马上去摇
	private TextView show_inform_tv;//红包通知
	
	private MessageBean mBean=null;//请求信息与返回信息
	private MessageJson params=null;//请求信息 参数
	private Spanned spdtext;
	private String phonetext="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_next_four);
		
		init();
	}

	private void init() {
		
		felicitate_tv=(TextView) findViewById(R.id.zwl_felicitate_tv);
//		<font color=#> ... </font>
		phonetext=secrecyPhone(SPUtil.getString(getString(R.string.USR)));
		spdtext=Html.fromHtml("恭喜您，您的手机号:"+"<font color=red>"+phonetext+"</font>"+"注册成功！");
		felicitate_tv.setText(spdtext);//恭喜通知
		goon_btn=(Button) findViewById(R.id.zwl_goon_btn);
		immediately_btn=(Button) findViewById(R.id.zwl_immediately_btn);
		affirm_btn=(Button) findViewById(R.id.zwl_affirm_btn);
		goto_btn=(Button) findViewById(R.id.zwl_goto_btn);
		show_inform_tv=(TextView) findViewById(R.id.zwl_show_inform_tv);
		
		setLinten();
		
	}

	private void setLinten() {
		goon_btn.setOnClickListener(this);
		immediately_btn.setOnClickListener(this);
		affirm_btn.setOnClickListener(this);
		goto_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		继续订单
		case R.id.zwl_goon_btn:
			finish();
//			MyApp.saveProbeMsg("注册4-继续订单-按钮","继续完成订单", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "",true);
			break;
//		立即验证	
		case R.id.zwl_immediately_btn:
			Bundle mBundle=new Bundle();
			mBundle.putInt(getString(R.string.cmd), 1001);
			start(ThirdActivity.class, PhoneVerify.class,mBundle);
//			MyApp.saveProbeMsg("注册4-立即验证-按钮","继续完成-立即验证", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "",true);
			finish();
			
//			replaceMiddle(PhoneVerify.class);
			break;
//		随便转转
		case R.id.zwl_affirm_btn:
//			MyApp.saveProbeMsg("注册4-随便转转-按钮","注册完成-随便转转-回到用户中心", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "",true);
			finish();
			
			break;
//	        马上购彩
		case R.id.zwl_goto_btn:
			MyApp.backToTCActivity();
			Bundle data=new Bundle();
			data.putInt("window_id", 1001);
			CustomTagEnum.startActivity(MyApp.activityList.get(0), data, CustomTagEnum.getItemById(1001));
//			MyApp.saveProbeMsg("注册4-马上购彩-按钮","注册完成-马上购彩!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "",true);
			break;

		default:
			break;
		}
		
	}
	/**
	 * 手机号加'*'
	 * @param strphone
	 * @return
	 */
	public static String secrecyPhone(String strphone){
		long n1=System.currentTimeMillis();
//		StringBuilder sb=new StringBuilder();
		String str="";
		for(int i=0; i< strphone.length(); i++) {
		   char c=strphone.charAt(i);
		   if(2 >= i  || i >= 7){
//			   sb.append(c); 
			   str +=c;
			   
		   }else{
			   str +='*';
//			   sb.append('*');
		   }
		}
		long n2=System.currentTimeMillis();
//		return sb.toString();
		return str;
		
	}
	
	@Override
	public void onDestroy() {
		SPUtil.putString(getString(R.string.verify), "");//清空
		super.onDestroy();
	}
	

}
