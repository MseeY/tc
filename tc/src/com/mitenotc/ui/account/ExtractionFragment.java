package com.mitenotc.ui.account;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.FormatUtil;

public class ExtractionFragment extends BaseFragment implements OnClickListener{
	
	private Button btn_extraction_to_bank;
	private Button btn_extraction_to_red_pac;
	private TextView tv_extra_cash;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_extraction_out);
		setTitleNav("资金转出", R.drawable.title_nav_back,0);
		initView();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		MessageJson msg = new MessageJson();
		submitData(0, 1100, msg);
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_extraction_to_bank = (Button) findViewById(R.id.btn_extraction_to_bank);
		btn_extraction_to_bank.setOnClickListener(this);
		
		btn_extraction_to_red_pac = (Button) findViewById(R.id.btn_extraction_to_red_pac);
		btn_extraction_to_red_pac.setOnClickListener(this);
		
		tv_extra_cash = (TextView) findViewById(R.id.tv_extra_cash);
		tv_extra_cash.setText("¥"+FormatUtil.moneyFormat2String(Double
				.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableCash()+""))));
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_extraction_to_bank:
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}
			start(ThirdActivity.class, ExtractionCash.class, null);
			break;
        case R.id.btn_extraction_to_red_pac:

        	if(AccountUtils.isFastClick(mActivity)){
				return;
			}
        	start(ThirdActivity.class, AccountBuyRedPac.class, null);
			break;
		}
		
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean bean = (MessageBean) msg.obj;
		if("0".equals(bean.getA())){
			tv_extra_cash.setText("¥"+FormatUtil.moneyFormat2String(Double
					.parseDouble(AccountUtils.fenToyuan(bean.getD()))));
			UserBean.getInstance().setAvailableMoney(Long.parseLong(bean.getC()));
			UserBean.getInstance().setAvailableCash(Long.parseLong(bean.getD()));
			UserBean.getInstance().setAvailableBalance(Long.parseLong(bean.getE()));
			AccountEnum.convertMessage(bean.getLIST());
		}
	}

}
