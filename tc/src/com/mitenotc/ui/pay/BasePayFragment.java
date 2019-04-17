package com.mitenotc.ui.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.encrypt.Rsa;
import com.merchant.android.XmlTool;
import com.merchant.android.objects.UpPay;
import com.merchant.android.parse.MerchantXmlParseService;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.account.BettingRecords;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.SPUtil;
import com.payeco.android.plugin.PayecoPluginLoadingActivity;
import com.umpay.quickpay.UmpPayInfoBean;
import com.umpay.quickpay.UmpayQuickPay;
/**
 * 支付的基类,处理支付接口
 * 
 * 1.支付宝 插接支付
 * 2.银联安全支付
 * 3.银联语音支付
 * 4.支付宝WAP支付
 * 
 * 说有的请求 1107接口的 支付 或者是 请求 1101接口的 充值都将有该fragment来完成请求的发送和处理. 
 * @author mitenotc
 */
public class BasePayFragment extends BaseFragment{

	public static  String PARTNER = "";
	public static  String SELLER = "";
	public static  String RSA_PRIVATE = "";
	public static  String RSA_PUBLIC = "";
	protected MessageJson message;
	private static final String PAYECO_PLUGIN_PAYEND_ACTION = "com.mitenotc.com.payend.broadcast";//银联插件回调广播的action
	
	private PayecoBroadcastReceiver receiver;
	protected PopupWindow pop;
	private View anchor;
	protected ListView listView;
	private List<String> datas ;
	protected BaseAdapter payAdapter;
	protected long dissmissTime;
	protected static List<MessageBean> savedBankInfos;
	protected static List<MessageBean> savedBankInfos_payeco_voice;
	protected static List<MessageBean> savedBankInfos_payeco_plugin;
	protected static String CMD_1109_TAG_A;
	private SavedBankInfoAdapter savedBankInfoAdapter;
	private int CMD;
	 /**
     * U付请求码
     */
    private static final int REQUESTCODE = 10000;
	private static final String UPAYSUCCESS = "0000";
	private static final String UPAYCANAL = "1001";
	private static final String UPAYERROR = "1002";
	private static int UPayTypeFlag = 0;
    private Dialog mDialog;
	
	
	public void registBroadCastReceiver() {
		receiver = new PayecoBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(PAYECO_PLUGIN_PAYEND_ACTION);
		mActivity.registerReceiver(receiver, intentFilter);
	}
	
