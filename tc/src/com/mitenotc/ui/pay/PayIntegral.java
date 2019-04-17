package com.mitenotc.ui.pay;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.BettingRecords;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 
 * @author ymx 2014-3-11 10:36:45 订单支付----- 积分支付
 */

public class PayIntegral extends BaseFragment implements OnClickListener {

	private TextView total_integral;
	private Button yes_pay;
	private TextView order_integral;
	private Bundle myBundle;
	private TextView pay_type;
	private TextView acc_title_text1;
	private TextView acc_title_text2;
	private TextView acc_title_content1;
	private TextView acc_title_content2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_order_pay);
		setTitleNav(R.string.acc_title_order_pay, R.drawable.title_nav_back, 0);
		initView();
		setIntegralListener();
	}

	private void initView() {
		acc_title_text1 = (TextView) findViewById(R.id.acc_title_text1);
		acc_title_text2 = (TextView) findViewById(R.id.acc_title_text2);
		acc_title_content1 = (TextView) findViewById(R.id.acc_title_content1);
		acc_title_content2 = (TextView) findViewById(R.id.acc_title_content2);
		acc_title_text1.setText("支付项目：");
		acc_title_text2.setText("订单金额：");
		total_integral = (TextView) findViewById(R.id.tv_order_total_integral);
		total_integral.setText(AccountUtils.fenToyuan(AccountEnum.point_account
				.getMoney() + ""));
		yes_pay = (Button) findViewById(R.id.bt_order_yes_pay);
		myBundle = getMyBundle();
		if (myBundle != null) {
			acc_title_content2.setText(AccountUtils.fenToyuan(myBundle
					.getString("money")) + "积分");
			String issue_text = MyApp.lotteryMap.get(myBundle
					.getString("lotteryId"))
					+ "第"
					+ "<font color=#007aff>"
					+ myBundle.getString("issue") + "</font>" + "期";
			acc_title_content1.setText(Html.fromHtml(issue_text));
		}
	}

	private void setIntegralListener() {
		yes_pay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 确认付款
		case R.id.bt_order_yes_pay:
			try {

				if (!"".equals(myBundle.getString("orderId"))
						&& !"".equals(myBundle.getString("money"))) {
					sendRequest(1107,
							AccountUtils.fenToyuan(myBundle.getString("money"))
									+ "", myBundle.getString("orderId"));
				} else {
					Toast.makeText(mActivity, "出问题啦", Toast.LENGTH_SHORT)
							.show();
				}

			} catch (Exception e) {
				return;
			}

			break;
		default:
			break;
		}

	}

	/*********************************** 请求网络阶段 ******************************/

	/** 获取网络数据 */
	private void sendRequest(int key, String integral, String orderId) {
		MessageJson msg = new MessageJson();
		msg.put("I", AccountUtils.yuanTofen(integral));
		msg.put("E", orderId);
		submitData(0, key, msg);
	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		if ("0".equals(messageBean.getA())) {
			Toast.makeText(mActivity, "付款成功", Toast.LENGTH_SHORT).show();
			UserBean.getInstance().setAvailableMoney(
					Long.parseLong(messageBean.getF()));
			UserBean.getInstance().setAvailableCash(
					Long.parseLong(messageBean.getG()));
			UserBean.getInstance().setAvailableBalance(
					Long.parseLong(messageBean.getH()));
			AccountEnum.convertMessage(messageBean.getLIST());
			start(ThirdActivity.class, BettingRecords.class, null);
			finish();
		}
	}
}
