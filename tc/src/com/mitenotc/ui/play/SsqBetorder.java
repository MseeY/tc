package com.mitenotc.ui.play;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.OrderBean;
import com.mitenotc.bean.RATEBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.NetUtil;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.adapter.BetListAdapter;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.pay.PayIntegral;
import com.mitenotc.ui.pay.Paymain;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.RefreshYiLouReceiver;
import com.mitenotc.ui.ui_utils.ShakeListener;
import com.mitenotc.ui.ui_utils.ShakeListener.OnShakeListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.LogUtil;

/**
 * 订单页面
 * 所有彩种通用
 * 
 * @author wanli
 *
 */
public class SsqBetorder extends BaseFragment implements OnClickListener, OnTouchListener, OnCheckedChangeListener, RefreshYiLou {
	private BaseBuyFragment fragment;//该彩种的选号界面的fragment//ck 添加
	private Class<? extends BaseFragment> positionPLfragment;//position指定跳转的play玩法彩种选号界面 fragment
	private Class<? extends BaseFragment> lotteryClazz;//当前彩种的选号界面的fragment的clazz.//ck 添加
	private Button oneselfchoose_btn;//自选号码
	private Button machinechoose_btn;//机选号码
	private Button clearchoose_btn;//清空列表
	private Button znzh_btn;//智能追号
	private Button payment_btn;//付款
	private Bundle bundle; 
	private OrderBean ord;
	
	
	private static String IS_STOP="1";//中奖后是否停止追期  默认是1 (0不停止；1停止)
    /**　　是否允许追期   只用当为 0 的时候可以追号  其它分情况弹出提示！　　**/
	private static int ISALLOW_TAG=0;
	
	private MessageJson paramsJson;//请求参数
	private MessageBean returnMB;//服务器参数
	private List<MessageBean> listMB;//服务器返回信息List
	private TicketBean ticketBean;
	
//	private RadioGroup  chase_choose_rgp;//追期选择
	private RadioButton pursue_one_month_rbtn;//追一个月
	private RadioButton pursue_three_month_rbtn;//追三个月
	private RadioButton pursue_max_month_rbtn;//最大追期
	
	private static long AllPOUR=0;//总注数
	private static Spannable notice;
	//追期倍数输入框中会用到的变量
	private String ed;//Edittext 输入的字符串
	private String ed2="";
	private Spannable spad=null;
	private String[] texts;
	private String sampleNotice0 = MyApp.res.getString(R.string.basebuy_notice_text);;//组织底部提示         共NUM注<font color="#ff9500"> MONEY 元</font>
	private String sampleNotice1 = MyApp.res.getString(R.string.basebuy_notice_jf_text);//组织底部提示         共NUM注<font color="#ff9500"> MONEY 积分</font>
	private String sampleNotice2 = MyApp.res.getString(R.string.betorder_show_text);//组织底部提示         追QS期投N倍ZHU注<font color="#ff9500">MONEY元</font>
	private String sampleNotice3 = MyApp.res.getString(R.string.moni_betorder_show_text);//组织底部提示         追QS期投N倍ZHU注<font color="#ff9500">MONEY元</font>
	
	
	/** 震动 **/
	private ShakeListener shakeListener;
	private  RefreshYiLouReceiver mReceiver;
	private long lastClickTime=0; //防止重复点击
	
	private TCDialogs dialog;//提示框
	private Dialog mDialog;
	private TCDialogs ticketTCDialog;//提示框
	private RelativeLayout chase_expect_RL;//追期和停止追号最外层布局
	
	private TextView no_selection_tv;//没有选择任何投注号码时候的提示（根据listView 的内容是否为空判断）
	private Spannable textspb;//默认提示
//	http://news.mitenotc.com/a/weituotouzhuguize/20140409/17.html  TCWebFragment
	private CheckBox agreeEntrust_checkBox;//同意委托
	
	private ListView  bet_indent_listview;//订单列表
	private EditText expect_number_ed;//连续买期数
	private EditText multiple_number_ed;//投注倍数
	private TextView show_bet_result_tv;//总共投注 结果与金额
	
	private int max_expect;
	private LinearLayout checkbox_layout;
	private LinearLayout choose_linearLayout;
	private CheckBox gain_reward_stop_checkBox;//停止追号
	private CheckBox zj_bet_checkBox;//追加投注

