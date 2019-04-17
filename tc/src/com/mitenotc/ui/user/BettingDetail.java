package com.mitenotc.ui.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.encrypt.Base64;
import com.google.gson.Gson;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.PhoneVerify;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.pay.PayIntegral;
import com.mitenotc.ui.pay.PayNormal;
import com.mitenotc.ui.play.PL121;
import com.mitenotc.ui.ui_utils.MWebView;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 普通投注详情 //IMI开头的订单为模拟投注的订单
 * 
 * @author mitenotc
 */
public class BettingDetail extends BaseFragment {
	private final static int ORDER_STATE_NOT_FINISHED = 0;// 1206接口 订单状态为0,
															// 未支付状态
	private int order_state;// 用于标记当前的订单状态

	protected MessageBean msgObj;
	protected List<MessageBean> mList_copy;
	protected boolean isChecked = true;// 用于记录当前的 checkBox 的状态
	protected int mListSize;
	// 订单信息
	protected String orderId;// 订单流水
	protected String lotteryName;
	protected String issue;
	protected long orderAmount;
	protected String orderAward;
	protected String orderState;
	protected String orderTime;
	protected String lotteryId;
	protected String htmlInfo;
	protected String s = "PGRpdiBjbGFzcz0ncGQwNjMgbGNiZXRUZXh0JyBpZD0nY2xhc2xpJyBzdHlsZT0nZGlzcGxheTpibG9jazsnPjx0YWJsZSB3aWR0aD0nMTAwJScgYm9yZGVyPScwJyBjZWxscGFkZGluZz0nMCcgY2VsbHNwYWNpbmc9JzAnIGNsYXNzPSdsY2JldFRpdGxlIG1nVG9wMDYnPjx0Ym9keT48dHI+PHRkIHdpZHRoPScxNSUnPuWcuuasoTwvdGQ+PHRkIHdpZHRoPSc3MCUnPuS4u+mYn1ZT5a6i6ZifL+aKleazqOmAiemhuTwvdGQ+PHRkIHdpZHRoPScxNSUnIHN0eWxlPSdib3JkZXItcmlnaHQ6bm9uZSc+5q+U5YiGPC90ZD48L3RyPjwvdGJvZHk+PC90YWJsZT48dGFibGUgaWQ9J3Rjb250JyB3aWR0aD0nMTAwJScgYm9yZGVyPScwJyBjZWxscGFkZGluZz0nMCcgY2VsbHNwYWNpbmc9JzAnIGNsYXNzPSdsY2JldFRhYmxlJz48dGJvZHk+PHRyIGlkPScxNDEyMTctMDAxJz4KCQkJCQkJCQkJCQkJCTx0ZCB3aWR0aD0nMTUlJyByb3dzcGFuPScyJz7lkagzPGJyPjAwMTwvdGQ+CgkJCQkJCQkJCQkJCQk8dGQgd2lkdGg9JzcwJSc+5rOw5Zu9KDxmb250IGNvbG9yPSdncmVlbic+IC0xLjA8L2ZvbnQ+KTxlbSBjbGFzcz0nZm9udFNpemUwNyc+VlM8L2VtPumprOadpeilv+S6mjwvdGQ+CgkJCQkJCQkJCQkJCQk8dGQgd2lkdGg9JzE1JScgY2Fsc3M9J3I5bGFzdCcgcm93c3Bhbj0nMic+5Y2KPGJyPuWFqDwvdGQ+PC90cj48dHI+PHRkPjxkaXYgY2xhc3M9J3RkbGVmdCBncmF5Jz4mbmJzcDs8c3BhbiBzdHlsZT0nY29sb3I6I2NjYzsnPuiDnCgxLjI5KTwvc3Bhbj48L2Rpdj48L3RkPjwvdHI+PHRyIGlkPScxNDEyMTctMDAyJz4KCQkJCQkJCQkJCQkJCTx0ZCB3aWR0aD0nMTUlJyByb3dzcGFuPScyJz7lkagzPGJyPjAwMjwvdGQ+CgkJCQkJCQkJCQkJCQk8dGQgd2lkdGg9JzcwJSc+5ZCJ57u05qOu54m5KDxmb250IGNvbG9yPSdncmVlbic+IC0xLjA8L2ZvbnQ+KTxlbSBjbGFzcz0nZm9udFNpemUwNyc+VlM8L2VtPuS9qee6s+iPsuiAtuWwlDwvdGQ+CgkJCQkJCQkJCQkJCQk8dGQgd2lkdGg9JzE1JScgY2Fsc3M9J3I5bGFzdCcgcm93c3Bhbj0nMic+5Y2KPGJyPuWFqDwvdGQ+PC90cj48dHI+PHRkPjxkaXYgY2xhc3M9J3RkbGVmdCBncmF5Jz4mbmJzcDs8c3BhbiBzdHlsZT0nY29sb3I6I2NjYzsnPuiDnCgxLjkyKTwvc3Bhbj48L2Rpdj48L3RkPjwvdHI+PC90Ym9keT48L3RhYmxlPjx0YWJsZSB3aWR0aD0nMTAwJScgYm9yZGVyPScwJyBjZWxscGFkZGluZz0nMCcgY2VsbHNwYWNpbmc9JzAnIGNsYXNzPSdsY2JldEZvb3Rlcic+PHRib2R5Pjx0cj48dGQ+6L+H5YWz5pa55byPOiAgIDLkuLIxPC90ZD48L3RyPjwvdGJvZHk+PC90YWJsZT48L2Rpdj4=";

