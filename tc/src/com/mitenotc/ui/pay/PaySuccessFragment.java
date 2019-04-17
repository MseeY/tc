package com.mitenotc.ui.pay;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.TCTimerHelper;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment2;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.BettingRecords;
import com.mitenotc.ui.account.BuyRedPacket;
import com.mitenotc.ui.account.ConsultationFragment;
import com.mitenotc.ui.account.MeFragment;
import com.mitenotc.ui.account.RechargeFragment;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

public class PaySuccessFragment extends BaseFragment implements OnClickListener{

	private TextView paysuccess_cashamount_tv;
	private TextView paysuccess_recharge_tv;
	private TextView paysuccess_desc_tv;
	
	private Button paysuccess_check_order_btn;
	private Button paysuccess_redpacket_btn;
	private Button paysuccess_rebug_btn;
	private ProgressBar paysuccess_processing_pb;
	private int cmd;
	private int resultState;
	private TCTimerHelper timer;
	private Bundle bundle;
	private TextView tv_acc_out_online_ask1;
//	private TextView tv_banquan_pay_success;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_pay_success);
		setTitleText(getString(R.string.title_nav_custom));
		initViews();
		setListeners();
		bundle = getMyBundle();
		cmd = getMyBundle().getInt(getString(R.string.cmd));
		resultState = getMyBundle().getInt("resultState");////System.out.println("resultState = " + resultState); ////System.out.println("bundle.getString(\"payMethod\") = "+bundle.getString("orderId"));
		if(resultState == 0){//支付成功的情况
			if("00".equals(bundle.getString("payMethod"))){// 使用账户支付, 一定是支付成功的,否则会被底层拦截, 并提示错误
				paysuccess_desc_tv.setText(getString(R.string.paysuccess_buy_0));
				paysuccess_check_order_btn.setText(getString(R.string.paysuccess_check_orders));
				hide(paysuccess_processing_pb);
//				MyApp.saveProbeMsg("账户支付充值成功","操作", "", System.currentTimeMillis()+"", "0", "");
			}else {//不使用账户支付
				show(paysuccess_processing_pb);
				paysuccess_desc_tv.setText(getString(R.string.paysuccess_progressing));
				if(cmd == 1107){//购彩支付,目前这里的处理和 账户支付是一样的. 但是需求还不明确
					paysuccess_check_order_btn.setText(R.string.paysuccess_check_orders);
//					MyApp.saveProbeMsg("购彩支付成功","请求-1107-success!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
				}else if(cmd == 1101){//充值
					paysuccess_check_order_btn.setText(R.string.paysuccess_goto_user);
//					MyApp.saveProbeMsg("普通充值成功","请求-1101-success!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
				}else if(cmd == 1105){//购买红包
					paysuccess_check_order_btn.setText(R.string.paysuccess_goto_user);
//					MyApp.saveProbeMsg("购买红包成功","请求-1105-success!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");

				}
				//开启 查询 订单 是否完成的 timer
				timer = new TCTimerHelper(new TCTimerHelper.TimerObserver(){
					@Override
					public void onUpdate() {
						sendRequest();
					}});
			}
		}else {//支付失败
			hide(paysuccess_processing_pb);
			paysuccess_check_order_btn.setText(getString(R.string.paysuccess_repay));//重新支付
			if(cmd == 1107){//购彩支付
				paysuccess_desc_tv.setText(getString(R.string.paysuccess_buy_1));
				paysuccess_check_order_btn.setText(R.string.paysuccess_repay);
//				MyApp.saveProbeMsg("购彩支付失败","请求-1107-fail!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"1", "购彩支付-fail",true);
			}else if(cmd == 1101){//充值
				paysuccess_desc_tv.setText(getString(R.string.paysuccess_recharge_1));
				paysuccess_rebug_btn.setText("完成充值");
				paysuccess_check_order_btn.setText("继续充值");
//				MyApp.saveProbeMsg("充值失败","请求-1101-fail!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"1", "充值支付-fail",true);
			}else if(cmd == 1105){//购买红包
				paysuccess_desc_tv.setText(getString(R.string.paysuccess_redpacket_1));
				paysuccess_check_order_btn.setText(R.string.paysuccess_goto_buy_redpacket);
//				MyApp.saveProbeMsg("购买红包失败","请求-1105-fail!", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"1", "购买红包支付-fail",true);
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(timer != null)
			timer.start();
	}
	@Override
	public void onPause() {
		super.onPause();
		if(timer != null)
			timer.stop();
	}
	
	@Override
	protected void initData() {}
	
	public void sendRequest(){//发送请求
		MessageJson json = new MessageJson();
		json.put("A", bundle.getString("orderId"));
		submitData(0, 1110, json);
	}
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean bean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1110:
			if("0".equals(bean.getC())){
				UserBean.getInstance().setAvailableMoney(Long.parseLong(bean.getD()));
				UserBean.getInstance().setAvailableCash(Long.parseLong(bean.getE()));
				UserBean.getInstance().setAvailableBalance(Long.parseLong(bean.getF()));
				AccountEnum.convertMessage(bean.getLIST());
				timer.stop();
				hide(paysuccess_processing_pb);
				if(cmd == 1107){//购彩支付,目前这里的处理和 账户支付是一样的. 但是需求还不明确
					paysuccess_desc_tv.setText(getString(R.string.paysuccess_buy_0));
					paysuccess_check_order_btn.setText(R.string.paysuccess_check_orders);
				}else if(cmd == 1101){//充值
					paysuccess_desc_tv.setText(getString(R.string.paysuccess_recharge_0));
					paysuccess_check_order_btn.setText(R.string.paysuccess_goto_user);
				}else if(cmd == 1105){//购买红包
					paysuccess_desc_tv.setText(getString(R.string.paysuccess_buy_0));
					paysuccess_check_order_btn.setText(R.string.paysuccess_goto_user);
				}
//				AccountUtils.showToast(mActivity, UserBean.getInstance().getAvailableMoney()+"");
			}
			break;
//		case 1100:
//			UserBean.getInstance().setAvailableMoney(Long.parseLong(bean.getC()));
//			UserBean.getInstance().setAvailableCash(Long.parseLong(bean.getD()));
//			UserBean.getInstance().setAvailableBalance(Long.parseLong(bean.getE()));
//			AccountEnum.convertMessage(bean.getLIST());
////			AccountUtils.showToast(mActivity, sting)
//			break;
		}

	}
	private void initViews() {
		tv_acc_out_online_ask1 = (TextView) findViewById(R.id.tv_acc_out_online_ask1);
//		tv_banquan_pay_success = (TextView) findViewById(R.id.tv_banquan_pay_success);
//		try {
//			if(!AppUtil.isEmpty(ConstantValue.copyright)){
//				tv_banquan_pay_success.setText(ConstantValue.copyright);
//			}else{
//				tv_banquan_pay_success.setVisibility(View.GONE);
//			}
//		} catch (Exception e) {
//			return;
//		}
		paysuccess_cashamount_tv = (TextView) findViewById(R.id.paysuccess_cashamount_tv);
		paysuccess_cashamount_tv.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
		paysuccess_recharge_tv = (TextView) findViewById(R.id.paysuccess_recharge_tv);
		paysuccess_desc_tv = (TextView) findViewById(R.id.paysuccess_desc_tv);
		
		paysuccess_check_order_btn = (Button) findViewById(R.id.paysuccess_check_order_btn);
//		paysuccess_redpacket_btn = (Button) findViewById(R.id.paysuccess_redpacket_btn);
		paysuccess_rebug_btn = (Button) findViewById(R.id.paysuccess_rebug_btn);
		
		paysuccess_processing_pb = (ProgressBar) findViewById(R.id.paysuccess_processing_pb);

	}
	private void setListeners() {
		tv_acc_out_online_ask1.setOnClickListener(this);
		paysuccess_check_order_btn.setOnClickListener(this);
//		paysuccess_redpacket_btn.setOnClickListener(this);
		paysuccess_recharge_tv.setOnClickListener(this);
		paysuccess_rebug_btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.paysuccess_check_order_btn://查看订单
			MyApp.backToTCActivity();
			if(resultState == 0){//支付成功的情况
				if("00".equals(bundle.getString("payMethod"))){// 使用账户支付, 一定是支付成功的,否则会被底层拦截, 并提示错误
					start(ThirdActivity.class,BettingRecords.class,null);
				}else {//不使用账户支付
					if(cmd == 1107){//购彩支付,目前这里的处理和 账户支付是一样的. 但是需求还不明确
						start(ThirdActivity.class,BettingRecords.class,null);
					}else if(cmd == 1101){//充值
//						GloableParams.bottom_check = BodyFragment2.class;//跳转到用户中心
//						CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.user);
						start(ThirdActivity.class, RechargeFragment.class, null);
					}else if(cmd == 1105){//购买红包
						CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.user);
//						GloableParams.bottom_check = BodyFragment2.class;//跳转到用户中心
					}
				}
			}else {//支付失败
				if(cmd == 1107){//购彩支付
					start(ThirdActivity.class,PayMoreway.class,bundle);
				}else if(cmd == 1101){//充值
					start(ThirdActivity.class,RechargeFragment.class,null);
				}else if(cmd == 1105){//购买红包
					start(ThirdActivity.class,BuyRedPacket.class,null);
				}
			}
			break;
		case R.id.paysuccess_rebug_btn://继续购买
			////System.out.println("MyApp.activityList.get(0)"+MyApp.activityList.get(0));
			
			MyApp.backToTCActivity();
			if(1107==cmd){
				CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.hall);
			}else if(1101==cmd){
			    CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.user);
			}
			
			break;
//		case R.id.paysuccess_redpacket_btn://购买红包
//			start(ThirdActivity.class,BuyRedPacket.class,null);
//			finish();
//			break;
		case R.id.paysuccess_recharge_tv://去充值
			finish();
			start(ThirdActivity.class,RechargeFragment.class,null);
			break;
		case R.id.tv_acc_out_online_ask1:
			start(ThirdActivity.class, ConsultationFragment.class, null);
			break;
		default:
			break;
		}
//		wenzi_chongzhi
	}
}