	//存储双色球投注信息 投注方式 投注号码 分类存放
	private BetListAdapter adapter=new BetListAdapter();//列表适配器	
	/**
	 * 处理清空
	 */
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 1:
				toClearListView();
				break;
			}
		}
	};
	
	@Override
	protected void onMessageReceived(Message msg)
	{
		super.onMessageReceived(msg);
		returnMB=(MessageBean) msg.obj;
		payment_btn.setEnabled(true);//防止重复点击
		payment_btn.setText("付款");
		if(mDialog!=null && mDialog.isShowing()){
	    	mDialog.dismiss();
		}
	     switch (msg.arg1){
	        	//请求服务器当前销售期数
		 			case 1200:
//		 				payment_btn.setEnabled(true);//防止重复点击
						if(0==Integer.parseInt(returnMB.getA()))
						{
							//获得销售期 并比对
							if(getSellexpect(returnMB)){
								MyApp.order.setIsStop(IS_STOP); //是否停止追期
								submitData(1205, 1205, MyApp.order.getOrderJson());
							}else{
								MyToast.showToast(mActivity, "销售期已成功更新！");
							}
						}else {
							  MyToast.showToast(mActivity, returnMB.getB());
						}
						break;
			         case 1205:
//			        	  A ： 0 成功 
			        	 if("0".equals(returnMB.getA()))
			        	 {
			        		 UserBean.getInstance().setAvailableMoney(Long.parseLong(returnMB.getD()));
			        		 UserBean.getInstance().setAvailableCash(Long.parseLong(returnMB.getE()));
			        		 UserBean.getInstance().setAvailableBalance(Long.parseLong(returnMB.getF()));
			        		 AccountEnum.convertMessage(returnMB.getLIST());
							 MyApp.order.setOrderId(returnMB.getC());//流水号
			        		 Bundle payBundle = new Bundle();
//			        		 payBundle.putString("lotteryId", MyApp.order.getLotteryId());
			        		 payBundle.putString("orderId", returnMB.getC());//1107接口需要的 订单号
			        		 
			        		 payBundle.putString("money", MyApp.order.getAmount()+"");//1107 接口需要的第三方支付金额,这里其实是支付的总金额
			        		 payBundle.putString("availableAmount", returnMB.getD());//用于确定是否进行红包支付
			        		 payBundle.putString("lotteryId", MyApp.order.getLotteryId());//支付中需要的 彩票的彩种和期数
			        		 payBundle.putString("issue", MyApp.order.getIssue());//支付中需要的期数
			        		 payBundle.putInt(MyApp.res.getString(R.string.cmd), 1107);//支付中需要的期数
//			        		 if("122".equals(MyApp.order.getLotteryId())||"130".equals(MyApp.order.getLotteryId())){
			        		 if(isMoNiLottery(MyApp.order.getLotteryId())){
			        			 start(ThirdActivity.class,PayIntegral.class,payBundle);
			        		 }else{
			        			 start(ThirdActivity.class,Paymain.class,payBundle);
			        		 }
			        		 finish_and_clearPlay();
			        		 MyApp.resetOrderBean();
			        		 setFragmentCacheEnable(false);//fragment 不需要缓存
			        	       
			        	 }else{
			        		 if(StringUtils.isBlank(returnMB.getA())) {
			        			 MyToast.showToast(mActivity,"erro :"+ returnMB.getB());
			        			 return;
			        		 }
			        		 int tempKey=Integer.parseInt(returnMB.getA());
			        		 switch (tempKey) {
			        		 
								case 14004:
						     		//14004	玩法已期结，无法完成本次投注
									if(dialog==null) {
										dialog=new TCDialogs(mActivity);
									}else if(dialog!=null && !dialog.isShowing()){
										dialog.popDeleteConfirm("提  示", returnMB.getB(), 
//									左边按钮为取消
												new MyClickedListener() {
											@Override
											public void onClick() {
												dialog.dismiss();
											}
//									右边按钮为确定
										}, new MyClickedListener() {
											@Override
											public void onClick() {
												paramsJson=new   MessageJson();
												paramsJson.put("A", MyApp.order.getLotteryId());//彩中玩法
//												submitData(1200, 1200, paramsJson);//先请求服务器当前销售 期数  做比对
												dialog.dismiss();
											}
										});
									}
									break;
	
								default:
									 MyToast.showToast(mActivity,"erro :"+ returnMB.getB());
									break;
								}
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
	protected void errorResult(Message msg) {
		payment_btn.setEnabled(true);//防止重复点击
		payment_btn.setText("付款");
		payment_btn.invalidate();
		if(mDialog!=null && mDialog.isShowing()){
	    	mDialog.dismiss();
		}
		if(msg.obj!=null){
			MyToast.showToast(mActivity,  msg.obj.toString());
		}
	}
	@Override
	protected void nullResult() {
		payment_btn.setEnabled(true);//防止重复点击
		payment_btn.setText("付款");
		if(mDialog!=null && mDialog.isShowing()){
	    	mDialog.dismiss();
		}
	}
	public  void finish_and_clearPlay() {
		 fragment.finish();
		 finish();
	}
	

	/**
	 * 服务器返回的当前销售期
	 * 
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
    
	/*************************************************************************** 
	 *          listView 清空数据    不需要抽但会通知票 通过传递boolean  值 通知清空所有的票      Ticketbean    清空           
	 ****************************************************************************/
	private void toClearListView() {
//		textspb=(Spannable) Html.fromHtml("<font color=#606060>"
//				+getString(R.string.zwl_inform_tv0)+"</font><font color=#ff9500>"
//				+getString(R.string.zwl_inform_tv1)+"</font>");
		MyApp.order.getTickets().clear();
//		show_bet_result_tv.setText(textspb);//改变提示  追0期0倍0注0元
		spad=(Spannable) Html.fromHtml(replaceNoticeText(MyApp.order.getChaseNum(),MyApp.order.getFold(), 0, 0));
        show_bet_result_tv.setText(spad);
        isgoneNopourTv(true);//显示提示
		MyToast.showToast(mActivity, "已清空！");
		adapter.notifyDataSetChanged();
		
	};
	@Override
	public void onStart() {
		super.onStart();
		registerBroadcastReceiver();
	}
	private void registerBroadcastReceiver() {
		   mReceiver =new RefreshYiLouReceiver(MyApp.order.getLotteryId()); 
		   mReceiver.setmRefreshYiLou(this);
		   IntentFilter mFilter=new IntentFilter();
		   mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_start_loading");
		   mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_stop_loading");
		   mActivity.registerReceiver(mReceiver, mFilter); 	
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		setFragmentCacheEnable(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zwl_show_bet);
		IS_STOP="1";//初始化默认停止追期
		mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setTitleNav(getString(R.string.title_nav_tv_text), R.drawable.title_nav_back, 0);
        init();
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
   
   /**
     * 初始化准备
	 */
	private void init() 
	{
		    ord = MyApp.order;
		    if(MyApp.order.getLotteryId()!=null  && !"".equals(MyApp.order.getLotteryId())){
			    	max_expect=CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getMaxChasableIssueNum();
			    	setTitleText(CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getLotteryName());
		    }
			chase_expect_RL=(RelativeLayout) findViewById(R.id.chase_expect_RL);
			
			bet_indent_listview=(ListView)findViewById(R.id.bet_indent_listview);
			
		
			bet_indent_listview.setVerticalScrollBarEnabled(false);//不显示滚动条
			bet_indent_listview.setCacheColorHint(0);
			bet_indent_listview.setDrawingCacheEnabled(false);
			
			
			oneselfchoose_btn=(Button) findViewById(R.id.oneselfchoose_btn);
			machinechoose_btn=(Button) findViewById(R.id.machinechoose_btn);
			clearchoose_btn=(Button) findViewById(R.id.clearchoose_btn);
			
			no_selection_tv=(TextView) findViewById(R.id.no_selection_tv);
	
			agreeEntrust_checkBox=(CheckBox) findViewById(R.id.zwl_agreeEntrust_checkBox);
			
			expect_number_ed=(EditText) findViewById(R.id.expect_number_ed);
			multiple_number_ed=(EditText) findViewById(R.id.multiple_number_ed);
	
			checkbox_layout=(LinearLayout) findViewById(R.id.checkbox_layout);
			choose_linearLayout=(LinearLayout) findViewById(R.id.choose_linearLayout);
		    gain_reward_stop_checkBox=(CheckBox) findViewById(R.id.gain_reward_stop_checkBox);
		    zj_bet_checkBox=(CheckBox) findViewById(R.id.zj_bet_checkBox);
		    
//		    chase_choose_rgp=(RadioGroup) findViewById(R.id.chase_choose_rb);
		    pursue_one_month_rbtn=(RadioButton) findViewById(R.id.pursue_one_month_box);
		    pursue_three_month_rbtn=(RadioButton) findViewById(R.id.pursue_three_month_box);
		    pursue_max_month_rbtn=(RadioButton) findViewById(R.id.pursue_max_month_box);
		    pursue_max_month_rbtn.setText("最大"+max_expect+"期");//设置最大期数
		    
		    show_bet_result_tv=(TextView) findViewById(R.id.show_bet_result_tv);//TextView 显示投注总数 和投注总金额
		    payment_btn=(Button) findViewById(R.id.payment_btn);
	//		  智能追号　默认是ｇｏｎｅ　 初始化默认界面数据 
		    znzh_btn=(Button) findViewById(R.id.znzh_btn);
		    multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
		    if(1 > MyApp.order.getChaseNum() ){
		    	expect_number_ed.setText("1");
		    }else{
		    	expect_number_ed.setText(String.valueOf(MyApp.order.getChaseNum()));
		    }
	        setNotice();
	    	 
			if(!isorderBeanNull())
			{
				getOrderBean();//获取orderBean中的信息资源
			}
			setLisnten();
	  }
	
	  /**
	   * 设置通知消息
	   */
		private void setNotice() {
		//	    三同号或三连号为true
	    	AllPOUR =MyApp.order.getAmount();
	    	spad=(Spannable) Html.fromHtml(replaceNoticeText(MyApp.order.getChaseNum(),MyApp.order.getFold(), ((AllPOUR/100)/2), (AllPOUR/100)));
	        show_bet_result_tv.setText(spad);
	    	isGoneOrVisibility();
		}
	/**
	 * -用来消息提示的格式化-<![CDATA[追QS期投N倍NUM注<font color="#ff9500">MONEY元</font>]]>--
	 * @param qs 追期数
	 * @param n 倍数
	 * @param zhu 注数
	 * @param money 金额
	 * @return
	 */
    public String replaceNoticeText(int qs,int n,long zhu, long money){
    	if("122".endsWith(MyApp.order.getLotteryId())||"130".endsWith(MyApp.order.getLotteryId())){
			return StringUtils.replaceEach(sampleNotice3, new String[]{"QS","N","ZHU","MONEY"}, 
					new String[]{String.valueOf(qs),String.valueOf(n),String.valueOf(zhu),String.valueOf(money)});
		}else{
			return StringUtils.replaceEach(sampleNotice2, new String[]{"QS","N","ZHU","MONEY"}, 
					new String[]{String.valueOf(qs),String.valueOf(n),String.valueOf(zhu),String.valueOf(money)});
		}
	}


	/**
	 * 
	 * 根据彩种决定是否显示智能追号btn
	 * 只有高频彩种支持智能追号
	 * 120   快三  ，
	 * 112  十一选五    
	 * 119  江西 时时彩 ， 
	 * 
	 * 109  胜负彩任9场（版本一不涉及上线）
	 **/
	private void isGoneOrVisibility() 
	{
//		智能追号的按钮
	    if(isSagacityLottery(MyApp.order.getLotteryId())){
	    	znzh_btn.setVisibility(View.VISIBLE);//显示智能追号
	    	choose_linearLayout.setVisibility(View.GONE);
	    }else{
	    	znzh_btn.setVisibility(View.INVISIBLE);
	    	show_bet_result_tv.setPadding(0, 4, 4, 4);// 为了界面效果使得文字居中
	    	show_bet_result_tv.setGravity(Gravity.LEFT+Gravity.CENTER_VERTICAL);
	    }
//	     是否为大乐透 显示追加号码
	    if("106".equals(ord.getLotteryId())){//大乐透必选显示追加号码
	    	choose_linearLayout.setVisibility(View.GONE);
	    	show(chase_expect_RL);
	    	show(zj_bet_checkBox);
//	    	chase_expect_RL.setVisibility(View.VISIBLE);
//	    	zj_bet_checkBox.setVisibility(View.VISIBLE);//追加checkBox 通常彩种是没有的
	    	if(MyApp.order.isIs_dlt_pursue_mode_enabled()){
	    			zj_bet_checkBox.setChecked(true);//对于大乐透是否显示追加  单价是有变化的
	    		}else{
	    			zj_bet_checkBox.setChecked(false);//对于大乐透是否显示追加  单价是有变化的
	    		}
	    }else
	    {
	    	hide(chase_expect_RL);
	    	hide(zj_bet_checkBox);
	    }
	
    }
	/**
	 *  判断是否为智能追号彩种
	 * @param lotteryStr
	 * @return
	 */
     private boolean isSagacityLottery(String lotteryStr){
    	 if("119".equals(lotteryStr)){
    		 return true;
    	 }else if("112".equals(lotteryStr)){
    		 return true;
    	 }else if("120".equals(lotteryStr)){
    		 return true;
    	 }else if("122".equals(lotteryStr)){
    		 return true;
    	 }else if("113".equals(lotteryStr)){//江西十一选五
    		 return true;
    	 }else if("123".equals(lotteryStr)){//江西  快三
    		 return true;
    	 }else if("121".equals(lotteryStr)){//山东 快乐扑克
    		 return true;
    	 }else if("130".equals(lotteryStr)){//模拟 十一运
    		 return true;
    	 }else{
    		 return false;
    	 }
     }

	private void onBack() {//****************ck 更改******************
		payment_btn.setEnabled(true);//防止重复点击
		payment_btn.setText("付款");
		payment_btn.invalidate();
		if(mDialog!=null && mDialog.isShowing()){
	    	mDialog.dismiss();
		}
		if(dialog==null){
			dialog=new TCDialogs(mActivity);
		}
		if(MyApp.order.getTickets().size() == 0){//选票为空不需要情况了
			mActivity.finish();//先跳转后清空
			return;
		}
		if(dialog.isShowing()){
			dialog.dismiss();
		}else{
			dialog.popDeleteConfirm(new MyClickedListener() {
				@Override
				public void onClick() {
					dialog.dismiss();
				}
			}, new  MyClickedListener() {
				@Override
				public void onClick() {
					MyApp.resetOrderBean();
					dialog.dismiss();
					fragment.finish();
					mActivity.finish();//先跳转后清空
				}
			});
		}
	}

	@Override
	protected void onLeftIconClicked() {//ck 添加
		onBack();
	}
	
  /**
    * 控件监听
    */
	private void setLisnten() {
		//手机返回键
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				onBack();
			}
		});
		
        //追期一个月
        pursue_one_month_rbtn.setOnCheckedChangeListener(this);
        //追期三个月
        pursue_three_month_rbtn.setOnCheckedChangeListener(this);
        //最大追其
        pursue_max_month_rbtn.setOnCheckedChangeListener(this);
                
        agreeEntrust_checkBox.setOnClickListener(this);
		oneselfchoose_btn.setOnClickListener(this);
		machinechoose_btn.setOnClickListener(this);
		clearchoose_btn.setOnClickListener(this);
		payment_btn.setOnClickListener(this);
		znzh_btn.setOnClickListener(this);
		
		//是否停止追期
		gain_reward_stop_checkBox.setOnCheckedChangeListener(this);
		zj_bet_checkBox.setOnCheckedChangeListener(this);
		
		
////		item 监听
		bet_indent_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,  int position,
					long id) {
//				item  对应的 ：position
			    bundle=new Bundle();
				bundle.putInt("position", position);
				fragment.setOrderClickedTicket(position);
//				MyApp.saveProbeMsg("购彩列表","点击-"+CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getLotteryName()
//						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
				finish();
			}
		});
