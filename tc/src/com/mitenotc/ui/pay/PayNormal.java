package com.mitenotc.ui.pay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.ForgetPwd;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

public class PayNormal extends BasePayFragment implements OnClickListener{
	
	private Bundle bundle;
	
	private long avalible_amount;//可用金额
	private long red_packet_amount;//红包账户金额
	private long payment_amount;//需要支付的总金额
	private long cash_payment;//现金账号支付的金额. 现金账号支付的金额+红包账户支付的金额  =  账户余额 支付的金额
	private long other_payment;//其他支付的金额
	private long red_packet_payment;
	private long available_money;//现金(不包含红包)
	private String selected_pay_method_code;
	private boolean is_account_remain_enough;
	private LinearLayout ll_pay_other_method; //支付的其他方式
	private TextView tv_pay_play_issue;
	private TextView tv_pay_order_money;
	private LinearLayout ll_pay_need_pwd;//订单支付是需要密码 控制隐藏和显示
	private TextView show_amount;
	private TextView show_available_amount;
	private TextView orderpay_forget_pwd;
	private TextView show_other_amount;
	private Button payorder_on_pay;
	private EditText input_pwd;
	private LinearLayout other_pay_methods;
	private TCDialogs dialog;
	private ListView payMethodslist;
	private TypedArray imgs;
	private String[] pay_methods;
	private String[] pay_methods_ex;
	private TextView chose_other_paymethods;
	private TextView chose_methods_text;
	private TextView chose_methods_ex;
	private ImageView img_pay_methods;

