package com.mitenotc.ui.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

public class AccountBuyRedPacSuccess extends BaseFragment implements OnClickListener{


	private ProgressBar paysuccess_processing_pb;
	private TextView paysuccess_desc_tv;
	private Button paysuccess_rebug_btn;
	private Button paysuccess_check_order_btn;
	private TextView paysuccess_recharge_tv;
	private TextView paysuccess_cashamount_tv;
	private TextView wenzi_chongzhi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_pay_success);
		setTitleNav("账户余额买红包", R.drawable.title_nav_back, 0);
		initView();
	}

	private void initView() {
		paysuccess_desc_tv= (TextView) findViewById(R.id.paysuccess_desc_tv);
		paysuccess_processing_pb = (ProgressBar) findViewById(R.id.paysuccess_processing_pb);
		paysuccess_processing_pb.setVisibility(View.GONE);
		paysuccess_desc_tv.setText("恭喜您,购买成功!");
		paysuccess_rebug_btn = (Button) findViewById(R.id.paysuccess_rebug_btn);
		paysuccess_check_order_btn = (Button) findViewById(R.id.paysuccess_check_order_btn);
		paysuccess_rebug_btn.setText("继续购买");
		paysuccess_check_order_btn.setText("立即购彩");
		paysuccess_rebug_btn.setOnClickListener(this);
		paysuccess_check_order_btn.setOnClickListener(this);
		wenzi_chongzhi = (TextView) findViewById(R.id.wenzi_chongzhi);
		
		paysuccess_recharge_tv = (TextView) findViewById(R.id.paysuccess_recharge_tv);
		paysuccess_recharge_tv.setVisibility(View.GONE);
		//当前的账户余额
		paysuccess_cashamount_tv = (TextView) findViewById(R.id.paysuccess_cashamount_tv);
		paysuccess_cashamount_tv.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//继续购买
		case R.id.paysuccess_rebug_btn:
			MyApp.backToTCActivity();
			start(ThirdActivity.class, AccountBuyRedPac.class, null);
			break;
		//立即购彩	
		case R.id.paysuccess_check_order_btn:
			MyApp.backToTCActivity();
			CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.hall);
			break;
		//在线客服
		case R.id.wenzi_chongzhi:
			start(ThirdActivity.class,ConsultationFragment.class , null);
			break;
		}
	}
}
