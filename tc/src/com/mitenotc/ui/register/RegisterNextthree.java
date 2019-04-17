package com.mitenotc.ui.register;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mitenotc.bean.AccountBean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.NetUtil;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.LoginFragment;
import com.mitenotc.ui.account.MeFragment;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.SPUtil;

public class RegisterNextthree extends BaseFragment implements OnClickListener {

	private TextView userPhone_tv;//显示用户手机号码
	private EditText upwd_ed;//密码一
	private EditText uagainpwd_ed;//密码二
	private Button ok_btn;//确认注册
	private String strPwd1=null;//未加密的密码
	private String pwd1=null;
	private String pwd2=null;
	private MessageBean resultmsg;
	private ProgressDialog dialog=null;
	
	private MessageBean mBean=null;//请求信息与返回信息
	private MessageJson params=null;//请求信息 参数
//	private MessageJson mProbeMsg=null;//探针信息
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_next_three);
        
		init();
	}

	private void init() {
		userPhone_tv=(TextView) findViewById(R.id.zwl_show_phoneNumber2);
		userPhone_tv.setText(Protocol.getInstance().getUSR());
		
		upwd_ed=(EditText) findViewById(R.id.zwl_pwd_ed);
		uagainpwd_ed=(EditText) findViewById(R.id.zwl_againpwd_ed);
		ok_btn=(Button) findViewById(R.id.zwl_bt_oKsubmit);
		ok_btn.setEnabled(true);
		
		setListen();
	}

	private void setListen() {

		ok_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//确认注册
		case R.id.zwl_bt_oKsubmit:
//			if(mProbeMsg==null){
//				mProbeMsg=new MessageJson();
//			 }
//			 mProbeMsg.put("A", MyApp.TEMP_PROBE_ID);
//			 mProbeMsg.put("B", "注册3-点击-确认注册");
//			 mProbeMsg.put("C", "确认注册-按钮-1000请求");
//			 mProbeMsg.put("D", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			affirmSubmit();
			
			break;
      default:
			break;
		}
		
	}

	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		mBean=(MessageBean) msg.obj;
			//			注册密码
			 switch (msg.arg1) {
						     
			//			     注册成功
							case 1000:
								 if("0".equals(mBean.getA()))
								 {
//									 MyToast.showToast(mActivity, mBean.getB());
									 MyToast.showToast(mActivity," 恭喜您，注册成功！");
									 SPUtil.putString(getString(R.string.USR),  Protocol.getInstance().getUSR());//存取手机号
									 SPUtil.putString(getString(R.string.recommendName),mBean.getC());//存取服务器返回的推荐名
									 SPUtil.putString(getString(R.string.pwd),pwd1);//存取密码
									  
									   UserBean.getInstance().setLogin(true);
									   UserBean.getInstance().setPhoneNum(Protocol.getInstance().getUSR());//存储用户的电话号码
									   UserBean.getInstance().setUserAccountState(mBean.getE());
									   //保存彩名
									   SPUtil.putString(R.string.uname, mBean.getC());//用户彩名
									   UserBean.getInstance().setLotteryName(mBean.getC());
//									   UserBean.getInstance().setUserRating(mBean.getE());//等级
									   UserBean.getInstance().setAvailableMoney(Long.parseLong(mBean.getF()));
									   UserBean.getInstance().setAvailableCash(Long.parseLong(mBean.getG()));
									   UserBean.getInstance().setAvailableBalance(Long.parseLong(mBean.getH()));
									   AccountEnum.convertMessage(mBean.getLIST());
//						        	   //成功注册之后  新的协议不需要 二次发送 登陆的请求
//						        	   params=new MessageJson();
//						        	   params.put("A", pwd1);//登陆密码
//						        	   submitData(2, 1002, params);
						        	   
									 //关闭 loginfragment 的 activity
									for (BaseActivity activity : MyApp.activityList) {
										if(activity.getCurrentFragment().getClass() == LoginFragment.class){
											activity.finish();
										}
									}
						        	   replaceMiddle(RegisterNextfour.class);
						       	    upwd_ed.setText("");
						    		uagainpwd_ed.setText("");
//						    		if(mProbeMsg!=null)
//										 mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//										 mProbeMsg.put("F", "0");
						           }
//								 else{
////						        	   if(mProbeMsg!=null)
////											mProbeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
////											mProbeMsg.put("F", "0");
////											mProbeMsg.put("G", "A错误代码"+ mBean.getA()+"B错误信息描述"+mBean.getB());
//								      }
//								MyApp.saveProbeMsg(mProbeMsg);// 存储探针信息
								break;
								//	登陆成功
							case 2:
						           if("0".equals(mBean.getA())){
//							        	MyToast.showToast(mActivity, mBean.getB());
							        	
							        	UserBean.getInstance().setLogin(true);
							        	UserBean.getInstance().setPhoneNum(Protocol.getInstance().getUSR());//存储用户的电话号码
							    		UserBean.getInstance().setUserAccountState(mBean.getF());
							    		//保存彩名
							    		UserBean.getInstance().setLotteryName(mBean.getC());
							    		UserBean.getInstance().setUserRating(mBean.getE());
							    		
							    		UserBean.getInstance().setAvailableMoney(Long.parseLong(mBean.getF()));
							    		UserBean.getInstance().setAvailableCash(Long.parseLong(mBean.getG()));
							    		UserBean.getInstance().setAvailableBalance(Long.parseLong(mBean.getH()));
							    		AccountEnum.convertMessage(mBean.getLIST());
							    		
					       		 }
								break;
			
					default:
						break;
			}

	}

//	确认提交
	private void affirmSubmit() {
	    strPwd1=upwd_ed.getText().toString();
		String strPwd2=uagainpwd_ed.getText().toString();
		
		if(detectionNullAndLength(strPwd1, strPwd2))
		{
//			ok_btn.setEnabled(false);
			
			pwd1=DigestUtils.md5Hex(StringUtils.stripToNull(strPwd1));
			pwd2=DigestUtils.md5Hex(StringUtils.stripToNull(strPwd2));
//				A	推荐人	
//				B	密码(MD5)	
//				C	彩名:显示名	
//				D	验证码	
				params=new MessageJson();
				params.put("A", Protocol.getInstance().getH());
				params.put("B", pwd1);
				params.put("C", "");
				params.put("D", SPUtil.getString(R.string.verify));//取出上一步骤中暂时存储的验证码
				submitData(1000, 1000, params);
//				清空
//				upwd_ed.setText("");
//				uagainpwd_ed.setText("");
		  }
	
	}
		
    /**
     * 判断为空  和长度
     * @param strPwd1
     * @param strPwd2
     * @return
     */
	private boolean detectionNullAndLength(String strPwd1, String strPwd2) 
	{
		//   是否为null
		if(StringUtils.isBlank(strPwd1)||StringUtils.isBlank(strPwd2))
		{
		     MyToast.showToast(mActivity, R.string.register_show_Toast7);
			return false;
			 //密码对比
		}else if(!strPwd1.equals(strPwd2)){
			MyToast.showToast(mActivity, R.string.register_show_Toast8);
			
			return false ;
			//  长度
	    }else if(strPwd1.length() < 6 || strPwd2.length() < 6)
		{
			MyToast.showToast(mActivity, getString(R.string.is_length_erro_text));//密码长度小于6
			
			return false;
		}
//		 //网络环境 
//	    if(!NetUtil.checkNetWork(mActivity))
//	    {
//	       MyToast.showToastLong(mActivity, R.string.no_connect_server);
//	       
//	       return false;
//		 }
	    

		return true;
	}
	
}