	private TextView acc_text1;
	private TextView acc_text2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("订单支付");
		initMyBundleDatas();
		pay_methods = MyApp.res.getStringArray(R.array.pay_methods);
		pay_methods_ex = MyApp.res.getStringArray(R.array.pay_methods_ex);
		imgs = MyApp.res.obtainTypedArray(R.array.pay_methods_drawable);
		selected_pay_method_code = "00";
		if(avalible_amount <=0){
			replaceMiddle(PayMoreway.class);
		}
		setContentView(R.layout.pay_normal);
		initView();
		setListen();
		hideRightIcon();//隐藏定制按钮
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(imgs!=null){
			imgs.recycle();
		}
	}
	
	private void setListen() {
		show_amount.setOnClickListener(this);
		orderpay_forget_pwd.setOnClickListener(this);
		payorder_on_pay.setOnClickListener(this);
		other_pay_methods.setOnClickListener(this);
	}

	private void initView() {
		initfindId();
		initLayout();//初始化界面
		initPaymentHead();//初始化头部参数
		initConfigInfo();//初始化配置信息
		initPayment();
	}

	private void initfindId() {
		ll_pay_other_method = (LinearLayout) findViewById(R.id.ll_pay_other_method);
		acc_text1 = (TextView) findViewById(R.id.acc_title_text1);
		acc_text2 = (TextView) findViewById(R.id.acc_title_text2);
		acc_text1.setText("支付项目：");
		acc_text2.setText("订单金额：");
		tv_pay_play_issue = (TextView) findViewById(R.id.acc_title_content1);
		tv_pay_order_money = (TextView) findViewById(R.id.acc_title_content2);
		ll_pay_need_pwd = (LinearLayout) findViewById(R.id.ll_pay_need_pwd);
		show_available_amount = (TextView) findViewById(R.id.payorder_show_available_amount);
		show_amount = (TextView) findViewById(R.id.orderpay_show_amount);
		orderpay_forget_pwd = (TextView) findViewById(R.id.orderpay_forget_pwd);
		payorder_on_pay = (Button) findViewById(R.id.btn_payorder_on_pay);
		input_pwd = (EditText) findViewById(R.id.et_payorder_input_pwd);
		other_pay_methods = (LinearLayout) findViewById(R.id.ll_payorder_paymoreway);
		chose_other_paymethods = (TextView) findViewById(R.id.tv_chose_other_paymethods);
		chose_methods_text = (TextView) findViewById(R.id.orderpay_chose_methods_text);
		chose_methods_ex = (TextView) findViewById(R.id.orderpay_chose_methods_ex);
		img_pay_methods = (ImageView) findViewById(R.id.orderpay_tv_paymethods_img);
	}
	
	private void initConfigInfo() {
		boolean paypwd = SPUtil.getBoolean(MyApp.res.getString(R.string.PAYPWD1), true);
		if(paypwd){
//			String payPwdFlag = SPUtil.getString(MyApp.res.getString(R.string.PAY_PWD_FLAG));
//			if("0".equals(payPwdFlag)){
				show(ll_pay_need_pwd);
//			}else{
//				hide(ll_pay_need_pwd);
//			}
		}else {
			hide(ll_pay_need_pwd);
		}
	}
	
	private int pos;
	
	private void initLayout() {
		System.out.println("210--------payment_amount > "+payment_amount+"  avalible_amount "+avalible_amount);
//		01-09 09:30:00.943: I/System.out(11552): 210--------payment_amount > 2000  avalible_amount 0

		if(payment_amount <= avalible_amount){
			is_account_remain_enough = true;
			ll_pay_other_method.setVisibility(View.GONE);
		}else{
			show_other_amount = (TextView) findViewById(R.id.payorder_tv_other_amount);
			is_account_remain_enough = false;
			ll_pay_other_method.setVisibility(View.VISIBLE);
			
			String index = SPUtil.getString(MyApp.res.getString(R.string.PAY_METHODS));
			if(!"".equals(index)||!AppUtil.isEmpty(index)){
				pos = Integer.parseInt(index);
				configOtherPayMethods(pos);
			}else{
				chose_other_paymethods.setVisibility(View.VISIBLE);
				img_pay_methods.setVisibility(View.GONE);
				chose_methods_text.setVisibility(View.GONE);
				chose_methods_ex.setVisibility(View.GONE);
			}
		}
	}
	
	private void configOtherPayMethods(int position){
		chose_other_paymethods.setVisibility(View.GONE);
		img_pay_methods.setVisibility(View.VISIBLE);
		chose_methods_text.setVisibility(View.VISIBLE);
		chose_methods_ex.setVisibility(View.VISIBLE);
		img_pay_methods.setImageDrawable(imgs.getDrawable(position));
		chose_methods_text.setText(pay_methods[position].substring(2));
		selected_pay_method_code = pay_methods[position].substring(0,2);
		chose_methods_ex.setText(pay_methods_ex[position]);
		if(position == 4){
			chose_methods_ex.setTextColor(Color.parseColor("#007aff"));
		}else{
			chose_methods_ex.setTextColor(Color.parseColor("#999999"));
		}
	}
	
	private void initPayment() {
		if(is_account_remain_enough){//账户余额足够
			//判断红包的余额
			if(red_packet_amount >= payment_amount){
				red_packet_payment = payment_amount;
			}else{//如果红包不足的的情况下
				red_packet_payment = red_packet_amount;
				cash_payment = payment_amount - red_packet_amount;
			}
		}else{
			red_packet_payment = red_packet_amount;
			cash_payment = available_money;
			other_payment = payment_amount - avalible_amount;
			show_other_amount.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(other_payment+""))));
		}
	}

	private void initPaymentHead() {
		String issue_text = MyApp.lotteryMap.get(bundle.getString("lotteryId"))+"\n第"+"<font color=#007aff>"+bundle.getString("issue")+"</font>"+"期";
		String money_text = "¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(payment_amount+"")));
		tv_pay_play_issue.setText(Html.fromHtml(issue_text));//设置期数
		tv_pay_order_money.setText(money_text);//设置投注金额	
		show_available_amount.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(avalible_amount+""))));
	}

	private void initMyBundleDatas() {
//		String s="现金账户 : "+AccountEnum.cash_account.getMoney()
//				+"投注账户 : "+AccountEnum.bet_account.getMoney()
//				+"奖金账户 : "+AccountEnum.award_account.getMoney()
//				+"红包账户: "+AccountEnum.redpacket_account.getMoney()
//				+"分红账户: "+AccountEnum.bonus_account.getMoney()
//				+"积分账户: "+AccountEnum.point_account.getMoney();
//		System.out.println("210----pay-->"+s);
		
		 bundle = getMyBundle();//  以下都是以分为计算单位
		 payment_amount = Long.parseLong(bundle.getString("money"));
		 avalible_amount = UserBean.getInstance().getAvailableBalance();
		 available_money = UserBean.getInstance().getAvailableMoney();
		 red_packet_amount = AccountEnum.redpacket_account.getMoney();
		 //如果Application退出之后再进来从缓存的UserBean对象中获取该值不准确  没有去用户中心的时候数据可能没有更新
		 //所有的下单 1205  或4001 下完订单之后最新的账户信息是保存在AccountEnum 之中的 
//		 avalible_amount = AccountEnum.award_account.getMoney();
//		 available_money = AccountEnum.cash_account.getMoney();//现金账户(现金账户中的钱就是可用现金)
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.orderpay_show_amount:
			showAmount();
			break;
		case R.id.orderpay_forget_pwd:
			Bundle forgetPswbundle = new Bundle();
			forgetPswbundle.putInt(getString(R.string.cmd), 1009);
			start(ThirdActivity.class,ForgetPwd.class,forgetPswbundle);
			break;
		case R.id.btn_payorder_on_pay:
			onpay();
			break;
		case R.id.ll_payorder_paymoreway:
			showOtherPayMethods();
			break;
		}
	}

	private void showOtherPayMethods() {
		dialog = new TCDialogs(mActivity);
		payMethodslist = dialog.getDialog_listview();
		mAdapter = new MyAdapter();
		payMethodslist.setAdapter(mAdapter);
		payMethodslist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				SPUtil.putString(getString(R.string.PAY_METHODS),position+"");
				configOtherPayMethods(position);
				dialog.dismiss();
			}
		});
		dialog.popPayMethods();
		
	}
	
	private TextView tv_text;
	private ImageView iv_image;
	
	private class MyAdapter extends BaseListAdapter {
		
		@Override
		public int getCount() {
			return pay_methods.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.pay_otherpay_methods_item, null);
			tv_text = (TextView) view.findViewById(R.id.tv_other_pay_methods_text);
			iv_image = (ImageView) view.findViewById(R.id.iv_orderpay_img);
			ImageView pay_circle = (ImageView) view.findViewById(R.id.iv_pay_circle_bg);
			View orderpay_item_line = view.findViewById(R.id.orderpay_item_line);
			tv_text.setText(pay_methods[position].substring(2));
			iv_image.setImageDrawable(imgs.getDrawable(position));
			if(position == pos){
				pay_circle.setBackgroundResource(R.drawable.phone_pay_circle);
			}
			if (position == 4) {
				orderpay_item_line.setBackgroundColor(Color.WHITE);
				
			}else{
				orderpay_item_line.setBackgroundColor(Color.parseColor("#DBDBDB"));
			}
			return view;
		}
	}
	
	//确认付款按钮
	private void onpay() {
		if(other_payment > 0){//第三方支付金额大于0的情况下, 必须传入第三方支付的渠道 "02".equals(selected_pay_method_code)  取消掉
			if("01".equals(selected_pay_method_code) 
					|| "03".equals(selected_pay_method_code) 
					|| "04".equals(selected_pay_method_code)
					|| "07".equals(selected_pay_method_code)
					|| "08".equals(selected_pay_method_code)){//第三方支付 已经选择
			}else {
				return;
			}
		}
		
		String pwd_ed_text = input_pwd.getText().toString().trim();
		Bundle bundle = getMyBundle();
		message = new MessageJson();
		if(ll_pay_need_pwd.getVisibility() == View.VISIBLE){
			if(AppUtil.isEmpty(pwd_ed_text)){
				Toast.makeText(mActivity, "请输入账户密码", Toast.LENGTH_SHORT).show();
				return;
			}
				
			pwd_ed_text = DigestUtils.md5Hex(pwd_ed_text);
			bundle.putString("password", pwd_ed_text);
			message.put("H", pwd_ed_text);
		}
	
//		String spofpwd = SPUtil.getString(MyApp.res.getString(R.string.PAY_PWD_BUY));
//		if(!AppUtil.isEmpty(spofpwd)){
//			message.put("H", spofpwd);
//		}
		
		bundle.putString("payMethod", selected_pay_method_code);
		if(!"00".equals(selected_pay_method_code)){
			message.put("A",selected_pay_method_code);
		}
		
		bundle.putString("money", other_payment+"");
		bundle.putString("redpacket_payment", red_packet_payment+"");
		bundle.putString("cache_payment", cash_payment+"");
		bundle.putString("password", pwd_ed_text);
		mActivity.getIntent().putExtras(bundle);//把bundle 重新设置给intent
		
		message.put("C", other_payment+"");
		System.out.println("抓取数据：other_payment"+other_payment);
		message.put("E", bundle.getString("orderId"));
		message.put("F", red_packet_payment+"");
		System.out.println("抓取数据：red_packet_payment"+red_packet_payment);
		message.put("G",cash_payment+"");
		System.out.println("抓取数据：cash_payment"+cash_payment);
		message.put("H", pwd_ed_text);
		if("00".equals(selected_pay_method_code)){
			sendRequest_account_pay(message);
		}else if ("01".equals(selected_pay_method_code)) {//银联语音支付
			replaceMiddle(PayecoVoiceFragment.class);
		}else if ("03".equals(selected_pay_method_code)) {//支付宝快捷支付
			sendRequest_ali_pay(message);
		}else if ("04".equals(selected_pay_method_code)) {//支付宝wAP支付
			sendRequest_ali_wap(message);
		}else if("07".equals(selected_pay_method_code)){
			sendRequest_savings_card(message);
		}else if("08".equals(selected_pay_method_code)){
			sendRequest_credit_card(message);
		}
	}
	
	private void showAmount() {
		dialog = new TCDialogs(mActivity);
		TextView show_money_amount = dialog.getShow_money_amount();
		TextView show_redpac_amount = dialog.getShow_redpac_amount();
		show_money_amount.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(available_money+""))));
		show_redpac_amount.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(red_packet_amount+""))));
		dialog.popAmount();
	}
}
