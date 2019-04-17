package com.mitenotc.enums;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.TCTimerHelper;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment2;
import com.mitenotc.ui.BodyFragment3;
import com.mitenotc.ui.BodyFragment3_find;
import com.mitenotc.ui.BodyFragment4;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.account.BettingRecords;
import com.mitenotc.ui.account.BuyRedPacket;
import com.mitenotc.ui.account.ConsultationFragment;
import com.mitenotc.ui.account.MessageCenter;
import com.mitenotc.ui.account.PerfectBankInfo;
import com.mitenotc.ui.account.RechargeFragment;
import com.mitenotc.ui.account.SettingFragment;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.base.BottomFragment;
import com.mitenotc.ui.lotteryinfo.AwardInfoDetail;
import com.mitenotc.ui.lotteryinfo.JCAwardInfoDetail;
import com.mitenotc.ui.play.JCPL310;
import com.mitenotc.ui.play.JCPL320;
import com.mitenotc.ui.play.JCPL321;
import com.mitenotc.ui.play.PL100;
import com.mitenotc.ui.play.PL102;
import com.mitenotc.ui.play.PL103;
import com.mitenotc.ui.play.PL106;
import com.mitenotc.ui.play.PL112;
import com.mitenotc.ui.play.PL113;
import com.mitenotc.ui.play.PL116;
import com.mitenotc.ui.play.PL117;
import com.mitenotc.ui.play.PL118;
import com.mitenotc.ui.play.PL119;
import com.mitenotc.ui.play.PL120;
import com.mitenotc.ui.play.PL121;
import com.mitenotc.ui.play.PL122;
import com.mitenotc.ui.play.PL123;
import com.mitenotc.ui.play.PL130;
import com.mitenotc.ui.user.BettingDetail;
import com.mitenotc.ui.user.ChaseDetail;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;
/**
 * 主界面自定义中心的 界面跳转的 helper枚举
 * @author mitenotc
 */
public enum CustomTagEnum {
	
	//界面id,启动方式,启动的fragment,lotteryId,iconId.
	hall(1001,0,BodyFragment2.class,-1,R.drawable.logo_hall1,"购彩大厅","为您精心挑选了全国各地最好玩的游戏"),
	awardInfo(1002,2,BodyFragment3.class,-1,R.drawable.logo_awardinfo1,"开奖信息","为您提供最及时开奖信息服务"),
	user(1003,0,BodyFragment4.class,-1,R.drawable.logo_user1,"我","个人的私属领地"),
	find(1004,0,BodyFragment3_find.class,-1,R.drawable.logo_find1,"发现","活动中心"),
	//彩种  增加彩种必须增加相对应的开奖公告
	lottery_pl3(100,2,PL100.class,100,R.drawable.logo_pl31,"排列三","技术型游戏，一日一开奖，中奖高达1000元"),
	lottery_pl5(102,2,PL102.class,102,R.drawable.logo_pl51,"排列五","2元赢取10万元"),
	lottery_qxc(103,2,PL103.class,103,R.drawable.logo_qxc1,"七星彩","最易中，连续猜中2个号，最少可赚取5元钱"),
	lottery_dlt(106,2,PL106.class,106,R.drawable.logo_dlt1,"大乐透","2元赢取500万，3元追加800万"),
	lottery_11x5(112,2,PL112.class,112,R.drawable.logo_11x51,"11运夺金","10分钟一期，好玩易中奖"),
	lottery_qlc(117,2,PL117.class,117,R.drawable.logo_qlc1,"七乐彩","选取7个号，赚取500万"),
	lottery_ssq(118,2,PL118.class,118,R.drawable.logo_ssq1,"双色球","全国热销游戏，每周二、四、日开奖"),
	lottery_ssc(119,2,PL119.class,119,R.drawable.logo_ssc1,"时时彩","经典游戏，50%返金，10分钟一期"),
	lottery_k3(120,2,PL120.class,120,R.drawable.logo_k31,"福彩快三","骰子游戏，每日78期，十分钟一开"),
	lottery_moni_k3(122,2,PL122.class,122,R.drawable.logo_k31,"模拟快三","不花钱，积分教你买彩票"),
	lottery_moni_11x5(130,2,PL130.class,130,R.drawable.logo_11x51,"模拟11运夺金","不花钱，积分教你买彩票"),
	
