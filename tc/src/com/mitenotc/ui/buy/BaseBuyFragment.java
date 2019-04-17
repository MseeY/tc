package com.mitenotc.ui.buy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.engine.ticket.TicketEngine;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.play.SsqBetorder;
import com.mitenotc.ui.ui_utils.DrawerLayout;
import com.mitenotc.ui.ui_utils.GridRadioGroup;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MGridView.ActionUpListener;
import com.mitenotc.ui.ui_utils.ShakeListener;
import com.mitenotc.ui.ui_utils.ShakeListener.OnShakeListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FadeAnimUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

public abstract class BaseBuyFragment extends BaseFragment { 
//	private long testTime;
	private boolean isCreated;//用于标记该fragment是否已经创建,如果已经创建,则基本的布局的加载不需要重新加载一次
	protected Bundle mBundle;//数据传递用的bundle
	protected TicketBean ticket;//选号的 ticket,与接口相对应
	protected ShakeListener shakeListener;
	protected List<List<Integer>> lotteryNums;
	protected MGridView[] containers;
//	private ViewGroup[] containersParents;
	
	protected String issue;//期次信息
	//玩法代码
	/** 子类要复写该引用,默认为双色球 */
	protected String lotteryId = "118";//
	//	子玩法代码
	protected String childId = "0";
	/** 子类要复写, 购彩界面初始化的销售方式*/
	protected String salesType = "1";
	
	private String randomableSalesType;//用于记录可以机选的销售方式. 该变量主要是用于倍投界面的机选
	private String randomableChildId;//用于记录可以机选的子玩法代码. 该变量主要是用于倍投界面的机选
	private int randomableSaleType_checkedId;// 用于记录可以机选的 子玩法的radiobutton 的 id
	
	private GridRadioGroup saleType_grg;//销售方式的contentView 中必须有该id的 RadioGroup 控件
	
	protected DrawerLayout drawerLayout;
	protected View lotteryView;//彩种布局
	
	private Button again_get_hmsg_btn;//获取不到网络数据时点击按钮 重新获取网络数据
	private Button basebuy_btn_shake;//摇一摇按钮
	private TextView basebuy_tv_shake;//摇一摇 条目的提示 textview
	private RelativeLayout basebuy_rl_shake;//摇一摇 整个条目,用于控制 该行的显示和隐藏
	

//	private TextView yilou_tv;// 遗漏按钮
	
	private LinearLayout basebuy_ll_history;
	
	//销售方式的 popwindow
	private PopupWindow pop_saleType;
	protected View content_saleType;//销售方式的view,用于pop_saleType的 content//如果为空则pop_saleType 不会被设置
	//显示(/隐藏 )遗漏 
	private PopupWindow pop_yiLouCut;
	protected View content_yiLouCut;//销售方式的view,用于pop_yiLou的 content//如果为空则pop_yiLou 不会被设置
	
	private long last_pop_time;//用于记录popwindow最后一次dismiss的时间
	
	//高频彩种用到的,用来显示剩余的秒数的
	//底部导航
	protected TextView basebuy_tv_notice;
	protected Button basebuy_btn_clear;
	protected Button basebuy_btn_ok;
	protected LinearLayout baseBottom_layout;
	protected View basetop_line;//分割线
	protected TextView fast3_await_cb;
	protected TextView fast3_time_tv;
	protected TextView fast3_expect_tv;
	protected TextView drop_down_d;
	protected TextView fast3_distance_tv;
	protected LinearLayout fast3_title_Layout;
	protected RelativeLayout fast3_expect_Rlyout;
	protected RelativeLayout basetitle_leftlayout;
	protected LinearLayout baseTitle_reightLayout;
	protected LinearLayout baseAwait_layout;//等待布局
	protected Map<TextView, CustomTagEnum> timerMap; 
	
	protected boolean is_clear_containers_required;//用于标记跳转到投注界面时需要清空当前的容器//主要是用于区分非跳转状态下该页面的 onstart 和 onstop 被调用的情况. 因为clear方法需要在onstop 中调用
	
	private LinearLayout historyListView;
//	private ListView historyListView;
	private boolean is_saleType_grg_clicked;//用于标记 子玩法切换时是用户的选择还是程序中的需求
	protected boolean isScroll=true;//用于标记 子玩法切换时是用户的选择还是程序中的需求
	
	// 悬浮窗口
	
//	private static WindowManager wm=null;
//	private static WindowManager.LayoutParams wmParams=null;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {//把 原来在 GridRadioGroup 的onCheckedChanged 中的操作放到handler中,主要考虑小米系统的兼容
			
			try {
			if(pop_saleType.isShowing())
				pop_saleType.dismiss();
			resetTitleState();
			RadioGroup group = (RadioGroup) msg.obj;
			int checkedId = msg.arg1;
			if(saleType_grg.getCurrentCheckedId() == checkedId){
				////System.out.println("curentId not changed");
				return;
			}
				saleType_grg.setCurrentCheckedId(checkedId);
			
			RadioButton rb = (RadioButton) group.findViewById(checkedId);
			if(rb == null ||rb.getTag() == null){//安全性考虑,rb不会是空的,同时 tag也不能为空
				return;
			}
			setTitleText(rb.getText().toString());//改变不同的销售方式的时候更改头部导航的标题文字
			tag = rb.getTag().toString();
			String[] tags = tag.split(",");
			salesType = tags[0];
			if(tags.length >1){
				childId = tags[1];
			}else {
				childId = "0";
			}
			ticket.setSalesType(salesType);
			ticket.setChildId(childId);
			resetContainers();
			is_saleType_grg_clicked = true;
			onSaleTypeChanged(tag);//回调tag变化,重新调整布局的变化
			clearbtn_DefaultSetting();
			if(drawerLayout != null){//用于把DrawerLayout的scrollView 复位
				drawerLayout.resetScrollView();
			}
			} catch (Exception e) {
				
			}
		}
	};
	private BuyReceiver buyReceiver;
	private Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				clearbtn_DefaultSetting();
				break;
			case 2:
				clearbtn_Setting_1();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 倍投界面的机选一注
	 * @return 
	 */
	public TicketBean randomTicket(){
		ticket = TicketEngine.createTicketByLotteryId(lotteryId);
		childId = randomableChildId;
		salesType = randomableSalesType;
		ticket.setLotteryId(lotteryId);
		ticket.setChildId(randomableChildId);
		ticket.setSalesType(randomableSalesType);
		if(MyApp.order.isIs_dlt_pursue_mode_enabled()){//大乐透的时候特例, 追加模式
			ticket.setChildId("1");
		}
			
		lotteryNums = ticket.getLotteryNums();////System.out.println("lotterynums = "+lotteryNums);
		resetContainers();
		//还原tag
		if("0".equals(randomableChildId)){
			tag = randomableSalesType;
		}else {
			tag = randomableSalesType+","+randomableChildId;
		}
		if(saleType_grg != null)
			saleType_grg.checkRadioButtonById(randomableSaleType_checkedId);
		
		onSaleTypeChanged(tag);
//		toDestroyWM();// 防止出现遗漏 (左边的按钮)
	
		doShake();
		shakeListener.setEnable(false);
		TicketBean createTicket = ticket.createTicket();
		createTicket.setRadioButtonId(randomableSaleType_checkedId);
		clear();
//		todestroyWM();//  防止出现遗漏 (左边的按钮)
		
		
		return createTicket;
	}
	 /**
	  * 拥有 遗漏WM 的需要复写此方法关闭对应的 wm
	  */
