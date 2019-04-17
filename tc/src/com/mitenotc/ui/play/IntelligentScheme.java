package com.mitenotc.ui.play;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.RATEBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.FADialog;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.adapter.BetschemeAdapter;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.pay.PayIntegral;
import com.mitenotc.ui.pay.Paymain;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.RefreshYiLouReceiver;
import com.mitenotc.ui.ui_utils.WaitDialogs;
import com.mitenotc.utils.BaseCalc;
/**
 * 智能追号页面
 * 
 * @author wanli
 *
 */
public class IntelligentScheme extends BaseFragment implements OnClickListener, RefreshYiLou {
    private Button amendScheme_btn;//修改
    private TextView time_tv;//时间显示
    private TextView time_tv1;//距 00 期截止
//    中奖后是否停止追号(0不停止；1停止)
    private static String IS_STOP="1";//默认是停止
    private BaseBuyFragment fragment;//该彩种的选号界面的fragment//ck 添加
	private Class<? extends BaseFragment> lotteryClazz;//当前彩种的选号界面的fragment的
    
    private  TextView show_bet_result_tv; //notice 提示
    private Button payment_btn;//付款button
    private Button initialize_btn;//恢复button
    private  Spannable	notice;
    /**共追 *期 *元***/
    private  String ALL_QH="" ;
    private  String	ALL_MAX_MONENY="";
    
    private  TextView znzh_table_top_tv;//智能追号表格上部的提示
	private static ListView bet_scheme_lv;//智能追号listView 方案列表
	private BetschemeAdapter betschemeAdapter;
    /***期数对应 倍数 追期一一对应的投注倍数**/
	private  List<String> ALL_BS_LIST = new ArrayList<String>();
	
    /***期数     根据修改页决定的最大追期数 决定所有的销售期号**/
    private  List<String> ALL_QH_LIST  = new ArrayList<String>();
    /***方案的预期盈利参数***/
    private  Map<String, String> paramMap;
   /****算法所计算的   累计投入 等信息List key 值为期号***/  
    private  Map<String, List<String>> CALC_MSG_MAP;
    
    private  String qhStr="";//期号
    
	private  Map<TextView, CustomTagEnum> timerMap;
	private  RefreshYiLouReceiver mReceiver;
    private MessageJson paramsJson; //请求参数
	private MessageBean returnMB;//返回结果
	private long orderN=0;//订单 总金额
	private int orderQ=0;//订单总追期数
	private InitAsyncTask mTask;
	private FADialog mfaDialog;
	private Dialog mDialog;
	private  TCDialogs yloudialog;//提示框
    
	/**
	 * 服务器返回的当前销售期
	 * true : 一致
	 * false ：相反
	 * 
	 */
	private boolean getSellexpect(MessageBean msgb) {
		//	比对期号是否一致  140304030
		if(msgb.getLIST().get(0).getD().equals(MyApp.order.getIssue()))
		{
			return true;
		}else
		{
//			更新Order 里面的期号！
			MyApp.order.setIssue(msgb.getLIST().get(0).getD());
			return false;
		}
	}
	
