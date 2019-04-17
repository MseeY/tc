package com.mitenotc.ui.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.register.Registermain;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 
 * @类名称: LoginFragment
 * @类描述: 登陆的Fragment
 * @创建人：yuanmengxi
 * @创建时间：2013-12-29 下午22:02:18
 * @备注： 登陆页面
 * @version V2.1.0
 * 修改日期：2014-7-14 14:38:33
 */

public class LoginFragment extends BaseFragment implements OnClickListener,OnCheckedChangeListener{

	private EditText et_phone;
	private EditText et_password;
	private Button bt_login;
	private TextView tv_foget_pwd;
	private Button tv_free_regist;
	private LinearLayout ll_sec_login;
	private TextView tv_sec_phone;
	private TextView tv_change_account;
	private String usr;
	private int login_mode;//登入模式/ 区别是手动输入的账号登入还是sharedPreference 中的账号登入//0 为SharedPreference 1为手动输入
	private LinearLayout ll_login_phonenumber;
	private CheckBox cb_login_remember_pwd;
	private TextView inputType;
	private Dialog mDialog;
	private Animation shake;
	
	@Override
	protected void initData() {
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_activity_login);
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		if(mActivity.getClass().getName().contains("TCActivity")){//在第一个界面
			setTitleNav(R.string.user_login,0, 0);
		}else {//在其他的activity
			setTitleNav(R.string.user_login,R.drawable.title_nav_back, 0);
		}
		
		initView();
		setLoginListener();
		
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (UserBean.getInstance().isLogin()) {
			replaceMiddle(MeFragment.class);
		}else {
			if (isHadLogin()) {
				login_mode = 0;
				usr = SPUtil.getString(getString(R.string.USR));
				ll_sec_login.setVisibility(View.VISIBLE);
				ll_login_phonenumber.setVisibility(View.GONE);
				tv_sec_phone.setText(AccountUtils.phoneNumber(SPUtil.getString(MyApp.res.getString(R.string.USR))));

			}else {
				login_mode = 1;
			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		is_logining=0;
	}

	/** 查询SharedPreference是否有用户信息，如果有那么就不是第一次登陆。 */
	private boolean isHadLogin() {
		return !AppUtil.isEmpty(SPUtil.getString(MyApp.res.getString(R.string.USR)));
	}

	/** 初始化控件 */
	private void initView() {
		inputType = (TextView) findViewById(R.id.tv_change_input_type);
		et_phone = (EditText) findViewById(R.id.ymx_et_phonename);
		et_password = (EditText) findViewById(R.id.ymx_et_pwd);
		bt_login = (Button) findViewById(R.id.ymx_bt_login);
		tv_foget_pwd = (TextView) findViewById(R.id.ymx_tv_foget_pwd);
		tv_free_regist = (Button) findViewById(R.id.ymx_tv_free_regist);
		ll_sec_login = (LinearLayout) findViewById(R.id.ymx_ll_second_login);
		tv_sec_phone = (TextView) findViewById(R.id.ymx_tv_sec_phone);
		tv_change_account = (TextView) findViewById(R.id.ymx_tv_change_account);
		ll_login_phonenumber = (LinearLayout) findViewById(R.id.ll_login_phonenumber);
		
		cb_login_remember_pwd = (CheckBox) findViewById(R.id.cb_login_remember_pwd);
		
		boolean pwd_boolean = SPUtil.getBoolean(MyApp.res.getString(R.string.REMEMBER_PWD_BOOLEAN), false);
		if(pwd_boolean){
			cb_login_remember_pwd.setChecked(true);
		}else{
			cb_login_remember_pwd.setChecked(false);
		}
		
		if(!cb_login_remember_pwd.isChecked()){
			String pwd = SPUtil.getString(MyApp.res.getString(R.string.REMEMBER_PWD));
			if(!AppUtil.isEmpty(pwd)){
				et_password.setText(pwd);
				et_password.setCursorVisible(false);
			}else{
				et_password.setCursorVisible(true);
				et_password.setText("");
			}
		}else{
			et_password.setCursorVisible(true);
			et_password.setText("");
		}

	}
	
	/** 对控件设置监听事件 */
	private void setLoginListener() {
		inputType.setOnClickListener(this);
		bt_login.setOnClickListener(this);
		tv_foget_pwd.setOnClickListener(this);
		tv_free_regist.setOnClickListener(this); 
		tv_change_account.setOnClickListener(this);
		cb_login_remember_pwd.setOnCheckedChangeListener(this);
		et_password.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				et_password.setCursorVisible(true);
				return false;
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			SPUtil.putBoolean(MyApp.res.getString(R.string.REMEMBER_PWD_BOOLEAN),true);
		}else{
			SPUtil.putBoolean(MyApp.res.getString(R.string.REMEMBER_PWD_BOOLEAN),false);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//登陆按钮
		case R.id.ymx_bt_login:
	
			// 输入信息是否合法
			if (checkUserInfo()){
				bt_login.setText("登 录 中..");
				mDialog.show();
				Protocol.getInstance().setUSR(usr);
				Protocol.getInstance().setPSW(DigestUtils.md5Hex(et_password.getText().toString().trim()));
				sendRequest(1002);
			}
			
			break;
		//忘记密码
		case R.id.ymx_tv_foget_pwd:
			Bundle bundle=new Bundle();
			bundle.putInt(getString(R.string.cmd), 1009);
			start(ThirdActivity.class, ForgetPwd.class, bundle);
			break;
		//免费注册	
		case R.id.ymx_tv_free_regist:
			start(ThirdActivity.class, Registermain.class, null);
			break;
		//换个账号	
		case R.id.ymx_tv_change_account:
			login_mode = 1;
			ll_sec_login.setVisibility(View.GONE);
			ll_login_phonenumber.setVisibility(View.VISIBLE);
			et_phone.requestFocus();
			et_phone.setText("");
			et_password.setText("");
			break;
		case R.id.tv_change_input_type:
			
			if(et_phone.getInputType() == InputType.TYPE_CLASS_PHONE){
				et_phone.setHint(R.string.login_hint1);
				et_phone.setInputType(InputType.TYPE_CLASS_TEXT);
			}else{
				et_phone.setHint(R.string.login_hint);
				et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
			}
			break;
		}
	} 

