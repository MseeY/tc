package com.mitenotc.ui.pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

public class PayMobileRechargeInput extends BaseFragment implements OnClickListener{
	
	private LinearLayout isGone;
	private TextView promptText;
	private Button isGone1;
	private EditText recharge_phonenumber0;
	private Button bt_phone_recharge;
	private Bundle myBundle;
	private static boolean buttonMode; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_phone_recharge_input);
		setTitleNav("移动话费抵扣红包", R.drawable.title_nav_back,0);
		buttonMode = false;
		initView();
	}

	private void initView() {
		
		isGone = (LinearLayout) findViewById(R.id.ll_acc_phone_recharge_method);
		isGone1 = (Button) findViewById(R.id.bt_right_now_send);
		promptText = (TextView) findViewById(R.id.tv_send_telephone2);
		isGone.setVisibility(View.GONE);
		isGone1.setVisibility(View.GONE);
		promptText.setVisibility(View.GONE);
		recharge_phonenumber0 = (EditText) findViewById(R.id.et_recharge_phonenumber0);
		recharge_phonenumber0.setHint("请输入您的手机号 ");
		bt_phone_recharge = (Button) findViewById(R.id.bt_phone_recharge_next2);
		bt_phone_recharge.setOnClickListener(this);
		myBundle = getMyBundle();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_phone_recharge_next2:
			if(buttonMode){
				MyApp.backToTCActivity();
				CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.user);
				return;
			}
			
			String money = myBundle.getString(MyApp.res.getString(R.string.MoneyModeOther));
			String phoneNumber = recharge_phonenumber0.getText().toString().trim();
			
			if(AppUtil.isEmpty(money)){
				Toast.makeText(mActivity, "参数异常", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(AppUtil.isEmpty(phoneNumber)){
				Toast.makeText(mActivity, "手机号码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//请求服务器
			MessageJson msg = new MessageJson();
			msg.put("A",phoneNumber);
			msg.put("B",money);
			submitData(0, 1117, msg);
			
			break;
		}
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0:
			if("0".equals(messageBean.getA())){
				recharge_phonenumber0.setVisibility(View.GONE);
				promptText.setVisibility(View.VISIBLE);
				promptText.setText("订单提交成功，请注意查收并回复短信完成充值。");
				bt_phone_recharge.setText("完 成 本 次 操 作");
				buttonMode = true;
			}
			
			break;
		}
	}
}
