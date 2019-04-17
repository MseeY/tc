package com.mitenotc.ui.register;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.RegisterBean;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.engine.EngineProxy;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.NetUtil;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.account.PhoneVerify;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.LogUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 注册第一步
 * 
 * @author 朱万利
 *
 */
public class RegisterNextone extends BaseFragment implements OnClickListener{
	/***获取验证码标示***/
	protected static final int NEXT = 1;
	/***弹出提示***/
	protected static final int POPUP_DIALOG=2;
	/***关闭提示***/
	protected static final int CLOSE_DIALOG= 3;
	
	private EditText uPhoneVerify_ed;//手机收到的验证码
	private Button gain_btn;//获取验证码
	private TextView follow_gain_tv;//随后验证
	private TextView i_have_verify_tv;//已有验证码
	private CheckBox agree_chbox;//同意条款
	private static String inputcodeStr;//输入电话号码

	private TCDialogs dialog=null;
	
	private MessageBean msBean=null;//请求信息与返回信息
	private MessageJson params=null;//请求信息 参数
//	protected MessageJson mProbeMsg=null;// 探针信息
	
	private int cmd=0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_next_one);
		init();
	}

	private void init() {
		Bundle mBundle=getMyBundle();
		if(mBundle!=null){
			cmd=mBundle.getInt(getString(R.string.cmd));
		}
		i_have_verify_tv=(TextView) findViewById(R.id.i_have_verify_tv);
		uPhoneVerify_ed=(EditText) findViewById(R.id.zwl_ed_phonenumber);
		gain_btn=(Button) findViewById(R.id.zwl_gain_button);
		agree_chbox=(CheckBox) findViewById(R.id.zwl_agree_checkBox);
		follow_gain_tv=(TextView)findViewById(R.id.zwl_follow_gain_tv);
//		忘记密码跳转过来不需要显示   随后验证TextVIew
		if(1009==cmd){
			follow_gain_tv.setVisibility(View.GONE);
			setTitleNav("忘记密码", R.drawable.title_nav_back, 0);
		}
		setListen();
	}

	private void setListen() {
		gain_btn.setOnClickListener(this);
		follow_gain_tv.setOnClickListener(this);
		i_have_verify_tv.setOnClickListener(this);
		agree_chbox.setOnClickListener(this);
		agree_chbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
//					MyToast.showToast(mActivity, R.string.register_show_Toast);
					
				}else{
//					MyToast.showToast(mActivity, R.string.dialog_btn_cancel);
				}
			}
		});
		//输入监听
       uPhoneVerify_ed.addTextChangedListener(new TextWatcher() {
			
			
			@Override
	   public void afterTextChanged(Editable s) {
			inputcodeStr=StringUtils.stripToNull(uPhoneVerify_ed.getText().toString());
			if(inputcodeStr==null){
				return ;
				
			}
			if( inputcodeStr.length() > 0){
				   gain_btn.setEnabled(true);
			}else{
				gain_btn.setEnabled(false);
				
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}});
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		//获取验证码
		case R.id.zwl_gain_button:
//			if(mProbeMsg==null){
//				mProbeMsg=new MessageJson();
//			 }
//			 mProbeMsg.put("A", MyApp.TEMP_PROBE_ID);
//			 mProbeMsg.put("B", "注册-点击-获取验证码");
//			 mProbeMsg.put("C", "获取验证码-按钮-1011 请求");
//			 mProbeMsg.put("D", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			getVerificationCode();
			break;
		//随后验证
        case R.id.zwl_follow_gain_tv:
//        	if(mProbeMsg==null){
//				mProbeMsg=new MessageJson();
//			 }
//			 mProbeMsg.put("A", MyApp.TEMP_PROBE_ID);
//			 mProbeMsg.put("B", "注册-点击-随后验证");
//			 mProbeMsg.put("C", "随后验证-文字按钮-1011 请求");
//			 mProbeMsg.put("D", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        	followVerify();
        	break;
       //已有验证码
        case R.id.i_have_verify_tv:
//        	if(mProbeMsg==null){
//				mProbeMsg=new MessageJson();
//			 }
//			 mProbeMsg.put("A", MyApp.TEMP_PROBE_ID);
//			 mProbeMsg.put("B", "注册-点击-已有验证码");
//			 mProbeMsg.put("C", "已有验证码-文字按钮-1011 请求");
//			 mProbeMsg.put("D", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        	followVerify_have();
        	
        	break;
//        	我同意
        case R.id.zwl_agree_checkBox:
        	
        	Bundle  mBundle=new Bundle();
        	mBundle.putString("title", "委托投注规则");
        	mBundle.putString("url", "http://news.mitenotc.com/a/wttzgz.html");// http://news.mitenotc.com/a/wttzgz.html  2014-5-19 朱老师固定下来的地址
        	start(ThirdActivity.class,TCWebFragment.class,mBundle);
//        	MyApp.saveProbeMsg("注册1-点击选择我同意","查看-点击-同意委托", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
        	break;
       default: 
			break;
		}
	}
	
	//子线程请求网络之后回调函数
	@Override
	protected void onMessageReceived(Message msg) {
		msBean=(MessageBean) msg.obj;
		switch (msg.arg1) {
			
		case 1001:
			if("0".equals(msBean.getA())){
				 MyToast.showToast(mActivity,"验证码已发送至您的手机！");
				 if(cmd==1009){
					 replaceMiddle(PhoneVerify.class);
				 }else{
					 replaceMiddle(RegisterNexttwo.class);
				 }
				 uPhoneVerify_ed.setText("");
//				 if(mProbeMsg!=null)
//					 mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					 mProbeMsg.put("F", "0");
			}else{
				
				 MyToast.showToast(mActivity," erro: " + msBean.getA());
//				 if(mProbeMsg!=null)
//						mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//						mProbeMsg.put("F", "0");
//						mProbeMsg.put("G", "A错误代码"+ msBean.getA()+"B错误信息描述"+msBean.getB());
			}
//			MyApp.saveProbeMsg(mProbeMsg);// 存储探针信息
			break;
//			随后验证
		case 333:
			if("0".equals(msBean.getA())){
				replaceMiddle(RegisterNextthree.class);
				
				uPhoneVerify_ed.setText("");
//				 if(mProbeMsg!=null)
//					 mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					 mProbeMsg.put("F", "0");
			 }else
			 {
//				 MyToast.showToast(mActivity, Protocol.getInstance().getUSR()+"号已注册，请直接登录！");
				
				 showDialog(Protocol.getInstance().getUSR()+"号已注册，请直接登录！");
//				 if(mProbeMsg!=null)
//					mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					mProbeMsg.put("F", "0");
//					mProbeMsg.put("G", "A错误代码"+ msBean.getA()+"B错误信息描述"+msBean.getB());
			 }
//			MyApp.saveProbeMsg(mProbeMsg);// 存储探针信息
			break;
//			获取验证码之前 先验证
		case 555:
			if("0".equals(msBean.getA())){
				 params=new MessageJson();
				 if(1009==cmd){
//					          忘记密码
					 params.put("A", "1");
					 params.put("B", cmd+"");
					 params.put("C", Protocol.getInstance().getUSR());
					 }else{
//						 正常情况下
						 params.put("A", "1");
						 params.put("B", "1001");
						 params.put("C", Protocol.getInstance().getUSR());
					 }
				 submitData(1001,1001,params);//获取验证码  请求网络
				 uPhoneVerify_ed.setText("");
//				 if(mProbeMsg!=null)
//				 mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//				 mProbeMsg.put("F", "0");
			}else{
				showDialog(Protocol.getInstance().getUSR()+"号已注册，请直接登录！");
//				if(mProbeMsg!=null)
//					mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					mProbeMsg.put("F", "0");
//					mProbeMsg.put("G", "A错误代码"+ msBean.getA()+"B错误信息描述"+msBean.getB());
				
			}
//			MyApp.saveProbeMsg(mProbeMsg);// 存储探针信息
			break;
//			我已有验证码
		case 444:
			if("0".equals(msBean.getA()))
			{
				if(1009==cmd){
					replaceMiddle(PhoneVerify.class);
				}else{
					replaceMiddle(RegisterNexttwo.class);
				}
				uPhoneVerify_ed.setText("");
//				 if(mProbeMsg!=null)
//					 mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					 mProbeMsg.put("F", "0");
			}else{
//				MyToast.showToast(mActivity, Protocol.getInstance().getUSR()+"号已注册，请直接登录！");
				showDialog(Protocol.getInstance().getUSR()+"号已注册，请直接登录！");
//				if(mProbeMsg!=null)
//					mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					mProbeMsg.put("F", "0");
//					mProbeMsg.put("G", "A错误代码"+ msBean.getA()+"B错误信息描述"+msBean.getB());
			   }
//			MyApp.saveProbeMsg(mProbeMsg);// 存储探针信息
			break;
			
	
		default:
			break;
		}
	}
		private void showDialog(String contentStr){
			if(dialog==null){
				dialog=new TCDialogs(mActivity);
			}
			dialog.unableChasesDialog("提    示 ", contentStr,"确定",
					new MyClickedListener() {
							
							@Override
							public void onClick()
							{
								SPUtil.putString(getString(R.string.USR), Protocol.getInstance().getUSR());
								dialog.dismiss();
								finish();
								
							}
						},
						
				false);
		}
	
	/**
	 * 随后验证
	 * 或
	 * 已有验证码
	 */
	
    private void followVerify() {
    	SPUtil.putString(getString(R.string.USR), "");//清空
    	SPUtil.putString(getString(R.string.verify), "");//清空
    	//剔除空格
    	inputcodeStr=StringUtils.stripToNull(uPhoneVerify_ed.getText().toString());
	   	
    	if(analyze(inputcodeStr)){//为空验证
//			手机号码需要存储
    		 Protocol.getInstance().setUSR(inputcodeStr);
    		 GloableParams.USR=inputcodeStr;
    		 params=new MessageJson();
    		 submitData(333,1011,params);//请求网络
	    	 
	     }
	}
    /**
     * 已有验证码
     */
    
    private void followVerify_have() {
    	SPUtil.putString(getString(R.string.USR), "");//清空
    	SPUtil.putString(getString(R.string.verify), "");//清空
    	//剔除空格
    	inputcodeStr=StringUtils.stripToNull(uPhoneVerify_ed.getText().toString());
    	
    	if(analyze(inputcodeStr)){//为空验证
////			手机号码需要存储
    		Protocol.getInstance().setUSR(inputcodeStr);
    		GloableParams.USR=inputcodeStr;
//    		
    		params=new MessageJson();
    		submitData(444,1011,params);//请求网络
    	}
    }
    
	/**
     * 获取验证码
     * 发送网络请求
     */
  private void getVerificationCode() {
	  //剔除空格
		inputcodeStr=StringUtils.stripToNull(uPhoneVerify_ed.getText().toString());
		if(analyze(inputcodeStr)){
//		手机号码需要暂时存储
			Protocol.getInstance().setUSR(inputcodeStr);
			
			 GloableParams.USR=inputcodeStr;
			 params=new MessageJson();
			 submitData(555,1011,params);//请求网络
		}
//		 uPhoneVerify_ed.setText("");
	}
  
    /**
     *  判断
     * @param str
     * @return
     */
    private boolean analyze(String str){
		 //为空判断
		 if(StringUtils.isBlank(str)){
				 MyToast.showToast(mActivity, R.string.register_show_Toast6);
				
		       return false;
				 //手机号码判断
		 }else if(11 != str.length()  ){
			 MyToast.showToast(mActivity, R.string.register_show_Toast0);
			 return false;
		 } else  
//		 if(isMobileNo(str)) {
			 //同意条款
			 if(agree_chbox.isChecked()==false){
				 MyToast.showToast(mActivity, R.string.register_show_Toast1);
				 return false;
				 //网络环境 
			 }
//			 else if(!NetUtil.checkNetWork(mActivity)){
//				 MyToast.showToastLong(mActivity, R.string.no_connect_server);
//				 return false;
//			 }
		 
//	     }else {
//	    	 MyToast.showToast(mActivity, R.string.register_show_Toast0);
//			 return false;
//	     }
	    return true;
  }
//      移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//  　　联通：130、131、132、152、155、156、185、186
//  　　电信：133、153、180、189、（1349卫通）
//    public static boolean isMobileNo(String mobilesStr){
//		Pattern mPattern=Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); 
//		Matcher matcher=mPattern.matcher(mobilesStr);
//		return matcher.matches();
//	}
	
}
