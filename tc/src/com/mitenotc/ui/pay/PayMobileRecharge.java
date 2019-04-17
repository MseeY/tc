package com.mitenotc.ui.pay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 2014-7-29 13:50:54
 * @author ymx
 * 移动话费充值
 */

public class PayMobileRecharge extends BaseFragment implements OnClickListener{
	
	private LinearLayout unicom_view;
	private LinearLayout money9; // 15元
	private LinearLayout money15; // 30元
	private LinearLayout money30; // 45元
	private LinearLayout money60; // 90元
	private LinearLayout money_other;
	private ImageView iv_9;
	private ImageView iv_15;
	private ImageView iv_30;
	private ImageView iv_60;
	private ImageView iv_other;
	private Button bt_next;
	private static String moneyMode = "1500";
	private TextView moneyText;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_phone_recharge);
		setTitleNav("移动话费抵扣红包", R.drawable.title_nav_back,0);
		initView();
		
	}

	private void initView() {
		otherMoneys = MyApp.res.getStringArray(R.array.other_money);
		unicom_view = (LinearLayout) findViewById(R.id.ll_unicom_view);
		unicom_view.setVisibility(View.GONE);
		money9 = (LinearLayout) findViewById(R.id.ll_mobile_recharge_9);
		money15 = (LinearLayout) findViewById(R.id.ll_mobile_recharge_15);
		money30 = (LinearLayout) findViewById(R.id.ll_mobile_recharge_30);
		money60 = (LinearLayout) findViewById(R.id.ll_mobile_recharge_60);
		money_other = (LinearLayout) findViewById(R.id.ll_mobile_recharge_other);
		money9.setOnClickListener(this);
		money15.setOnClickListener(this);
		money30.setOnClickListener(this);
		money60.setOnClickListener(this);
		money_other.setOnClickListener(this);
		iv_9 = (ImageView) findViewById(R.id.iv_mobile_recharge_9);
		iv_9.setImageResource(R.drawable.phone_pay_circle);
		iv_15 = (ImageView) findViewById(R.id.iv_mobile_recharge_15);
		iv_30 = (ImageView) findViewById(R.id.iv_mobile_recharge_30);
		iv_60 = (ImageView) findViewById(R.id.iv_mobile_recharge_60);
		iv_other = (ImageView) findViewById(R.id.iv_mobile_recharge_other);
		moneyText = (TextView) findViewById(R.id.tv_mobile_other_money);
		bt_next = (Button) findViewById(R.id.bt_phone_recharge_next);
		bt_next.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_mobile_recharge_9:
			initCircle();
			iv_9.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "1500";
			break;
		case R.id.ll_mobile_recharge_15:
			initCircle();
			iv_15.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "3000";
			break;
		case R.id.ll_mobile_recharge_30:
			initCircle();
			iv_30.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "4500";
			break;
		case R.id.ll_mobile_recharge_60:
			initCircle();
			iv_60.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "9000";
			break;
		case R.id.ll_mobile_recharge_other:
			iv_9.setImageResource(0);
			iv_15.setImageResource(0);
			iv_30.setImageResource(0);
			iv_60.setImageResource(0);
			iv_other.setImageResource(R.drawable.phone_pay_circle);
			moneyMode = "300";
			moneyText.setText("3元");
			showOtherMoneyDialog();
			break;
		case R.id.bt_phone_recharge_next:
			Bundle bundle = new Bundle();
			bundle.putString(MyApp.res.getString(R.string.MoneyModeOther),moneyMode);
			start(ThirdActivity.class,PayMobileRechargeInput.class,bundle);
			break;
		}
	}
	
	private String[] otherMoneys;
	private PopupWindow popup;
	
	private void showOtherMoneyDialog() {
		
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(true);
		
		mAdapter = new MyOutherMoneyAdapter();
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String money = otherMoneys[position];
				moneyText.setText(money);
				moneyMode = AccountUtils.yuanTofen(money.substring(0, money.length()-1));
				popup.dismiss();
			}
		});
		
		popup = new PopupWindow(lv, money_other.getWidth(),AppUtil.getDisplayHeight(mActivity) / 3);
		AccountUtils.configPopupWindow(money_other, popup);
		
	}
	
	/** 银行卡的适配器 */
	private class MyOutherMoneyAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			return otherMoneys.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewOtherMoneyHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,R.layout.m_acc_me_safe_item, null);
				holder = new ViewOtherMoneyHolder();
				holder.tv_textMoney = (TextView) convertView.findViewById(R.id.tv_listview_item_number);
				convertView.setTag(holder);
			} else {
				holder = (ViewOtherMoneyHolder) convertView.getTag();
			}
			
			holder.tv_textMoney.setText(otherMoneys[position]);
			return convertView;
		}
	}
	
	class ViewOtherMoneyHolder {
		TextView tv_textMoney;
	}
	
	//初始化圆点
	private void initCircle(){
		iv_9.setImageResource(0);
		iv_15.setImageResource(0);
		iv_30.setImageResource(0);
		iv_60.setImageResource(0);
		iv_other.setImageResource(0);
		moneyText.setText("其他");
	}

}
