package com.mitenotc.bean.ticket;

import java.util.ArrayList;
import java.util.List;

import com.mitenotc.bean.TicketBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.utils.BaseCalc;
/**
 * 
 * 排列三  选票
 * @author admin
 *
 */
public class TicketBean_100 extends TicketBean{

	@Override
	public void onCalc() {
		calcLotteryCount_pl3();
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_pl3();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_pl3();
	}
	
	/**
	 * 排列三 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String formatLotteryNums_pl3() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		formatStr = "";
		if("3".equals(salesType) && lotteryNums.size()>1){//排列三组三单式.只有一注
			formatStr += lotteryNums.get(1).get(0);
			formatStr += lotteryNums.get(0).get(0);
			formatStr += lotteryNums.get(0).get(0);
		}else{//(当前第一个版本)除了排列三的组三单式,其他销售方式的数据格式化都可以用该方法来实现.
			appendFormatNums(lotteryNums, "*", false);
		}
		return formatStr;
	}
	
	/**
	 * 排列三 号码 格式化到 界面显示的格式
	 * @return
	 */
	private String showLotteryNums_pl3() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		showStr = "";
		if("3".equals(salesType) && lotteryNums.size()>1){//三星组三单式.只有一注
			showStr += lotteryNums.get(0).get(0);
			showStr += lotteryNums.get(0).get(0);
			showStr += lotteryNums.get(1).get(0);
		}else {//(当前第一个版本)除了三星的组三单式,时时彩的数据格式化都可以用该方法来实现.
			appendShowNums_directly(lotteryNums, " ");
		}
		return showStr;
	}
	
	/**
	 * 计算 排列三
	 * @return
	 */
	public long calcLotteryCount_pl3(){
		price = 200;
		if("0".equals(salesType) || "1".equals(salesType)){//直选单式或复式
			calcLotteryNum_tool_common(1,1,1);
		}else if("3".equals(salesType) || "4".equals(salesType)){//组三单式 或 组六单式和复式
			if(lotteryNums.size()>1){//组三单式有两个容器
				calcLotteryNum_tool_common("3", NaN, 1,1);
			}else {//组六只有一个容器
				calcLotteryNum_tool_common("4","4",3);
			}
		}else if("5".equals(salesType)){//组三复式
			if(sizes[0]<2){
				isTicketAvailable = false;
				lotteryCount = 0;
			}else{
				isTicketAvailable = true;
				lotteryCount = BaseCalc.calc(sizes[0], 2)*2;
			}
		}
		return lotteryCount;
	}
	@Override
	public List<MessageJson> getTicketJsonArray() {
		reloadSizes();
		formatStr = "";
		List<MessageJson> list = new ArrayList<MessageJson>();
		if("4".equals(salesType)){// 组六单式 拆分为 组选单式
			if(lotteryNums.size()==1 && lotteryNums.get(0).size() == 3){//组六一个容器
				salesType="3";
				for (int i = 0; i < lotteryNums.get(0).size(); i++) {
					formatStr +=lotteryNums.get(0).get(i);
				}
				MessageJson msg = new MessageJson();
				msg.put("A", childId);
				msg.put("B", salesType);
				msg.put("C", formatStr);
				msg.put("D", fold);
				msg.put("E", lotteryCount);
				msg.put("F", ticketAmount);
				list.add(msg);
			}else{
				list.add(getTicketJson());// 非单式票 正常下单
			}
		}else{
			list.add(getTicketJson());
		}
		return list;
	}
}
