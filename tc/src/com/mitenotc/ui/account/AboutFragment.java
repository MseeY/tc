package com.mitenotc.ui.account;

import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.R;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.MemoryManager;
import com.mitenotc.utils.AppUtil;

public class AboutFragment extends BaseFragment implements OnClickListener {
	
	private TextView verson_number;
	private Button check_verson;
	private LinearLayout cefu_phoneNumber;
	private TextView about_phoneNumber;
	private TextView acc_text_banquan;
	private TextView acc_text_guanwang;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_setting_about);
		setTitleNav(R.string.acc_title_about, R.drawable.title_nav_back,0);
		initView();
		setAboutListener();
	}

	private void initView() {
		verson_number = (TextView) findViewById(R.id.tv_acc_setting_about_verson_number);
		verson_number.setText(AppUtil.getVersion(mActivity));
		check_verson = (Button) findViewById(R.id.bt_acc_setting_about_check_verson);
		cefu_phoneNumber = (LinearLayout) findViewById(R.id.ll_about_cefu_phoneNumber);
		about_phoneNumber = (TextView) findViewById(R.id.tv_about_phoneNumber);
		acc_text_guanwang = (TextView) findViewById(R.id.acc_text_guanwang);
		acc_text_banquan = (TextView) findViewById(R.id.acc_text_banquan);

		if(!AppUtil.isEmpty(ConstantValue.copyright)){
			((TextView) findViewById(R.id.copyright_tv)).setText(ConstantValue.copyright);//版权
			acc_text_banquan.setVisibility(View.VISIBLE);
		}else{
			acc_text_banquan.setVisibility(View.GONE);
			findViewById(R.id.about_logo3).setVisibility(View.GONE);
		}
		
		if(!AppUtil.isEmpty(ConstantValue.officialWebsite)){
			((TextView) findViewById(R.id.tv_acc_about_address)).setText(ConstantValue.officialWebsite);//http://m.mitenotc.com 官网
			acc_text_guanwang.setVisibility(View.VISIBLE);
		}else{
			acc_text_guanwang.setVisibility(View.GONE);
		}
		
		if(AppUtil.isEmpty(ConstantValue.serviceTel_format)){
			cefu_phoneNumber.setVisibility(View.GONE);
		}else{
			findViewById(R.id.ll_about_cefu_phoneNumber).setVisibility(View.VISIBLE);
			about_phoneNumber.setText(Html.fromHtml("<u>"+ConstantValue.serviceTel_format1+"</u>"));//400-0328-666电话
		}
//		about_phoneNumber.setText(Html.fromHtml("<u>"+MyApp.res.getString(R.string.payment_phone_number1)+"</u>"));
		
	}

	private void setAboutListener() {
		check_verson.setOnClickListener(this);
		cefu_phoneNumber.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_acc_setting_about_check_verson:
			sendRequest(1355);
			break;
		case R.id.ll_about_cefu_phoneNumber:
//			AccountUtils.boda(mActivity, MyApp.res.getString(R.string.payment_phone_number));
			if(!AppUtil.isEmpty(ConstantValue.serviceTel)){
				AccountUtils.boda(mActivity, ConstantValue.serviceTel);//4000328666 电话
			}
			break;
		}
	}
	
/*****************************请求网络阶段*****************************************/	
	
	/** 获取网络数据 */
	private void sendRequest(int key) {
		MessageJson msg = new MessageJson();
		submitData(0, key, msg);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		GloableParams.isLoading = false;
	}
	
	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		final MessageBean messageBean = (MessageBean) msg.obj;
		if("0".equals(messageBean.getA())){
			TCDialogs t = new TCDialogs(mActivity);
			t.popHadNewVersion(new OkClickedListener() {
				@Override
				public void okClicked() {
					
					if(!MemoryManager.externalMemoryAvailable()||MemoryManager.getAvailableExternalMemorySize() < 5.5 * 1024 * 1024){
						Toast.makeText(mActivity, "无SD卡或SD卡容量不足！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					DownLoadUtil dlu = new DownLoadUtil(mActivity);
					dlu.createDialog(messageBean.getD());
					dlu.createNotification();
				
				}
			}, messageBean.getE());
		}else if("15001".equals(messageBean.getA())){
			Toast.makeText(mActivity, "已是当前最新版本", Toast.LENGTH_SHORT).show();
		}
	}
}