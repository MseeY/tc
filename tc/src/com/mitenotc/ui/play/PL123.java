package com.mitenotc.ui.play;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitenotc.bean.K3Bean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MGridView.ActionUpListener;
import com.mitenotc.ui.ui_utils.custom_ui.Custom_PL120IV;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.RefreshYiLouReceiver;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.DensityUtil;
/**
 * 123    江苏快三
 * 
 * 除了默认      为和值布局之外
 * 
 * 本类只做布局划分判断
 * 
 * @author wanli
 *
 */

@BaseBuyAnnotation(lotteryId = "123",salesType = "0",childId="1")
public class PL123 extends BaseBuyFragment implements OnCheckedChangeListener  ,RefreshYiLou{
//	特殊Ticket
	private View initView;
	private  MessageJson paramsJson;
	private  MessageBean mBean;
	private static boolean booleanTag=true;// 遗漏改变刷新标志
	private static boolean YILOU_TAG=false; //  遗漏btn
	private  RefreshYiLouReceiver mReceiver;
//	子玩法布局中分情况涉及到的控件
	private TextView fast3_top_tv;//胆拖投注中的布局提示窗体
	private List<Integer> selectballList=null;//大小单双 组合选中结果
	private  int  addTag=0;//大小单双 组合情况
	private  boolean IS_UP_INCIDENT=false;
    private LinearLayout fast3_pl_contentLayout;//中间玩法布局
    private List<Integer> sampleNumList;//初始化 和值选号容器1的时候 需要显示的数字
    private List<Integer> sampleTNumList;//初始化 和值选号容器1的时候 需要显示的数字
    private String[] sampleStr;//初始化 和值选号容器1的时候 需要显示的文本(初始化 同号)
    private String[] sampleTStr;//初始化 text
    private String[] sampleBStr;//初始化 不同号
    private String[] sampleFStr;//初始化 复选
    
	private JSONObject ylJson;
    public Map<String,List<String>> ylMap=new HashMap<String, List<String>>();// 混存遗漏数据

//    和值的大小单双
    private CheckBox  chbox0;
    private CheckBox  chbox1;
    private CheckBox  chbox2;
    private CheckBox  chbox3;
    
    private Bundle mBundle;
	private TCDialogs dialog;//提示框
	private List<View> customView=new ArrayList<View>();
	private List<View> customView1=new ArrayList<View>();
	private List<View> customView2=new ArrayList<View>();

//	private MyWM wm;
	
	@Override
	public void onStart() {
		super.onStart();
		registerBroadcastReceiver();
		sendgetYilouRequest();
    }
    /** 
      * 注册广播 
      */  
    private void registerBroadcastReceiver(){  
         if(mReceiver==null){
        	 mReceiver =new RefreshYiLouReceiver("123"); 
        	 mReceiver.setmRefreshYiLou(this);
        	 IntentFilter mFilter=new IntentFilter();
        	 mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+123+"_start_loading");
        	 mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+123+"_stop_loading");
        	 mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+123+"_awardinfo_received");
        	 mActivity.registerReceiver(mReceiver, mFilter); 	
         }
    } 
	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 *
	 */
	@Override
	protected void initContainers() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.fast3_hz_text));//和值
	}
	/**
	 * 重写
	 * 
	 * 加载title 布局  设置pop_saleType的contentview
	 * 
	 * content_saleType 可以用来获取引用 salesType = "1"
	 * 
	 */
	@Override
	protected View customContent_saleType() {
		
		return View.inflate(mActivity, R.layout.fast3_title_rb, null);
	}

	/**
	 * 重写
	 * 
	 * 加载play布局 
	 * 
	 */
	@Override
	protected View customLotteryView() {
		initView=View.inflate(mActivity, R.layout.f_fast3_main, null);
		fast3_pl_contentLayout=(LinearLayout) initView.findViewById(R.id.fast3_pl_contentLayout);
        fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_hz, null));

       containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.hz_mgv);//和值选号容器1
//     setSecondLayerBackground(R.drawable.hall_item_bg_selected);//快三投注背景  使用图片方式不符合界面效果
       setSecondLayerBackground(R.color.view_line2);//快三投注背景
	   sendgetYilouRequest();//请求遗漏
//	   setTitleNav(Integer.parseInt(lotteryId),"购彩", R.drawable.title_nav_back, R.drawable.title_nav_menu);
	   setTitleNav(Integer.parseInt(lotteryId),"购彩", R.drawable.title_nav_back,0);
//		fast3_await_cb.setVisibility(View.GONE); //title 时间提示旁边显示等待开奖中  默认是gone
//	   wm=new MyWM(mActivity);
//	   wm.setisShowyilouViewLinstener(new ChangeyilouShowView() {
//		
//		@Override
//		public void changeViewItemyilouShow(boolean isChangeView) {
//			itemChangeaddOmit(isChangeView);//实现刷新遗漏的效果	
//		}
//	  });
	   return initView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
//	/**
//	 * 产生随机票的时候 不能显示出 wm
//	 */
//	@Override
//	protected void toDestroyWM() {
//		  wm.DestroyWM();
//	}
	
