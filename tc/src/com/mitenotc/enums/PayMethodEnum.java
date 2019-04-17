package com.mitenotc.enums;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;

public enum PayMethodEnum {

	alipay_plugin("03",MyApp.res.getString(R.string.paymethod_ali_plugin)) ,
	payeco_plugin("02",MyApp.res.getString(R.string.paymethod_payeco_plugin)),// 2014-6-27
	payeco_voicepay("01",MyApp.res.getString(R.string.paymethod_payeco_voicepay)),
	ali_WAP("04",MyApp.res.getString(R.string.paymethod_ali_wap)),
	savings_card("07",MyApp.res.getString(R.string.paymethod_upay_savings_card)),
	credit_card("08",MyApp.res.getString(R.string.paymethod_upay_credit_card));
	
	private String paymethod_code;
	private String paymethod_text;
	private static int index = PayMethodEnum.values().length;
	
	public static int getIndex() {
		return index;
	}

	public static String[] getPaymethod_texts(){
		String[] paymethod_texts = new String[index];
		for (int i = 0; i < index;i++) {
			paymethod_texts[i] = PayMethodEnum.values()[i].paymethod_text;
		}
		return paymethod_texts;
	}
	
	private PayMethodEnum(String paymethod_code,String paymethod_text){
		this.paymethod_code = paymethod_code;
		this.paymethod_text = paymethod_text;
	}

	public String getPaymethod_code() {
		return paymethod_code;
	}

	public void setPaymethod_code(String paymethod_code) {
		this.paymethod_code = paymethod_code;
	}

	public String getPaymethod_text() {
		return paymethod_text;
	}

	public void setPaymethod_text(String paymethod_text) {
		this.paymethod_text = paymethod_text;
	}
}
