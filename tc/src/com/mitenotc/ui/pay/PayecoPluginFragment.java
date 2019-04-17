package com.mitenotc.ui.pay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 银联安全插件支付
 * @author mitenotc
 */
public class PayecoPluginFragment extends BasePayFragment{
	private LinearLayout payeco_voicepay_bank;
	private CheckBox voicepay_cb_chose_certificate_type;
	private CheckBox voicepay_cb_chose_open_bank_address;
	
	private EditText voicepay_et_name;
	private EditText voicepay_et_card_num;
	private EditText voicepay_et_phone_num;
	private EditText voicepay_et_certificate_num;
	private EditText voicepay_et_certificate_address;
	private EditText voicepay_et_beneficiary;
	private EditText voicepay_et_beneficiary_num;
	private TextView voicepay_notice;
	
	private View[] views;
	
	private TextView acc_title_text1;
	private TextView acc_title_content1;
	private TextView acc_title_text2;
	private TextView acc_title_content2;
	
	private Button voicepay_confirm_pay;
	
	private List<String>certificate_types; 
	
	private TextView zwl_play_periods_tv;
	private TextView zwl_tv_show_1;
	private TextView zwl_show_phoneNumber;
	
	private TextView zwl_more_payment_way_tv;
	
	private static final int LAYOUT_MODE_BASIC_REQUIRED_DATA = 0;//获取基本需要的数据的界面
	private static final int LAYOUT_MODE_USER_PREFERED_DATA = 2;//仅显示用户倾向的数据的界面
	private int mode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		certificate_types = Arrays.asList(getResources().getStringArray(R.array.certificate_type));
		setTitleText(MyApp.res.getString(R.string.paymethod_payeco_plugin));
		setContentView(R.layout.f_fragment_voicepay);
		initMyBundleData();
		initViews();
		if(savedBankInfos_payeco_plugin == null || savedBankInfos_payeco_plugin.size()<1){//如果语音支付还没有请求到成功的数据
			switchLayout(LAYOUT_MODE_BASIC_REQUIRED_DATA);
		}else {
			switchLayout(LAYOUT_MODE_USER_PREFERED_DATA);
		}
		setListeners();
		
