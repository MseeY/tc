package com.mitenotc.bean.ticket;

import com.mitenotc.bean.TicketBean;
/**
 * 排列五  选票
 * @author admin
 *
 */
public class TicketBean_102 extends TicketBean{

	@Override
	public void onCalc() {
		calcLotteryCount_pl5();
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_qxc();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_qxc();
	}
	// 七星彩 和 排列5 的格式完全相同,唯一的不同只有 容器的数量不同. 且都不涉及胆拖
	/**
	 * 排列5 和 七星彩 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String formatLotteryNums_qxc() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		String tempStr = showStr;
		showStr = "";
		appendShowNums_directly(lotteryNums,"*");//
		formatStr = showStr;
		showStr = tempStr;
		return formatStr;
	}
	/**
	 * 排列5 和 七星彩 的显示字符串的格式化
	 * @return
	 */
	private String showLotteryNums_qxc() {
		showStr = "";
		appendShowNums_directly(lotteryNums,"&#160;&#160;");//用两个空格分隔
		return showStr;
	}
	/**
	 * 计算 排列5 注数
	 * @return
	 */
	public long calcLotteryCount_pl5(){
		price = 200;
		calcLotteryNum_tool_common(1,1,1,1,1);
		return lotteryCount;
	} 
}