//	protected void toDestroyWM() {
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		////System.out.println("lotteryid = "+lotteryId +" : "+" saletype = "+salesType);
		isFragmentCacheEnable = true;
		mBundle = getMyBundle();
//		initSuspendWM();//WindowManager 参数初始化
		
		if(mBundle == null || (mBundle.get("position") == null && !"selfSelect".equals(mBundle.getString("subAction")))){//如果是初始化一个新的玩法,就把 order重新设置
			MyApp.resetOrderBean();
		}
//		if(mBundle != null && mBundle.get("position") != null)
//			initTicket();
//		mListView.setPullRefreshEnable(false);//取消下拉刷新功能
//		mListView.setEnabled(false);//设置不可点击
		
		if(!isCreated || SecondActivity.pop == null){//判断 该fragment是否是刚刚被创建的,还是从cache中获取的,用于保证只需要创建一次的数据 不再重复创建
			//必须加上 popwindow 不为空的判断,否则pop的显示会出现异常,如果secondActivity 是重新创建的,并且pop为空,则为了解决pop显示问题,重新创建 布局
			assignAnnotationValues();//得到 类上面的 注释的值
			setContentView(R.layout.zwl_fg_playdoubleball_main);//购彩主布局,包含底部导航
			initShakeListener();//初始化摇一摇监听事件
			initViews();//初始化主布局和 选号区的 控件
			initListener();//设置主界面及选号区的监听事件
			initSaleType();//初始化 销售方式的 popwindow
		}
		setTitleNav(Integer.parseInt(lotteryId),"购彩", R.drawable.title_nav_back, R.drawable.title_nav_menu);
		
		initTicket();
		shakeListener.start();
		shakeListener.setEnable(false);
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				onBack();
			}
		});
		
		initReceiver();
		resetTitleState();
		mhandler.sendEmptyMessage(1);
		
		String tempStr= SPUtil.getString(R.string.custom_codes);
		if(tempStr.contains(","+lotteryId+",")||tempStr.contains(lotteryId+",")){
			hideRightIcon();//已经定制的 隐藏定制按钮
		}
	} 
	/**
	 * WindowManager 悬浮菜单 已经废弃使用
	 */
//	private void initSuspendWM() {
//    	//获取WindowManager
//    	wm=(WindowManager)MyApp.context.getSystemService("window");//Context.WINDOW_SERVICE
//        //设置LayoutParams(全局变量）相关参数
//    	wmParams = new WindowManager.LayoutParams();
//    	
//        wmParams.type=LayoutParams.TYPE_PHONE;   //设置window type
//        wmParams.format=PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明
//        //设置Window flag
//        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
//                               | LayoutParams.FLAG_NOT_FOCUSABLE;
//        //以屏幕左上角为原点，设置x、y初始值
//        wmParams.x=0;
//        wmParams.y=DensityUtil.dip2px(mActivity, 70);
//        //设置悬浮窗口长宽数据
//        wmParams.width=50;
//        wmParams.height=50;
//		
//	}
    /**
	 * 创建左边悬浮按钮 WindowManager
	 */
//    public void createLeftFloatView(){
//    	todestroyWM();
//    	if(leftbtn_yiloutv==null){
//    		leftbtn_yiloutv=new TextView(mActivity);
//    	}
//    	if(leftbtn_yiloutv2==null){
//    		leftbtn_yiloutv2=new TextView(mActivity);
//    	}
//    	leftbtn_yiloutv2.setBackgroundResource(R.drawable.left_wm_bg);
//    	leftbtn_yiloutv2.setText("显示遗漏");
//    	leftbtn_yiloutv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
//    	leftbtn_yiloutv2.setTextColor(MyApp.res.getColor(R.color.white));
//    	
//    	leftbtn_yiloutv.setBackgroundResource(R.drawable.left_wm_bg);
//    	leftbtn_yiloutv.setText("漏");
//    	leftbtn_yiloutv.setTextColor(MyApp.res.getColor(R.color.white));
//    	leftbtn_yiloutv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
//    	leftbtn_yiloutv.setOnClickListener(new View.OnClickListener() {	
//			public void onClick(View arg0) {
//				todestroyWM();
//				//调整悬浮窗口
//		        wmParams.gravity=Gravity.LEFT|Gravity.BOTTOM;
//		        //设置悬浮窗口长宽数据
//		        wmParams.width=160;
//		        wmParams.height=50;
//		        //显示myFloatView图像
//		        wm.addView(leftbtn_yiloutv2, wmParams);
//		        leftbtn_yiloutv2.invalidate();
//			}
//		});
//    	
//    	leftbtn_yiloutv2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				todestroyWM();
//			   String temStr=leftbtn_yiloutv2.getText().toString().trim();
//			   if("显示遗漏".equals(temStr)){
//				   leftbtn_yiloutv2.setText("隐藏遗漏");
//				   changeViewItemyilouShow(true);
//			   }else{
//				   leftbtn_yiloutv2.setText("显示遗漏");
//				   changeViewItemyilouShow(false);
//			   }
//				
//				//调整悬浮窗口
//		        wmParams.gravity=Gravity.LEFT|Gravity.BOTTOM;
//		        //设置悬浮窗口长宽数据
//		        wmParams.width=50;
//		        wmParams.height=50;
//		        //显示myFloatView图像
//		        wm.addView(leftbtn_yiloutv, wmParams);
//		        leftbtn_yiloutv.invalidate();
//			}
//		});
//    	//调整悬浮窗口
//        wmParams.gravity=Gravity.LEFT|Gravity.BOTTOM;
//        //显示myFloatView图像
//        wm.addView(leftbtn_yiloutv, wmParams);
//        leftbtn_yiloutv.invalidate();
//    }
	public void changeViewItemyilouShow(boolean b) {
	}
