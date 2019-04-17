package com.mitenotc.bean.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.BaseCalc;
import com.mitenotc.utils.Getlotterykind;
import com.mitenotc.utils.ParseLottery;
import com.mitenotc.utils.JCBaseCalc;
/**
 * 竞彩足球(包含竞足单关计算) ticket
 * @author mitenotc
 * 
 * 竞彩玩法和其它彩种的不同之处 
 * 就是每一个salesType 对应一个独立的玩法
 *
 */
public class TicketBean_310 extends TicketBean {
//	210	让球胜平负 
//	218	胜平负 
//	209	混合过关  
//	protected static List<String> batchSessionList=new ArrayList<String>();//缩写为BS存储 20141117-001
	protected static Map<String,List<String>> selectMap;
	protected String sampleNotice_jc = MyApp.res.getString(R.string.basebuy_notice_jc_text);
	protected String sampleNotice_jcLack = MyApp.res.getString(R.string.basebuy_notice_jclack_text);
	
	List<Integer> betcodes=null;
	List<Boolean> isDanList=null;
	@Override
	public long calcLotteryCount() {
		lotteryCount = 0;//清空注数
		session=0;
		if(StringUtils.isBlank(salesType)||StringUtils.isBlank(childId) || childId.equals("0")){//检测是否满足场次要求(默认是过关) 为了安全考虑
			isTicketAvailable=false;
			return lotteryCount;
		}
		betcodes= new ArrayList<Integer>();//每场筛选的 胜平负
		isDanList= new ArrayList<Boolean>();//每场筛选 是否设置胆拖
		for (int i = 0; i < selectMB.size(); i++) {
			MessageBean mb=selectMB.get(i);//拿出筛选场MB
			String key="";
			if(mb.getC().length() >6){//为了字符串截取安全
				 key=mb.getC().subSequence(mb.getC().length()-6, mb.getC().length())+"-"+mb.getG();
				 if(lotteryTag.containsKey(key)){
					 mb.setCH_NUM(lotteryTag.get(key).size());
				 }
			}else{
				mb.setCH_NUM(0);
			}
			isDanList.add(mb.isISD());
			betcodes.add(mb.getCH_NUM());
			if(mb.getCH_NUM() > 0){
				session++;
			}
		}
		for (int j = 0; j < passTypeList.size(); j++) {//串关方式
			int isDanNum = getIsDanNum(isDanList);//设置胆拖数目
			 if(passTypeList.get(j) > 0 && passTypeList.get(j) <= 108){//单串 单一串关 最多108 (8串1) 小于108
				lotteryCount +=JCBaseCalc.getAllLC(betcodes, (passTypeList.get(j)-100),isDanList,isDanNum);
			}else if(passTypeList.get(j) > 108){//混串
				int[] zl =	Getlotterykind.getCG(passTypeList.get(j));//混串所对应的单串
				for (int k = 0; k < zl.length; k++) {
					lotteryCount+=JCBaseCalc.getAllLC(betcodes, (zl[k]-100),isDanList,isDanNum);
				}
			}
		}
//		lotteryCount+=calcJCZS((n-100), selectMessageBean);
//		Map<String,Integer> map=new HashMap<String, Integer>();
//		for (int i = 0; i < selectMessageBean.size(); i++) {
//			MessageBean mb=selectMessageBean.get(i);
//			if(lotteryTag.containsKey(mb.getC()+"-"+mb.getG())){
//				map.put(mb.getC()+"-"+mb.getG(), lotteryTag.get(mb.getC()+"-"+mb.getG()).size());	
//			}
//		}
//		for (int i = 0; i < passTypeList.size(); i++) {
//			if(passTypeList.get(i) >0 && passTypeList.get(i)<=108){
//				lotteryCount+=BaseCalc.JCCalc(map, (passTypeList.get(i)-100), selectMessageBean.size());
//			}
//		}
//		System.out.println("310--->fold-- "+fold+"|lotteryCount---"+lotteryCount+"|price --->"+price);
       //(场次,串)+(场次,果)
		ticketAmount = fold*lotteryCount*price;//计算金额
		if(selectMB.size() >= Integer.parseInt(childId)){//场次数
			isTicketAvailable=true;
		}else{
			isTicketAvailable=false;
		}
//		System.out.println("310---pt--------> "+passTypeList.toString()+" tA  : "+ticketAmount+" lc  : "+lotteryCount);
		return lotteryCount;
	}
	static List<String> returnList=new ArrayList<String>();
	List<String> pxList=new ArrayList<String>();
	
	
/*******************奖金优化拆单子票*******************/
	@Override
	public  LinkedList<Object[]>  createChildTicket(){
		LinkedList<Object[]> cTicketList=new LinkedList<Object[]>();
		List<MessageJson> ctbList=new LinkedList<MessageJson>();
		MessageJson tempOb=null;
		String key="",str="" ,vsStr="";
		for (int i = 0; i < selectMB.size(); i++) {
			MessageBean mb=selectMB.get(i);
			if(mb!=null && mb.getC().length() >6){//批次
				key=mb.getC().subSequence(mb.getC().length()-6, mb.getC().length())+"-"+mb.getG();//141119-001
				if(lotteryTag.containsKey(key)&& lotteryTag.get(key)!=null ){//(3_1.6,1_0.1,0_1.20);
						for (int j = 0; j < lotteryTag.get(key).size(); j++) {
							 str=lotteryTag.get(key).get(j);//210 218 : 3_1.6   209 : 210^3_1.6   
							 tempOb=new MessageJson();
							 vsStr=mb.getK();
							 if(vsStr.length() > 4){
								 vsStr=mb.getK().substring(0, 4);
							 }
							 
							 if("209".equals(salesType)){//218^1_3.40
								 if(str.substring(4,str.length()).startsWith("3")){//胜
									 vsStr+="|胜";
								 }else if(str.substring(4,str.length()).startsWith("1")){//平
									 vsStr+="|平";
								 }else if(str.substring(4,str.length()).startsWith("0")){//负
									 vsStr+="|负";
								 }
								 tempOb.put("ms", vsStr);
								 tempOb.put("sb_json", str.subSequence(0, 4)+key+"("+str.substring(4,str.length())+")");
							 }else{
								 if(str.startsWith("3")){//胜
									 vsStr+="|胜";
								 }else if(str.startsWith("1")){//平
									 vsStr+="|平";
								 }else if(str.startsWith("0")){//负
									 vsStr+="|负";
								 }
								 tempOb.put("ms", vsStr);
								 tempOb.put("sb_json", key+"("+str+")");
							 }
							 tempOb.put("sp", str.split("_")[1]);//1.6
							 if(-1==ctbList.indexOf(tempOb)){//不能重复添加
								 ctbList.add(tempOb);
							 }
						}
					}
				}
		}
		MessageJson[] temp=new MessageJson[ctbList.size()];
		for (int i = 0; i < ctbList.size(); i++) {
			temp[i]=ctbList.get(i);
		}
		MessageJson[]  itemMj=null;
		LinkedList<Object[]> tempArray=null;
		for (int j = 0; j < passTypeList.size(); j++) {//串关方式
			if(101 == passTypeList.get(j)){
				tempArray=new LinkedList<Object[]>();
				for (int i = 0; i < temp.length; i++) {
					itemMj=new MessageJson[]{temp[i]};
					tempArray.add(itemMj);
				}
			}else if(passTypeList.get(j) > 101 && passTypeList.get(j) <= 108){//单串 M串N 最多108 (8串1) 小于108
				tempArray=BaseCalc.cmn(temp, (passTypeList.get(j)-100));//按串关方式组合
				
			}
//			else if(passTypeList.get(j) > 108){//混串  M 串 N 暂不支持
//			}
			if(tempArray!=null && tempArray.size() > 0){
				boolean tb;
				for (int i = 0; i < tempArray.size(); i++) {
					  tb=true;
					for (int k = 0; k < tempArray.get(i).length; k++) {//设置串关
						MessageJson mj=(MessageJson) tempArray.get(i)[k];
						mj.put("cg", String.valueOf(passTypeList.get(j)));//串关
					}
					if(1==tempArray.get(i).length){//单关
							cTicketList.add(tempArray.get(i));
					}else if(tempArray.get(i).length >=2){//串关
						MessageJson m0=(MessageJson) tempArray.get(i)[0];
						MessageJson m2=(MessageJson) tempArray.get(i)[1];
							
//						if(!StringUtils.isBlank(childId) && "1".equals(childId)){//单关
//							tb=true;
//						}else 
							
						if(!StringUtils.isBlank(salesType) && "209".equals(salesType)){//混关
							//"218^141226-002".length()=14;
							if(m0.getString("sb_json").length() > 14  && m2.getString("sb_json").length() > 14){
								if(m0.getString("sb_json").substring(0, 14).equals(m2.getString("sb_json").substring(0, 14))){
									tb=false;
								}
							}
						}else{//非混关 相同场次不能形成一个组合结果
							if(m0.getString("sb_json").length() > 10  && m2.getString("sb_json").length() > 10){
								if(m0.getString("sb_json").substring(0, 10).equals(m2.getString("sb_json").substring(0, 10))){
									tb=false;
								}
							}
						}
						if(tb){//剔除同一场次的 组合情况
							cTicketList.add(tempArray.get(i));
						}
					}
				}
			}
		}
		return cTicketList;
	}
//	public List<MessageJson> boJcTicketJson() {
//		List<MessageJson> returnArray=new ArrayList<MessageJson>();
//		MessageJson msg=null ,tmsg = null;
//		String b="";
//		for (int i = 0; i < cTicketList.size(); i++) {
//			if(cTicketList.get(i) !=null && cTicketList.get(i).length > 0){
//				msg =new MessageJson();
//				for (int j = 0; j < cTicketList.get(i).length; j++) {
//					tmsg=(MessageJson) cTicketList.get(i)[j];
//					if(!tmsg.isNull("cg") && j==0){//只拿一个就行
//						msg.put("A", tmsg.get("cg"));//A	过关方式
//					}
//						b += tmsg.get("sb_json");
//					}
//				if(!tmsg.isNull("zs")){//D	注数
//					msg.put("B", tmsg.get("cg"));
//				}
//				if(!tmsg.isNull("bs")){//C	倍数
//					msg.put("B", tmsg.get("cg"));
//				}
//				if(!StringUtils.isBlank(b)){//-----TODO------投注代码 可能需要根据注数重新拼接字符串
//					msg.put("B", b);//B	投注代码，注与注之间用^分开
//				}
//				returnArray.add(msg);	
//			}
//			
//		}
//		E	投注场次（最小场次^投注最大场次）前端不用传入
//		return returnArray;
//	}

//    public static List<CTicketBean>  combination(List<CTicketBean> list, int n) {
//    	List<CTicketBean> returnlist=new ArrayList<CTicketBean>();
//		   return returnlist;
//	   }

//	   private static void combination(List<CTicketBean> list, int n,int m,List<CTicketBean> returnlist) {
//		   String s="";
//		   CTicketBean ctb=null;
//		   if(n==0 ) {
//		     return;
//		   }else {
//			   if(m==list.size()) {
//			     return;
//			   }
////			   StringBuilder s = new StringBuilder(sb.toString()+list.get(m));
//               ctb=list.get(m);
//	           s=ctb.getSubmitStr();
//	           s+=ctb.getSubmitStr();
//			   ctb.setSubmitStr(s);
//			   if(-1==returnlist.indexOf(ctb)){
//				   returnlist.add(ctb);
//			   }else{
//				   returnlist.remove(returnlist.indexOf(ctb));
//			   }
//			   m++;
//			   combination(list,n-1,m,returnlist);
//			   combination(list,n,m,returnlist);
//		   }
//	   }