//		选票长按事件
		bet_indent_listview.setOnItemLongClickListener(new  OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,  final int position,
					long id) {
				dialog=new TCDialogs(mActivity);
				dialog.popDeleteConfirm("提  示", "删除选号？", 
						
//				左边按钮为取消
				new MyClickedListener() {
					
					@Override
					public void onClick() {
						dialog.dismiss();
					}
//				右边按钮为确定
				}, new MyClickedListener() {
					
					@Override
					public void onClick() {
						MyApp.order.getTickets().remove(position);
						
						adapter.notifyDataSetChanged();
						setNotice();  //更新提示!
						dialog.dismiss();
						
					}
				});
				
				return false;
			}
		});
		
		
		//注册传感器工具
			shakeListener = new ShakeListener(mActivity);
			shakeListener.setOnShakeListener(new OnShakeListener() {
					@Override
					public void onShake() {
						if(ticketTCDialog==null ){  //防止多次
							
							ticketBean=fragment.randomTicket();//机选一注
						    Spannable newLotteryNumSpb=(Spannable) Html.fromHtml(ticketBean.showLotteryNums()) ;
						    if(newLotteryNumSpb !=null )
						    {
						    	ticketTCDialog=new TCDialogs(mActivity);
						    	ticketTCDialog.popDeleteConfirm("摇一摇  天赐福号",newLotteryNumSpb,
	                                          //		取消	
	 							    	    new MyClickedListener() {
								    		
								    		@Override
								    		public void onClick() {
								    			ticketBean=null;//置空
								    			ticketTCDialog.dismiss();
								    			ticketTCDialog=null;
								    		}
                                    	//	确定 
								    	}, new  MyClickedListener() {
								    		
								    		@Override
								    		public void onClick() {
								    			MyApp.order.getTickets().add(0,ticketBean);
								    			String str = multiple_number_ed.getText().toString();
								    			if(!AppUtil.isEmpty(str)){
								    				MyApp.order.setFold(Integer.parseInt(str));
								    			}
								    			adapter.notifyDataSetChanged();
												setNotice();  //更新提示!
												ticketTCDialog.dismiss();
												ticketTCDialog=null;
								    			
								    		}
								    	});	
						    	}
						    	
						   }
					}

				});
			