    @Override
    public void onStart() {
    	super.onStart();
    	time_tv1.setText("距"+CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue()+"期截止:");
    	if(timerMap==null){
    		timerMap = new HashMap<TextView, CustomTagEnum>();
    	}else
    	{
    		timerMap.clear();
    	}
		if(StringUtils.isBlank(MyApp.order.getLotteryId())){
			return;
		}else{
			timerMap.put(time_tv, CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())));
		}
		
		
    }
    /** 
     * 注册广播 
     */  
   private void registerBroadcastReceiver()
   {  
		   mReceiver =new RefreshYiLouReceiver(MyApp.order.getLotteryId()); 
		   mReceiver.setmRefreshYiLou(this);
		   IntentFilter mFilter=new IntentFilter();
		   mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_start_loading");
		   mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_stop_loading");
		   mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_awardinfo_received");
		   mActivity.registerReceiver(mReceiver, mFilter);  
   } 
   
	@Override
	public void onDestroy() {
		super.onDestroy();
		CustomTagEnum.stopTimer();
		if(mReceiver!=null){
			mActivity.unregisterReceiver(mReceiver);
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		CustomTagEnum.stopTimer();
		mTask.cancel(true);
		
	}
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noopsyche_chase_mark);
		init();
		registerBroadcastReceiver();
		initBuyFragment();
	}
	/**
	 * 初始化当前彩种的选号界面的fragment//ck 添加
	 */
	  private void initBuyFragment() 
	  {
		  try {
				lotteryClazz = (Class<? extends BaseFragment>) Class.forName("com.mitenotc.ui.play.PL"+MyApp.order.getLotteryId());
				fragment = (BaseBuyFragment) getFragment(lotteryClazz);
			} catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				Toast.makeText(mActivity, "该玩法暂不存在", 0).show();
			}
		}
	private void init() {
		ALL_QH_LIST=new ArrayList<String>();
		ALL_BS_LIST=new ArrayList<String>();
		yloudialog=new TCDialogs(mActivity);
		
		bet_scheme_lv=(ListView) findViewById(R.id.bet_scheme_lv);
		payment_btn=(Button) findViewById(R.id.payment_btn);
		initialize_btn=(Button) findViewById(R.id.initialize_huifu_btn);
		show_bet_result_tv=(TextView) findViewById(R.id.show_bet_result_tv);
		znzh_table_top_tv=(TextView) findViewById(R.id.znzh_table_top_tv);
		time_tv=(TextView) findViewById(R.id.time_tv);
		time_tv1=(TextView) findViewById(R.id.time_tv1);
		amendScheme_btn=(Button) findViewById(R.id.amendScheme_btn);
		
		bet_scheme_lv.setVerticalScrollBarEnabled(false);//不显示滚动条
		bet_scheme_lv.setCacheColorHint(0);
		bet_scheme_lv.setDrawingCacheEnabled(false);
		
		betschemeAdapter=new BetschemeAdapter(mActivity,this);
//		设置监听
		if(StringUtils.isBlank(MyApp.order.getLotteryId())){
			return;
		}else{
			switch (Integer.parseInt(MyApp.order.getLotteryId())) {
			case 120:
				setTitleNav("福彩快三-智能追号",R.drawable.title_nav_back, 0);
				break;
			case 112:
				setTitleNav("11运夺金-智能追号",R.drawable.title_nav_back, 0);
				break;
			case 122:
				setTitleNav("模拟快三-智能追号",R.drawable.title_nav_back, 0);
				break;
			case 119:
				setTitleNav("时时彩-智能追号",R.drawable.title_nav_back, 0);
				break;
			case 113:
				setTitleNav("多乐彩-智能追号",R.drawable.title_nav_back, 0);
				break;
			case 123:
				setTitleNav("江苏快三-智能追号",R.drawable.title_nav_back, 0);
				break;
			case 130:
				setTitleNav("模拟11运夺金-智能追号",R.drawable.title_nav_back, 0);
				break;
			default:
				setTitleNav("智能追号",R.drawable.title_nav_back, 0);
				break;
			}
		}
		mTask=new InitAsyncTask();
		mTask.execute();
		
		setListen();	    
	}
	/**
	 *  * 对外提供改变提示的方法
	 * @param zhuiqishuStr
	 * @param zongjineStr  '15339293684'
	 */
	public  void setNoticeText(){
		ALL_QH=RATEBean.getInstance().getDEFAULT_XH();
		if("122".equals(MyApp.order.getLotteryId())||"130".equals(MyApp.order.getLotteryId())){//模拟快三
			if(StringUtils.isBlank(ALL_MAX_MONENY)){
				return;
			}else{
				notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+ALL_QH+"期</font><font color=#ff9500>"+ALL_MAX_MONENY+"积分</font>");
			}
  	   }else{
  		 notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+ALL_QH+"期</font><font color=#ff9500>"+ALL_MAX_MONENY+"元</font>");
  	   }
		
		show_bet_result_tv.setText(notice);//默认提示
		znzh_table_top_tv.setText("共"+RATEBean.getInstance().getDEFAULT_XH()
				+"期 全程最低盈利率"+RATEBean.getInstance().getONE_YLL()+"%");
	}
	/**
	 *  * 对外提供改变提示的方法
	 * @param zhuiqishuStr
	 * @param zongjineStr  '15339293684'
	 */
	public   void setNoticeText(String moneyStr){
		ALL_MAX_MONENY=moneyStr;
		ALL_QH=RATEBean.getInstance().getDEFAULT_XH();
		if("122".equals(MyApp.order.getLotteryId())||"130".equals(MyApp.order.getLotteryId())){//模拟快三
			if(StringUtils.isBlank(ALL_MAX_MONENY)){
				return;
			}else{
				if(!StringUtils.isBlank(ALL_MAX_MONENY)){
					int temp =Integer.parseInt(ALL_MAX_MONENY);
					notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+ALL_QH+"期</font><font color=#ff9500>"+temp+"积分</font>");
				}else{
					
					notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+ALL_QH+"期</font><font color=#ff9500>0积分</font>");
				}
			}
		}else{
			  notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+ALL_QH+"期</font><font color=#ff9500>"+ALL_MAX_MONENY+"元</font>");
		}
		
		show_bet_result_tv.setText(notice);//默认提示
		znzh_table_top_tv.setText("共"+RATEBean.getInstance().getDEFAULT_XH()
				+"期 全程最低盈利率"+RATEBean.getInstance().getONE_YLL()+"%");
	}
	
	private void setListen() {
		amendScheme_btn.setOnClickListener(this);//修改方案
		payment_btn.setOnClickListener(this);//付款
		initialize_btn.setOnClickListener(this);//恢复默认
		//手机返回键
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				onBack();//不提示
			}
        });
	  }

	/**
	  * 异步计算
	  * 
	  * @author admin
	  *
	  */
	 
	 class InitAsyncTask extends  AsyncTask<String, String, String >{
		 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog=new Dialog(mActivity,R.style.dialog_theme);
			mDialog.setContentView(R.layout.m_wait_dialog);
			mDialog.show();
			
		} 
		@Override
		protected String  doInBackground(String... params) {
			initializeList();
			if(CALC_MSG_MAP==null){
				return "0";
			}else{
				return "1";
			}
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("1".equals(result)){
				// 取出计算后的倍数
				resultListView();
				setNoticeText();
				bet_scheme_lv.setAdapter(betschemeAdapter);
				notifyDataSetChanged();
				onProgressUpdate("1");
			}else if("0".equals(result)){
				initializeList();
				resultListView();
				setNoticeText();
				notifyDataSetChanged();
				onProgressUpdate("1");
			}
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			mDialog.dismiss();				    
	     
		}
	}
	public void setupCalc_msg_map(Map<String, List<String>> obj)
	{
		
		CALC_MSG_MAP = obj;
		resultListView();
	}
    /**
     * 刷新ListView 数据
     * @param result
     */
	 
	private   void resultListView() {
			if (CALC_MSG_MAP==null){
				
				return;
			}
		    ALL_QH_LIST.clear();
    		ALL_BS_LIST.clear();
    		String[] tmplist = CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getMaxChasableIssue().split(",");
    		List<String>  valueStr=null;
    		for (int i = 0; i < CALC_MSG_MAP.size() ; i++) 
    		{    			
    			valueStr= CALC_MSG_MAP.get(String.valueOf(i));    			
    			ALL_BS_LIST.add(i, valueStr.get(3));
    			ALL_QH_LIST.add(i,tmplist[i]);//期号

    		     //	获取最大 的智能追号方案的 追期数  和 计算金额
    			if(CALC_MSG_MAP.size() == i+1)
    				ALL_MAX_MONENY =  valueStr.get(0);
    		  } 
    		
    		RATEBean.getInstance().setDEFAULT_XH(String.valueOf(ALL_QH_LIST.size()));
    		RATEBean.getInstance().setALL_QH_LIST(ALL_QH_LIST);
    		RATEBean.getInstance().setALL_BS_LIST(ALL_BS_LIST);
    		RATEBean.getInstance().setCALC_MAP(CALC_MSG_MAP);
    		
    		int tempn=Integer.parseInt(RATEBean.getInstance().getDEFAULT_XH());
    		if(tempn > CALC_MSG_MAP.size()){
    			String contextStr="方案只能成"+CALC_MSG_MAP.size()+"期！";
    			yloudialog.unableChasesDialog("提      示", contextStr,"确定",
						new MyClickedListener() {
					
					@Override
					public void onClick()
					{
						yloudialog.dismiss();
					}
				},false);
    		}
	}

	  /**
	   * 初始化只能追号数据
	   * 
	   *  所有期号
	 * @return 
	   * @return 
	   * @return
	   */
	 public    void initializeList()
	 {
		 String   LotteryIdStr=MyApp.order.getLotteryId();		 
		if(StringUtils.isBlank(LotteryIdStr))
		{
			 return;
		}
			 RATEBean.getInstance().setLottery(LotteryIdStr);
			 RATEBean.getInstance().setBS(String.valueOf(MyApp.order.getFold()));
			 RATEBean.getInstance().setNOW_QH(CustomTagEnum.getItemByLotteryId(Integer.parseInt(LotteryIdStr)).getIssue());			 
			 RATEBean.getInstance().setMAX_XH(String.valueOf(CustomTagEnum.getItemByLotteryId(Integer.parseInt(LotteryIdStr)).getMaxChasableIssueNum())); 
			 RATEBean.getInstance().setLotteryCount((int)MyApp.order.getTickets().get(0).getLotteryCount());//累积投入倍数 （首次投入倍数）
			 getInitializeMapmsg() ;//获得计算结果
	  }
	 
	/**
	 *  计算结果
	 * @param rateBean
	 * @return Map<String, List<String>>  数据容器
	 */
	public void getInitializeMapmsg(){
			if(CALC_MSG_MAP!=null){
				CALC_MSG_MAP.clear();//清空处理
			}
		    switch (RATEBean.getInstance().getFANG_AN()) {
//			   1  只有全程最低盈利率 
//		    paramMap只存储一个参数 ： 固定key  1： 全程最低盈利率 30（默认）
						case 1:
							paramMap=new HashMap<String, String>();
							paramMap.put("1", RATEBean.getInstance().getONE_YLL());
//                                   @param beishu ==注数
							CALC_MSG_MAP=BaseCalc.jsqxx(RATEBean.getInstance().getBS(), 
									RATEBean.getInstance().getLotteryCount()+"",
									RATEBean.getInstance().getBonusScopeList(),
									paramMap, 
									RATEBean.getInstance().getDEFAULT_XH(), 
									"0");
							
							break;
//			   2 前 n 期 最低为 m %盈利率  
//			   paramMap只存储三个参数 ： 全程对低盈利率  固定key  2： 前n  key 3 ： 前n期盈利率  key 4 ： 前n期之后盈利率
						case 2:
							paramMap=new HashMap<String, String>();
							paramMap.put("2", RATEBean.getInstance().getTWO_QH());
							paramMap.put("3", RATEBean.getInstance().getTWO_QYLL());
							paramMap.put("4", RATEBean.getInstance().getTWO_HYLL());
							RATEBean.getInstance().setTWO_FAN_MAP(paramMap);
							
							CALC_MSG_MAP=BaseCalc.jsqxx(RATEBean.getInstance().getBS(), 
									RATEBean.getInstance().getLotteryCount()+"",
									RATEBean.getInstance().getBonusScopeList(),
									paramMap, 
									RATEBean.getInstance().getDEFAULT_XH(), 
									"0");
							
							break;
//		      3 全程最低盈利  y  元
//			 paramMap只存储一个参数 ： 固定key  1： 全程最低盈利 30元（默认）
						case 3:
							paramMap=new HashMap<String, String>();
							paramMap.put("5", RATEBean.getInstance().getTHREE_RMB());
							CALC_MSG_MAP=BaseCalc.jsqxx(RATEBean.getInstance().getBS(), 
									RATEBean.getInstance().getLotteryCount()+"",
									RATEBean.getInstance().getBonusScopeList(),
									paramMap, 
									RATEBean.getInstance().getDEFAULT_XH(), 
									"0");
							
							break;
			
						default:
							break;
					}
     }


	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
