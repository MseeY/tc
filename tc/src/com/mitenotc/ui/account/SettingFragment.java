package com.mitenotc.ui.account;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MemoryManager;
import com.mitenotc.utils.SPUtil;

/**
 * 系统设置
 * 
 * @author ymx 2014-3-11 10:41:42
 * 
 */
public class SettingFragment extends BaseFragment implements OnClickListener {

	private TextView setting_help;
	private Button update_pwd;
	private TextView check_verson;
	private TextView setting_about;
	private CheckBox cb_push_remind; // 推送时提醒
	private TextView cb_need_paypwd; // 订单支付需要密码
	// private CheckBox cb_frozen_money;//一次性冻结全款
	private CheckBox cb_shake; // 摇摇机选
	private CheckBox cb_sound; // 声音效果

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_setting);
//		setTitleNav("系统设置", R.drawable.title_nav_back, 0);
		setTitleNav(CustomTagEnum.setting.getId(),"系统设置", R.drawable.title_nav_back, R.drawable.title_nav_menu);
		initView();
		initSettingListener();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setTitleNav(CustomTagEnum.setting.getId(),"系统设置", R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,CustomTagEnum.setting.getId());
		setTitleNav(CustomTagEnum.setting.getId(),"系统设置", R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}

	private void initView() {

		setting_help = (TextView) findViewById(R.id.acc_setting_help);
//		update_pwd = (Button) findViewById(R.id.bt_acc_setting_update_pwd);
		check_verson = (TextView) findViewById(R.id.tv_acc_setting_check_verson);
		setting_about = (TextView) findViewById(R.id.tv_acc_setting_about);
		// findViewById checkbox
		cb_push_remind = (CheckBox) findViewById(R.id.cb_setting_push_remind);
//		cb_need_paypwd = (TextView) findViewById(R.id.cb_setting_need_paypwd);
		cb_shake = (CheckBox) findViewById(R.id.cb_setting_shake);
		cb_sound = (CheckBox) findViewById(R.id.cb_setting_sound);
		
		if ("".equals(SPUtil.getString(MyApp.res.getString(R.string.ALLCHECKBOX)))) {
			try {
				jsonObj.put(MyApp.res.getString(R.string.REMIND), false);
				jsonObj.put(MyApp.res.getString(R.string.SHAKE), false);
				jsonObj.put(MyApp.res.getString(R.string.SOUND), false);
				cb_push_remind.setChecked(false);
				cb_shake.setChecked(false);
				cb_sound.setChecked(false);
				
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}

		if (!"".equals(SPUtil.getBoolean(MyApp.res.getString(R.string.REMIND),true))) {
			
			if (SPUtil.getBoolean(MyApp.res.getString(R.string.REMIND), true)) {
				cb_push_remind.setChecked(false);
			} else {
				cb_push_remind.setChecked(true);
			}
		}

		if (!"".equals(SPUtil.getBoolean(MyApp.res.getString(R.string.SHAKE),
				true))) {
			if (SPUtil.getBoolean(MyApp.res.getString(R.string.SHAKE), true)) {
				cb_shake.setChecked(false);
			} else {
				cb_shake.setChecked(true);
			}
		}

		if (!"".equals(SPUtil.getBoolean(MyApp.res.getString(R.string.SOUND),
				true))) {
			if (SPUtil.getBoolean(MyApp.res.getString(R.string.SOUND), true)) {
				cb_sound.setChecked(false);
			} else {
				cb_sound.setChecked(true);
			}
		}

		setCheckBoxTOSp();
	}

	JSONObject jsonObj = new JSONObject();

	private void setCheckBoxTOSp() {
		cb_push_remind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						try {
							
							if(cb_shake.isChecked()){
								jsonObj.put(MyApp.res.getString(R.string.SHAKE), false);
							}else{
								jsonObj.put(MyApp.res.getString(R.string.SHAKE), true);
							}
							if(cb_sound.isChecked()){
								jsonObj.put(MyApp.res.getString(R.string.SOUND), false);
							}else{
								jsonObj.put(MyApp.res.getString(R.string.SOUND), true);
							}
							
							if (isChecked) {
								
								SPUtil.putBoolean(MyApp.res.getString(R.string.REMIND), false);
								jsonObj.put(MyApp.res.getString(R.string.REMIND), false);
								MessageJson msg = new MessageJson();
								msg.put("A", "1");
								msg.put("B", jsonObj.toString());
								submitData(0, 1303, msg);
							} else {
						
								SPUtil.putBoolean(MyApp.res.getString(R.string.REMIND), true);
								jsonObj.put(MyApp.res.getString(R.string.REMIND), true);
								MessageJson msg = new MessageJson();
								msg.put("A", "1");
								msg.put("B", jsonObj.toString());
								submitData(0, 1303, msg);
							}
						
						} catch (NotFoundException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

		cb_shake.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				try {
					
					if(cb_push_remind.isChecked()){
						jsonObj.put(MyApp.res.getString(R.string.REMIND), false);
					}else{
						jsonObj.put(MyApp.res.getString(R.string.REMIND), true);
					}
					
					if(cb_sound.isChecked()){
						jsonObj.put(MyApp.res.getString(R.string.SOUND), false);
					}else{
						jsonObj.put(MyApp.res.getString(R.string.SOUND), true);
					}
					
					if (isChecked) {
						SPUtil.putBoolean(MyApp.res.getString(R.string.SHAKE),false);
						jsonObj.put(MyApp.res.getString(R.string.SHAKE), false);
						MessageJson msg = new MessageJson();
						msg.put("A", "1");
						msg.put("B", jsonObj.toString());
						submitData(0, 1303, msg);
					} else {
						
						SPUtil.putBoolean(MyApp.res.getString(R.string.SHAKE),true);
						jsonObj.put(MyApp.res.getString(R.string.SHAKE), true);
						MessageJson msg = new MessageJson();
						msg.put("A", "1");
						msg.put("B", jsonObj.toString());
						submitData(0, 1303, msg);
					}
				
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		cb_sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				try {
					
					if(cb_push_remind.isChecked()){
						jsonObj.put(MyApp.res.getString(R.string.REMIND), false);
					}else{
						jsonObj.put(MyApp.res.getString(R.string.REMIND), true);
					}
					if(cb_shake.isChecked()){
						jsonObj.put(MyApp.res.getString(R.string.SHAKE), false);
					}else{
						jsonObj.put(MyApp.res.getString(R.string.SHAKE), true);
					}
					if (isChecked) {
						
						SPUtil.putBoolean(MyApp.res.getString(R.string.SOUND),false);
						jsonObj.put(MyApp.res.getString(R.string.SOUND), false);
						MessageJson msg = new MessageJson();
						msg.put("A", "1");
						msg.put("B", jsonObj.toString());
						submitData(0, 1303, msg);
					} else {
						
						SPUtil.putBoolean(MyApp.res.getString(R.string.SOUND),true);
						jsonObj.put(MyApp.res.getString(R.string.SOUND), true);
						MessageJson msg = new MessageJson();
						msg.put("A", "1");
						msg.put("B", jsonObj.toString());
						submitData(0, 1303, msg);
					}
				
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initSettingListener() {
		setting_help.setOnClickListener(this);
//		update_pwd.setOnClickListener(this);
		check_verson.setOnClickListener(this);
		setting_about.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 帮助中心
		case R.id.acc_setting_help:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			start(ThirdActivity.class, HelpSenter.class, null);
			break;

		// 检测新版本
		case R.id.tv_acc_setting_check_verson:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			
			if(GloableParams.isLoading == true){
				Toast.makeText(mActivity, "正在下载中,请稍后..", Toast.LENGTH_SHORT).show();
				return;
			}
			
			sendRequest(1355, null);
			break;
		// 关于我们
		case R.id.tv_acc_setting_about:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			start(ThirdActivity.class, AboutFragment.class, null);
			break;

		}
	}

	/***************************** 请求网络阶段 *****************************************/

	/** 获取网络数据 */
	private void sendRequest(int key, JSONObject jsonObj) {
		switch (key) {
		case 1355:
			MessageJson msg1 = new MessageJson();
			submitData(0, key, msg1);
			break;
		}

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		GloableParams.isLoading = false;
	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		switch (msg.arg2) {
		case 1355:
			final MessageBean messageBean = (MessageBean) msg.obj;
			if ("0".equals(messageBean.getA())) {
				TCDialogs t = new TCDialogs(MyApp.activityList.get(1));
				t.popHadNewVersion(new OkClickedListener() {
					
					@Override
					public void okClicked() {
						
						if(!MemoryManager.externalMemoryAvailable()||MemoryManager.getAvailableExternalMemorySize() < 5.5 * 1024 * 1024){
							Toast.makeText(mActivity, "无SD卡或SD卡容量不足！", Toast.LENGTH_SHORT).show();
							return;
						}
						
						DownLoadUtil dlu = new DownLoadUtil(MyApp.activityList.get(1));
						dlu.createDialog(messageBean.getD());
						dlu.createNotification();
					}
				}, messageBean.getE());
			} else if ("15001".equals(messageBean.getA())) {
				Toast.makeText(mActivity, "已是当前最新版本", Toast.LENGTH_SHORT).show();
			}
			break;

		case 1303:
			MessageBean msg1 = (MessageBean) msg.obj;
			if("0".equals(msg1.getA())){
				SPUtil.putString(MyApp.res.getString(R.string.ALLCHECKBOX),jsonObj.toString());
			}
			break;
		}
	}
}
