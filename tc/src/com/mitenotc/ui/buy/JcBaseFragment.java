package com.mitenotc.ui.buy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.mitenotc.bean.JcBfBean;
import com.mitenotc.bean.JcTeamInfo;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.engine.ticket.TicketEngine;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.adapter.JCListAdapter;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.play.JczqBetorder;
import com.mitenotc.ui.play.SsqBetorder;
import com.mitenotc.ui.ui_utils.GridRadioGroup;
import com.mitenotc.ui.ui_utils.JCListView;
import com.mitenotc.ui.ui_utils.ShakeListener;
import com.mitenotc.ui.ui_utils.ShakeListener.OnShakeListener;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.SPUtil;

public abstract class JcBaseFragment extends BaseFragment {
	private boolean isCreated;//用于标记该fragment是否已经创建,如果已经创建,则基本的布局的加载不需要重新加载一次
    protected JCListAdapter mjcAdapter;
	protected Bundle mBundle;//数据传递用的bundle
	protected JCListView home_expandable_Lv;
	protected TicketBean ticket;//选号的 ticket,与接口相对应
	protected JcBfBean bfbean;
	protected View lotteryView;//彩种布局
	protected View content_saleType;//销售方式的view,用于pop_saleType的 content//如果为空则pop_saleType 不会被设置
	private PopupWindow pop_saleType;
	private GridRadioGroup saleType_grg;
	private long last_pop_time;//用于记录popwindow最后一次dismiss的时间
	private boolean is_saleType_grg_clicked;//用于标记 子玩法切换时是用户的选择还是程序中的需求
	protected RelativeLayout home_jc_lotteryView;//彩种布局
	private LinearLayout jc_list_header;// 列表头部布局
	protected String lotteryId = "0";//玩法代码
	protected String childId = "0";	//	子玩法代码
	protected String salesType = "";//默认是竞彩足球的  胜平负
	//主要是用于区分非跳转状态下该页面的 onstart 和 onstop 被调用的情况. 因为clear方法需要在onstop 中调用
	protected boolean is_clear_containers_required;
	
	protected ShakeListener shakeListener;
	private String randomableSalesType;//用于记录可以机选的销售方式. 该变量主要是用于倍投界面的机选
	private String randomableChildId;//用于记录可以机选的子玩法代码. 该变量主要是用于倍投界面的机选
	private int randomableSaleType_checkedId;// 用于记录可以机选的 子玩法的radiobutton 的 id
	//底部导航
	protected TextView basebuy_tv_notice;
	protected Button basebuy_btn_clear;
	private   Button basebuy_btn_ok;
//	protected   GridRadioGroup	sort_layout;
//	protected   RadioButton cg_btn, dg_btn;
	
    protected   List<String> lotteryScene=new LinkedList<String>(); //竞彩所选展开标记
    protected   Map<String,List<String>>  gameNameMap=new HashMap<String, List<String>>();//赛事分类
    protected   Dialog mDialog;
    
    protected RelativeLayout rl_null_saishi;
    
	protected   Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
				/**把 原来在 GridRadioGroup 的onCheckedChanged 中的操作放到handler中,主要考虑小米系统的兼容**/
				if(pop_saleType.isShowing())
					pop_saleType.dismiss();
				resetTitleState();
				RadioGroup group = (RadioGroup) msg.obj;
				int checkedId = msg.arg1;
				if(saleType_grg.getCurrentCheckedId() == checkedId){
					return;
				}
				if(0==checkedId){//为零情况下 采取默认选中
					checkedId=saleType_grg.getCurrentCheckedId();
				}
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				if(rb.getTag() == null)//安全性考虑,rb不会是空的,同时 tag也不能为空
					return;
				setTitleText(rb.getText().toString());//改变不同的销售方式的时候更改头部导航的标题文字
				
