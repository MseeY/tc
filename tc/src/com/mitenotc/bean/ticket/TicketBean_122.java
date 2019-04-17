package com.mitenotc.bean.ticket;

import java.util.ArrayList;
import java.util.List;

import com.mitenotc.bean.K3Bean;
import com.mitenotc.bean.RATEBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.utils.Getlotterykind;
/**
 * 模拟快3 选票
 * @author admin
 *
 */
public class TicketBean_122 extends TicketBean{
	
	private  static final String TK_SHOW_SLH="&#160;&#160;&#160;&#160;\n(三连号通选)";
	private  static final String TK_SHOW_STH="&#160;&#160;&#160;&#160;\n(三同号通选)";
	
	@Override
	public void onCalc() {
		calcLotteryCount_k3();
	}
	
	@Override
	public String formatLotteryNums() {
		return formatLotteryNums_k3();
	}
	
	@Override
	public String showLotteryNums() {
		return showLotteryNums_k3();
	}
	/**
	 *  模拟快3 计算注数
	 *  
	 * 	K3Bean  去实现
	 * @param lotteryNums2 
	 * @return
	 */
	protected long calcLotteryCount_k3() {
		
//			清空
			    lotteryCount = 0;
			    
				if("0".equals(salesType))
				{
					if("1".equals(childId))
					{//和值
						calcLotteryNum_tool_common("0","0",1);
					}else if("2".equals(childId)){//三同号
						   if(lotteryNums.get(0).size()==0)
						   {
							  calcLotteryNum_tool_common("0","0",0,1); 
						   }else
						   {
							   calcLotteryNum_tool_common("0","0",1,0);
							   if(lotteryNums.get(1).size()!=0)
							   {
								   lotteryCount +=lotteryNums.get(1).size();  
							   }
						   }
					}else if("6".equals(childId)){//二同号单选 11,22#4,5,6   //二同号复选
				         calcLotteryNum_tool_common("0","0",1,1,0);
				         if(lotteryNums.get(2).size() > 0){
				        	 lotteryCount +=lotteryNums.get(2).size();
				        	 isTicketAvailable=true;
				         }
					}else if("5".equals(childId)){//三不同号
					  
						if(lotteryNums.get(0).size() < 3){
						  calcLotteryNum_tool_common("0","0",0,1);
					     }else{
						   calcLotteryNum_tool_common("0","0",3,0);
						   if(lotteryNums.get(1).size()!=0){
							   lotteryCount +=lotteryNums.get(1).size();
							   
						   }
					   }
						
					}else if("8".equals(childId))
					{// 二不同号
						calcLotteryNum_tool_common("0","0",2);
					}
					
				  }else if("2".equals(salesType))
				  {
				     if("5".equals(childId)){ // 胆拖的 三不同号 25
				    	 if(lotteryNums.get(0).size()==2){
				    		 calcLotteryNum_tool_common("2","2",2,1);//"0","2",1,2
				    	 }else if(lotteryNums.get(0).size()==1 && lotteryNums.get(1).size() > 2){
				    		 calcLotteryNum_tool_common("2","2",1,2);//"1","2",1,1
				    	 }
						if(lotteryCount <= 1){  //至少协议中下单至少两注
							lotteryCount=0;
						}
					}else if("8".equals(childId))
					{ // 胆拖的 二不同号 28
						 calcLotteryNum_tool_common("2","2",1,1);
						if(lotteryCount <= 1)
						{//至少协议中下单至少两注
							lotteryCount=0;
						}
					 }
				    }
				ticketAmount = fold*lotteryCount*price;
				if(ticketAmount <=0){//考虑到安全性,如果选择的注数过大会导致 ticketAmount的值大于 int 型 最大数值,导致 ticketAmount 为负数,因此这里从新判断,增加安全性
					isTicketAvailable = false;
				}
				if(lotteryCount >= 1)
				{
					znzh_ks_tosaveBonus((int)lotteryCount,salesType+childId,lotteryNums);//智能追号
				}
			return lotteryCount;
		}	