//			 追期期数EditText   OnTouchListener监听
			expect_number_ed.setOnTouchListener(this);
//			投注倍数 EditText OnTouchListener监听
			multiple_number_ed.setOnTouchListener(this);
			
//			投注倍数 EditText   addTextChangedListener监听
			multiple_number_ed.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.toString().startsWith("0")){
						multiple_number_ed.setText("1");
						multiple_number_ed.setSelection(multiple_number_ed.length());
						Toast.makeText(mActivity, "至少输入1", Toast.LENGTH_SHORT).show();
					}
								String ed2="";
							    ed=multiple_number_ed.getText().toString().trim();
							    if(StringUtils.isBlank(ed) || "0".equals(ed)){
									 MyApp.order.setFold(1);//投注倍数
									 AllPOUR =MyApp.order.getAmount();
									 int templc=MyApp.order.getLotterysCount();
											
											spad=(Spannable) Html.fromHtml(replaceNoticeText(MyApp.order.getChaseNum(),
													1, templc, (AllPOUR/100)));
											show_bet_result_tv.setText(spad);
							    }else{
				    	            MyApp.order.setFold(Integer.parseInt(ed));
								    AllPOUR =MyApp.order.getAmount();
								    int templc=MyApp.order.getLotterysCount();
								    //更新通知  追期不为空
									ed2=expect_number_ed.getText().toString();
									if(StringUtils.isBlank(ed2))
									{
										spad=(Spannable) Html.fromHtml(replaceNoticeText(1,
												Integer.parseInt(ed), templc, (AllPOUR/100)));
										show_bet_result_tv.setText(spad);
										MyApp.order.setFold(Integer.parseInt(ed));//投注倍数
									}
									else
									{   
										MyApp.order.setFold(Integer.parseInt(ed));//投注倍数
										spad=(Spannable) Html.fromHtml(replaceNoticeText(MyApp.order.getChaseNum(),
												Integer.parseInt(ed), templc, (AllPOUR/100)));
										show_bet_result_tv.setText(spad);
										
									}
							 }
							    
//					   adapter.notifyDataSetChanged();		    
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {

					
					}
					
			});