//    /**
//     * 关闭 WindowManager
//     */
//	public void todestroyWM(){
//		if(leftbtn_yiloutv!=null && leftbtn_yiloutv.getParent()!=null){
//			wm.removeView(leftbtn_yiloutv);
//		}else if(leftbtn_yiloutv2!=null && leftbtn_yiloutv2.getParent()!=null){
//			wm.removeView(leftbtn_yiloutv2);
//		}
//	}

	/**
	 * 后退是清空 重置 MyApp.order
	 */
	@Override
	protected void onLeftIconClicked() {
		onBack();
	}
 
	private void initReceiver() {
		buyReceiver = new BuyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mitenotc.ui.play.on_lottery_"+lotteryId+"_start_loading");
		filter.addAction("com.mitenotc.ui.play.on_lottery_"+lotteryId+"_stop_loading");
		filter.addAction("com.mitenotc.ui.play.on_lottery_"+lotteryId+"_awardinfo_received");
		mActivity.registerReceiver(buyReceiver, filter);
	}
	/**
	 * 设置 投注界面中的 order 中点击的 Ticket
	 * 投注界面调用该方法后 直接finish 就能直接改变该界面的布局 使得 saletype 和childtype 与选中的ticket一致
	 * @param position order 中点击的 ticket的position
	 */
	public void setOrderClickedTicket(int position){
		Bundle myBundle = getMyBundle();
		if(myBundle == null){
			myBundle = new Bundle();
		}
		myBundle.putInt("position", position);
		mBundle = myBundle;
		mActivity.getIntent().putExtras(myBundle);//把 intent 中的 bundle 换掉
		initTicket();
	}

	protected void initTicket() {
		
		if(mBundle != null && mBundle.get("position") != null){//如果是从 倍投界面返回到该界面的,并且是修改已经生成的 ticket,必须带有 position 参数
			int position = mBundle.getInt("position");
			ticket = MyApp.order.getTickets().get(position);
			salesType = ticket.getSalesType();
			childId = ticket.getChildId();
			lotteryId = ticket.getLotteryId();
			lotteryNums = ticket.getLotteryNums();
			if(saleType_grg != null && ticket.getRadioButtonId() >0)
				saleType_grg.checkRadioButtonById(ticket.getRadioButtonId());
			changeTag();
			
			a:for (int i = 0,j = 0; i < lotteryNums.size(); i++) {//把票中的号 赋值给 选号容器
				for (; j < containers.length; j++) {
					if(containers[j] != null && containers[j].getVisibility() == View.VISIBLE){////System.out.println("j = "+j);
						containers[j].setSelectedBalls(lotteryNums.get(i));
						containers[j].notifyDataSetChanged();
						j++;
						continue a;
					}
				} 
			}
			basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
		}else {//如果不是修改已经生成的 ticket
			customTicket();
		}
	}

	public void customTicket() {
		resetBuyTicket();
		changeTag();
		mhandler.sendEmptyMessage(1);
	}

	/**
	 * 重置选号界面的ticket
	 */
	protected void resetBuyTicket() {
		ticket = TicketEngine.createTicketByLotteryId(lotteryId);
		ticket.setLotteryId(lotteryId);
		ticket.setSalesType(salesType);//默认的销售方式
		ticket.setChildId(childId);
		lotteryNums = ticket.getLotteryNums();
	}

	private String tag = "";
	private void changeTag() {
		
		if("0".equals(childId)){
			tag = salesType;
		}else {
			tag = salesType+","+childId;
		}
		onSaleTypeChanged(tag);
//		String tags = SPUtil.getString(R.string.custom_codes);
//		if(!AppUtil.isEmpty(tags) && (","+tags+",").contains(","+CustomTagEnum.getItemById(Integer.parseInt(lotteryId)).startType+",")){
//			//该模块已经定制  就不需要显示 定制logo 按钮
//			return;
//		}
		showRightIcon();
	}
	
	public void assignAnnotationValues() {
		BaseBuyAnnotation annotation = getClass().getAnnotation(BaseBuyAnnotation.class);
		if(annotation != null){
			lotteryId = annotation.lotteryId();
			salesType = annotation.salesType();
			childId = annotation.childId();
			randomableSalesType = salesType;
			randomableChildId = childId;
		}
	}

	/**
	 * 发送请求--历史数据
	 */
	private void sendRequest(int key){
//		new Thread(){}.start();
		MessageJson msg = new MessageJson();
		switch (key) {
		case 1200:
//			msg.put("A", lotteryId);
//			submitData(0, 1200, msg);
			break;
		case 1201:
			if("130".equals(lotteryId)){// 模拟十一运投注130采用的是 十一运的开奖号码112
				msg.put("A", "112");
			}else{
				msg.put("A", lotteryId);
			}
			msg.put("C","12");
//			fast3_expect_tv.setText(Html.fromHtml("第 -- 期: --"));
			if(fast3_expect_Rlyout!=null && fast3_expect_Rlyout.getVisibility()==View.VISIBLE){
				fast3_expect_Rlyout.setVisibility(View.INVISIBLE);
			}
			
			submitData(0, 1201, msg);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		if(1 == msg.arg1){
			if(messageBean.getLIST() != null && messageBean.getLIST().size()>0){
				String issue2 = CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getIssue();
				if(issue2.length() > 2){
					issue2 = issue2.substring(issue2.length()-2);
				}
				fast3_distance_tv.setText("距"+issue2+"期截止:");
				fast3_distance_tv.setVisibility(View.VISIBLE);
				CustomTagEnum.convertMessage(messageBean.getLIST().get(0));
			}
		}
		switch (msg.arg2) {
		case 1200:
//			issue = messageBean.getLIST().get(0).getD();
			break;
		case 1201:
			
			mList = messageBean.getLIST();
			if(mList != null){
				MessageBean item = mList.get(0);
				String awardNums = item.getE();
				if(awardNums.contains("+")){
					awardNums = awardNums.replace("+", "&#160;&#160;<font color=#007aff>");
					awardNums += "</font>";
				}else if(AppUtil.isEmpty(awardNums)){
					awardNums = "--";
				}

				awardNums = awardNums.replace(",", "&#160;");
				String issueStr = item.getC();
				if(issueStr.length() >2){
					issueStr = issueStr.substring(issueStr.length()-2);
				}else if (AppUtil.isEmpty(issueStr)) {//默认没有获取到数据的处理
					issueStr = "--";
				}
				
				fast3_expect_Rlyout.setVisibility(View.VISIBLE);
				fast3_expect_tv.setText(Html.fromHtml("第"+issueStr+"期: "+awardNums));
				if("121".endsWith(lotteryId) && initInflateTitleView(item)!=null){
					basetitle_leftlayout.removeAllViews();
					basetitle_leftlayout.addView(initInflateTitleView(item));//"第"+issueStr+"期: ",item.getE()  还有等待布局
					basetitle_leftlayout.setVisibility(View.VISIBLE);
//					fast3_expect_Rlyout.removeAllViews();
//					fast3_expect_Rlyout.addView(initInflateTitleView(item));//"第"+issueStr+"期: ",item.getE()
				}
//				fast3_await_cb.setVisibility(View.GONE);// 关闭等待
				if(baseAwait_layout!=null && baseAwait_layout.getVisibility()==View.VISIBLE){
					baseAwait_layout.setVisibility(View.GONE);
				}
				if(basetitle_leftlayout!=null && basetitle_leftlayout.getVisibility()==View.INVISIBLE){
					baseAwait_layout.setVisibility(View.VISIBLE);
				}
//				fast3_expect_tv.setVisibility(View.VISIBLE);
//				fast3_time_tv.setVisibility(View.VISIBLE);
//				fast3_expect_Rlyout.setVisibility(View.VISIBLE);
				
				BaseBuyAdapter baseBuyAdapter = customBuyAdaper();
				for (int i=0; i< mList.size();i++) {
					if(basebuy_ll_history.getChildAt(i) == null){
						basebuy_ll_history.addView(baseBuyAdapter.getView(i, basebuy_ll_history.getChildAt(i), null));
					}else {
						baseBuyAdapter.getView(i, basebuy_ll_history.getChildAt(i), null);
					}
				}
			        
			}
			break;
		}
	}
	
	/**当子类的历史记录布局不是默认的布局时,复写该方法来完成历史记录布局的显示 
	 * 用于子类复写
	 * 复写该方法 返回一个 BaseBuyAdapter 的子类, 复写该adapter 中的 getView 方法. 不同的玩法有不同的历史记录布局
	 * @return
	 */
	public BaseBuyAdapter customBuyAdaper(){
		return new BaseBuyAdapter();
	}

//	  子类根据具体需求添加  历史开奖号码个性化显示View     --- TODO
	public View initInflateTitleView(MessageBean msgb){
		return null;
	 };//int resource, ViewGroup root
	public class BaseBuyAdapter extends BaseListAdapter{
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder holder = null;
			Viewholder_item item_holder = null;
			View itemView = null;
			
			if(convertView == null ){
				holder=new Viewholder();
				convertView=mInflater.inflate(R.layout.m_lv_history_msg_item, null);
				holder.tv1=(TextView) convertView.findViewById(R.id.it_periods_tv);
				holder.tv2=(TextView) convertView.findViewById(R.id.it_history_number_tv);
				holder.Rlayout=(RelativeLayout) convertView.findViewById(R.id.history_number_rLayout);
				
				convertView.setTag(holder);
			}else{
				holder=(Viewholder) convertView.getTag();
			}
			
			MessageBean item = mList.get(position);
			holder.tv1.setText(item.getC()+"期");//历史期号
			String awardNums = item.getE();
//			if(item.getA() != null && "121".equals(item.getA()) && item.getE()!=null && !"".equals(item.getE())){//-------TODO 后期 需要抽取
//					holder.Rlayout.removeAllViews();
//					if(itemView==null){
//						item_holder=new Viewholder_item();
//						itemView=mInflater.inflate(R.layout.history_121,null);
//						
//						item_holder.num_tv1=(TextView) itemView.findViewById(R.id.num_tv1);
//						item_holder.num_tv2=(TextView) itemView.findViewById(R.id.num_tv2);
//						item_holder.num_tv3=(TextView) itemView.findViewById(R.id.num_tv3);
//						item_holder.text_tv=(TextView) itemView.findViewById(R.id.text_tv);
//						holder.Rlayout.addView(itemView);
//						
//						itemView.setTag(item_holder);
//					}else 
//					{
//						item_holder=(Viewholder_item) itemView.getTag();
//					}
//					
//					String[] numStr=awardNums.split(",");//  扑克的整体数据  ：111,102,302
//					String[] newNumStr=new String[3]; //拆分出的扑克对应text（后两位是text, 前一位数字表示的是 扑克花色)
//					
//					if(numStr.length==3){//为了安全  加以判断 
//						
//						for (int i = 0; i < numStr.length; i++) 
//						{// 拆分出具体扑克  text
//							newNumStr[i]=numStr[i].substring(1, 3);
//						}
//						String tempStr="";
//					   //  排序 小---> 大  为了便于 判断是否为 顺子扑克牌
//						for (int i = 0; i < newNumStr.length; i++) {
//							for (int j = 0; j < newNumStr.length; j++) {
//								if(Integer.parseInt(newNumStr[i]) < Integer.parseInt(newNumStr[j])){
//									tempStr = newNumStr[i];
//									newNumStr[i]=newNumStr[j];
//									newNumStr[j]=tempStr;
//								}
//							}
//						}
//						
//						String pk_type="";//  数据显示 的特殊扑克的提示text
//						if( numStr[0].substring(0, 1).equals(numStr[1].substring(0, 1)) &&
//							numStr[1].substring(0, 1).equals(numStr[2].substring(0, 1))){
//							//  同花（判断每个号码的第一位数字是否相同 决定是否为同花）
//									pk_type="同花";
//									
//									//	同花顺： 是指在同花的前提下顺子
//						    	if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
//						    	   Integer.parseInt(newNumStr[1])+1 == Integer.parseInt(newNumStr[2])){// 普通顺子
//						    		pk_type="同花顺";
//						    	}else if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
//						    			1==(Integer.parseInt(newNumStr[1]) - Integer.parseInt(newNumStr[2]))){ // AQK  特殊顺子的判断
//						    		pk_type="同花顺";
//						    	}
//						    	
//						}else{//非同花
//							
//							if(newNumStr[0].equals(newNumStr[1]) && newNumStr[1].equals(newNumStr[2])){//  豹子
//								pk_type="豹子";
//							}else if(newNumStr[0].equals(newNumStr[1]) || newNumStr[1].equals(newNumStr[2])){//对子
//								pk_type="对子";
//						    }else {
//						    	if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
//						    	   Integer.parseInt(newNumStr[1])+1 == Integer.parseInt(newNumStr[2])){// 普通顺子
//						    		pk_type="顺子";
//						    	}else if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
//						    			1==(Integer.parseInt(newNumStr[1]) - Integer.parseInt(newNumStr[2]))){ // AQK  特殊顺子的判断
//						    		pk_type="顺子";
//						    	}
//						    }
//							
//					   }
//					//  划分对子  豹子    顺子  同花顺
//						for (int i = 0; i < numStr.length; i++) 
//						{
//							if(numStr[i].length() == 3)
//							{
//									switch (i) 
//									{
//										case 0:
//											inputBackgroundId(item_holder.num_tv1,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
//											break;
//										case 1:
//											inputBackgroundId(item_holder.num_tv2,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
//											break;
//										case 2:
//											inputBackgroundId(item_holder.num_tv3,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
//											break;
//										default:
//											break;
//									}
//							}
//						}
//						item_holder.text_tv.setText(pk_type);//  开奖扑克种类的提示
//					}
//					
//				}else {}
			if(awardNums.contains("+")){
				awardNums = awardNums.replace("+", "&#160;&#160;<font color=#007aff>");
				awardNums += "</font>";
			}
			awardNums = awardNums.replace(",", "&#160;&#160;");
			holder.tv2.setText(Html.fromHtml(awardNums));//历史期号对应的历史投注号码
			return convertView;
		}
		
		
	}
	// 设置扑克牌的背景（具体扑克  花色）
	private void inputBackgroundId(TextView tv,String id,String textid) {
//			 背景
		switch (Integer.parseInt(id)) {
		case 1://1黑桃
			tv.setBackgroundResource(R.drawable.pk_1);
			break;
		case 2://2红桃
			tv.setBackgroundResource(R.drawable.pk_2);
			break;
		case 3://3梅花
			tv.setBackgroundResource(R.drawable.pk_3);
			break;
		case 4://4方块。
			tv.setBackgroundResource(R.drawable.pk_4);
			break;
		
		default:
			break;
		}
//			字符text
		int temNum=Integer.parseInt(textid);
		switch (temNum) {
		case 1:
			tv.setText("A");
			
			break;
		case 11://J
			tv.setText("J");
			break;
		case 12://Q
			tv.setText("Q");
			break;
		case 13://K
			tv.setText("K");
			break;
		default:
			tv.setText(temNum+"");  // 号码异常情况下
			break;
		}
	}

	
  class Viewholder{
	   TextView tv1;//历史期数
	   TextView tv2;//历史号码
	   RelativeLayout Rlayout;
   }
  class Viewholder_item{
	  TextView num_tv1;
	  TextView num_tv2;
	  TextView num_tv3;
	  TextView text_tv;
  }

	private void initViews() {
		baseBottom_layout = (LinearLayout) findViewById(R.id.ssp_bottom_ll);
		basebuy_ll_history = (LinearLayout) findViewById(R.id.basebuy_ll_history);
		basetop_line=findViewById(R.id.basetop_line);
//	    --TODO	为了保证类似快乐扑克不同风格的Title布局需求此方式废弃
		fast3_await_cb = (TextView) findViewById(R.id.fast3_await_cb);
		fast3_time_tv = (TextView) findViewById(R.id.fast3_time_tv);
		fast3_expect_tv = (TextView) findViewById(R.id.fast3_expect_tv);
		drop_down_d = (TextView) findViewById(R.id.drop_down_d);
		basetitle_leftlayout = (RelativeLayout) findViewById(R.id.basetitle_leftlayout);
		fast3_expect_Rlyout = (RelativeLayout) findViewById(R.id.fast3_expect_Rlyout);
		baseTitle_reightLayout = (LinearLayout) findViewById(R.id.baseTitle_reightLayout);
		baseAwait_layout = (LinearLayout) findViewById(R.id.baseAwait_layout);
		fast3_distance_tv = (TextView)findViewById(R.id.fast3_distance_tv);
		
		fast3_title_Layout = (LinearLayout) findViewById(R.id.fast3_title_Layout);
		
		//主布局底部
		basebuy_tv_notice=(TextView) findViewById(R.id.inall_inform_tv);//把id改成 basebuy_tv_notice
		basebuy_btn_clear=(Button) findViewById(R.id.clear_gv_btn);//basebuy_btn_clear
		basebuy_btn_ok=(Button) findViewById(R.id.affirm_next_btn);//basebuy_btn_ok
//		yilou_tv=(TextView) findViewById(R.id.yilou_tv);//basebuy_btn_ok
//		fast3_distance_tv = (TextView)fast3_title_Layout.findViewById(R.id.fast3_distance_tv);
		
		containers = new MGridView[7];//默认为最大为7个,这样就写死了可以在 initContainers里面复写
		drawerLayout=(DrawerLayout) findViewById(R.id.ssp_bat_main_drawwerLayout);//自定义控件
		drawerLayout.removeAllViews();
//		view=View.inflate(mActivity, R.layout.zwl_play_dballcommon, null);//主要的彩种布局,根据每个彩种的不同,分别设置的布局//最好用一个回调方法
		lotteryView = customLotteryView();
		drawerLayout.addView(lotteryView);//加载布局
		
		//获取网络数据按钮
		again_get_hmsg_btn=(Button) findViewById(R.id.again_get_hmsg_btn);
		//以下 摇一摇的此按钮必须在customLotteryView() 加载完之后调用否则Caused by: java.lang.NullPointerException-----TODO
		basebuy_btn_shake=(Button) findViewById(R.id.common_btn);//把 id 改成: basebuy_btn_shake
		basebuy_tv_shake = (TextView) findViewById(R.id.drag_code_tv);//把 id 改成: basebuy_tv_shake
		basebuy_rl_shake = (RelativeLayout) findViewById(R.id.reLayout);//把 id 改成: basebuy_rl_shake
	    
//		initHistoryListView();
//		basebuy_ll_history.addView(historyListView);
		FormatUtil.setTypeface(fast3_time_tv, 0); //时间设置子字体
	    initContainers();//在containers 初始化完成后,初始化 相应saleType下的布局的显示
//	    onSaleTypeChanged(salesType);
	    initActionUpListener();
	}
	/**
	 * 类似快乐扑克独特的下底部需求
	 * @param drawable
	 * @param drawable2
	 * @param resid
	 */
	protected void setBottomBackgroundResource(Drawable clearDrawable ,Drawable neatDrawable,int resid,int textcolorId) {
		View v=(View) baseBottom_layout.getParent();
		if(v!=null){
				v.setBackgroundColor(MyApp.res.getColor(resid));
				baseBottom_layout.setBackgroundColor(MyApp.res.getColor(resid));
				basebuy_btn_clear.setBackgroundColor(MyApp.res.getColor(resid));
				basebuy_btn_ok.setBackgroundColor(MyApp.res.getColor(resid));
				
				clearDrawable.setBounds(0, 0,clearDrawable.getMinimumWidth(),clearDrawable.getMinimumHeight());
				basebuy_btn_clear.setCompoundDrawables(clearDrawable,null , null,null);
				neatDrawable.setBounds(0, 0,neatDrawable.getMinimumWidth(),neatDrawable.getMinimumHeight());
				basebuy_btn_ok.setCompoundDrawables(neatDrawable,null , null,null);
				basebuy_btn_clear.setTextColor(MyApp.res.getColor(textcolorId));
				basebuy_btn_ok.setTextColor(MyApp.res.getColor(textcolorId));
				basebuy_tv_notice.setTextColor(MyApp.res.getColor(textcolorId));
		}
	}
	
	/**
	 * 针对 界面UI的需求 需要变化 设置清空按钮的背景色
	 * @param btn
	 */
	public void changeBtnbgAndTextColors(int i){
		if(basebuy_btn_clear!=null){
			if("white".equals(basebuy_btn_clear.getTag()) || i > 0 ){
				clearbtn_Setting_1();
			}else{
				clearbtn_DefaultSetting();
			}
		}
	}
	/**
	 * 清空按钮 变成灰色选择器
	 */
	public void clearbtn_DefaultSetting(){
		if(basebuy_btn_clear!=null){
			basebuy_btn_clear.setTag("white");
//			basebuy_btn_clear.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.btn_bg_gray_selector));
//			basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.white));
//			basebuy_btn_clear.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_btn_white_m));
//			basebuy_btn_clear.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.btn_white_or_gray_selector));
			
			basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.login_color_4));
			basebuy_btn_clear.setPadding(10, 10, 10, 10);
			basebuy_btn_clear.setGravity(Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL);
			basebuy_btn_clear.setText("清 空");
			basebuy_btn_clear.invalidate();
		}
	}
	/**
	 * 清空按钮 变成橘黄色选择器
	 */
	public void clearbtn_Setting_1(){
		if(basebuy_btn_clear!=null){
			basebuy_btn_clear.setTag("orange");
//			basebuy_btn_clear.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.btn_bg_orange_selector));
//			basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.white));
			
			basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.btn_orange_u));
			basebuy_btn_clear.setPadding(10, 10, 10, 10);
			basebuy_btn_clear.setGravity(Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL);
			basebuy_btn_clear.setText("清 空");
			basebuy_btn_clear.invalidate();
		}
	}
	@Deprecated
	private void initHistoryListView() {
//		historyListView = new ListView(mActivity);
//		historyListView.setLayoutParams(new LayoutParams(-1, -1));
//		historyListView.setVerticalScrollBarEnabled(false);//不显示滚动条
//		historyListView.setFadingEdgeLength(0);//删除上线的黑边
//		historyListView.setBackgroundColor(Color.TRANSPARENT);
//		historyListView.setCacheColorHint(0);
//		historyListView.setDrawingCacheEnabled(false);
//		historyListView.setSelector(new ColorDrawable(0x00000000)); //设置item的点击事件的背景
	}

	/**
	 * 初始化 popwindow 和 popwindow 的contentview
	 */
	private void initSaleType() {
		content_saleType = customContent_saleType();
		if(content_saleType != null){
			saleType_grg  = (GridRadioGroup) content_saleType.findViewById(R.id.saleType_grg);
			pop_saleType = new PopupWindow(content_saleType, -1, -2);
			pop_saleType.setBackgroundDrawable(new BitmapDrawable());
			pop_saleType.setOutsideTouchable(true);
			pop_saleType.setOnDismissListener(new OnDismissListener() {//在pop dismiss的时候把标题的checked状态改回到false
				@Override
				public void onDismiss() {
					last_pop_time = System.currentTimeMillis();
					resetTitleState();
				}
			});
			saleType_grg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
//					if(pop_saleType.isShowing())
//						pop_saleType.dismiss();
//					resetTitleState();
					Message msg = Message.obtain();
					msg.arg1 = checkedId;
					msg.obj = group;
					handler.sendMessage(msg);//上面的3行代码 由于考虑到系统 原因,放到handler中执行. 如果没有放在handler中,小米手机会出现异常
				}
			});
		}
	}
	private void resetContainers(){
		for (int i = 0; i < containers.length; i++) {
			if(containers[i] != null){
				containers[i].clear();
			}
		}
	}
	
	/**
	 * 把ticket中的lotteryNums 重新加载
	 * @param nums 要加载的container的角标
	 */
	public void reloadLotteryNums(int... nums){
		//加载数据之前, 首先调整可见性
		for (MGridView container : containers) {//设置所有的container不可见
			if(container != null)
				container.setVisibility(View.GONE);
		}
		if(mBundle == null || mBundle.get("position") == null || is_saleType_grg_clicked)//不是从投注界面返回的情况下就清空 lottryNums, 或者是 用户主动切换的情况下
			lotteryNums.clear();
		for (int i = 0; i < nums.length; i++) {
			if(containers[nums[i]] != null){
				containers[nums[i]].setVisibility(View.VISIBLE);//设置 需要的container 可见
				if(mBundle == null || mBundle.get("position") == null || is_saleType_grg_clicked)//不是从投注界面返回的情况下就清空 lottryNums
				lotteryNums.add(containers[nums[i]].getSelectedBalls());
			}
		}
		is_saleType_grg_clicked = false;
	}
	/**
	 * 重新加载购彩的布局,该方法是对 reloadLotteryNums 和 changeShakeVisibility 两个方法的整合.
	 * 主要用于 onSaleTypeChanged 方法的调用. 当销售模式发生变化的时候, 重新加载 ticket 中的数据, 重新调整布局的可见性
	 * @param isShake
	 * @param nums
	 */
	public void reload(boolean isShake, int... nums){
		changeShakeVisibility(isShake);
		reloadLotteryNums(nums);
	}

	/**
	 * 设置 互斥 选号区
	 * @param nums
	 */
	public void mutualContainers(int... nums){
		for (int i = 0; i < nums.length; i++) {
			MGridView[] mutualViews = new MGridView[containers.length];
			for (int j = 0; j < nums.length; j++) {
				if(i != j){
					mutualViews[j] = containers[nums[j]]; 
				}
			}

			if(containers[nums[i]] != null)
				containers[nums[i]].setMutualViews(mutualViews);
		}
	}
	/**
	 * 取消所有的互斥选号区
	 */
	public void cancelMutualContainers(){
		for (int i = 0; i < containers.length; i++) {
			if(containers[i] != null)
				containers[i].setMutualViews(null);
		}
	}
	
	//**************************子类继承时需要复写的方法 start**************************
	//具体的事例参照Buy_ssqFragment
	/**
	 * 子类复写该方法类设置 选号区的初始化
	 */
	protected abstract void initContainers(); /*{//当container或者该控件的父控件不可见时一定要把该container的 visible设为gone
		//初始化每一个container的机选的最少标准球数
		containers[0].setMiniNum(6);
		containers[1].setMiniNum(6);
		containers[2].setMiniNum(1);
		
		//初始化每一个容器的样本数据
		List<Integer> sampleNums = new ArrayList<Integer>();
		for (int i = 1; i <= 16;i++ ) {
			sampleNums.add(i);
		}
		containers[2].setSampleNums(sampleNums);
		containers[2].setBallSRCId(R.drawable.pop_ball_blue);
		containers[2].setTextColorId(getResources().getColor(R.color.blue));
		
	}*/



	/**
	 * 该方法至少一个示例,主要在 initContainers 和onSaleTypeChanged 方法中使用
	 * @param index
	 * @param isVisible
	 */
	@Deprecated
	private void toogleContainer(int index,boolean isVisible) {
		containers[index].setShakeble(isVisible);
		if(isVisible){
			containers[index].setVisibility(View.VISIBLE);
//			containersParents[index].setVisibility(View.VISIBLE);
		}else{
			containers[index].setVisibility(View.GONE);
//			containersParents[index].setVisibility(View.GONE);
		}
	}
	
	/**
	 *子类需要复写该方法来改变彩种的布局
	 * @param v
	 * @return
	 */
	protected abstract View customLotteryView(); /*{//默认为双色球
		return View.inflate(mActivity, R.layout.zwl_play_dballcommon, null);
	}*/
	
	/**
	 * 子类复写该方法来设置pop_saleType的contentview
	 * @return
	 */
	protected abstract View customContent_saleType();/* {//默认为双色球的普通投注和胆拖投注
		return View.inflate(mActivity, R.layout.f_sale_type_ssq, null);
	}*/
