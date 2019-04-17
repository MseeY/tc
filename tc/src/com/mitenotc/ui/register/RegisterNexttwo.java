package com.mitenotc.ui.register;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.LogUtil;
import com.mitenotc.utils.SPUtil;

public class RegisterNexttwo extends BaseFragment implements OnClickListener, TextWatcher {
	/***提交验证***/
	protected  int tiemInt = 60;
	private TextView userPhone_tv;//显示用户手机号
	private EditText verify_ed;//用户获取到的验证码
	private Button submit_check_btn;//提交验证码
	private TextView again_send_tv;//重新发送
	private TextView follow_verify_tv;//随后验证
	private String inputver;//输入客户端验证码
	private MessageBean mBean=null;//信息体服务器端返回的
	private MessageJson params=null;//请求参数
//	private MessageJson mProbeMsg=null;//探针
	private List<MessageBean> returnLise;
	private Message message;//倒计时需要的信息

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_next_two);
		init();
		//添加自动识别验证码
//		AuthGetCode();
	}

	private void init() {
		userPhone_tv=(TextView) findViewById(R.id.zwl_show_phoneNumber_tv);
		userPhone_tv.setText(Protocol.getInstance().getUSR());//显示用户手机号码
		
		verify_ed=(EditText)findViewById(R.id.zwl_verify_ed);
		submit_check_btn=(Button) findViewById(R.id.zwl_submit_btn);
		again_send_tv=(TextView) findViewById(R.id.zwl_again_send_tv);
		follow_verify_tv=(TextView) findViewById(R.id.zwl_follow_verify_tv);
		
		setListen();
	}


	private void setListen() {
		//输入验证
		verify_ed.addTextChangedListener(this);
		submit_check_btn.setOnClickListener(this);
		
		again_send_tv.setOnClickListener(this);
		follow_verify_tv.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		if(smsReceiver!=null){
//			mActivity.unregisterReceiver(smsReceiver);
//		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		      提交验证
		case R.id.zwl_submit_btn:
			sumbitVerify();
			
			break;
//			重发
        case R.id.zwl_again_send_tv:
		    againSend();
			
		    break;
//			 随后验证
       case R.id.zwl_follow_verify_tv:
    	    replaceMiddle(RegisterNextthree.class);
    	    break;
		default:
			break;
		}
		
	}
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);   
		mBean=(MessageBean) msg.obj;
		
		switch (msg.arg1) {
			case 111:
            	if("0".equals(mBean.getA())){
            		 MyToast.showToast(mActivity,"验证成功！");
            		 params=new MessageJson();
        			 submitData(555,1011,params);//请求网络
            	}
	           break;
			case 222:
//				again_send_tv.setEnabled(true);
				if("0".equals(mBean.getA())){
					
					MyToast.showToast(mActivity, "验证码已发送至您的手机！");
//					倒计时
					 again_send_tv.setEnabled(false);
					 tiemInt = 60;
					 message=handler.obtainMessage(1);
					 handler.sendMessageDelayed(message,1000);
				}
				break;
			case 555:
//				again_send_tv.setEnabled(true);
				if("0".equals(mBean.getA())){
					 replaceMiddle(RegisterNextthree.class);
				}else{
				      finish();//已注册用户二次验证
				}
				break;
				
		
			default:
				break;
		}
	
	}
	/**
	 * 重新发送
	 */
	private void againSend() {
//		again_send_tv.setEnabled(false);
         params=new MessageJson();
         params.put("A", "1");
		 params.put("B", "1001");
		 params.put("C", Protocol.getInstance().getUSR());
		 submitData(222,1001,params);//请求网络

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
				tiemInt --;
				if(tiemInt > 0){
					again_send_tv.setEnabled(false);
					again_send_tv.setText(tiemInt+"秒后点击重发");
					message=handler.obtainMessage(1);
					handler.sendMessageDelayed(message, 1000);
				}else{
					again_send_tv.setText("重新发送");
					again_send_tv.setEnabled(true);
					
				}
				break;

			case 0:
				//把验证码添加到输入框
				verify_ed.setText(strContent);
				break;
			}
		}
	};
	
	/**
	   * 提交验证码
	   * 
	   */

	private void sumbitVerify() {
		if(StringUtils.isBlank(verify_ed.getText().toString())){
			MyToast.showToast(mActivity, "验证码不能为空！");
			return;
		}
		
		inputver=StringUtils.stripToNull(verify_ed.getText().toString());
		SPUtil.putString(getString(R.string.verify), inputver);// 存储验证码 下一步会使用到
		 //判断
		 if(analyze(inputver)){
//			  submit_check_btn.setEnabled(false);
			 
			  GloableParams.USR=Protocol.getInstance().getUSR();
			  params=new MessageJson();
			  params.put("A", "2");
			  params.put("B", "1001");
			  params.put("C", inputver);
              submitData(111, 1001, params);  
              verify_ed.setText("");//清空
		 }
//		 submit_check_btn.setEnabled(true);
	}
	
   /**
     * 判断
     * @param str
     * @return
     */
  private boolean analyze(String inputver){
	    //为空判断
		 if(inputver == null || inputver.equals("") ){
				 LogUtil.info( "inputcodeStr is null!");
				 MyToast.showToast(mActivity, R.string.register_show1_Toast1);
				 return false;
			}
		//验证码判断
		 if(inputver.length() > 6 || inputver.length() < 6 ){
			 LogUtil.info( "inputver Failure !");
			 verify_ed.clearComposingText();
			 MyToast.showToast(mActivity, R.string.register_show_Toast3);
			 return false;
		   }
//		 //网络环境 
//	    if(!NetUtil.checkNetWork(mActivity)){
//	       MyToast.showToastLong(mActivity, R.string.no_connect_server);
//	       return false;
//		  }
	    return true;
  }

    
    //编辑窗体监听
	@Override
	public void afterTextChanged(Editable arg0) {
         if(StringUtils.isBlank(verify_ed.getText().toString())){
        	 return;
          }
			if(StringUtils.stripToNull(verify_ed.getText().toString()).length() > 0){
				submit_check_btn.setEnabled(true);
			}else{
				submit_check_btn.setEnabled(false);
				submit_check_btn.setBackgroundColor(R.drawable.btn_bg_white_selector);
			}
	}


	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}


	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}
	
	private IntentFilter filter;
	private BroadcastReceiver smsReceiver;
	private String strContent;
	
	private void AuthGetCode() {
		filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					String from = sms.getOriginatingAddress();
					if (!AppUtil.isEmpty(from)) {
						String code = patternCode(message);
						if (!AppUtil.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(0);
						}
					}
				}
			}
		};

		mActivity.registerReceiver(smsReceiver, filter);
	}

	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return String
	 */
	
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)"; 
	
	private String patternCode(String patternContent) {
		if (AppUtil.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
}