//			 追期期数EditText   addTextChangedListener监听
			expect_number_ed.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.toString().startsWith("0")){
						expect_number_ed.setText("1");
						expect_number_ed.setSelection(expect_number_ed.length());
						Toast.makeText(mActivity, "至少输入1", Toast.LENGTH_SHORT).show();
					}           
					ed=expect_number_ed.getText().toString();//剔除所有空格
							if(StringUtils.isBlank(ed) || "0".equals(ed)){
								  MyApp.order.setChaseNum(1);//追号期数
								   AllPOUR =MyApp.order.getAmount();
								   int templc=MyApp.order.getLotterysCount();
										
										spad=(Spannable) Html.fromHtml(replaceNoticeText(1,
												MyApp.order.getFold(), templc, (AllPOUR/100)));
										show_bet_result_tv.setText(spad);
										MyApp.order.setChaseNum(1);
							}else{
								   MyApp.order.setChaseNum(Integer.parseInt(ed));//追号期数
									AllPOUR =MyApp.order.getAmount();
									int templc=MyApp.order.getLotterysCount();
									//更新通知  追期不为空
									ed2=multiple_number_ed.getText().toString();
									if(StringUtils.isBlank(ed2))
									{
										spad=(Spannable) Html.fromHtml(replaceNoticeText(Integer.parseInt(ed),
												1, templc, (AllPOUR/100)));
										show_bet_result_tv.setText(spad);
									}else
									{   
										    spad=(Spannable) Html.fromHtml(replaceNoticeText(Integer.parseInt(ed),
													Integer.parseInt(ed2),templc , (AllPOUR/100)));
										    show_bet_result_tv.setText(spad);
										    MyApp.order.setChaseNum(Integer.parseInt(ed));//追号期数
									}
							}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					
				}
			});
	
			
	}

 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		自选号码
		case R.id.oneselfchoose_btn:
			fragment.customTicket();
			finish();
			break;
//		机选一注	
		case R.id.machinechoose_btn:
		    machinechooseOne();
//			if(MyApp.order.getLotteryId()!=null && !"".equals(MyApp.order.getLotteryId()))
//			MyApp.saveProbeMsg(CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getLotteryName(),"选号订单页面-点击-机选一注", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
		    
			break;
//		清空列表
		case R.id.clearchoose_btn:
			toclearList();
			
			break;
//		付款
		case R.id.payment_btn:
			if("0".equals(multiple_number_ed.getText().toString().trim())){
				multiple_number_ed.setText("1");
				multiple_number_ed.setSelection(multiple_number_ed.length());
				Toast.makeText(mActivity, "至少输入1", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(("0".equals(expect_number_ed.getText().toString().trim()))){
				expect_number_ed.setText("1");
				expect_number_ed.setSelection(multiple_number_ed.length());
				Toast.makeText(mActivity, "至少输入1", Toast.LENGTH_SHORT).show();
				return;
			}
			if(mDialog==null){
				mDialog=new Dialog(mActivity,R.style.dialog_theme);
			 }
			if(mDialog!=null && mDialog.isShowing()){
		    	mDialog.dismiss();
			}
			mDialog.setContentView(R.layout.m_wait_dialog);
			mDialog.show();
			payment_btn.setEnabled(false);
			payment_btn.setText("付款..");
			
//			if(isFastClick(mActivity)){
//				return;
//			}
			gotopay();
			 
			break;
//		我同意委托
		case R.id.zwl_agreeEntrust_checkBox:
//			http://news.mitenotc.com/a/wttzgz.html  TCWebFragment
			
			 Bundle  mBundle=new Bundle();
			 mBundle.putString("title", "委托投注规则");
			 mBundle.putString("url", "http://news.mitenotc.com/a/wttzgz.html");
			 start(ThirdActivity.class,TCWebFragment.class,mBundle);
			 if(MyApp.order.getLotteryId()!=null && !"".equals(MyApp.order.getLotteryId()))
//					MyApp.saveProbeMsg(CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getLotteryName(),"选号订单页面-点击-同意委托", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			break;
//			智能追号
		case R.id.znzh_btn:
//			网络不做检测
//			if(NetUtil.checkNetWork(mActivity)){
			znzh_btn.setEnabled(false);
			skipToIntelligent();
			znzh_btn.setEnabled(true);
			
//				return;
//			}
//			unableChaseshowDialogs(4);
			break;
		default:
			break; 
		}
		
	}
	
    /**
     * 1.获取当前的销售期  
     * 2.获取当前的销售期 对应的时间
     *
     * 跳转到智能追号页面
     * 
     */
	private void skipToIntelligent() {
		String  qhStr="";
		String  qhStr0="";
		List<TicketBean>	tickets=MyApp.order.getTickets();
		 if(tickets==null || tickets.size() ==0)
		   {
		    	 ISALLOW_TAG=7;
		   }else{ 
			   String issueStr = MyApp.order.getTickets().get(0).getSalesType()+"-"+MyApp.order.getTickets().get(0).getChildId();
			   
			   if(tickets.size() > 1 ){
			   
				   ISALLOW_TAG = 1;
			    }else {
				ISALLOW_TAG=0;
				if(StringUtils.isBlank(multiple_number_ed.getText().toString())){
					MyApp.order.setFold(1);
				}else{
					MyApp.order.setFold(Integer.parseInt(multiple_number_ed.getText().toString()));
				}
				if("130".equals(MyApp.order.getLotteryId())){
					qhStr=CustomTagEnum.getItemByLotteryId(112).getIssue();//14031519
					qhStr0=CustomTagEnum.getItemByLotteryId(112).getMaxChasableIssue();
				}else{
					qhStr=CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue();//14031519
					qhStr0=CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getMaxChasableIssue();
				}
				
			   if(StringUtils.isBlank(qhStr)||StringUtils.isBlank(qhStr0)){//其中之一为空
					  ISALLOW_TAG=8;//暂时无法获得期号！
				 }
          //	  追号拦截  部分彩种不支持智能追号 奖金不好计算
				switch (Integer.parseInt(MyApp.order.getLotteryId())) 
				{
					case 119:
						  if("3-5".equals(issueStr)){//五星-通选
							  ISALLOW_TAG=5;
						  }
						break;
					case 121:
						if("0-0".equals(issueStr))
						{//包选（整合玩法）
							ISALLOW_TAG=5;
						}
						break;
	
					default:
						break;
					}
			    }
			  }
			if(0==ISALLOW_TAG)
			{
				RATEBean.getInstance().setBS(String.valueOf(MyApp.order.getFold()));
				RATEBean.getInstance().setLottery(MyApp.order.getLotteryId());//存储  彩种id
				
				start(ThirdActivity.class,IntelligentScheme.class, null);
				
	//			fragment.finish();//fragment选号
				finish();
//			 if(probeMsg!=null){
//            	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            	 probeMsg.put("F", "0");
//            	 MyApp.saveProbeMsg(probeMsg,true);
//                }
			}else 
			{
				unableChaseshowDialogs(ISALLOW_TAG);
			}
	}
	


	/**
	 * 不能追号的
	 * 情况弹出提示
	 * @param  i ： 分类提示
	 */
	private void unableChaseshowDialogs(int i) {
		dialog=new TCDialogs(mActivity);
		String contextStr ="";
		switch (i) 
			{
		//		不支持多注彩票智能追号！
				case 1:
					contextStr +="不支持多注彩玩法智能追号！";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "不支持多注彩票智能追号！");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//			没有获取到当前销售期号，无法进行只能追号！
				case 3:
					contextStr +="没有获取到当前销售期号，无法进行智能追号！";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "没有获取到当前销售期号，无法进行智能追号！");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//		  网络异常，请检查网络！	
				case 4:
					contextStr +=" 网络异常，请检查网络！";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", " 网络异常，请检查网络！");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//		 此玩法暂不支持智能追号！	
				case 5:
					contextStr +="此玩法投注，暂不支持智能追号！";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "此玩法复式投注，暂不支持智能追号！");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//	   "获取信息失败，请稍后尝试!";
				case 6:
					contextStr +="获取信息失败，请稍后尝试!";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "获取信息失败，请稍后尝试!");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//	    "订单列表为空!"
				case 7:
					contextStr +="订单列表为空!";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "订单列表为空!");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//	   "暂无法获取期号，请稍后再试!"
				case 8:
					contextStr +="暂无法获取期号，请稍后再试!";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "暂无法获取期号，请稍后再试!");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
		//	    "单注金额必须小于20000元！"
				case 9:
					contextStr +="单注金额必须小于20000元！";