	/** 获取网络数据 */
	private void sendRequest(int key) {
		
		sendRequest(key,et_password.getText().toString().trim());
	} 
	
	
	public void sendRequest(int key, String psw){
		MessageJson msg = new MessageJson();
		msg.put("A", DigestUtils.md5Hex(StringUtils.trimToNull(psw)));
		
		submitData(0, key, msg);
	}
	@Override
	protected void errorResult(Message msg) {
		bt_login.setText("登    录");
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
		Toast.makeText(mActivity, msg.obj+"", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void nullResult() {
		bt_login.setText("登    录");
	}
	
	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1002:
			if("0".equals(messageBean.getA())){
				
				String et_pwd = et_password.getText().toString().trim();
				
				SPUtil.putString(R.string.USR, usr);
				SPUtil.putString(R.string.SND, messageBean.getD());//用户SESSION
				SPUtil.putString(R.string.uname, messageBean.getC());//用户彩名
				
				SPUtil.putString(MyApp.res.getString(R.string.pwd), DigestUtils.md5Hex(
						StringUtils.trimToNull(et_pwd)));
				
				if(!cb_login_remember_pwd.isChecked()){
					SPUtil.putString(MyApp.res.getString(R.string.REMEMBER_PWD),et_pwd);
				}else{
					SPUtil.putString(MyApp.res.getString(R.string.REMEMBER_PWD),"");
				}
				
				UserBean.getInstance().setUserAccountState(messageBean.getF());
				//保存彩名
				UserBean.getInstance().setLotteryName(messageBean.getC());
				UserBean.getInstance().setUserRating(messageBean.getE());
				
				UserBean.getInstance().setAvailableMoney(Long.parseLong(messageBean.getG()));
				UserBean.getInstance().setAvailableCash(Long.parseLong(messageBean.getH()));
				UserBean.getInstance().setAvailableBalance(Long.parseLong(messageBean.getI()));
				AccountEnum.convertMessage(messageBean.getLIST());
				
				UserBean.getInstance().setPhoneNum(usr);//存储用户的电话号码
				
				MessageJson msg1 = new MessageJson();//同步系统设置中的checkbox
				msg1.put("A", "1");
				submitData(0, 1302, msg1);
				
				MessageJson msg2 = new MessageJson();//同步我的定制
				msg2.put("A", "3");
				submitData(1, 1302, msg2);
				MessageJson msg4 = new MessageJson();//同步排序彩种
				msg4.put("A", "6");
				submitData(2, 1302, msg4);
				
				MessageJson msg3 = new MessageJson();//同步支付时需要密码
				msg3.put("A", "5");
				submitData(0, 1304, msg3);
				
//				SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_FLAG), "0");
//				SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_BUY),"");
				
				if(!usr.equals(SPUtil.getString(MyApp.res.getString(R.string.USR)))){
					SPUtil.putString(MyApp.res.getString(R.string.USR), usr);
				}
				
				UserBean.getInstance().setLogin(true);
				//如果是支付或者充值界面过来  需要进行判断  再返回到支付或者充值交界面 
				//2014-7-11 利用了这一跳转回到了发现
				Bundle mbundle = getMyBundle();
				if(mbundle!=null){
					int cmd = mbundle.getInt(MyApp.res.getString(R.string.cmd));
					switch (cmd) {
					case 1107:
						finish();
						break;
					default:
						break;
					}
					return;
				}
				bt_login.setText("登    录");
				if(mDialog!=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				switch (Integer.parseInt(messageBean.getF())) {
				case 0:
					showDialog();
					break;
				case 1:
					finish();
					closeSoftInput();
					break;
				case 2:
					showDialog();
					break;
				case 3:
					finish();
					closeSoftInput();
					break;
				case 4:
					break;
				}
			}else{
				bt_login.setText("登    录");
			}
			
			break;
		case 1302:
			switch (msg.arg1) {
			case 0:
				//保存系统设置中的checkbox的状态
				if("0".equals(messageBean.getA())){
					JSONObject jsonObj;
					try {
						jsonObj = new JSONObject(messageBean.getC().toString());
						SPUtil.putBoolean(MyApp.res.getString(R.string.REMIND),jsonObj.getBoolean(MyApp.res.getString(R.string.REMIND)));
						SPUtil.putBoolean(MyApp.res.getString(R.string.SHAKE),jsonObj.getBoolean(MyApp.res.getString(R.string.SHAKE)));
						SPUtil.putBoolean(MyApp.res.getString(R.string.SOUND),jsonObj.getBoolean(MyApp.res.getString(R.string.SOUND)));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case 1:
				mList = messageBean.getLIST();
				if(mList != null && mList.size() != 0){
					for (MessageBean bean : mList) {
						CustomTagEnum.convertCustomTag(bean);
					}
				}
				String string = messageBean.getC();////System.out.println("messageBean.getC(); = " +messageBean.getC());
				if(!AppUtil.isEmpty(string) && !string.equals(SPUtil.getString(R.string.lottery_ids))){
					SPUtil.putString(R.string.custom_codes, string);
					System.out.println("1302--login---3----> " +messageBean.getC());
				}
				break;
			case 2:
//				1302---6---是不返回list
//				mList = messageBean.getLIST();
//				if(mList != null && mList.size() != 0){
//					for (MessageBean bean : mList) {
//						CustomTagEnum.convertCustomTag(bean);
//					}
//				}
				String string2 = messageBean.getC();
				if(!AppUtil.isEmpty(string2)){//登录后不比对更新用户的彩种排序字串
					SPUtil.putString(R.string.lottery_ids, string2);
					System.out.println("1302--login---6---> " +messageBean.getC());
				}
				break;
			}
			break;
		case 1304:
			if("0".equals(messageBean.getA())){
				if("0".equals(messageBean.getC())){
					SPUtil.putBoolean(MyApp.res.getString(R.string.PAYPWD1),false);
				}else{
					SPUtil.putBoolean(MyApp.res.getString(R.string.PAYPWD1),true);
				}
			}
			break;
		}
	}

	//关闭软键盘
	public void closeSoftInput(){
		InputMethodManager im1 = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE); 
		im1.hideSoftInputFromWindow(mActivity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}

	/** 用户信息的校验 */
	private boolean checkUserInfo() {
		if (0 == login_mode) {
			if (AppUtil.isEmpty(et_password.getText().toString().trim())) {
				et_password.startAnimation(shake);
				return false;
			}
			return true;
		} else {
			usr = et_phone.getText().toString().trim();
			
			if(TextUtils.isEmpty(usr)&&AppUtil.isEmpty(et_password.getText().toString().trim())){
				et_phone.startAnimation(shake);
				et_password.startAnimation(shake);
				if(mDialog!=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				return false;
			}
			
			if(TextUtils.isEmpty(usr)){
				et_phone.startAnimation(shake);
				return false;
			}
			
			if (AppUtil.isEmpty(et_password.getText().toString().trim())) {
				et_password.startAnimation(shake);
				return false;
			}
			
			return true;
		}
	}
	
	

	private void showDialog() {
		TCDialogs dialog = new TCDialogs(mActivity);
		dialog.setCancelable(false);
		ImageView dialog_title_right = dialog.getDialog_title_right();
		dialog_title_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				return;
			}
		});
		
		dialog.popBindNumber(new OkClickedListener() {
			@Override
			public void okClicked() {
				finish();
				Bundle mbBundle=new Bundle();
				mbBundle.putInt(getString(R.string.cmd), 1001);
				start(ThirdActivity.class, PhoneVerify.class, mbBundle);
			}
		}, new MyClickedListener() {// cancel 点击事件
					@Override
					public void onClick() {
						if(mActivity.getClass() == TCActivity.class){
							replaceMiddle(MeFragment.class);
						}else {
							finish();
						}
					}
				});
	}
	
}
