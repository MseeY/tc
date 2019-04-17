package com.mitenotc.bean.ticket;

import com.mitenotc.bean.TicketBean;
/**
 * 大乐透  选票
 * @author admin
 *
 */
public class TicketBean_106 extends TicketBean{

	@Override
	public void onCalc() {
		calcLotteryCount_dlt();
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_dlt();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_dlt();
	}
	
	/**
	 * 由于当前的需求中不对蓝球区进行胆拖设置
	 * 大乐透 网络协议的封装 与双色球 完全一样
	 * 实际中需求 大乐透的后驱要用 |* ,与双色球的唯一不同之处
	 * @return
	 */
	public String formatLotteryNums_dlt(){
		formatStr = "";
		if(!isTicketAvailable)//如果票 没有满足 基本的玩法要求,则返回为空
			return null;
		if("0".equals(salesType) || "1".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),"|",lotteryNums.get(1),true);
		}else if("2".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),"*",lotteryNums.get(1),"|*",lotteryNums.get(2),true);
		} 
		return formatStr;
	}
	/**
	 * 大乐透 的显示字符串的格式化
	 * @return
	 */
	public String showLotteryNums_dlt(){//大乐透的显示方式和双色球的完全一样
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
	/**
	 * 计算大乐透注数
	 * @return
	 */
	public long calcLotteryCount_dlt(){
		if("0".equals(childId)){
			price = 200;
		}else if("1".equals(childId)){
			price = 300;
		}
		calcLotteryNum_common_and_dantuo(5, 2, 2);
		return lotteryCount;
	} 
}