//    @Override
//	public void onDestroy() {
//		if(wm!=null){
//			wm.DestroyWM();
//		}
//		super.onDestroy();
////		mActivity.getTitle_nav_iv_right2().setChecked(false);//漏btn
//	}
	@Override
	protected void onSaleTypeChanged(String tag) {
		mActivity.getTitle_nav_iv_right2().setChecked(false);// 恢复默认
		tag=tag.replace(",", "");
		switch (Integer.parseInt(tag)){
    	 // 和值
		   case 1:
			    //createLeftFloatView();
//			    wm.showDefaultyiLouTV();
			    changeContentView_1();
			 
			break;
		 // 三同号
	        case 2:
	        	//createLeftFloatView();
//	        	wm.showDefaultyiLouTV();
	        	changeContentView_2();
	       
	        break;	
		
		 // 二同号
	      case 6:
	    	    //createLeftFloatView();
//		    	 wm.showDefaultyiLouTV();
	    	    changeContentView_6();
	    	
			break;	
		// 三不同号
	      case 5:
	    	   //todestroyWM();
//	  			wm.hintWM();
	    	   changeContentView_5();
	    
			break;	
		// 二不同号
	      case 8:
	    	   //todestroyWM();
//	  			wm.hintWM();
	    	   changeContentView_8();
			
			break;
//			----------------胆码
	    // 三不同号
	      case 25:
	    	  //todestroyWM();
//	  		  wm.hintWM();
	    	  changeContentView_25();
			break;
//         二不同号
	      case 28:
	    	  //todestroyWM();
//	  			wm.hintWM();
	    	  changeContentView_28();
				
			break;	
		default:
			break;
		}
		if(mBundle!=null){
		   return;	
		}else{
			clear();
		}
		if(ylMap.size()==0){
			booleanTag=true;
			sendgetYilouRequest();//请求遗漏
		}else
        {
			booleanTag=false;
		}
//		setv_ksIssue(true);//设置当前销售期提示 title
		
//		hideRightIcon();// 隐藏定制
	}
	
   @Override
	protected void onMessageReceived(Message msg) 
   {
		super.onMessageReceived(msg);
		mBean = (MessageBean) msg.obj;
		paramsJson=null;
		   switch(msg.arg1) 
		   {
			//		请求成功
			case 1204:
			    String e1,e2,e3, e4, e5, e6, e7,e8,e9,e10,e11,e12;
				if("0".equals(mBean.getA()))
				{
					try {
						// 遗漏值   E1	和值遗漏值  E2	二同号复选遗漏值   E3	三同号遗漏值
						e1 = mBean.getE1();// 和值
                        e2 = mBean.getE2();// 二同号复选
						e3 = mBean.getE3();// 三同号
						//最大遗漏  E4	和值最大遗漏   E5	二同号最大遗漏   E6	三同号最大遗漏
						e4 = mBean.getE4();// 和值
						e5 = mBean.getE5();// 二同号
						e6 = mBean.getE6();// 三同号
						//上次遗漏   E7	和值上次遗漏  E8	二同号上次遗漏  E9	三同号上次遗漏
						e7 = mBean.getE7();// 和值
						e8 = mBean.getE8();// 二同号
						e9 = mBean.getE9();// 三同号
						//投资价值 E10	和值投资价值  E11	二同号投资价值   E12	三同号投资价值
						e10= mBean.getE10();// 和值
						e11= mBean.getE11();// 二同号
						e12= mBean.getE12();// 三同号

//						System.out.println("e1  ="+e1);
//						System.out.println("e2  ="+e2);
//						System.out.println("e3  ="+e3);
//						System.out.println("e4  ="+e4);
//						System.out.println("e5  ="+e5);
//						System.out.println("e6  ="+e6);
//						System.out.println("e7  ="+e7);
//						System.out.println("e8  ="+e8);
//						System.out.println("e9  ="+e9);
//						System.out.println("e10 ="+e10);
//						System.out.println("e11 ="+e11);
//						System.out.println("e12 ="+e12);
					    	//遗漏值(当前)
							saveYL(1,e1,"E1");
							saveYL(2,e2,"E2");
							saveYL(3,e3,"E3");
//							//最大遗漏
							saveYL(1,e4,"E4");
							saveYL(2,e5,"E5");
							saveYL(3,e6,"E6");
//							//上次遗漏
							saveYL(1,e7,"E7");
							saveYL(2,e8,"E8");
							saveYL(3,e9,"E9");
//							//投资价值
							saveYL(1,e10,"E10");
							saveYL(2,e11,"E11");
							saveYL(3,e12,"E12");
					
						if(booleanTag)
						{
							itemChangeaddOmit(YILOU_TAG);//实现刷新遗漏的效果	
							booleanTag=false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					MyToast.showToast(mActivity, mBean.getB());
				}
			break;

		}
		
		
	}
    /**
     * 遗漏缓存为java bean
     * @param tempkey 取值方式
     * @param str
     * @param list
     */
	private void saveYL(int tempkey,String str,String name) throws Exception  {
	List<String> list=new ArrayList<String>();
	if(StringUtils.isBlank(str))
	{
		return;
	}
	switch (tempkey) 
	{
	case 1:
			if(!StringUtils.isBlank(str))
			{
				//和值
				ylJson=new JSONObject(str);
				for (int i = 3 ; i <= 18; i++) 
				{
					list.add(i-3,ylJson.getString(String.valueOf(i)));
				}
			}
		break;
	case 2:
			if(!StringUtils.isBlank(str))
			{
				// 二同号
				ylJson=new JSONObject(str);
				for (int i = 1 ; i <= 6; i++) 
				{
					list.add(i-1,ylJson.getString(String.valueOf(i)+String.valueOf(i)));
				}
			}
		break;
	case 3:
			if(!StringUtils.isBlank(str))
			{
				// 三同号
				 ylJson=new JSONObject(str);
				for (int i = 1 ; i <= 6; i++)
				{
					list.add(i-1,ylJson.getString(String.valueOf(i)+String.valueOf(i)+String.valueOf(i)));
				}
			}
		break;
	}
	  ylMap.put(name, list);
  }
   
   /**
    *  onMessageReceived  回调解析完数据之后 首次会调用的方法 
    *  onMessageReceived  中如果之后再次调用需要设置booleanTag 为 true
    *  
    */
	private void itemChangeaddOmit(boolean tag) 
	{
		String[] temName;
		List<String> jzList = null;
		if(tag)
		{
			 YILOU_TAG =false;
			switch (Integer.parseInt(childId)) 
			{
				 // 和值
				case 1:
					if(sampleStr==null || 16 > sampleStr.length ){
						sampleStr=MyApp.res.getStringArray(R.array.fast3_hz_bonus_text1);// 奖金
					}
					if(sampleNumList==null){
						sampleNumList=new ArrayList<Integer>();
					}
					sampleNumList.clear();
					for (int i = 3; i <= 18; i++) {
						sampleNumList.add(i);
					}
				    temName=new String[]{"E1","E7","E4"};
					inititemViewAndNumber(true,null,sampleStr,temName);//sampleNumList customView 的number 和View 初始化
					LinearLayout.LayoutParams mlayoutparam=new LinearLayout.LayoutParams
							(LinearLayout.LayoutParams.FILL_PARENT,DensityUtil.dip2px(mActivity, 200));
					containers[0].setLayoutParams(mlayoutparam);
					
					containers[0].simpleInit(false, sampleNumList, 1, customView);
				    containers[0].notifyDataSetChanged();
					
					break;
				// 三同号
				case 2:
					if(sampleNumList==null){
						sampleNumList=new ArrayList<Integer>();
					}
					if(sampleTNumList==null){
						sampleTNumList=new ArrayList<Integer>();
					}
					sampleNumList.clear();
					for (int i = 1; i <= 6; i++) {
						sampleNumList.add(i);
					}
					sampleTNumList.clear();
					sampleTNumList.add(1);
					
					sampleStr=MyApp.res.getStringArray(R.array.fast3_sth_bonus_text1);// 奖金
					sampleTStr=MyApp.res.getStringArray(R.array.fast3_sth_tbonus_text);
					
					temName=new String[]{"E3","E9","E6"};
					inititemViewAndNumber(true,null,sampleStr,temName);//sampleNumList customView 的number 和View 初始化
					containers[0].simpleInit(false, sampleNumList, 1, customView);
					containers[0].notifyDataSetChanged();
					
					break;
			   // 二同号
				case 6:
					sampleFStr=new String[]{"11*","22*","33*","44*","55*","66*"};
					temName=new String[]{"E2","E8","E5"};
					inititemViewAndNumber(true,null,sampleFStr,temName);//sampleNumList customView 的number 和View 初始化
					containers[2].simpleInit(false, sampleNumList, 1, customView);
				    containers[2].notifyDataSetChanged();
//						 MyToast.showToast(mActivity, "无法获取当前遗漏，请稍后重试！");
//						 booleanTag=true;
//						 YILOU_TAG=true;
//						 sendgetYilouRequest();
					
					break;
				}
		}else 
		{
			YILOU_TAG=true;
			switch (Integer.parseInt(childId)) 
			 {
			   	 // 和值
					   case 1:
						    sampleStr=MyApp.res.getStringArray(R.array.fast3_hz_bonus_text1);// 奖金
							if(sampleNumList==null){
								sampleNumList=new ArrayList<Integer>();
							}
							sampleNumList.clear();
							for (int i = 3; i <= 18; i++) {
								sampleNumList.add(i);
							}
							if(ylMap.get("E10")!=null){
								 jzList=ylMap.get("E10");
							}
							inititemViewAndNumber(false,jzList,sampleStr,null);//sampleNumList customView 的number 和View 初始化
//							 这里的参数设置必须和布局最外层 方式保持一致 LinearLayout.LayoutParams
							LinearLayout.LayoutParams mlayoutparam=new LinearLayout.LayoutParams
									(LinearLayout.LayoutParams.FILL_PARENT,DensityUtil.dip2px(mActivity, 250));
							containers[0].setLayoutParams(mlayoutparam);
							
							containers[0].simpleInit(false, sampleNumList, 1, customView);
						    containers[0].notifyDataSetChanged();
						break;
					 // 三同号
				        case 2:
							if(sampleNumList==null){
								sampleNumList=new ArrayList<Integer>();
							}
							sampleNumList.clear();
							for (int i = 1; i <= 6; i++) {
								sampleNumList.add(i);
							}
							if(sampleTNumList==null){
								sampleTNumList=new ArrayList<Integer>();
							}
							sampleTNumList.clear();
							sampleTNumList.add(1);
							
							sampleStr=MyApp.res.getStringArray(R.array.fast3_sth_bonus_text1);// 奖金
							sampleTStr=MyApp.res.getStringArray(R.array.fast3_sth_tbonus_text);
							
							if(ylMap.get("E12")!=null){
								 jzList=ylMap.get("E12");
								
							}
							inititemViewAndNumber(false,jzList,sampleStr,null);//sampleNumList customView 的number 和View 初始化
							containers[0].simpleInit(false, sampleNumList, 1, customView);
						    containers[0].notifyDataSetChanged();
				       
				        break;	
					
					 // 二同号
				      case 6:
							if(ylMap.get("E11")!=null){
								 jzList=ylMap.get("E11");
							}
							sampleFStr=new String[]{"11*","22*","33*","44*","55*","66*"};
							inititemViewAndNumber(false,jzList,sampleFStr,null);//sampleNumList customView 的number 和View 初始化
							containers[2].simpleInit(false, sampleNumList, 1, customView);
						    containers[2].notifyDataSetChanged();
						break;	
				
						default:
							break;
					}
			}
		
	}

   /**
    * 复写 baseActiviy  中的 title　
    * 顶部右边第二个图标的  监听事件   用来刷新界面上的遗漏 
    */
    @Override
	protected void onRightIconClicked2() {
		super.onRightIconClicked2();
		if(mActivity.getTitle_nav_iv_right2().isChecked()){
			itemChangeaddOmit(true);
//			MyApp.saveProbeMsg("123-福彩快三","漏->号(当前选号--有遗漏)",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//		  			,"",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"");
		}else{
			itemChangeaddOmit(false);
//			MyApp.saveProbeMsg("123-福彩快三","漏<-号(当前选号--无遗漏)",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//		  			,"",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"");
		}

	}
	
//	/**
//	 * 重写 修改布局的显示方式
//	 */
//    @Override
//	public void changeViewItemyilouShow(boolean b) {
//		super.changeViewItemyilouShow(b);
//		itemChangeaddOmit(b);
//	}
    
	public void changeLotteryView(View addView,int n,int... mVnums){
		fast3_pl_contentLayout.removeAllViews();
		fast3_pl_contentLayout.addView(addView);
		for (int i = 0; i < n; i++) 
		{
			for (int j = 0; j <mVnums.length; j++)
			{
				if(mVnums[j]!=0)
				{
					containers[i]=(MGridView) fast3_pl_contentLayout.findViewById(mVnums[j]);//设置  更换容器的id 
				}
			}
		}
	}
	
     	
     @Override
    public void onStop() 
    {
		super.onStop();
		if(mReceiver!=null)
		{
		 mActivity.unregisterReceiver(mReceiver);
		 mReceiver=null;
		}
		 clear();
//		 if(wm!=null){
//				wm.hintWM();
//		  }
//		 //todestroyWM();//  右边遗漏按钮 WM
    }	

    /**
     *  发送获取遗漏的请求
     *  
     */
	public void sendgetYilouRequest() {
	  if(paramsJson==null){
		  paramsJson=new MessageJson();
		  paramsJson.put("A", "123");
		  submitData(1204, 1204, paramsJson);
	  }
	}
	
	
	private void changeContentView_28() {
		 hideRightIcon2();// 需求改变暂时屏蔽顶部遗漏按钮//title gone  顶部的漏button
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
		setTitleText(getString(R.string.SaleType_set_d_text));
		 
		 fast3_pl_contentLayout.removeAllViews();
		 fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_d_sbth, null));
		containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.fast3_ddm_mgv);//容器0
		containers[1]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.fast3_dtm_mgv);//容器1
		
		fast3_top_tv=(TextView) fast3_pl_contentLayout.findViewById(R.id.fast3_top_tv);//TextView
		fast3_top_tv.setText(getString(R.string.fast3_top_tv));
	    if(sampleNumList==null){
	    	sampleNumList=new ArrayList<Integer>();
	    }
	    sampleNumList.clear();
	    for (int i = 1; i <= 6; i++) {
	    	sampleNumList.add(i);
	    }
		sampleStr=new String[]{"1","2","3","4","5","6"};
		initSimpleItem(sampleStr,customView);	
		initSimpleItem(sampleStr,customView1);	
		containers[0].simpleInit(false, false, sampleNumList, 1, customView);
		containers[1].simpleInit(false, false, sampleNumList, 1, customView1);
		