				System.out.println("221--->"+rb.getText().toString());
				salesType=rb.getTag().toString();// 销售方式
				ticket.setSalesType(salesType);//默认的销售方式 对于竞彩来说就是玩法id
				MyApp.order.setLotteryId(salesType);//这里实际传递的是 salesType 即竞彩的 玩法
				initTicket();
				setNotice();
				changeBtnbgAndTextColors(ticket.getSession());
				onSaleTypeChanged(salesType+","+childId);//回调tag变化,重新调整布局的变化
		}
	};
	/**
	 * 清空按钮 
	 * 针对 界面UI的需求 需要变化 
	 * 设置清空按钮的背景色
	 * @param btn
	 */
	public void changeBtnbgAndTextColors(int i){
		if(basebuy_btn_clear!=null){
			basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.login_color_4));
			basebuy_btn_clear.setPadding(10, 10, 10, 10);
			basebuy_btn_clear.setGravity(Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL);
			basebuy_btn_clear.setText("清 空");
			basebuy_btn_clear.invalidate();
			if( i > 0 ){//是否需要提醒 清空
				basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.btn_orange_u));
				basebuy_btn_clear.setPadding(10, 10, 10, 10);
				basebuy_btn_clear.setGravity(Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL);
				basebuy_btn_clear.setText("清 空");
				basebuy_btn_clear.invalidate();
			}
		}
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		isFragmentCacheEnable = true;
		if(!isCreated){
			assignAnnotationValues();//得到注解的值
			setContentView(R.layout.buy_jc_main);
			initShakeListener();//初始化摇一摇监听事件
			initView();
			initSaleType();//初始化 销售方式的 popwindow
		}
		setTitleNav("竞彩", R.drawable.title_nav_back,R.drawable.title_nav_menu,R.drawable.selector_title_iv_right_bg3);
		bfbean = new JcBfBean();
		initTicket();
		initListener();
		//定制和 布局的最后确认
		changeTag();//确定选号界面
		shakeListener.start();
		shakeListener.setEnable(false);
		String tempStr= SPUtil.getString(R.string.custom_codes);
		if(tempStr.contains(","+lotteryId+",")||tempStr.contains(lotteryId+",")||tempStr.contains(lotteryId)){
			hideRightIcon();//已经定制的 隐藏定制按钮
		}
    }
    /** 竞彩 没有这样的lotteryId 特殊 不需要传递给后台  只作为Application 内部识别lotteryId **/
	protected void initTicket() {
		MyApp.resetOrderBean();//每个新玩法重置订单
		ticket = TicketEngine.createTicketByLotteryId(lotteryId);
		ticket.setLotteryId(lotteryId);
		ticket.setSalesType(salesType);//默认的销售方式 对于竞彩来说就是玩法id
		ticket.setChildId(childId);
		ticket.setFold(10);//竞彩默认是10倍
		MyApp.order.setFold(10);//默认都是投注十倍的
		MyApp.order.getPassTypeList().clear();
		if(Arrays.asList(MyApp.res.getStringArray(R.array.jczq_playid)).contains(salesType)){//足球
			if("1".equals(childId)){
				MyApp.order.getPassTypeList().add(101); //设置默认闯关方式
				MyApp.order.setObunch("101");//默认是二串一
			}else{
				MyApp.order.getPassTypeList().add(102); //设置默认闯关方式
				MyApp.order.setObunch("102");//默认是二串一
			}
		}else if (Arrays.asList(MyApp.res.getStringArray(R.array.jclq_playid)).contains(salesType)){//篮球
			if("1".equals(childId)){
				MyApp.order.getPassTypeList().add(02); //设置默认闯关方式
				MyApp.order.setObunch("02");//默认是二串一
			}else{
				MyApp.order.getPassTypeList().add(03); //设置默认闯关方式
				MyApp.order.setObunch("03");//默认是二串一
			}
			System.out.println("321----03---->"+MyApp.order.getPassTypeList());
		}
		ticket.setPassTypeList(MyApp.order.getPassTypeList());
		MyApp.order.setLotteryId(salesType);//这里实际传递的是 salesType 即竞彩的 玩法
		MyApp.order.setAppId(lotteryId);//CustomTagEnum 中指定id 
	}
	/**
	 * 编辑或者新建票
	 */
	public void customTicket() {
		if(MyApp.order.getTickets().size() > 0){
			ticket=MyApp.order.getTickets().get(0);
			MyApp.order.getTickets().remove(0);
			MyApp.order.getSceneNumber();
		}else{
			initTicket();
			onSaleTypeChanged(salesType);
			if(MyApp.order.getPassTypeList()==null){//设置默认闯关方式
				List<Integer> list=new ArrayList<Integer>();
				if(Arrays.asList(MyApp.res.getStringArray(R.array.jczq_playid)).contains(lotteryId)){//足球
					if("1".equals(childId)){
						list.add(101);
					}else{
						list.add(102);
					}
				}else if (Arrays.asList(MyApp.res.getStringArray(R.array.jclq_playid)).contains(lotteryId)){//篮球
					if("1".equals(childId)){
						list.add(02);
					}else{
						list.add(03);
					}
				}
				MyApp.order.setPassTypeList(list);
			}
			
			if(Arrays.asList(MyApp.res.getStringArray(R.array.jczq_playid)).contains(lotteryId)){//足球
				if("1".equals(childId)){
					MyApp.order.setObunch("101");//单关
				}else{
					MyApp.order.setObunch("102");
				}
			}else if (Arrays.asList(MyApp.res.getStringArray(R.array.jclq_playid)).contains(lotteryId)){//篮球
				if("1".equals(childId)){
					MyApp.order.setObunch("02");//篮彩单关
				}else{
					MyApp.order.setObunch("03");//篮彩03-(2串1)
				}
			}
		}
		if(mjcAdapter!=null){
			home_expandable_Lv.setAdapter(mjcAdapter);
			mjcAdapter.notifyDataSetChanged();
			mjcAdapter.notifyList();
		}
	}
	
	public void reflash(){
		
	}
	
	public void newTicket() {
		initTicket();
		if(mjcAdapter!=null){
			home_expandable_Lv.setAdapter(mjcAdapter);
			mjcAdapter.notifyDataSetChanged();
			mjcAdapter.notifyList();
		}
	}
    /**
	 * 对摇一摇事件的抽取
	 */
	private void initShakeListener() {
		shakeListener = new ShakeListener(mActivity);
		shakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				doShake();
			}
		});
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
					Message msg = Message.obtain();
					msg.arg1 = checkedId;
					msg.obj = group;
					msg.what = 0;
					mhandler.sendMessage(msg);//上面的3行代码 由于考虑到系统 原因,放到handler中执行. 如果没有放在handler中,小米手机会出现异常
				}
			});
		}
	}

	/**
	 * 子类复写该方法来设置pop_saleType的contentview
	 * @return
	 */
	protected abstract View customContent_saleType();
	protected abstract void onSaleTypeChanged(String tag);
	/**
	 *子类需要复写该方法来改变彩种的布局
	 * @param v
	 * @return
	 */
	@Deprecated
	protected abstract View customLotteryView();
	protected abstract void initJCAdapter(List<MessageBean> mList);
	private void changeTag() {
//		String tag = "";
//		if("1".equals(childId)){
//			tag = salesType;
//		}else {
//		tag = salesType+","+childId;
//		}
		String tag = salesType+","+childId;
		setNotice();
		onSaleTypeChanged(tag);
	}
	@Override
	protected void onLeftIconClicked() {//顶部返回按钮的监听重写
		if(ticket.getSession() >0){
			onBack();
		}else{
			mActivity.finish();//先跳转后清空
		}
	}
	protected void onBack() {
    	final TCDialogs dialog=new TCDialogs(mActivity);
		dialog.popDeleteConfirm("提示","清空选票 ?",new MyClickedListener() {
			@Override
			public void onClick() {dialog.dismiss();}
		}, new  MyClickedListener() {
			@Override
			public void onClick() {
				dialog.dismiss();
				mActivity.finish();//先跳转后清空
				MyApp.resetOrderBean();
			}
		});
    }
	
	private void initView() {
	    basebuy_tv_notice=(TextView) findViewById(R.id.inall_inform_tv);
		basebuy_btn_clear=(Button) findViewById(R.id.clear_gv_btn);//basebuy_btn_clear
		basebuy_btn_ok=(Button) findViewById(R.id.affirm_next_btn);//basebuy_btn_ok
//		sort_layout=(GridRadioGroup) findViewById(R.id.sort_layout);
//		cg_btn=(RadioButton) findViewById(R.id.cg_rbtn);//过关
//		dg_btn=(RadioButton) findViewById(R.id.dg_rbtn);//单关
//		home_jc_lotteryView=(RelativeLayout)findViewById(R.id.home_jc_lotteryView);
		jc_list_header=(LinearLayout)findViewById(R.id.jc_list_header);
		
//		home_jc_lotteryView.removeAllViews();
//		lotteryView=customLotteryView();
//		home_jc_lotteryView.addView(lotteryView);
		home_expandable_Lv=(JCListView)findViewById(R.id.home_expandable_Lv);
		rl_null_saishi = (RelativeLayout) findViewById(R.id.rl_null_saishi);
		basebuy_tv_notice.setText(Html.fromHtml(MyApp.res.getString(R.string.basebuy_notice_jc_text1)));//HtmlTextColorAndSize(p,0,p.length()-15,0,p.length()-15,0,p.length()-15));
		View mView=View.inflate(mActivity,R.layout.jc_g_header_view, null);
		home_expandable_Lv.clearChoices();
		home_expandable_Lv.setHeaderClick(mView);
		home_expandable_Lv.setHeaderView(mView);
	}
	
	/**
	 * 如果无对阵，那么调用该函数
	 */
	protected void showNullsaishi(){
		if(rl_null_saishi!=null){
			rl_null_saishi.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 如果有对阵，那么调用该函数
	 */
	protected void hideNullsaishi(){
		if(rl_null_saishi!=null){
			rl_null_saishi.setVisibility(View.GONE);
		}
	}
	
    /**
	 * 设置通知消息
	 *选票信息描述
	 * @return
	 */
	protected void setNotice() 
	{
		basebuy_tv_notice.setText(Html.fromHtml(replaceNoticeText(ticket.getSession())));//默认提示
	}
//    <string name="basebuy_notice_jc_text"><![CDATA[已选<font color="#ff9500">NUM</font>场]]></string>
//    <string name="basebuy_notice_jc_text1"><![CDATA[<font color="#ff9500">请至少选择2场比赛\n页面赔率仅供参考,请以出票为准</font>]]></string>
//    <string name="basebuy_notice_jclack_text"><![CDATA[已选<font color="#ff9500">NUM</font>场,还差<font color="#ff9500">LACK_NUM</font>场]]></string>
//    <string name="jcbetorder_notice_text"><![CDATA[投N倍ZHU注<font color="#ff9500">MONEY元</font>]]></string>
	private  String replaceNoticeText(int NUM){
		String s=MyApp.res.getString(R.string.basebuy_notice_jc_text1);//默认提示
		if(0 < NUM && Integer.parseInt(childId) > NUM ){
			s = StringUtils.replaceEach(MyApp.res.getString(R.string.basebuy_notice_jclack_text),new String[]{"NUM","LACK_NUM"}, new String[]{String.valueOf(NUM),String.valueOf((Integer.parseInt(childId)- NUM))});//已选NUM场,还差Lack_NUM场
		}else if(Integer.parseInt(childId) <= NUM){
			s = StringUtils.replaceEach(MyApp.res.getString(R.string.basebuy_notice_jc_text),new String[]{"NUM"}, new String[]{String.valueOf(NUM)});//已选NUM场
		}
		changeBtnbgAndTextColors(NUM);//清空按钮变色
    	return s;
	}
	protected void initListener(){
		//手机返回按钮
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				if(ticket.getSession() >0){
					onBack();
				}else{
					finish();
				}
			}
		});