	lottery_jx_dlc(113,2,PL113.class,113,R.drawable.logo_dlc1,"多乐彩","10分钟一期，好玩易中奖"),//-----TODO -朱万利--江西快三 ---多乐彩--
	lottery_jx_k3(123,2,PL123.class,123,R.drawable.logo_k31,"江苏快三","骰子游戏，每日78期，十分钟一开"),
	lottery_pk3(121,2,PL121.class,121,R.drawable.logo_pk31,"快乐扑克3","10分钟一期，好玩易中奖"),
	lottery_fc3d(116,2,PL116.class,116,R.drawable.logo_fc3d1,"福彩3D","选取3个号，轻轻松松最高可赚取1000元"),
	lottery_zc(310,2,JCPL310.class,310,R.drawable.logo_zc,"竞彩足彩","2串1 倍投收益高"),
	lottery_zcdg(320,2,JCPL320.class,320,R.drawable.logo_zqdg,"竞足单关","竞彩足球单关90%中奖率"),
	lottery_lq(321,2,JCPL321.class,321,R.drawable.logo_jclq,"竞彩篮球","2串1 倍投收益高"),
	
	//开奖详情
	award_detail_pl3(1100,2,AwardInfoDetail.class,-1,R.drawable.logo_pl31,"排列三","排列三 描述信息",100),
	award_detail_pl5(1102,2,AwardInfoDetail.class,-1,R.drawable.logo_pl51,"排列五","排列五 描述信息",102),
	award_detail_qxc(1103,2,AwardInfoDetail.class,-1,R.drawable.logo_qxc1,"七星彩","七星彩 描述信息",103),
	award_detail_dlt(1106,2,AwardInfoDetail.class,-1,R.drawable.logo_dlt1,"大乐透","大乐透 描述信息",106),
	award_detail_11x5(1112,2,AwardInfoDetail.class,-1,R.drawable.logo_11x51,"11运夺金","11运夺金 描述信息",112),
	award_detail_qlc(1117,2,AwardInfoDetail.class,-1,R.drawable.logo_qlc1,"七乐彩","七乐彩 描述信息",117),
	award_detail_ssq(1118,2,AwardInfoDetail.class,-1,R.drawable.logo_ssq1,"双色球","双色球 描述信息",118),
	award_detail_ssc(1119,2,AwardInfoDetail.class,-1,R.drawable.logo_ssc1,"时时彩","时时彩 描述信息",119),
	award_detail_jx_dlc(1113,2,AwardInfoDetail.class,-1,R.drawable.logo_dlc1,"多乐彩","多乐彩 描述信息",113),//-----TODO -朱万利--江西快三 ---多乐彩--
	award_detail_js_k3(1123,2,AwardInfoDetail.class,-1,R.drawable.logo_k31,"江苏快三","江苏快三 描述信息",123),
	award_detail_fc_k3(1120,2,AwardInfoDetail.class,-1,R.drawable.logo_k31,"福彩快三","福彩快三 描述信息",120),
	award_detail_monik3(1122,2,AwardInfoDetail.class,-1,R.drawable.logo_k31,"模拟快三","模拟快三 描述信息",122),
	award_detail_pk3(1121,2,AwardInfoDetail.class,-1,R.drawable.logo_pk31,"快乐扑克3","........",121),
	award_detail_fc3d(1116,2,AwardInfoDetail.class,-1,R.drawable.logo_fc3d1,"福彩3D","........",116),
	award_detail_zc(1210,2,JCAwardInfoDetail.class,-1,R.drawable.logo_zc,"竞彩足彩","........",210),//--TODO 测试
	award_detail_moni11x5(1130,2,AwardInfoDetail.class,-1,R.drawable.logo_11x51,"模拟11运夺金","........",130),
	