//					if(probeMsg!=null)//  为了安全考虑
//					   	 probeMsg.put("E", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		            	 probeMsg.put("F", "2");
//		            	 probeMsg.put("G", "单注金额必须小于20000元！");
//		            	 MyApp.saveProbeMsg(probeMsg,true);
					break;
				default:
					break;
			}
		
		    dialog.unableChasesDialog("提    示 ", contextStr,"确定",
			new MyClickedListener() {
				
				@Override
				public void onClick()
				{
					dialog.dismiss();
					dialog=null;//置空
					
				}
			},
			
		false);
	}
	
    /**
     * 1 判断单票选号 是否超过25个
     * 2 判断是否登录
     * 3 判断期号是否为空
     */
	private void gotopay() 
	{
		boolean tagballNums=false;
		//防止重复点击  采用时间控制
//		payment_btn.setEnabled(false);
		AllPOUR=MyApp.order.getAmount();
		String isSueStr="";
		if("130".equals(MyApp.order.getLotteryId())){// 模拟十一运夺金 故此处采用十一运的id获得销售期号
			isSueStr=CustomTagEnum.getItemByLotteryId(112).getIssue();
		}else{
			isSueStr=CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getIssue();
		}
		if(MyApp.order.getTickets().size()==0)
		{
			MyToast.showToast(mActivity, "您还没有订单！");
			if(mDialog!=null && mDialog.isShowing()){
		    	mDialog.dismiss();
			}
			payment_btn.setEnabled(true);//防止重复点击
			payment_btn.setText("付款");
			payment_btn.invalidate();
			return;
		}else if(StringUtils.isBlank(isSueStr)){
			MyToast.showToast(mActivity, "无法获取当前期号，请稍后重试！");
			return;
		}
		 if(!UserBean.getInstance().isLogin())
		 {
    		 startLoginForResult();
    		 return;
    	  }
//    	 双色球  大乐透  七星彩 单票选号超过25 号码拦截
    	 if("118".equals(MyApp.order.getLotteryId())
    			 ||"106".equals(MyApp.order.getLotteryId())||"117".equals(MyApp.order.getLotteryId())){//双色球 大乐透  七乐彩
    		 
    		 List<TicketBean> listTKB=MyApp.order.getTickets();
    		 List<List<Integer>> LotteryNumsList=null;
    		 for (int i = 0; i < listTKB.size(); i++) 
    		 {
			 int temp=0;
			 LotteryNumsList=listTKB.get(i).getLotteryNums();
			 for (int j = 0; j < LotteryNumsList.size(); j++) 
			 {
				 temp +=LotteryNumsList.size();
			 }
			 if(temp > 25){
				 MyToast.showToast(mActivity, "选号最多不能超多25个选号！");
				 tagballNums=true;
				 return;
			 }
    			 
    		 }
    	 }
    	 
		 if(tagballNums){
			 MyToast.showToast(mActivity, "选号最多不能超多25个选号！");
		 }else {
			 setExpectAndMultiple();// 修改Order中的 追期和  倍数
			 System.out.println("IS_STOP ---->"+IS_STOP);
			 MyApp.order.setIsStop(IS_STOP); //是否停止追期
			 MyApp.order.setIssue(isSueStr);//当前销售期期号 14050544
			 submitData(1205, 1205, MyApp.order.getOrderJson());//{"D":200,"LIST":[{"D":"1","E":"1","F":"200","A":"11","B":"0","C":"01"}],"E":"1","F":1,"A":"2014-05-05 16:01:42","B":"121","C":"14050544"}
		 }
//		 payment_btn.setEnabled(true);
			
	}
	
	

     /**
      * MyApp.order 存取订单的 投注倍数 和追期 期数
      */
	
	private void setExpectAndMultiple() {
		String multipleStr  = multiple_number_ed.getText().toString(); 
		String expectStr = expect_number_ed.getText().toString();
		if(multipleStr!=null && !"".equals(multipleStr)){
			MyApp.order.setFold(Integer.parseInt(multipleStr));
		}else
		{
			MyApp.order.setChaseNum(1);
		}
		   
		if(expectStr!=null && !"".equals(expectStr))
		{
		     MyApp.order.setChaseNum(Integer.parseInt(expectStr));//追号期数
		  
		}else{
				MyApp.order.setChaseNum(1);//追号期数	
		}
	}
  
	/**
	 * MyApp.order.getTickets()  每一票的投注倍数
	 * 
	 * @param multipleStr
	 */