//		修改   跳转到修改追期方案
		case R.id.amendScheme_btn:
			showFADialog();
//			MyApp.saveProbeMsg("修改方案", "智能追号方案-修改-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			break;
//		付款
		case R.id.payment_btn:
			
			aptitudegotopay();
			
			break;
//		恢复默认
		case R.id.initialize_huifu_btn:
			recoverDefaultSet();//恢复初始化
			getInitializeMapmsg() ;//获得计算结果
			resultListView();	
			notifyDataSetChanged();
			setNoticeText();
			MyToast.showToast(mActivity, "恢复默认设置！");
//			MyApp.saveProbeMsg("恢复默认方案", "智能追号方案-恢复默认-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			break;
			
           default:
			  break;
		}
		
	}
	/**
	 * 弹出 修改Dialog
	 */
	private void showFADialog() {
//		弹出自定义Dialog布局
    	   mfaDialog=new FADialog(mActivity,this);
		   mfaDialog.addListener(
//		生成方案
		   new OnClickListener() {
			@Override
			public void onClick(View v) {
				          mfaDialog.getCreate_scheme_btn().setEnabled(false);//防止重复点击
						   if(mfaDialog.toCreateScheme())
							{
							   getInitializeMapmsg(); //获得计算结果  
							   resultListView();
							   notifyDataSetChanged();
							   //  默认是方案一 调用的是和初始化的算法一直
							   int fang_AN = RATEBean.getInstance().getFANG_AN();
							   changeToptv(fang_AN,mfaDialog);
							}else{
								recoverDefaultSet();//回复默认
							    resultListView();
							    notifyDataSetChanged();
//								MyToast.showToastLong(mActivity, "生成方案失败，恢复默认设置！");							   
						   	}
						   
							mfaDialog.getCreate_scheme_btn().setEnabled(true);
//							MyApp.saveProbeMsg("修改方案", "生成方案-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
							mfaDialog.dismiss();
					}
//			取消
           }, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mfaDialog.getAbolish_btn().setEnabled(false);
				MyToast.showToastLong(mActivity, "取消修改！");
//				MyApp.saveProbeMsg("修改方案", "取消修改方案-按钮", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
				mfaDialog.getAbolish_btn().setEnabled(false);
				RATEBean.getInstance().setFANG_AN(1);//取消方案
				mfaDialog.dismiss();
			}
         });
		
	}
	/**
	 * 智能方案页付款
	 */
	
	private void aptitudegotopay() {
//		if(NetUtil.checkNetWork(mActivity)){// 网络检测由于android 最新版本 不支持此权限 所以取消掉
		
			//if(!"".equals(qhStr) && ALL_QH_LIST!=null){
//				payment_btn.setEnabled(false);//防止重复点击
				 
				if(!UserBean.getInstance().isLogin())
				{
					startLoginForResult();
					return;
				}
				if(MyApp.order.getTickets().size()==0)
				{
					MyToast.showToast(mActivity, "您还没有订单！");
					return;
				}
		    		
				if(ALL_QH_LIST.size()<=0)
				{
					MyToast.showToast(mActivity, "没有可追的期信息，请稍后重试！");
					return;					
				}
				try {
					JSONObject jo=null;
					JSONArray ja=new JSONArray();
					for (int i = 0; i < ALL_QH_LIST.size(); i++) 
					{  
						if(0==i){
							MyApp.order.setIssue(ALL_QH_LIST.get(0));
						}
						jo=new JSONObject();
						jo.put("A", ALL_QH_LIST.get(i));
						jo.put("B", RATEBean.getInstance().getALL_BS_LIST().get(i));
						ja.put(jo);
					}
					
                   if(ALL_MAX_MONENY != null && !"".equals(ALL_MAX_MONENY))
					 {
						orderN = Long.parseLong(ALL_MAX_MONENY)*100;
					 }
					MyApp.order.setAmount(orderN);
					MyApp.order.setChaseNum(ALL_QH_LIST.size());
					MyApp.order.setIssue(CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue());	
					
					MyApp.order.setIsStop(IS_STOP);//中奖后是否停止追期
					submitData(1205, 1205, MyApp.order.getOrderJson(orderN,ja));//先请求服务器当前销售 期数
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
	}
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		returnMB=(MessageBean) msg.obj;
		switch (msg.arg1) {
		case 1:
			if(returnMB.getLIST() != null && returnMB.getLIST().size()>0)
			{ 
				String issue2 ="--";
			   if(StringUtils.isNumeric(MyApp.order.getLotteryId())){
				   issue2= CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue();
			    }
				time_tv1.setText("距"+issue2+"期截止:");
				CustomTagEnum.convertMessage(returnMB.getLIST().get(0));
			}
			break;
		case 1200:
/*			if(0==Integer.parseInt(returnMB.getA()))
			{
//				payment_btn.setEnabled(true);//防止重复点击
				//获得销售期 并比对
				if(getSellexpect(returnMB)){
					try {
//						[1, 1, 1, 2, 2, 3, 4, 5, 7, 10]
						ALL_BS_LIST=RATEBean.getInstance().getALL_BS_LIST();
						ALL_QH_LIST=new ArrayList<String>();
						
						int firstqh=Integer.parseInt(qhStr);
						int sineN=Integer.parseInt(RATEBean.getInstance().getDEFAULT_XH());
						for (int i = 0; i < sineN; i++) 
						{
							int temp=firstqh + i;
							ALL_QH_LIST.add(temp+"");
						}
						
						JSONObject jo=null;
						JSONArray ja=new JSONArray();
						for (int i = 0; i < sineN; i++) 
						{
							jo=new JSONObject();
							jo.put("A", ALL_QH_LIST.get(i));
							jo.put("B", ALL_BS_LIST.get(i));
							ja.put(jo);
						}
						
                       if(ALL_MAX_MONENY != null && !"".equals(ALL_MAX_MONENY))
						 {
							orderN = Long.parseLong(ALL_MAX_MONENY)*100;
						 }
                        orderQ =ALL_QH_LIST.size();
                        RATEBean.getInstance().setDEFAULT_XH(orderQ+"");
						MyApp.order.setAmount(orderN);
						MyApp.order.setChaseNum(orderQ);
						MyApp.order.setIssue(firstqh+"");	
						
						////System.out.println("orderN :"+orderN);
						////System.out.println("orderQ :"+orderQ);
						////System.out.println("firstqh :"+firstqh);
						////System.out.println("ja :"+ja.toString());
						MyApp.order.setIsStop("1");
						submitData(1205, 1205, MyApp.order.getOrderJson(orderN,ja));//先请求服务器当前销售 期数
						
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
//					payment_btn.setEnabled(true);//防止重复点击
					MyToast.showToast(mActivity, "当前销售期已错过,方案追期已更新到下一期！");
				}
			}else {
				MyToast.showToast(mActivity, returnMB.getB());
//				payment_btn.setEnabled(true);
			}*/
			break ;
		case 1205:
			if(0==Integer.parseInt(returnMB.getA()))
			{
				UserBean.getInstance().setAvailableMoney(Long.parseLong(returnMB.getD()));
				UserBean.getInstance().setAvailableCash(Long.parseLong(returnMB.getE()));
				UserBean.getInstance().setAvailableBalance(Long.parseLong(returnMB.getF()));
				AccountEnum.convertMessage(returnMB.getLIST());
				
				MyApp.order.setOrderId(returnMB.getC());//流水号
				Bundle payBundle = new Bundle();
				payBundle.putString("lotteryId", MyApp.order.getLotteryId());
				payBundle.putString("orderId", returnMB.getC());//1107接口需要的 订单号
				payBundle.putString("money", orderN+"");//1107 接口需要的第三方支付金额,这里其实是支付的总金额
				payBundle.putString("availableAmount", returnMB.getD());//用于确定是否进行红包支付
				payBundle.putString("lotteryId", MyApp.order.getLotteryId());//支付中需要的 彩票的彩种和期数
				payBundle.putString("issue", MyApp.order.getIssue());//支付中需要的期数
				payBundle.putInt(MyApp.res.getString(R.string.cmd), 1107);//支付中需要的期数
				
				
//				if("122".equals(MyApp.order.getLotteryId())||"130".equals(MyApp.order.getLotteryId())){
				if(isMoNiLottery(MyApp.order.getLotteryId())){
					start(ThirdActivity.class,PayIntegral.class,payBundle);
				}else{
					start(ThirdActivity.class,Paymain.class,payBundle);
				}
				MyApp.resetOrderBean();
				setFragmentCacheEnable(false);//fragment 不需要缓存
				recoverDefaultSet();//恢复默认设置
				fragment.finish();
				finish();
			 }
			break;
		}
	}
	/**
	 * 划分彩种支付方式  
	 * 模拟彩种走模拟支付渠道 正式销售彩种走正式的支付渠道
	 * @param getLotteryId
	 * @return
	 */
	private boolean isMoNiLottery(String getLotteryId){
		boolean tag=false;
		String[] miArrayid = MyApp.res.getStringArray(R.array.mi_lottery);
		for (int i = 0; i < miArrayid.length; i++) {
			if(miArrayid[i].equals(getLotteryId)){
				tag=true;
			}
		}
		return tag;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		time_tv1.setText("距"+CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue()+"期截止:");
		CustomTagEnum.startTimer(this, timerMap,1);
		notifyDataSetChanged();
		setNoticeText();
		
	}
