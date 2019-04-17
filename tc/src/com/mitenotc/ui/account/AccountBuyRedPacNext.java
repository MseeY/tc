package com.mitenotc.ui.account;

import org.apache.commons.codec.digest.DigestUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

public class AccountBuyRedPacNext extends BaseFragment implements OnClickListener{

	private TextView acc_title_text1;
	private TextView acc_title_text2;
	private TextView acc_title_content1;
	private TextView acc_title_content2;
	private TextView acc_title_content5;
	private LinearLayout ll_pay_other_method;
	private Button btn_payorder_on_pay;
	private TextView payorder_show_available_amount;
	private TextView orderpay_show_amount;
	private TextView orderpay_forget_pwd;
	private Bundle myBundle;
	private String redPacValue;
	private String inputValue;
	private EditText input_pwd;
	private LinearLayout ll_pay_need_pwd;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_normal);
		setTitleNav("账户余额买红包", R.drawable.title_nav_back, 0);
		initView();
		initBodyView();
	}

	private void initView() {
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		//初始化头部信息
		acc_title_text1 = (TextView) findViewById(R.id.acc_title_text1);
		acc_title_text2 = (TextView) findViewById(R.id.acc_title_text2);
		acc_title_content1 = (TextView) findViewById(R.id.acc_title_content1);
		acc_title_content2 = (TextView) findViewById(R.id.acc_title_content2);
		acc_title_content5 = (TextView) findViewById(R.id.acc_title_content5);
		acc_title_content5.setVisibility(View.VISIBLE);
		acc_title_text1.setText("支付项目：");
		acc_title_text2.setText("红包面值：");
		acc_title_content1.setText("账户余额买红包");
	}
	
	private void initBodyView() {
		// TODO Auto-generated method stub
		ll_pay_other_method = (LinearLayout) findViewById(R.id.ll_pay_other_method);
		ll_pay_other_method.setVisibility(View.GONE);
		btn_payorder_on_pay = (Button) findViewById(R.id.btn_payorder_on_pay);
		btn_payorder_on_pay.setOnClickListener(this);
		btn_payorder_on_pay.setText("确 认 购 买");
		
		payorder_show_available_amount = (TextView) findViewById(R.id.payorder_show_available_amount);
		payorder_show_available_amount.setText("¥"+FormatUtil.moneyFormat2String(Double
				.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableCash()+""))));
		orderpay_show_amount = (TextView) findViewById(R.id.orderpay_show_amount);
		orderpay_show_amount.setOnClickListener(this);
		orderpay_forget_pwd = (TextView) findViewById(R.id.orderpay_forget_pwd);
		orderpay_forget_pwd.setOnClickListener(this);
		input_pwd = (EditText) findViewById(R.id.et_payorder_input_pwd);
		myBundle = getMyBundle();
		if(myBundle!=null){
			inputValue = myBundle.getString("INPUTVALUE");
			sendRequst(inputValue);
		}
		ll_pay_need_pwd = (LinearLayout) findViewById(R.id.ll_pay_need_pwd);
		
		boolean paypwd = SPUtil.getBoolean(MyApp.res.getString(R.string.PAYPWD1), true);
		if(paypwd){
			show(ll_pay_need_pwd);
		}else {
			hide(ll_pay_need_pwd);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.orderpay_show_amount:
			showAmount();
			break;
		case R.id.btn_payorder_on_pay:
			MessageJson msj = new MessageJson();
			msj.put("A", inputValue);
			msj.put("B", redPacValue);
			String pwd_ed_text = input_pwd.getText().toString().trim();
			Bundle bundle = getMyBundle();
			if(ll_pay_need_pwd.getVisibility() == View.VISIBLE){
				if(AppUtil.isEmpty(pwd_ed_text)){
					Toast.makeText(mActivity, "请输入账户密码", Toast.LENGTH_SHORT).show();
					return;
				}
					
				pwd_ed_text = DigestUtils.md5Hex(pwd_ed_text);
				bundle.putString("password", pwd_ed_text);
				msj.put("C", pwd_ed_text);
			}
			mDialog.show();
			submitData(1, 1120, msj);
			break;
		case R.id.orderpay_forget_pwd:
			Bundle forgetPswbundle = new Bundle();
			forgetPswbundle.putInt(getString(R.string.cmd), 1009);
			start(ThirdActivity.class,ForgetPwd.class,forgetPswbundle);
			break;
		}
	}

	private void sendRequst(String value) {
		// TODO Auto-generated method stub
		MessageJson msj = new MessageJson();
		msj.put("A", value);
		submitData(0, 1119, msj);
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean bean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0:
			if("0".equals(bean.getA())){
				redPacValue = bean.getC();
				String userValue = "¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(redPacValue)));
				String discountValue = FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(bean.getD())));
				acc_title_content2.setText(userValue);
				acc_title_content5.setText("（本次购买为您节省了"+discountValue+"元）");
				if(mDialog!=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				
			}else{
				Toast.makeText(mActivity, bean.getB(), 0).show();
			}
			break;
		case 1:
			if("0".equals(bean.getA())){
				UserBean.getInstance().setAvailableMoney(Long.parseLong(bean.getC()));
				UserBean.getInstance().setAvailableCash(Long.parseLong(bean.getD()));
				UserBean.getInstance().setAvailableBalance(Long.parseLong(bean.getE()));
				AccountEnum.convertMessage(bean.getLIST());
				if(mDialog!=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				start(ThirdActivity.class, AccountBuyRedPacSuccess.class, null);
			}else{
			    Toast.makeText(mActivity, bean.getB(), 0).show();
			}
			break;
		}
	}
	
	private void showAmount() {
		long available_money = UserBean.getInstance().getAvailableMoney();
		long red_packet_amount = AccountEnum.redpacket_account.getMoney();
		TCDialogs dialog = new TCDialogs(mActivity);
		TextView show_money_amount = dialog.getShow_money_amount();
		TextView show_redpac_amount = dialog.getShow_redpac_amount();
		show_money_amount.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(available_money+""))));
		show_redpac_amount.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(red_packet_amount+""))));
		dialog.popAmount();
	}

}
