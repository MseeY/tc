package com.mitenotc.bean.ticket;

import java.util.ArrayList;
import java.util.List;

import com.mitenotc.bean.TicketBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.utils.BaseCalc;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.Getlotterykind;
/**
 * 快乐扑克  选票
 * @author admin
 *
 */
public class TicketBean_121 extends TicketBean{

	@Override
	public void onCalc() {
		int childtype = Integer.parseInt(childId);
		if(childtype <= 10){//非任选（包选和各种猜 的彩种）
			lotteryCount = sizes[0]+sizes[1];
			if(lotteryCount == 0){
				isTicketAvailable = false;
			}else if(lotteryCount == 1){
				isTicketAvailable = true;
			}else{
				isTicketAvailable = true;
			}
		}else{//任选一到六
			int randomMode = childtype - 10;//任选及可以通过当前的childtype - 10 获得
			if(sizes[0] < randomMode){
				isTicketAvailable = false;
				lotteryCount = 0;
			}else if(sizes[0] == randomMode){//单式
				isTicketAvailable = true;
				lotteryCount = 1;
			}else{//复式
				isTicketAvailable = true;
				lotteryCount = BaseCalc.calc(sizes[0], randomMode);
			}
		}
		
		if(lotteryCount >=1){
			   Getlotterykind.getInstance().znzh_pl121_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
	    }
	}
	
	/**
	 * 该方法用于多票情况下的格式化
	 * 需求： 1.把第一个界面的childid 设为 0
	 * 		 2.把包选时传递的号码设成与 childId 一样的值	
	 */
	@Override
	public List<MessageJson> getTicketJsonArray() {//用该方法时 formatLotteryNums() 可以不用
		reloadSizes();
		List<MessageJson> list = new ArrayList<MessageJson>();
		if("0".equals(childId)){//第一个界面，全为包选的格式化
//			////System.out.println("lotteryNums.get(0)-----------:"+lotteryNums.get(0).toString());
	
				for (int j = 0; j < lotteryNums.get(0).size(); j++)
				{
					String tempChilId="0";
					switch (lotteryNums.get(0).get(j)) 
					{
						case 11://对子包选
							
							tempChilId="9";
							break;
						case 10://豹子包选
							
							tempChilId="7";
							break;
						case 7://同花包选
							
							tempChilId="1";
							break;
						case 9:// 顺子包选 
							
							tempChilId="5";
							break;
						case 8://同花顺包选
							
							tempChilId="3";
							break;

						default:
							break;
					}
					list.add(getTicketJsonk3(tempChilId, "0", FormatUtil.ballNumberFormat(lotteryNums.get(0).get(j)), "1", fold*1*price+"", fold+""));
				}
		}else{//其它界面的格式化
			String childId_normal_chose = "";// 普通选号的childId
			String childId_all_chose = "";//包选的childid
			//区分多张票的不同的 childId
			if("1".equals(childId) || "2".equals(childId)){
				childId_normal_chose = "2";
				childId_all_chose = "1";
			}else if("3".equals(childId) || "4".equals(childId)){
				childId_normal_chose = "4";
				childId_all_chose = "3";
			}else if("5".equals(childId) || "6".equals(childId)){
				childId_normal_chose = "6";
				childId_all_chose = "5";
			}else if("7".equals(childId) || "8".equals(childId)){
				childId_normal_chose = "8";
				childId_all_chose = "7";
			}else if("9".equals(childId) || "10".equals(childId)){
				childId_normal_chose = "10";
				childId_all_chose = "9";
			}else{
				childId_normal_chose = childId;
			}
			
			if(sizes[0] == 1){//单式
				list.add(getTicketJsonk3(childId_normal_chose, "0", appendOneContainer(lotteryNums.get(0), true)+"", "1", fold*1*price+"", fold+""));//第一张ticket
			}else if(sizes[0] > 1){//复式
				/*if(sizes[1] == 1 && !"".equals(appendOneContainer(lotteryNums.get(1), true))){//包选 只有当 包选为 1票时才成立
					list.add(getTicketJsonk3(childId_all_chose, "0", appendOneContainer(lotteryNums.get(1), true), "1", fold*1*price+"", fold+""));//第二张ticket
				}else*/ 
//				if(0 == sizes[1]  && 14 == lotteryCount || 0 == sizes[1]  &&  5 == lotteryCount ) {//  多票拆分
//						list.add(getTicketJsonk3(childId_all_chose, "0",getbxNumstr(childId_all_chose) , "1", fold*1*price+"", fold+""));//第二张ticket
//				}
				if(1 == sizes[1] && lotteryCount > 1 ){
						lotteryCount -=1;
				}
				int tempN=Integer.parseInt(childId_normal_chose);
				
				if(tempN >= 11 && sizes[0] == (tempN-10)){//  任选玩法的单式String childId,   String salesType,   String formatStr,  String lotteryCount,  String ticketAmount,  String fold
					if((tempN-10) == lotteryNums.get(0).size() && !"".equals(appendOneContainer(lotteryNums.get(0), true))){//单式投注
						list.add(getTicketJsonk3(childId_normal_chose, "0", appendOneContainer(lotteryNums.get(0), true), String.valueOf(lotteryCount), String.valueOf(fold*lotteryCount*price), fold+""));//第一张ticket
					}else{
						list.add(getTicketJsonk3(childId_normal_chose, "1", appendOneContainer(lotteryNums.get(0), true), String.valueOf(lotteryCount), String.valueOf(fold*lotteryCount*price), fold+""));//第一张ticket
					}
				}else if(Integer.parseInt(childId_normal_chose) >= 11){
					if((Integer.parseInt(childId_normal_chose)-10) == lotteryNums.get(0).size() &&  !"".equals(appendOneContainer(lotteryNums.get(0), true))){
						list.add(getTicketJsonk3(childId_normal_chose, "0", appendOneContainer(lotteryNums.get(0), true), String.valueOf(lotteryCount), String.valueOf(fold*lotteryCount*price), fold+""));//第一张ticket
					}else if(lotteryNums.get(0).size() > (Integer.parseInt(childId_normal_chose)-10)){
						list.add(getTicketJsonk3(childId_normal_chose, "1", appendOneContainer(lotteryNums.get(0), true), String.valueOf(lotteryCount), String.valueOf(fold*lotteryCount*price), fold+""));//第一张ticket
					}
				}else {
					if(1 == lotteryNums.get(0).size() &&  !"".equals(appendOneContainer(lotteryNums.get(0), true))){
						list.add(getTicketJsonk3(childId_normal_chose, "0", appendOneContainer(lotteryNums.get(0), true), String.valueOf(lotteryCount), String.valueOf(fold*lotteryCount*price), fold+""));//第一张ticket
					}else if(lotteryNums.get(0).size() > 1){
						list.add(getTicketJsonk3(childId_normal_chose, "1", appendOneContainer(lotteryNums.get(0), true), String.valueOf(lotteryCount), String.valueOf(fold*lotteryCount*price), fold+""));//第一张ticket
					}
					
				}
				
			}
			if(sizes[1] == 1 && !"".equals(appendOneContainer(lotteryNums.get(1), true))){//包选 只有当 包选为 1票时才成立
				list.add(getTicketJsonk3(childId_all_chose, "0", appendOneContainer(lotteryNums.get(1), true), "1", fold*1*price+"", fold+""));//第二张ticket
			}
		   
		}
		return list;
	}

