package com.mitenotc.ui.pay;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.download.util.StringUtils;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.PayMethodEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.RechargeFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 更多支付方式
 * 
 * @author wanli
 * 
 */
public class PayMoreway extends BasePayFragment implements OnClickListener {
	private TextView zwl_play_periods_tv;
	private TextView zwl_tv_show_1;
	private TextView zwl_show_phoneNumber;
	private TextView zwl_more_payment_way_tv;
	private TextView pay_more_way_tv_recharge;//  立即充值
	private TextView zwl_balance_tv;
	
	private LinearLayout paymoreway_ll_alipay;
	private LinearLayout paymoreway_ll_payeco_voicepay;
	private LinearLayout paymoreway_ll_ali_wap;
	private LinearLayout paymoreway_ll_upay_savings;
	private LinearLayout paymoreway_ll_upay_credit;
	private LinearLayout paymoreway_ll_payeco_plugin;
	
	private Spannable textSp;
	
	private View[] pay_method_views;
	private Button zwl_affirm_payment_btn;
	private PayMethodEnum payMethod;
	private TextView acc_text2;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_money_elect);
		setTitleNav(R.string.acc_title_order_pay, R.drawable.title_nav_back, 0);
		initView();
		initListeners();
		initPayMethodVisibility();
		if(savedBankInfos_payeco_voice == null || savedBankInfos_payeco_voice.size()<1){//如果语音支付还没有请求到成功的数据
			sendRequest_saved_bankinfo("01");
		}else if(savedBankInfos_payeco_plugin == null || savedBankInfos_payeco_plugin.size() < 1){
			sendRequest_saved_bankinfo("02");
		}
	}

	private void initView() {
		zwl_play_periods_tv = (TextView) findViewById(R.id.acc_title_content1);
		zwl_tv_show_1 = (TextView) findViewById(R.id.acc_title_text1);
		acc_text2 = (TextView) findViewById(R.id.acc_title_text2);
		acc_text2.setText("订单金额：");
		zwl_show_phoneNumber = (TextView) findViewById(R.id.acc_title_content2);
		zwl_more_payment_way_tv = (TextView) findViewById(R.id.zwl_more_payment_way_tv);
		pay_more_way_tv_recharge = (TextView) findViewById(R.id.pay_more_way_tv_recharge);
		zwl_affirm_payment_btn = (Button) findViewById(R.id.zwl_affirm_payment_btn);
		initHeaderDatas();
		zwl_balance_tv = (TextView) findViewById(R.id.zwl_balance_tv);
		zwl_balance_tv.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
		
		if(getMyBundle().getInt(MyApp.res.getString(R.string.cmd)) == 1101){//如果之前就是一个充值界面
			hide(pay_more_way_tv_recharge);
		}
		paymoreway_ll_alipay = (LinearLayout) findViewById(R.id.paymoreway_ll_alipay);
		//2014-6-18  同事郑岩说 济南会议讨论去掉次支付方式---TODO
		paymoreway_ll_payeco_plugin = (LinearLayout) findViewById(R.id.paymoreway_ll_payeco_plugin);
		
		
		paymoreway_ll_payeco_voicepay = (LinearLayout) findViewById(R.id.paymoreway_ll_payeco_voicepay);
		paymoreway_ll_ali_wap = (LinearLayout) findViewById(R.id.paymoreway_ll_ali_wap);
		paymoreway_ll_upay_savings = (LinearLayout) findViewById(R.id.paymoreway_ll_upay_savings);
		paymoreway_ll_upay_credit = (LinearLayout) findViewById(R.id.paymoreway_ll_upay_credit);
		
		
		// 支付方式所对应的 提示语
//		paymoreway_ll_alipay_tv = (TextView) findViewById(R.id.paymoreway_ll_alipay_tv);
//		paymoreway_ll_payeco_voicepay_tv = (TextView) findViewById(R.id.paymoreway_ll_payeco_voicepay_tv);
//		paymoreway_ll_ali_wap_tv= (TextView) findViewById(R.id.paymoreway_ll_ali_wap_tv);
		
//		paymoreway_ll_alipay_tv.setText(jointRbtnText(getString(R.string.paymethod_ali_plugin), 
//				8, getString(R.string.paymethod_ali_plugin).length(), 0, 0, 8, getString(R.string.paymethod_ali_plugin).length()));
//		paymoreway_ll_payeco_voicepay_tv.setText(jointRbtnText(getString(R.string.paymethod_payeco_voicepay), 
//				7, getString(R.string.paymethod_payeco_voicepay).length(), 0, 0, 7, getString(R.string.paymethod_payeco_voicepay).length()));
//		paymoreway_ll_ali_wap_tv.setText(jointRbtnText(getString(R.string.paymethod_ali_wap), 
//				9, getString(R.string.paymethod_ali_wap).length(), 0, 0, 9, getString(R.string.paymethod_ali_wap).length()));
		
		
		pay_method_views = new View[6];
		pay_method_views[0] = paymoreway_ll_alipay;
		pay_method_views[5] = paymoreway_ll_payeco_plugin;
		pay_method_views[1] = paymoreway_ll_payeco_voicepay;
		pay_method_views[2] = paymoreway_ll_ali_wap;
		pay_method_views[3] = paymoreway_ll_upay_savings;
		pay_method_views[4] = paymoreway_ll_upay_credit;
	}