//		containers[0].simpleInit(false , false, 1, sampleNumList, sampleStr, 47,48, R.drawable.fast3_choose_true, 
//	     		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
//		containers[1].simpleInit(false , false, 1, sampleNumList, sampleStr, 47,48, R.drawable.fast3_choose_true, 
//	     		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
		mutualContainers(0,1);//同号容器  和  不同号容器为互斥 
		
		containers[0].setMaxNum(1);	
		containers[0].setMiniNum(1);
		
		containers[1].setMiniNum(2);
		containers[1].setMiniNum(0);
		
		reloadLotteryNums(0,1);
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
				ticket.setSalesType("2");
				ticket.setLotteryNums(lotteryNums);
				
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				shakeListener.vibrate();
				changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
			}
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
				ticket.setSalesType("2");
				ticket.setLotteryNums(lotteryNums);
				
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				shakeListener.vibrate();
				changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
			}
		});
		
		
	}

	private void changeContentView_25() {
		 hideRightIcon2();// 需求改变暂时屏蔽顶部遗漏按钮//title gone  顶部的漏button
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
		 setTitleText(getString(R.string.SaleType_set_d_text1));
		 fast3_pl_contentLayout.removeAllViews();
		 fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_d_sbth, null));
		containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.fast3_ddm_mgv);//容器0
		containers[1]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.fast3_dtm_mgv);//容器1
		
		
		fast3_top_tv=(TextView) fast3_pl_contentLayout.findViewById(R.id.fast3_top_tv);//TextView
		fast3_top_tv.setText(getString(R.string.fast3_top_tv1));
		
	    if(sampleNumList==null){
	    	sampleNumList=new ArrayList<Integer>();
	    }
    	sampleNumList.clear();
    	for (int i = 1; i <= 6; i++) {
			sampleNumList.add(i);
		}
		sampleStr=new String[]{"1","2","3","4","5","6"};
		initSimpleItem(sampleStr,customView1);
		initSimpleItem(sampleStr,customView2);
		containers[0].simpleInit(false, false, sampleNumList, 1, customView1);
		containers[1].simpleInit(false, false, sampleNumList, 1, customView2);
		