	public void unRegistBroadCastReceiver(){
		if (receiver != null) {
			mActivity.unregisterReceiver(receiver);
			receiver = null;
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		CMD = getMyBundle().getInt(getString(R.string.cmd));
		if(CMD == 1105){//购买 红包 调用的是 支付的接口
			CMD = 1107;
		}
		initPop();
	}
	private void initPop() {
		listView = new ListView(mActivity);
		listView.setSelector(new ColorDrawable(0x00000000));
		payAdapter = new PayAdapter();
		listView.setAdapter(payAdapter);
		datas = new ArrayList<String>();
		listView.setBackgroundResource(R.drawable.ed_bg);
		pop = new PopupWindow(listView, AppUtil.getDisplayWidth(mActivity)-DensityUtil.dip2px(mActivity, 42), DensityUtil.dip2px(mActivity, 200));
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		
		pop.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				if (anchor!= null) {
					dissmissTime = System.currentTimeMillis();
					CheckBox cb = (CheckBox) anchor;
					cb.setChecked(false);
				}
			}
		});
	}
	
	public void showPop(View anchor, List<String> datas){
		if(System.currentTimeMillis() - dissmissTime < 300)
			return;
		if(listView.getAdapter() != payAdapter){
			listView.setAdapter(payAdapter);
		}
		this.anchor = anchor;
		this.datas = datas;
		pop.setWidth(anchor.getWidth());
		payAdapter.notifyDataSetChanged();
		pop.showAsDropDown(anchor);
	}
	public void showPop_saved_bankinfo(View anchor, List<MessageBean> datas){
		if(System.currentTimeMillis() - dissmissTime < 300)
			return;
		if(listView.getAdapter() != savedBankInfoAdapter){
			listView.setAdapter(savedBankInfoAdapter);
		}
		this.anchor = anchor;
		BasePayFragment.savedBankInfos = datas;
		pop.setWidth(anchor.getWidth());
		if(savedBankInfoAdapter!=null){
			savedBankInfoAdapter.notifyDataSetChanged();
		}
		pop.showAsDropDown(anchor);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			mActivity.unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
	
	@Override
	protected void initData() {}
	
	private Bundle bundle;
	
	@Override
	protected void onMessageReceived(Message msg) {////System.out.println("arg1 ,,,,,,,,,,,,,,,,,,,,,,,,,= "+msg.arg1);
		MessageBean bean = (MessageBean) msg.obj;
		if("14004".equals(bean.getA())){
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			Toast.makeText(mActivity, bean.getB(), Toast.LENGTH_SHORT).show();
			return;
		}
		bundle = getMyBundle();
		switch (msg.arg1) {
		case 0://账户支付,没有使用第三方支付
			convertAccountMessage(bean);
			Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
			bundle.putInt("resultState", 0);
			bundle.putString("payMethod", "00");//没有使用第三方支付,该key值是用于 paysuccessfragment 用于判断是从 账户上直接支付的
			start(ThirdActivity.class,PaySuccessFragment.class,bundle);
			finish();
			break;
		case 1://ali 插件 支付
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			convertAccountMessage(bean);
			if(CMD == 1101){//充值
				bundle.putString("orderId", bean.getC());
				mActivity.getIntent().putExtras(bundle);
			}
			
//			ali_pay("泰彩彩票","泰彩彩票使用支付宝极简支付",FormatUtil.moneyFormat2String(Integer.parseInt(message.getString("C"))/100));
			try {
				ali_pay(bean.getD());
			} catch (Exception e) {
				sendRequest(bean.getC(),"支付宝插件启动失败");
			}
			
			break;
		case 2://银联的安全插件支付
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			convertAccountMessage(bean);
			if(CMD == 1101){//充值
				bundle.putString("orderId", bean.getC());
				mActivity.getIntent().putExtras(bundle);
			}
			payeco_plugin_pay(bean.getD());
			break;
		case 3:////System.out.println("a = "+bean.getA());
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			convertAccountMessage(bean);
			if("11004".equals(bean.getA())){
				onPayeco_voicepay_received_11004(bean);
			}else {
				showDialog_pay_success(bean);
//				start(ThirdActivity.class,PaySuccessFragment.class,null);
//				finish();
//				Toast.makeText(mActivity, "跳转到通知 估计开奖 派奖时间", 0).show();
			}
			break;
		case 4:
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			convertAccountMessage(bean);
			Bundle myBundle = getMyBundle();
			myBundle.putString("url", bean.getD());
			myBundle.putString("title", "支付宝Wap支付（免手续费）");
//			mActivity.getIntent().putExtras(myBundle);
//			replaceMiddle(Ali_WAP_Fragment.class);
			try {
//				start(ThirdActivity.class,Ali_WAP_Fragment.class,myBundle);
				start(ThirdActivity.class,TCWebFragment.class,myBundle);//替换 之前的WebFragment
			} catch (Exception e) {
				sendRequest(bean.getC(), "启动wap支付失败");
			}
			
			break;
		case 5:
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			convertAccountMessage(bean);//借记卡支付
			if(CMD == 1101){//充值
				bundle.putString("orderId", bean.getC());
				mActivity.getIntent().putExtras(bundle);
			}
			UmpPayInfoBean infoBean1 = new UmpPayInfoBean();
            infoBean1.setEditFlag("0");
	        UmpayQuickPay.requestPayWithBind(getActivity(), bean.getD(), "", "0", "", infoBean1, REQUESTCODE);
			break;
		case 6:
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			convertAccountMessage(bean);//信用卡支付
			if(CMD == 1101){//充值
				bundle.putString("orderId", bean.getC());
				mActivity.getIntent().putExtras(bundle);
			}
			UmpPayInfoBean infoBean2 = new UmpPayInfoBean();
			infoBean2.setEditFlag("0");
	        UmpayQuickPay.requestPayWithBind(getActivity(), bean.getD(), "", "1", "", infoBean2, REQUESTCODE);
	        UPayTypeFlag = 1;
	        break;
		case 1000://请求用户已经登录过的银行卡信息
//			savedBankInfos = bean.getLIST();
			if("01".equals(CMD_1109_TAG_A)){
				savedBankInfos_payeco_voice = bean.getLIST();
			}else if("02".equals(CMD_1109_TAG_A)){
				savedBankInfos_payeco_plugin = bean.getLIST();
			}
			savedBankInfoAdapter = new SavedBankInfoAdapter();
			if(savedBankInfos == null || savedBankInfos.size()<1){
				break;
			}
			
			savedBankinfoReceived();
			break;
		case 1001://ali 插件支付 证书的请求
			PARTNER = bean.getC();
			SELLER = bean.getF();
			RSA_PRIVATE = bean.getD();
			RSA_PUBLIC = bean.getE();
			
			if(AppUtil.isEmpty(PARTNER) || AppUtil.isEmpty(SELLER) || AppUtil.isEmpty(RSA_PRIVATE) || AppUtil.isEmpty(RSA_PUBLIC)){
				Toast.makeText(MyApp.context, "数据出错", 0).show();
				break;//防止无限的请求
			}else {
				mDialog.show();
				submitData(1, CMD, message);//ali 插件 支付
			}
			break;
		default:
			break;
		}
	}
	
//	@Override
//	protected void errorResult(Message msg) {
//		Toast.makeText(mActivity, msg.obj+"", Toast.LENGTH_SHORT).show();
//		mDialog.dismiss();
//	}
	
	public void convertAccountMessage(MessageBean bean){
//		String payPWD = (String) message.get("H");
//		if(!AppUtil.isEmpty(payPWD)){
//			SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_FLAG), "1");
//			SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_BUY),payPWD);
//		}
		
		String availableMoney = bean.getF();
		String availableCash = bean.getG();
		String availableBalance = bean.getH();
		if(CMD == 1101){//1101的接口和 1107 的接口返回后标签不同
			availableMoney = bean.getE();
			availableCash = bean.getF();
			availableBalance = bean.getG();
		}
		if(AppUtil.isNumeric(availableMoney))
			UserBean.getInstance().setAvailableMoney(Long.parseLong(availableMoney));
		if(AppUtil.isNumeric(availableMoney))
		 UserBean.getInstance().setAvailableCash(Long.parseLong(availableCash));
		if(AppUtil.isNumeric(availableMoney))
		 UserBean.getInstance().setAvailableBalance(Long.parseLong(availableBalance));
		if(bean.getLIST() != null)
		 AccountEnum.convertMessage(bean.getLIST());
	}
	
	/**
	 * 用户已经用过银联语音,或者银联插件支付,回调该方法,重新布置银联支付界面为简化的界面
	 */
	protected void savedBankinfoReceived() {
		
	}
	/**
	 * 在第一次请求时 返回的错误码是 11004的时候调用该方法,有子类复写完成
	 * 银联语音支付的中间出了过程需要结合界面
	 * 因此这里提供方法 有子类去复写
	 * @param bean
	 */
	protected void onPayeco_voicepay_received_11004(MessageBean bean) {
	}

	/**
	 * 调用ali插件支付
	 * @param messageJson
	 */
	public void sendRequest_ali_pay(MessageJson messageJson){//支付宝 插件支付
		message = messageJson;
//		if(AppUtil.isEmpty(PARTNER) || AppUtil.isEmpty(SELLER) || AppUtil.isEmpty(RSA_PRIVATE) || AppUtil.isEmpty(RSA_PUBLIC)){
//			MessageJson message = new MessageJson();
//			message.put("A", "03");
//			submitData(1001, 1108, message);//ali 插件支付 证书的请求
//		}else {
			submitData(1, CMD, messageJson);//ali 插件 支付
//		}
	}
	
	/**
	 * 调用支付宝插件的方法
	 * @param subject
	 * @param body
	 * @param price
	 */
	@Deprecated //由于某些手机会出现 Res 加密错误,所有加密放在了服务端 是用 ali_pay(String info);
	private void ali_pay(String subject, String body, String price) {
		String orderInfo = getOrderInfo(subject,body,price);
		String signType = getSignType();
		String sign = sign(signType, orderInfo);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		String info = orderInfo + "&sign=" + "\"" + sign + "\"" + "&" + getSignType();
////System.out.println("info ====================== "+info);
		Intent intent = new Intent();
		intent.setPackage(mActivity.getPackageName());
		intent.setAction("com.alipay.mobilepay.android");
		intent.putExtra("order_info", info);
		startActivityForResult(intent, 0);
	}
	private void ali_pay(String info) {////System.out.println("info =============="+info);
		Intent intent = new Intent();
		intent.setPackage(mActivity.getPackageName());
		intent.setAction("com.alipay.mobilepay.android");
		intent.putExtra("order_info", info);
		startActivityForResult(intent, 0);
	}
	/**
	 * 阿里支付的回调
	 */
	@Override
	public void onActivityResult(int requestCode, int result, Intent data) {
		super.onActivityResult(requestCode, result, data);
		if(data == null){
			return;
		}
		String action = data.getAction();
		String resultStatus = data.getStringExtra("resultStatus");
		String memo = data.getStringExtra("memo");
		String resultString = data.getStringExtra("result");
		
	/*	try {
			PrintWriter pw = new PrintWriter(new File(Environment.getExternalStorageDirectory(),"tc.txt"));
			pw.print("resultStatus = "+resultStatus +"\n  action = "+action+"\n  memo = "+memo+"\n  resultString = "+resultString);
			////System.out.println("resultStatus = "+resultStatus +"\n  action = "+action+"\n  memo = "+memo+"\n  resultString = "+resultString);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		
		Bundle myBundle = getMyBundle();
		if("9000".equals(resultStatus) || "8000".equals(resultStatus)){
			myBundle.putInt("resultState", 0);//0 代表成功//1代表失败
		}else {
			myBundle.putInt("resultState", 1);//0 代表成功//1代表失败
		}
		
		if("4000".equals(resultStatus)){
			sendRequest(bundle.getString("orderId"), "(支付宝)订单支付失败");
		}else if("6001".equals(resultStatus)){
			sendRequest(bundle.getString("orderId"), "(支付宝)用户中途取消");
		}else if("6002".equals(resultStatus)){
			sendRequest(bundle.getString("orderId"), "(支付宝)网络连接出错");
		}
		
		start(ThirdActivity.class,PaySuccessFragment.class,myBundle);
		finish();
	}
	
	/**
	 * U付的回调结果
	 */
	@Override
	protected void onUpayResultF(int requestCode, int resultCode, Intent data) {
		super.onUpayResultF(requestCode, resultCode, data);
		if(data == null){
			return;
		}
		if (requestCode == REQUESTCODE) {
			// U付（联动优势）
			String umPayCode = data.getStringExtra("umpResultCode");
			Bundle myBundle = getMyBundle();
			if(UPAYSUCCESS.equals(umPayCode)){
				myBundle.putInt("resultState", 0);
			}else{
				myBundle.putInt("resultState", 1);
			}
			
			if(UPayTypeFlag == 0){  //是借记卡用户
				if(UPAYCANAL.equals(umPayCode)){
					sendRequest(bundle.getString("orderId"), "(U付借记卡)用户中途取消");
				}else if(UPAYERROR.equals(umPayCode)){
					sendRequest(bundle.getString("orderId"), "(U付借记卡)传入参数错误");
				}
			}else{
				if(UPAYCANAL.equals(umPayCode)){
					sendRequest(bundle.getString("orderId"), "(U付信用卡)用户中途取消");
				}else if(UPAYERROR.equals(umPayCode)){
					sendRequest(bundle.getString("orderId"), "(U付信用卡)传入参数错误");
				}
			}
	        
			start(ThirdActivity.class,PaySuccessFragment.class,myBundle);
			finish();
		 }
	}
	
	public static String getOrderInfo(String subject, String body, String price) {
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
		orderInfo += "&";
		orderInfo += "seller_id=" + "\"" + SELLER + "\"";
		orderInfo += "&";
		orderInfo += "out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		orderInfo += "&";
		orderInfo += "subject=" + "\"" + subject + "\"";
		orderInfo += "&";
		orderInfo += "body=" + "\"" + body + "\"";
		orderInfo += "&";
		orderInfo += "total_fee=" + "\"" + price + "\"";
		orderInfo += "&";
		orderInfo += "notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 接口名称， 定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型，定值
		orderInfo += "&payment_type=\"1\"";

		// 字符集，默认utf-8
		orderInfo += "&_input_charset=\"utf-8\"";

		// 超时时间 ，默认30分钟.
		// 设置未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		// 该功能需要联系支付宝配置关闭时间。
		orderInfo += "&it_b_pay=\"30m\"";

		// 商品展示网址,客户端可不加此参数
		orderInfo += "&show_url=\"m.alipay.com\"";

//		 verify(sign, orderInfo);

		return orderInfo;
	}

	 @SuppressWarnings("deprecation")
	 private static void verify(String sign, String content) {
	 String decodedSign = URLDecoder.decode(sign);
	 boolean isOk = Rsa.doCheck(content, decodedSign, RSA_PUBLIC);
	 ////System.out.println(isOk);
	 }

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	public static String sign(String signType, String content) {////System.out.println("content = "+content);
		return Rsa.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/**
	 * 银联插件的请求
	 * @param messageJson
	 */
	public void sendRequest_payeco_plugin(MessageJson messageJson){
		this.message = messageJson;
		mDialog.show();
		submitData(2, CMD, messageJson);
	}
	
	/**
	 * 银联的语音支付
	 * @param messageJson
	 */
	public void sendRequest_payeco_voicepay(MessageJson messageJson){
		this.message = messageJson;
		mDialog.show();
		System.out.println("sendRequest_payeco_voicepay");
		submitData(3, CMD, messageJson);
	}
	
	public void sendRequest_ali_wap(MessageJson message){
		this.message = message;
		mDialog.show();
		submitData(4, CMD, message);
	}
	
	public void sendRequest_savings_card(MessageJson message){
		this.message = message;
		mDialog.show();
		submitData(5, CMD, message);
		
	}
	
	public void sendRequest_credit_card(MessageJson message){
		this.message = message;
		mDialog.show();
		submitData(6, CMD, message);
	}
	/**
	 * 调用银联插件
	 */
	public void payeco_plugin_pay(String xml){////System.out.println("xml ======= "+xml);
		Intent intent = new Intent(mActivity,PayecoPluginLoadingActivity.class);
		intent.putExtra("upPay.Req", xml);
		startActivity(intent);
	}
	
	/**
	 * 银联插件回调广播处理
	 * 注意该广播必须在子类注册才能正常使用
	 * @author mitenotc
	 */
	public class PayecoBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (PAYECO_PLUGIN_PAYEND_ACTION.equals(action)) {
				String payResp = intent.getExtras().getString("upPay.Rsp");////System.out.println("payResp = "+payResp);
				UpPay upPayRespObj = (UpPay) XmlTool.xmlToObject(payResp,UpPay.class, MerchantXmlParseService.MERCHANT_OBJECT);
//				Intent intentTarget = new Intent(mActivity,
//						MerchantClientPayResultActivity.class);
//				intentTarget.putExtra("upPay", upPayRespObj);
//				startActivity(intentTarget);
				Bundle myBundle = getMyBundle();
				if (upPayRespObj != null) {////System.out.println("upPayRespObj.getRespCode() ======== "+upPayRespObj.getRespCode());
					myBundle.putString("responseDesc", upPayRespObj.getRespDesc());
					if("0000".equals(upPayRespObj.getRespCode())){////System.out.println("resultState ======= "+0);
						myBundle.putInt("resultState", 0);//0表示成功
					}else {////System.out.println("resultState ======== "+1);
						myBundle.putInt("resultState", 1);//1表示失败
					}
				
					if("I002".equals(upPayRespObj.getRespCode())){
						sendRequest(myBundle.getString("orderId"), "IVR：持卡人操作超时");
					}else if("I022".equals(upPayRespObj.getRespCode())){
						sendRequest(myBundle.getString("orderId"), "IVR：用户输入信息错误");
					}else if("I023".equals(upPayRespObj.getRespCode())){
						sendRequest(myBundle.getString("orderId"), "IVR：用户两次输入不符");
					}
				}
				
				start(ThirdActivity.class,PaySuccessFragment.class,myBundle);
				mActivity.finish();
			} else {
				Toast.makeText(mActivity, "返回结果出错", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/** 获取网络数据 */
	private void sendRequest(String orderId,String error) {
		MessageJson msg = new MessageJson();
		msg.put("A", orderId);
		msg.put("B", error);
		submitData(4241, 1114, msg);
	}
	
	class PayAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			if (datas == null) {
				return 0;
			}
			return datas.size();
		}
		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			TextView tv = null;
			if(convertView == null){
				convertView = View.inflate(mActivity, R.layout.m_acc_me_safe_item, null);
				tv = (TextView) convertView.findViewById(R.id.tv_listview_item_number);
				convertView.setTag(tv);
			}else {
				tv = (TextView) convertView.getTag();
			}
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) anchor;
					cb.setText(datas.get(position));
					cb.setChecked(false);
					cb.setTag(position);
					pop.dismiss();
				}
			});
			tv.setText(datas.get(position));
			return convertView;
		}
	}
	
	class SavedBankInfoAdapter extends BaseAdapter{
		public SavedBankInfoAdapter(){
			if("01".equals(CMD_1109_TAG_A)){
				savedBankInfos = savedBankInfos_payeco_voice;
			}else if("02".equals(CMD_1109_TAG_A)){
				savedBankInfos = savedBankInfos_payeco_plugin;
			}
		}
		@Override
		public int getCount() {
			if(savedBankInfos == null)
				return 0;
			else {
				return savedBankInfos.size();
			}
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(mActivity, R.layout.payeco_saved_bankinfo_item, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) convertView.findViewById(R.id.payeco_saved_bankinfo_name);
				holder.tv_card = (TextView) convertView.findViewById(R.id.payeco_saved_bankinfo_card);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			MessageBean item = savedBankInfos.get(position);
			String cardNum = item.getB();
			final String name = "持卡人 : "+item.getC();
			final String cardLast4Num = "卡尾号 : "+cardNum.substring(cardNum.length() - 4,cardNum.length());
			holder.tv_name.setText(name);
			holder.tv_card.setText(cardLast4Num);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) anchor;
					cb.setText(name+"        "+cardLast4Num);
					cb.setChecked(false);
					cb.setTag(position);
					pop.dismiss();
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_name;
			TextView tv_card;
		}
	}
	
	public void showDialog_pay_success(final MessageBean bean){
		final Dialog dialog = new PaySuccessDialog(mActivity);
		View view = View.inflate(mActivity, R.layout.pay_success_notice, null);
		Button pay_success_btn = (Button) view.findViewById(R.id.pay_success_btn);
		Button pay_problem_btn = (Button) view.findViewById(R.id.pay_problem_btn);
		
		pay_success_btn.setOnClickListener(new OnClickListener() {//支付完成
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				MyApp.backToTCActivity();
				int cmd = getMyBundle().getInt(getString(R.string.cmd));
				if(cmd == 1107){//购彩支付,目前这里的处理和 账户支付是一样的. 但是需求还不明确
					start(ThirdActivity.class,BettingRecords.class,null);
				}else if(cmd == 1101){//充值
					CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.user);
				}else if(cmd == 1105){//购买红包
					CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.user);
				}
			}
		});
		pay_problem_btn.setOnClickListener(new OnClickListener() {//支付遇到困难
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				sendRequest(bean.getC(),"用户点了语音支付遇到问题");
				MyApp.backToTCActivity();
				CustomTagEnum.startActivity(MyApp.activityList.get(0), null, CustomTagEnum.consultation);
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}
	
	class PaySuccessDialog extends Dialog{

		public PaySuccessDialog(Context context) {
			super(context,R.style.pay_success_dialog_style);
			setOwnerActivity((BaseActivity) context);
		}
	}
	
	public void sendRequest_saved_bankinfo(String code){
		MessageJson message = new MessageJson();
		message.put("A", code);
		CMD_1109_TAG_A = code;
		submitData(1000, 1109, message);
	}
	
	public void sendRequest_account_pay(MessageJson message){//账户支付,没有使用第三方支付
		this.message = message;
		submitData(0, 1107, message);
//		{"E":"qc1407161741512173","F":"0","G":200,"H":"e10adc3949ba59abbe56e057f20f883e","C":"0"}
	}

	public List<String> getDatas() {
		return datas;
	}
	public void setDatas(List<String> datas) {
		this.datas = datas;
	}
}