//	private   Spannable jointRbtnText(String str,int littleStart,int littleEnd , int StyleStart ,int StyleEnd,int ColorStart,int ColorEnd){
//		textSp=(Spannable) Html.fromHtml(str);
//		SpannableString用法说明 
//		其中参数what是要设置的Style span，start和end则是标识String中Span的起始位置，而 flags是用于控制行为的，通常设置为0或Spanned中定义的常量，常用的有：
//		•Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含两端start和end所在的端点
//		•Spanned.SPAN_EXCLUSIVE_INCLUSIVE --- 不包含端start，但包含end所在的端点
//		•Spanned.SPAN_INCLUSIVE_EXCLUSIVE --- 包含两端start，但不包含end所在的端点
//		•Spanned.SPAN_INCLUSIVE_INCLUSIVE --- 包含两端start和end所在的端点
//		    textSp= new SpannableString(str); 
////		    13, getString(R.string.paymethod_ali_plugin).length(), 0, 0, 7, 13));
//			//		设置字体大小  （默认字体16   ）
//			//		textSp.setSpan(new AbsoluteSizeSpan(16), 1, str.length()-5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
//			textSp.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mActivity, 12)), littleStart,littleEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE); //str.length()-8, str.length()
//			//		设置字体样式  （加粗）
//			//textSp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), StyleStart, StyleEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE); //0, str.length()-8 
//			//		设置字体 颜色（体前景色ForegroundColorSpan   背景色 BackgroundColorSpan）R.color.klpk_bonustext_color
//			textSp.setSpan(new ForegroundColorSpan(MyApp.res.getColor(R.color.fast3_text_color2)), ColorStart,ColorEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);//str.length()-4, str.length()-1  
//			                                         
//		return textSp;
//	}
	/**
	 * 初始化头部的信息
	 */
	private void initHeaderDatas() {
		Bundle myBundle = getMyBundle();
		String lotteryId = myBundle.getString("lotteryId");
//		String payDesc = myBundle.getString("payDesc");
		if(!AppUtil.isEmpty(lotteryId)){
			zwl_play_periods_tv.setText(Html.fromHtml(MyApp.lotteryMap.get(lotteryId)+"第<font color=#007aff>"+myBundle.getString("issue")+"</font>期"));
			zwl_tv_show_1.setText("支付项目：");
		}else {
			zwl_play_periods_tv.setText("购彩预付款充值");
			zwl_tv_show_1.setText("商品描述：");
			if("".equals(lotteryId)){//账户充值
				zwl_play_periods_tv.setText("购彩预付款充值");//+payDesc.substring(0,3));
			}
			
			zwl_affirm_payment_btn.setText("马 上 支 付");//+payDesc.substring(0,3));
			
			////System.out.println("pay affirm = "+zwl_affirm_payment_btn.getText().toString());
		}
		zwl_show_phoneNumber.setText(Html.fromHtml("¥ "+"<font color=#ff0000>"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(myBundle.getString("money"))))+"</font>"));//有改动
	}

	private void initListeners() {
		zwl_more_payment_way_tv.setOnClickListener(this);
		
		paymoreway_ll_alipay.setOnClickListener(this);
		paymoreway_ll_payeco_voicepay.setOnClickListener(this);
		paymoreway_ll_payeco_plugin.setOnClickListener(this);
		paymoreway_ll_ali_wap.setOnClickListener(this);
		paymoreway_ll_upay_savings.setOnClickListener(this);
		paymoreway_ll_upay_credit.setOnClickListener(this);
		
		zwl_affirm_payment_btn.setOnClickListener(this);
		pay_more_way_tv_recharge.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zwl_more_payment_way_tv:
			switchVisibility(pay_method_views.length,View.GONE,View.GONE);
			payMethod = null;
			break;
		case R.id.paymoreway_ll_alipay://第一种支付方式
			switchVisibility(0,View.VISIBLE,View.VISIBLE);
			payMethod = PayMethodEnum.alipay_plugin;
			break;
		case R.id.paymoreway_ll_payeco_voicepay://第二种支付方式
			switchVisibility(1,View.VISIBLE,View.VISIBLE);
			payMethod = PayMethodEnum.payeco_voicepay;
			break;
		case R.id.paymoreway_ll_ali_wap://第三种支付方式
			switchVisibility(2,View.VISIBLE,View.VISIBLE);
			payMethod = PayMethodEnum.ali_WAP;
			break;
			
		case R.id.paymoreway_ll_upay_savings://第四种支付方式
			switchVisibility(3,View.VISIBLE,View.VISIBLE);
			payMethod = PayMethodEnum.savings_card;
			break;
			
		case R.id.paymoreway_ll_upay_credit://第五种支付方式
			switchVisibility(4,View.VISIBLE,View.VISIBLE);
			payMethod = PayMethodEnum.credit_card;
			break;
		case R.id.paymoreway_ll_payeco_plugin://第六种支付方式
			switchVisibility(5,View.VISIBLE,View.VISIBLE);
			payMethod = PayMethodEnum.payeco_plugin;
			break;
		case R.id.zwl_affirm_payment_btn:
			onConfirm();
			break;
		case R.id.pay_more_way_tv_recharge://充值
			start(ThirdActivity.class, RechargeFragment.class, null);
			break;
		default:
			break;
		}
	}


	/**
	 * 确认支付
	 */
	private void onConfirm() {////System.out.println("bundle.getString(\"orderId\") = "+getMyBundle().getString("orderId"));
		SPUtil.putInt(getString(R.string.pay_method), payMethod.ordinal());//把 支付方式的 index 存到 sharedpreference
		Bundle bundle = getMyBundle();
		bundle.putString("payMethod", payMethod.getPaymethod_code());
		mActivity.getIntent().putExtras(bundle);//把bundle 重新设置给intent
		message = new MessageJson();
		message.put("A", payMethod.getPaymethod_code());
		message.put("C", bundle.getString("money"));
		message.put("E", bundle.getString("orderId"));
		message.put("F", bundle.getString("redpacket_payment"));
		message.put("G", bundle.getString("cache_payment"));
		message.put("H", bundle.getString("password"));
		switch (payMethod) {
		case alipay_plugin:
//				payMethod.onPay(messageJson, this);
			sendRequest_ali_pay(message);
			break;
		case payeco_voicepay:
			start(ThirdActivity.class,PayecoVoiceFragment.class,bundle);
			break;
		case ali_WAP:
			sendRequest_ali_wap(message);
			break;
		case savings_card:
			sendRequest_savings_card(message);
			break;
		case credit_card:
			sendRequest_credit_card(message);
			break;
		case payeco_plugin:
			start(ThirdActivity.class,PayecoPluginFragment.class,bundle);
//			sendRequest_payeco_plugin(message);
			break;
		default:
			break;
		}
	}

	private void switchVisibility(int index_payMethod,int visibility_more_way,int visibility_affirm_pay) {
		setPayMethodVisibility(index_payMethod);
		setVisibility(zwl_more_payment_way_tv,visibility_more_way);
		setVisibility(zwl_affirm_payment_btn,visibility_affirm_pay);
	}


	/**
	 * 初始化 支付方式的可见性
	 */
	private void initPayMethodVisibility() {
		int payMethod_prefer = SPUtil.getInt(getString(R.string.pay_method), -1);////System.out.println("payMethod_prefer = "+payMethod_prefer);
		if(payMethod_prefer == -1){
			switchVisibility(pay_method_views.length, View.GONE, View.GONE);
		}else if(payMethod_prefer>=PayMethodEnum.getIndex()){
				for (View view : pay_method_views) {
					view.setVisibility(View.VISIBLE);
				}
		}else{
			payMethod = PayMethodEnum.values()[payMethod_prefer];//从sharedpreference 中 取出之前选择的支付方式的 index
			switchVisibility(payMethod_prefer,View.VISIBLE,View.VISIBLE);
		}
	}
	/**
	 * 设置所有的 支付方式可见或不可见
	 * @param visibility
	 */
	private void setPayMethodVisibility(int index) {
		if(index >= pay_method_views.length){//全部可见
			for (View view : pay_method_views) {
				view.setVisibility(View.VISIBLE);
			}
		}else {//设置 index 可见
			for (View view : pay_method_views) {
				view.setVisibility(View.GONE);
			}
			setVisibility(pay_method_views[index], View.VISIBLE);
		}
//		hide(pay_method_views[1]);
	}

}