	protected View header;
	private View footer;

	protected LinearLayout Lottery_number_llayout;// 开奖号码
	protected LinearLayout header_llayout0;// 普通彩种开奖 headerView
	protected LinearLayout jcheader_llayout;// 普通彩种开奖 headerView
	private TextView betting_detail_tv_lottery_name;
	private TextView betting_detail_tv_award_money;
	private TextView betting_detail_tv_issue;
	private TextView betting_detail_tv_order_amount;
	private TextView betting_detail_tv_order_state;
	private TextView betting_detail_tv_order_award;

	private TextView betting_detail_tv_order_time;
	private TextView betting_detail_tv_order_num;
	private TextView betting_detail_notice_tv;
	private TextView betting_detail_tv_verify;
	private TextView betting_detail_award_num;
	private Button betting_detail_btn_award_intro;
	private Button betting_detail_btn_cancel_pay;// 取消支付
	private Button betting_detail_btn_pay;// 立即支付

	private TextView jc_lottery_name;// 竞彩玩法名称
	private TextView jc_winAprize;// 竞彩中奖金额
	private TextView jc_ordermoney;// 竞彩订单金额
	private TextView jc_orderstate;// 竞彩订单状态
	private TextView jc_allbetinfo;// 竞彩订单信息
	private String awardNums;
	private Dialog mDialog;
	private String isDanguan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDialog = new Dialog(mActivity, R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		Bundle myBundle = getMyBundle();
		orderId = myBundle.getString("orderId");
		setTitleText("投注详情");
		setContentView(R.layout.f_betting_detail_rl);
		LinearLayout ll_container = (LinearLayout) findViewById(R.id.betting_detail_ll_container);
		ll_container.addView(mListView);
		mListView.setDividerHeight(0);
		mListView.setPullRefreshEnable(false);
		initView();
		initListener();
		// if(isJC(lotteryId)){//View.GONE
		// hide(Lottery_number_llayout);
		// hide(header_llayout0);
		// show(jcheader_llayout);//竞彩header 布局
		// mListView.setDivider(new
		// ColorDrawable(getResources().getColor(R.color.view_line)));//#cecece//分割线
		// }else{
		// hide(jcheader_llayout);//竞彩header 布局
		// show(Lottery_number_llayout);
		// show(header_llayout0);
		// }
		// Bundle[{orderState=等待付款, orderAward=0, order_state=0,
		// orderAmount=2000, lotteryId=218, issue=20000, orderTime=2014-12-17
		// 10:21, orderId=qc1412171021515623, lotteryName=竞彩_胜平负}]
		if (myBundle != null && myBundle.containsKey("gson_msg")
				&& !StringUtils.isBlank(myBundle.getString("gson_msg"))) {// 从追号详情中过来
			Message message = new Message();
			message.arg1 = 0;
			message.obj = new Gson().fromJson(myBundle.getString("gson_msg"),
					MessageBean.class);
			onMessageReceived(message);
		} else {// 不是从追号详情中过来
			sendRequest();
		}

		if (!StringUtils.isBlank(lotteryId)
				&& Arrays
						.asList(MyApp.res.getStringArray(R.array.jczq_lottery))
						.contains(lotteryId)) {
			hide(Lottery_number_llayout);// 竞彩 开奖号码 不在此处显示
		}
		addHeadAndFoot();
	}

	@Override
	protected void onReLogin() {
		super.onReLogin();
		sendRequest();
	}

	public void switchLayout(int order_state) {
		betting_detail_btn_pay.setVisibility(View.VISIBLE);
		betting_detail_btn_cancel_pay.setVisibility(View.VISIBLE);
		switch (order_state) {
		case ORDER_STATE_NOT_FINISHED:
			betting_detail_btn_cancel_pay.setText("取消订单");
			betting_detail_btn_pay.setText("立即支付");
			betting_detail_btn_cancel_pay
					.setBackgroundResource(R.drawable.btn_bg_gray_selector);
			betting_detail_btn_cancel_pay.setTextColor(Color.BLACK);
			break;
		default:
			betting_detail_btn_cancel_pay.setText("继续投注本次号码");
			betting_detail_btn_pay.setText(lotteryName + "投注");
			if ((!StringUtils.isBlank(lotteryId)&& Arrays.asList(MyApp.res.getStringArray(R.array.jczq_lottery)).contains(lotteryId))||
					(!StringUtils.isBlank(lotteryId)&& Arrays.asList(MyApp.res.getStringArray(R.array.jclq_lottery)).contains(lotteryId))) {
				hide(betting_detail_btn_cancel_pay);// 竞彩 不支持继续购买本次
													// 只允许在选号界面去重新选票
			}
			break;
		}
	}