	public long getTicketAmount() {
		calcLotteryCount();
		return ticketAmount;
	}
	public boolean isTicketAvailable() {
		calcLotteryCount();
		return isTicketAvailable;
	}
	/**
	 * 把竞彩TicketBean 封装成4001 下单 G字段:票信息(LIST)：
	 * 参考格式 141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
		标签	描述	长度	可能值
		A	过关方式	*	串关格式说明
		B	投注代码，注与注之间用^分开	*	投注代码说明
		C	倍数	*	
		D	注数	*	
	 *  @return
	 */
//	  "D":"2","A":"102^103^104^105",
//	   非混关
//    "B":"20141120-001(3_0.00);20141120-002(1_0.00);20141120-003(3_0.00);20141120-004(3_0.00);20141120-005(1_0.00)",
//	   混关
//    "B":"218^141208-001(3_1.86);210^141208-001(1_3.40);218^141208-002(1_3.40);210^141208-002(3_3.35)",
//    "C":"10"
	@Override
	public MessageJson getTicketJson() {
		lotteryCount=calcLotteryCount();//为了安全下单前再次计算
		MessageJson msg = new MessageJson();
		msg.put("A", tBunch);
		msg.put("B", formatLotteryNums_details());
		msg.put("C", String.valueOf(fold));
		msg.put("D", String.valueOf(lotteryCount));
		return msg;
	}
	/**
	 * 竞彩 投注内容
	 * 例如:218 210: 141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
	 *         209 : 141208-001(218^3_1.86#210^1_3.40)
	 */
	@Override
	public String formatLotteryNums() {
		formatStr="";
		MyApp.order.setIssue("20000");//测试 
		if(StringUtils.isBlank(salesType))return formatStr;
		switch (Integer.parseInt(salesType)) {
			//	让球胜平负 //胜平负  //141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
			case 210:
			case 218:
				formatStr=formatContentUtils(0,",");
				break;
			case 209://	混合过关  141208-001(218^3_1.86#210^1_3.40) 
				formatStr=formatContentUtils(1,"#");
				break;
			default:
				System.out.println("210--salesType is null ");
				break;
		}
		return formatStr;
	}
	/**
	 * 竞彩 投注详情  与内容有所差异
	 * * 例如:218 210: 141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
	 *           209 : 218^141208-001(3_1.86);210^141208-001(1_3.40);218^141208-002(1_3.40);210^141208-002(3_3.35)
	 * @return
	 */
	public String formatLotteryNums_details() {
		formatStr="";
		MyApp.order.setIssue("20000");//测试 
		if(StringUtils.isBlank(salesType))return formatStr;
		switch (Integer.parseInt(salesType)) {
		//	让球胜平负 //胜平负  //141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
		case 210:
		case 218:
			formatStr=formatContentUtils(0,",");
			break;
		case 209://	混合过关   218^141208-001(3_1.86);
			formatStr=formatContentUtils_details();
			break;
		default:
//			System.out.println("210--salesType is null ");
			break;
		}
		return formatStr;
	}
	//特殊混关
	private String formatContentUtils_details() {
		String str="";
		String dan="";
		for (int i = 0; i < selectMB.size(); i++) {
			MessageBean mb=selectMB.get(i);
			   String key="";
			if(mb!=null && mb.getC().length() >6){//为了字符串截取安全
					key=mb.getC().subSequence(mb.getC().length()-6, mb.getC().length())+"-"+mb.getG();
//			        MyApp.order.setIssue(mb.getC().subSequence(mb.getC().length()-6, mb.getC().length()));//测试
					if(lotteryTag.containsKey(key))//(3_1.6,1_0.1,0_1.20);
					{
						List<String> sLit=lotteryTag.get(key);
						if(sLit.size() >0){
							for (int j = 0; j < sLit.size(); j++) {
								str += sLit.get(j).substring(0,4)+key;//211^141119-001
								if(j == sLit.size()-1){
									str +="("+sLit.get(j).substring(4,sLit.get(j).length())+")";
								}else{
									str += "("+sLit.get(j).substring(4,sLit.get(j).length())+");";
								}
						    }
						}
//						if(mb.isISD() && (dan.length()>6||0==dan.length())){ 
//							dan +=key;//胆码(批次号_场次)
//						}else if(mb.isISD()){
//							dan +=key+";";//胆码(批次号_场次)
//						}
						
						if(mb.isISD()){
							dan += key+";";
						}
						
						if(i!=(selectMB.size()-1)) {
							str+=";";//普通 结尾方式
						}else if(!StringUtils.isBlank(dan)){//胆码区分增加在尾部
							str+="*"+dan.substring(0, dan.length()-1);//把最后一个胆的“;”去掉
						}
					}
			}
		}
		return str;
	}
	/**
	 * 格式化提交代码
	 * @param tag  0(默认) 1 混关特殊 胆拖后要增加  :  *+批次号(六位)+场次号
	 * @param subTag
	 * @return
	 */
	private String formatContentUtils(int tag,String subTag) {
		String str="";
		String dan="";
		switch (tag) {
		case 1://混关
			for (int i = 0; i < selectMB.size(); i++) {
				MessageBean mb=selectMB.get(i);
				String key="";
				if(mb!=null && mb.getC().length() >6){//为了字符串截取安全
					key=mb.getC().subSequence(mb.getC().length()-6, mb.getC().length())+"-"+mb.getG();
//			        MyApp.order.setIssue(mb.getC().subSequence(mb.getC().length()-6, mb.getC().length()));//测试
					if(lotteryTag.containsKey(key))//(3_1.6,1_0.1,0_1.20);
					{
						List<String> sLit=lotteryTag.get(key);
						if(sLit.size() >0) 
						{
							str +=key;//141119-001
							str+="(";
							for (int j = 0; j < sLit.size(); j++) {
								if(j == sLit.size()-1){
									str += String.valueOf(sLit.get(j));
								}else{
									str += sLit.get(j)+subTag;
								}
							}
							str+=")";
						}
						
						if(mb.isISD() && (dan.length()>6||0==dan.length())){ 
							dan +=key;//胆码(批次号_场次)
						}else if(mb.isISD()){
							dan +=key+";";//胆码(批次号_场次)
						}
						
						if(i!=(selectMB.size()-1)) {
							str+=";";//普通 结尾方式
						}else if(!StringUtils.isBlank(dan)){
							str+="*"+dan;
						}
					}
				}
			}
			break;

		default://非混关
			for (int i = 0; i < selectMB.size(); i++) {
				MessageBean mb=selectMB.get(i);
				String key="";
				if(mb!=null && mb.getC().length() >6){//为了字符串截取安全
					key=mb.getC().subSequence(mb.getC().length()-6, mb.getC().length())+"-"+mb.getG();
//			        MyApp.order.setIssue(mb.getC().subSequence(mb.getC().length()-6, mb.getC().length()));//测试
					if(mb.isISD()) str+="(";//是否设置为胆拖
					if(lotteryTag.containsKey(key))//(3_1.6,1_0.1,0_1.20);
					{
						List<String> sLit=lotteryTag.get(key);
						if(sLit.size() >0) 
						{
							str +=key;//141119-001
							str+="(";
							for (int j = 0; j < sLit.size(); j++) {
								if(j == sLit.size()-1){
									str += String.valueOf(sLit.get(j));
								}else{
									str += sLit.get(j)+subTag;
								}
							}
							str+=")";
							if( mb.isISD()) str+=")";//胆拖 结尾方式
						}
						if(i!=(selectMB.size()-1)) {
							str+=";";//普通 结尾方式
						}
					}
				}
			}
			break;
		}
		return str;
	}
	/**
	 * 竞彩添加具体的选票数据
	 * @param m
	 */
	@Override
	public void setSelectMessageBean(List<MessageBean> listMb) {
		selectMB=listMb;
	}