	//以下界面为其他跳转界面
//	awardInfodetail(1004,2,AwardInfoDetail.class,-1,R.drawable.logo_ssq),//开奖详情页,具体的彩种 用 bundle 传递
//	bet(1005,2,SsqBetorder.class,-1,R.drawable.logo_ssq),//投注界面.
	recharge(1006,2,RechargeFragment.class,-1,R.drawable.logo_recharge1,"账户充值","让自己的钱包鼓起来吧!"),//充值页面
//	bettingDetail(1007,2,BettingDetail.class,-1,R.drawable.logo_ssq1,"投注详情","....."),
	list_redPacket(1008,2,BuyRedPacket.class,-1,R.drawable.logo_cjk1,"红包商城","优惠多多,常来看看!"),
	consultation(1009,2,ConsultationFragment.class,-1,R.drawable.logo_consultation1,"在线咨询","亲，需要帮助，就来找我吧！"),//在线咨询
	message_center(1010,2,MessageCenter.class,-1,R.drawable.logo_message_center1,"消息中心","亲，需要帮助，就来找我吧！"),//消息中心
	setting(1011,2,SettingFragment.class,-1,R.drawable.logo_setting1,"系统设置","能让它更适合您的使用"),//系统设置
	betting_record(1012,2,BettingRecords.class,-1,R.drawable.logo_betting_record1,"投注记录","一键即可查询您的购彩信息"),//投注记录
	betting_detail(1013,2,BettingDetail.class,-1,R.drawable.logo_ssq1,"投注详情","投注记录 描述信息"),//投注记录
	chase_detail(1014,2,ChaseDetail.class,-1,R.drawable.logo_ssq1,"追号详情","投注记录 描述信息"),//追号记录
	perfectBankInfo(2000,2,PerfectBankInfo.class,-1,R.drawable.acc_logo2,"提现增速","完善开户行信息"),//完善银行卡信息
	
	tc_web(10000,2,TCWebFragment.class,-1,R.drawable.logo_ssq1);//用于打开 webview的界面
	
	int id;//用于区别每个界面的数据的id
	/**界面的打开方式 0代表是主界面的fragment,用bottomFragment的check方法.
	  1代表使用replace方法,替换当前activity的fragment. 
	  2代表使用start方法开启一个新的activity
	  3竞彩专用**/
	int startType;
	
	private Class targetFragment;
	//彩票的信息
	private int iconId;
	private int lotteryId;//玩法代码 与协议中的相对应. 如果不是彩票则用 -1表示
	private int awardDetailId;//开奖详情的 lotteryId;
	private String lotteryName;//玩法名称
	private String state;//状态
	private String issue;//期号
	private String endTime;//销售截止时间
	private String currServerTime;//服务器当前时间
	private long remainTime;//剩余秒数(距离停售还有多少秒)
	private String desc;//玩法提示语
	private int maxChasableIssueNum;//可追号期数
	private String maxChasableIssue;//可追期号(高频和小盘玩法才有)
	private int isAward;//是否加奖(0无加奖；1有加奖)
	
	private long messageConvertedTime;//数据转换时的时间
	private boolean is_requiring_network;
	private static TCTimerHelper timer;
	private static String issue_sample_text = MyApp.res.getString(R.string.item_hall_tv_issue_text);
	private static BaseFragment currFragment;
	public static String lottery_ids_server="";//用于记录彩种服务端的默认排序
	