	private void initView() {
		betting_detail_btn_pay = (Button) findViewById(R.id.betting_detail_btn_pay);
		betting_detail_btn_cancel_pay = (Button) findViewById(R.id.betting_detail_btn_cancel_pay);
		betting_detail_btn_cancel_pay.setVisibility(View.GONE);
		betting_detail_btn_pay.setVisibility(View.GONE);
		initHeaderViews();
		initFooterViews();
	}

	private void initHeaderViews() {
		header = View.inflate(mActivity, R.layout.f_bettting_detail_title1,
				null);
		header_llayout0 = (LinearLayout) header
				.findViewById(R.id.header_llayout0);
		jcheader_llayout = (LinearLayout) header
				.findViewById(R.id.jcheader_llayout);
		Lottery_number_llayout = (LinearLayout) header
				.findViewById(R.id.Lottery_number_llayout);
		betting_detail_tv_lottery_name = (TextView) header
				.findViewById(R.id.betting_detail_tv_lottery_name);
		betting_detail_tv_award_money = (TextView) header
				.findViewById(R.id.betting_detail_tv_award_money);
		betting_detail_tv_issue = (TextView) header
				.findViewById(R.id.betting_detail_tv_issue);
		betting_detail_tv_order_amount = (TextView) header
				.findViewById(R.id.betting_detail_tv_order_amount);
		betting_detail_tv_order_state = (TextView) header
				.findViewById(R.id.betting_detail_tv_order_state);
		betting_detail_tv_order_award = (TextView) header
				.findViewById(R.id.betting_detail_tv_order_award);
		betting_detail_award_num = (TextView) header
				.findViewById(R.id.betting_detail_award_num);
		// 竞彩
		jc_lottery_name = (TextView) header.findViewById(R.id.jc_lottery_name);// 竞彩玩法名称
		jc_winAprize = (TextView) header.findViewById(R.id.jc_winAprize);// 竞彩中奖金额
		jc_ordermoney = (TextView) header.findViewById(R.id.jc_ordermoney);// 竞彩订单金额
		jc_orderstate = (TextView) header.findViewById(R.id.jc_orderstate);// 竞彩订单状态
		jc_allbetinfo = (TextView) header.findViewById(R.id.jc_allbetinfo);// 竞彩订单信息
		// 玩法名称加粗
		betting_detail_tv_lottery_name.getPaint().setFakeBoldText(true);
		jc_lottery_name.getPaint().setFakeBoldText(true);

	}

