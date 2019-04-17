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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 2014-5-5 15:46:15
 * 输入手机号的界面
 * @author ymx
 *
 */
public class PayPhoneRechargeInput extends BaseFragment implements OnClickListener{
	
	private EditText phoneNumber0;
	private EditText hasTelephone1;
	private TextView send_telephone2;
	private Button phone_recharge_next2;
	private Button right_now_send;
	
	private Bundle myBundle;
	
	private LinearLayout ll_phone_recharge_me;
	private LinearLayout ll_phone_recharge_other;
	private LinearLayout ll_phone_recharge_guhua;
	private ImageView iv_me;
	private ImageView iv_other;
	private ImageView iv_guhua;
	private LinearLayout phone_recharge_method;
	private static int methodMode = 3;
	private TextView recharge_text1;
	private TextView recharge_text2;
	private TextView recharge_text3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_phone_recharge_input);
		setTitleNav("话费抵扣红包", R.drawable.title_nav_back,0);
		initView();
	}
	
	private void initView() {
		myBundle = getMyBundle();
		phoneNumber0 = (EditText) findViewById(R.id.et_recharge_phonenumber0);
		hasTelephone1 = (EditText) findViewById(R.id.tv_has_recharge_telephone1);
		send_telephone2 = (TextView) findViewById(R.id.tv_send_telephone2);
		phone_recharge_next2 = (Button) findViewById(R.id.bt_phone_recharge_next2);
		right_now_send = (Button) findViewById(R.id.bt_right_now_send);
		phone_recharge_method = (LinearLayout) findViewById(R.id.ll_acc_phone_recharge_method);
		ll_phone_recharge_me = (LinearLayout) findViewById(R.id.ll_phone_recharge_me);
		ll_phone_recharge_other = (LinearLayout) findViewById(R.id.ll_phone_recharge_other);
		ll_phone_recharge_guhua = (LinearLayout) findViewById(R.id.ll_phone_recharge_guhua);
		ll_phone_recharge_me.setOnClickListener(this);
		ll_phone_recharge_other.setOnClickListener(this);
		ll_phone_recharge_guhua.setOnClickListener(this);
		iv_me = (ImageView) findViewById(R.id.iv_phone_recharge_me);
		iv_me.setImageResource(R.drawable.phone_pay_circle);
//		phoneNumber0.setHint("请输入您的本机号码");
		phoneNumber0.setHint("请输入区号+固定电话");
		methodMode = 3;
		iv_other = (ImageView) findViewById(R.id.iv_phone_recharge_other);
		iv_guhua = (ImageView) findViewById(R.id.iv_phone_recharge_guhua);
		recharge_text1 = (TextView) findViewById(R.id.recharge_text1);
		recharge_text1.setText("固话充值");
		recharge_text2 = (TextView) findViewById(R.id.recharge_text2);
		recharge_text2.setText("用本机充值");
		recharge_text2.setTextColor(Color.parseColor("#DDDDDD"));
		recharge_text3 = (TextView) findViewById(R.id.recharge_text3);
		recharge_text3.setText("用其他手机充值");
		recharge_text3.setTextColor(Color.parseColor("#DDDDDD"));
		
		initMode();
		phoneNumber0.setVisibility(View.VISIBLE);
		phone_recharge_next2.setOnClickListener(this);
		right_now_send.setOnClickListener(this);
		
		initButton();
		phone_recharge_next2.setVisibility(View.VISIBLE);
	}

	//初始化按钮的显示
	private void initButton() {
		phone_recharge_next2.setVisibility(View.GONE);
		right_now_send.setVisibility(View.GONE);
		
	}

	//初始化界面
	private void initMode() {
		phoneNumber0.setVisibility(View.GONE);
		hasTelephone1.setVisibility(View.GONE);
		send_telephone2.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//输入完手机号要点击的确定
		case R.id.bt_phone_recharge_next2:
			if(AppUtil.isEmpty(phoneNumber0.getText().toString().trim())){
				Toast.makeText(mActivity, "请输入手机号码", Toast.LENGTH_SHORT).show();
				return;
			}
				
			MessageJson msg = new MessageJson();
			msg.put("A",phoneNumber0.getText().toString());
			
			if(myBundle != null ||AppUtil.isEmpty(myBundle.toString()) ){
				msg.put("B", myBundle.getString(MyApp.res.getString(R.string.MoneyMode)));
			}
			
			submitData(0, 1112, msg);
			
			break;
		case R.id.ll_phone_recharge_me:
//			initCircle();
//			iv_me.setImageResource(R.drawable.phone_pay_circle);
//			phoneNumber0.setHint("请输入您的本机号码");
//			methodMode = 1;
//			iv_guhua.setImageResource(R.drawable.phone_pay_circle);
//			phoneNumber0.setHint("请输入区号+固定电话");
			methodMode = 3;
			break;
		case R.id.ll_phone_recharge_other:
//			initCircle();
			Toast.makeText(mActivity, "暂未开通，敬请期待!", Toast.LENGTH_SHORT).show();
//			iv_other.setImageResource(R.drawable.phone_pay_circle);
//			phoneNumber0.setHint("请输入其他手机号码");
//			methodMode = 2;
			break;
		case R.id.ll_phone_recharge_guhua:
//			initCircle();
			Toast.makeText(mActivity, "暂未开通，敬请期待!", Toast.LENGTH_SHORT).show();
//			iv_guhua.setImageResource(R.drawable.phone_pay_circle);
//			phoneNumber0.setHint("请输入区号+固定电话");
//			methodMode = 3;
			break;

		}
	}
	
	//初始化圆点
		private void initCircle(){
			iv_me.setImageResource(0);
			iv_other.setImageResource(0);
			iv_guhua.setImageResource(0);
		}
	
	@Override
	protected void onMessageReceived(Message msg) {
		final MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0:
			if("0".equals(messageBean.getA())){
//				String phoneNum = phoneNumber0.getText().toString();
				
				phoneNumber0.setVisibility(View.GONE);
				send_telephone2.setVisibility(View.VISIBLE);
				send_telephone2.setText(messageBean.getE());
				phone_recharge_next2.setVisibility(View.GONE);
				
				ll_phone_recharge_me.setClickable(false);
				ll_phone_recharge_other.setClickable(false);
				ll_phone_recharge_guhua.setClickable(false);
				recharge_text1.setTextColor(Color.parseColor("#DDDDDD"));
				recharge_text2.setTextColor(Color.parseColor("#DDDDDD"));
				recharge_text3.setTextColor(Color.parseColor("#DDDDDD"));
				
				switch (methodMode) {
				case 1://用本机充值
					right_now_send.setVisibility(View.VISIBLE);
					break;
				case 2://用其他手机充值
					right_now_send.setVisibility(View.GONE);
					break;
				case 3://用固话充值
					right_now_send.setVisibility(View.GONE);
					break;
				}
				
//				if("1".equals(messageBean.getH())){
//					
//					if(!phoneNum.equals(AppUtil.getLine1Number(mActivity))){
//						right_now_send.setVisibility(View.GONE);
//					}else{
//						right_now_send.setVisibility(View.VISIBLE);
//					}
//					
//				}else{
//					right_now_send.setVisibility(View.GONE);
//				}
				
				right_now_send.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//判断手机是否有sim卡
						TelephonyManager manager = (TelephonyManager)mActivity.getSystemService(Context.TELEPHONY_SERVICE);
						int simState = manager.getSimState();
						if(simState == 1){
							Toast.makeText(mActivity, "您还没有插入sim卡！", Toast.LENGTH_SHORT).show();
							return;
						}
						
			            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + messageBean.getG()));
			            mActivity.startActivity(intent);
					}
				});
				
			}
			break;
		default:
			break;
		}
	}
}
