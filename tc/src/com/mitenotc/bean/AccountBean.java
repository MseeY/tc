package com.mitenotc.bean;

public class AccountBean {

	private String accountName;
	private int accountState;
	private int accountAmount;
	private String accountNumber;
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public int getAccountState() {
		return accountState;
	}
	public void setAccountState(int accountState) {
		this.accountState = accountState;
	}
	public int getAccountAmount() {
		return accountAmount;
	}
	public void setAccountAmount(int accountAmount) {
		this.accountAmount = accountAmount;
	}
}