		sendRequest_saved_bankinfo("02");
		registBroadCastReceiver();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unRegistBroadCastReceiver();
	}
	
	private void initMyBundleData() {
		Bundle bundle = getMyBundle();
		message = new MessageJson();
		message.put("A", bundle.getString("payMethod"));
		message.put("C", bundle.getString("money"));
		message.put("E", bundle.getString("orderId"));
		message.put("F", bundle.getString("redpacket_payment"));
		message.put("G", bundle.getString("cache_payment"));
		message.put("H", bundle.getString("password"));
	}
	
	@Override
	protected void savedBankinfoReceived() {
		switchLayout(LAYOUT_MODE_USER_PREFERED_DATA);
	}
	
	private void initViews() {
		
		acc_title_content1 = (TextView) findViewById(R.id.acc_title_content1);
		acc_title_content2 = (TextView) findViewById(R.id.acc_title_content2);
		
		payeco_voicepay_bank = (LinearLayout) findViewById(R.id.payeco_voicepay_bank);
		voicepay_cb_chose_certificate_type = (CheckBox) findViewById(R.id.voicepay_cb_chose_certificate_type);
		voicepay_cb_chose_open_bank_address = (CheckBox) findViewById(R.id.voicepay_cb_chose_open_bank_address);
		
		voicepay_et_name = (EditText) findViewById(R.id.voicepay_et_name);
		voicepay_et_card_num = (EditText) findViewById(R.id.voicepay_et_card_num);
		voicepay_et_phone_num = (EditText) findViewById(R.id.voicepay_et_phone_num);
		voicepay_et_certificate_num = (EditText) findViewById(R.id.voicepay_et_certificate_num);
		voicepay_et_certificate_address = (EditText) findViewById(R.id.voicepay_et_certificate_address);
		voicepay_et_beneficiary = (EditText) findViewById(R.id.voicepay_et_beneficiary);
		voicepay_et_beneficiary_num = (EditText) findViewById(R.id.voicepay_et_beneficiary_num);
		
		voicepay_notice = (TextView) findViewById(R.id.voicepay_notice);
		voicepay_notice.setText(Html.fromHtml(getString(R.string.voicepay_notice)));
		
		
//		zwl_play_periods_tv = (TextView) findViewById(R.id.zwl_play_periods_tv);
//		zwl_tv_show_1 = (TextView) findViewById(R.id.zwl_tv_show_1);
//		zwl_show_phoneNumber = (TextView) findViewById(R.id.zwl_show_phoneNumber);
		zwl_more_payment_way_tv = (TextView) findViewById(R.id.zwl_more_payment_way_tv);
		initHeaderDatas();
		
		voicepay_confirm_pay = (Button) findViewById(R.id.voicepay_confirm_pay);
	}
	
	public void switchLayout(int mode){
		this.mode = mode;
		switch (mode) {
		case LAYOUT_MODE_BASIC_REQUIRED_DATA:
			hideViews(zwl_more_payment_way_tv,payeco_voicepay_bank,voicepay_cb_chose_certificate_type,voicepay_et_certificate_address,voicepay_et_beneficiary,voicepay_et_beneficiary_num);
			showViews(voicepay_et_name,voicepay_et_card_num,voicepay_et_phone_num,voicepay_et_certificate_num,voicepay_cb_chose_open_bank_address);
			voicepay_et_name.setHint(getString(R.string.payeco_plugin_name));
			voicepay_et_card_num.setHint(getString(R.string.payeco_plugin_card_num));
			voicepay_et_phone_num.setHint(getString(R.string.payeco_plugin_phone_num));
			voicepay_et_certificate_num.setHint(getString(R.string.payeco_plugin_certificate_num));
			voicepay_cb_chose_open_bank_address.setHint(getString(R.string.payeco_plugin_certificate_type));
			break;
		case LAYOUT_MODE_USER_PREFERED_DATA:
			hideViews(voicepay_et_name,voicepay_et_card_num,voicepay_et_phone_num,voicepay_et_certificate_num,payeco_voicepay_bank,voicepay_cb_chose_certificate_type,voicepay_et_certificate_address,voicepay_et_beneficiary,voicepay_et_beneficiary_num);
			showViews(voicepay_cb_chose_open_bank_address,zwl_more_payment_way_tv);
			voicepay_cb_chose_open_bank_address.setHint(getString(R.string.voicepay_hint_chose_card));
			break;

		default:
			break;
		}
	}
	/**
	 * 初始化头部的信息
	 */
	private void initHeaderDatas() {
		Bundle myBundle = getMyBundle();
		String lotteryId = myBundle.getString("lotteryId");
		String payDesc = myBundle.getString("payDesc");
		if(!AppUtil.isEmpty(lotteryId)){
			acc_title_content1.setText(Html.fromHtml(MyApp.lotteryMap.get(lotteryId)+"第<font color=#007aff>"+myBundle.getString("issue")+"</font>期"));
		}else {
			acc_title_content1.setText(payDesc);
		}
		acc_title_content2.setText(Html.fromHtml("¥ "+"<font color=#ff0000>"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(myBundle.getString("money"))))+"</font>"));//改动过
	}
	private void setListeners() {
		
		zwl_more_payment_way_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchLayout(LAYOUT_MODE_BASIC_REQUIRED_DATA);
			}
		});
		
		voicepay_cb_chose_open_bank_address.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(mode == LAYOUT_MODE_BASIC_REQUIRED_DATA){
					showPop(voicepay_cb_chose_open_bank_address,certificate_types);
				}else {
					showPop_saved_bankinfo(voicepay_cb_chose_open_bank_address, savedBankInfos);
				}
			}
		});
		voicepay_confirm_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (mode) {
				case LAYOUT_MODE_BASIC_REQUIRED_DATA:
					String name = voicepay_et_name.getText().toString().trim();
					String card_num = voicepay_et_card_num.getText().toString().trim();
					String phone_num = voicepay_et_phone_num.getText().toString().trim();
					String certificate_num = voicepay_et_certificate_num.getText().toString().trim();
					String certificate_type = voicepay_cb_chose_open_bank_address.getText().toString().trim();
					
					if(AppUtil.isEmpty(name) || AppUtil.isEmpty(card_num) || AppUtil.isEmpty(phone_num) || AppUtil.isEmpty(certificate_num) || AppUtil.isEmpty(certificate_type)){
						Toast.makeText(mActivity, "请填入完整信息", 0).show();
						return;
					}
					String b = phone_num + "|" + card_num;
					String d = name + "|" +certificate_num + "|"+FormatUtil.ballNumberFormat((Integer)voicepay_cb_chose_open_bank_address.getTag()+1)+"|"+Protocol.getInstance().getUSR();
					////System.out.println("=========================================");
					message.put("B", b);//银行卡号或支付账号
					message.put("D", d);//三方支付附加信息   姓名|身份证|证件类型|本地彩票账户；
					break;
				case LAYOUT_MODE_USER_PREFERED_DATA:
					Integer position = (Integer) voicepay_cb_chose_open_bank_address.getTag();
					if(position == null){
						Toast.makeText(mActivity, "请选择支付信息", 0).show();
						return;
					}
					MessageBean bean = savedBankInfos.get(position);
					message.put("B", bean.getA()+"|"+bean.getB());//银行卡号或支付账号
					message.put("D", bean.getC()+"|"+bean.getE()+"|"+bean.getD()+"|"+Protocol.getInstance().getUSR());
					break;
				default:
					break;
				}
				sendRequest_payeco_plugin(message);
			}
		});
	}
}