	@Override
	public String getNotice() {
		calcLotteryCount();
//		System.out.println("210---------session"+session);
	    notice = replaceNoticeString(session);
		return notice;
	}
	
	@Override
	public String getNotice1() {
	    notice = replaceNoticeString(session);
		return notice;
	}

	/**
	 * 提示筛选场数
	 * @param num
	 * @return
	 */
	public String replaceNoticeString(long num){
		System.out.println(";;;;;;;;;;;"+num+";;;;;;"+childId);
		if(num == 0){
			return MyApp.res.getString(R.string.basebuy_notice_jc_text1);
		}else if(num >= Integer.parseInt(childId)) {
			return StringUtils.replaceEach(MyApp.res.getString(R.string.basebuy_notice_jc_text), new String[]{"NUM"}, new String[]{String.valueOf(num)});
		}else{
			return StringUtils.replaceEach(MyApp.res.getString(R.string.basebuy_notice_jclack_text), new String[]{"NUM","LACK_NUM"},
					new String[]{String.valueOf(num),String.valueOf(Integer.parseInt(childId)-num)});
		}
	}
	/**
	 * 自由过关计算注数
	 *  select 串关对应的 数 例如 102 :2
	 * @param select
	 * @param listInfos
	 * @return
	 */
//	public int calcJCZS(int select,List<MessageBean> listInfos){
//		List<Integer> betcodes = new ArrayList<Integer>();
//		List<Boolean> isDanList = new ArrayList<Boolean>();
//		for(int i=0;i<listInfos.size();i++){
//			MessageBean mb = listInfos.get(i);
//			int checkNum = mb.getCH_NUM();//默认值为0
//			isDanList.add(mb.isISD());
//			betcodes.add(checkNum);
//		}
//		int isDanNum = getIsDanNum(isDanList);
//		return PublicJcMethod.getAllAmt(betcodes, select,isDanList,isDanNum);
//	}
	/**
	 * 获取设胆个数
	 * @return
	 */
	public int getIsDanNum(List<Boolean> isDanList){
		int num = 0;
		for(int i=0;i<isDanList.size();i++){
			if(isDanList.get(i)){
				num++;
			}
		}
		return num;
	}
}