//		对外 listView 数据刷新
		public  void notifyDataSetChanged(){
			if(betschemeAdapter!=null){
				betschemeAdapter.notifyDataSetChanged();
             }
		}
		
	
	/**
	 * 登陆返回之后   回调 去付款的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1002 && UserBean.getInstance().isLogin()){//登录成功才会再次去请求gotopay
			aptitudegotopay();
		}
	}
	/**
	 *  rateBean 恢复到默认初始化	方案
	 */
	private void recoverDefaultSet() {
		RATEBean.getInstance().setFANG_AN(1);
		RATEBean.getInstance().setDEFAULT_XH("10");
		RATEBean.getInstance().setMAX_XH("10");
		RATEBean.getInstance().setBS(String.valueOf(MyApp.order.getFold()));//倍数
		RATEBean.getInstance().setONE_YLL("30");
		RATEBean.getInstance().setTWO_QH("5");
		RATEBean.getInstance().setTWO_QYLL("50");
		RATEBean.getInstance().setTWO_HYLL("20");
		RATEBean.getInstance().setTHREE_RMB("30");
		
	}
	/**
	 * 根据方案的改变  改变顶部提示
	 * @param fang_AN
	 */
	private void changeToptv(int fang_AN,final FADialog mfaDialog) {
		switch (fang_AN) {
		case 1:
			
			znzh_table_top_tv.setText("共"+RATEBean.getInstance().getDEFAULT_XH()
					+"期 全程最低盈利率"+RATEBean.getInstance().getONE_YLL()+"%");
			MyToast.showToast(mActivity, "成功生成方案");
			break;
		case 2:
			znzh_table_top_tv.setText("共"+RATEBean.getInstance().getDEFAULT_XH()
					+"期 前"+RATEBean.getInstance().getTWO_QH()+"期"+RATEBean.getInstance().getTWO_QYLL()
					+"%之后盈利率"+RATEBean.getInstance().getTWO_HYLL()+"%");
			MyToast.showToast(mActivity, "成功生成方案");					
			break;
		case 3:
			znzh_table_top_tv.setText("共"+RATEBean.getInstance().getDEFAULT_XH()
					+"期 全程最低盈利"+RATEBean.getInstance().getTHREE_RMB()+"元");
			MyToast.showToast(mActivity, "成功生成方案");
			break;

		default:
//			默认提示
			znzh_table_top_tv.setText("共"+RATEBean.getInstance().getDEFAULT_XH()
					+"期 全程最低盈利率"+RATEBean.getInstance().getONE_YLL()+"%");
			MyToast.showToast(mActivity, "生成方案失败！");
			break;
		}
		if("122".equals(MyApp.order.getLotteryId())){
			notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+RATEBean.getInstance().getDEFAULT_XH()+"期</font><font color=#ff9500>"+ALL_MAX_MONENY+"积分</font>");
		}else{
			notice= (Spannable) Html.fromHtml("<font color=#606060>共追"+RATEBean.getInstance().getDEFAULT_XH()+"期</font><font color=#ff9500>"+ALL_MAX_MONENY+getString(R.string.zwl_dballcommon_toast_1_6)+"</font>");
		}
		show_bet_result_tv.setText(notice);//默认提示
	}

	/**
	 * 监听手机返回键
	 */
	private void onBack() {
		yloudialog.popDeleteConfirm("放弃智能追号", "当前所有方案信息将不会保存！", new MyClickedListener() {
					@Override
					public void onClick() {
						yloudialog.dismiss();
					
					}
				}, new  MyClickedListener() {
					@Override
					public void onClick() {
						TicketBean mTicketBean = MyApp.order.getTickets().get(0);//智能追号的选票有且只有一票
//						修改订单中的信息
						MyApp.order.setAmount(mTicketBean.getTicketAmount());
						MyApp.order.setFold(1);//倍数
						if(ALL_QH_LIST.size() > 1){
							MyApp.order.setIssue(ALL_QH_LIST.get(0));//当前销售期
						}
						MyApp.order.setChaseNum(1);//追期数
						
						start(SecondActivity.class,SsqBetorder.class,null);
						recoverDefaultSet();//恢复默认设置
						yloudialog.dismiss();
						finish();
					}
				});
			
	}
	@Override
	public void onReceiveYilou_start() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onReceiveYilou_stop() {
		String issueStr=CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue();
		if(MyApp.order.getIssue().equals(issueStr)){
			return;
		}
		
		this.initializeList();
		this.resultListView();
		notifyDataSetChanged();//刷新
		String contextStr="当前销售期已更新为"+issueStr+"，\n请核对期号！";
		time_tv1.setText("距"+issueStr+"期截止:");
		yloudialog.unableChasesDialog("销售期更新提示!", contextStr,"确定",
				new MyClickedListener() {
			
			@Override
			public void onClick()
			{
				yloudialog.dismiss();
			}
		},false);
	}

	@Override
	public void onReceiveYilou_awardinfo() {
		// TODO Auto-generated method stub
		
	}

	public static void setIS_STOP(String iS_STOP) {
		IS_STOP = iS_STOP;
	}

	public static ListView getBet_scheme_lv() {
		return bet_scheme_lv;
	}

}
