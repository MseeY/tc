package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;


import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.BaseCalc;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.Getlotterykind;

/**
 * 每一张ticket的数据
 * @author mitenotc
 *当彩种类似于双色球之类的 一个界面仅有一只玩法是 该类为一张ticket的JavaBean
 *当彩种类似于 快乐扑克三 之类的 一个界面多个玩法时 该类代表的是该界面中的选号情况，childId，salesType，lotteryCount 不适用与一张票的数据
 */
public class TicketBean {

	protected String lotteryId;
	//	子玩法代码
	protected String childId = "0";//默认为0
	//	销售方式
	protected String salesType = "1";//默认为复式
	//	投注号码
	protected List<List<Integer>> lotteryNums = new ArrayList<List<Integer>>();
	//	倍数
	protected int fold = 1;
	//	注数
	protected long lotteryCount = 0;
	//	金额
	protected long ticketAmount;
	
	protected int price = 200;//每注 的单价
	
	protected  boolean isTicketAvailable; //是否满足一张ticket
	
	private K3Bean mBean=null;
	
	private int radioButtonId;//子玩法 对应的radiobutton的 id;//该值暂时放在这儿
	
	protected  boolean is_blond_integral=false;//显示积分还是元  默认显示 元
	protected String sampleNotice_jf = MyApp.res.getString(R.string.basebuy_notice_jf_text);
	protected String sampleNotice = MyApp.res.getString(R.string.basebuy_notice_text);

	protected String notice;//用于选号区显示"共 1 注 2 元"
	
	protected static String formatStr = "";//用于网络提交的数据的格式化
	
	protected static String showStr = "";//用于界面显示的格式化
	
