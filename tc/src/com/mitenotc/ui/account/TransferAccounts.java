package com.mitenotc.ui.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;

/**
 * 大银行转账
 * @author ymx
 *2014-3-17 11:08:36
 */
public class TransferAccounts extends BaseFragment implements OnClickListener {
	
	private LinearLayout bank_explain;
	private LinearLayout bank_ask;
	private LinearLayout bank_text;
	private TextView tv_btn_tikuan_shuoming;
	private LinearLayout fund_extraction_text;
	private ScrollView sv_big_bank;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_big_bank);
		setTitleNav(R.string.acc_big_accounts,R.drawable.title_nav_back,0);
		initView();
	}

	private void initView() {
		tv_btn_tikuan_shuoming = (TextView) findViewById(R.id.tv_btn_tikuan_shuoming);
		tv_btn_tikuan_shuoming.setOnClickListener(this);
		sv_big_bank = (ScrollView) findViewById(R.id.sv_big_bank);
		fund_extraction_text = (LinearLayout) findViewById(R.id.ll_acc_fund_extraction_text);
		bank_ask = (LinearLayout) findViewById(R.id.ll_big_bank_ask);
		bank_ask.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_btn_tikuan_shuoming:
			switch (fund_extraction_text.getVisibility()) {
			case View.VISIBLE:
				fund_extraction_text.setVisibility(View.GONE);
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_big_bank.fullScroll(ScrollView.FOCUS_UP);
				    }
				});
				break;
			case View.GONE:
				fund_extraction_text.setVisibility(View.VISIBLE);
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_big_bank.fullScroll(ScrollView.FOCUS_DOWN);
				    }
				});
				break;
			}
			
			break;
		case R.id.ll_big_bank_ask:
			start(ThirdActivity.class, ConsultationFragment.class, null);
			break;
		}
	}
}