//		containers[0].simpleInit(false , false, 1, sampleNumList, sampleStr, 47,48, R.drawable.fast3_choose_true, 
//	     		   R.color.award_info_tv_title_second_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
//		containers[1].simpleInit(false , false, 1, sampleNumList, sampleStr, 47,48, R.drawable.fast3_choose_true, 
//	     		   R.color.award_info_tv_title_second_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
		
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
		containers[0].setMaxNum(2);
		containers[1].setMiniNum(2);
		
		mutualContainers(0,1);//同号容器  和  不同号容器为互斥
		reloadLotteryNums(0,1);
		
		containers[0].setActionUpListener(new ActionUpListener() {
				@Override
				public void onActionUp() {
					ticket.setSalesType("2");
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());	
				}
			});
		containers[1].setActionUpListener(new ActionUpListener() {
			@Override
			public void onActionUp() {
				ticket.setSalesType("2");
				ticket.setLotteryNums(lotteryNums);
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				shakeListener.vibrate();
				changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
			}
		});
	}



	private void changeContentView_8() {
			 setShakeNotice("");
			 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
			 changeShakeVisibility(true);
			 setTitleText(getString(R.string.SaleType_set_p_text));
			 hideRightIcon2();// 需求改变暂时屏蔽顶部遗漏按钮//title gone  顶部的漏button
			 
		 
		    fast3_pl_contentLayout.removeAllViews();
		    fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_ebth, null));
			containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.ebth_mgv);//容器1
			
		    if(sampleNumList==null){
		    	sampleNumList=new ArrayList<Integer>();
		    }
	    	sampleNumList.clear();
	    	for (int i = 1; i <= 6; i++) {
				sampleNumList.add(i);
			}
			sampleStr=new String[]{"1","2","3","4","5","6"};
			initSimpleItem(sampleStr,customView1);	
			containers[0].simpleInit(false, false, sampleNumList, 1, customView1);
//			containers[0].simpleInit(false , false, 1, sampleNumList, sampleStr, 94,48, R.drawable.fast3_choose_true, 
//		    		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
			containers[0].setMiniNum(2);
			containers[0].setMaxNum(0);
			reloadLotteryNums(0);
			
			containers[0].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
				}
			});
	}



	private void changeContentView_5() {
		   hideRightIcon2();// 需求改变暂时屏蔽顶部遗漏按钮//title gone  顶部的漏button
		   //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		   changeShakeVisibility(true);
	       setShakeNotice("");
		   setTitleText(getString(R.string.SaleType_set_p_text1));
		   fast3_pl_contentLayout.removeAllViews();
			
		    fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_sbth, null));
			containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.sbth_mgv);//容器1
			
			containers[1]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.sbth_tmgv);//三连号通选
	
			if(sampleTNumList==null){
				sampleTNumList=new ArrayList<Integer>();
			}
			sampleTNumList.clear();
			sampleTNumList.add(0);
		    if(sampleNumList==null){
		    	sampleNumList=new ArrayList<Integer>();
		    }
	    	sampleNumList.clear();
	    	for (int i = 1; i <= 6; i++) {
				sampleNumList.add(i);
			}
			sampleStr=new String[]{"1","2","3","4","5","6"};
			initSimpleItem(sampleStr,customView1);	
			containers[0].simpleInit(false, false, sampleNumList, 1, customView1);
