package com.mitenotc.ui.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.ui.base.BaseFragment;

/**
 * 提现成功   2014-3-25 15:14:24
 * @author ymx
 *
 */

public class ExtractionSuccess extends BaseFragment implements OnClickListener{
	
	private TextView online_ask;  //在线咨询
	private Button bank_client;   //返回客户端

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_extraction_success);
		setTitleNav(R.string.title_nav_custom, R.drawable.title_nav_back,0);
		initView();
		setSuccessListener();
		
	}

	private void initView() {
		online_ask = (TextView) findViewById(R.id.tv_acc_fund_online_ask);
		bank_client = (Button) findViewById(R.id.bt_acc_fund_success_back);
	}

	private void setSuccessListener() {
		online_ask.setOnClickListener(this);
		bank_client.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//在线咨询
		case R.id.tv_acc_fund_online_ask:
			start(SecondActivity.class, ConsultationFragment.class, null);
			break;
		case R.id.bt_acc_fund_success_back:
			finish();
			break;
		}
	}
}