	//快三 智能追号 奖金
	private void znzh_ks_tosaveBonus(int lotteryCount, String tag, List<List<Integer>> lotteryNums) {
		List<Integer> bonusList;
		switch (Integer.parseInt(tag)) {
   	 // 和值
		   case 1:
//			   tag
			   
			   bonusList=Getlotterykind.getInstance().getK3_hz_List_Bonus(lotteryNums.get(0));//  奖金 8 元
			   
			   RATEBean.getInstance().setBonusScopeList(bonusList);
			 
			break;
//		 // 三同号
	        case 2:
	        	if(lotteryNums.get(0).size() ==0){
	        		bonusList=new ArrayList<Integer>();
		        	bonusList.add(40);  //三同号通选40元
		        	RATEBean.getInstance().setBonusScopeList(bonusList);
	        	}else{
	        		
	        		bonusList=new ArrayList<Integer>();
	        		bonusList.add(40);
	        		bonusList.add(240);
	        		RATEBean.getInstance().setBonusScopeList(bonusList);
	        	}
	        	
	        break;	
//		
//		 // 二同号
	      case 6:
	    	  if(lotteryNums.get(2).size()==0){
	    		  bonusList=new ArrayList<Integer>();
		    	  bonusList.add(80); 
		    	  RATEBean.getInstance().setBonusScopeList(bonusList);
	    	  }else{
	    		  bonusList=new ArrayList<Integer>();
	    		  bonusList.add(15); 
	    		  bonusList.add(95); 
	    		  RATEBean.getInstance().setBonusScopeList(bonusList);
	    	  }
	    	
			break;	
//		// 三不同号
	      case 5:
	    	  if(lotteryNums.get(0).size() <= 3){
	        		bonusList=new ArrayList<Integer>();
		        	bonusList.add(10);  //三连通选10元
		        	RATEBean.getInstance().setBonusScopeList(bonusList);
	        	}else{
	        		bonusList=new ArrayList<Integer>();
	        		bonusList.add(50);
	        		RATEBean.getInstance().setBonusScopeList(bonusList);
	        	}
	    
			break;	
//		// 二不同号
	      case 8:
	    	  bonusList=new ArrayList<Integer>();
	    	  bonusList.add(8); 
	    	  RATEBean.getInstance().setBonusScopeList(bonusList);
			break;
////			----------------胆码
	    // 三不同号
	      case 25:
	    	  bonusList=new ArrayList<Integer>();
	    	  bonusList.add(40); 
	    	  RATEBean.getInstance().setBonusScopeList(bonusList);
			break;
//        二不同号
	      case 28:
	    	  bonusList=new ArrayList<Integer>();
	    	  bonusList.add(8); 
	    	  RATEBean.getInstance().setBonusScopeList(bonusList);
				
			break;	
		default:
			  bonusList=new ArrayList<Integer>();
//	    	  b=Getlotterykind.getInstance().getPl120Bonus(tag);
	    	  bonusList.add(8); 
	    	  RATEBean.getInstance().setBonusScopeList(bonusList);
//				
			break;
		}
	}
	
	/**
	 *模拟 快3   显示字符串的格式化
	 * @return
	 */
	protected String showLotteryNums_k3() {
			showStr = "";
			if("0".equals(salesType)){
				if("1".equals(childId)){//和值
					appendShowNums_directlyk31(lotteryNums.get(0), " ");
				}else if("2".equals(childId)){//三同号
					
					appendShowNums_directlyk31(lotteryNums.get(0)," ","",3);
					if(lotteryNums.size() >=2 ){
						if(lotteryNums.get(1).size()==1){
							
							showStr +=TK_SHOW_STH;
						}
					}
					
				}else if("6".equals(childId)){//二同号  11,22 # 1, 2
					 appendShowNums_directlyk31(lotteryNums.get(0),",","&#160;",2);
					 showStr +="&#160;--&#160;";
					 appendShowNums_directlyk31(lotteryNums.get(1),",","&#160;",0);
					 showStr +="&#160;--&#160;";
					if(lotteryNums.get(2).size()!=0){
						 appendShowNums_directlyk31(lotteryNums.get(2),"*&#160;","",2);
						 showStr +="*&#160;";
					 }
				}else if("5".equals(childId)){//三不同号
					appendShowNums_directlyk31(lotteryNums.get(0), "&#160;");
					if(lotteryNums.size() >=2 ){
						if(lotteryNums.get(1).size()==1){
							
							showStr +=TK_SHOW_SLH;
						}
					}
						
				}else if("8".equals(childId)){// 二不同号
					appendShowNums_directlyk31(lotteryNums.get(0), " ");
				}
			}else if("2".equals(salesType))
			{
			 if("5".equals(childId)){ // 胆拖的 三不同号
					appendShowNumsk3(lotteryNums,"(",") ");
				}else if("8".equals(childId)){ // 胆拖的 二不同号
					appendShowNumsk3(lotteryNums,"(",") ");
				}
			}
			return showStr;
		}
	/**
	 * 用于快三胆拖玩法的  showStr界面号码展示
	 * @param lotteryNums
	 * @param leftfg
	 * @param reightfg
	 */
	