//			containers[0].simpleInit(false , false, 1, sampleNumList, sampleStr, 47,48, R.drawable.fast3_choose_true, 
//					R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
			 customView.clear();
			 Custom_PL120IV  mctmiv=new Custom_PL120IV(mActivity);
			  mctmiv.initView(false,R.drawable.fast3_choose_true,R.drawable.fast3_choose_false ,"三连号通选", null,null);		  
			 customView.add(mctmiv);
			 containers[1].simpleInit(false, sampleTNumList, 1, customView);
			 containers[1].setVisibility(View.VISIBLE);
//			 sampleTStr=MyApp.res.getStringArray(R.array.fast3_slh_tbonus_text);
			 containers[1].simpleInit(false , false, 1, sampleTNumList, sampleTStr, 290,48, R.drawable.fast3_choose_true, 
		    		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
		   
			containers[0].setMiniNum(3);
			containers[1].setMiniNum(0);
		    containers[0].setMaxNum(0);
		    containers[1].setMaxNum(0);
		    reloadLotteryNums(0,1);
	    	
		    containers[0].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();//震动100毫秒
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
				}
			});
		    containers[1].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();//震动100毫秒
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
				}
			});
	}



	private void changeContentView_6() {
		 List<String> jzList=null;
		    showRightIcon2();  //需求改变暂时屏蔽
		    //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		    changeShakeVisibility(true);
		    cancelMutualContainers();// 取消互斥
		    setTitleText(getString(R.string.fast3_eth_text));
		    setShakeNotice(getString(R.string.fast3_show_tv3));
		    fast3_pl_contentLayout.removeAllViews();
			
		    fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_eth, null));
			containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.eth_one_mgv);//容器0
			containers[1]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.eth_two_mgv);//容器1
			containers[2]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.eth_three_mgv);//容器2
			mutualContainers(0,1);//同号容器  和  不同号容器为互斥
			if(sampleNumList==null){
				sampleNumList=new ArrayList<Integer>();
			}
			sampleNumList.clear();
			for (int i = 1; i <= 6; i++) {
				sampleNumList.add(i);
			}
			sampleStr=new String[]{"11","22","33","44","55","66"};
			sampleBStr=new String[]{"1","2","3","4","5","6"};
			sampleFStr=new String[]{"11*","22*","33*","44*","55*","66*"};
			
			initSimpleItem(sampleStr,customView1);	
			containers[0].simpleInit(false, false, sampleNumList, 1, customView1);
			initSimpleItem(sampleBStr,customView2);	
			containers[1].simpleInit(false, false, sampleNumList, 1, customView2);
			
//	        containers[0].simpleInit(false , false, 1, sampleNumList, sampleStr, 47,48, R.drawable.fast3_choose_true, 
//	     		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);//同号
//			containers[1].simpleInit(false , false, 1, sampleNumList, sampleBStr, 47,48, R.drawable.fast3_choose_true, 
//		    		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);//不同号
			
			if(ylMap.get("E11")!=null){
				 jzList=ylMap.get("E11");
			}
			inititemViewAndNumber(false,jzList,sampleFStr,null);//sampleNumList customView 的number 和View 初始化
			
			containers[2].simpleInit(false, sampleNumList, 1, customView);
			
//			containers[2].simpleInit(false , false, 1, sampleNumList, sampleFStr, 69,48, R.drawable.fast3_choose_true, 
//		    		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);//复选
			
			containers[0].setMiniNum(1);
			containers[1].setMiniNum(1);
			
			containers[2].setMiniNum(0);
			containers[0].setMaxNum(0);
			containers[1].setMaxNum(0);
			containers[2].setMaxNum(0);
			reloadLotteryNums(0,1,2);
			
			containers[0].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
					
				}
			});
			containers[1].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
					
				}
			});
			containers[2].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					ticket.setLotteryNums(lotteryNums);
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();
					changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
					
				}
			});
		
	}
	/**
	 * 简单初始化 选号itemView
	 * @param sampleStr
	 */
	private void initSimpleItem(String[] sampleStr,List<View> customView) {
		 customView.clear();
		for (int i = 0; i < sampleStr.length; i++) {
			Custom_PL120IV  mctmiv=new Custom_PL120IV(mActivity);
			////System.out.println(" sampleStr[i] ---: "+sampleStr[i]);
			String text= sampleStr[i];//getString(R.string.fs_sth_th);//三同号通选
			mctmiv.initView(false,R.drawable.fast3_choose_true,R.drawable.fast3_choose_false ,text, null,null);		  
			customView.add(mctmiv);
		}
	}

	private void changeContentView_2() {
		 List<String> jzList=null;
		showRightIcon2();
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.fast3_sth_text));
		
	    setShakeNotice(getString(R.string.fast3_show_tv1));
	    
		fast3_pl_contentLayout.removeAllViews();
		fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_sth, null));
		containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.sth_mgv);//和值选号容器1
		containers[1]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.sth_tmgv);
		
		if(sampleNumList==null){
			sampleNumList=new ArrayList<Integer>();
		}
		sampleNumList.clear();
		for (int i = 1; i <= 6; i++) {
			sampleNumList.add(i);
		}
		if(sampleTNumList==null){
			sampleTNumList=new ArrayList<Integer>();
		}
		sampleTNumList.clear();
		sampleTNumList.add(1);
		
		sampleStr=MyApp.res.getStringArray(R.array.fast3_sth_bonus_text1);// 奖金
//		sampleTStr=MyApp.res.getStringArray(R.array.fast3_sth_tbonus_text);
		if(ylMap.get("E12")!=null){
			 jzList=ylMap.get("E12");
			
		}
		inititemViewAndNumber(false,jzList,sampleStr,null);//sampleNumList customView 的number 和View 初始化
		
		containers[0].simpleInit(false, sampleNumList, 1, customView);
		
