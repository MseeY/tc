package com.mitenotc.ui.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.UserBean;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

public class AccountBuyRedPac extends BaseFragment {

	private EditText et_buy_redpac_money;
	private TextView tv_buy_redpac_money;
	private Button btn_next_buzhou;
	private Animation shake;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_account2redpac);
		setTitleNav("账户余额买红包", R.drawable.title_nav_back, 0);
		initView();
	}

	private void initView() {
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		//可用余额
		tv_buy_redpac_money = (TextView) findViewById(R.id.tv_buy_redpac_money);
		//编辑框
		et_buy_redpac_money = (EditText) findViewById(R.id.et_buy_redpac_money);
		
		tv_buy_redpac_money.setText("¥"+FormatUtil.moneyFormat2String(Double
				.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableCash()+""))));
		//下一步按钮
		btn_next_buzhou = (Button) findViewById(R.id.btn_next_buzhou);
		btn_next_buzhou.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str = et_buy_redpac_money.getText().toString().trim();
				if(AppUtil.isEmpty(str)){
					if(shake!=null){
						et_buy_redpac_money.startAnimation(shake);
					}
					return;
				}
				long parseLong = Long.parseLong(str+"00");
				long availableCash = UserBean.getInstance().getAvailableCash();
				if(parseLong > availableCash){
					Toast.makeText(mActivity, "输入的金额不能大于购买余额", 0).show();
					return;
				}
				
				if(str.startsWith("0")){
					Toast.makeText(mActivity,"购买金额不能为0元；",Toast.LENGTH_SHORT).show();
					et_buy_redpac_money.setText("");
					return;
				}
				
				Bundle bundle = new Bundle();
				bundle.putString("INPUTVALUE", str+"00");
				start(ThirdActivity.class, AccountBuyRedPacNext.class , bundle);
			}
		});
		
		et_buy_redpac_money.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				AccountUtils.changedEdittext(btn_next_buzhou, s, count);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