	private String getbxNumstr(String childId){
		String numStr="";// 号码
		switch (Integer.parseInt(childId)) {
		case 9://对子包选
			numStr="11";
			break;
		case 1://同花包选
			numStr="07";
			break;
		case 3://同花顺包选
			numStr="08";
			break;
		case 5://顺子包选
			numStr="09";
			break;
		case 7://豹子包选
			numStr="10";
			break;
		}
		return numStr;
	}
	@Override
	public String formatLotteryNums() {//该方法用于 "单票" 情况下的 数据格式化
		return "";
	}

	@Override
	public String showLotteryNums_klpk() {//根据实际界面的需求进行格式化
		showStr="";
	    switch (Integer.parseInt(salesType+childId)) {
		//		    包选
			case 0:
				if(lotteryNums.get(0).size() > 0 ){
					for (int i = 0; i < lotteryNums.get(0).size(); i++) {
						switch (lotteryNums.get(0).get(i)) {
							case 11:
								showStr +=" 对子包选 ";
								break;
							case 10:
								showStr +=" 豹子包选 ";
								break;
							case 7:
								showStr +=" 同花包选 ";
								break;
							case 9:
								showStr +=" 顺子包选 ";
								break;
							case 8:
								showStr +=" 同花顺包选 ";
								break;
	
							default:
								break;
							}
					}
				}
				break;
		//			猜对子（包选  :代表layout）
			case 9:
				if(lotteryNums.get(0).size() > 0 ){
					for (int i = 0; i < lotteryNums.get(0).size(); i++) {
						if(1==lotteryNums.get(0).get(i)){
							showStr +=" AA ";
						}else if(11==lotteryNums.get(0).get(i)){
							showStr +=" JJ ";
						}else if(12==lotteryNums.get(0).get(i)){
							showStr +=" QQ ";
						}else if(13==lotteryNums.get(0).get(i)){
							showStr +=" KK ";
						}else {
							int iStr=lotteryNums.get(0).get(i);
							showStr +=" "+String.valueOf(iStr)+String.valueOf(iStr)+" ";
						}
						
					}
				}
				if(1 == lotteryNums.get(1).size() ){
					showStr +="（对子包选）";
				}
				break;
		//		       猜豹子（包选  :代表layout）
			case 7:
				if(lotteryNums.get(0).size() > 0 ){
					for (int i = 0; i < lotteryNums.get(0).size(); i++) {
						if(1==lotteryNums.get(0).get(i)){
							showStr +="AAA ";
						}else if(11==lotteryNums.get(0).get(i)){
							showStr +=" JJJ ";
						}else if(12==lotteryNums.get(0).get(i)){
							showStr +=" QQQ ";
						}else if(13==lotteryNums.get(0).get(i)){
							showStr +=" KKK ";
						}else {
							int iStr=lotteryNums.get(0).get(i);
							showStr +=" "+String.valueOf(iStr)+String.valueOf(iStr)+String.valueOf(iStr)+" ";
						}
						
					}
				}
				if(1 == lotteryNums.get(1).size() ){
					showStr +="（豹子包选）";
				}
				break;
		//			猜同花（包选  :代表layout）
			case 1:
				if(lotteryNums.get(0).size() > 0 ){
					for (int i = 0; i < lotteryNums.get(0).size(); i++) {
						int iStr=lotteryNums.get(0).get(i);
						  switch (iStr) {
								case 1:
									showStr +="黑桃 ";
									break;
								case 2:
									showStr +=" 红桃 ";
									break;
								case 3:
									showStr +=" 梅花 ";
									break;
								case 4:
									showStr +=" 方块 ";
									break;
			
								default:
									break;
						       }
						
					}
				}
				if(1 == lotteryNums.get(1).size() ){
					showStr +="（同花包选）";
				}
				break;
		//			猜顺子（包选  :代表layout）
			case  5:
				if(lotteryNums.get(0).size() > 0 ){
					for (int i = 0; i < lotteryNums.get(0).size(); i++) {
						int iStr=lotteryNums.get(0).get(i);
						switch (iStr) {
								case 1:
									showStr +="A23 ";
							
									break;
								case 2:
									showStr +=" 234 ";
									
									break;
								case 3:
									showStr +=" 345 ";
									
									break;
								case 4:
									showStr +=" 456 ";
									
									break;
								case 5:
									showStr +=" 567 ";
									
									break;
								case 6:
									showStr +=" 678 ";
									
									break;
								case 7:
									showStr +=" 789 ";
									
									break;
								case 8:
									showStr +=" 8910 ";
									
									break;
								case 9:
									showStr +=" 910J ";
									
									break;
								case 10:
									showStr +=" 10JQ ";
									
									break;
								case 11://J
									showStr +=" JQK ";
									break;
								case 12://Q
									showStr +=" QKA ";
									break;
					
								default:
									break;
						}
						
					}
				}
				if(1 == lotteryNums.get(1).size() ){
					showStr +="（顺子包选）";
				}
				break;
		//			猜同花顺（包选  :代表layout）
			case 3:
				if(lotteryNums.get(0).size() > 0 ){
					for (int i = 0; i < lotteryNums.get(0).size(); i++) {
						int iStr=lotteryNums.get(0).get(i);
						  switch (iStr) {
								case 1:
									showStr +="黑桃顺子 ";
									break;
								case 2:
									showStr +=" 红桃顺子 ";
									break;
								case 3:
									showStr +=" 梅花顺子 ";
									break;
								case 4:
									showStr +=" 方块顺子 ";
									break;
								default:
									break;
						       }
						}
				}
				
				if(1 == lotteryNums.get(1).size() ){
					showStr +="（同花顺包选）";
				}
				break;
				
		//			任选一  ------------------任选（布局相同，提示信息不同）-------------------
			case 11:
				addPKShowOneStr();
				break;
		//			任选二
			case 12:
				addPKShowOneStr();
				break;
		//			任选三
			case 13:
				addPKShowOneStr();
				break;
		//			任选四
			case 14:
				addPKShowOneStr();
				break;
		//			任选五
			case 15:
				addPKShowOneStr();
				break;
		//			任选六
			case 16:
				addPKShowOneStr();
				break;
		
			default:
				break;
			}
		
		return showStr;
	}
	
	/**
	 * 单张PK 选号
	 */
   private void addPKShowOneStr(){
	   showStr="";
		if(lotteryNums.get(0).size() > 0 ){
			for (int i = 0; i < lotteryNums.get(0).size(); i++) {
				if(1==lotteryNums.get(0).get(i)){
					showStr +=" A ";
				}else if(11==lotteryNums.get(0).get(i)){
					showStr +=" J ";
				}else if(12==lotteryNums.get(0).get(i)){
					showStr +=" Q ";
				}else if(13==lotteryNums.get(0).get(i)){
					showStr +=" K ";
				}else {
					int iStr=lotteryNums.get(0).get(i);
					showStr +=" "+String.valueOf(iStr)+" ";
				}
				
			}
		}
   }
}
