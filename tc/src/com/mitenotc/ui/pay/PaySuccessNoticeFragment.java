package com.mitenotc.ui.pay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.account.BettingRecords;
import com.mitenotc.ui.account.ConsultationFragment;
import com.mitenotc.ui.base.BaseFragment;

public class PaySuccessNoticeFragment extends BaseFragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_success_notice);
		hideTitle();//隐藏头部
		View pay_success_btn = findViewById(R.id.pay_success_btn);
		View pay_problem_btn = findViewById(R.id.pay_problem_btn);
		pay_success_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start(ThirdActivity.class,ConsultationFragment.class,null);
				finish();
			}
		});
		pay_problem_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start(ThirdActivity.class,BettingRecords.class,null);
				finish();
			}
		});
	}
}