	private  void appendShowNumsk3(List<List<Integer>> lotteryNums, String leftfg, String reightfg) {
		if(0 == lotteryNums.size()){
			 showStr="";
			return;
		}else{
			   showStr +=leftfg;
			for (int i = 0; i < lotteryNums.get(0).size(); i++) {
				showStr +=lotteryNums.get(0).get(i);
			}
			    showStr +=reightfg;
			for (int i = 0; i < lotteryNums.get(1).size(); i++) {
				showStr +=lotteryNums.get(1).get(i)+" ";
			}
		}
	}
	
	/**
	 * 选中号码格式化为第二页显示的String 字符创
	 * @param containers_selectBalls
	 * @param intervalStr 不同容器MG之间选号的间隔符号
	 * @param delimiter 同一容器选中号码之间的 间隔符号
	 */

	private  void appendShowNums_directlyk31(List<Integer> selectBalls,String delimiter) {
		if(selectBalls.size() ==0)
		{
			showStr += delimiter;
			return;
		}
		for (int i =0;i<selectBalls.size();i++) 
		{
			showStr += selectBalls.get(i)+delimiter;
		}
	}
//	三同号
	private  void appendShowNums_directlyk31(List<Integer> selectBalls,String zhuStr,String fgStr,int n) {
		
		   showStr += "";
		for (int i = 0; i < selectBalls.size(); i++) {
			if(0 != i){
				showStr += zhuStr;
			}
			if(selectBalls.get(i)!=null && n > 0){
				appendShowNums_directlyk31(selectBalls.get(i).toString(),fgStr, n);
			}else{
				showStr +=selectBalls.get(i).toString();
			}
			
		}
	
	}
	private  void appendShowNums_directlyk31(String integerStr, String fgStr,int n) {
		for (int i = 1; i <= n; i++) {
			if(i!=1){
				showStr += fgStr;
			}
			  showStr  += integerStr+"";
		}
	}
	/**
	 * 模拟快3 号码 格式化到 网络协议要求的格式 
	 * 
	 * K3Bean 实现
	 * @return
	 */
	public  String formatLotteryNums_k3() {//该网络的字符串 和 界面中显示的字符串的格式的唯一区别是分隔符不同
		formatStr = "";
		if("0".equals(salesType)){
				if("1".equals(childId)){//和值
					appendFormatNumsK3(lotteryNums.get(0),"^","",0); 
			}else if("2".equals(childId)){//三同号单选
				appendFormatNumsK3(lotteryNums.get(0),"^","*",3); 
			}else if("3".equals(childId)){//三同号通选
				formatStr +="A*A*A";
			}else if("6".equals(childId)){//二同号  单选
				if(lotteryNums.get(0).size() >= 1 && lotteryNums.get(1).size() >= 1){
							
							for (int i = 0; i < lotteryNums.get(0).size(); i++) {
								for (int j = 0; j < lotteryNums.get(1).size(); j++) {
									if( 0 != j){
										formatStr += "^";
									}else if( 0 != i){
										formatStr += "^";
									}
									formatStr += lotteryNums.get(0).get(i)+"*"+lotteryNums.get(0).get(i)+"*"+lotteryNums.get(1).get(j);
								}
							}
				}
				if(lotteryNums.get(2)!=null && lotteryNums.get(2).size() >0){
					   formatStr +="|";
					   appendFormatNumsK3(lotteryNums.get(2),"^"," ",0); 
				}
				
			}else if("5".equals(childId)){//三不同号
				for (int i = 0; i < lotteryNums.get(0).size(); i++) {
					for (int j = i+1; j < lotteryNums.get(0).size(); j++) {
						for (int j2 = j+1; j2 < lotteryNums.get(0).size(); j2++) {
							formatStr +=lotteryNums.get(0).get(i) + "*" + lotteryNums.get(0).get(j);
							
							formatStr += "*" +lotteryNums.get(0).get(j2);
							
							if( i != lotteryNums.get(0).size()-3)
							{
								formatStr += "^";
							}
						}
						
					}
				}
					
			}else if("4".equals(childId)){//三不同号
				if(lotteryNums.size() >=2 ){
					if(lotteryNums.get(1).size()==1){
						
						formatStr +="A*B*C";
					}
				}
			}else if("8".equals(childId)){// 二不同号  单选
				
				for (int i = 0; i < lotteryNums.get(0).size(); i++) {
					for (int j = i+1; j < lotteryNums.get(0).size(); j++) {
						      formatStr +=lotteryNums.get(0).get(i) + "*" + lotteryNums.get(0).get(j);
							if (i != lotteryNums.get(0).size()-2) 
							{
								formatStr += "^";
							}
					}
			   }

			}
		}else if("2".equals(salesType)){
			if("5".equals(childId)){ // 胆拖的 三不同号
				appendFormatNumsK3(lotteryNums.get(0),"","",0);
				formatStr +="*";
				appendFormatNumsK3(lotteryNums.get(1),"","",0);
			}else if("8".equals(childId)){ // 胆拖的 二不同号
				appendFormatNumsK3(lotteryNums.get(0),"","",0);
				formatStr +="*";
				appendFormatNumsK3(lotteryNums.get(1),"","",0);
			}
		}
		
		return formatStr;
	}
	/**
	 *  *  *  协议中的格式
	 * 对一个容器中的号码格式的拼接
	 * @param selectBalls  号码
	 * @param fgStr
	 * @param n  单个号码重复次数
	 */
	private  void appendFormatNumsK3(List<Integer> selectBalls,String zhuStr,String fgStr,int n) {
		
		formatStr += "";
		for (int i = 0; i < selectBalls.size(); i++) {
			if(0!=i){
				formatStr += zhuStr;
			}
			if(selectBalls.get(i)!=null&& n > 0){
				
				appendFormatNumsK3(selectBalls.get(i).toString(),fgStr, n);
			}else{
				
				formatStr  +=selectBalls.get(i).toString();
			}
			
		}
	
	}
	private  void appendFormatNumsK3(String integerStr, String fgStr,int n) {
		for (int i = 1; i <= n; i++) {
			if(i!=1){
				formatStr += fgStr;
			}
			    formatStr  += integerStr+"";
		}
	}
	