//		if(e3_yl.size()==sampleStr.length){
//			for (int i = 0; i < e3_yl.size(); i++) {
//				sampleStr[i] +="\r\n"+e3_yl.get(i);
//			}
//		}
//		containers[0].simpleInit(false, false, 1, sampleNumList, sampleStr, 94, 48, R.drawable.fast3_choose_true, 
//	    		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
		 customView1.clear();
		 Custom_PL120IV  mctmiv=new Custom_PL120IV(mActivity);
	   	  String text= "三同号通选";//getString(R.string.fs_sth_th);//三同号通选
		  String tzjztext = "任意一个豹子号开出即中40元";//getString(R.string.fs_sth_th1);//任意一个豹子号开出即中40元
		  mctmiv.initView(false,R.drawable.fast3_choose_true,R.drawable.fast3_choose_false ,text, "",tzjztext);		  
		 customView1.add(mctmiv);
		 containers[1].simpleInit(false, sampleTNumList, 1, customView1);
		 
//		containers[1].simpleInit(false, false, 1, sampleTNumList, sampleTStr, 290, 48, R.drawable.fast3_choose_true, 
//	    		   R.color.fast3_text_color, R.color.bottom_nav_text_checked, R.drawable.fast3_choose_false);
		
		
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(0);
		
		containers[0].setMaxNum(0);
		containers[1].setMaxNum(0);
		reloadLotteryNums(0,1);
		mBundle=getMyBundle();
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
				
				
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
			}
			
			
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                ticket.setLotteryNums(lotteryNums);
				
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				
				changeBtnbgAndTextColors(ticket.formatLotteryNums().length());
			}
		
			
		});
	}

    /**
     * 和值
     */

	private void changeContentView_1() {
		 List<String> jzList=null;
		     showRightIcon2();  //需求改变暂时屏蔽
			//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
			changeShakeVisibility(true);
		    setTitleText(getString(R.string.fast3_hz_text));//和值
		    setShakeNotice(getString(R.string.fast3_show_tv));//摇一摇提示
			fast3_pl_contentLayout.removeAllViews();
			fast3_pl_contentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_pl120_hz, null));
			containers[0]=(MGridView) fast3_pl_contentLayout.findViewById(R.id.hz_mgv);//和值选号容器1
			chbox0=(CheckBox) fast3_pl_contentLayout.findViewById(R.id.chbox0);//大
			chbox1=(CheckBox) fast3_pl_contentLayout.findViewById(R.id.chbox1);//小
			chbox2=(CheckBox) fast3_pl_contentLayout.findViewById(R.id.chbox2);//单
			chbox3=(CheckBox) fast3_pl_contentLayout.findViewById(R.id.chbox3);//双
			
			sampleStr=MyApp.res.getStringArray(R.array.fast3_hz_bonus_text1);// 奖金
			if(sampleNumList==null){
				sampleNumList=new ArrayList<Integer>();
			}
			sampleNumList.clear();
			for (int i = 3; i <= 18; i++) {
				sampleNumList.add(i);
			}
			if(ylMap.get("E10")!=null){
				 jzList=ylMap.get("E10");
				
			}
			inititemViewAndNumber(false,jzList,sampleStr,null);//sampleNumList customView 的number 和View 初始化
			
			containers[0].simpleInit(false, sampleNumList, 1, customView);
	        containers[0].setMiniNum(1);//和值选号容器1
			containers[0].setMaxNum(0);//和值选号容器1
			
			reloadLotteryNums(0);
			//大小单双的CheckBox 监听
			setHzLayoutListen();

			containers[0].setActionUpListener(new ActionUpListener() {
				
				@Override
				public void onActionUp() {
					        IS_UP_INCIDENT=true;
					        
						    accordWitchDXDS(lotteryNums.get(0).toString());//判断是否符合大小单双
						    
							basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
							
							shakeListener.vibrate();
							
							IS_UP_INCIDENT=false;
							changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
				}

				
			});
			
			
	}
    /**
     * 初始化 和值界面text
     * @param tzjzList
     * @param initStr
     * @param isShowJZ 是否显示投资价值  (true 显示投资价值  false相反)
     * @param recommendNnmber
     */
	private void inititemViewAndNumber(boolean isShowJZ,List<String> tzjzList, String[] initStr,String[] temName) {
		Custom_PL120IV  mctmiv=null;
		if(isShowJZ)
		{ 
			String dqStr=null;
			String scStr=null;
			String zdStr=null;
			if(ylMap.get(temName[0])==null || ylMap.get(temName[1]) == null || ylMap.get(temName[2]) ==null){
				return;
			}else{
				if(ylMap.get(temName[0]).size() >= initStr.length 
						&& ylMap.get(temName[1]).size() >= initStr.length 
						&& ylMap.get(temName[2]).size() >= initStr.length){
				 customView.clear();
				 for (int i = 0; i < initStr.length ; i++) {
					mctmiv=new Custom_PL120IV(mActivity);
					dqStr="当前:"+ylMap.get(temName[0]).get(i);
					scStr="上次:"+ylMap.get(temName[1]).get(i);
					zdStr="最大:"+ylMap.get(temName[2]).get(i);
					mctmiv.initYLView(R.drawable.fast3_choose_true,R.drawable.fast3_choose_false,dqStr, scStr, zdStr);
					customView.add(mctmiv);
			     }
			  }
		    }
		  return;
		}else if(tzjzList!=null){
			if(tzjzList.size() >= initStr.length && sampleNumList.size() >= initStr.length ){//  不显示 遗漏
			    customView.clear();
			    String tzjztext="";
			    String text="";
			    String  bonustext="";
				for (int i = 0; i < initStr.length; i++) {
					mctmiv=new Custom_PL120IV(mActivity);
					if("6".equals(childId)){// 二同号 特殊性
						text=initStr[i];
						tzjztext="投资价值:"+tzjzList.get(i).trim();
						mctmiv.initView(false,R.drawable.fast3_choose_true,R.drawable.fast3_choose_false ,text, bonustext,tzjztext);
						if(AppUtil.isDouble(tzjzList.get(i).trim())){
							double temp=Double.parseDouble(tzjzList.get(i).trim());
							if(temp >= ConstantValue.RECOMMEND_COMPARISON){//  指定推荐号
								mctmiv.initView(false,R.drawable.fast3_choose_tj_true,R.drawable.fast3_choose_tj_false ,text, bonustext,tzjztext);
							}
						}
					}else{
						text=String.valueOf(sampleNumList.get(i));
						tzjztext="投资价值:"+tzjzList.get(i).trim();
						bonustext=initStr[i];
						mctmiv.initView(R.drawable.fast3_choose_true,R.drawable.fast3_choose_false ,text, bonustext,tzjztext);
						if(AppUtil.isDouble(tzjzList.get(i).trim())){
							double temp=Double.parseDouble(tzjzList.get(i).trim());
							if(temp >= ConstantValue.RECOMMEND_COMPARISON){//  指定推荐号
								mctmiv.initView(R.drawable.fast3_choose_tj_true,R.drawable.fast3_choose_tj_false ,text, bonustext,tzjztext);
							}
						}
					}
					customView.add(mctmiv);
				}
			}
		 }else{//  没有数据 默认初始化
			    customView.clear();
			    for (int i = 0; i < initStr.length; i++) 
			    {
				  mctmiv=new Custom_PL120IV(mActivity);
				  String tzjztext="投资价值:-.-";
				  String text=String.valueOf(sampleNumList.get(i));
				  String  bonustext=initStr[i];
				  mctmiv.initView(R.drawable.fast3_choose_true,R.drawable.fast3_choose_false ,text, bonustext,tzjztext);		  
				  customView.add(mctmiv);
			   }
		 }
	}
    //判断时候符合大小单双
	private void accordWitchDXDS(String listToStr) {
//		1	大
		if("[11, 12, 13, 14, 15, 16, 17, 18]".equals(listToStr)){
			 chbox0.setChecked(true);
			 chbox1.setChecked(false);
			 chbox2.setChecked(false);
			 chbox3.setChecked(false);
			
//		2	小
		}else if("[3, 4, 5, 6, 7, 8, 9, 10]".equals(listToStr)){
			 chbox0.setChecked(false);
			 chbox1.setChecked(true);
			 chbox2.setChecked(false);
			 chbox3.setChecked(false);
//	    30   单
		}else if("[3, 5, 7, 9, 11, 13, 15, 17]".equals(listToStr)){
			 chbox0.setChecked(false);
			 chbox1.setChecked(false);
			 chbox2.setChecked(true);
			 chbox3.setChecked(false);
//		40   双	
		}else if("[4, 6, 8, 10, 12, 14, 16, 18]".equals(listToStr)){
			 chbox0.setChecked(false);
			 chbox1.setChecked(false);
			 chbox2.setChecked(false);
			 chbox3.setChecked(true);
//	    31 大 单	
		}else if("[11, 13, 15, 17]".equals(listToStr)){
			 chbox0.setChecked(true);
			 chbox1.setChecked(false);
			 chbox2.setChecked(true);
			 chbox3.setChecked(false);
//		32  小 单
		}else if("[3, 5, 7, 9]".equals(listToStr)){
			 chbox0.setChecked(false);
			 chbox1.setChecked(true);
			 chbox2.setChecked(true);
			 chbox3.setChecked(false);
//		41 大双	
		}else if("[12, 14, 16, 18]".equals(listToStr)){
			 chbox0.setChecked(true);
			 chbox1.setChecked(false);
			 chbox2.setChecked(false);
			 chbox3.setChecked(true);
//		42 小双
		}else if("[4, 6, 8, 10]".equals(listToStr)){
			 chbox0.setChecked(false);
			 chbox1.setChecked(true);
			 chbox2.setChecked(false);
			 chbox3.setChecked(true);
		}else {
			clearChbox();	
		}
	}

	private void setHzLayoutListen() {
		chbox0.setOnCheckedChangeListener(this);
		chbox1.setOnCheckedChangeListener(this);
		chbox2.setOnCheckedChangeListener(this);
		chbox3.setOnCheckedChangeListener(this);
		
	}

	@Override
	public void onCheckedChanged(CompoundButton cpBtn, boolean isChecked) {
			switch (cpBtn.getId()) {
			//		大
					case R.id.chbox0:
						if(chbox0.isChecked()){
							chbox1.setChecked(false);//大和小互斥
							addTag=calcaddtag();
						}else{
							addTag=calcaddtag();
						}
						
						setMVshowSelectBall(addTag);
						
						break;
			//		小
					case R.id.chbox1:
					       if(chbox1.isChecked()){
					    	   chbox0.setChecked(false);//大和小互斥
					    	       addTag=calcaddtag();
								}else{
									addTag=calcaddtag();
								}
					    	setMVshowSelectBall(addTag);
						break;
			//		单
					case R.id.chbox2:
						    if(chbox2.isChecked()){
						    	chbox3.setChecked(false);//单和双互斥
								addTag=calcaddtag();
							}else{
								addTag=calcaddtag();
							}
							 setMVshowSelectBall(addTag);
							 
						break;
			//		双
					case R.id.chbox3:
							if(chbox3.isChecked()){
								chbox2.setChecked(false);//单和双互斥
								addTag=calcaddtag();
							}else{
								addTag=calcaddtag();
							}
							
							setMVshowSelectBall(addTag);
							
						break;
	               
					default:
						break;
		           }
					basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
					shakeListener.vibrate();//震动100毫秒
					changeBtnbgAndTextColors(2);// 直接指定
			
		
	}
