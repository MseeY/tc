package com.mitenotc.bean.ticket;

import com.mitenotc.bean.TicketBean;
import com.mitenotc.utils.Getlotterykind;

public class TicketBean_130 extends TicketBean {


	@Override
	public void onCalc() {
		is_blond_integral=true;
		calcLotteryCount_11x5();
		if(lotteryCount >=1)
		{
			Getlotterykind.getInstance().znzh_11x5_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
		}
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_11x5();
	}
	
	@Override
	public String showLotteryNums() {
		is_blond_integral=true;
		return showLotteryNums_11x5();
	}
	
	/**
	 * 11选5 号码 格式化到 网络协议要求的格式 
	 * @return
	 */
	private String formatLotteryNums_11x5() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		formatStr = "";
		int childType = Integer.parseInt(childId);
		if((1<= childType && childType <=8) || childType == 11 || childType == 12 ){
			if("0".equals(salesType) || "1".equals(salesType)){//普通投注方式
				appendFormatNums(lotteryNums.get(0),true);
			}else if("2".equals(salesType)){//胆拖投注方式
				appendFormatNums(lotteryNums.get(0),"*",lotteryNums.get(1),true);
			}
		}else if(childType == 9){//前二直选 
			String delimiter = "";
			if("3".equals(salesType)){//前2直选 定位复式,不是单式(2014-5-22: 更新-- TODO ：前二、前三定位复式，每位不能有重复的数字) 
				delimiter = "*";
			}
			appendFormatNums(lotteryNums.get(0),delimiter,lotteryNums.get(1),true);
		}else if(childType == 10){//前三直选 
			String delimiter = "";
			if("3".equals(salesType)){//前2直选 定位复式,不是单式(2014-5-22: 更新-- TODO ：前二、前三定位复式，每位不能有重复的数字) 
				delimiter = "*";
			}
			appendFormatNums(lotteryNums.get(0),delimiter,lotteryNums.get(1),delimiter,lotteryNums.get(2),true);
		}
		return formatStr;
	}
	/**
	 * 11选5 的显示字符串的格式化
	 * @return
	 */
	private String showLotteryNums_11x5() {
		showStr = "";
		int childType = Integer.parseInt(childId);
		if((1<= childType && childType <=8) || childType == 11 || childType == 12 ){
			if("0".equals(salesType) || "1".equals(salesType)){//普通投注方式
				appendShowNums(lotteryNums.get(0),true);
			}else if("2".equals(salesType)){//胆拖投注方式
				appendShowNums_dantuo();
			}
		}else if(childType == 9){//前二直选
			appendShowNums(lotteryNums.get(0), "|", lotteryNums.get(1));
		}else if(childType == 10){//前三直选
			appendShowNums(lotteryNums.get(0),"|",lotteryNums.get(1),"|",lotteryNums.get(2));
		}
		return showStr;
	}
	/**
	 * 计算11选5注数
	 *   
	 * @return
	 */
	public long calcLotteryCount_11x5()
	{
		price = 200;
		int childType = Integer.parseInt(childId);
		if(1<=childType && childType<=8){//任选二到任选7, 直选一,任选8
			 if("0".equals(salesType) || "1".equals(salesType)){//普通方式
				 calcLotteryNum_tool_common(childType);
			 }else if("2".equals(salesType)){//胆拖方式
				 if(childType == 1 || childType == 8){
					 throw new RuntimeException("任选一 和 任选八 没有胆拖模式");
				 }
				calcLotteryNum_tool_dantuo(childType, 2);
			}
		}else if(childType == 9){//前二直选
//			calcLotteryNum_tool_common("0","3",1,1);
//			lotteryCount -= getDuplicateNumCount(11);//减去重号重复的注数
			calcChildType_9();
			if(lotteryCount <1){
				isTicketAvailable = false;
			}else if (lotteryCount == 1) {
				isTicketAvailable = true;
				salesType = "0";
			}else {
				isTicketAvailable = true;
				salesType = "3";
			}
		}else if(childType == 10){//前三直选
//			calcLotteryNum_tool_common("0","3",1,1,1);
//			lotteryCount -= getDuplicateNumCount(11);//减去重号重复的注数
			calcChildType_10();
			if(lotteryCount <1){
				isTicketAvailable = false;
			}else if (lotteryCount == 1) {
				isTicketAvailable = true;
				salesType = "0";
			}else {
				isTicketAvailable = true;
				salesType = "3";
			}
		}else if(childType == 11){//前二组选
			calcLotteryNum_common_and_dantuo(2, 2);
		}else if(childType == 12){//前三组选
			calcLotteryNum_common_and_dantuo(3, 2);
		}
		return lotteryCount;
	} 

}
