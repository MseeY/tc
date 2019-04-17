package com.mitenotc.bean;


import android.graphics.drawable.Drawable;
public class BankBean {
	private String bankName;
	private Drawable bankLogo;
	private String bankNumber;
	private String bankProvince;
	private String bankCity;
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Drawable getBankLogo() {
		return bankLogo;
	}
	public void setBankLogo(Drawable bankLogo) {
		this.bankLogo = bankLogo;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public String getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

}