//		sort_layout.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup rgroup, int id) {
//				// TODO Auto-generated method stub
//				switch (id) {
//				case R.id.cg_rbtn://过关:
//					childId="2";
//					initTicket();
//					changeTag();
//					break;
//				case R.id.dg_rbtn://单关:
//					childId="1";
//					initTicket();
//					changeTag();
//					break;
//				default:
//					break;
//				}
//			}
//		});
//		cg_btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				childId="2";
//				changeTag();
//			}
//		});
//		dg_btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				childId="1";
//				changeTag();
//			}
//		});
		jc_list_header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(home_expandable_Lv!=null){
					home_expandable_Lv.headerViewClick();
				}
			}
		});
		basebuy_btn_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clear();
			    shakeListener.vibrate();
			}
		});
		basebuy_btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				basebuy_btn_ok.setEnabled(false);
				 ok();
				 shakeListener.vibrate();
				basebuy_btn_ok.setEnabled(true);
			}
		});
	}
	protected void clear(){}
	protected  void ok(){}
	/**
	 * 摇一摇随机选号
	 */
	protected void doShake() {
//		System.out.println("210---------->initShakeListener");
	}
	/**
	 * 获取注解值
	 */
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
	 *顶部 右上角 按钮点击事件  
	 */
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,Integer.parseInt(lotteryId));
		setTitleNav(Integer.parseInt(lotteryId),mActivity.getTitleText(), R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}
	@Override
	public void onStart() {
		super.onStart();
		is_clear_containers_required = false;
	}
	public boolean isFragmentCacheEnable() {
		return isFragmentCacheEnable;
	}
	@Override
	public void onPause() {
		super.onPause();
		if(shakeListener != null)
			shakeListener.setEnable(false);
		isCreated = true;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		ViewGroup parent = (ViewGroup) mView.getParent();
		parent.removeAllViews();//如果没有该行代码,会出现 子控件已经有父类的异常
		clear();
	}
	@Override
	public void onResume() {
		super.onResume();
		shakeListener.start();
		if(content_saleType != null){//如果有多种不同的销售方式,通过设置pop_saleType 不为空来显示不同的销售方式
			initPop_saleType();
		}
		setTitleNav(MyApp.lotteryMap.get(salesType), R.drawable.title_nav_back,R.drawable.title_nav_menu,R.drawable.selector_title_iv_right_bg3);
		String tempStr= SPUtil.getString(R.string.custom_codes);
		if(tempStr.contains(","+lotteryId+",")||tempStr.contains(lotteryId+",")||tempStr.contains(lotteryId)){
			hideRightIcon();//已经定制的 隐藏定制按钮
		}
		basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
		changeBtnbgAndTextColors(ticket.getSession());
		if(bfbean.getScoreList()!=null){
			ticket.setSession(bfbean.getScoreList().size());
			basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
			changeBtnbgAndTextColors(ticket.getSession1());
		}
		
		
	}
	/**
	 * @param str 传入String 
	 * @param littleStart 小字体 开始位置
	 * @param littleEnd    小字体结束位置
	 * @param StyleStart  加粗开始位置
	 * @param StyleEnd    加粗结束位置
	 * @param ColorStart   颜色开始位置
	 * @param ColorEnd     颜色结束位置
	 * @return Spannable 
	 */
	private     Spannable textSp=null;
	protected   Spannable HtmlTextColorAndSize(String str,int littleStart,int littleEnd , int StyleStart ,int StyleEnd,int ColorStart,int ColorEnd){
//		textSp=(Spannable) Html.fromHtml(str);
//		SpannableString用法说明 
//		其中参数what是要设置的Style span，start和end则是标识String中Span的起始位置，而 flags是用于控制行为的，通常设置为0或Spanned中定义的常量，常用的有：
//		•Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含两端start和end所在的端点
//		•Spanned.SPAN_EXCLUSIVE_INCLUSIVE --- 不包含端start，但包含end所在的端点
//		•Spanned.SPAN_INCLUSIVE_EXCLUSIVE --- 包含两端start，但不包含end所在的端点
//		•Spanned.SPAN_INCLUSIVE_INCLUSIVE --- 包含两端start和end所在的端点
	        if(textSp==null){
	        	textSp= new SpannableString(str); 
	        }
			//		设置字体大小  （玩法字体16 最高奖金14）
			//	textSp.setSpan(new AbsoluteSizeSpan(16), 1, str.length()-5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
			textSp.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mActivity, 14)), littleStart,littleEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE); //str.length()-8, str.length()
			//		设置字体样式  （加粗）
			textSp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), StyleStart, StyleEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE); //0, str.length()-8 
			//		设置字体 颜色（体前景色ForegroundColorSpan   背景色 BackgroundColorSpan）R.color.klpk_bonustext_color
			textSp.setSpan(new ForegroundColorSpan(MyApp.res.getColor(R.color.klpk_bonustext_color)), ColorStart,ColorEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);//str.length()-4, str.length()-1  
		return textSp;
	}
	
}
