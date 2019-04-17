package com.mitenotc.ui.account;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.pay.PayMobileRecharge;
import com.mitenotc.ui.pay.PayMoreway;
import com.mitenotc.ui.pay.PayPhoneRecharge;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 账户资金管理-----立即充值   界面
 * @author ymx
 */
public class RechargeFragment extends BaseFragment implements OnClickListener {

	private Button recharge_next;
	private TextView account_balance;
	private TextView phone_number;
	private EditText money;
	private TextView big_bank;
	private LinearLayout recharge_huafei;
	private TextView tv_acc_out_online_ask3;
	private TextView kefu_phone_number;
	private ImageView kefu_phone_image;
	private LinearLayout recharge_mobile;
	private TextView fengxian;
	private TextView titleText1;
	private TextView titleText2;
	private TextView acc_text_banquan1;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_prepaid_input_money1);
//		setTitleNav(R.string.acc_title_recharge, R.drawable.title_nav_back, 0);
		setTitleNav(CustomTagEnum.recharge.getId(),"账户充值", R.drawable.title_nav_back, R.drawable.title_nav_menu);
		initView();
		setRechargeListener();
		initUserName();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		MessageJson msg = new MessageJson();
		submitData(0, 1100, msg);
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean messageBean = (MessageBean) msg.obj;
		if("0".equals(messageBean.getA())){
			UserBean.getInstance().setAvailableMoney(Long.parseLong(messageBean.getC()));
			UserBean.getInstance().setAvailableCash(Long.parseLong(messageBean.getD()));
			UserBean.getInstance().setAvailableBalance(Long.parseLong(messageBean.getE()));
			AccountEnum.convertMessage(messageBean.getLIST());
			account_balance.setText("￥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
		}
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,CustomTagEnum.recharge.getId());
		setTitleNav(CustomTagEnum.recharge.getId(),"账户充值", R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}

	private void initUserName() {
		if(!UserBean.getInstance().isLogin()||"".equals(UserBean.getInstance().getPhoneNum())
				||UserBean.getInstance().getPhoneNum()==null){
			startLoginForResult();
			is_logining=1;
		}else{
			phone_number.setText(AccountUtils.phoneNumber(UserBean.getInstance().getPhoneNum()));
		}
	}
	

	private ScrollView sv_recharge;
	private Animation shake;
	private TextView rl_btn_tikuan_shuoming;
	private LinearLayout ll_acc_fund_extraction_text;
	private void initView() {
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		recharge_next = (Button) findViewById(R.id.bt_acc_fund_recharge_next);
		phone_number = (TextView) findViewById(R.id.chongzhi_content1);
		account_balance = (TextView) findViewById(R.id.chongzhi_content2);
		rl_btn_tikuan_shuoming = (TextView) findViewById(R.id.rl_btn_tikuan_shuoming);
		ll_acc_fund_extraction_text = (LinearLayout) findViewById(R.id.ll_acc_fund_extraction_text);
		sv_recharge = (ScrollView) findViewById(R.id.sv_recharge);

//		titleText1 = (TextView) findViewById(R.id.acc_title_text1);
//		titleText2 = (TextView) findViewById(R.id.acc_title_text2);
//		titleText1.setText("您的手机号码：");
//		titleText2.setText("当前账户余额：");
		recharge_huafei = (LinearLayout) findViewById(R.id.ll_recharge_huafei);
		tv_acc_out_online_ask3 = (TextView) findViewById(R.id.tv_acc_out_online_ask3);
		
		account_balance.setText("￥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
		money = (EditText) findViewById(R.id.tv_acc_extraction_input_money);
		big_bank = (TextView) findViewById(R.id.ll_big_bank);
		kefu_phone_number = (TextView) findViewById(R.id.tv_kefu_phone_number);
		kefu_phone_image = (ImageView) findViewById(R.id.iv_kefu_phone_number);
		recharge_mobile = (LinearLayout) findViewById(R.id.ll_recharge_mobile);
		fengxian = (TextView) findViewById(R.id.tv_touzi_text);
//		acc_text_banquan1 = (TextView) findViewById(R.id.acc_text_banquan1);
		if (ConstantValue.WholesalePay) {
			big_bank.setVisibility(View.VISIBLE);
		}else{
			big_bank.setVisibility(View.GONE);
		}
		
		if (ConstantValue.UnicomPhone) {
			recharge_huafei.setVisibility(View.VISIBLE);
		}else{
			recharge_huafei.setVisibility(View.GONE);
		}
		
//		if(!AppUtil.isEmpty(ConstantValue.copyright)){
//			acc_text_banquan1.setVisibility(View.VISIBLE);
//			acc_text_banquan1.setText(ConstantValue.copyright);
//		}else{
//			acc_text_banquan1.setVisibility(View.GONE);
//		}
		
		if(AppUtil.isEmpty(ConstantValue.serviceTel_format)){
			kefu_phone_image.setVisibility(View.GONE);
			kefu_phone_number.setVisibility(View.GONE);
		}else{
			kefu_phone_image.setVisibility(View.VISIBLE);
			kefu_phone_number.setVisibility(View.VISIBLE);
			kefu_phone_number.setText(Html.fromHtml("<u>"+ConstantValue.serviceTel_format1+"</u>"));//400-0328-666电话
		}
	}

	private void setRechargeListener() {
		rl_btn_tikuan_shuoming.setOnClickListener(this);
		recharge_next.setOnClickListener(this);
		big_bank.setOnClickListener(this);
		recharge_huafei.setOnClickListener(this);
		tv_acc_out_online_ask3.setOnClickListener(this);
		kefu_phone_number.setOnClickListener(this);
		kefu_phone_image.setOnClickListener(this);
		recharge_mobile.setOnClickListener(this);
		fengxian.setOnClickListener(this);
		money.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				AccountUtils.changedEdittext(recharge_next, s, count);
				
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_btn_tikuan_shuoming:
			switch (ll_acc_fund_extraction_text.getVisibility()) {
			case View.VISIBLE:
				ll_acc_fund_extraction_text.setVisibility(View.GONE);
				rl_btn_tikuan_shuoming.setText("温馨提示");
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_recharge.fullScroll(ScrollView.FOCUS_UP);
				    }
				});
				break;
			case View.GONE:
				ll_acc_fund_extraction_text.setVisibility(View.VISIBLE);
				rl_btn_tikuan_shuoming.setText("收起提示");
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_recharge.fullScroll(ScrollView.FOCUS_DOWN);
				    }
				});
				break;
			}
			break;
		case R.id.bt_acc_fund_recharge_next:
			
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}else{
				Bundle bundle=new Bundle();
				String trim = money.getText().toString().trim();
				if(AppUtil.isEmpty(trim)){
//					Toast.makeText(mActivity, "请输入您要充值的金额", Toast.LENGTH_SHORT).show();
					money.startAnimation(shake);
					return;
				}
				
				if(money.getText().toString().trim().startsWith("0")){
					Toast.makeText(mActivity, "您的充值金额不能为0", Toast.LENGTH_SHORT).show();
					money.setText("");
					return;
				}
				bundle.putString("money", AccountUtils.yuanTofen(money.getText().toString().trim()));
				bundle.putInt(MyApp.res.getString(R.string.cmd), 1101);
				bundle.putString("payDesc", "账户充值");
				start(ThirdActivity.class, PayMoreway.class, bundle);
			}
			
			break;
		case R.id.ll_big_bank:
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}else{
//				MyApp.saveProbeMsg("大额银行转账","按钮", System.currentTimeMillis()+"", "", "0", "");
				start(ThirdActivity.class, TransferAccounts.class, null);
			}
			break;
		case R.id.ll_recharge_huafei://联通话费充值
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}else{
				start(ThirdActivity.class, PayPhoneRecharge.class, null);
			}
			break;
		case R.id.tv_acc_out_online_ask3:
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}else{
				start(ThirdActivity.class, ConsultationFragment.class, null);
			}
			break;
		case R.id.tv_kefu_phone_number:
			AccountUtils.boda(mActivity, ConstantValue.serviceTel);
			break;
		case R.id.iv_kefu_phone_number:
			AccountUtils.boda(mActivity, ConstantValue.serviceTel);
			break;
		case R.id.ll_recharge_mobile://移动话费充值
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}else{
				start(ThirdActivity.class, PayMobileRecharge.class, null);
			}
			break;
		case R.id.tv_touzi_text:
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}else{
				start(ThirdActivity.class, BuyRedPacket.class, null);
			}
			break;
		}
	}
	
	@Override
	protected void onReLogin() {
		super.onReLogin();
		initUserName();
	}
}