//	获得总值
	private int  calcaddtag(){
		return getDSaddtag()+getDXaddtag();
	}
	//单双任选一的tag 0  30 40 三种值
	private int getDSaddtag() {
		if(chbox2.isChecked()){
			return Integer.parseInt(chbox2.getTag().toString());
		}else if(chbox3.isChecked()){
			return Integer.parseInt(chbox3.getTag().toString());
		}
			
		return 0;
	}
	//大小任选一的tag 0  1 2 三种值
	private int getDXaddtag() {
		if(chbox0.isChecked()){
			return Integer.parseInt(chbox0.getTag().toString());
		}else if(chbox1.isChecked()){
			return Integer.parseInt(chbox1.getTag().toString());
		}
			
		return 0;
	}



	//1  2  30 40   31 32  41  42  0共九种情况
	 private void setMVshowSelectBall(int m) {
		 if(IS_UP_INCIDENT){
			 return; 
		 }
		 switch (m) {
				 case 0:
//					 checkBox 都未选中
						 containers[0].clear();
						 containers[0].notifyDataSetChanged();
				      break;
		//		大
				case 1:
					containers[0].clear();
					selectballList=containers[0].getSelectedBalls();
					for (int i = 11; i <=18; i++) {
						selectballList.add(i);
					}
					containers[0].notifyDataSetChanged();
					
					break;
		//	       小
				case 2:
					    containers[0].clear();
						selectballList=containers[0].getSelectedBalls();
						for (int i = 3; i <=10; i++) {
							selectballList.add(i);
						}
						containers[0].notifyDataSetChanged();
					
					  break;
		//	    单
				case 30:
					    containers[0].clear();
						selectballList=containers[0].getSelectedBalls();
						 for (int i = 3; i <=17; i++) {
							if(1==(i%2)){
								selectballList.add(i);
							}
						}
						containers[0].notifyDataSetChanged();
					
					  break;
			//   双
				case 40:
					  containers[0].clear();
						selectballList=containers[0].getSelectedBalls();
						 for (int i = 4; i <=18; i++) {
							if(0==(i%2)){
								selectballList.add(i);
							}
						}
						containers[0].notifyDataSetChanged();
					  break;
			//   大单
				case 31:
					containers[0].clear();
					selectballList=containers[0].getSelectedBalls();
					 for (int i = 11; i <=17; i++) {
						if(1==(i%2)){
							selectballList.add(i);
						}
					}
					containers[0].notifyDataSetChanged();
					  break;
			//  小单
				case 32:
					containers[0].clear();
					selectballList=containers[0].getSelectedBalls();
					 for (int i = 3; i <=9; i++) {
						if(1==(i%2)){
							selectballList.add(i);
						}
					}
					containers[0].notifyDataSetChanged();
					
					  break;
			//  大双
				case 41:
					
					  containers[0].clear();
						selectballList=containers[0].getSelectedBalls();
						 for (int i = 12; i <=18; i++) {
							if(0==(i%2)){
								selectballList.add(i);
							}
						}
						containers[0].notifyDataSetChanged();
					  break;
			//  小双
				case 42:
					containers[0].clear();
					selectballList=containers[0].getSelectedBalls();
					 for (int i = 4; i <=10; i++) {
						if(0==(i%2)){
							selectballList.add(i);
						}
					}
					containers[0].notifyDataSetChanged();
					
				   break;
				default:
					break;
		    }
	}
	 
    /* 清空所有选中的大小单双*/
	 private  void clearChbox(){
		 
		 if(chbox0==null||
				 chbox1==null||
				 chbox2==null||
				 chbox3==null){
			return; 
		 }
		 chbox0.setChecked(false);
		 chbox1.setChecked(false);
		 chbox2.setChecked(false);
		 chbox3.setChecked(false);
	 }
	/**
	 * 摇一摇随机选号
	 */
	protected void doShake() {
		clear();
		lotteryNums.clear();
		for (int i = 0; i < containers.length; i++) { 
			MGridView container = containers[i];
			if(container != null && container.getVisibility() == View.VISIBLE ){//是否设置为摇晃获取随机数&& container.isShakeble()
				lotteryNums.add(container.randomSelect());//最好在最后点确定的时候复制
			}
		}
//		ticket.setLotteryNums(lotteryNums);
		
		basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
		
	}
	

	

	   /**
	    *  排列PL120 已经复写
	    * 
		 * 子类可以复写该方法 来更改 确认按钮的逻辑.
		 * 一般情况下不用更改
		 */
		protected void ok(){
			mBundle=getMyBundle();
               //      普通票
        	if(ticket!=null&&ticket.isTicketAvailable())
        	{
        		if("5".equals(childId) && "0".equals(salesType)){
        			int ballN=ticket.getLotteryNums().get(0).size();
        			if(ballN > 0 && ballN < 3){
        				MyToast.showToast(mActivity, "三不同号至少选择3个号码！");
        				return;
        			}
        		}else if("6".equals(childId) && "0".equals(salesType) ){//二同号
        			int ballN  = ticket.getLotteryNums().get(0).size();
    			    ballN += ticket.getLotteryNums().get(1).size();
    			if(ballN > 0 && ballN < 2)
    			{
    				MyToast.showToast(mActivity, "二同号至少选择2个号码！");
    				return;
    			 }
    		    }
        		
            	if(mBundle != null && mBundle.get("position") != null)
            	{//当 该票是从 倍投 界面返回到该界面来修改的时候,只要把 ticket 中的 lotteryNums 重新生成一次
                		
            	    	ticket.createTicket();
                		MyApp.order.setLotteryId(lotteryId);
                		MyApp.order.setIssue(CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getIssue());
                		start(SsqBetorder.class);
//                		-------------------4-9添加
                		resetBuyTicket();
            			is_clear_containers_required = true;
                		return;
            	}
            	//如果该票不是从 倍投界面返回修改的,则要加到 订单 中
            		MyApp.order.getTickets().add(0,ticket.createTicket());
            		MyApp.order.setLotteryId(lotteryId);
            		MyApp.order.setIssue(CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getIssue());
            		start(SsqBetorder.class);
//            		--------------------4-9添加
        			resetBuyTicket();
        			is_clear_containers_required = true;
        	}else
        	{
        		if("5".equals(childId) && "2".equals(salesType) ){// 胆拖三不同号
        			MyToast.showToast(mActivity, "请至少选择3注" );
        		}else if("8".equals(childId) && "2".equals(salesType)){
        			MyToast.showToast(mActivity, "请至少选择2注" );
        		}else{
        			MyToast.showToast(mActivity, "请至少选择一票" );
        		}
	        }
		}

		
	/**
	 * 清空按钮的抽取
	 */
	protected void clear(){
		basebuy_tv_notice.setText("");
		for (MGridView container : containers) {
			if(container != null)
				container.clear();
		}
		
		clearChbox();
	}



	
	/**
	 * 实现刷新遗漏  的接口
	 */
	@Override
	public void onReceiveYilou_start() {
		    
	}
	/**
	 * 实现刷新遗漏  的接口
	 */
	@Override
	public void onReceiveYilou_stop() {
		String issueStr=CustomTagEnum.lottery_k3.getIssue();
		if(issueStr==null || "".equals(issueStr) ){
			return;
		}
		dialog=new TCDialogs(mActivity);
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
		fast3_await_cb.setVisibility(View.VISIBLE); //title 时间提示旁边显示等待开奖中
	    booleanTag =true;
	    YILOU_TAG=false;
		sendgetYilouRequest();//重新请求 实现遗漏的更新
		
	}


}
