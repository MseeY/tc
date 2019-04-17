package com.mitenotc.ui.pay;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;

/**
 * ymx
 * 2014-5-5 10:17:06
 * 手机充值
 */

public class PayPhoneRecharge extends BaseFragment implements OnClickListener{

	private LinearLayout money3;
	private LinearLayout money15;
	private LinearLayout money30;
	private ImageView iv_3;
	private ImageView iv_15;
	private ImageView iv_30;
	private Button bt_next;
	private static String moneyMode = "3";
	private LinearLayout mobile_view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_phone_recharge);
		setTitleNav("联通话费抵扣红包", R.drawable.title_nav_back,0);
		initView();
	}

	private void initView() {
		mobile_view = (LinearLayout) findViewById(R.id.ll_mobile_view);
		mobile_view.setVisibility(View.GONE);
		
		money3 = (LinearLayout) findViewById(R.id.ll_phone_recharge_3);
		money15 = (LinearLayout) findViewById(R.id.ll_phone_recharge_15);
		money30 = (LinearLayout) findViewById(R.id.ll_phone_recharge_30);
		money3.setOnClickListener(this);
		money15.setOnClickListener(this);
		money30.setOnClickListener(this);
		iv_3 = (ImageView) findViewById(R.id.iv_phone_recharge_3);
		iv_3.setImageResource(R.drawable.phone_pay_circle);
		iv_15 = (ImageView) findViewById(R.id.iv_phone_recharge_15);
		iv_30 = (ImageView) findViewById(R.id.iv_phone_recharge_30);
		bt_next = (Button) findViewById(R.id.bt_phone_recharge_next);
		bt_next.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_phone_recharge_3:
			initCircle();
			iv_3.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "3";
			break;
		case R.id.ll_phone_recharge_15:
			initCircle();
			iv_15.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "2";
			break;
		case R.id.ll_phone_recharge_30:
			initCircle();
			iv_30.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "1";
			break;
		case R.id.bt_phone_recharge_next:
			Bundle bundle = new Bundle();
			bundle.putString(MyApp.res.getString(R.string.MoneyMode),moneyMode);
			start(ThirdActivity.class,PayPhoneRechargeInput.class,bundle);
			break;
		}
	}
	
	//初始化圆点
	private void initCircle(){
		iv_3.setImageResource(0);
		iv_15.setImageResource(0);
		iv_30.setImageResource(0);
	}
}
