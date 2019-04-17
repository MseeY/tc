package com.mitenotc.enums;

import java.util.List;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.utils.AppUtil;

public enum AccountEnum {
	/*1	现金账户
	2	投注账户
	3	奖金账户
	4	红包账户
	5	分红账户
	6  积分账户
	"LIST":[
	        {"D":"5","A":"分红账户","B":"1","C":"0"},
	        {"D":"3","A":"奖金账户","B":"1","C":"16574800"},
	        {"D":"2","A":"投注账户","B":"1","C":"0"},
	        {"D":"1","A":"现金账户","B":"1","C":"530900"},
	        {"D":"6","A":"积分账户","B":"1","C":"523500"},
	        {"D":"4","A":"红包","B":"1","C":"0"}
	        ]
	*/
	
	cash_account(1,"现金账户"),
	bet_account(2,"投注账户"),
	award_account(3,"奖金账户"),
	redpacket_account(4,"红包账户"),
	bonus_account(5,"分红账户"),
	point_account(6,"积分账户");
	private int id;
	private String accountName;
	private long money;//金额
	private int state;//状态
	
	private AccountEnum(int id, String accountName) {
		this.id = id;
		this.accountName = accountName;
	}
	public static AccountEnum getItemById(int id){
		for (AccountEnum item : AccountEnum.values()) {
			if(item.id == id)
				return item;
		}
		return null;
	}
	public static void convertMessage(MessageBean bean){
		if(AppUtil.isNumeric(bean.getD())){//判断 标签d 是否为有效的数字
			AccountEnum item = getItemById(Integer.parseInt(bean.getD()));
			if(item != null){//安全考虑
				item.accountName = bean.getA();
				if(AppUtil.isNumeric(bean.getB()))
					item.state = Integer.parseInt(bean.getB());
				if(AppUtil.isNumeric(bean.getC()))
					item.money = Long.parseLong(bean.getC());
			}
		}
	}
	
	public static void convertMessage(List<MessageBean> beanList){
		if(beanList==null)return;//为空判断
		for (MessageBean bean : beanList) {
			convertMessage(bean);
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
