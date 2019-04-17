package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.mitenotc.utils.BaseCalc;
import com.mitenotc.utils.Getlotterykind;



public class K3Bean extends TicketBean {
	
  	
	private  static final String TK_SHOW_SLH="&#160;&#160;&#160;&#160;\n(三连号通选)";
	private  static final String TK_SHOW_STH="&#160;&#160;&#160;&#160;\n(三同号通选)";
	
	
	/**
	 * 计算票中的号码共 有多少 注,主要用于选号界面的 notice的显示.
	 * @return
	 */
	public long calcLotteryCount(){
//		清空
		lotteryCount = 0;
		reloadSizes();//重新加载 sizes
		if(StringUtils.isNumeric(lotteryId)){
			switch (Integer.parseInt(lotteryId)) {
			case 120:
//				快三特殊子类和父类之间涉及数据传递
				calcLotteryCount_k3();
				
				break;
			case 122:
				is_blond_integral=true;//模拟提示
//				快三特殊子类和父类之间涉及数据传递
				calcLotteryCount_k3();
				
				break;
			case 123:
//				快三特殊子类和父类之间涉及数据传递
				calcLotteryCount_k3();
				
				break;
			default:
				break;
			}
		}
		
		ticketAmount = fold*lotteryCount*price;
		if(ticketAmount <=0){//考虑到安全性,如果选择的注数过大会导致 ticketAmount的值大于 int 型 最大数值,导致 ticketAmount 为负数,因此这里从新判断,增加安全性
			isTicketAvailable = false;
		}
//		toTicketBeanSave();---用来测试时候查看  调用
		
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
	 * 界面显示的字符串的格式化. 主要用在 倍投界面的显示
	 * @return
	 */
	public String showLotteryNums() {
		showStr = "";
		if(StringUtils.isNumeric(getLotteryId())){
			switch (Integer.parseInt(getLotteryId())) {
            case 120:
				showLotteryNums_k3();
				break;
            case 122:
            	showLotteryNums_k3();
            	break;
            case 123:
            	showLotteryNums_k3();
            	break;
			default:
				break;
			}
		}
		return showStr;
	}
	

	/**
	 * 把号码格式到 网络协议要求的格式
	 * @return
	 * ----TODO  万利 改动 private -->public
	 */
	public String formatLotteryNums() {
		calcLotteryCount();//格式前 先对注释进行计算. 出于安全性考虑
		if(!isTicketAvailable){//安全性考虑,如果当前的 ticket 不是有效的ticket,则返回空//TODO 这里最好提醒用户,票不是有效的
			return null;
		}
		formatStr = "";
		if(StringUtils.isNumeric(lotteryId)){
			switch (Integer.parseInt(lotteryId)) {

			case 120:
				formatLotteryNums_k3();
				break;
			case 122:
				formatLotteryNums_k3();
				break;
			case 123:
				formatLotteryNums_k3();
				break;
			default:
				break;
			}
		}
		return formatStr;
	}
	/**
	 * 获取给用户的消息提示,选号界面 消息提示的格式化
	 * @return
	 */
	public String getNotice(){
		calcLotteryCount();
		if(isTicketAvailable){
			notice = replaceNoticeText(lotteryCount, ticketAmount/100);
		}else {
			notice = replaceNoticeText(0, 0);
		}
		
		return notice;
	}
	

	
	
/*	*//**
	 * 重置该票的数据
	 *//*
	public void reSet() {
		lotteryId = "118";//默认为双色球
		salesType = "0";//单式为0,复试为1,胆拖为2
		lotteryNums = new ArrayList<List<Integer>>();
		fold =1;//默认倍数为1;
		lotteryCount = 1;
		price = 200;//以分为单位
		ticketAmount = fold*lotteryCount*price;
	}*/
	//**********************************快三 方法区*************************************
	// 和值（1）	        单式投注 (0)	    3
	// 三同号单选（2）	单式投注 （0）	    1*1*1
	// 三同号通选（3）	单式投注  (0)	    A*A*A
	// 三连号通选（4）	单式投注 （0）	    A*B*C
	// 三不同号投注（5）	单式投注（0）    1*2*3
    // 二同号单选（6）	单式投注（0）	    1*1*2
	// 二同号复选（7）	单式投注(0)       1（代表11*）
	// 二不同号投注（8）	单式投注（0）	1*2
	//**********************************快3 方法区*************************************
	/**
	 * 快3 号码 格式化到 网络协议要求的格式 
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



	/**
	 * 快3的显示字符串的格式化
	 * @return
	 */
	
	public String showLotteryNums_k3() {
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
	 * 容器中的  注数
	 * 计算 快三注数
	 * @return
	 */
	
	public  long calcLotteryCount_k3()
	{
//		清空
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
			    	 if(lotteryNums.get(0).size()<2){
			    		 
			    		 calcLotteryNum_tool_common("2","2",2,1);
			    		 
			    	 }else {
			    		 
			    		 calcLotteryNum_tool_common("2","2",2,1);
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
	
	
	/**
	 * 普通模式,多个容器选择的情况,也可以是1个容器. 默认为单式为0,复式为1;
	 * @param miniNums
	 */
	  protected void calcLotteryNum_tool_common(long... miniNums){
		calcLotteryNum_tool_common("0","1",miniNums);
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
	 * 普通模式,多个容器选择的情况,也可以是1个容器
	 * @param saleType0 单式的销售方式的数值
	 * @param saleTpye1 复式的销售方式的数值
	 * @param miniNums
	 */
	public void calcLotteryNum_tool_common(String saleType0,String saleTpye1,long... miniNums){////System.out.println("mininums.length === "+miniNums.length+" : childid = "+childId);
		//个容器中的数量初始化
//		reloadSizes();//这行代码最好放在外边
		
		int flag = isLotteryAvalible(miniNums);
		if(flag == -1){//不足一注
			salesType = saleType0;
			lotteryCount = 0;
			isTicketAvailable= false;
		}else if(flag == 0){//单式
			salesType = saleType0;
			lotteryCount = 1;
			isTicketAvailable=true;
		}else {//复式
			if(!NaN.equals(saleTpye1)){//not a number
				salesType = saleType0;
				lotteryCount = calcLotteryCount(miniNums);
				isTicketAvailable = true;
			}
		}
		
	}
	
	
	/**
	 * 检验当前的 注数是否满足一注
	 * miniNums的长度 应该和 lotteryNums 的 长度一致
	 * @param miniNums
	 * @return
	 */
	public int isLotteryAvalible(long... miniNums){
		int flag = -1;// -1为不满足一注, 0,为 刚好一注,1为大于一注. 同时 0 和协议中的销售方式 单式一致,1和协议中的复式一致
         
		for (int i = 0; i < lotteryNums.size(); i++) {////System.out.println("sizes.length = "+sizes.length); ////System.out.println("mininums.length = "+miniNums.length); ////System.out.println("i = "+i);
				if(sizes[i] < miniNums[i]){//存在一个未选满的就返回 未选满
					
					flag =-1;
				}else if(sizes[i] > miniNums[i]){//排除未选满的状态后,存在大于一注的情况,则设置 flag 为 大于一注
					flag = 1;
				}else {//排除以上两种情况后,就是刚好一注的情况
					if(flag != 1)//只有当 当前没有 flag = 1的情况的时候才去赋值
						flag = 0;
				}
				
			}
		return flag;//默认返回 未满足一注.
	}
	
	/**
	 * 排列组合计算注数
	 * @param miniNums
	 * @return
	 */
	private int calcLotteryCount(long... miniNums){
		int count = 1;
		
		for (int i = 0; i < lotteryNums.size(); i++) {
			count *= BaseCalc.calc(sizes[i],miniNums[i]);//[1, 5, 1, 0, 0, 0, 0]，[0, 0, 1]
		}
			
		return count;
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
	 * @return the lotteryCount
	 */
	public long getLotteryCount() {
		return lotteryCount;
	}


	/**
	 * @param lotteryCount the lotteryCount to set
	 */
	public void setLotteryCount(long lotteryCount) {
		this.lotteryCount = lotteryCount;
	}




	/**
	 * @param ticketAmount the ticketAmount to set
	 */
	public void setTicketAmount(int ticketAmount) {
		this.ticketAmount = ticketAmount;
	}


	public boolean isIsk3BeanAvailable1() {
		return false;
	}


	/**
	 * @return the childId
	 */
	public String getChildId() {
		return childId;
	}


	/**
	 * @param childId the childId to set
	 */
	public void setChildId(String childId) {
		this.childId = childId;
	}


	public static String getTkShowSlh() {
		return TK_SHOW_SLH;
	}

	public static String getTkShowSth() {
		return TK_SHOW_STH;
	}

}
