package com.mitenotc.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

import android.graphics.drawable.Drawable;

/**
 * 用户的数据存储
 * @author mitenotc
 *
 */
public class UserBean {

	private static UserBean instance = new UserBean();
	public static UserBean getInstance() {
		return instance;
	};
	private String lotteryName;//彩名
	private String phoneNum;//电话号码
	private long availableMoney;//可用余额, 不包括红包
	private long availableCash;//可提现金额
	private long availableBalance;//账户可用资金(能用来购彩的金额，包括红包)
	private long availableCashStart;
	private UserBean(){
	String snd=	SPUtil.getString(MyApp.res.getString(R.string.SND));//用户SESSION
	String usr=	SPUtil.getString(MyApp.res.getString(R.string.USR));//用户SESSION
		if(AppUtil.isEmpty(snd) || AppUtil.isEmpty(usr)){
			isLogin=false;
		}else{
			phoneNum=usr;
			isLogin=true;
			String name = SPUtil.getString(MyApp.res.getString(R.string.uname));//用户SESSION
			if(!AppUtil.isEmpty(name)){
				lotteryName=name;
			}
		}
	}
	public long getAvailableCashStart() {
		return availableCashStart;
	}
	public void setAvailableCashStart(long availableCashStart) {
		this.availableCashStart = availableCashStart;
	}

	private String userAccountState;//用户账号 的 状态
//	private List<AccountBean> accounts; 
	private List<BankBean> bankInfos; //银行卡信息
	
	private List<Map<String, Drawable>> logoList;
	

	public List<Map<String, Drawable>> getLogoList() {
		return logoList;
	}
	public void setLogoList(List<Map<String, Drawable>> logoList) {
		this.logoList = logoList;
	}
	public List<BankBean> getBankInfos() {
		return bankInfos;
	}
	public void setBankInfos(List<BankBean> bankInfos) {
		this.bankInfos = bankInfos;
	}

	private String trueName;   //真实姓名
	private String userRating;  //用户等级
	
	public String getUserRating() {
		return userRating;
	}
	public void setUserRating(String userRating) {
		this.userRating = userRating;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	private boolean isLogin;
	
	
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public String getLotteryName() {
		return lotteryName;
	}
	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getUserAccountState() {
		return userAccountState;
	}
	public void setUserAccountState(String userAccountState) {
		this.userAccountState = userAccountState;
	}
	public static void setInstance(UserBean instance) {
		UserBean.instance = instance;
	}
//	public List<AccountBean> getAccounts() {
//		return accounts;
//	}
//	public void setAccounts(List<AccountBean> accounts) {
//		this.accounts = accounts;
//	}
	public long getAvailableCash() {
		return availableCash;
	}
	public void setAvailableCash(long availableCash) {
		this.availableCash = availableCash;
	}
	public long getAvailableMoney() {
		return availableMoney;
	}
	public void setAvailableMoney(long availableMoney) {
		this.availableMoney = availableMoney;
	}
	public long getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(long availableBalance) {
		this.availableBalance = availableBalance;
	}
}