	protected final static String NaN = "NaN";//不是一个数字,用于只有一注的情况
	/** 竞彩 过关方式  **/
	protected String tBunch ="";
	protected int  session =0;
	/** 竞彩  设置胆拖**/
	public TicketBean() {
		super();
		if(MyApp.order != null && MyApp.order.isIs_dlt_pursue_mode_enabled()){//对大乐透追加模式的判断
			price = 300;
			childId = "1";
		}
	}
	/**
	 * 竞彩添加具体的选票数据
	 * @param m
	 */
	protected   List<MessageBean> selectMB=new LinkedList<MessageBean>();//竞彩的选票信息:
//	protected   List<MessageBean> selectMB=new ArrayList<MessageBean>();//竞彩的选票信息:
	protected   List<Integer> passTypeList=new LinkedList<Integer>();//串关方式
	protected   Map<String,List<String>> lotteryTag=new HashMap<String, List<String>>();// 所选场次选票数据选票信息:(胜平负_SP值)
	public void setSelectMessageBean(List<MessageBean> selectMessageBean){
		this.selectMB=selectMessageBean;
	}
	public List<MessageBean> getSelectMessageBean() {
		return selectMB;
	}
	public void setPassTypeList(List<Integer> list) {
		if(list== null)return;
		this.passTypeList=list;
	}
	public List<Integer> getPassTypeList() {
		return passTypeList;
	}
	public Map<String, List<String>> getLotteryTag() {
		return lotteryTag;
	}
	public void setLotteryTag(Map<String, List<String>> lotteryTag) {
		this.lotteryTag = lotteryTag;
	}
	/**
	 * 竞彩4001 下单G:LIST : A 	过关方式 * 串关格式说明
	 * @return
	 */
	public String getJC4001G_LISTA(){
		return "";
	}
	
	
	protected static int[] sizes = new int[7];
	private  void resetSizes(){
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = 0;
		}
	}
	/**
	 * 重新加载 容器的 size 值
	 */
	protected void reloadSizes(){
		resetSizes();
		for (int i = 0; i < lotteryNums.size(); i++) {
			sizes[i] = lotteryNums.get(i).size();
		}
	}
	//******************************主方法区************************
	/**
	 * 把该TicketBean 封装成 json 用于网络数据的封装
	 * @return
	 */
	public MessageJson getTicketJson(){
		MessageJson msg = new MessageJson();
		msg.put("A", childId);
		msg.put("B", salesType);
		msg.put("C", formatLotteryNums());
		msg.put("D", fold+"");
		msg.put("E", lotteryCount+"");
		msg.put("F", ticketAmount+"");
		return msg;
	}
	public JSONArray getArrayTicket(){
		return null;
	}
	/**
	 * 把该K3特殊票的拆封调用 封装成 json 用于网络数据的封装
	 * @return
	 */
	public static MessageJson getTicketJsonk3(String childId,String salesType,String formatStr,String lotteryCount,String ticketAmount,String fold){
		MessageJson msg = new MessageJson();
		msg.put("A", childId);
		msg.put("B", salesType);
		msg.put("C", formatStr);
		msg.put("D", fold);
		msg.put("E", lotteryCount);
		msg.put("F", ticketAmount);
		return msg;
	}
	
	/** 一个页面多张票的情况复写该方法
	 * 考虑到一个页面多张票的情况
	 * @return
	 */
	public List<MessageJson> getTicketJsonArray(){
		return null;
	}
	/**
	 * 把号码格式到 网络协议要求的格式
	 * @return
	 */
	public String formatLotteryNums() {
		calcLotteryCount();//格式前 先对注释进行计算. 出于安全性考虑
		if(!isTicketAvailable){//安全性考虑,如果当前的 ticket 不是有效的ticket,则返回空//TODO 这里最好提醒用户,票不是有效的
			return null;
		}
		formatStr = "";
		if(StringUtils.isNumeric(lotteryId)){
			switch (Integer.parseInt(lotteryId)) {
			case 100:
				formatLotteryNums_pl3();
				break;
			case 102:
				formatLotteryNums_qxc();
				break;
			case 103:
				formatLotteryNums_qxc();
				break;
			case 106:
				formatLotteryNums_dlt();
				break;
			case 116:
				formatLotteryNums_fc3d();
				break;
			case 112:
				formatLotteryNums_11x5();
				break;
			case 113: // 江西 十一选五 4-22
				formatLotteryNums_11x5();
				break;
			case 117:
				formatLotteryNums_qlc();
				break;
			case 118:
				formatLotteryNums_ssq();
				break;
			case 119:
				formatLotteryNums_ssc();
				break;
			case 120:
				////System.out.println("120formatStr=="+formatStr);
				formatLotteryNums_k3();
				break;
			case 123://江西 快三 --4-22
				formatLotteryNums_k3();
				break;
			case 122:
				formatLotteryNums_k3();
				break;
			case 130://模拟 十一运
				formatLotteryNums_11x5();
				break;
			case 121://不需要 调用formatLotteryNums（）；
				break;
			default:
				break;
			}
		}
		return formatStr;
	}


	
	/**
	 * 界面显示的字符串的格式化. 主要用在 倍投界面的显示
	 * @return
	 */
	public String showLotteryNums() {
		showStr = "";
		if(StringUtils.isNumeric(lotteryId)){
			switch (Integer.parseInt(lotteryId)) {
			case 100:
				showLotteryNums_pl3();
				break;
			case 102:
				showLotteryNums_qxc();
				break;
			case 103:
				showLotteryNums_qxc();
				break;
			case 106:
				showLotteryNums_dlt();
				break;
			case 116:
				showLotteryNums_fc3d();
				break;
			case 112:
				showLotteryNums_11x5();
				break;
			case 113://江西 十一选五 4-22
				showLotteryNums_11x5();
				break;
			case 117:
				showLotteryNums_qlc();
				break;
			case 118:
				showLotteryNums_ssq();
				break;
			case 119:
				showLotteryNums_ssc();
				break;
			case 120:
				showLotteryNums_k3();
				break;
			case 123:  //江西快三 4-22
				showLotteryNums_k3();
				break;
			case 122:  //模拟福彩快三
				is_blond_integral=true;
				showLotteryNums_k3();
				break;
			case 130://模拟 十一运夺金
				is_blond_integral=true;
				showLotteryNums_11x5();
				break;
			case 121:
				showLotteryNums_klpk();
				break;
			default:
				break;
			}
		}
		return showStr;
	}
	/**
	 * TicketBean_116  实现
	 * @return
	 */
	protected String showLotteryNums_fc3d() {
		return showStr;
	}
	protected String formatLotteryNums_fc3d() {
		
		return formatStr;
	}
	protected long calcLotteryCount_fc3d() {
		return lotteryCount;
	}
	
	/**
	 * 
	 * TicketBean_121  实现
	 * @return
	 */
	public String showLotteryNums_klpk(){
		return showStr;
	};
	/**
	 * 只有竞彩涉及
	 * 	 * @return
	 */
	public LinkedList<Object[]>  createChildTicket(){
		return null;
	}
	/**
	 * 计算票中的号码共 有多少 注,主要用于选号界面的 notice的显示.
	 * @return
	 */
	public long calcLotteryCount(){////System.out.println("lotteryid = "+lotteryId);////System.out.println("childid = "+childId);////System.out.println("saletype = "+salesType);
//		清空
		lotteryCount = 0;////System.out.println("childId **************************= "+childId);////System.out.println("saletype = "+salesType);////System.out.println("lotterynums.size = "+lotteryNums.size());
		reloadSizes();//重新加载 sizes
		if(StringUtils.isNumeric(lotteryId)){
			switch (Integer.parseInt(lotteryId)) {
			case 100:
				calcLotteryCount_pl3();
				break;
			case 102:
				calcLotteryCount_pl5();
				break;
			case 103:
				calcLotteryCount_qxc();
				break;
			case 106:
				calcLotteryCount_dlt();
				break;
			case 116:
				calcLotteryCount_fc3d();
				break;
			case 112:
				calcLotteryCount_11x5();
				if(lotteryCount >=1)
				{
					Getlotterykind.getInstance().znzh_11x5_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
				}
				break;
			case 113://江西 十一选五 4-22
				calcLotteryCount_11x5();
				if(lotteryCount >=1){
					Getlotterykind.getInstance().znzh_11x5_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
				}
				break;
			case 117:
				calcLotteryCount_qlc();
				break;
			case 118:
				calcLotteryCount_ssq();
				break;
			case 119:
				calcLotteryCount_ssc();
				if(lotteryCount >=1){
				   Getlotterykind.getInstance().znzh_pl119_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
				}
				break;
			case 120:
				calcLotteryCount_k3();
				break;
				
			case 123://江西快三 4-22
				calcLotteryCount_k3();
				break;
			case 122:
				is_blond_integral=true;
				calcLotteryCount_k3();
				break;
			case 130://模拟十一运
				is_blond_integral=true;
				calcLotteryCount_11x5();
				if(lotteryCount >=1)
				{
					Getlotterykind.getInstance().znzh_11x5_tosaveBonus((int)lotteryCount, lotteryNums, salesType+"-"+childId);	
				}
				break;
			case 121:
				onCalc();
				break;
			}
		}
		
		ticketAmount = fold*lotteryCount*price;
		if(ticketAmount <= 0){//考虑到安全性,如果选择的注数过大会导致 ticketAmount的值大于 int 型 最大数值,导致 ticketAmount 为负数,因此这里从新判断,增加安全性
			isTicketAvailable = false;
		}else {
			isTicketAvailable = true;
		}

			
		return lotteryCount;
	}
	
	/**121
	 * 计算
	 */
	public void onCalc(){};

	/**
	 * 获取给用户的消息提示,选号界面 消息提示的格式化
	 * @return
	 */
	public String getNotice(){////System.out.println("lotteryCount ========== "+lotteryCount);
		calcLotteryCount();
		if(isTicketAvailable){
			notice = replaceNoticeText(lotteryCount, ticketAmount/100);
		}else {
			notice = replaceNoticeText(0, 0);
		}
	
		return notice;
	}
	
	public String getNotice1(){////System.out.println("lotteryCount ========== "+lotteryCount);
		calcLotteryCount();
		if(isTicketAvailable){
			notice = replaceNoticeText(lotteryCount, lotteryCount*200/100);
		}else {
			notice = replaceNoticeText(0, 0);
		}
	
		return notice;
	}
	
	/**
	 * 初始化对象
	 * @param mBean
	 * @return
	 */
	private K3Bean createKsBena() {
		if(mBean==null){
			mBean=new K3Bean();
		 }
		 mBean.setLotteryNums(lotteryNums);
		 mBean.setLotteryId(lotteryId);
		 mBean.setSalesType(salesType);
		 mBean.setChildId(childId);
		 mBean.setSizes(sizes);
		return mBean;
	}
	
	/**
	 * 把 票中原来存在 MGridView的 selectballs 中的数据拷贝一份, 重新赋值个 该TicketBean
	 * @return
	 */
	public TicketBean createTicket(){//把该票中的号码(原来存在 MGridView 对象 中的数据) 拷贝一份新的. 赋值给 该TicketBean
		List<List<Integer>> containersNums = new ArrayList<List<Integer>>();//所有 containers 的被选中的号
		List<Integer> containerNums = new ArrayList<Integer>();//一个container 的选择的号
		for (List<Integer> nums : lotteryNums) {
			containerNums = new ArrayList<Integer>();
			AppUtil.copyList(nums, containerNums);
			containersNums.add(containerNums);
		}
		lotteryNums = containersNums;
		return this;
	}
	
	//*********************************快三 方法区*************************************
	/**
	 * 快3 号码 格式化到 网络协议要求的格式 
	 * 
	 * K3Bean 实现
	 * @return
	 */
	protected String formatLotteryNums_k3() {
		            mBean=new K3Bean();
		return formatStr=mBean.formatLotteryNums_k3();
	}
	/**
	 *  快3 计算注数
	 *  
	 * 	K3Bean  去实现
	 * @param lotteryNums2 
	 * @return
	 */
	protected long calcLotteryCount_k3() {
            mBean = createKsBena();
       return  lotteryCount=mBean.calcLotteryCount_k3();
		
	}
	
	/**
	 * 快3   显示字符串的格式化
	 * @return
	 */
	protected String showLotteryNums_k3() {
		          mBean = createKsBena();
		return  showStr=mBean.showLotteryNums_k3();
	}
	
	//**********************************双色球 方法区*************************************
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

	
	//**********************************大乐透 方法区*************************************
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
		return showLotteryNums_ssq();
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
	//**********************************七乐彩 方法区*************************************
	/**
	 * 七乐彩 号码 格式化到 网络协议要求的格式
	 * @return
	 */
	private String formatLotteryNums_qlc() {
		formatStr = "";
		if(!isTicketAvailable)//如果票 没有满足 基本的玩法要求,则返回为空
			return null;
		if("0".equals(salesType) || "1".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),true);
		}else if("2".equals(salesType)){
			appendFormatNums(lotteryNums.get(0),"*",lotteryNums.get(1),true);
		}
		return formatStr;
	}
	/**
	 * 七乐彩 的显示字符串的格式化
	 * @return
	 */
	private String showLotteryNums_qlc() {
		showStr = "";
		if("0".equals(salesType) || "1".equals(salesType)){
			appendShowNums(lotteryNums.get(0),true);
		}else if("2".equals(salesType)){
			appendShowNums_dantuo();
		}
		return showStr;
	}
	/**
	 * 计算七乐彩注数
	 * @return
	 */
	public long calcLotteryCount_qlc(){
		price = 200;
		calcLotteryNum_common_and_dantuo(7, 2);
		return lotteryCount;
	} 
	//**********************************排列5 和 七星彩 方法区*************************************
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
	 * 计算七星彩注数
	 * @return
	 */
	public long calcLotteryCount_qxc(){
		price = 200;
		calcLotteryNum_tool_common(1,1,1,1,1,1,1);
		return lotteryCount;
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
	
	//**********************************11选5 方法区*************************************
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
				if("3".equals(salesType)){//前2直选 定位复式,不是单式
					delimiter = "*";
				}
				appendFormatNums(lotteryNums.get(0),delimiter,lotteryNums.get(1),true);
			}else if(childType == 10){//前三直选
				String delimiter = "";
				if("3".equals(salesType)){//前2直选 定位复式,不是单式
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
//				calcLotteryNum_tool_common("0","3",1,1);
//				lotteryCount -= getDuplicateNumCount(11);//减去重号重复的注数
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
//				calcLotteryNum_tool_common("0","3",1,1,1);
//				lotteryCount -= getDuplicateNumCount(11);//减去重号重复的注数
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
		/**
		 * 获取所有的容器中 任意2个容器中 有 相同的号码的个数. 专门用于前二直选和前三直选
		 * 例如: 容器A: 2 3 4
		 * 		 容器B: 2 5 6
		 * 则 A中的2 和B 中的2 不能构成一注
		 * 这样的组合的注数的计算结果的获取为该方法的意图
		 * @param sampleNumCount 号码的个数 十一选五为11个
		 * @return  这样组合的注数
		 *//*
		public int getDuplicateNumCount(int sampleNumCount){
			if(lotteryNums.size() < 2){//至少要2个容器				
				return 0;
			}
			int duplicateNumCount = 0;//重复号码的个数
			int duplicateTime = 0;//某一个号码重复的次数
			for (int i = 0; i <= sampleNumCount; i++) {
				duplicateTime = 0;
				for (List<Integer> lotteryNum : lotteryNums) {
					if(lotteryNum.contains(i)){
						duplicateTime ++;
					}
				}
				if(duplicateTime > 1){
					duplicateNumCount ++;
				}
			}
			
			return duplicateNumCount;
		}*/
		
		/**
		 * 前二直选注数计算
		 */
		public void calcChildType_9(){
			for (int i : lotteryNums.get(0)) {
				for (int j : lotteryNums.get(1)) {
					if(i != j){
						lotteryCount++;
					}
				}
			}
		}
		
		/**
		 * 前三直选注数计算
		 */
		public void calcChildType_10(){
			for (int i : lotteryNums.get(0)) {
				for (int j : lotteryNums.get(1)) {
					if(i != j){
						for (int m : lotteryNums.get(2)) {
							if(i != j && j != m && i != m){
								lotteryCount++;
							}
						}
					}
				}
			}
		}

		//**********************************江西时时彩 方法区*************************************
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
//					---------------------------------------万利--------------------------------------
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
//						---------------------------------万利--------------------------------
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
//						calcLotteryNum_tool_common("0", NaN, 1,1);
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
//						calcLotteryNum_tool_common("0", NaN, 1,1);
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
				
	//**********************************排列三 方法区*************************************
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
							calcLotteryNum_tool_common("3","4",3);
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
	//*******************************工具方法区**********************************
	//***************************网络数据格式化 工具方法区***************************
	/**
	 * 对一个容器中的号码格式的拼接
	 * @param selectBalls
	 */
	protected void appendFormatNums(List<Integer> selectBalls,boolean isFormat) {
		if(selectBalls.size() == 0){//该选区的号码为空,用于任选一,任选二
			formatStr += "*";
			return;
		}
		for (Integer i : selectBalls) {
			String str = i+"";
			if(isFormat){
				str = FormatUtil.ballNumberFormat(i);
			}
			formatStr += str;
		}
	}
	
	/**
	 * 把一个容器中的数据拼接起来
	 * @param selectBalls
	 * @param isFormat
	 * @return
	 */
	public String appendOneContainer(List<Integer> selectBalls,boolean isFormat) {
		String formatStr = "";
		for (Integer i : selectBalls) {
			String str = i+"";
			if(isFormat){
				str = FormatUtil.ballNumberFormat(i);
			}
			formatStr += str;
		}
		
		return formatStr;
	}
	/**
	 * 把所有的container 中的号码进行格式
	 * @param selectBalls
	 * @param delimiter
	 * @param isFormat
	 */
	protected void appendFormatNums(List<List<Integer>> selectBalls,String delimiter,boolean isFormat) {
		formatStr = "";
		for (int i = 0; i < selectBalls.size(); i++) {
			if(i != 0){
				formatStr += delimiter;
			}
			appendFormatNums(selectBalls.get(i), isFormat);
		}
	}
	/**
	 * 把2个容器中的号码 用 分隔符 拼接起来
	 * @param selectBalls0
	 * @param delimiter
	 * @param selectBalls1
	 */
	protected void appendFormatNums(List<Integer> selectBalls0,String delimiter,List<Integer> selectBalls1,boolean isFormat){
		appendFormatNums(selectBalls0,isFormat);
		formatStr += delimiter;
		appendFormatNums(selectBalls1,isFormat);
	}
	/**
	 * 把3个容器中的号码 用 分隔符 拼接起来
	 * @param selectBalls0
	 * @param delimiter0
	 * @param selectBalls1
	 * @param delimiter1
	 * @param selectBalls2
	 */
	protected void appendFormatNums(List<Integer> selectBalls0,String delimiter0,List<Integer> selectBalls1,String delimiter1,List<Integer> selectBalls2,boolean isFormat){
		appendFormatNums(selectBalls0,isFormat);
		formatStr += delimiter0;
		appendFormatNums(selectBalls1,isFormat);
		formatStr += delimiter1;
		appendFormatNums(selectBalls2,isFormat);
	}
	//***************************界面数据显示格式化 工具方法区***************************
	/**
	 * 胆拖投注的显示字符串 拼接,默认需要格式化
	 * 默认胆码区是 0角标的container,拖码区是1角标的container
	 */
	protected void appendShowNums_dantuo() {
		showStr += "(";
		appendShowNums(lotteryNums.get(0),true);
		showStr += ")";
		appendShowNums(lotteryNums.get(1),true);
	}
	/**
	 * container 中的每个号都要加空格
	 * @param selectBalls
	 */
	protected void appendShowNums(List<Integer> selectBalls,boolean isFormat) {
		if(selectBalls.size() == 0){
			showStr += "-";
			return;
		}
		for (int i =0;i<selectBalls.size();i++) {
			String num = selectBalls.get(i)+"";
			if(isFormat){//是否要对数据格式化的判断
				num = FormatUtil.ballNumberFormat(num);
			}
			if(i == 0){
				showStr += num;
			}else {
				showStr +="&#160;&#160;" + num;
			}
		}
	}
	/**
	 * 把两个 container 中的数据拼接起来显示
	 * @param selectBalls0
	 * @param delimiter
	 * @param selectBalls1
	 */
	public void appendShowNums(List<Integer> selectBalls0,String delimiter,List<Integer> selectBalls1){
		appendShowNums(selectBalls0, true);
		showStr += delimiter;
		appendShowNums(selectBalls1, true);
	}
	/**
	 * 把三个 container 中的数据拼接起来显示
	 * @param selectBalls0
	 * @param delimiter0
	 * @param selectBalls1
	 * @param delimiter1
	 * @param selectBalls2
	 */
	public void appendShowNums(List<Integer> selectBalls0,String delimiter0,List<Integer> selectBalls1,String delimiter1,List<Integer> selectBalls2){
		appendShowNums(selectBalls0, true);
		showStr += delimiter0;
		appendShowNums(selectBalls1, true);
		showStr += delimiter1;
		appendShowNums(selectBalls2, true);
	}
	
	/**
	 * 用于类似于 七星彩和 排列5 的模式,把数字直接组合,不同container中的数据用 分隔符分隔
	 * container中的每个号都不加空格
	 * @param selectBalls
	 */
	private void appendShowNums_directly(List<Integer> selectBalls) {
		if(selectBalls.size() ==0){
			showStr += "-";
			return;
		}
		for (int i =0;i<selectBalls.size();i++) {
				showStr += selectBalls.get(i);
		}
	}
	/**
	 * 给containers 中的每个 selectBalls 不加空格,并且不同的container 用 delimiter 分隔
	 * @param selectBalls
	 */
	protected void appendShowNums_directly(List<List<Integer> >containers_selectBalls,String delimiter) {
		for (int i =0;i<containers_selectBalls.size();i++) {
			if(i != 0)
				showStr += delimiter;
			appendShowNums_directly(containers_selectBalls.get(i));
		}
	}
	//***************************计算注数 工具方法区***************************
	/**计算投注的注数
	 * 用于 类似 双色球的 红球和蓝球区都有,且只用红球去有胆拖的投注.
	 * 当前协议的 大乐透蓝球区没有胆拖,因此该方法同样适用大乐透
	 * @param miniNum_red 红球区最少个数
	 * @param miniNum_tuo_ma 拖码区最少个数
	 * @param miniNum_blue 蓝球区最少个数
	 */
	public void calcLotteryNum_common_and_dantuo(int miniNum_red,int miniNum_tuo_ma, int miniNum_blue){
		if("0".equals(salesType)  || "1".equals(salesType)){//双色球普通投注
			calcLotteryNum_tool_common(miniNum_red,miniNum_blue);//已经对计算普通投注的方式进行了抽取
		}else if("2".equals(salesType)){//双色球胆拖投注
			calcLotteryNum_tool_dantuo(miniNum_red, miniNum_tuo_ma, miniNum_blue);
		}
	}
	/**计算投注的注数
	 * 用于类似七乐彩 的投注. 只有红球区,没有蓝球区. 普通和胆拖玩法的计算的封装
	 * @param miniNum_red
	 * @param miniNum_tuo_ma
	 */
	public void calcLotteryNum_common_and_dantuo(int miniNum_red,int miniNum_tuo_ma){
		if("0".equals(salesType)  || "1".equals(salesType)){//双色球普通投注
			calcLotteryNum_tool_common(miniNum_red);//已经对计算普通投注的方式进行了抽取
		}else if("2".equals(salesType)){//双色球胆拖投注
			calcLotteryNum_tool_dantuo(miniNum_red, miniNum_tuo_ma);
		}
	}
	
	/**
	 * 默认胆码区至少选一个,该方法不包括蓝球区
	 * @param miniNum 满足一注时该区的个数
	 * @param miniNum_tuo_ma 拖码区最少个数
	 */
	public void calcLotteryNum_tool_dantuo(int miniNum, int miniNum_tuo_ma){
		if(sizes[0] < 1  || sizes[0] >miniNum-1 || sizes[1]<miniNum_tuo_ma || sizes[1]< miniNum+1-sizes[0]){//胆码至少1个最多5个,拖码最少2个,并且加上胆码要至少7个(因为是胆拖,至少2注),蓝球至少1个
			lotteryCount = 0;
			isTicketAvailable = false;
		}else {
			salesType = "2";
			lotteryCount = BaseCalc.calc(sizes[1], miniNum-sizes[0]);
			isTicketAvailable = true;
		}
	}
	/**
	 * 默认胆码去至少选一个,该方法包括蓝球区, 蓝球区不进行胆拖选择
	 * @param miniNum
	 * @param miniNum_tuo_ma
	 * @param miniNum_blue 满足一注彩票投注时,蓝球区最少个数, 
	 */
	public void calcLotteryNum_tool_dantuo(int miniNum, int miniNum_tuo_ma,int miniNum_blue){
		if(sizes[0] < 1  || sizes[0] >miniNum-1 || sizes[1]<miniNum_tuo_ma || sizes[1]< miniNum+1-sizes[0] || sizes[2]<miniNum_blue){
			lotteryCount = 0;
			isTicketAvailable = false;
		}else {
			salesType = "2";
			lotteryCount = BaseCalc.calc(sizes[1], miniNum-sizes[0])*BaseCalc.calc(sizes[2],miniNum_blue);
			isTicketAvailable = true;
		}
	}
	

	/**
	 * 普通模式,多个容器选择的情况,也可以是1个容器
	 * @param saleType0 单式的销售方式的数值
	 * @param saleTpye1 复式的销售方式的数值
	 * @param miniNums
	 */
	protected void calcLotteryNum_tool_common(String saleType0,String saleTpye1,int... miniNums){////System.out.println("mininums.length === "+miniNums.length+" : childid = "+childId);
		//个容器中的数量初始化
//		reloadSizes();//这行代码最好放在外边
		int flag = isLotteryAvalible(miniNums);
		if(flag == -1){//不足一注
			lotteryCount = 0;
			isTicketAvailable = false;
		}else if(flag == 0){//单式
			salesType = saleType0;
			lotteryCount = 1;
			isTicketAvailable = true;
		}else {//复式
			if(!NaN.equals(saleTpye1)){//not a number
				salesType = saleTpye1;
				lotteryCount = calcLotteryCount(miniNums);
				isTicketAvailable = true;
			}
		}
	}
	/**
	 * 普通模式,多个容器选择的情况,也可以是1个容器. 默认为单式为0,复式为1;
	 * @param miniNums
	 */
	protected  void calcLotteryNum_tool_common(int... miniNums){
		calcLotteryNum_tool_common("0","1",miniNums);
	}
	
	/**
	 * 检验当前的 注数是否满足一注
	 * miniNums的长度 应该和 lotteryNums 的 长度一致
	 * @param miniNums
	 * @return
	 */
	protected int isLotteryAvalible(int... miniNums){
		int flag = -1;// -1为不满足一注, 0,为 刚好一注,1为大于一注. 同时 0 和协议中的销售方式 单式一致,1和协议中的复式一致
		for (int i = 0; i < lotteryNums.size(); i++) {////System.out.println("lotteryNums.size() = "+lotteryNums.size()); ////System.out.println("mininums.length = "+miniNums.length); ////System.out.println("i = "+i);
			if(sizes[i] < miniNums[i]){//存在一个未选满的就返回 未选满
				return -1;
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
	private int calcLotteryCount(int... miniNums){
		int count = 1;
		for (int i = 0; i < lotteryNums.size(); i++) {
			count *= BaseCalc.calc(sizes[i], miniNums[i]);
		}
		return count;
	}

	//****************************消息提示的格式化 工具方法区****************************
	public String replaceNoticeText(long num, long money){
		if(is_blond_integral){
			return StringUtils.replaceEach(sampleNotice_jf, new String[]{"NUM","MONEY"}, new String[]{String.valueOf(num),String.valueOf(money)});
		}else{
			return StringUtils.replaceEach(sampleNotice, new String[]{"NUM","MONEY"}, new String[]{String.valueOf(num),String.valueOf(money)});
		}
	}
	
	/**
	 * 重置该票的数据
	 */
	public void reSet() {
		lotteryId = "118";//默认为双色球
		childId = "0";//默认为0// 大乐透的追加模式为1;其他情况更具文档具体设置
		salesType = "0";//单式为0,复试为1,胆拖为2
		lotteryNums = new ArrayList<List<Integer>>();
		fold =1;//默认倍数为1;
		lotteryCount = 1;
		price = 200;//以分为单位
		ticketAmount = fold*lotteryCount*price;
	}
	
	public boolean isTicketAvailable() {
		calcLotteryCount();
		return isTicketAvailable;
	}
	public int getSession() {
		calcLotteryCount();
		return session;
	}
	
	public int getSession1() {
		return session;
	}

	public void setTicketAvailable(boolean isTicketAvailable) {
		this.isTicketAvailable = isTicketAvailable;
	}

	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public List<List<Integer>> getLotteryNums() {
		return lotteryNums;
	}

	public void setLotteryNums(List<List<Integer>> lotteryNums) {
		this.lotteryNums = lotteryNums;
	}

	public long getTicketAmount() {
		calcLotteryCount();
		return ticketAmount;
	}

	public void setTicketAmount(int ticketAmount) {
		this.ticketAmount = ticketAmount;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	/**
	 * @return the sizes
	 */
	public int[] getSizes() {
		return sizes;
	}
	/**
	 * @param sizes the sizes to set
	 */
	public void setSizes(int[] sizes) {
		this.sizes = sizes;
	}
	public long getLotteryCount() {
		calcLotteryCount();
		return lotteryCount;
	}
	public int getFold() {
		return fold;
	}
	public void setFold(int fold) {
		this.fold = fold;
	}
	public void setIs_blond_integral(boolean is_blond_integral) {
		this.is_blond_integral = is_blond_integral;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRadioButtonId() {
		return radioButtonId;
	}
	public void setRadioButtonId(int radioButtonId) {
		this.radioButtonId = radioButtonId;
	}
	public String gettBunch() {
		return tBunch;
	}
	public void settBunch(String tBunch) {
		this.tBunch = tBunch;
	}
	public void setSession(int session) {
		this.session = session;
	}
	
	
}
