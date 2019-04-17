package com.mitenotc.ui.account;

import java.util.ArrayList;
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

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.NetUtil;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.R;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.register.RegisterNextfour;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.LogUtil;
import com.mitenotc.utils.SPUtil;
/**
 * 修改密码
 * 
 * 找回密码
 * 
 * @author wanli
 *
 */
public class ChangePwd extends BaseFragment implements OnClickListener {


	private TextView userPhone_tv;//显示用户手机号码
	private EditText originally_ed;//原密码
	private EditText upwd_ed;//密码一
	private EditText uagainpwd_ed;//密码二
	private Button ok_btn;//确认注册
	private String pwd0=null;//原密码
	private String s1=null;
	private String s2=null;
	private String s3=null;
	private MessageBean resultmsg;
	private TCDialogs dialog=null;
   
	/**
	 * cmd=1007 : 修改密码
	 * 
	 * cmd=1009 : 找回密码
	 */
	private int cmd=0;//命令
	private int verify=0;//验证
	private   String USR;//手机号
	
	
	private MessageBean mBean=null;//请求信息与返回信息
	private MessageJson params=null;//请求信息 参数
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_next_three);
		init();
	}

	private void init() {
		cmd=getMyBundle().getInt(getString(R.string.cmd));
		if(1009==cmd){
			USR=Protocol.getInstance().getUSR();	
		}else{
			USR=UserBean.getInstance().getPhoneNum();
		}
		
		originally_ed=(EditText)findViewById(R.id.zwl_pwd_ed0);
		userPhone_tv=(TextView) findViewById(R.id.zwl_show_phoneNumber2);
		userPhone_tv.setText(USR);//显示号码
		
		upwd_ed=(EditText) findViewById(R.id.zwl_pwd_ed);
		uagainpwd_ed=(EditText) findViewById(R.id.zwl_againpwd_ed);
		ok_btn=(Button) findViewById(R.id.zwl_bt_oKsubmit);
		ok_btn.setText(getString(R.string.affirm_alter_text));
		   
		//默认 根据具体cmd决定布局
			switch (cmd) {
				case 1007:
					setTitleNav("修改密码", R.drawable.title_nav_back, 0);
					originally_ed.setVisibility(View.VISIBLE);// 默认是1007 表示修改密码
					break;
			    case 1009:
			    	setTitleNav("忘记密码", R.drawable.title_nav_back, 0);
			    	verify=getMyBundle().getInt(getString(R.string.verify));
			    	
					originally_ed.setVisibility(View.GONE);// 默认是1009 表示找回密码密码
					break;
				default:
					break;
				}
		
		setListen();
	}

	private void setListen() {
		ok_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//确认修改
		case R.id.zwl_bt_oKsubmit:
//			if(NetUtil.checkNetWork(mActivity)){
				oksubmit(cmd);
//			}else{
//				MyToast.showToast(mActivity, getString(R.string.net_setting_toast));//提示检查网络
//			}
			
			break;
      default:
			break;
		}
		
	}



	private void oksubmit(int cmd) {
		// 默认 根据具体cmd决定调用方法
		switch (cmd) {
		case 1007:
			 affirmSubmit(cmd);// 默认是1007 表示  修改密码
			
			break;
         case 1009:
			
        	 affirmSubmit(cmd);// 默认是1009表示  找回密码
			break;
		default:
			break;
		}
		
	}

	@Override
	protected void onMessageReceived(Message msg) {
		mBean=(MessageBean) msg.obj;
		switch (msg.what) {
		case MSG_ERROR:
			 MyToast.showToast(mActivity, mBean.getB());
			break;
			
		case MSG_OK:
			     switch (msg.arg1) {
		//				 修改密码成功
					     case 1007:
//					    	 ok_btn.setEnabled(true);
					    	 if("0".equals(mBean.getA())){
//					    		 MyToast.showToast(mActivity, mBean.getB());
					    		 MyToast.showToast(mActivity, "恭喜您,修改成功!");
					    		 SPUtil.putString(getString(R.string.pwd),s1);//存取密码
					    		 
					    		 congratulationDialogs(2,"修改密码", "恭喜您，您账户密码已经修改成功！","关闭");//恭喜
					    		    
					    		 originally_ed.setText("");
								 upwd_ed.setText("");
								 uagainpwd_ed.setText("");
					    		 finish();
					    	 }
					    	 break;
		//			     找回密码成功
						case 1009:
//							ok_btn.setEnabled(true);
							 if("0".equals(mBean.getA())){
//					        	   MyToast.showToast(mActivity, mBean.getB()); 
								   MyToast.showToast(mActivity, "恭喜您,找回密码成功!");
					        	   SPUtil.putString(getString(R.string.pwd),s1);//存取密码
					        	   congratulationDialogs(2,"忘记密码", "恭喜您，您账户密码已经成功找回！","关闭");//恭喜
					        		
					        	   upwd_ed.setText("");
								   uagainpwd_ed.setText("");
					           }
							break;
				
						default:
							break;
				}
          
	          
			break;
			
		case MSG_NULL:
			 MyToast.showToast(mActivity, mBean.getB());
			 
			break;
		default:
			break;
		}
	}
