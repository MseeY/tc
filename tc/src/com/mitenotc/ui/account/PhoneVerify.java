package com.mitenotc.ui.account;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.NetUtil;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.register.RegisterNexttwo;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.LogUtil;
import com.mitenotc.utils.SPUtil;
/**
 * 用户中心中使用到的提交验证码  
 * 
 * 和注册register_next_two 布局相同  
 * 稍有一点点区别
 * @author wanli
 *
 */
public class PhoneVerify extends BaseFragment implements OnClickListener {
	private   TextView show_phoneNumber_tv;//显示手机号码
	private   TextView again_send_tv;//重新发送
	private   TextView follow_verify_tv;//随后验证
	private   TextView without_tv;//没有收到
	private   TextView reminder_Text_tv;//提示信息
	
	private   RelativeLayout Below_Rlayout;
	private   EditText verify_ed;
	private   Button submit_btn;
	private   CheckBox msg_checkBox;
	private   TCDialogs	dialog;
	/** 
	 * 更具具体的cmd  命令号显示控制布局
	 *  1001 短信验证（手机验证）
	 *  1007 修改密码
	 *  1009 找回密码（忘记密码）
	 *   
	 *   
	 **/
	private   int cmd=0;
	private   String USR;//手机号
	private   int verify=0;//验证
	private   Bundle bundle;
	
	
	private   MessageJson params;
	private   MessageBean msBean;
	private String inputver;//用户输入的验证码
	protected int tiemInt  ;
	private Message message;//倒计时需要的信息
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_next_two);
		
		init();
	}
	
	private void init() {
		Bundle nBundle=getMyBundle();
		if(nBundle!=null){
			cmd=nBundle.getInt(getString(R.string.cmd));
		}
		
		show_phoneNumber_tv=(TextView) findViewById(R.id.zwl_show_phoneNumber_tv);//显示手机号码
		verify_ed=(EditText) findViewById(R.id.zwl_verify_ed);//输入框
		submit_btn=(Button) findViewById(R.id.zwl_submit_btn);//提交
		again_send_tv=(TextView) findViewById(R.id.zwl_again_send_tv);//重新发送
		without_tv=(TextView) findViewById(R.id.without_tv);//没有收到
		reminder_Text_tv=(TextView) findViewById(R.id.reminder_Text_tv);
		follow_verify_tv=(TextView) findViewById(R.id.zwl_follow_verify_tv);
		msg_checkBox=(CheckBox) findViewById(R.id.msg_checkBox);
		Below_Rlayout=(RelativeLayout) findViewById(R.id.Below_Rlayout);
		
		reminder_Text_tv.setText(getString(R.string.reminder_text));
		again_send_tv.setTextColor(getResources().getColor(R.color.award_info_tv_title_color));
		
		
		without_tv.setVisibility(View.GONE);
		again_send_tv.setText(getString(R.string.register_show2_text4));// 重新发送   ---> 没有收到
		msg_checkBox.setText(getString(R.string.checkbox_text));
		follow_verify_tv.setText(getString(R.string.register_show2_text5));// 随后验证   ---> 重新发送
		
		show_phoneNumber_tv.setText(Protocol.getInstance().getUSR());//显示用户手机号码
//		USR="13866667777";
		if(1009==cmd){
			Below_Rlayout.setVisibility(View.VISIBLE);
			msg_checkBox.setText(getString(R.string.register_show_text1));
			USR=Protocol.getInstance().getUSR();
			setTitleNav("忘记密码", R.drawable.title_nav_back, 0);
		}else if(1001==cmd){
			setTitleNav("手机号码验证", R.drawable.title_nav_back, 0);
			USR=UserBean.getInstance().getPhoneNum();
			Below_Rlayout.setVisibility(View.GONE);
			togetVerify(cmd, USR);
			
		}
//		else if(1000==cmd){//注册时候去验证
//			setTitleNav("手机号码验证", 0, 0);
//			USR=UserBean.getInstance().getPhoneNum();
//			Below_Rlayout.setVisibility(View.GONE);
//			togetVerify(cmd, USR);
//			
//		}
		if(USR==null){
			again_send_tv.setEnabled(false);//默认首次如果没有手机号就不能重新发送
		}else{
			again_send_tv.setEnabled(true);
		}
		
		switch (cmd) {
//		*  1001 短信验证（手机验证）
		case 1001:
			togetVerify(cmd, USR);
			break;
		default:
			break;
		}
		
//		InputFilter[] filters= {new InputFilter.LengthFilter(11)};  
//		verify_ed.setFilters(filters); 
		setListen();
	}

	private void setListen() {
		submit_btn.setOnClickListener(this);
		follow_verify_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		提交验证码
		case R.id.zwl_submit_btn:
			gotosubimt(cmd);
			
			break;
	//重新发送
		case R.id.zwl_follow_verify_tv:
//			follow_verify_tv.setEnabled(false);
			togetVerify(cmd, USR);
			
			break;
		default:
			break;
		}
	}
	/**
	 * 时间跑秒倒计时
	 */
	final Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
					tiemInt  --;
					if(tiemInt > 0){
					    //  follow_verify_tv  重用： 忘记登录页面有所调整   本来是随后验证  在本页面修改为 重新发送
						follow_verify_tv.setEnabled(false);
						follow_verify_tv.setText(tiemInt+"秒后点击重发");
						message=handler.obtainMessage(1);
						handler.sendMessageDelayed(message, 1000);
						return;
					}
					follow_verify_tv.setText("重新发送");
					follow_verify_tv.setEnabled(true);
				break;

			default:
				break;
			}
		}
	};	


	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		msBean=(MessageBean) msg.obj;
		switch (msg.arg1) {
				//			获取验证码
							case 1:
								if("0".equals(msBean.getA())){
//									MyToast.showToast(mActivity, msBean.getB());
									MyToast.showToast(mActivity, "验证码已发送至您的手机！");
									  //			倒计时
									  //  follow_verify_tv  重用： 忘记登录页面有所调整   本来是随后验证  在本页面修改为 重新发送
									 tiemInt=60;
								     follow_verify_tv.setEnabled(false);
								     message=handler.obtainMessage(1);
									 handler.sendMessageDelayed(message,1000);
								}
								break;
				//         提交验证码
					       case 2:
//					    	   submit_btn.setEnabled(true);//不可重复点击
					    		if("0".equals(msBean.getA())){
//									MyToast.showToast(mActivity, msBean.getB());
					    			MyToast.showToast(mActivity, "验证成功！");
					    			UserBean.getInstance().setUserAccountState(msBean.getD());//状态码
									if(1009==cmd){
										bundle=new Bundle();
										bundle.putInt(getString(R.string.verify), verify);
										replaceMiddle(ChangePwd.class);
									}else if(1001==cmd){
										
										congratulationDialogs("手机号码验证", "开始", "取消");
										 finish();
									}else if(1000==cmd){
										//关闭 loginfragment 的 activity
										for (BaseActivity activity : MyApp.activityList) {
											if(activity.getCurrentFragment().getClass() == LoginFragment.class){
												activity.finish();
											}
										}
										
										  finish();
									}
									verify_ed.setText("");
								 }
								
								break;
				//		       重新发送
					       case 3:
					    	   if("0".equals(msBean.getA())){
//									MyToast.showToast(mActivity, msBean.getB());
					    		   MyToast.showToast(mActivity, "验证码已发送至您的手机！");
								}
								break;
				
							default:
								break;
		   }
	
	}