	protected void initFooterViews() {
		footer = View.inflate(mActivity, R.layout.f_betting_detail_foot, null);
		betting_detail_tv_order_time = (TextView) footer
				.findViewById(R.id.betting_detail_tv_order_time);
		betting_detail_tv_order_num = (TextView) footer
				.findViewById(R.id.betting_detail_tv_order_num);
		betting_detail_notice_tv = (TextView) footer
				.findViewById(R.id.betting_detail_notice_tv);
		betting_detail_btn_award_intro = (Button) footer
				.findViewById(R.id.betting_detail_btn_award_intro);
		betting_detail_tv_verify = (TextView) footer
				.findViewById(R.id.betting_detail_tv_verify);

		if (orderId.startsWith("IMI")) {
			betting_detail_notice_tv.setText(MyApp.res
					.getString(R.string.betting_detail_tv_notice_moni));
		} else {
			betting_detail_notice_tv.setText(MyApp.res
					.getString(R.string.betting_detail_tv_notice));
		}
		if (!"1".equals(UserBean.getInstance().getUserAccountState())
				&& !"3".equals(UserBean.getInstance().getUserAccountState())) {
			setVisibility(betting_detail_tv_verify, View.VISIBLE);
			betting_detail_tv_verify.setText(Html
					.fromHtml(getString(R.string.betting_detail_tv_alert)));
		} else {
			setVisibility(betting_detail_tv_verify, View.GONE);
		}
		betting_detail_tv_order_time.setText(orderTime);

		betting_detail_tv_order_num.setText(orderId);

		betting_detail_tv_verify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle mbBundle = new Bundle();
				mbBundle.putInt(MyApp.res.getString(R.string.cmd), 1001);
				start(SecondActivity.class, PhoneVerify.class, mbBundle);
				// start(ThirdActivity.class, PhoneVerify.class, null);
			}
		});
		betting_detail_btn_award_intro
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						if (AppUtil.isNumeric(lotteryId)) {
							if (Arrays
									.asList(MyApp.res
											.getStringArray(R.array.jczq_lottery))
									.contains(lotteryId)) {// 竞彩
								bundle.putString("title", "竞彩足球玩法说明");
								bundle.putString("url",
										"file:///android_asset/help/pl310.htm");
							} else if (Arrays
									.asList(MyApp.res
											.getStringArray(R.array.jclq_lottery))
									.contains(lotteryId)) {
								bundle.putString("title", "竞彩篮球玩法说明");
								bundle.putString("url",
										"file:///android_asset/help/pl321.html");
							} else {
								bundle.putString(
										"title",
										CustomTagEnum.getItemByLotteryId(
												Integer.parseInt(lotteryId))
												.getLotteryName()
												+ "玩法说明");
								if ("120".equals(lotteryId)
										|| "122".equals(lotteryId)
										|| "123".equals(lotteryId)) {
									bundle.putString("url",
											"file:///android_asset/help/pl120.html");
								} else if ("112".equals(lotteryId)
										|| "113".equals(lotteryId)) {
									bundle.putString("url",
											"file:///android_asset/help/pl112.html");
								} else {
									bundle.putString("url",
											"file:///android_asset/help/pl"
													+ lotteryId + ".html");
								}
							}

							start(ThirdActivity.class, TCWebFragment.class,
									bundle);
						}
					}
				});
	}

	/**
	 * 判断是否为竞彩足球
	 * 
	 * @param lotteryId
	 * @return
	 */
	public static boolean isJC(String lotteryId) {
		boolean b = false;
		String[] jczq = MyApp.res.getStringArray(R.array.jczq_lottery);
		if (StringUtils.isBlank(lotteryId)) {
			return b;
		} else if (jczq != null) {
			for (int i = 0; i < jczq.length; i++) {
				if (jczq[i].equals(lotteryId)) {
					b = true;
				}
			}
		}
		return b;
	}

	private void initListener() {

		betting_detail_btn_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (order_state == ORDER_STATE_NOT_FINISHED) {// 立即支付
					if (orderAmount > 0) {
						onPay(orderId, issue);
					} else {
						Toast.makeText(mActivity, "支付信息异常", 0).show();
					}
				} else if ("14004".equals(order_state)) {
					Toast.makeText(mActivity, "玩法已期截", 0).show();
					return;
				} else {// 投注
					MyApp.backToTCActivity();
					try {
					// 竞彩
						if (!StringUtils.isBlank(lotteryId)&& Arrays.asList(MyApp.res.getStringArray(R.array.jczq_lottery)).contains(lotteryId)) {
							if("101".equals(isDanguan)){
								CustomTagEnum.startActivity(MyApp.activityList.get(0),null, CustomTagEnum.getItemByLotteryId(320));// 足彩单关
							}else{
								CustomTagEnum.startActivity(MyApp.activityList.get(0),null, CustomTagEnum.getItemByLotteryId(310));// 足彩非单关
							}
						
						}else if(!StringUtils.isBlank(lotteryId)&& Arrays.asList(MyApp.res.getStringArray(R.array.jclq_lottery)).contains(lotteryId)){
							CustomTagEnum.startActivity(MyApp.activityList.get(0),null, CustomTagEnum.getItemByLotteryId(321));// 篮彩
						}else{
							CustomTagEnum.startActivity(MyApp.activityList.get(0),null, CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)));
						}
					} catch (Exception e) {
					}
				}
			}
		});
		betting_detail_btn_cancel_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final MessageJson messageJson = new MessageJson();
				messageJson.put("A", orderId);
				if (order_state == ORDER_STATE_NOT_FINISHED) {// 取消订单
					TCDialogs dialogs = new TCDialogs(mActivity);
					dialogs.popCancelOrder(new OkClickedListener() {
						@Override
						public void okClicked() {
							submitData(1, 1211, messageJson);
						}
					});
				} else {// 继续投注本次号码
					messageJson.put("A",FormatUtil.timeFormat(System.currentTimeMillis()));
					messageJson.put("B",CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getIssue());
					messageJson.put("C", orderId);
					submitData(2, 1210, messageJson);
				}
			}
		});
	}

	protected void sendRequest() {// 查询订单详情
		MessageJson messageJson = new MessageJson();
		messageJson.put("A", orderId);
		submitData(0, 1207, messageJson);
	}

	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0:
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			MessageBean headBean = null;
			if (!StringUtils.isBlank(messageBean.getK())) {
				headBean = new Gson().fromJson(messageBean.getK(),
						MessageBean.class);
			}
			orderId = headBean.getA();// 订单流水
			lotteryId = headBean.getB();// 玩法代码
			lotteryName = headBean.getB1();// 玩法名称
			issue = headBean.getC();// 玩法期号
			orderTime = headBean.getD();// 查询时间
			orderAmount = Long.parseLong(headBean.getE());// 金额
			orderAward = headBean.getG();
			htmlInfo = messageBean.getG3();
			
			try {
			if (!StringUtils.isBlank(lotteryId)&& Arrays.asList(MyApp.res.getStringArray(R.array.jczq_lottery)).contains(lotteryId)) {
				if(!AppUtil.isEmpty(messageBean.getO())){ //把单关的子玩法代码取出来
					JSONArray jsonArray = new JSONArray(messageBean.getO());
					JSONObject jsonObj = jsonArray.getJSONObject(0);
					Object object = jsonObj.get("A");
					isDanguan = object.toString();
				}
			}
			
//			if (!StringUtils.isBlank(lotteryId)&& Arrays.asList(MyApp.res.getStringArray(R.array.jclq_lottery)).contains(lotteryId)) {
//				if(!AppUtil.isEmpty(messageBean.getO())){ //把单关的子玩法代码取出来
//					JSONArray jsonArray = new JSONArray(messageBean.getO());
//					JSONObject jsonObj = jsonArray.getJSONObject(0);
//					Object object = jsonObj.get("A");
//					isDanguan = object.toString();
//				}
//			}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msgObj = messageBean;
			orderState = headBean.getH0();// 中文状态描述
			order_state = Integer.parseInt(headBean.getH());// 状态码
			awardNums = messageBean.getLIST1().get(0).getG();// 开奖号码的获取
			if (getMyBundle() != null
					&& getMyBundle().containsKey("gson_msg")
					&& !StringUtils
							.isBlank(getMyBundle().getString("gson_msg"))) {// 从追号详情中过来
				issue = getMyBundle().getString("A");
				orderState = getMyBundle().getString("C0");
				orderAward = getMyBundle().getString("D");
				awardNums = getMyBundle().getString("G");// 开奖号码
				if (getMyBundle().containsKey("F")
						&& StringUtils.isBlank(getMyBundle().getString("F"))) {
					orderAmount = Integer
							.parseInt(getMyBundle().getString("F"));
				}
			}
			initHeadData();
			switchLayout(order_state);
			onOrderReceived(messageBean);
			AccountEnum.convertMessage(messageBean.getLISTVALUE());
			UserBean.getInstance().setAvailableMoney(
					Long.parseLong(messageBean.getH()));
			UserBean.getInstance().setAvailableCash(
					Long.parseLong(messageBean.getI()));
			UserBean.getInstance().setAvailableBalance(
					Long.parseLong(messageBean.getJ()));
			mListView.stopRefresh();

			break;
		case 1:// 取消订单
			Toast.makeText(mActivity, "成功取消订单", 0).show();
			finish();
			break;
		case 2:// 继续支付
			if (orderAmount > 0) {
				onPay(messageBean.getC(),
						CustomTagEnum.getItemByLotteryId(
								Integer.parseInt(lotteryId)).getIssue());
			} else {
				Toast.makeText(mActivity, "支付信息异常", 0).show();
			}
			break;
		}
	}

	/**
	 * 继续支付 和 立即支付 时 逻辑是一样的,只是 orderId 不同
	 * 
	 * @param orderId
	 */
	public void onPay(String orderId, String issue) {
		if (AppUtil.isEmpty(issue)) {
			Toast.makeText(mActivity, "无法获取到有效期次", 0).show();
			return;
		}
		Bundle bundle = obtainBundle(orderId, issue);
		if (orderId.startsWith("IMI")) {// IMI开头的订单为模拟投注的订单
			start(ThirdActivity.class, PayIntegral.class, bundle);// 积分支付
		} else {
			start(ThirdActivity.class, PayNormal.class, bundle);// 继续支付
		}
		finish();
	}

	public Bundle obtainBundle(String orderId, String issue) {
		Bundle payBundle = new Bundle();
		// payBundle.putString("lotteryId", lotteryId);
		payBundle.putString("orderId", orderId);// 1107接口需要的 订单号

		payBundle.putString("money", orderAmount + "");// 1107
														// 接口需要的第三方支付金额,这里其实是支付的总金额
		// payBundle.putString("availableAmount",
		// availableAmount);//用于确定是否进行红包支付
		payBundle.putString("lotteryId", lotteryId);// 支付中需要的 彩票的彩种和期数
		payBundle.putString("issue", issue);// 支付中需要的期数
		payBundle.putInt(MyApp.res.getString(R.string.cmd), 1107);// 支付中需要的期数
		return payBundle;
	}

	/*
	 * bundle.putString("money", orderAmount*100+"");
	 * bundle.putString("lotteryId", lotteryId); bundle.putString("orderId",
	 * messageBean.getC()); bundle.putString("issue", issue);
	 * bundle.putInt(getString(R.string.cmd), 1107);
	 * bundle.putString("availableCash",
	 * UserBean.getInstance().getAvailableMoney()+"");
	 */

	/**
	 * 在 onMessageReceived 中获取到Order 数据
	 * 
	 * @param messageBean
	 */
	protected void onOrderReceived(MessageBean messageBean) {
		mList = messageBean.getLIST();// 获得的订单详情
		mListSize = mList.size();
		mAdapter = new BettingDetailAdapter();
		mListView.setAdapter(mAdapter);
	}

	// 初始化头部信息
	private void initHeadData() {
		// 非竞彩情况下
		betting_detail_tv_lottery_name.setText(lotteryName);
		setTitleText(lotteryName + "投注详情");
		betting_detail_tv_issue.setText(issue);
		if (orderId.startsWith("IMI")) {
			betting_detail_tv_award_money.setText("· 订单积分：");
			// betting_detail_tv_order_amount.setText(orderAmount/100+"积分");
			betting_detail_tv_order_amount.setText(FormatUtil
					.moneyFormat2String(Double.parseDouble(AccountUtils
							.fenToyuan(orderAmount + "")))
					+ "积分");
		} else {
			betting_detail_tv_award_money.setText("· 订单金额：");
			betting_detail_tv_order_amount.setText("¥"
					+ FormatUtil.moneyFormat2String(Double
							.parseDouble(AccountUtils.fenToyuan(orderAmount
									+ ""))));
		}
		betting_detail_tv_order_state.setText(orderState);
		if (AppUtil.isNumeric(orderAward) && Integer.parseInt(orderAward) > 0) {
			if (orderId.startsWith("IMI")) {
				orderAward = FormatUtil.moneyFormat2String(Double
						.parseDouble(AccountUtils.fenToyuan(orderAward)))
						+ "积分";
			} else {
				orderAward = "¥"
						+ FormatUtil
								.moneyFormat2String(Double
										.parseDouble(AccountUtils
												.fenToyuan(orderAward)));
			}
		} else {
			orderAward = "——";
		}

		betting_detail_tv_order_award.setText(orderAward);

		betting_detail_tv_order_time.setText(orderTime);// foot的数据,为了方便,就放这.
														// 但是这样可读性不好

		// foot 部分的开奖号码的显示
		if (awardNums.contains("+")) {
			awardNums = awardNums.replace("+",
					"&#160;&#160;<font color=#007aff>");
			awardNums += "</font>";
		}

		if ("121".equals(lotteryId)) {// 快乐扑克 特殊显示方式
			if (AppUtil.isEmpty(awardNums) || !awardNums.contains(",")) {
				return;
			}
			Lottery_number_llayout.removeAllViews();
			Lottery_number_llayout.setOrientation(LinearLayout.HORIZONTAL);
			if (AppUtil.isEmpty(awardNums)) {
				hide(Lottery_number_llayout);
			} else {
				show(Lottery_number_llayout);
			}
			TextView num_tv0 = new TextView(mActivity);
			num_tv0.setText("本期开奖号码:");
			num_tv0.setPadding(30, 0, 0, 0);
			TextView num_tv1 = new TextView(mActivity);
			TextView num_tv2 = new TextView(mActivity);
			TextView num_tv3 = new TextView(mActivity);
			TextView text_tv = new TextView(mActivity);
			num_tv1.setGravity(Gravity.CENTER | Gravity.RIGHT);// 动态调整位置
			num_tv2.setGravity(Gravity.CENTER | Gravity.RIGHT);
			num_tv3.setGravity(Gravity.CENTER | Gravity.RIGHT);

			PL121.showLotteryKLPK(num_tv1, num_tv2, num_tv3, text_tv, awardNums); // 调用了
																					// 快乐扑克玩法中的
																					// 显示历史号码的方法
			Lottery_number_llayout.addView(num_tv0);
			Lottery_number_llayout.addView(num_tv1);
			Lottery_number_llayout.addView(num_tv2);
			Lottery_number_llayout.addView(num_tv3);
			Lottery_number_llayout.addView(text_tv);
			return;
		}
		// 其它普通彩种
		awardNums = awardNums.replace(",", "&#160;&#160;");
		if (AppUtil.isEmpty(awardNums)) {
			hide((View) betting_detail_award_num.getParent());
		} else {
			show((View) betting_detail_award_num.getParent());
			betting_detail_award_num.setText(Html.fromHtml(awardNums));
		}
	}

	protected void addHeadAndFoot() {
		mListView.addHeaderView(header);
		mListView.addFooterView(footer);
	}

	@Override
	protected void initData() {
	}

	class BettingDetailAdapter extends BaseListAdapter {
		@Override
		public int getCount() {
			if (Arrays.asList(MyApp.res.getStringArray(R.array.jczq_lottery))
					.contains(lotteryId)) {// 特殊删除 1207
											// 返回的LIST中的拆单票前端没有用到(投注订单内容的显示调用的h5)
				// for (int i = 1; i < mList.size(); i++) {
				// mList.remove(i);
				// }
				MessageBean mb = null;
				if (mList.size() > 0)
					mb = mList.get(0);
				else
					mb = mList_copy.get(0);
				mList = new ArrayList<MessageBean>();
				mList.add(mb);
				if(isChecked)
					return 2;
				else
					return 1;
			}
			return mList.size() + 1;// 选号详情
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHodler holder;
			String saleMode = "--", beiD = "--";
			if (convertView != null && convertView.getTag() != null) {// 如果可以复用,满足
																		// getTag()
																		// 中有设置过
				holder = (ViewHodler) convertView.getTag();
			} else {
				convertView = View.inflate(mActivity,
						R.layout.f_item_betting_detail, null);
				holder = new ViewHodler(convertView);
				convertView.setTag(holder);
			}
			// 对不可见性的初始化
			holder.betting_detail_cb.setVisibility(View.GONE);// 默认组栏
			holder.betting_detail_ll_bet_num.setVisibility(View.GONE);// 普通
			holder.betting_detail_ll_order.setVisibility(View.GONE);// 追号
			holder.betting_jcbet_layout.setVisibility(View.GONE);// 竞彩

			if (position == 0) {
				if (Arrays.asList(
						MyApp.res.getStringArray(R.array.jczq_lottery))
						.contains(lotteryId)) {
					holder.betting_detail_cb.setText("选购详情1条");
				} else {
					holder.betting_detail_cb
							.setText("选购详情  " + mListSize + "条");
				}
				show(holder.betting_detail_cb);
				holder.betting_detail_cb.setChecked(isChecked);
				holder.betting_detail_cb
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (isChecked = !isChecked) {
									mList = mList_copy;
								} else {
									mList_copy = mList;
									mList = new ArrayList<MessageBean>();
								}
								mAdapter.notifyDataSetChanged();
							}
						});
				return convertView;
			}
			// 背景图片的初始化
			holder.betting_detail_ll_bet_num.setVisibility(View.VISIBLE);
			holder.betting_detail_ll_bet_num
					.setBackgroundResource(R.drawable.btn_white_bg_no_bottom);
			if (position == mList.size()) {
				holder.betting_detail_ll_bet_num
						.setBackgroundResource(R.drawable.btn_white_bg_no_bottom1);
			}

			MessageBean item = mList.get(position - 1);
			saleMode = item.getA1();
			if ("118".equals(lotteryId) || "106".equals(lotteryId)
					|| "103".equals(lotteryId) || "100".equals(lotteryId)
					|| "102".equals(lotteryId)) {// 濡傛灉鏄弻鑹茬悆,澶т箰閫忕瓑鐨勭敤鐨勬槸閿�敭鏂瑰紡,鑰屼笉鏄�瀛愮帺娉曞悕绉�
				saleMode = item.getB1();
			}

			if (!StringUtils.isBlank(item.getD())) {
				beiD = item.getD();
			}
			holder.betting_detail_tv_sale_mode.setText(saleMode + "(" + beiD
					+ "倍)");
			holder.betting_detail_tv_bet_num
					.setText(Html.fromHtml(item.getC1()));// C1 是格式化的字符串 html形式的
			// 竞彩拆单的特殊性 导致mList.size()与实际的显示需要有冲突
			if ((Arrays.asList(MyApp.res.getStringArray(R.array.jczq_lottery))
					.contains(item.getG())||Arrays.asList(MyApp.res.getStringArray(R.array.jclq_lottery))
					.contains(item.getG())) && !StringUtils.isBlank(htmlInfo)) {// &&
																				// !StringUtils.isBlank(htmlInfo)){//竞彩
				// 解码
				String data = "<html><body bgcolor='#ffffffff'>"
						+ "<link href='file:///android_asset/help/bass.css' rel='stylesheet' type='text/css'>"
						+ "<link href='file:///android_asset/help/fc.css' rel='stylesheet' type='text/css'>"
						+ new String(Base64.decode(htmlInfo))
						+ "</body></html>";
				holder.jcbet_info_web.loadDataWithBaseURL("", data,
						"text/html", "UTF-8", "");
				// 展示html
				show(holder.betting_jcbet_layout);
				show(holder.jcbet_info_web);
				hide(holder.betting_detail_ll_bet_num);
				hide(holder.betting_detail_tv_sale_mode);
			}
			// //获得当前的item
			// MessageBean item=null;
			// // if(mList!=null && -1!=mList.indexOf(position-1)){
			// // item = mList.get(position-1);
			// // }
			// item = mList.get(position);
			// if(item==null){return convertView;}
			// CustomTagEnum cte_item=null;
			// if(!StringUtils.isBlank(item.getA())){
			// cte_item=CustomTagEnum.getItemById(Integer.parseInt(item.getA()));
			// }
			// if(cte_item==null){return convertView;}
			// //划分出竞彩mListView 需要设置item 分割线
			// // if(!StringUtils.isBlank(item.getA()) && isJC(item.getA())){
			// holder.betting_jcbet_layout.setVisibility(View.VISIBLE);
			// holder.jc_startmsg.setText(Html.fromHtml(MyApp.res.getString(R.string.jc_startmsg)));//比赛开始时间_场次
			// holder.jc_isDan.setText("胆");//是否设置为胆码
			// holder.jc_vsmsg.setText(Html.fromHtml(MyApp.res.getString(R.string.jc_userBetmsg)));//对阵信息
			// holder.jc_betmsg.setText("胜0.00");//投注信息
			// holder.jc_resultmsg.setText("--");//彩果
			// // }
			//
			// holder.betting_detail_ll_bet_num.setVisibility(View.VISIBLE);
			// //背景图片的初始化
			// holder.betting_detail_ll_bet_num.setBackgroundResource(R.drawable.btn_white_bg_no_bottom);
			// if(position == mList.size()){
			// holder.betting_detail_ll_bet_num.setBackgroundResource(R.drawable.btn_white_bg);
			// }
			// if(!StringUtils.isBlank(item.getA1())){
			// saleMode= item.getA1();
			// }
			// if(!StringUtils.isBlank(item.getD())){
			// beiD = item.getD();
			// }
			// if("118".equals(lotteryId) || "106".equals(lotteryId) ||
			// "103".equals(lotteryId) || "100".equals(lotteryId) ||
			// "102".equals(lotteryId)){//如果是双色球,大乐透等的用的是销售方式,而不是 子玩法名称
			// saleMode = item.getB1();
			// }
			// holder.betting_detail_tv_sale_mode.setText(saleMode+ beiD +"倍");
			// holder.betting_detail_tv_bet_num.setText(Html.fromHtml(item.getC1()));//C1
			// 是格式化的字符串 html形式的

			return convertView;
		}

	}

	private String numBertext = "";

	class ViewHodler {
		CheckBox betting_detail_cb;
		// CheckBox betting_detail_cb_unfinished;
		LinearLayout betting_detail_ll_bet_num;
		LinearLayout betting_detail_ll_order;
		TextView betting_detail_tv_sale_mode;
		TextView betting_detail_tv_bet_num;
		TextView betting_detail_tv_date;
		TextView betting_detail_tv_issue;
		TextView betting_detail_tv_bet_money;
		TextView betting_detail_tv_award_time;
		TextView chase_detail_tv_award_money;
		// 竞彩单独使用
		LinearLayout betting_jcbet_layout;
		MWebView jcbet_info_web;

		// TextView jc_resultmsg;
		// TextView jc_betmsg;
		// TextView jc_vsmsg;
		// TextView jc_isDan;
		// TextView jc_startmsg;
		public ViewHodler(View view) {
			betting_detail_cb = (CheckBox) view
					.findViewById(R.id.betting_detail_cb);// 组栏
			betting_detail_ll_bet_num = (LinearLayout) view
					.findViewById(R.id.betting_detail_ll_bet_num);// 默认投注
			betting_detail_ll_order = (LinearLayout) view
					.findViewById(R.id.betting_detail_ll_order);// 追号
			betting_jcbet_layout = (LinearLayout) view
					.findViewById(R.id.jcbet_layout);// 竞彩

			betting_detail_tv_sale_mode = (TextView) view
					.findViewById(R.id.betting_detail_tv_sale_mode);
			betting_detail_tv_bet_num = (TextView) view
					.findViewById(R.id.betting_detail_tv_bet_num);
			betting_detail_tv_date = (TextView) view
					.findViewById(R.id.betting_detail_tv_date);
			betting_detail_tv_issue = (TextView) view
					.findViewById(R.id.betting_detail_tv_issue);
			betting_detail_tv_bet_money = (TextView) view
					.findViewById(R.id.betting_detail_tv_bet_money);
			betting_detail_tv_award_time = (TextView) view
					.findViewById(R.id.betting_detail_tv_award_time);
			chase_detail_tv_award_money = (TextView) view
					.findViewById(R.id.chase_detail_tv_award_money);
			// 竞彩专用
			jcbet_info_web = (MWebView) view.findViewById(R.id.jcbet_info_web);
			jcbet_info_web.getSettings().setDomStorageEnabled(true);
			jcbet_info_web.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
			String appCachePath = mActivity.getCacheDir().getAbsolutePath();
			jcbet_info_web.getSettings().setAppCachePath(appCachePath);
			jcbet_info_web.getSettings().setAllowFileAccess(true);
			jcbet_info_web.getSettings().setAppCacheEnabled(true);

			// jc_resultmsg = (TextView) view.findViewById(R.id.jc_resultmsg);
			// jc_betmsg = (TextView) view.findViewById(R.id.jc_betmsg);
			// jc_vsmsg = (TextView) view.findViewById(R.id.jc_vsmsg);
			// jc_isDan = (TextView) view.findViewById(R.id.jc_isDan);
			// jc_startmsg = (TextView) view.findViewById(R.id.jc_startmsg);
		}

	}
}