//	多种玩法不支持智能追号 提示用户
	
	private void congratulationDialogs(final int i,String t,String c,String b) {//****************ck 更改******************
		dialog=new TCDialogs(mActivity);
		dialog.unableChasesDialog(t , c , b,
				new MyClickedListener() {
					
					@Override
					public void onClick() {
						if(1==i){
							replaceMiddle(LoginFragment.class);
						}else if(2==i){
							finish();//找回密码finsh()当前  直接回到登陆页
						}
						dialog.dismiss();
					}
				},false);
	}
	/**
	 * 修改密码
	 * 三个编辑
	 */
	private void affirmSubmit(int  cmd) {
		String ed1=originally_ed.getText().toString();//原密码
		String ed2=upwd_ed.getText().toString();
		String ed3=uagainpwd_ed.getText().toString();
		switch (cmd) {
				case 1007:
					if(analyze(ed1,ed2,ed3)){
//					    ok_btn.setEnabled(false);
						s1=DigestUtils.md5Hex(StringUtils.trimToNull(ed1));//原密码
						s2=DigestUtils.md5Hex(StringUtils.trimToNull(ed2));//新密码
						s3=DigestUtils.md5Hex(StringUtils.trimToNull(ed3));//新密码
						//	1007 修改密码  （手机验证完成后重新输入新的密码）
						//	A	验证码	
						//	B	密码(MD5)	
						params=new MessageJson();
						params.put("A", s1);//旧密码
						params.put("B", s2);//新密码
						submitData(1007, cmd, params);
				     }
					break;
				case 1009:
					
					if(analyze(ed2,ed3)){
//						ok_btn.setEnabled(false);
						s2=DigestUtils.md5Hex(StringUtils.trimToNull(ed2));//新密码
						s3=DigestUtils.md5Hex(StringUtils.trimToNull(ed3));//新密码
						
						//	1009 找回密码 （手机验证完成后重新输入新的密码）
						//	A	验证码	
						//	B	密码(MD5)
						params=new MessageJson();
						params.put("A", SPUtil.getString(getString(R.string.verify)));//的取出上一手机校验之后的验证码
						params.put("B", s2);
						submitData(1009, cmd, params);
						
					
					}
				break;

		default:
			break;
		}	
	}
	/**
	 *  * 判断
	 * @param pwd1
	 * @param pwd2
	 * @param pwd3
	 * @return
	 */
	private boolean analyze(String pwd1,String pwd2,String pwd3){
		if(StringUtils.isBlank(pwd1)){
			MyToast.showToast(mActivity,  getString(R.string.former_pwd_text));
			
			return false;
			//为空
		}else if(StringUtils.isBlank(pwd2) || StringUtils.isBlank(pwd3) ){
			MyToast.showToast(mActivity, getString(R.string.is_null_erro_text));
			
			return false;
			//密码对比
		}else if( pwd2.length() < 6 || pwd3.length() < 6 ){
			MyToast.showToast(mActivity, getString(R.string.is_length_erro_text));
			
			return false;
		}else if(!pwd2.equals(pwd3)){
			MyToast.showToast(mActivity, getString(R.string.register_show_Toast8));
			
			return false;
		}
//		 //网络环境 
//	    if(!NetUtil.checkNetWork(mActivity)){
//	       MyToast.showToastLong(mActivity, R.string.no_connect_server);
//	       return false;
//		 }
	    
	    
	    return true;
	}
	/**
	 * 判断
	 * @param pwd1
	 * @param pwd2
	 * @return
	 */
	private boolean analyze(String pwd2,String pwd3){
		if(StringUtils.isBlank(pwd2) || StringUtils.isBlank(pwd3) ){
			MyToast.showToast(mActivity, getString(R.string.is_null_erro_text));
			
			return false;
			//密码对比
		}else if(!pwd2.equals(pwd3)){
			MyToast.showToast(mActivity, getString(R.string.register_show_Toast8));
			
			return false ;
		}else if( pwd2.length() < 6 || pwd3.length() < 6 ){
			MyToast.showToast(mActivity, getString(R.string.is_length_erro_text));
			
			return false;
		}
//		//网络环境 
//		if(!NetUtil.checkNetWork(mActivity)){
//			MyToast.showToastLong(mActivity, R.string.no_connect_server);
//			return false;
//		}
		return true;
	}


}