//	private void setTicketfold(int multipleStr) {
//		MyApp.order.setFold(multipleStr);//投注倍数
//		List<TicketBean> tickList = MyApp.order.getTickets();
//		List<TicketBean> nowTickList=new ArrayList<TicketBean>();
//		for (int i = 0; i < tickList.size(); i++) {
//			TicketBean everyTicket = tickList.get(i);
//			everyTicket.setFold(multipleStr);//单票倍数 
//			nowTickList.add(everyTicket);
//		}
//		MyApp.order.setTickets(nowTickList);
//	}
	
	/**
	 * 登陆返回之后   回调 去付款的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1002 && UserBean.getInstance().isLogin()){//登录成功才会再次去请求gotopay
			gotopay();
		}
	}
//	追期 的RadioButton
//	private void setchtrue() {
//		if(pursue_max_month_rbtn.isChecked()){
//			pursue_one_month_rbtn.setChecked(false);
//			pursue_three_month_rbtn.setChecked(false);
//		}else if(pursue_three_month_rbtn.isChecked()){
//			pursue_one_month_rbtn.setChecked(false);
//			pursue_max_month_rbtn.setChecked(false);
//		}else if(pursue_one_month_rbtn.isChecked()){
//			pursue_three_month_rbtn.setChecked(false);
//			pursue_max_month_rbtn.setChecked(false);
//			
//		}
//		
//	}

	 /**
      * 获得选号fragment传递过来的orderBean 
      **/
	   private void getOrderBean() 
	   {
		 
		if(MyApp.order!= null)
		{
		   adapter = new BetListAdapter(mActivity,MyApp.order);//*****************ck 添加*******************
		   bet_indent_listview.setAdapter(adapter);//适配数据
        }
		
	  }
	/**
	 * true 为空
	 * false 不为空
	 * 类传递orderBean判断用户是否选择了号码
	 */
	private boolean isorderBeanNull() 
	{

		if(MyApp.order == null)
		{
			isgoneNopourTv(true);
			return true;
		}
		isgoneNopourTv(false);
		return false;
	}
	
   /**
    * 提示TextView
    * @param b true  View.VISIBLE
    *          false View.GONE
    */
	private void isgoneNopourTv(boolean b) 
	{
	      if(b) 
	      {
	    	  no_selection_tv.setVisibility(View.VISIBLE);//显示 您还没有选择投注号码提示
	    	 
	    	  return;
		  }
			  no_selection_tv.setVisibility(View.GONE);
	}
	
    /**
     * 机选一注
     */
	private void machinechooseOne() {
		//*************************************ck 添加******************************
		MyApp.order.getTickets().add(0,fragment.randomTicket());
		adapter.notifyDataSetChanged();
		//*************************************ck 添加******************************
		isgoneNopourTv(false);
//		int templc=MyApp.order.getLotterysCount();
		String str = multiple_number_ed.getText().toString();
		if(!AppUtil.isEmpty(str)){
			MyApp.order.setFold(Integer.parseInt(str));
		}
		
		AllPOUR =MyApp.order.getAmount();
//		int temp1=1,temp2 =1;
//		if(ed2!=null && !"".equals(ed2)){
//			temp2=Integer.parseInt(ed2);
//		}
//		if(ed!=null && !"".equals(ed)){
//			temp1=Integer.parseInt(ed);
//		}
		spad=(Spannable) Html.fromHtml(replaceNoticeText(MyApp.order.getChaseNum(),MyApp.order.getFold(),
				                       MyApp.order.getLotterysCount(), (AllPOUR/100)));
		show_bet_result_tv.setText(spad);
//		setNotice();//默认提示
	}


	/**
	 * 清空listView  dialog  提示 用户选择是否清空
	 */
	private void toclearList()
	{
		if(MyApp.order.getTickets().size() == 0){
			return;
		}
		dialog =new TCDialogs(mActivity);
		dialog.popDeleteConfirm("清空提示", "您确定要清空当前投注列表吗？",
				new MyClickedListener()
		    {
			
					@Override
					public void onClick() {
						dialog.dismiss();
						
					}
		   }, new MyClickedListener()
		   {
			
			@Override
			public void onClick() {
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
						mHandler.sendEmptyMessage(1);
//					}
//				}).start();
				
			}
		 });
		
	}
	
   /**
     *  实现item 跳转
     *  
     *  排列三 PL100
	 *  排列五 PL102
	 *  七星彩PL103
	 *  大乐透PL106
	 *  胜负彩任9场PL109
	 *  十一选五 PL112
	 *  七乐彩 PL117
     *  双色球普通投注 PL118
	 *  江西 时时彩 PL119
	 *  快三 PL120
     * @param LotteryIdStr
     * @return
     */
	public static Class<? extends BaseFragment> getPlFragment(String LotteryIdStr){
		if("100".equals(LotteryIdStr))
		{		
			 return PL100.class;
		}else if("102".equals(LotteryIdStr))
		{
			return  PL102.class;
		}else if("103".equals(LotteryIdStr))
		{
			  return PL103.class;
		}else if("106".equals(LotteryIdStr))
		{
			  return PL106.class;
		}else if("109".equals(LotteryIdStr))
		{
			  
			  return PL109.class; //  胜负任九场   没有继承BaseBuyFragment  直接继承的  BaseFragment--TODO  版本一不涉及上线
		}else if("112".equals(LotteryIdStr))
		{
			   return PL112.class;
		}else if("117".equals(LotteryIdStr))
		{
			  return PL117.class;
		}else if("118".equals(LotteryIdStr))
		{
			  return PL118.class;
		}else if("119".equals(LotteryIdStr))
		{
			  return PL119.class;
		}else if("120".equals(LotteryIdStr))
		{
			  return PL120.class;
		}else if("122".equals(LotteryIdStr))
		{
			  return PL122.class;
		}
		
		return null;
	}
	
     /**
      * EditText OnTouch 事件
      *
      */
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
				switch (v.getId()) 
					{
					case R.id.expect_number_ed:
						String str=expect_number_ed.getText().toString();
						if(event.getAction()==MotionEvent.ACTION_DOWN){
							expect_number_ed.setSelection(str.length());
							chase_expect_RL.setVisibility(View.VISIBLE);//关闭追期   包括停止追期
//						    if("119".equals(MyApp.order.getLotteryId())
//						    		||"112".equals(MyApp.order.getLotteryId())
//						    		||"120".equals(MyApp.order.getLotteryId())
//						    		||"122".equals(MyApp.order.getLotteryId())){//高频彩不显示
//								return false;
//							}else{
//								
//								choose_linearLayout.setVisibility(View.VISIBLE);
//							}
						}
						break;
					case R.id.multiple_number_ed:
						str=multiple_number_ed.getText().toString();
						multiple_number_ed.setSelection(str.length());
						if(event.getAction()==MotionEvent.ACTION_DOWN){
							chase_expect_RL.setVisibility(View.GONE);//关闭追期   包括停止追期
							choose_linearLayout.setVisibility(View.GONE);
						}
						break;
	                default:
						break;
					}
		return false;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton cpdBtn, boolean ischecked) 
	{
		switch (cpdBtn.getId()) 
			{
		//			最大155期  --max_expect
			case R.id.pursue_max_month_box:
					
						if(cpdBtn.isChecked()){
//							LogUtil.info("ischecked 1 CompoundButton  : "+cpdBtn.isClickable());
//							
//							pursue_max_month_rbtn.setPadding(10,0, 10, 0);	
//							pursue_max_month_rbtn.setTextColor(Color.RED);
							if("0".equals(max_expect)){
								MyToast.showToast(mActivity, "无法获得最大期号,请稍候重试！");
							}else{
								expect_number_ed.setText(max_expect+"");
								MyToast.showToast(mActivity, "最大追"+max_expect+"期！");
							}
						}else{
//							LogUtil.info(ischecked+" 2 CompoundButton  : "+cpdBtn.isClickable());
//							pursue_max_month_rbtn.setPadding(1, 0, 20, 0);
//							pursue_max_month_rbtn.setTextColor(R.drawable.cb_textcolor_selector);
						}
				break;
		//			追三个月
			case R.id.pursue_three_month_box:
				
					if(cpdBtn.isChecked()){
//						LogUtil.info(cpdBtn+" 1 CompoundButton  : "+cpdBtn.isClickable());
						
//						pursue_three_month_rbtn.setPadding(10,0, 10, 0);	
//						pursue_three_month_rbtn.setTextColor(Color.RED);
						expect_number_ed.setText("40");
						
						MyToast.showToast(mActivity, "追期三个月40期!");
					}else{
//						LogUtil.info("cpdBtn 2 CompoundButton  : "+cpdBtn.isClickable());
//						pursue_three_month_rbtn.setPadding(1, 0, 20, 0);
//						pursue_three_month_rbtn.setTextColor(R.drawable.cb_textcolor_selector);
					}
					
				break;
		//			追一个月
			case R.id.pursue_one_month_box:
						if(cpdBtn.isChecked()){
//							LogUtil.info("cpdBtn 1 CompoundButton  : "+cpdBtn.isClickable());
							
//							pursue_one_month_rbtn.setPadding(10,0, 10, 0);	
//							pursue_one_month_rbtn.setTextColor(Color.RED);
							expect_number_ed.setText("14");
							
							MyToast.showToast(mActivity, "追期一个月14期!");
						}else{
//							LogUtil.info(" cpdBtn 2 CompoundButton  : "+cpdBtn.isClickable());
//							pursue_one_month_rbtn.setPadding(1, 0, 20, 0);
//							pursue_one_month_rbtn.setTextColor(R.drawable.cb_textcolor_selector);
						}
				break;
		//是否停止追期(0不停止；1停止)
			case R.id.gain_reward_stop_checkBox:
					if(ischecked){
							MyToast.showToast(mActivity, R.string.is_positive);
							IS_STOP="1";
						}else{
							IS_STOP="0";
							MyToast.showToast(mActivity, R.string.dialog_btn_cancel);
						}
				break;
				//追加投注
			case R.id.zj_bet_checkBox:
				if(ischecked){
					MyApp.order.setIs_dlt_pursue_mode_enabled(true);
				}else{
					MyApp.order.setIs_dlt_pursue_mode_enabled(false);
				}
				adapter.notifyDataSetChanged();
				int templc=MyApp.order.getLotterysCount();
				AllPOUR=MyApp.order.getAmount();
				spad=(Spannable) Html.fromHtml(replaceNoticeText(1,1,templc , (AllPOUR/100)));
				show_bet_result_tv.setText(spad);
				break;
		
			default:
				break;
			}
	}
	/** 防止点击过快  出现白屏 */
	private   boolean isFastClick(Context mActivity) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1500 ) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	@Override
	public void onStop() {
		super.onStop();
		shakeListener.stop();
		if(mReceiver!=null){
			 mActivity.unregisterReceiver(mReceiver);
			 mReceiver=null;
		 }
	}
	@Override
	public void onResume() {
		super.onResume();
		shakeListener.start();
		ord = MyApp.order;
		payment_btn.setEnabled(true);//  付款时候 用户 按手机的返回键
		payment_btn.setText("付款");
		if(mDialog!=null && mDialog.isShowing()){
	    	mDialog.dismiss();
		}
	}


	@Override
	public void onReceiveYilou_start() {

	}
	
	@Override
	public void onReceiveYilou_stop(){
		dialog=new TCDialogs(mActivity);
		String issueStr=MyApp.order.getIssue();
		String contextStr="当前销售期已更新为"+issueStr+"，\n请核对期号！";
		dialog.unableChasesDialog("销售期更新提示!", contextStr,"确定",
				new MyClickedListener() {
			@Override
			public void onClick()
			{
				dialog.dismiss();
			}
		},false);
	}

	@Override
	public void onReceiveYilou_awardinfo() {
		// TODO Auto-generated method stub
		
	};



}
