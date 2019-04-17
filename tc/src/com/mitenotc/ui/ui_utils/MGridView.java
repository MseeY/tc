package com.mitenotc.ui.ui_utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.ui.ui_utils.custom_ui.Custom_PL120IV;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 对购彩界面选号栏的封装,只要获取selectedBalls 就获取到了用户选择的球
 * 
 * @author mitenotc
 */
public class MGridView extends GridView {

	private List<View> alistView = new ArrayList<View>();
	private List<View> customItems;
	private List<String> yi_lou_texts = new ArrayList<String>();
	
	public static final int RED_MODE = 0;
	public static final int BLUE_MODE = 1;
	private List<Integer> sampleNums;// 容器的数据样本//需要 具体事例 分别初始化的参数,默认初始化为 双色球的红球选号区 33 个球
//	private List<String> sampleTexts;//容器中显示的文本 
	private String[] sampleTexts;
	private Spannable[] sampleSapns;//专门用于快三的文字大小不一样时,使用span
	private int unselectedTextColorId;// 字体颜色//需要具体事例 分别初始化的参数,默认初始化为 双色球的红色字体
	private int selectedBallSRCId;// 球的背景图片//需要具体事例 分别初始化的参数,默认初始化为 双色球的红色背景
	private int selectedTextColorId;
	private int unselectedBallSRCId;
	private boolean isNumFormat = true;// 是否要求2位数格式化,如 01,02
	private int textFormatMode = TEXT_MODE_NUMBERS;
	public static final int TEXT_MODE_LARGE_SMALL = 1;//时时彩的大小单双模式
	public static final int TEXT_MODE_NUMBERS = 0;//普通的数字模式
	public static final int TEXT_MODE_CUSTOM = 2;//显示的文字自定义// 该模式需要使用 sampleTexts
	private PopupWindow pop;// pop 的引用
	private TextView pop_ball_tv;
	protected List<Integer> selectedBalls;// 选中的号码的容器
	protected List<String> selectedText;// 选中的号码对应的text
	// private List<Integer> selectedBalls;//选中的号码的容器
	private BaseAdapter mAdapter;
	protected ActionUpListener actionUpListener;
	private int miniNum = 1;//该选区最少应该选中的个数//需要具体事例 分别初始化的参数,默认初始化为 双色球的红球区的个数, 6
	protected int maxNum = 0;//该选区最多允许选择的个数. 例如双色球胆码区最多只能选择5个//如果是0,则该引用 不起作用
	private boolean isSelfMutual;//是否为自身互斥. 例如排列3 存在自身互斥,一个 选号区只能选一个号.
	private boolean isShakeble;
	private MGridView[] mutualViews;//互斥 MGridView 中的选号,用于胆拖码中 同一个号码 只能有一个 区 被选中
	private MGridView[] mutualContainers;//互斥 MGridView, 用于容器的互斥. 例如时时彩的任一 单式, 只能在5个容器中的一个容器内有一个号被选中
	private int mutualContainer_select_num;//互斥 MGridView 需要被选中的数量. 时时彩 任一是一个, 任二是两个
	private boolean isPopRequired = true;
	
	
	public MGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
			init();
	}

	public MGridView(Context context) {
		super(context);
		init();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	private void init() {
		setVerticalScrollBarEnabled(false);
//		selectedBallSRCId = R.drawable.pop_ball_red;
		selectedBallSRCId = R.drawable.shape_ball_red;
		unselectedTextColorId = 0xffff0000;
		selectedTextColorId = R.color.white;
//		unselectedBallSRCId = R.drawable.pop_ball_default;
		unselectedBallSRCId = R.drawable.shape_ball_white;
		selectedBalls = new ArrayList<Integer>();
		selectedText = new ArrayList<String>();
		sampleNums = new ArrayList<Integer>();
		setHorizontalScrollBarEnabled(false);
//		for (int i = 0; i < 33; i++) {
//			sampleNums.add(i + 1);
//			simpleSampleNums(1, 33);
//		}
		mAdapter = new BallAdapter();
		setAdapter(mAdapter);

		/*
		 * int measureSpec = MeasureSpec.makeMeasureSpec(0,
		 * MeasureSpec.UNSPECIFIED); view.measure(measureSpec, measureSpec);
		 */
		boolean isTest = false;
		if(isTest){
			initPopForEdit();
		}else {
			initPop();
		}
	}
	
	public void simpleInit(boolean isPopRequired,List<Integer> sampleNums,int miniNum,List<View> customItems){
		this.isPopRequired = isPopRequired;
		this.sampleNums = sampleNums;
		this.miniNum = miniNum;
		this.customItems = customItems;
	}
	
	public void simpleInit(boolean isPopRequired,boolean isSelfMutual,List<Integer> sampleNums,int miniNum,List<View> customItems){
		this.isPopRequired = isPopRequired;
		this.isSelfMutual=isSelfMutual;
		this.sampleNums = sampleNums;
		this.miniNum = miniNum;
		this.customItems = customItems;
	}
	
	/**
	 * 简单的初始化, 自定义 sampleNms,sampleTexts
	 * @param isSelfMutual
	 * @param miniNum
	 * @param sampleNums
	 * @param sampleTexts
	 * @param sampleView
	 * @param selectedBallSRCId
	 * @param unselectedTextColorId
	 * @param selectedTextColorId
	 * @param unselectedBallSRCId
	 */
	public void simpleInit(boolean isPopRequired,boolean isSelfMutual,int miniNum,List<Integer> sampleNums,String[] sampleTexts,int sampleViewWidth,int sampleViewHeight,int selectedBallSRCId,int unselectedTextColorId,int selectedTextColorId,int unselectedBallSRCId){
		this.isPopRequired = isPopRequired;
		this.isSelfMutual = isSelfMutual;
		this.miniNum = miniNum;
		this.sampleNums = sampleNums;
		this.sampleTexts = sampleTexts;
		this.sampleSapns = null;
		this.selectedBallSRCId = selectedBallSRCId;
		this.unselectedTextColorId = getResources().getColor(unselectedTextColorId);//unselectedTextColorId;//0xffff0000;
		this.selectedTextColorId = selectedTextColorId;
		this.unselectedBallSRCId = unselectedBallSRCId;
		alistView.clear();
		for (int i = 0; i < sampleNums.size(); i++) {
			TextView tv = null;
//			if(alistView.size()< i+1){
				tv = new TextView(getContext());
				tv.setGravity(Gravity.CENTER);
				tv.getPaint().setFakeBoldText(true);
//				tv.setPadding(0, 0, 0, 0);
				tv.setTextSize(12);
				tv.setTextColor(this.unselectedTextColorId);
				tv.setBackgroundResource(unselectedBallSRCId);
				tv.setLayoutParams(new LayoutParams(DensityUtil.dip2px(getContext(), sampleViewWidth), DensityUtil.dip2px(getContext(), sampleViewHeight)));
				alistView.add(tv);
//			}else {
//				tv = (TextView) alistView.get(i);
//				tv.setLayoutParams(new LayoutParams(sampleViewWidth, sampleViewHeight));
//			}
		}
		if(sampleTexts != null){
			textFormatMode = TEXT_MODE_CUSTOM;
		}
	}
	
	public void simpleInit_span(boolean isPopRequired,boolean isSelfMutual,int miniNum,List<Integer> sampleNums,Spannable[] sampleSapns,int sampleViewWidth,int sampleViewHeight,int selectedBallSRCId,int unselectedTextColorId,int selectedTextColorId,int unselectedBallSRCId){
		this.sampleSapns = sampleSapns;
		simpleInit(isPopRequired,isSelfMutual,miniNum,sampleNums,null,sampleViewWidth,sampleViewHeight,selectedBallSRCId,unselectedTextColorId,selectedTextColorId,unselectedBallSRCId);
	}
	
	/**
	 * 简单的初始化设置.
	 * @param colorMode 红球还是蓝球的选择,使用该类的静态常量
	 * @param startNum 开始号码
	 * @param endNum 结束号码
	 */
	public void simpleInit(int colorMode, int startNum, int endNum){
		switch (colorMode) {
		case RED_MODE:
//			selectedBallSRCId = R.drawable.pop_ball_red;
			selectedBallSRCId = R.drawable.shape_ball_red;
			unselectedTextColorId = 0xffff0000;
			break;
		case BLUE_MODE:
//			selectedBallSRCId = R.drawable.pop_ball_blue;
			selectedBallSRCId = R.drawable.shape_ball_blue;
			unselectedTextColorId = getResources().getColor(R.color.blue);
			break;
		default:
			break;
		}
		simpleSampleNums(startNum, endNum);
	}
	/**
	 * 用于时时彩的 大,小,单,双
	 * @param textFormatMode
	 * @param nums
	 */
	public void simpleInit(int textFormatMode, int... nums){
		this.textFormatMode = textFormatMode;
		sampleNums.clear();
		for (int i : nums) {
			sampleNums.add(i);
			View sampleView = View.inflate(getContext(), R.layout.f_pop_ball_item,null);
			alistView.add(sampleView);
		}
	}
	/**
	 * 时时彩 大小单双容器的初始化
	 */
	public void simpleInit_ssc_large_small(){
		simpleInit(TEXT_MODE_LARGE_SMALL, 9,0,1,2);
	}
	/**
	 * 在 三个参数的方法的基础上 增加了最少号码数
	 *  简单的初始化设置.
	 * @param miniNum 随机选号最少个数
	 * @param colorMode 红球还是蓝球的选择,使用该类的静态常量
	 * @param startNum 开始号码
	 * @param endNum 结束号码
	 */
	public void simpleInit(int miniNum, int colorMode, int startNum, int endNum){
		this.miniNum = miniNum;
		simpleInit(colorMode, startNum, endNum);
	}
	public void simpleInit(int miniNum, int colorMode, int startNum, int endNum,boolean isNumFormat){
		this.miniNum = miniNum;
		this.isNumFormat = isNumFormat;
		simpleInit(colorMode, startNum, endNum);
	}
	/**
	 * 初始化操作,对4个参数的方法的重载
	 * @param isSelfMutual
	 * @param miniNum
	 * @param colorMode
	 * @param startNum
	 * @param endNum
	 */
	public void simpleInit(Boolean isSelfMutual,int miniNum, int colorMode, int startNum, int endNum){
		this.isSelfMutual = isSelfMutual;
		simpleInit(miniNum,colorMode, startNum, endNum);
	}
	/**
	 * 对上面的方法的重载,添加了 对数字 format 的控制
	 * @param isSelfMutual
	 * @param miniNum
	 * @param colorMode
	 * @param startNum
	 * @param endNum
	 * @param isNumFormat
	 */
	public void simpleInit(Boolean isSelfMutual,int miniNum, int colorMode, int startNum, int endNum,boolean isNumFormat){
		this.isNumFormat = isNumFormat;
		simpleInit(isSelfMutual, miniNum, colorMode, startNum, endNum);
	}

	private void initPop() {
		pop = SecondActivity.pop;
		if(pop!=null){
			pop_ball_tv = SecondActivity.pop_ball_tv;
			popWidth =pop.getWidth();
			popHeight =pop.getHeight();
			pop.setOnDismissListener(new OnDismissListener() {// 用于解决滑动出GridView后取消了pop,同时要设置cb为空//该问题在SecondActivity
				// 中的dispatchTouchEvent
				// 中解决,因此要在这里吧cb置为空
				@Override
				public void onDismiss() {
					cb = null;
				}
			});
		}
	}
	//该方法仅仅是为了 编辑的时候能够显示出 预览界面 而设的
	private void initPopForEdit() {
		View view = View.inflate(getContext(), R.layout.f_pop_ball, null);
		pop_ball_tv = (TextView) view.findViewById(R.id.pop_ball_tv);
		
		int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(measureSpec, measureSpec);
		int popWidth = view.getMeasuredWidth();
		int popHeight = view.getMeasuredHeight();
		
		pop = new PopupWindow(view,popWidth,popHeight);
//		pop.setAnimationStyle(1);
	}
	
	private int index;// 点击到的子控件的角标
	public static TextView cb;// 选号栏 默认的球
	private int popWidth;// pop 中控件的宽
	private int popHeight;// pop 中控件的高
	private int downX;
	private int downY;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		View child = null;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = x;
			downY = y;
			index = pointToPosition(x, y);
			if(customItems != null)
				break;
			child = getChildAt(index);
			if (child != null) {
				cb = (TextView) child.findViewById(R.id.pop_ball_cb_default);
				if(cb == null){//如果为空代表是筛子类的,筛子类的 子view 直接就是TextView
					cb = (TextView)child;
				}
				if(isPopRequired){
					String text = getText(index);////System.out.println("text = ---------------"+text);
					int offX = (cb.getWidth() - popWidth) / 2;// 可以把红球加1像素的透明边框
					int offY = -popHeight;
	
					pop_ball_tv.setText(text);
					pop_ball_tv.setBackgroundResource(selectedBallSRCId);
					pop.showAsDropDown(cb, offX, offY + 1);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			/*
			 * if(Math.abs(x-downX)>5 ||Math.abs(y-downY)>5){ pop.dismiss();
			 * index = -1; cb = null; }
			 */
			break;
		case MotionEvent.ACTION_UP:
			if (index == pointToPosition(x, y)) {
				child = getChildAt(index);
				if (child != null && customItems == null ){
					cb = (TextView) child.findViewById(R.id.pop_ball_cb_default);// 主要是在pop消失时先把cb置为空,以防止不正当的获取cb的对象,因此这里从新获取cb对象,问题的根源是gridview的滑动事件会导致gridview的ontouch事件无法被回调
					if(cb == null){
						cb = (TextView) child;
					}
				}
			}
			
			if(customItems != null){////System.out.println("child instanceof CustomItem ======== "+(child instanceof CustomItem));
				if (child != null && index>=0 && child instanceof CustomItem) {
					CustomItem item = (CustomItem) child;
					if (selectedBalls.contains(sampleNums.get(index))) {
						item.unselected();
						selectedBalls.remove(sampleNums.get(index));
						if(item.getText()!=null && -1 != selectedText.indexOf(item.getText())){// -1 表示不存在
							selectedText.remove( selectedText.indexOf(item.getText()));
						}
					}else {
						selectedBalls.add(sampleNums.get(index));
						if(maxNum > 0 && selectedBalls.size() > maxNum){
							// 为了防止用户在已经提示限制的情况下 继续点击选号  号码被异常选中的bug 
//							if(mutualViews != null)//添加互斥选择,存在互斥的 选号区才进行操作
//								mutualSelect(mutualViews);
							if(isSelfMutual){
								if(selectedBalls.size() >0){
									int indes=selectedBalls.get(0);//上一个选号
									for (int i = 0; i < customItems.size(); i++) {
										if(customItems.get(i).getTag().equals(String.valueOf(indes))){
											((CustomItem) customItems.get(i)).unselected();
										}
									}
									selectedBalls.remove(0);
								}
								item.selected();
								if(item.getText()!=null && -1 == selectedText.indexOf(item.getText())){
									selectedText.clear();//isSelfMutual 自身互斥
									selectedText.add(item.getText());
								}
//								System.out.println("125------->"+selectedBalls.toString());
							}else{
								item.unselected();
								selectedBalls.remove(sampleNums.get(index));
								if(item.getText()!=null && -1 != selectedText.indexOf(item.getText().toString())){// -1 表示不存在
									selectedText.remove(selectedText.indexOf(item.getText()));
								}
								Toast.makeText(getContext(), "该选区最多只能选择"+maxNum+"个号", 0).show();
							}
							// 自定义itemView 的点击 响应 接口
							Collections.sort(selectedBalls);
							if (actionUpListener != null) {
								actionUpListener.onActionUp();
							}
							return true;
						}
						item.selected();
						if(item.getText()!=null && -1 == selectedText.indexOf(item.getText())){
							selectedText.add(item.getText());
						}
						if(mutualViews != null)//添加互斥选择,存在互斥的 选号区才进行操作
							mutualSelect(mutualViews);
						mutualContainerSelect_costum();
//						select(index);
//						if(mutualViews != null){//添加互斥选择,存在互斥的 选号区才进行操作
//							////System.out.println("120=============mutualViews  !=null");
//							mutualSelect(mutualViews);
//				     	}
						
					}
					
					Collections.sort(selectedBalls);
					// 球的有效点击 响应 接口
					if (actionUpListener != null) {
						actionUpListener.onActionUp();
					}
				}
			}else {
				if (cb != null && index>=0) {
					if (selectedBalls.contains(sampleNums.get(index))) {
						unSelect(index);
					} else {
						if(maxNum > 0 && selectedBalls.size() >= maxNum){
							Toast.makeText(getContext(), "该选区最多只能选择"+maxNum+"个号", 0).show();
							return true;
						}
						
						select(index);
						mutualContainerSelect();
						if(mutualViews != null)//添加互斥选择,存在互斥的 选号区才进行操作
							mutualSelect(mutualViews);
					}
					
					Collections.sort(selectedBalls);
					// 球的有效点击 响应 接口
					if (actionUpListener != null) {
						actionUpListener.onActionUp();
					}
				}
			}

			mAdapter.notifyDataSetChanged();
			////System.out.println("selectballs = " + selectedBalls);
			index = -1;
			cb = null;
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * 用于 时时彩的任一,任二 单式 容器的互斥
	 */
	private void mutualContainerSelect() {
		if(mutualContainer_select_num > 0 && mutualContainers != null){//存在容器互斥,也就是时时彩的任一和任二 单式
			int count =0;
			for (MGridView view : mutualContainers) {
				count += view.getSelectedBalls().size();
			}
			if(count >= mutualContainer_select_num){
				for (MGridView view : mutualContainers) {
					if(view.getSelectedBalls().size()>0){
						int index = view.getSampleNums().indexOf(view.getSelectedBalls().get(0));
						TextView tv = (TextView) view.getChildAt(index).findViewById(R.id.pop_ball_cb_default);
						view.unSelect(index, tv);
					}
				}
			}
		}
	}
	/**
	 * 自定义控件中 两容器互斥
	 */
	private void mutualContainerSelect_costum() {
		if(mutualContainer_select_num > 0 && mutualViews != null){//存在容器互斥,也就是时时彩的任一和任二 单式
			int count =0;
			for (MGridView view : mutualViews) {
				count += view.getSelectedBalls().size();
			}
			if(count >= mutualContainer_select_num){
				for (MGridView view : mutualViews) {
					if(view.getSelectedBalls().size()>0){
						int index = view.getSampleNums().indexOf(view.getSelectedBalls().get(0));
						TextView tv = (TextView) view.getChildAt(index).findViewById(R.id.pop_ball_cb_default);
						view.unSelect(index, tv);
					}
				}
			}
		}
	}

	/**
	 * 设置球的不选中
	 * @param index
	 */
	public void select(int index) {
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		if(isSelfMutual && selectedBalls.size()>0){
			int preIndex = sampleNums.indexOf(selectedBalls.get(0));//之前选中的号码的角标
			TextView tv = (TextView) getChildAt(preIndex).findViewById(R.id.pop_ball_cb_default);
			unSelect(preIndex, tv);
		}
		cb.setTextColor(getResources().getColor(selectedTextColorId));
		cb.setBackgroundResource(selectedBallSRCId);
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		selectedBalls.add(sampleNums.get(index));
	}
	/**
	 * 设置球的不选中
	 * @param index
	 */
	public void select(int index,TextView cb) {
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		if(isSelfMutual && selectedBalls.size()>0){
			int preIndex = sampleNums.indexOf(selectedBalls.get(0));//之前选中的号码的角标
			TextView tv = (TextView) getChildAt(preIndex).findViewById(R.id.pop_ball_cb_default);
			unSelect(preIndex, tv);
		}
		cb.setTextColor(getResources().getColor(selectedTextColorId));
		cb.setBackgroundResource(selectedBallSRCId);
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		selectedBalls.add(sampleNums.get(index));
	}

	/**
	 * 设置球的选中
	 * @param index
	 */
	public void unSelect(int index) {
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		cb.setTextColor(unselectedTextColorId);
		cb.setBackgroundResource(unselectedBallSRCId);
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		selectedBalls.remove(sampleNums.get(index));
	}
	/**
	 * 自定义控件 重载 此方法
	 * @param index
	 */
	public void unCoustomSelect(int index) {
		selectedBalls.remove(sampleNums.get(index));
	}
	/**
	 * 
	 * @param index
	 */
	public void unSelect(int index,TextView cb) {
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		cb.setTextColor(unselectedTextColorId);
		cb.setBackgroundResource(unselectedBallSRCId);
		cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
		selectedBalls.remove(sampleNums.get(index));
	}

	
	/**
	 * 用于胆拖 的互斥选择
	 * @param gv
	 */
	public void mutualSelect(MGridView gv){
		if(gv != null && gv.getVisibility() == View.VISIBLE && gv.getSampleNums().get(index) != null){//只有当该 角标能获取到一个相应的球的时候才能就行相应的操作
			//这里的判断是用于 胆拖互斥的时候外部调用,此时 cb 并没有在 点击事件中赋值,但是 已经拿到了另一个选号界面的号的角标
			////System.out.println("index = "+index);
			////System.out.println("gv.getChildAt(index) = "+gv.getChildAt(index));
			TextView cb = (TextView) gv.getChildAt(index).findViewById(R.id.pop_ball_cb_default);
			if(cb == null && gv.alistView.size() > index){
				cb = (TextView) gv.alistView.get(index);
				gv.unSelect(index,cb);
			}else if(gv.customItems!=null && gv.customItems.size() > 0){
				Custom_PL120IV mview = (Custom_PL120IV) gv.customItems.get(index);
				mview.unselected();
				gv.unCoustomSelect(index);
			}else{
				gv.unSelect(index,cb);
			}
		}
		
	}
	
	/**
	 * 把所有需要互斥选择的都设置互斥选择
	 * @param gvs
	 */
	public void mutualSelect(MGridView... gvs){
		for (MGridView gv : gvs) {
			mutualSelect(gv);
		}
	}

	/**
	 * 获取当前选中的球所在的 position的值
	 * 注意,该方法只有在action_down到action_up 之间有效,出了该事件区间,该值就是 -1; 该值也就无效了
	 * @return
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * 判断 数据是否要格式化
	 * @param num
	 * @return
	 */
	private String getText(int index) {
		int num = sampleNums.get(index);
		String text = "";
		if(textFormatMode == TEXT_MODE_NUMBERS){
			if (isNumFormat){
				text = new DecimalFormat("00").format(num);
				text = text.charAt(0)+" "+text.charAt(1);
			}else
				text = num + "";
		}else if(textFormatMode == TEXT_MODE_LARGE_SMALL){
			switch (num) {//时时彩中用到的大小单双
			case 9:
				text = "大";
				break;
			case 0:
				text = "小";
				break;
			case 1:
				text = "单";
				break;
			case 2:
				text = "双";
				break;
			default:
				break;
			}
		}else if (textFormatMode == TEXT_MODE_CUSTOM) {
			text = sampleTexts[index];
		}
		return text;
	}

	class BallAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			if(sampleNums == null)
				return 0 ;
			return sampleNums.size();
		}

		@Override
		public Object getItem(int position) {
			return sampleNums.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {////System.out.println("hash = "+hashCode() +"  :  parent.getchildcount = "+parent.getChildCount()+ "  :  position = "+position + "  :  current time = "+System.currentTimeMillis());
			//*******************拥有CustomItem 接口以后的代码结构*********************
			if(customItems != null){
//				if(convertView == null){//如果这里 没有复用convertview 会出现 popupwindow 点击 宕机 现象
				if(customItems.size() > position)
					convertView = customItems.get(position);
//				}
				if(parent.getChildCount() != position){
					return convertView;
				}
				
				TextView view_yi_lou = (TextView) convertView.findViewById(R.id.pop_ball_tv_yi_lou);
				if(yi_lou_texts != null && yi_lou_texts.size()>0 && view_yi_lou != null){
					view_yi_lou.setText(yi_lou_texts.get(position));
				}

				// 设置球的显示
				if(convertView instanceof CustomItem){
					CustomItem item = (CustomItem) convertView;
					if (selectedBalls.contains(sampleNums.get(position))) {
						item.selected();
					} else {
						item.unselected();
					}
				}
				return convertView;
			}
			
			//********************从这儿以下是拥有CustomItem 接口以前的. 所有的item 都是在该类中生成的**************************
			//*******************拥有CustomItem 接口以后的代码结构*********************
			if(customItems != null){
				if(convertView == null){//如果这里 没有复用convertview 会出现 popupwindow 点击 宕机 现象
					convertView = customItems.get(position);
				}
				if(parent.getChildCount() != position){
					return convertView;
				}
				
				TextView view_yi_lou = (TextView) convertView.findViewById(R.id.pop_ball_tv_yi_lou);
				if(yi_lou_texts != null && yi_lou_texts.size()>0 && view_yi_lou != null){
					view_yi_lou.setText(yi_lou_texts.get(position));
				}

				// 设置球的显示
				if(convertView instanceof CustomItem){
					CustomItem item = (CustomItem) convertView;
					if (selectedBalls.contains(sampleNums.get(position))) {
						item.selected();
					} else {
						item.unselected();
					}
				}
				return convertView;
			}
			
			//********************从这儿以下是拥有CustomItem 接口以前的. 所有的item 都是在该类中生成的**************************
//			convertView = View.inflate(getContext(), R.layout.f_pop_ball_item,null);
				if(convertView == null){//如果这里 没有复用convertview 会出现 popupwindow 点击 宕机 现象
					convertView = alistView.get(position);
				}
				
				if(parent.getChildCount() != position){
					return convertView;
				}
			TextView cb = (TextView) convertView.findViewById(R.id.pop_ball_cb_default);
			if(cb == null){
				cb = (TextView)convertView;
				cb.setPadding(0, 0, 0, 0);
			}
			// 格式化的数据
			String text = getText(position);
//			-----------TODO 万利添加-----------------------------------------
//			SpannableString textStrSpan=new SpannableString(text);
//			if(textStrSpan.length() > 3){
//				textStrSpan.setSpan(new AbsoluteSizeSpan(22), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);//包含两端start和end所在的端点
//				textStrSpan.setSpan(new AbsoluteSizeSpan(14), 3, textStrSpan.length() , Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//包含两端start和end所在的端点
////				cb.setText(textStrSpan);
//			}/*else{
//				textStrSpan.setSpan(new AbsoluteSizeSpan(22), 0, textStrSpan.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//包含两端start和end所在的端点
//				cb.setText(textStrSpan);
//			}*/
////			------------------------------------------------------------------
			if(sampleSapns == null){
				cb.setText(text);
			}else {
				cb.setText(sampleSapns[position]);
			}
			//设置遗漏数据
			TextView view_yi_lou = (TextView) convertView.findViewById(R.id.pop_ball_tv_yi_lou);
			if(yi_lou_texts != null && yi_lou_texts.size()>0 && view_yi_lou != null){
				view_yi_lou.setText(yi_lou_texts.get(position));
			}

			// 设置球的显示
			if (selectedBalls.contains(sampleNums.get(position))) {
				cb.setTextColor(getResources().getColor(selectedTextColorId));
				cb.setBackgroundResource(selectedBallSRCId);
			} else {
				cb.setTextColor(unselectedTextColorId);
				cb.setBackgroundResource(unselectedBallSRCId);
			}
			cb.setPadding(0, 0, 0, 0);//为了解决 快三显示的文字丢失的问题. 原因就在于 padding 会被改回系统默认的padding,因此要在这里把padding改成0. 由于不稳定性,因此在改变textview的前后都设置了 padding为0
			return convertView;
		}
	}
  

	/**
	 * 对外提供 mAdapter的notifyDataSetChanged方法
	 */
	public void notifyDataSetChanged() {
		if(mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

	/**
	 * 清空选中的球
	 */
	public void clear() {
		selectedBalls.clear();
		mAdapter.notifyDataSetChanged();
	}
	
	public List<Integer> randomSelect(){////System.out.println("randomselect");
		selectedBalls.clear();
		int num;
		Random random = new Random(); 
		a : while (selectedBalls.size() < miniNum) {
			num = sampleNums.get(random.nextInt(sampleNums.size()));
			if (selectedBalls.contains(num)) {
				continue;
			}
			if(mutualViews != null){//满足互斥的容器中不存在相同的号
				for (MGridView mutualView : mutualViews) {
					if(mutualView != null &&mutualView.selectedBalls.contains(num)){
						continue a;
					}
				}
			}
			selectedBalls.add(num);
		}
		Collections.sort(selectedBalls);
		mAdapter.notifyDataSetChanged();
		return selectedBalls;
	}

	public int getTextFormatMode() {
		return textFormatMode;
	}

	public void setTextFormatMode(int textFormatMode) {
		this.textFormatMode = textFormatMode;
	}

	public interface ActionUpListener {
		void onActionUp();
	}

	public List<Integer> getSampleNums() {
		return sampleNums;
	}

	public void setSampleNums(List<Integer> sampleNums) {
		this.sampleNums = sampleNums;
	}

	public boolean isNumFormat() {
		return isNumFormat;
	}

	public void setNumFormat(boolean isNumFormat) {
		this.isNumFormat = isNumFormat;
	}

	public PopupWindow getPop() {
		return pop;
	}

	public void setPop(PopupWindow pop) {
		this.pop = pop;
	}

	public TextView getPop_ball_tv() {
		return pop_ball_tv;
	}

	public void setPop_ball_tv(TextView pop_ball_tv) {
		this.pop_ball_tv = pop_ball_tv;
	}

	public int getTextColorId() {
		return unselectedTextColorId;
	}

	public void setTextColorId(int textColorId) {
		this.unselectedTextColorId = textColorId;
	}

	public int getBallSRCId() {
		return selectedBallSRCId;
	}

	public void setBallSRCId(int ballSRCId) {
		this.selectedBallSRCId = ballSRCId;
	}

	public List<Integer> getSelectedBalls() {
		return selectedBalls;
	}
	/**
	 * 竞彩用到toString 102^103^104^105  最终格式
	 * @return
	 */
	public String getSelectedBallstoString() {
		String s="";
		if(selectedBalls==null)return s;
		for (int i = 0; i < selectedBalls.size(); i++) {
			if(i == selectedBalls.size()-1){
				s += String.valueOf(selectedBalls.get(i));
			}else{
				s += selectedBalls.get(i)+"^";
			}
		}
		return s;
	}

	
	public List<View> getCustomItems() {
		return customItems;
	}

	public void setSelectedBalls(List<Integer> selectedBalls) {
		this.selectedBalls = selectedBalls;
	}

	public BaseAdapter getmAdapter() {
		return mAdapter;
	}

	public ActionUpListener getActionUpListener() {
		return actionUpListener;
	}

	public void setActionUpListener(ActionUpListener actionUpListener) {
		this.actionUpListener = actionUpListener;
	}

	public boolean isShakeble() {
		return isShakeble;
	}

	public void setShakeble(boolean isShakeble) {
		this.isShakeble = isShakeble;
	}

	public int getMiniNum() {
		return miniNum;
	}

	public void setMiniNum(int miniNum) {
		this.miniNum = miniNum;
	}

	public int getMaxNum() {
		return maxNum;
	}
	
	public MGridView[] getMutualViews() {
		return mutualViews;
	}

	public void setMutualViews(MGridView[] mutualViews) {
		this.mutualViews = mutualViews;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	
	public boolean isSelfMutual() {
		return isSelfMutual;
	}

	public void setSelfMutual(boolean isSelfMutual) {
		this.isSelfMutual = isSelfMutual;
	}
	public List<String> getSelectedText() {
		return selectedText;
	}
	public String getSelectedTextToString() {
		String s="";
		if(selectedText==null)return s;
		for (int i = 0; i < selectedText.size(); i++) {
			if(i == selectedText.size()-1){
				s += String.valueOf(selectedText.get(i));
			}else{
				s += selectedText.get(i)+",";
			}
		}
		return s;
	}
	

	/**
	 * 给样本号码简单的赋值方式
	 * @param start 开始号码
	 * @param end 结束号码
	 */
	public void simpleSampleNums(int start, int end){
		alistView.clear();
		sampleNums.clear();
		for (int i = start; i <= end;i++ ) {
			sampleNums.add(i);
			View sampleView = View.inflate(getContext(), R.layout.f_pop_ball_item,null);
//			sampleView.setLayoutParams(new LayoutParams(DensityUtil.dip2px(getContext(), 40),DensityUtil.dip2px(getContext(), 50)));
			alistView.add(sampleView);
		}
	}
	
	public void set_YiLou_Visibility(boolean isVisible){
		for (View view : alistView) {
			View view_yi_lou = view.findViewById(R.id.pop_ball_tv_yi_lou);
			if(isVisible)
				view_yi_lou.setVisibility(View.VISIBLE);
			else
				view_yi_lou.setVisibility(View.GONE);
		}
	}
	/**
	 * 获取不到网络数据的时候初始化 遗漏 为 "--"
	 */
	public void reset_YiLou_text(){
		for (int i = 0; i < alistView.size(); i++) {
			TextView view_yi_lou = (TextView) alistView.get(i).findViewById(R.id.pop_ball_tv_yi_lou);
			view_yi_lou.setText("--");
		}
	}
	/**
	 * 给遗漏设置 遗漏值
	 * @param texts
	 */
	public void set_YiLou_text(List<String> texts){
		if(texts == null || alistView.size() != texts.size()){
			reset_YiLou_text();
			return;
		}
		yi_lou_texts = texts;
		for (int i = 0; i < alistView.size(); i++) {
			TextView view_yi_lou = (TextView) alistView.get(i).findViewById(R.id.pop_ball_tv_yi_lou);
			view_yi_lou.setText(texts.get(i));
		}
	}

	public MGridView[] getMutualContainers() {
		return mutualContainers;
	}

	public void setMutualContainers(MGridView[] mutualContainers) {
		this.mutualContainers = mutualContainers;
	}

	public int getMutualContainer_select_num() {
		return mutualContainer_select_num;
	}

	public void setMutualContainer_select_num(int mutualContainer_select_num) {
		this.mutualContainer_select_num = mutualContainer_select_num;
	}
	
	public interface CustomItem{
		public void selected();
		public void unselected();
		public String getText();
	}
	
}