//	/**
//	 * 子类复写该方法来设置pop_yiLouCut 的contentview
//	 * @return
//	 */
//	protected abstract View customContent_yiLouCut();
	/**
	 * 子类复写该方法
	 * 销售方式发生变化的时候回调该方法来改变选号区的布局
	 * @param tag
	 */
	protected abstract void onSaleTypeChanged(String tag);
	//**************************子类继承时需要复写的方法 end**************************
	/**
	 * 对摇一摇事件的抽取
	 */
	private void initShakeListener() {
		shakeListener = new ShakeListener(mActivity);
		shakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				doShake();
				changeBtnbgAndTextColors(2);// 直接指定
			}
		});
	}
	/**
	 * 摇一摇随机选号
	 */
	protected void doShake() {
		lotteryNums.clear();
		for (int i = 0; i < containers.length; i++) { 
			MGridView container = containers[i];
			if(container != null && container.getVisibility() == View.VISIBLE ){//是否设置为摇晃获取随机数&& container.isShakeble()
				lotteryNums.add(container.randomSelect());//最好在最后点确定的时候复制
			}
		}
		basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
	}
	
	private void initListener() {
//		 drop_down_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//				if(isChecked){
////					MyToast.showToast(mActivity, "上拉!");
//					drawerLayout.scrollTo(0, 0);
//				}else{
////					 MyToast.showToast(mActivity, "下拉!");
//					 drawerLayout.scrollTo(0, -300);
//				}
//			}
//		});
		 drop_down_d.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showOrup();
			}
		});

		basebuy_btn_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clear();
				mhandler.sendEmptyMessage(1);
			}
		});
		basebuy_btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ok();
			}
		});
		again_get_hmsg_btn.setOnClickListener(new OnClickListener() {//获取网络数据
			@Override
			public void onClick(View v) {
				sendRequest(1201);
			}
		});
		basebuy_btn_shake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doShake();
				shakeListener.vibrate(100);
				clearbtn_Setting_1();
			}
		});
		fast3_title_Layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showOrup();
			}
		});
	}
	/**
	 * 展开 或收起 历史开奖号码
	 */
	public void showOrup() {
		Drawable mDrawable=null;
		if(isScroll){
			 isScroll=false;
			 drawerLayout.scrollTo(0, -300);
			  mDrawable=MyApp.res.getDrawable(R.drawable.drop_down_u);
			  mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
						mDrawable.getMinimumHeight());
			 drop_down_d.setCompoundDrawables(null, null,mDrawable, null);
		}else{
			isScroll=true;
			drawerLayout.scrollTo(0, 0);
			 mDrawable=MyApp.res.getDrawable(R.drawable.drop_down_d);
			  mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
						mDrawable.getMinimumHeight());
			 drop_down_d.setCompoundDrawables(null, null,mDrawable, null);
		}
	}

	/**
	 * 清空按钮的抽取
	 */
	protected void clear(){
		for (MGridView container : containers) {
			if(container != null)
				container.clear();
		}
		basebuy_tv_notice.setText("");
	}
	/**
	 * 子类可以复写该方法 来更改 确认按钮的逻辑.
	 * 一般情况下不用更改
	 */
	protected void ok(){
		if(ticket.isTicketAvailable()){
			if(mBundle != null && mBundle.get("position") != null){//当 该票是从 倍投 界面返回到该界面来修改的时候,只要把 ticket 中的 lotteryNums 重新生成一次
				ticket.createTicket();
				mBundle.remove("position");
			}else {//如果该票不是从 倍投界面返回修改的,则要加到 订单 中
				MyApp.order.getTickets().add(0,ticket.createTicket());
			} 
			
			if(saleType_grg != null)
				ticket.setRadioButtonId(saleType_grg.getCheckedId());
			MyApp.order.setLotteryId(lotteryId);
			MyApp.order.setIssue(CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getIssue());
//			getFragmentManager().beginTransaction().remove(this).commit(); //不用移除了,因为改成该界面在投注界面里finish
//			FragmentManager fm = getFragmentManager();
//			Bundle bundle = new Bundle();
//			fm.putFragment(bundle, "buyfragment", this);
			start(ThirdActivity.class,SsqBetorder.class,null);
//			finish();//放到第二个界面去finish,为了机选一注的使用
			is_clear_containers_required = true;
		}else {
			Toast.makeText(mActivity, "请至少选择一票!", 0).show();
		}
	}
	
	/**
	 * 对ActionUp事件的 抽取
	 */
	protected void initActionUpListener(){
		for (int i = 0; i < containers.length; i++) {
			if(containers[i] != null)
			containers[i].setActionUpListener(new ActionUpListener() {
				@Override
				public void onActionUp() {//System.out.println("ticket ================== "+ticket.getClass().getName());
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();
					int temp=0;
					for (int j = 0; j < ticket.getLotteryNums().size(); j++) {
						temp += ticket.getLotteryNums().get(j).size();
					}
					mhandler.sendEmptyMessage(2);
					if(0==temp){
						mhandler.sendEmptyMessage(1);
					}
				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		is_clear_containers_required = false;
	}
	@Override
	public void onStop() { 
		super.onStop();
		if(is_clear_containers_required){
			clear();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(shakeListener != null){
			if("2".equals(tag)||"2".equals(tag.subSequence(0, 1))){
				shakeListener.setEnable(false);
			}else{
				shakeListener.setEnable(true);
			}
		}
		
		if(content_saleType != null){//如果有多种不同的销售方式,通过设置pop_saleType 不为空来显示不同的销售方式
			initPop_saleType();
		}
		sendRequest(1201);//更新 历史开奖记录
//		sendRequest(1200);//查询期次信息
		if(baseAwait_layout!=null && baseAwait_layout.getVisibility()==View.GONE){
			show(baseAwait_layout);//未获得期号前显示等待开奖
			basetitle_leftlayout.setVisibility(View.INVISIBLE);
		}
		
		//开启跑秒//如果跑秒需求的话,跑秒的条目可见,则开启跑秒
		if(fast3_title_Layout.getVisibility() == View.VISIBLE){
			CustomTagEnum item =null;
			if("130".equals(lotteryId)){
				item= CustomTagEnum.getItemByLotteryId(112);
			}else{
				item= CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId));
			}
			if(timerMap == null){
				timerMap = new HashMap<TextView, CustomTagEnum>();
				timerMap.put(fast3_time_tv, item);
			}
//			////System.out.println("timermap.size = "+timerMap.size());
			String issue2 = item.getIssue();
			if(AppUtil.isEmpty(issue2))
				return;
			if(issue2.length() > 2){
				issue2 = issue2.substring(issue2.length()-2);
			}
			fast3_distance_tv.setText("距"+issue2+"期截止:");
			CustomTagEnum.startTimer(this, timerMap,1);
			if(baseAwait_layout!=null && baseAwait_layout.getVisibility()==View.VISIBLE){
				baseAwait_layout.setVisibility(View.GONE);
			}
			basetitle_leftlayout.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(shakeListener != null)
			shakeListener.setEnable(false);
		isCreated = true;
		
		CustomTagEnum.stopTimer();//关闭跑秒
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		ViewGroup parent = (ViewGroup) mView.getParent();
		parent.removeAllViews();//如果没有该行代码,会出现 子控件已经有父类的异常
		shakeListener.stop();
		clear();
		if(buyReceiver != null){
			mActivity.unregisterReceiver(buyReceiver);
			buyReceiver = null;
		}
		//把下拉的 drawer 复位
		drawerLayout.resetScroll();
		
//		todestroyWM();
	}


	private void initPop_saleType() {
		setTitleCheckedChangedListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(System.currentTimeMillis() - last_pop_time > 300)
						pop_saleType.showAsDropDown(buttonView, 0, 0);
					else
						resetTitleState();
				}else {
					if(pop_saleType.isShowing())
						pop_saleType.dismiss();
				}
			}
		});
	}
	/**
	 * 设置摇一摇 条目的提示内容
	 * @param text
	 */
	public void setShakeNotice(String text){
		if(basebuy_tv_shake != null)
			basebuy_tv_shake.setText(text);
	}
   //    ------------TODO  万利 添加 5-5
	public void setShakeNotice(Spannable textSpad){
		if(basebuy_tv_shake != null)
			basebuy_tv_shake.setText(textSpad);
	}
	/**
	 * 设置shake 条目的可见性,同时改变 摇一摇传感器的 开和关
	 * @param isVisible
	 */
	public void changeShakeVisibility(boolean isVisible){
		if(isVisible){
			shakeListener.setEnable(true);
			 basebuy_rl_shake.setVisibility(View.VISIBLE);
			 randomableSalesType = salesType;//设置机选一注的 salesType 和 childId;
			 randomableChildId = childId;
			 if(saleType_grg != null){
				 randomableSaleType_checkedId = saleType_grg.getCheckedId();
			 }
		}else {
			shakeListener.setEnable(false);
			basebuy_rl_shake.setVisibility(View.GONE);
		}
	}
	
	private void onBack() {//****************ck 更改******************
		int count = 0;//当前选号容器 选中的球的数量,如果为0 则表示用户没有选择号码
		for (List<Integer> lotteryNum : lotteryNums) {
			count += lotteryNum.size();
		}
		if(count == 0 && MyApp.order.getTickets().size() == 0){//当前的选号容器为空, 且订单中的 票为空
			MyApp.resetOrderBean();
			finish();//直接退出
			return;
		}
		final TCDialogs dialog=new TCDialogs(mActivity);
		dialog.popDeleteConfirm("提  示","清 除 选 号?",new MyClickedListener() {
			@Override
			public void onClick() {
//				MyApp.saveProbeMsg("弹出提示-清除选号?","查看-点击-取消", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
				dialog.dismiss();
			}
		}, new  MyClickedListener() {
			@Override
			public void onClick() {
				dialog.dismiss();
//				MyApp.resetOrderBean();
				
				mActivity.finish();//先跳转后清空
				MyApp.resetOrderBean();
//				MyApp.saveProbeMsg("弹出提示-清除选号?","查看-点击-确认", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
			}
		});
	}
	
	/**
	 *顶部 右上角 按钮点击事件  
	 */
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,Integer.parseInt(lotteryId));
		setTitleNav(Integer.parseInt(lotteryId),mActivity.getTitleText(), R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}
	/**
	 * 高频头部的数据接收处理 receiver
	 * @author mitenotc
	 */
	class BuyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("121----intent.getAction()"+intent.getAction());
			if(("com.mitenotc.ui.play.on_lottery_"+lotteryId+"_start_loading").equals(intent.getAction())){
				fast3_expect_tv.setText(getString(R.string.fast3_tv_1));
//				fast3_await_cb.setText(getString(R.string.fast3_tv_2));
//				show(fast3_await_cb);
				show(baseAwait_layout);
				fast3_expect_Rlyout.setVisibility(View.INVISIBLE);//  不显示 特殊title布局未获得更新后期号前
				
			}else if(("com.mitenotc.ui.play.on_lottery_"+lotteryId+"_stop_loading").equals(intent.getAction())){
				String issue2 = CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getIssue();
				if(issue2.length() > 2){
					issue2 = issue2.substring(issue2.length()-2);
				}
				fast3_distance_tv.setText("距"+issue2+"期截止:");
				hide(baseAwait_layout);
				show(fast3_expect_Rlyout);
			}else if (("com.mitenotc.ui.play.on_lottery_"+lotteryId+"_awardinfo_received").equals(intent.getAction())) {////System.out.println("on awardinfo received ============= ");
				sendRequest(1201);//获取历史信息
				hide(baseAwait_layout);
				show(fast3_expect_Rlyout);
			}
		}
	}
	
	public void setSecondLayerBackground(int resid){
		drawerLayout.setSecondLayerBackground(resid);//  界面底部button 更新
//		mView.setBackgroundResource(resid);
	}
}

