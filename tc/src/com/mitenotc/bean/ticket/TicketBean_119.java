package com.mitenotc.bean.ticket;

import org.apache.commons.lang3.StringUtils;

import com.mitenotc.bean.TicketBean;
import com.mitenotc.utils.BaseCalc;
import com.mitenotc.utils.Getlotterykind;
/**
 *  时时彩 选票
 * @author admin
 *
 */
public class TicketBean_119 extends TicketBean{

	@Override
	public void onCalc() {
		calcLotteryCount_ssc();
		if(lotteryCount >=1){
			   Getlotterykind.getInstance().znzh_pl119_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
		}
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_ssc();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_ssc();
	}
	
	/**
	 * 计算 时时彩
	 * @return
	 */
	public long calcLotteryCount_ssc(){
		price = 200;
		if("1".equals(childId)){//一星
			calcLotteryNum_tool_common(1);
		}else if("2".equals(childId)){//二星
			if("0".equals(salesType) || "1".equals(salesType)){//单式或复式
				calcLotteryNum_tool_common(1,1);
			}else if("5".equals(salesType) || "6".equals(salesType)){//组选单式或组选复式
				calcLotteryNum_tool_common("5","6",2);
			}else if("2".equals(salesType)){//大小单双
				calcLotteryNum_tool_common("2", NaN, 1,1);
			}
		}else if("3".equals(childId)){//三星
			if("0".equals(salesType) || "1".equals(salesType)){//单式或复式
				calcLotteryNum_tool_common(1,1,1);
			}else if("2".equals(salesType)){//组三单式
				calcLotteryNum_tool_common("2", NaN, 1,1);
			}else if("3".equals(salesType)){//组三复式
				if(sizes[0]<3){//-------------------组三复式  协议不支持最低2个选号（至少需要选择三个好码）----万利
					isTicketAvailable = false;
					lotteryCount = 0;
				}else{
					isTicketAvailable = true;
					lotteryCount = BaseCalc.calc(sizes[0], 2)*2;
				}
			}else if("4".equals(salesType) || "5".equals(salesType)){//组六
				calcLotteryNum_tool_common("4","5",3);
			}
		}else if("4".equals(childId)){//四星
			calcLotteryNum_tool_common(1,1,1,1);
		}else if("5".equals(childId)){//五星
			calcLotteryNum_tool_common(1,1,1,1,1);
		}else if("6".equals(childId)){
//			calcLotteryNum_tool_common("0", NaN, 1,1);
			int count = 0;//记录一个选中的号的个数, 协议中 仅有一注的可能,没有大于一注的情况, 需要前台设置 selfMutual. 自身互斥,保证每个 selectBalls 中最多一个号
			for (int i = 0; i < lotteryNums.size(); i++) {
				count += lotteryNums.get(i).size();
			}
			if(count == 1){//不足一注
				lotteryCount = 1;
				isTicketAvailable = true;
			}else{//单式
				lotteryCount = 0;
				isTicketAvailable = false;
			}
		}else if("7".equals(childId)){//任二
//			calcLotteryNum_tool_common("0", NaN, 1,1);
			int count = 0;//记录一个选中的号的个数, 协议中 仅有一注的可能,没有大于一注的情况, 需要前台设置 selfMutual. 自身互斥,保证每个 selectBalls 中最多一个号
			for (int i = 0; i < lotteryNums.size(); i++) {
				count += lotteryNums.get(i).size();
			}
			if(count == 2){//满足一注
				lotteryCount = 1;
				isTicketAvailable = true;
			}else{//
				lotteryCount = 0;
				isTicketAvailable = false;
			}
		}
		return lotteryCount;
	}
	
	/**
	 * 时时彩 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String formatLotteryNums_ssc() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		formatStr = "";
		if("3".equals(childId) && "2".equals(salesType)){//三星组三单式.只有一注
			formatStr += lotteryNums.get(1).get(0);
			formatStr += lotteryNums.get(0).get(0);
			formatStr += lotteryNums.get(0).get(0);
		}else if(("6".equals(childId) || "7".equals(childId)) && "0".equals(salesType)){//任选一 或 任选二 的单式(当前版本任选一,任选二只有单式)
			appendFormatNums(lotteryNums, "", false);
		}else {//(当前第一个版本)除了三星的组三单式,时时彩的数据格式化都可以用该方法来实现.
			appendFormatNums(lotteryNums, "*", false);
		}
		return formatStr;
	}
	
	/**
	 * 时时彩 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String showLotteryNums_ssc() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		showStr = "";
		if("3".equals(childId) && "2".equals(salesType)){//三星组三单式.只有一注
			showStr += "- -";
			showStr += lotteryNums.get(0).get(0);
			showStr += lotteryNums.get(0).get(0);
			showStr += lotteryNums.get(1).get(0);
		}else if(("6".equals(childId) || "7".equals(childId)) && "0".equals(salesType)){//任选一 或 任选二 的单式(当前版本任选一,任选二只有单式)
			appendShowNums_directly(lotteryNums, " ");
//		---------------------------------------万利--------------------------------------
		}else if ("2".equals(childId) && "2".equals(salesType)){//大小单双 （大:9  小:0  单: 1  双:2）
			
			String str0="";
			String str1="";
			if(lotteryNums.get(0).size()==1){
				str0=String.valueOf(lotteryNums.get(0).get(0));
			}
			if(lotteryNums.get(1).size()==1){
				str1=String.valueOf(lotteryNums.get(1).get(0));
			}
			showStr +=getDXDS(str0);//第一个选号容器
			showStr +=getDXDS(str1);//第二个选号容器
//			---------------------------------万利--------------------------------
		}else {//(当前第一个版本)除了三星的组三单式,时时彩的数据格式化都可以用该方法来实现.
			int childType = Integer.parseInt(childId);
			for (int i = childType; i < 5; i++) {//一星时,前面有4个 "-",二星时有3个"-"
				showStr += "- ";
			}
			appendShowNums_directly(lotteryNums, " ");
		}
		return showStr;
	}
	
	/**
	 * （大:9  小:0  单: 1  双:2）
	 * @param key
	 * @return
	 */
	private static String getDXDS(String key){
		String showStr="";
		if(StringUtils.isBlank(key)){
			return showStr;
		}
		switch (Integer.parseInt(key)) {
			//小:0
			case 0:
				showStr="小";
				break;
			//单: 1
			case 1:
				showStr="单";
				
				break;
			//双:2
			case 2:
				showStr="双";
				break;
			//	大:9
			case 9:
				showStr="大";
				break;

			default:
				break;
		}
		return showStr;
	}
}