	public static void update(BaseFragment fragment, Map<TextView, CustomTagEnum> timerMap,int mode) {//注意要使用该方法,必须使得 onMessageReceived方法有一个 case 1;
		if(timerMap == null) return;
		Set<TextView> keySet = timerMap.keySet();
		for (TextView textView : keySet) {
			CustomTagEnum item = timerMap.get(textView);
			if(item == null)
				continue;
			if(item.lotteryId == -1)//如果不是彩种就不请求//实际上不是彩种也要请求网络数据
				continue;
			long updateRemainedTime = item.updateRemainedTime();
			if(updateRemainedTime < 0){//如果已经到了停止开奖的时间,则从新获取该其次的信息
				if(item.isIs_requiring_network())//如果正在请求网络数据,则返回
					continue;
				//没有正在请求数据,就请求数据
				item.setIs_requiring_network(true);//设置正在请求数据为true
				
				//发送开始请求网络数据的广播
				if(currFragment != null && currFragment.getActivity()!=null){
					Intent intent = new Intent();
					intent.setAction("com.mitenotc.ui.play.on_lottery_"+item.lotteryId+"_start_loading");////System.out.println("--------------------------------------------------start_loading");
					currFragment.getActivity().sendBroadcast(intent);
				}
				 
				MessageJson messageJson = new MessageJson();
				messageJson.put("A", item.getLotteryId());
				messageJson.put("isforce", "1");
				fragment.submitData(1, 1200, messageJson);//注意这里的 key 为1
				if(mode == 0){
					textView.setText(StringUtils.replaceEach(issue_sample_text, new String[]{"ISSUE","TIME"}, new String[]{item.getIssue(),"正在获取数据"}));
				}else {
					textView.setText("--:--");
				}
				continue;
			}
			String lasttime2 = updateRemainedTime/1000+"";
			String lastTime = BaseFragment.getLastTime(lasttime2);//以毫秒为单位
			if(mode == 0){
				textView.setText(StringUtils.replaceEach(issue_sample_text, new String[]{"ISSUE","TIME"}, new String[]{item.getIssue(),lastTime}));
			}else {
				textView.setText(lastTime);
			}
		}
	}
	
	/**
	 * @param fragment
	 * @param timerMap
	 * @param mode 用于设置 textview 设置字符串的形式. 购彩大厅界面的形式为0, 选号界面的 为1
	 */
	public static void startTimer(final BaseFragment fragment, final Map<TextView, CustomTagEnum> timerMap,final int mode){
		timer = new TCTimerHelper(new TCTimerHelper.TimerObserver() {
			@Override
			public void onUpdate() {
				update(fragment, timerMap,mode);
//				////System.out.println("timer.size = "+timerMap.size());
			}
		});
		currFragment = fragment;
		timer.start();
	}
	
	public static void stopTimer(){////System.out.println("timer stopped ------------------------------------");
		if(timer!=null)
		timer.stop();
		currFragment = null;
	}
	
//	private int position;//用于排序
	
	CustomTagEnum(){};
	CustomTagEnum(int id,int startType,Class targetFragment,int lotteryId,int iconId,String name,String desc,int awardDetailId){
		this.id = id;
		this.startType = startType;
		this.targetFragment = targetFragment;
		
		this.lotteryId = lotteryId;
		this.iconId = iconId;
		this.lotteryName = name;
		this.desc = desc;
		
		this.awardDetailId = awardDetailId;
	};
	CustomTagEnum(int id,int startType,Class targetFragment,int lotteryId,int iconId,String name,String desc){
		this.id = id;
		this.startType = startType;
		this.targetFragment = targetFragment;
		
		this.lotteryId = lotteryId;
		this.iconId = iconId;
		this.lotteryName = name;
		this.desc = desc;
	};
	CustomTagEnum(int id,int startType,Class targetFragment,int lotteryId,int iconId){
		this.id = id;
		this.startType = startType;
		this.targetFragment = targetFragment;
		
		this.lotteryId = lotteryId;
		this.iconId = iconId;
	};
	