	@Override
	public List<MessageJson> getTicketJsonArray() {
		reloadSizes();
		List<MessageJson> list = new ArrayList<MessageJson>();
		
		if("0".equals(salesType)){//普通  三同号  三不同号   二同号  需要拆分 (只正对特殊票处理其余为default)
			switch (Integer.parseInt(childId)) {
				 // 三同号
		        case 2:
		        	if(lotteryNums.get(1).size()==1){
                      //	三同号通选
		        		list.add(getTicketJsonk3("3", "0", "A*A*A", "1", "200", fold+""));
		        		if(lotteryNums.get(0).size() > 0){
		        			//	 三同号普通票
		        			list.add(getTicketJsonk3("2", "0", formatLotteryNums(), String.valueOf(lotteryCount-1),
		        					String.valueOf(ticketAmount-200), fold+""));
		        		}
		        	}else {
		        		list.add(getTicketJson());
		        	}
		        break;	
			
			 // 二同号
		      case 6:
		    	  if(lotteryNums.get(2).size() >= 1){
		    		  long ticketNum=lotteryNums.get(2).size();  //二同号复选 票数
		    		  long ticketNum1=lotteryCount-ticketNum;  //二同号单选 票数
		    		 
		    		  if( 0 != ticketNum1 ){
		    		    String Str=formatLotteryNums();
		    			String str1= Str.substring(0, Str.indexOf("|"));
		    			String str2= Str.substring(Str.indexOf("|")+1,Str.length());
		    			 //	二同号 单选 06 --1*1*2
		    			list.add(getTicketJsonk3("6", "0", str1, ticketNum1+"", 
		    					 ((ticketNum1 * 200 ) * fold)+"",  fold+""));
		    			 //	二同号 复选 07 --11*
		    			 list.add(getTicketJsonk3("7", "0", str2,ticketNum+"" ,
		    					 ((ticketNum * 200) * fold)+"", fold+""));
		    		 }else {
		    			 String Str=formatLotteryNums();
		    			 Str =Str.replace("|", "");
		    			 list.add(TicketBean.getTicketJsonk3("7", "0", Str,ticketNum+"" ,
		    					 ((ticketNum*200)*fold)+"", fold+""));
		    		 }
		    		 
		        	}else {
		        		list.add(getTicketJson());
		        	}
		        	
		        	
				break;	
			// 三不同号
		      case 5:
		    	  if(lotteryNums.get(1).size()==1){
                      //	三连号通选
		    		  list.add(getTicketJsonk3("4", "0", "A*B*C", "1", "200", fold+""));
		        		if(lotteryNums.get(0).size() >= 3){
		        			//	 三不同号 普通票
		        			list.add(getTicketJsonk3("5", "0", formatLotteryNums(), String.valueOf(lotteryCount-1),String.valueOf((ticketAmount-200)), fold+""));
		        		}
		        	}else{
		        		list.add(getTicketJson());
		        	}
		    
				break;	

				default:
					list.add(getTicketJson());
					break;
			}
			
			
		}else{
			list.add(getTicketJson());
		}
		return list;
	}
}