//	多种玩法不支持智能追号 提示用户

	private void congratulationDialogs(String title,String t,String c) {//****************ck 更改******************
		dialog=new TCDialogs(mActivity);
		dialog.phoneVSuccess(title, getString(R.string.dialog_tv_ver_phonenumber_text), t, c
				, new  MyClickedListener() {
					
					@Override
					public void onClick() {
						dialog.dismiss();
						
							finish();//哪里过来去哪里
					}
				}, new MyClickedListener() {
					
					@Override
					public void onClick() {
						dialog.dismiss();
						finish();//哪里过来去哪里
					}
				});
				
	}
//
//	/**
//	 * 提交验证码
//	 */
//	private void submitV() {
//		if(verify_ed.getText()==null){
//			return;
//		}
//		
//		String inVstr=StringUtils.stripToNull(verify_ed.getText().toString());
//		SPUtil.putString(getString(R.string.verify), inVstr);// 存储验证码 verify  临时用
//		 //判断
//		 if(analyze(inVstr)){
//			  submit_btn.setEnabled(false);
//			 
//			  GloableParams.USR=SPUtil.getString(getString(R.string.USR));
//			  params=new MessageJson();
//			  params.put("A", "2");
//			  params.put("B", "1001");
//			  params.put("C", inVstr);
//              submitData(2, cmd, params);  
//		 }
//		 submit_btn.setEnabled(true);
//		 verify_ed.setText("");//清空
//		
//	}
	/**
	 * 获取验证码(重新发送)
	 * 
	 * @param cmd
	 * @param phoneStr
	 */
	 private boolean togetVerify(int cmd ,String phoneStr){
			 //网络环境 
		    if(!NetUtil.checkNetWork(mActivity)){
		    	MyToast.showToastLong(mActivity, R.string.no_connect_server);
		    	return false;
			   }
			    params=new MessageJson();
			    params.put("A", "1");
			    params.put("B", cmd+"");
			    params.put("C", phoneStr);
			    submitData(1,1001,params);//1请求网络 获取验证码
		     
		  return true;
	 }
	/**
	 * 提交验证码
	 * @param cmd
	 * @return boolean
	 */
	 private boolean gotosubimt(int cmd ) {
		 String  strV=verify_ed.getText().toString();
		  if(StringUtils.isBlank(strV)){
			  return false;
		  }else {
            inputver = StringUtils.stripToNull(strV);
		    SPUtil.putString(getString(R.string.verify), inputver);// 存储验证码 下一步会使用到
			verify=Integer.parseInt(inputver);
			 if(analyze(inputver)){
//				  submit_btn.setEnabled(false);//不可重复点击
				  
				  params=new MessageJson();
				  params.put("A", "2");
				  params.put("B", cmd+"");
				  params.put("C", inputver+"");
				  submitData(2,1001,params);//2请求网络 提交验证码
			  }else{
				return false;  
			  }
		  }
		
		return true;
		 
	}
	
	/**
	 *  判断
	 * @param str
	 * @return
	 */
	private boolean analyze(String str) {
		 //为空判断
		 if(str == null||str.equals("") ){
				 LogUtil.info( "inputcodeStr is null!");
				 MyToast.showToast(mActivity, R.string.register_show_Toast6);
				 return false;
			}
		  //验证码判断
			 if(str.length() > 6 || str.length() < 6 ){
				 LogUtil.info( "length length exception !");
				 MyToast.showToast(mActivity, "请输入6位校验码！");
				 return false;
			 }
//		 //网络环境 
//	    if(!NetUtil.checkNetWork(mActivity)){
//	       MyToast.showToastLong(mActivity, R.string.no_connect_server);
//	       return false;
//		  }
	    return true;
	}

}