	public static boolean isAwardDetailItem(CustomTagEnum item){
		return item.awardDetailId != 0;
	}
	public static CustomTagEnum getItemById(int id){
		for (CustomTagEnum item : CustomTagEnum.values()) {
			if(item.id == id)
				return item;
		}
		return null;
	}
	/**
	 * 通过 lotteryId 找item
	 * @param id
	 * @return
	 */
	public static CustomTagEnum getItemByLotteryId(int lotteryId){
		for (CustomTagEnum item : CustomTagEnum.values()) {
//			////System.out.println("item.lotteryId = "+item.lotteryId);////System.out.println("lotteryId = "+lotteryId );
			if(item.lotteryId == lotteryId)
				return item;
		}
		return null;
	}
	/**
	 * 界面跳转专门用于 bodyfragment1 的界面跳转,根据当前的startType的不同使用不同的跳转方式
	 */
	public static void startActivity(BaseActivity mActivity,Bundle data,CustomTagEnum item){
		if(item == null)
			return;
		switch (item.startType) {
		case 0:// 第一个界面的底部导航上的页面的跳转
			BottomFragment bottomFragment = (BottomFragment) mActivity.getBottomFragment();//注意这里的 mActivity 只有是 TCActivity才能有效
			if(bottomFragment != null)
				bottomFragment.check(item.targetFragment.getName());
			break;
		case 1://不会启动新的activity
			mActivity.replaceMiddle(item.targetFragment);
			break;
		case 2://会启动新的activity
			if(isAwardDetailItem(item)){
				if(data == null)
					data = new Bundle();
				
				data.putString("lotteryId", item.awardDetailId+"");
			}
			if(item.lotteryId == -1){//非彩种的选号界面 的 item
				mActivity.start(ThirdActivity.class,item.targetFragment,data);
			}else {//彩种的选号界面 的 item
				mActivity.start(SecondActivity.class,item.targetFragment,data);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 把网络中获取的MessageBean转换到该 类的 事例中
	 * @param bean
	 */
	public static void convertMessage(MessageBean bean){
		if(bean == null || !AppUtil.isNumeric(bean.getA()))
			return;
		CustomTagEnum item = getItemByLotteryId(Integer.parseInt(bean.getA()));////System.out.println("bean = "+bean);////System.out.println("bean.getB() = "+bean.getB());
		if(item == null)
			return;
		
		item.messageConvertedTime = System.currentTimeMillis();//记录获得数据时的时间点
		item.setIs_requiring_network(false);//标记网络数据已经获取完成
		
		////System.out.println("currentfragment = "+currFragment);
//		if(currFragment != null)
		////System.out.println("currFragment.getActivity() = "+currFragment.getActivity());
		if(currFragment != null && currFragment.getActivity() != null){
			//发送 彩种获取完 网络数据的广播
			Intent intent = new Intent();
			intent.setAction("com.mitenotc.ui.play.on_lottery_"+item.lotteryId+"_stop_loading");
			currFragment.getActivity().sendBroadcast(intent );
			////System.out.println("sendBroadcast(intent ) = "+"com.mitenotc.ui.play.on_lottery_"+item.lotteryId+"_stop_loading");
			////System.out.println("sendBroadcast(intent ) = "+"com.mitenotc.ui.play.on_lottery_"+item.lotteryId+"_awardinfo_received");
		}
		
//		////System.out.println("itemname = "+item.getLotteryName());
//		////System.out.println("is_requiring_network = "+item.is_requiring_network);
		item.lotteryName = bean.getB();
//		System.out.println("210------->bean.getB() :"+bean.getB());
		item.state = bean.getC();
		item.issue = bean.getD();
		item.endTime = bean.getE();
		item.currServerTime = bean.getF();
		if(StringUtils.isNumeric(bean.getG()))
			item.remainTime = Long.parseLong(bean.getG());////System.out.println("bean.getG() = "+bean.getG());
		if(!AppUtil.isEmpty(bean.getH()))
			item.desc = bean.getH();
		if(StringUtils.isNumeric(bean.getI()))
			item.maxChasableIssueNum = Integer.parseInt(bean.getI());
		item.maxChasableIssue = bean.getJ();
		if(StringUtils.isNumeric(bean.getK()))
			item.isAward = Integer.parseInt(bean.getK());
		
		if(item.lotteryId != 122){
			lottery_ids_server += ","+item.lotteryId;
		}
	}
	/**
	 * 主要是 我的定制中的 开奖信息
	 * @param bean
	 */
	public static void convertCustomTag_awardInfo(MessageBean bean){
		if(bean != null || AppUtil.isNumeric(bean.getA())){
			CustomTagEnum item = CustomTagEnum.getItemById(Integer.parseInt(1+bean.getA()));
			if(item != null){
				item.setDesc("第"+bean.getC()+"期，开奖号码:"+bean.getE());
				item.setIssue(bean.getC());
			}
		}
	}
	/**
	 * 我的定制, 1302接口数据的转换
	 * @param bean
	 */
	public static void convertCustomTag(MessageBean bean){//主要与 1302接口相对应
		String tempid="";
		if(bean == null || !AppUtil.isNumeric(bean.getA())){
			return;
		}
		CustomTagEnum item = getItemById(Integer.parseInt(bean.getA()));
		if(item == null)
			return;
		if(bean.getA().equals("210")){//210  用于管理竞彩足球的所有玩法,竞彩足球特殊特殊处理
			item.lotteryName = "竞彩足球";
		}else{
			item.lotteryName = bean.getB();
		}
		if(!AppUtil.isEmpty(bean.getC()))
			item.desc = bean.getC();
		if("".equals(tempid)){
			tempid+=bean.getA();
		}else{
			tempid+=","+bean.getA();
		}
//		if(item == user){
//			if(UserBean.getInstance().isLogin()){
//				user.desc = "您的可用余额为:<font color='#ff0000'> ¥"+FormatUtil.moneyFormat2String(UserBean.getInstance().getAvailableBalance()/100)+"</font>";
//			}else {
//				user.desc = "您还没有登录";
//			}
//		}
	}
 
	/**
	 * 通过id 来跳转到相应的界面
	 */
	public static void startActivityWithId(BaseActivity mActivity,Bundle data,int id){
		startActivity(mActivity, data, getItemById(id));
	}
	
	public long updateRemainedTime(){
		if(AppUtil.isEmpty(endTime) || AppUtil.isEmpty(currServerTime)){
			return -1;
		}
		long endTimeLong = FormatUtil.timeFormat(endTime);
//		long currServerTimeLong =  FormatUtil.timeFormat(currServerTime);
//		////System.out.println("System.currentTimeMillis()-messageConvertedTime = "+(System.currentTimeMillis()-messageConvertedTime));
//		////System.out.println("remainTime "+this.lotteryName+" = " + this.remainTime);
		return endTimeLong-System.currentTimeMillis();
//		return remainTime;
	}
	/**
	 * 通过messageBean 来确定相应的 Enum,调用该对象的 updateRemainedTime方法来重新计算剩余的时间
	 * @param bean
	 * @return
	 */
	public static long updateRemainedTime(MessageBean bean){
		CustomTagEnum item = getItemByLotteryId(Integer.parseInt(bean.getA()));
		return item.updateRemainedTime();
	}
	public int getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(int lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStartType() {
		return startType;
	}
	public void setStartType(int startType) {
		this.startType = startType;
	}
	public Class getTargetFragment() {
		return targetFragment;
	}
	public void setTargetFragment(Class targetFragment) {
		this.targetFragment = targetFragment;
	}
	public int getIconId() {
		return iconId;
	}
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	public String getLotteryName() {
		return lotteryName;
	}
	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCurrServerTime() {
		return currServerTime;
	}
	public void setCurrServerTime(String currServerTime) {
		this.currServerTime = currServerTime;
	}
	public long getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getMaxChasableIssueNum() {
		return maxChasableIssueNum;
	}
	public void setMaxChasableIssueNum(int maxChasableIssueNum) {
		this.maxChasableIssueNum = maxChasableIssueNum;
	}
	public String getMaxChasableIssue() {
		return maxChasableIssue;
	}
	public void setMaxChasableIssue(String maxChasableIssue) {
		this.maxChasableIssue = maxChasableIssue;
	}
	public int getIsAward() {
		return isAward;
	}
	public void setIsAward(int isAward) {
		this.isAward = isAward;
	}
	public boolean isIs_requiring_network() {
		return is_requiring_network;
	}
	public void setIs_requiring_network(boolean is_requiring_network) {
		this.is_requiring_network = is_requiring_network;
	}

	
//	public int getPosition() {
//		return position;
//	}
//	public void setPosition(int position) {
//		this.position = position;
//	}
}
