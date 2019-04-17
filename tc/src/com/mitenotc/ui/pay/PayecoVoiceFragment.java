package com.mitenotc.ui.pay;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.dao.impl.AddressDaoImpl;
import com.mitenotc.enums.PayMethodEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

public class PayecoVoiceFragment extends BasePayFragment{

	private static final int LAYOUT_MODE_ALL_REQUIRED_DATA = 1;//获取所有需要的数据的界面
	private static final int LAYOUT_MODE_BASIC_REQUIRED_DATA = 0;//获取基本需要的数据的界面
	private static final int LAYOUT_MODE_USER_PREFERED_DATA = 2;//仅显示用户倾向的数据的界面
	private int mode;
	private LinearLayout payeco_voicepay_bank;
	private CheckBox voicepay_cb_chose_open_bank_province;
	private CheckBox voicepay_cb_chose_open_bank_city;
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
	
	private Button voicepay_confirm_pay;
	
	private String[] refers;
	
	private List<String>certificate_types; 
	private List<String> provinces; 
	private List<String> citys; 
	private AddressDaoImpl dao;
	
	//头部的tv
	private TextView acc_title_text1;
	private TextView acc_title_content1;
	private TextView acc_title_text2;
	private TextView acc_title_content2;
	private TextView acc_title_content4;
	
