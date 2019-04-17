package com.mitenotc.bean.ticket;

import com.mitenotc.bean.TicketBean;
/**
 *  七乐彩  选票
 * @author admin
 *
 */
public class TicketBean_117 extends TicketBean{

	@Override
	public void onCalc() {
		calcLotteryCount_qlc();
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_qlc();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_qlc();
	}
	
	/**
	 * 七乐彩 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String formatLotteryNums_qlc() {
		formatStr = "";
		if(!isTicketAvailable)//如果票 没有满足 基本的玩法要求,则返回为空
			return null;
		if("0".equals(salesType) || "1".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),true);
		}else if("2".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),"*",lotteryNums.get(1),true);
		}
		return formatStr;
	}
	/**
	 * 七乐彩 的显示字符串的格式化
	 * @return
	 */
	private String showLotteryNums_qlc() {
		showStr = "";
		if("0".equals(salesType) || "1".equals(salesType)){
			appendShowNums(lotteryNums.get(0),true);
		}else if("2".equals(salesType)){
			appendShowNums_dantuo();
		}
		return showStr;
	}
	/**
	 * 计算七乐彩注数
	 * @return
	 */
	public long calcLotteryCount_qlc(){
		price = 200;
		calcLotteryNum_common_and_dantuo(7, 2);
		return lotteryCount;
	} 
}
