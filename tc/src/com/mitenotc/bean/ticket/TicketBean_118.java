package com.mitenotc.bean.ticket;

import com.mitenotc.bean.TicketBean;
/**
 * 双色球   选票
 * @author admin
 *
 */
public class TicketBean_118 extends TicketBean{

	@Override
	public void onCalc() {
		calcLotteryCount_ssq();
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_ssq();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_ssq();
	}
	
	/**
	 * 计算双色球注数
	 * @return
	 */
	private long calcLotteryCount_ssq() {
		 
		childId = "0";//双色球只有 childId = "0" 的模式
		price = 200;//双色球每注为2元
		calcLotteryNum_common_and_dantuo(6, 2, 1);//对胆拖和普通都在的计算的封装
		return lotteryCount;
	}
	
	/**
	 * 双色球 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String formatLotteryNums_ssq() {
		formatStr = "";
		if(!isTicketAvailable)//如果票 没有满足 基本的玩法要求,则返回为空
			return null;
		if("0".equals(salesType) || "1".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),"|",lotteryNums.get(1),true);
		}else if("2".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),"*",lotteryNums.get(1),"|",lotteryNums.get(2),true);
		}
		return formatStr;
	}
	
	/**
	 * 双色球 的显示字符串的格式化
	 * @return
	 */
	private String showLotteryNums_ssq() {
		showStr = "";
		if("0".equals(salesType) || "1".equals(salesType)){
			appendShowNums(lotteryNums.get(0),true);
			showStr += "&#160;&#160;<font color=#007aff>";
			appendShowNums(lotteryNums.get(1),true);
			showStr += "</font>";
		}else if("2".equals(salesType)){
			appendShowNums_dantuo();
			showStr += "&#160;&#160;<font color=#007aff>";
			appendShowNums(lotteryNums.get(2),true);
			showStr += "</font>";
		}
		return showStr;
	}
}