	private TextView zwl_more_payment_way_tv;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.f_fragment_voicepay);
		certificate_types = Arrays.asList(getResources().getStringArray(R.array.certificate_type));
		dao = new AddressDaoImpl();
		provinces = dao.findProvince();
		initMyBundleData();
		initViews();
		
		if(savedBankInfos_payeco_voice == null || savedBankInfos_payeco_voice.size()<1){//如果语音支付还没有请求到成功的数据
			switchLayout(LAYOUT_MODE_BASIC_REQUIRED_DATA);
		}else {
			switchLayout(LAYOUT_MODE_USER_PREFERED_DATA);
		}
		
		setListeners();
		setTitleText("银联语音");
		sendRequest_saved_bankinfo("01");
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
	
	/*1	开户姓名
	2	开户证件号码
	3	开户银行所在省市
	4	开户证件类型(01:身份证；02:护照；03:军人证；04:台胞证)
	5	受益人姓名
	6	持卡人IP地址
	7	开户证件地址
	8	受益人手机号
	9	产品销售地
	10	开户银行登记手机号*/


	private void initViews() {
		payeco_voicepay_bank = (LinearLayout) findViewById(R.id.payeco_voicepay_bank);
		voicepay_cb_chose_open_bank_province = (CheckBox) findViewById(R.id.voicepay_cb_chose_open_bank_province);
		voicepay_cb_chose_open_bank_city = (CheckBox) findViewById(R.id.voicepay_cb_chose_open_bank_city);
		
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
		
		voicepay_confirm_pay = (Button) findViewById(R.id.voicepay_confirm_pay);
		
		acc_title_text1 = (TextView) findViewById(R.id.acc_title_text1);
		acc_title_content1 = (TextView) findViewById(R.id.acc_title_content1);
		acc_title_text2 = (TextView) findViewById(R.id.acc_title_text2);
		acc_title_content2 = (TextView) findViewById(R.id.acc_title_content2);
		acc_title_content4 = (TextView) findViewById(R.id.acc_title_content4);
		zwl_more_payment_way_tv = (TextView) findViewById(R.id.zwl_more_payment_way_tv);
		
		initHeaderDatas();
		
		views = new View[10];
		views[0] = voicepay_et_name;
		views[1] = voicepay_et_certificate_num;
		views[2] = payeco_voicepay_bank;
		views[3] = voicepay_cb_chose_certificate_type;
		views[4] = voicepay_et_beneficiary;
		views[5] = null;
		views[6] = voicepay_et_certificate_address;
		views[7] = voicepay_et_beneficiary_num;
		views[8] = null;
		views[9] = voicepay_et_phone_num;
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
		acc_title_content2.setText(Html.fromHtml("¥ "+"<font color=#ff0000>"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(myBundle.getString("money"))))+"</font>"));//有改动
	}

	private void setListeners() {
		zwl_more_payment_way_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchLayout(LAYOUT_MODE_BASIC_REQUIRED_DATA);
			}
		});
		voicepay_cb_chose_certificate_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					showPop(voicepay_cb_chose_certificate_type,certificate_types);
			}
		});
		voicepay_cb_chose_open_bank_province.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				showPop(voicepay_cb_chose_open_bank_province,provinces);
			}
		});
		voicepay_cb_chose_open_bank_city.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String province = voicepay_cb_chose_open_bank_province.getText().toString().trim();
				if(AppUtil.isEmpty(province))
					return;
				showPop(voicepay_cb_chose_open_bank_city, dao.findCityByName(province));
			}
		});
		
		voicepay_cb_chose_open_bank_address.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				showPop_saved_bankinfo(voicepay_cb_chose_open_bank_address, savedBankInfos);
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
					if(AppUtil.isEmpty(name) || AppUtil.isEmpty(card_num) || AppUtil.isEmpty(phone_num)){
						Toast.makeText(mActivity, "请填入完整的支付信息", 0).show();
						return;
					}
					String b = phone_num + "|" + card_num;
					String d = "1"+name+"|||||||||";
					
					message.put("B", b);
					message.put("D", d);
					break;
				case LAYOUT_MODE_ALL_REQUIRED_DATA:
					String data = "2";//第二次请求以2为开头的
					for (int i = 0; i < refers.length; i++) {
						if("1".equals(refers[i]) && views[i] != null && views[i].getVisibility() == View.VISIBLE){
							String str = "";//
							if(i== 3){//证件类型的网格式化
								str = FormatUtil.ballNumberFormat(i+1);
							}else if(i== 2){
								String str1 = voicepay_cb_chose_open_bank_province.getText().toString().trim();
								String str2 = voicepay_cb_chose_open_bank_city.getText().toString().trim();
								if(AppUtil.isEmpty(str1) || AppUtil.isEmpty(str2)){
									Toast.makeText(mActivity, "请输入完整的支付信息", 0).show();
									return;
								}
								str = str1 + "," + str2;
							}else {
								TextView tv = (TextView) views[i];
								str = tv.getText().toString().trim();
							}
							if(AppUtil.isEmpty(str)){
								Toast.makeText(mActivity, "请输入完整的支付信息", 0).show();
								return;
							}
							data += str;
						}
						data += "|";
					}
					
					message.put("D", data.substring(0,data.length()-1));
					break;
				case LAYOUT_MODE_USER_PREFERED_DATA:
					Integer position = (Integer) voicepay_cb_chose_open_bank_address.getTag();
					if(position == null){
						Toast.makeText(mActivity, "请选择支付信息", 0).show();
						return;
					}
					MessageBean bean = savedBankInfos.get(position);
					////System.out.println("bean = "+bean);
					message.put("B", bean.getA()+"|"+bean.getB());
					message.put("D", "1"+bean.getC());
					break;
				default:
					break;
				}
				sendRequest_payeco_voicepay(message);
			}
		});
	}
	/**
	 * 隐藏相关的控件
	 */
	public void hideAllRelativeViews(){
		hide(zwl_more_payment_way_tv);//隐藏 手动输入更多
		
		hide(voicepay_cb_chose_certificate_type);
		hide(voicepay_cb_chose_open_bank_address);
		hide(payeco_voicepay_bank);
		hide(voicepay_et_name);
		hide(voicepay_et_card_num);
		hide(voicepay_et_phone_num);
		hide(voicepay_et_certificate_num);
		hide(voicepay_et_certificate_address);
		hide(voicepay_et_beneficiary);
		hide(voicepay_et_beneficiary_num);
	}
	/**
	 * 显示相关的控件
	 * @param views
	 */
	public void showReletiveViews(View...views){
		for (View view : views) {
			show(view);
		}
	}
	
	public void switchLayout(int mode){
		this.mode = mode;
		hideAllRelativeViews();
		switch (mode) {
		case LAYOUT_MODE_ALL_REQUIRED_DATA:
			
			hideViews(acc_title_text1,acc_title_content1,acc_title_text2,acc_title_content2);
			show(acc_title_content4);
			
			showReletiveViews(voicepay_cb_chose_certificate_type,
					payeco_voicepay_bank,voicepay_et_name,
					voicepay_et_card_num,voicepay_et_phone_num,
					voicepay_et_certificate_num,voicepay_et_certificate_address,
					voicepay_et_beneficiary,voicepay_et_beneficiary_num);
			voicepay_cb_chose_certificate_type.setHint(getString(R.string.voicepay_hint_chose_certificate_type));
			voicepay_et_certificate_num.setHint(getString(R.string.voicepay_hint_certificate_num));
			voicepay_cb_chose_open_bank_address.setHint(getString(R.string.voicepay_hint_open_band_address));
			voicepay_et_certificate_address.setHint(getString(R.string.voicepay_hint_certificate_address));
			voicepay_et_beneficiary.setHint(getString(R.string.voicepay_hint_beneficiary));
			voicepay_et_beneficiary_num.setHint(getString(R.string.voicepay_hint_beneficiary_num));
			voicepay_et_name.setHint(getString(R.string.voicepay_hint_name));
			voicepay_et_card_num.setHint(getString(R.string.voicepay_hint_card_num));
			voicepay_et_phone_num.setHint(getString(R.string.voicepay_hint_phone));
			break;
		case LAYOUT_MODE_BASIC_REQUIRED_DATA:
			showViews(acc_title_text1,acc_title_content1,acc_title_text2,acc_title_content2);
			hide(acc_title_content4);
			showReletiveViews(voicepay_et_name,voicepay_et_card_num,voicepay_et_phone_num);
			voicepay_et_name.setHint(getString(R.string.voicepay_hint_name));
			voicepay_et_card_num.setHint(getString(R.string.voicepay_hint_card_num));
			voicepay_et_phone_num.setHint(getString(R.string.voicepay_hint_phone));
			break;
		case LAYOUT_MODE_USER_PREFERED_DATA:
			showViews(zwl_more_payment_way_tv,acc_title_text1,acc_title_content1,acc_title_text2,acc_title_content2);
			hide(acc_title_content4);
			showReletiveViews(voicepay_cb_chose_open_bank_address);
			voicepay_cb_chose_open_bank_address.setHint(getString(R.string.voicepay_hint_chose_card));
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onPayeco_voicepay_received_11004(MessageBean bean) {////System.out.println("11004 ========= "+bean.getD());
		try {
			String d = bean.getD();
			JSONObject json = new JSONObject(d);
			String refer = json.getString("REFER");
			refers = refer.split("\\|");
			switchLayout(LAYOUT_MODE_ALL_REQUIRED_DATA);
			for (int i = 0; i < refers.length; i++) {
				if(views[i] != null){
					if("1".equals(refers[i]) ){
						show(views[i]);
					}else {
						hide(views[i]);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
