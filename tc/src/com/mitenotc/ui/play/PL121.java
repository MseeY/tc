package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.CustomPK;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MGridView.ActionUpListener;
import com.mitenotc.ui.ui_utils.RefreshYiLouReceiver;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 121	山东快乐扑克3
 * 
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "121",salesType = "0" ,childId = "0")
public class PL121 extends BaseBuyFragment implements RefreshYiLou{
     
	private View titleView;
	private View initMainView;
	private LinearLayout initcontentLayout;
//	为了设置RadioButton 上的字体大小区别和奖金颜色 
	private RadioButton pk_rbtn1;
	private RadioButton pk_rbtn2;
	private RadioButton pk_rbtn3;
	
	private RadioButton pk_rbtn4;
	private RadioButton pk_rbtn5;
	private RadioButton pk_rbtn6;
	
	private RadioButton pk_rbtn7;
	private RadioButton pk_rbtn8;
	private RadioButton pk_rbtn9;
	
	private RadioButton pk_rbtn10;
	private RadioButton pk_rbtn11;
	private RadioButton pk_rbtn12;
	private Spannable textSp;

	private TextView pk3_tv_one;
	private TextView pk3_tv_two;

	private TextView pk3_tv_oneMark;
	private TextView pk3_tv_twoMark;
	
	private List<Integer> sampleNums_one=new ArrayList<Integer>() ;
	private List<Integer> sampleNums_two=new ArrayList<Integer>() ;
	private List<View>    customItems_one=new ArrayList<View>();
	private List<View>    customItems_two=new ArrayList<View>();
	private CustomPK ctmPK;//自定义  扑克
	private android.widget.RelativeLayout.LayoutParams ctmPkWidgetParam;
	private android.widget.RelativeLayout.LayoutParams ctmPkWidgetParam2;
	
	private  RefreshYiLouReceiver mReceiver;
	private  MessageJson paramsJson;
	private  MessageBean mBean;
	
	private List<String> rx_Yl=new ArrayList<String>();
	private List<String> th_YL =new ArrayList<String>();
	private List<String> sz_YL =new ArrayList<String>();
	private List<String> ths_YL =new ArrayList<String>();
	private List<String> bz_YL =new ArrayList<String>();
	private List<String> dz_YL =new ArrayList<String>();
	private List<String> bx_YL =new ArrayList<String>();  //整合玩法 遗漏 对子包选 豹子包选 同花包选 顺子包选 同花顺包选 
	private JSONObject ylJson=null;
	private TCDialogs dialog;//提示框
	private static boolean booleanTag;// 遗漏改变刷新标志
	
    private static String e1;	
    private static String e2;	
    private static String e3;	
    private static String e4;
    private static String e5;
    private static String e6;
	
	  
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
	        	 mReceiver =new RefreshYiLouReceiver("121"); 
	        	 mReceiver.setmRefreshYiLou(this);
	        	 IntentFilter mFilter=new IntentFilter();
	        	 mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+121+"_start_loading");
	        	 mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+121+"_stop_loading");
	        	 mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+121+"_awardinfo_received");
	        	 mActivity.registerReceiver(mReceiver, mFilter); 	
	         }
	    } 
	    /**
	     *  发送获取遗漏的请求
	     *  
	     */
		public void sendgetYilouRequest() {
			  if(paramsJson==null){
				  paramsJson=new MessageJson();
				  paramsJson.put("A", "121");
//				  ////System.out.println("paramsJson "+paramsJson.toString());
				  submitData(1204, 1204, paramsJson);
			  }
		}
	@Override
	protected void initContainers() {
//		实现个性化title 接口（高频彩种）
	}
	@Override
	public View initInflateTitleView(MessageBean msgb) {
		String tempStr="第--期: ";
		if(msgb!=null&&6 < msgb.getC().length()){// 为了安全
			tempStr="第"+msgb.getC().substring(6, msgb.getC().length())+"期: ";
		}
		return getPLtitle_view121(tempStr,msgb.getE());//String issueStr = item.getC();
	}
	@Override
	public BaseBuyAdapter customBuyAdaper(){
		return new BaseBuyAdapter(){
			@SuppressLint("NewApi")
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
					holder.v1=(View) convertView.findViewById(R.id.v1);
					holder.v2=(View) convertView.findViewById(R.id.v2);
					holder.line_end=(View) convertView.findViewById(R.id.line_end);
					
					convertView.setTag(holder);
				}else{
					holder=(Viewholder) convertView.getTag();
				}
				
				MessageBean item = mList.get(position);
				holder.tv1.setText(item.getC()+"期");//历史期号
				String awardNums = item.getE();
				if(item.getA() != null && "121".equals(item.getA()) && item.getE()!=null && !"".equals(item.getE())){//-------TODO 后期 需要抽取
						holder.Rlayout.removeAllViews();
						if(itemView==null){
							item_holder=new Viewholder_item();
							itemView=mInflater.inflate(R.layout.history_121,null);
							
							item_holder.num_tv1=(TextView) itemView.findViewById(R.id.num_tv1);
							item_holder.num_tv2=(TextView) itemView.findViewById(R.id.num_tv2);
							item_holder.num_tv3=(TextView) itemView.findViewById(R.id.num_tv3);
							item_holder.text_tv=(TextView) itemView.findViewById(R.id.text_tv);
							FormatUtil.setTypeface(item_holder.num_tv1, 1);
							FormatUtil.setTypeface(item_holder.num_tv2, 1);
							FormatUtil.setTypeface(item_holder.num_tv3, 1);
//							 try {
//								final Typeface fontFace1 = Typeface.createFromAsset(MyApp.context.getAssets(),"fonts/460-cai978.ttf");
//								 item_holder.num_tv1.setTypeface(fontFace1);
//								 item_holder.num_tv2.setTypeface(fontFace1);
//								 item_holder.num_tv3.setTypeface(fontFace1);
//							 } catch (Exception e) {
//								e.printStackTrace();
//								System.out.println("121----------->"+"文件加载失败:721-CAI978.ttf "+ e.getMessage());
//							 }
							holder.Rlayout.addView(itemView);
							
							itemView.setTag(item_holder);
						}
					    item_holder=(Viewholder_item) itemView.getTag();
						
						showLotteryKLPK(item_holder.num_tv1,item_holder.num_tv2,item_holder.num_tv3,item_holder.text_tv, awardNums);
						holder.tv1.setTextColor(MyApp.res.getColor(R.color.blue1)); //期号字体颜色
						holder.v2.setBackgroundColor(MyApp.res.getColor(R.color.blue1));
						convertView.setBackgroundColor(MyApp.res.getColor(R.color.blue3));
//						holder.v1.setBackgroundResource(MyApp.res.getDrawable(R.drawable.circle_blue));
						holder.v1.setBackgroundResource(R.drawable.circle_blue);
						hide(holder.line_end);//隐藏下标线
					}else {
						if(awardNums.contains("+")){
							awardNums = awardNums.replace("+", "&#160;&#160;<font color=#007aff>");
							awardNums += "</font>";
						}
						awardNums = awardNums.replace(",", "&#160;&#160;");
						holder.tv2.setText(Html.fromHtml(awardNums));//历史期号对应的历史投注号码
						
					}
				return convertView;
			}

		};
	}

	/**
	 *  快乐扑克显示方式
	 *  历史加奖号码   在投注详情之中有调用到此方法
	 * @param item_holder
	 * @param awardNums
	 */
		public static void showLotteryKLPK(TextView tv_num1,TextView tv_num2,TextView tv_num3,TextView tv_text4,
				String awardNums) {
			String[] numStr=awardNums.split(",");//  扑克的整体数据  ：111,102,302
			String[] newNumStr=new String[3]; //拆分出的扑克对应text（后两位是text, 前一位数字表示的是 扑克花色)
			
			if(numStr.length==3){//为了安全  加以判断 
				
				for (int i = 0; i < numStr.length; i++) 
				{// 拆分出具体扑克  text
					newNumStr[i]=numStr[i].substring(1, 3);
				}
				String tempStr="";
				//  排序 小---> 大  为了便于 判断是否为 顺子扑克牌
				for (int i = 0; i < newNumStr.length; i++) {
					for (int j = 0; j < newNumStr.length; j++) {
						if(Integer.parseInt(newNumStr[i]) < Integer.parseInt(newNumStr[j])){
							tempStr = newNumStr[i];
							newNumStr[i]=newNumStr[j];
							newNumStr[j]=tempStr;
						}
					}
				}
				
				String pk_type="";//  数据显示 的特殊扑克的提示text
				if( numStr[0].substring(0, 1).equals(numStr[1].substring(0, 1)) &&
						numStr[1].substring(0, 1).equals(numStr[2].substring(0, 1))){
					//  同花（判断每个号码的第一位数字是否相同 决定是否为同花）
					pk_type="同花";
					
					//	同花顺： 是指在同花的前提下顺子
					if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
							Integer.parseInt(newNumStr[1])+1 == Integer.parseInt(newNumStr[2])){// 普通顺子
						pk_type="同花顺";
					}else if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
							1==(Integer.parseInt(newNumStr[1]) - Integer.parseInt(newNumStr[2]))){ // AQK  特殊顺子的判断
						pk_type="同花顺";
					}
					
				}else{//非同花
					
					if(newNumStr[0].equals(newNumStr[1]) && newNumStr[1].equals(newNumStr[2])){//  豹子
						pk_type="豹子";
					}else if(newNumStr[0].equals(newNumStr[1]) || newNumStr[1].equals(newNumStr[2])){//对子
						pk_type="对子";
					}else {
						if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
								Integer.parseInt(newNumStr[1])+1 == Integer.parseInt(newNumStr[2])){// 普通顺子
							pk_type="顺子";
						}else if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
								1==(Integer.parseInt(newNumStr[1]) - Integer.parseInt(newNumStr[2]))){ // AQK  特殊顺子的判断
							pk_type="顺子";
						}
					}
					
				}
				//  划分对子  豹子    顺子  同花顺
				for (int i = 0; i < numStr.length; i++) 
				{
					if(numStr[i].length() == 3)
					{
						switch (i) 
						{
						case 0:
							inputBackgroundId(true,tv_num1,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
							break;
						case 1:
							inputBackgroundId(true,tv_num2,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
							break;
						case 2:
							inputBackgroundId(true,tv_num3,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
							break;
						default:
							break;
						}
					}
				}
				tv_text4.setText(pk_type);//  开奖扑克种类的提示
			}
		}
		
	   /**
	    * 
	    * 特殊高频彩种对应自己的title
	    * @param awardNums  开奖号码 数据String  ： 102,103,105
	    * @param string 
	    * @return View  
	    */
		@SuppressLint({ "CutPasteId", "NewApi" })
		private View getPLtitle_view121(String issueStr, String awardNums ) {
			System.out.println("121---issueStr-->"+issueStr+"awardNums-->"+awardNums);
			final Viewholder_title title_VH=new Viewholder_title();
		    View	v=View.inflate(mActivity,R.layout.history_121title,null);  // 特殊玩法的顶部title 显示方式
		    title_VH.expect_tv0=(TextView) v.findViewById(R.id.fast3_expect_tv);
		    title_VH.num_tv1=(TextView) v.findViewById(R.id.num_tv1);
		    title_VH.num_tv2=(TextView) v.findViewById(R.id.num_tv2);
		    title_VH.num_tv3=(TextView) v.findViewById(R.id.num_tv3);
		    title_VH.text_tv=(TextView) v.findViewById(R.id.text_tv);
		    
//		    baseAwait_layout = (LinearLayout) v.findViewById(R.id.baseAwait_layout);//等待布局 指定新id
//		    fast3_expect_Rlyout = (RelativeLayout) v.findViewById(R.id.fast3_expect_Rlyout);
//			fast3_time_tv = (TextView) v.findViewById(R.id.fast3_time_tv);
//			fast3_expect_tv = (TextView)v. findViewById(R.id.fast3_expect_tv);
//			fast3_distance_tv = (TextView)v.findViewById(R.id.fast3_distance_tv);
			
//			fast3_await_cb = (TextView) v.findViewById(R.id.fast3_await_cb);
//			drop_down_d = (CheckBox)v.findViewById(R.id.drop_down_d);
			
		    if(awardNums!=null &&awardNums.contains(",")){
		    String[] numStr=awardNums.split(",");
		    String[] newNumStr=new String[3]; //拆分出的扑克对应text（后两位是text, 前一位数字表示的是 扑克花色)
			String tempStr="";
			
				for (int i = 0; i < numStr.length; i++) 
				{// 拆分出具体扑克  text
					if(numStr[i].length()==3){
						newNumStr[i]=numStr[i].substring(1, 3);
					}
				}
			   //  排序 小---> 大  为了便于 判断是否为 顺子扑克牌
				for (int i = 0; i < newNumStr.length; i++) {
					for (int j = 0; j < newNumStr.length; j++) {
						if(Integer.parseInt(newNumStr[i]) < Integer.parseInt(newNumStr[j])){
							tempStr = newNumStr[i];
							newNumStr[i]=newNumStr[j];
							newNumStr[j]=tempStr;
						}
					}
				}
				if(numStr.length==3){
					
					for (int i = 0; i < numStr.length; i++) 
					{
						if(numStr[i].length() == 3)
						{
							switch (i) 
							{
							case 0:
								inputBackgroundId(false,title_VH.num_tv1,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
								break;
							case 1:
								inputBackgroundId(false,title_VH.num_tv2,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
								break;
							case 2:
								inputBackgroundId(false,title_VH.num_tv3,numStr[i].substring(0, 1),numStr[i].substring(1, 3));
								break;
							default:
								break;
							}
						}
					}
			
				}
		  
			  title_VH.text_tv.setText(getPKType(numStr, newNumStr));//-- 开奖扑克种类的提示
			  
		    }
		 
		    title_VH.expect_tv0.setText(issueStr);//第--期：
//		    FormatUtil.setTypeface(fast3_time_tv, 0);
		    FormatUtil.setTypeface(title_VH.num_tv1, 1);
		    FormatUtil.setTypeface(title_VH.num_tv2, 1);
		    FormatUtil.setTypeface(title_VH.num_tv3, 1);
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//				 try {
//					Typeface fontFace = Typeface.createFromAsset(MyApp.context.getAssets(),"fonts/721-CAI978.ttf");//字体样式库
//					final Typeface fontFace1 = Typeface.createFromAsset(MyApp.context.getAssets(),"fonts/460-cai978.ttf");
//					 fast3_time_tv.setTypeface(fontFace);
//					 title_VH.num_tv1.setTypeface(fontFace1);
//					 title_VH.num_tv2.setTypeface(fontFace1);
//					 title_VH.num_tv3.setTypeface(fontFace1);
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.println("121----------->"+"文件加载失败:721-CAI978.ttf "+ e.getMessage());
//				}
//				}
//			}).start();
//			fast3_title_Layout.setBackground(MyApp.res.getDrawable(R.drawable.background_repeat_blue));
//			fast3_time_tv.setTextColor(MyApp.res.getColor(R.color.yellow0));
//			fast3_time_tv.setTextSize(15);
//			basetop_line.setBackgroundColor(MyApp.res.getColor(R.color.black0));
//			fast3_distance_tv.setBackgroundColor(MyApp.res.getColor(R.color.white));
		return v;
	}
	 class Viewholder_title{
			  TextView expect_tv0;
			  TextView num_tv1;
			  TextView num_tv2;
			  TextView num_tv3;
			  TextView text_tv;
		  }
	  class Viewholder{
		   TextView tv1;//历史期数
		   TextView tv2;//历史号码
		   RelativeLayout Rlayout;
		   View v1,v2,line_end;
	   }
		/**
		 * PK 划分  
		 * @param numStr  开奖号码的数据  例如：102,105,209 
		 * @param newNumStr  拆分后的参数 例如 02,05,09
		 * @return
		 */
  private String getPKType(String[] numStr,String[] newNumStr){
			String pk_type="";//  数据显示 的特殊扑克的提示text
			if(numStr==null || newNumStr==null){
			return  pk_type;	
			}
			if( numStr[0].substring(0, 1).equals(numStr[1].substring(0, 1)) &&
				numStr[1].substring(0, 1).equals(numStr[2].substring(0, 1))){
				//  同花（判断每个号码的第一位数字是否相同 决定是否为同花）
						pk_type="同花";
						
						//	同花顺： 是指在同花的前提下顺子
			    	if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
			    	   Integer.parseInt(newNumStr[1])+1 == Integer.parseInt(newNumStr[2])){// 普通顺子
			    		pk_type="同花顺";
			    	}else if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
			    			1==(Integer.parseInt(newNumStr[1]) - Integer.parseInt(newNumStr[2]))){ // AQK  特殊顺子的判断
			    		pk_type="同花顺";
			    	}
			}else{//非同花
				
				if(newNumStr[0].equals(newNumStr[1]) && newNumStr[1].equals(newNumStr[2])){//  豹子
					pk_type="豹子";
				}else if(newNumStr[0].equals(newNumStr[1]) || newNumStr[1].equals(newNumStr[2])){//对子
					pk_type="对子";
			    }else {
			    	if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
			    	   Integer.parseInt(newNumStr[1])+1 == Integer.parseInt(newNumStr[2])){// 普通顺子
			    		pk_type="顺子";
			    	}else if(Integer.parseInt(newNumStr[0])+1 == Integer.parseInt(newNumStr[1]) && 
			    			1==(Integer.parseInt(newNumStr[1]) - Integer.parseInt(newNumStr[2]))){ // AQK  特殊顺子的判断
			    		pk_type="顺子";
			    	}
			    }
				
		   }
			return pk_type;
		}

	   @Override
		protected void onMessageReceived(Message msg) 
	   {
			super.onMessageReceived(msg);
			mBean = (MessageBean) msg.obj;
			paramsJson=null;
			   switch(msg.arg1) 
			   {
				//请求成功
				case 1204:
					String[] sz_YLKey =null;
					String szStr="010203,020304,030405,040506,050607,060708,070809,080910,091011,101112,111213,011213"; //顺子遗漏数据的 key
					sz_YLKey=szStr.split(",");
				      try {
										e1= mBean.getE1();//同花遗漏值       th_YL 
					                    e2 = mBean.getE2();// 顺子遗漏值    sz_YL
										e3 = mBean.getE3();// 同花顺遗漏值 ths_YL
										e4 = mBean.getE4();//豹子遗漏值     bz_YL
										e5 = mBean.getE5();//对子遗漏值     dz_YL
										e6 = mBean.getE6();//任选遗漏值      rx_Yl
										
										bx_YL.clear();//  整合遗漏值存储 只做一次清空
										if(StringUtils.isBlank(e1)|| StringUtils.isBlank(e2) 
												||StringUtils.isBlank(e3)||StringUtils.isBlank(e4)
												||StringUtils.isBlank(e5)||StringUtils.isBlank(e6))
										{
											booleanTag=false;
											return;
										}else {
											booleanTag=true;
										}
										// 对子遗漏值     dz_YL----------------1
										if(!StringUtils.isBlank(e5))
										{
											ylJson=new JSONObject(e5);
											dz_YL.clear();
											bx_YL.add(0,ylJson.getString("DZBX"));
//											dz_YL.add(0,ylJson.getString("DZBX"));
											for (int i = 1; i <= 13; i++) {
												dz_YL.add(ylJson.getString(FormatUtil.ballNumberFormat(i)));
											}
											System.out.println("ylJson5=="+ylJson.toString());
											System.out.println("dz_YL"+dz_YL.toString());
										}
										// 豹子遗漏值     bz_YL
										if(!StringUtils.isBlank(e4))
										{
											ylJson=new JSONObject(e4);
											bz_YL.clear();
											bx_YL.add(0,ylJson.getString("BZBX"));
//											bz_YL.add(0,ylJson.getString("BZBX"));// 0 位为包选
											for (int i = 1; i <= 13; i++) {
												bz_YL.add(ylJson.getString(FormatUtil.ballNumberFormat(i)));
											}
											System.out.println("ylJson4=="+ylJson.toString());
											System.out.println("bz_YL:"+bz_YL.toString());
										}
										if(!StringUtils.isBlank(e1))
										{
											//同花遗漏值 
//											 ylJson:{"THBX":"39","3":"89","2":"45","1":"39","4":"135"}
											ylJson=new JSONObject(e1);
											th_YL.clear();
											bx_YL.add(0,ylJson.getString("THBX"));
//											th_YL.add(0,ylJson.getString("THBX"));// 0 位为包选
											for (int i = 1; i <= 4; i++) {
												th_YL.add(ylJson.getString(String.valueOf(i)));
											}
											System.out.println("ylJson1=="+ylJson.toString());
											System.out.println("th_YL:"+th_YL.toString());
										}
										if(!StringUtils.isBlank(e2))
										{
											// 顺子遗漏值
//											ylJson:{"111213":"12","040506":"36","030405":"72","070809":"304",
//										    "020304":"285","011213":"49","060708":"304","050607":"39","091011":"304",
//										    "SZBX":"12","010203":"48","080910":"160","101112":"42"}
											 ylJson=new JSONObject(e2);
					                         sz_YL.clear();
					                         bx_YL.add(0,ylJson.getString("SZBX"));
//					                         sz_YL.add(0,ylJson.getString("SZBX"));// 0 位为包选
					                         for (int i = 0; i < sz_YLKey.length; i++) {
					                        	 if(!ylJson.isNull(sz_YLKey[i])){
					                        		 sz_YL.add(ylJson.getString(sz_YLKey[i]));
					                        	 }
											}
					                         
					                         System.out.println("ylJson2=="+ylJson.toString());
					                         System.out.println("sz_YL:"+sz_YL.toString());
										}
										if(!StringUtils.isBlank(e3))
										{
											// 同花顺遗漏值 ths_YL
//											ylJson:{"3":"304","THSBX":"39","2":"304","1":"39","4":"304"}
											 ylJson=new JSONObject(e3);
											 ths_YL.clear();
											 bx_YL.add(0, ylJson.getString("THSBX"));
//											 ths_YL.add(0,ylJson.getString("THSBX"));// 0 位为包选
											 for (int i = 1; i <= 4; i++) {
												 ths_YL.add(ylJson.getString(String.valueOf(i)));
												}
											 System.out.println("ylJson3=="+ylJson.toString());
											 System.out.println("ths_YL"+ths_YL.toString());
										}
										if(!StringUtils.isBlank(e6))
										{
											// 任选遗漏值      rx_Yl
//											ylJson=6={"10":"1","08":"0","09":"0","04":"6","05":"2",
//													"06":"14","07":"7","13":"3","01":"4","11":"2","02":"0","12":"6","03":"3"}
											ylJson=new JSONObject(e6);
											rx_Yl.clear();
											for (int i = 1; i <= 13; i++) {
												rx_Yl.add(ylJson.getString(FormatUtil.ballNumberFormat(i)));
											}
											System.out.println("ylJson=6="+ylJson.toString());
											System.out.println("rx_Yl"+rx_Yl.toString());
											
										}
										if(booleanTag)
										{
											itemChangeaddOmit();//实现刷新遗漏的效果	
											booleanTag=false;
										}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					

					
				break;

			default:
				break;
			}
			
		}
	   
   /**
    *适配各个子玩法的 遗漏 数据
    *
    */
	private void itemChangeaddOmit() {
		
		switch (Integer.parseInt(childId)) {
		//		    包选
			case 0:
				initTextAndView(5);
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one); //添加了遗漏
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		//			猜对子
			case 9:
				initTextAndView(2);
				
				ctmPK=new CustomPK(mActivity);
				ctmPK.setOneTextStr("对子包选");
				ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
				ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
				ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
				if(5 == bx_YL.size()&& bx_YL.get(4) != null){
					ctmPK.setPK_yilouText(bx_YL.get(4));
				}
				customItems_two.add(ctmPK);
				sampleNums_two.clear();
				sampleNums_two.add(11); //对子包选 09  投注号码 11 
				
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
				containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				containers[1].notifyDataSetChanged();// 刷新遗漏
				break;
		//		       猜豹子
			case 7:
				initTextAndView(3);
				
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
				ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
				ctmPK.setOneTextStr("豹子包选");
				ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
				if(5 == bx_YL.size()&& bx_YL.get(3) != null){
					ctmPK.setPK_yilouText(bx_YL.get(3));
				}
				customItems_two.add(ctmPK);
				sampleNums_two.clear();
				sampleNums_two.add(10); //豹子包选 07  投注号码 10
				
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
				containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				containers[1].notifyDataSetChanged();// 刷新遗漏
				break;
		//			猜同花
			case 1:
				customItems_two.clear();
				customItems_one.clear();
				sampleNums_one.clear();
				for (int i = 1; i <= 4; i++) {
					sampleNums_one.add(i);
					switch (i) {
					case 1:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.pk_ht);// 黑桃
						if(4==th_YL.size() && th_YL.get(0)!=null){
							ctmPK.setPK_yilouText(th_YL.get(0));
						}
						customItems_one.add(ctmPK);
						break;
					case 2:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.pk_h);// 红桃
						if(4==th_YL.size() && th_YL.get(1)!=null){
							ctmPK.setPK_yilouText(th_YL.get(1));
						}
						customItems_one.add(ctmPK);
						break;
					case 3:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.pk_mh);// 梅花
						if(4==th_YL.size() && th_YL.get(2)!=null){
							ctmPK.setPK_yilouText(th_YL.get(2));
						}
						customItems_one.add(ctmPK);
						break;
					case 4:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.pk_f);// 方块
						if(4==th_YL.size() && th_YL.get(3)!=null){
							ctmPK.setPK_yilouText(th_YL.get(3));
						}
						customItems_one.add(ctmPK);
						
						break;

					default:
						break;
					}
				}
				
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
				ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
				ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
				ctmPK.setOneTextStr("同花包选");
				if(5 == bx_YL.size() && bx_YL.get(2) != null){
					ctmPK.setPK_yilouText(bx_YL.get(2));
				}
				customItems_two.add(ctmPK);
				sampleNums_two.clear();
				sampleNums_two.add(7); //同花包选 01  投注号码 07 
				
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
				containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				containers[1].notifyDataSetChanged();// 刷新遗漏
				break;
		//			猜顺子
			case  5:
				customItems_two.clear();
				initTextAndView(4);

				
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
				ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
				ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
				ctmPK.setOneTextStr("顺子包选");
				if(5 == bx_YL.size() && bx_YL.get(1) != null){
					ctmPK.setPK_yilouText(bx_YL.get(1));
				}
				customItems_two.add(ctmPK);
				sampleNums_two.clear();
				sampleNums_two.add(9); //同花包选 05  投注号码 09 
				
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
				containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				containers[1].notifyDataSetChanged();// 刷新遗漏
				break;
		//			猜同花顺
			case 3:
				customItems_two.clear();
				customItems_one.clear();
				sampleNums_one.clear();
				for (int i = 1; i <= 4; i++) {
					sampleNums_one.add(i);
					switch (i) {
					case 1:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.sz_ht);// 黑桃
						if(4 == ths_YL.size()  && ths_YL.get(0)!=null){
							ctmPK.setPK_yilouText(ths_YL.get(0));
						}
						
						customItems_one.add(ctmPK);
						break;
					case 2:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.sz_h);// 红桃
						if(4 == ths_YL.size()  && ths_YL.get(1)!=null){
							ctmPK.setPK_yilouText(ths_YL.get(1));
						
						}
						customItems_one.add(ctmPK);
						break;
					case 3:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.sz_mh);// 梅花
						if(4 == ths_YL.size()  && ths_YL.get(2)!=null){
							ctmPK.setPK_yilouText(ths_YL.get(2));
						}
						customItems_one.add(ctmPK);
						break;
					case 4:
						ctmPK=new CustomPK(mActivity);
						ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
						ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
						ctmPK.setBackG(1, R.drawable.sz_f);// 方块
						if(4 == ths_YL.size()  && ths_YL.get(3)!=null){
							ctmPK.setPK_yilouText(ths_YL.get(3));
						}
						customItems_one.add(ctmPK);
						
						break;

					default:
						break;
					}
				}
				
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 150),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
				ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 152),DensityUtil.dip2px(mActivity, 68));//242,102);
				ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
				ctmPK.setOneTextStr("同花顺包选");
				if(5 == bx_YL.size() && bx_YL.get(0) != null){
					ctmPK.setPK_yilouText(bx_YL.get(0));
				}
				customItems_two.add(ctmPK);
				sampleNums_two.clear();
				sampleNums_two.add(8); //同花包选 03  投注号码 08
				
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
				containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				containers[1].notifyDataSetChanged();// 刷新遗漏
				break;
				
		//			任选一  ------------------任选（布局相同，提示信息不同）-------------------
			case 11:
				initTextAndView(1);
				containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		//			任选二
			case 12:
				initTextAndView(1);
				containers[0].simpleInit(false, sampleNums_one, 2, customItems_one);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		//			任选三
			case 13:
				initTextAndView(1);
				containers[0].simpleInit(false, sampleNums_one, 3, customItems_one);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		//			任选四
			case 14:
				initTextAndView(1);
				containers[0].simpleInit(false, sampleNums_one, 4, customItems_one);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		//			任选五
			case 15:
				initTextAndView(1);
				containers[0].simpleInit(false, sampleNums_one, 5, customItems_one);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		//			任选六
			case 16:
				initTextAndView(1);
				containers[0].simpleInit(false, sampleNums_one, 6, customItems_one);
				containers[0].notifyDataSetChanged();// 刷新遗漏
				break;
		
			default:
				break;
		}
			
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
	@SuppressLint("NewApi")
	protected View customContent_saleType() {
			titleView=View.inflate(mActivity, R.layout.f_klpk_title, null);
			pk_rbtn1=(RadioButton) titleView.findViewById(R.id.pk_rbtn1);
			pk_rbtn2=(RadioButton) titleView.findViewById(R.id.pk_rbtn2);
			pk_rbtn3=(RadioButton) titleView.findViewById(R.id.pk_rbtn3);
			
			pk_rbtn4=(RadioButton) titleView.findViewById(R.id.pk_rbtn4);
			pk_rbtn5=(RadioButton) titleView.findViewById(R.id.pk_rbtn5);
			pk_rbtn6=(RadioButton) titleView.findViewById(R.id.pk_rbtn6);
			
			pk_rbtn7=(RadioButton) titleView.findViewById(R.id.pk_rbtn7);
			pk_rbtn8=(RadioButton) titleView.findViewById(R.id.pk_rbtn8);
			pk_rbtn9=(RadioButton) titleView.findViewById(R.id.pk_rbtn9);
			
			pk_rbtn10=(RadioButton) titleView.findViewById(R.id.pk_rbtn10);
			pk_rbtn11=(RadioButton) titleView.findViewById(R.id.pk_rbtn11);
			pk_rbtn12=(RadioButton) titleView.findViewById(R.id.pk_rbtn12);

			pk_rbtn1.setText(jointRbtnText(getString(R.string.t_text_bx),
					0, 3,
					0, 3,
					getString(R.string.t_text_bx).length()-4, getString(R.string.t_text_bx).length()-1));//设置转换后的字体----TODO
			
			pk_rbtn2.setText(jointRbtnText(getString(R.string.t_text_cdz),
					0, 3,
					0, 3,
					getString(R.string.t_text_cdz).length()-3, getString(R.string.t_text_cdz).length()-1));
			
			pk_rbtn3.setText(jointRbtnText(getString(R.string.t_text_cbz),
					0, 3,
					0, 3,
					getString(R.string.t_text_cbz).length()-5, getString(R.string.t_text_cbz).length()-1));
			
			pk_rbtn4.setText(jointRbtnText(getString(R.string.t_text_cth),
					0, 3,
					0, 3,
					getString(R.string.t_text_cth).length()-3, getString(R.string.t_text_cth).length()-1));
			
			pk_rbtn5.setText(jointRbtnText(getString(R.string.t_text_csz),
					0, 3,
					0, 3,
					getString(R.string.t_text_csz).length()-4, getString(R.string.t_text_csz).length()-1));
			
			pk_rbtn6.setText(jointRbtnText(getString(R.string.t_text_cths),
					0, 4,
					0, 4,
					getString(R.string.t_text_cths).length()-5, getString(R.string.t_text_cths).length()-1));
			
			pk_rbtn7.setText(jointRbtnText(getString(R.string.t_text_pkry),
					0, 3,
					0, 3,
					getString(R.string.t_text_pkry).length()-2, getString(R.string.t_text_pkry).length()-1));
			
			pk_rbtn8.setText(jointRbtnText(getString(R.string.t_text_pkre),
					0, 3,
					0, 3,
					getString(R.string.t_text_pkre).length()-3, getString(R.string.t_text_pkre).length()-1));
			
			pk_rbtn9.setText(jointRbtnText(getString(R.string.t_text_pkrs),
					0, 3,
					0, 3,
					getString(R.string.t_text_pkrs).length()-4, getString(R.string.t_text_pkrs).length()-1));
			
			pk_rbtn10.setText(jointRbtnText(getString(R.string.t_text_pkrs_1),
					0, 3,
					0, 3,
					getString(R.string.t_text_pkrs_1).length()-3, getString(R.string.t_text_pkrs_1).length()-1));
			
			pk_rbtn11.setText(jointRbtnText(getString(R.string.t_text_pkrw),
					0, 3,
					0, 3,
					getString(R.string.t_text_pkrw).length()-3, getString(R.string.t_text_pkrw).length()-1));
			
			pk_rbtn12.setText(jointRbtnText(getString(R.string.t_text_pkrl),
					0, 3,
					0, 3,
					getString(R.string.t_text_pkrl).length()-3, getString(R.string.t_text_pkrl).length()-1));
//			fast3_title_Layout.setBackgroundResource(MyApp.res.getDrawable(R.drawable.pk_titlebg));
			fast3_title_Layout.setLayoutParams(new LayoutParams(-1,DensityUtil.dip2px(mActivity, 60)));//两张pk);
			fast3_title_Layout.setBackgroundResource(R.color.blue3);
			baseTitle_reightLayout.setBackgroundResource(R.drawable.pktitle_reightbg);
			FormatUtil.setTypeface(fast3_time_tv, 2); //时间设置子字体
			fast3_time_tv.setTextColor(MyApp.res.getColor(R.color.yellow0));
			fast3_time_tv.setTextSize(25);
			basetop_line.setBackgroundColor(MyApp.res.getColor(R.color.black0));
			fast3_distance_tv.setTextColor(MyApp.res.getColor(R.color.white));
			
		return titleView;
	}
	/**
	 *  -----------2014-5-16-- 抽取工具-----------------------------------
	 * * 为了不同长度的 String  中包含 大字体24dp  和 小字体 10dp  以及金额的不同色替换
	 *   代码里的参数需要转换
	 *   
	 * @param str
	 * @param littleStart 小字体 开始位置
	 * @param littleEnd    小字体结束位置
	 * @param StyleStart  加粗开始位置
	 * @param StyleEnd    加粗结束位置
	 * @param ColorStart   颜色开始位置
	 * @param ColorEnd     颜色结束位置
	 * @return Spannable 
	 */
	private   Spannable jointRbtnText(String str,int littleStart,int littleEnd , int StyleStart ,int StyleEnd,int ColorStart,int ColorEnd){
//		textSp=(Spannable) Html.fromHtml(str);
//		SpannableString用法说明 
//		其中参数what是要设置的Style span，start和end则是标识String中Span的起始位置，而 flags是用于控制行为的，通常设置为0或Spanned中定义的常量，常用的有：
//		•Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含两端start和end所在的端点
//		•Spanned.SPAN_EXCLUSIVE_INCLUSIVE --- 不包含端start，但包含end所在的端点
//		•Spanned.SPAN_INCLUSIVE_EXCLUSIVE --- 包含两端start，但不包含end所在的端点
//		•Spanned.SPAN_INCLUSIVE_INCLUSIVE --- 包含两端start和end所在的端点
	        Spannable textSp= new SpannableString(str); 
			//		设置字体大小  （玩法字体16 最高奖金14）
			//		textSp.setSpan(new AbsoluteSizeSpan(16), 1, str.length()-5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
			textSp.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mActivity, 20)), littleStart,littleEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE); //str.length()-8, str.length()
			//		设置字体样式  （加粗）
			textSp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), StyleStart, StyleEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE); //0, str.length()-8 
			//		设置字体 颜色（体前景色ForegroundColorSpan   背景色 BackgroundColorSpan）R.color.klpk_bonustext_color
			textSp.setSpan(new ForegroundColorSpan(MyApp.res.getColor(R.color.klpk_bonustext_color)), ColorStart,ColorEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);//str.length()-4, str.length()-1  
			                                         
		return textSp;
	}
	
	/**
	 * 重写
	 * 
	 * 加载play  布局  设置pop_saleType的contentview
	 * 
	 * lotteryView 可以用来改变彩种的布局
	 * 
	 */
	@Override
	protected View customLotteryView() 
	{
		initMainView=View.inflate(mActivity, R.layout.pk_main, null);
		initcontentLayout=(LinearLayout) initMainView.findViewById(R.id.fast3_pl_contentLayout);
//		此处 是不影响 界面的  可以把它理解为界面预加载  但是实际在回调onSaleTypeChanged 方法的时候 界面会指定玩法所替代
//		initAddViewOrUpdate(R.layout.f_klpk_rx);
//		
//		pk3_tv_one.setText(getString(R.string.f_pk_dxtext1));
//		pk3_tv_two.setText(getString(R.string.f_pk_bxtext1));
//		
//		initTextAndView(2);
//		
//		ctmPK=new CustomPK(mActivity);
//		ctmPK.setOneTextStr("对子包选");
//		ctmPkWidgetParam=new LayoutParams(41, 53);//单张pk
//		ctmPkWidgetParam2=new LayoutParams(195, 58);//单张pk
//		ctmPK.setShowPKNum(1,ctmPkWidgetParam,ctmPkWidgetParam2);
//		
//		customItems_two.add(ctmPK);
//		
//		sampleNums_two.add(0); 
//		
//		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
//		containers[1].simpleInit(false, sampleNums_two, 1, customItems_two);
	
		
	    
//		setSecondLayerBackground(R.drawable.bg_horizontal_repeat_blue);//模拟投注深蓝色背景
//	    fast3_title_Layout.setBackgroundResource(R.drawable.bg_horizontal_repeat_blue);//顶部浅蓝色时间提示
//		setSecondLayerBackground(R.drawable.pkmain_bg);//模拟投注深蓝色背景
		setSecondLayerBackground(R.color.blue6);//模拟投注深蓝色背景
		setBottomBackgroundResource(MyApp.res.getDrawable(R.drawable.clear_121), MyApp.res.getDrawable(R.drawable.next_121),R.color.title_nav_tv_shadow_color,R.color.white);//黑色低栏
//	    fast3_await_cb.setTextColor(getResources().getColor(R.color.white));
//		fast3_expect_tv.setTextColor(getResources().getColor(R.color.white));
//		fast3_distance_tv.setTextColor(getResources().getColor(R.color.white));
//		fast3_time_tv.setTextColor(getResources().getColor(R.color.white));
		return initMainView;
	}
	
	public  class Viewholder_item{
		  public TextView num_tv1;
		  public TextView num_tv2;
		  public TextView num_tv3;
		  public TextView text_tv;
	  }
    // 设置扑克牌的背景（具体扑克  花色）
	/**
	 * 
	 * @param tag 背景大小图true 小图  false 大图
	 * @param tv
	 * @param id
	 * @param textid
	 */
	private static  void inputBackgroundId(boolean tag,TextView tv,String id,String textid) {
//			 背景
			switch (Integer.parseInt(id)) {
				case 1://1黑桃
					if(tag){
						tv.setBackgroundResource(R.drawable.pk_1);
					}else{
						tv.setBackgroundResource(R.drawable.pk_title_ht);
					}
					break;
				case 2://2红桃
					if(tag){
						
						tv.setBackgroundResource(R.drawable.pk_2);
					}else{
						tv.setBackgroundResource(R.drawable.pk_title_rt);
					}
					break;
				case 3://3梅花
					if(tag){
						tv.setBackgroundResource(R.drawable.pk_3);
					}else{
						tv.setBackgroundResource(R.drawable.pk_title_mh);
					}
					break;
				case 4://4方块。
					if(tag){
						tv.setBackgroundResource(R.drawable.pk_4);
					}else{
						tv.setBackgroundResource(R.drawable.pk_title_fk);
					}
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
//			tv.setPadding(40, 0, 0, 0);// 设置字符位置

		}
	/**
	 * 加载布局
	 */
	private void initAddViewOrUpdate(int resource) {
		initcontentLayout.removeAllViews();
		initcontentLayout.addView(LinearLayout.inflate(mActivity, resource, null));//布局默认是 猜对子 
		//	提示
		pk3_tv_one=(TextView) initcontentLayout.findViewById(R.id.pk3_tv_one);
		pk3_tv_two=(TextView) initcontentLayout.findViewById(R.id.pk3_tv_two);
       //   箭头
		pk3_tv_oneMark=(TextView) initcontentLayout.findViewById(R.id.pk3_tv_oneMark);
		pk3_tv_twoMark=(TextView) initcontentLayout.findViewById(R.id.pk3_tv_twoMark);
	   //   选号容器
		containers[0]=(MGridView) initcontentLayout.findViewById(R.id.pk3_mgv_one);
		containers[1]=(MGridView) initcontentLayout.findViewById(R.id.pk3_mgv_two);
		
		initActionUpListener();
	}
	/**
	 * 调整布局  内控件的隐藏
	 */
	private void setViewInvisible(){
		pk3_tv_oneMark.setVisibility(View.GONE);//布局需要有小调整
		pk3_tv_two.setVisibility(View.INVISIBLE);
		pk3_tv_twoMark.setVisibility(View.INVISIBLE);
		containers[1].setVisibility(View.INVISIBLE);
		containers[0].setVisibility(View.VISIBLE);
	}
	/**
	 * 调整布局  内控件的显示
	 */
	private void setViewVisible(){
		if(pk3_tv_oneMark!=null)
		pk3_tv_oneMark.setVisibility(View.VISIBLE);//布局需要有小调整
		if(pk3_tv_two!=null)
		pk3_tv_two.setVisibility(View.VISIBLE);
		if(pk3_tv_twoMark!=null)
		pk3_tv_twoMark.setVisibility(View.VISIBLE);
		if(containers[1] != null )
		containers[1].setVisibility(View.VISIBLE);
	}
	
	/**
	 * 重写
	 * 
	 * 根据salesType的变化
	 *  改变选号区的布局
	 * 
	 */
	
	@Override
	protected void onSaleTypeChanged(String tag) {
		basebuy_tv_notice.setText("");//清空底部提示
		tag=tag.replace(",", "");
		replaceTitleBackgroundResource(R.color.blue8,R.drawable.selector_title_bluebg);//顶部Title条	
	    switch (Integer.parseInt(tag)) {
		//		    包选
			case 0:
				changeContentView_0();
				
				break;
		//			猜对子（包选  :代表layout）
			case 9:
				changeContentView_9();
				
				break;
		//		       猜豹子（包选  :代表layout）
			case 7:
				changeContentView_7();
				
				break;
		//			猜同花（包选  :代表layout）
			case 1:
				changeContentView_1();
				
				break;
		//			猜顺子（包选  :代表layout）
			case  5:
				changeContentView_5();
				
				break;
		//			猜同花顺（包选  :代表layout）
			case 3:
				changeContentView_3();
				
				break;
				
		//			任选一  ------------------任选（布局相同，提示信息不同）-------------------
			case 11:
				changeContentView_11();
				
				break;
		//			任选二
			case 12:
				changeContentView_12();
				
				break;
		//			任选三
			case 13:
				changeContentView_13();
				
				break;
		//			任选四
			case 14:
				changeContentView_14();
				
				break;
		//			任选五
			case 15:
				changeContentView_15();
				
				break;
		//			任选六
			case 16:
				changeContentView_16();
				
				break;
		
			default:
				break;
			}
	    if(0==rx_Yl.size()||0==th_YL.size()
	    		||0==sz_YL.size()|| 0==ths_YL.size()
	    		||0==bz_YL.size()||0==dz_YL.size()
	    		||0==bx_YL.size()){
			booleanTag=true;
			sendgetYilouRequest();//请求遗漏
		}
	}
	
	/**
	 * 任选六
	 */
	private void changeContentView_16() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_5));
		setShakeNotice(getShakeNoticeString("6"));//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_rx);
		initTextAndView(1);
		setShakeNotice("至少选6张牌");//摇一摇提示
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_bh_text, "12元"));
		setViewInvisible();
		containers[0].simpleInit(false, sampleNums_one, 6, customItems_one);
		containers[0].setMaxNum(0);
		reload(true, 0);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
			}
          });
		
	}
	/**
	 * 任选五
	 */
	
	private void changeContentView_15() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_4));
		setShakeNotice(getShakeNoticeString("5"));//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_rx);
		initTextAndView(1);
		setShakeNotice("至少选5张牌");//摇一摇提示
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_bh_text, "22元"));
		setViewInvisible();
		containers[0].simpleInit(false, sampleNums_one, 5, customItems_one);
		containers[0].setMaxNum(0);
		reload(true, 0);
		
		containers[0].setActionUpListener(new ActionUpListener() {
					
					@Override
					public void onActionUp() {
		                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
						
						shakeListener.vibrate();//震动100毫秒
						changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
					}
		});
	
	}
	/**
	 * 任选四
	 */
	private void changeContentView_14() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_3));
		setShakeNotice(getShakeNoticeString("4"));//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_rx);
		initTextAndView(1);
		setShakeNotice("至少选4张牌");//摇一摇提示
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_bh_text, "46元"));
		setViewInvisible();
		containers[0].simpleInit(false, sampleNums_one, 4, customItems_one);
		containers[0].setMaxNum(0);
		reload(true, 0);
		
        containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
			}
		});
	}
	/**
	 * 任选三
	 */
	private void changeContentView_13() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_2));
		setShakeNotice(getShakeNoticeString("3"));//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_rx);
		initTextAndView(1);
		setShakeNotice("至少选3张牌");//摇一摇提示
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_bh_text, "116元"));
		setViewInvisible();
		containers[0].simpleInit(false, sampleNums_one, 3, customItems_one);
		containers[0].setMaxNum(0);
		reload(true, 0);
		
		containers[0].setActionUpListener(new ActionUpListener() {
					
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
			}
		});
	}
	/**
	 * 任选二
	 */
	
	private void changeContentView_12() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title1_3));
		setShakeNotice(getShakeNoticeString("2"));//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_rx);
		initTextAndView(1);
		setShakeNotice("至少选2张牌");//摇一摇提示
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_rx2_text,"33元"));
		setViewInvisible();
		containers[0].simpleInit(false, sampleNums_one, 2, customItems_one);
		reload(true, 0);
		
		containers[0].setActionUpListener(new ActionUpListener() {
					
					@Override
					public void onActionUp() {
		                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
						
						shakeListener.vibrate();//震动100毫秒
						changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
					}
				});
	}
	/**
	 * 任选一 
	 */
	private void changeContentView_11() {
		 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title1_2));
		setShakeNotice(getShakeNoticeString("1"));//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_rx);
		initTextAndView(1);
		setShakeNotice("至少选一张牌");//摇一摇提示
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_rx1_text, "1",R.string.f_pk_rx3_text,"5元"));
		setViewInvisible();
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		
		reload(true, 0);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
			}
		});
	}
	/**
	 * 摇一摇右边提示 拼接String
	 * 
	 * “至少选  + StrText  +  张牌”
	 * @param StrText
	 * @return
	 */
	private Spannable getShakeNoticeSpad(int resId,String StrText ) {
		return (Spannable) Html.fromHtml(getString(resId)+"<font color=#dbba20>"+StrText+"</font>");
	}
	private Spannable getShakeNoticeSpad(int resId,String Str,int resId1,String StrText ) {
		return (Spannable) Html.fromHtml(getString(resId)+Str+getString(resId1)+"<font color=#dbba20>"+StrText+"</font>");
	}
	private String getShakeNoticeString(String StrText ) {
		return getString(R.string.pk3_shakenotice_text)+StrText+getString(R.string.pk3_shakenotice_text1);
	}

	/**
	 * 猜同花顺
	 */
	private void changeContentView_3() {
		 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.pk3_title_cths));
		setShakeNotice("至少选一张牌");//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_cth);
		setViewVisible();
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_dxtext2,"2150元"));
		pk3_tv_two.setText(getShakeNoticeSpad(R.string.f_pk_bxtext3,"535元"));
		
		customItems_two.clear();
		customItems_one.clear();
		sampleNums_one.clear();
		for (int i = 1; i <= 4; i++) {
			sampleNums_one.add(i);
			switch (i) {
			case 1:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.sz_ht);// 黑桃
				if(4 == ths_YL.size()  && ths_YL.get(0)!=null){
					ctmPK.setPK_yilouText(ths_YL.get(0));
				}
				
				customItems_one.add(ctmPK);
				break;
			case 2:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.sz_h);// 红桃
				if(4 == ths_YL.size()  && ths_YL.get(1)!=null){
					ctmPK.setPK_yilouText(ths_YL.get(1));
				
				}
				customItems_one.add(ctmPK);
				break;
			case 3:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.sz_mh);// 梅花
				if(4 == ths_YL.size()  && ths_YL.get(2)!=null){
					ctmPK.setPK_yilouText(ths_YL.get(2));
				}
				customItems_one.add(ctmPK);
				break;
			case 4:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.sz_f);// 方块
				if(4 == ths_YL.size()  && ths_YL.get(3)!=null){
					ctmPK.setPK_yilouText(ths_YL.get(3));
				}
				customItems_one.add(ctmPK);
				
				break;
			}
		}
		
		ctmPK=new CustomPK(mActivity);
		ctmPkWidgetParam=new LayoutParams(290, 92);//三张pk
		ctmPkWidgetParam2=new LayoutParams(292, 102);
		ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 150),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
		ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 152),DensityUtil.dip2px(mActivity, 68));//242,102);
		ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
		ctmPK.setOneTextStr("同花顺包选");
		if(5 == bx_YL.size() && bx_YL.get(0) != null){
			ctmPK.setPK_yilouText(bx_YL.get(0));
		}
		ctmPK.getYiLouTextVew().setGravity(Gravity.CENTER|Gravity.LEFT);
		ctmPK.getYiLouTextVew().setPadding(DensityUtil.dip2px(mActivity, 45), 0, 0, 0);
		customItems_two.add(ctmPK);
		sampleNums_two.clear();
		sampleNums_two.add(8); //同花包选 03  投注号码 08
		
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
		reload(true, 0,1);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
		
	}
	/**
	 * 猜顺子
	 */
	
	private void changeContentView_5() {
		 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.pk3_title_csz));
		setShakeNotice("至少选一张牌");//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_csz);
		setViewVisible();
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_dxtext3,"400元"));
		pk3_tv_two.setText(getShakeNoticeSpad(R.string.f_pk_bxtext4,"33元"));
		
		customItems_two.clear();
		initTextAndView(4);

		
		ctmPK=new CustomPK(mActivity);
//		ctmPkWidgetParam=new LayoutParams(245, 92);//三张pk
//		ctmPkWidgetParam2=new LayoutParams(245, 102);
		ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
		ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
		ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
		ctmPK.setOneTextStr("顺子包选");
		if(5 == bx_YL.size() &&  bx_YL.get(1) != null){
			ctmPK.setPK_yilouText(bx_YL.get(1));
		}
		ctmPK.getYiLouTextVew().setGravity(Gravity.CENTER|Gravity.LEFT);
		ctmPK.getYiLouTextVew().setPadding(DensityUtil.dip2px(mActivity, 45), 0, 0, 0);
		customItems_two.add(ctmPK);
		sampleNums_two.clear();
		sampleNums_two.add(9); //同花包选 05  投注号码 09 
		
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
		reload(true, 0,1);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
	}
	/**
	 * 猜同花
	 */
	private void changeContentView_1() {
		 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.pk3_title_cth));
		setShakeNotice("至少选一张牌");//摇一摇提示
		
		initAddViewOrUpdate(R.layout.f_klpk_cth);
		
		setViewVisible();
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_dxtext4,"90元"));
		pk3_tv_two.setText(getShakeNoticeSpad(R.string.f_pk_bxtext5,"22元"));
		
		customItems_two.clear();
		customItems_one.clear();
		sampleNums_one.clear();
		for (int i = 1; i <= 4; i++) {
			sampleNums_one.add(i);
			switch (i) {
			case 1:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.pk_ht);// 黑桃
				if(4==th_YL.size() && th_YL.get(0)!=null){
					ctmPK.setPK_yilouText(th_YL.get(0));
				}
				customItems_one.add(ctmPK);
				break;
			case 2:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.pk_h);// 红桃
				if(4==th_YL.size() && th_YL.get(1)!=null){
					ctmPK.setPK_yilouText(th_YL.get(1));
				}
				customItems_one.add(ctmPK);
				break;
			case 3:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.pk_mh);// 梅花
				if(4==th_YL.size() && th_YL.get(2)!=null){
					ctmPK.setPK_yilouText(th_YL.get(2));
				}
				customItems_one.add(ctmPK);
				break;
			case 4:
				ctmPK=new CustomPK(mActivity);
				ctmPkWidgetParam=new LayoutParams(85, 129);//单张pk
				ctmPK.setShowPKNum(1,ctmPkWidgetParam,null);
				ctmPK.setBackG(1, R.drawable.pk_f);// 方块
				if(4==th_YL.size() && th_YL.get(3)!=null){
					ctmPK.setPK_yilouText(th_YL.get(3));
				}
				customItems_one.add(ctmPK);
				
				break;

			default:
				break;
			}
		}
		
		ctmPK=new CustomPK(mActivity);
//		ctmPkWidgetParam=new LayoutParams(240, 92);//三张pk
//		ctmPkWidgetParam2=new LayoutParams(240, 102);
		ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
		ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
		ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
		ctmPK.setOneTextStr("同花包选");
		if(5 == bx_YL.size() &&  bx_YL.get(2) != null){
			ctmPK.setPK_yilouText(bx_YL.get(2));
		}
		ctmPK.getYiLouTextVew().setGravity(Gravity.CENTER|Gravity.LEFT);
		ctmPK.getYiLouTextVew().setPadding(DensityUtil.dip2px(mActivity, 45), 0, 0, 0);
		customItems_two.add(ctmPK);
		sampleNums_two.clear();
		sampleNums_two.add(7); //同花包选 01  投注号码 07 
		
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
		reload(true, 0,1);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
	}
	/**
	 *  猜豹子
	 */
	private void changeContentView_7() {
		 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.pk3_title_cbz));
		setShakeNotice("至少选一张牌");//摇一摇提示
		
		initAddViewOrUpdate(R.layout.f_klpk_cdz);
		setViewVisible();
		
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_bztext,"6400元"));
		pk3_tv_two.setText(getShakeNoticeSpad(R.string.f_pk_bxtext2,"500元"));
		
		initTextAndView(3);
		
		ctmPK=new CustomPK(mActivity);
//		ctmPkWidgetParam=new LayoutParams(240, 92);//三张pk
//		ctmPkWidgetParam2=new LayoutParams(242, 102);
		ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
		ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
		ctmPK.setOneTextStr("豹子包选");
		ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
		if(5 == bx_YL.size() &&  bx_YL.get(3) != null){
			ctmPK.setPK_yilouText(bx_YL.get(3));
		}
		ctmPK.getYiLouTextVew().setGravity(Gravity.CENTER|Gravity.LEFT);
		ctmPK.getYiLouTextVew().setPadding(DensityUtil.dip2px(mActivity, 45), 0, 0, 0);
		customItems_two.add(ctmPK);
		sampleNums_two.clear();
		sampleNums_two.add(10); //豹子包选 07  投注号码 10
		
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
		// --TODO
		reload(true, 0,1);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
				
			}
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
	}
	/**
	 * 	猜对子
	 */
   private void changeContentView_9() {
		 //显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.pk3_title_cdz));
		setShakeNotice("至少选一张牌");//摇一摇提示
		initAddViewOrUpdate(R.layout.f_klpk_cdz);
		
		setViewVisible();
		pk3_tv_one.setText(getShakeNoticeSpad(R.string.f_pk_dxtext1,"88元"));
		pk3_tv_two.setText(getShakeNoticeSpad(R.string.f_pk_bxtext1,"7元"));
		initTextAndView(2);
		
		ctmPK=new CustomPK(mActivity);
		ctmPK.setOneTextStr("对子包选");
//		ctmPkWidgetParam=new RelativeLayout.LayoutParams(140, 64);//三张pk
//		ctmPkWidgetParam2=new RelativeLayout.LayoutParams(142,68);
		ctmPkWidgetParam=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 140),DensityUtil.dip2px(mActivity, 62));//240, 92);//三张pk
		ctmPkWidgetParam2=new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 142),DensityUtil.dip2px(mActivity, 68));//242,102);
		ctmPK.setShowPKNum(6,ctmPkWidgetParam,ctmPkWidgetParam2);
		if(5 == bx_YL.size() && bx_YL.get(4) != null){
			ctmPK.setPK_yilouText(bx_YL.get(4));
		}
		ctmPK.getYiLouTextVew().setGravity(Gravity.CENTER|Gravity.LEFT);
		ctmPK.getYiLouTextVew().setPadding(DensityUtil.dip2px(mActivity, 45), 0, 0, 0);
		customItems_two.add(ctmPK);
		sampleNums_two.clear();
		sampleNums_two.add(11); //对子包选 09  投注号码 11 
		
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		containers[1].simpleInit(false, sampleNums_two, 0, customItems_two);
		reload(true, 0,1);
		
		containers[0].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
		containers[1].setActionUpListener(new ActionUpListener() {
			
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size()+ticket.getLotteryNums().get(1).size());
			}
		});
		
		
	}
	/**
	 * 准备  Text  和MGridView的子View
	 */
	private void initTextAndView(int n) {
		sampleNums_one.clear();
		customItems_one.clear();
		customItems_two.clear();
		switch (n) {
		case 1:
			  initPK_1(); //单张 扑克
			break;
		case 2:
			  initPK_2(); //对子 扑克
			break;
		case 3:
			  initPK_3();//豹子扑克
			break;
		case 4:
			  initPK_4();//顺子  扑克
			break;
		case 5:
			  initPK_5();//包选  扑克
			break;

		default:
			break;
		}

	}
	
	
	/*
		 同花包选(1)单式投注(0)
		同花顺包选（3）	单式投注(0)
		顺子包选（5）	单式投注(0)
		豹子包选（7）	单式投注(0)
		对子包选（9）	单式投注(0)*/
	/**
	 * 包选 Pk
	 * 
	 */
	private void initPK_5() {
		for (int i = 0; i <= 4 ; i++) {
			ctmPK=new CustomPK(mActivity);
			switch (i) {
					case 0://对子包选（9）	单式投注(0)
						sampleNums_one.add(11);
//						ctmPK.setBx_itemMsg("对子", (Spannable)Html.fromHtml("奖金<font color=#dbba20>7</font>元"), R.drawable.pk_dz);
						ctmPK.setBx_itemMsg("对子", "7", R.drawable.pk_dz);
						if(5 == bx_YL.size() && bx_YL.get(4)!=null)
						ctmPK.setPK_yilouText(bx_YL.get(4));
				
						break;
					case 1://豹子包选（7）	单式投注(0)
						sampleNums_one.add(10);
//						ctmPK.setBx_itemMsg("豹子", (Spannable)Html.fromHtml("奖金<font color=#dbba20>500</font>元"), R.drawable.pk_bz);
						ctmPK.setBx_itemMsg("豹子", "500", R.drawable.pk_bz);
						if(5 == bx_YL.size() && bx_YL.get(3)!=null)
							ctmPK.setPK_yilouText(bx_YL.get(3));
						break;
					case 2://同花包选(1)     单式投注(0)
						sampleNums_one.add(7);
//						ctmPK.setBx_itemMsg("同花", (Spannable)Html.fromHtml("奖金<font color=#dbba20>22</font>元"), R.drawable.pk_th);
						ctmPK.setBx_itemMsg("同花", "22", R.drawable.pk_th);
						if(5 == bx_YL.size()&& bx_YL.get(2)!=null )
							ctmPK.setPK_yilouText(bx_YL.get(2));
						break;
					case 3://顺子包选（5）	单式投注(0)
						sampleNums_one.add(9);
//						ctmPK.setBx_itemMsg("顺子", (Spannable)Html.fromHtml("奖金<font color=#dbba20>33</font>元"), R.drawable.pk_sz);
						ctmPK.setBx_itemMsg("顺子", "33", R.drawable.pk_sz);
						if(5 == bx_YL.size()&& bx_YL.get(1)!=null )
							ctmPK.setPK_yilouText(bx_YL.get(1));
						break;
					case 4://同花顺包选（3）	单式投注(0)
						sampleNums_one.add(8);
//						ctmPK.setBx_itemMsg("同花顺", (Spannable)Html.fromHtml("奖金<font color=#dbba20>535</font>元"), R.drawable.pk_ths);
						ctmPK.setBx_itemMsg("同花顺", "535", R.drawable.pk_ths);
						if(5 == bx_YL.size()&& bx_YL.get(0)!=null)
							ctmPK.setPK_yilouText(bx_YL.get(0));
						break;
					default:
						break;
			}
//			 为了解决屏幕适配问题  对屏幕测量  一下方式以及废弃掉  ---TODO
//			////System.out.println("HHH:"+AppUtil.getDisplayHeight(mActivity));
//			switch (AppUtil.getDisplayHeight(mActivity)) {
//				case 1280:// 屏幕适配  nuoio努比亚手机
//					ctmPkWidgetParam=new LayoutParams(LayoutParams.MATCH_PARENT, 208);//单张  包选pk   
//					break;
//	
//				case 480:// 屏幕适配    未测试--------TODO
//					ctmPkWidgetParam=new LayoutParams(LayoutParams.MATCH_PARENT, 90);//单张  包选pk  （83-90 之间  按比例计算理论值 区间）
//					break;
//				case 854:// 屏幕适配   小米手机
//					ctmPkWidgetParam=new LayoutParams(LayoutParams.MATCH_PARENT, 160);//单张  包选pk   
//					break;
//					
//				default:
//					break;
//			}
			
//			DensityUtil 工具类统配所有的屏幕 适配问题  将单位进行转换 解决屏幕走样问题
			ctmPkWidgetParam=new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mActivity, 105));//单张  包选pk  
			
			
			ctmPK.setShowPKNum(5,ctmPkWidgetParam,null);
	
			customItems_one.add(ctmPK);
		}
	}
	/**
	 *顺子 PK
	 *可以修改单独每一张的扑克高度 但是不便于维护
	 */
	private void initPK_4() {
		for (int i = 1; i <= 12 ; i++) {
			sampleNums_one.add(i);
			switch (i) {
					case 1:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("A 2 3");
//						ctmPK.setOneTextStr2("A");
//						ctmPK.setTwoTextStr2("2");
//						ctmPK.setThreeTextStr2("3");
				        if(12 == sz_YL.size() && sz_YL.get(0)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(0));
				        }
						break;
					case 2:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("2 3 4");
//						ctmPK.setOneTextStr2("2");
//						ctmPK.setTwoTextStr2("3");
//						ctmPK.setThreeTextStr2("4");
						if(12 == sz_YL.size() && sz_YL.get(1)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(1));
				        }
						break;
					case 3:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("3 4 5");
//						ctmPK.setOneTextStr2("3");
//						ctmPK.setTwoTextStr2("4");
//						ctmPK.setThreeTextStr2("5");
						if(12 == sz_YL.size() && sz_YL.get(2)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(2));
				        }
						break;
					case 4:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("4 5 6");
//						ctmPK.setOneTextStr2("4");
//						ctmPK.setTwoTextStr2("5");
//						ctmPK.setThreeTextStr2("6");
						if(12 == sz_YL.size() && sz_YL.get(3)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(3));
				        }
						break;
					case 5:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("5 6 7");
//						ctmPK.setOneTextStr2("5");
//						ctmPK.setTwoTextStr2("6");
//						ctmPK.setThreeTextStr2("7");
						if(12 == sz_YL.size() && sz_YL.get(4)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(4));
				        }
						break;
					case 6:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("6 7 8");
//						ctmPK.setOneTextStr2("6");
//						ctmPK.setTwoTextStr2("7");
//						ctmPK.setThreeTextStr2("8");
						if(12 == sz_YL.size() && sz_YL.get(5)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(5));
				        }
						break;
					case 7:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("7 8 9");
//						ctmPK.setOneTextStr2("7");
//						ctmPK.setTwoTextStr2("8");
//						ctmPK.setThreeTextStr2("9");
						if(12 == sz_YL.size() && sz_YL.get(6)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(6));
				        }
						break;
					case 8:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("8 9 10");
//						ctmPK.setOneTextStr2("8");
//						ctmPK.setTwoTextStr2("9");
//						ctmPK.setThreeTextStr2("10");
						if(12 == sz_YL.size() && sz_YL.get(7)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(7));
				        }
						break;
					case 9:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("9 10 J");
//						ctmPK.setOneTextStr2("9");
//						ctmPK.setTwoTextStr2("10");
//						ctmPK.setThreeTextStr2("J");
						if(12 == sz_YL.size() && sz_YL.get(8)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(8));
				        }
						break;
					case 10:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("10 J Q");
//						ctmPK.setOneTextStr2("10");
//						ctmPK.setTwoTextStr2("J");
//						ctmPK.setThreeTextStr2("Q");
						if(12 == sz_YL.size() && sz_YL.get(9)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(9));
				        }
						break;
					case 11://J
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("J Q K");
//						ctmPK.setOneTextStr2("J");
//						ctmPK.setTwoTextStr2("Q");
//						ctmPK.setThreeTextStr2("K");
						if(12 == sz_YL.size() && sz_YL.get(10)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(10));
				        }
						break;
					case 12://Q
						ctmPK=new CustomPK(mActivity);

						ctmPK.setOneTextStr("Q K A");
//						ctmPK.setOneTextStr2("Q");
//						ctmPK.setTwoTextStr2("K");
//						ctmPK.setThreeTextStr2("A");
						if(12 == sz_YL.size() && sz_YL.get(11)!=null){
				        	ctmPK.setPK_yilouText(sz_YL.get(11));
				        }
						break;
		
					default:
						break;
			}
//			ctmPK.setShowPKNum(4,null,null);
			ctmPK.setShowPKNum(1,null,null);
			customItems_one.add(ctmPK);
		}
	}
	/**
	 * 单张 扑克
	 */
	private void initPK_1() {
		for (int i = 1; i <= 13 ; i++) {
			sampleNums_one.add(i);
				switch (i) {
					case 1:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("A");
						if(rx_Yl.size() >= 13 && rx_Yl.get(0)!=null ){
							ctmPK.setPK_yilouText(rx_Yl.get(0));
						}
						break;
					case 11://J  rx_Yl中数据的下标从0 开始
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("J");
						if(rx_Yl.size() >= 13 && rx_Yl.get(10)!=null ){
							ctmPK.setPK_yilouText(rx_Yl.get(10));
						}
						break;
					case 12://Q
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("Q");
						if(rx_Yl.size() >= 13 && rx_Yl.get(11)!=null ){
							ctmPK.setPK_yilouText(rx_Yl.get(11));
						}
						break;
					case 13://K
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("K");
						if(rx_Yl.size() >= 13 && rx_Yl.get(12)!=null ){
							ctmPK.setPK_yilouText(rx_Yl.get(12));
						}
						break;
		
					default:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr(String.valueOf(i));
						if(rx_Yl.size() >= 13 && rx_Yl.get(i)!=null ){
							ctmPK.setPK_yilouText(rx_Yl.get(i));
						}
						break;
				   }
				
//				ctmPkWidgetParam=new LayoutParams(85,129);//单张pk
//				ctmPkWidgetParam2=new LayoutParams(85,92);//单张pk
				ctmPK.setShowPKNum(1,null,null);
				customItems_one.add(ctmPK);
		}
	}
	/**
	 * 对子 扑克
	 */
	private void initPK_2() {
		for (int i =1 ; i <= 13 ; i++) {
			sampleNums_one.add(i);
			switch (i) {
					case 1://A
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("A A");
//						ctmPK.setTwoTextStr("A");
						if(13 == dz_YL.size()  && dz_YL.get(0)!=null){
							ctmPK.setPK_yilouText(dz_YL.get(0));
						}
						break;
					case 10://J
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("10 10");
						ctmPK.getOneTextVew().setTextSize(14);
//						ctmPK.setTwoTextStr("J");
						if(13 == dz_YL.size()  && dz_YL.get(10)!=null){
							ctmPK.setPK_yilouText(dz_YL.get(10));
						}
						break;
					case 11://J
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("J J");
//						ctmPK.setTwoTextStr("J");
						if(13 == dz_YL.size()  && dz_YL.get(10)!=null){
							ctmPK.setPK_yilouText(dz_YL.get(10));
						}
						break;
					case 12://Q
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("Q Q");
//						ctmPK.setTwoTextStr("Q");
						if(13 == dz_YL.size()  && dz_YL.get(11)!=null){
							ctmPK.setPK_yilouText(dz_YL.get(11));
						}
						break;
					case 13://K
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("K K");
//						ctmPK.setTwoTextStr("K");
						if(13 == dz_YL.size()  && dz_YL.get(12)!=null){
							ctmPK.setPK_yilouText(dz_YL.get(12));
						}
						break;
					default:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr(String.valueOf(i)+" "+String.valueOf(i));
//						ctmPK.setTwoTextStr(String.valueOf(i));
						if(13 == dz_YL.size()  && dz_YL.get(i)!=null){
							ctmPK.setPK_yilouText(dz_YL.get(i));
						}
						break;
			}
//			ctmPK.setShowPKNum(2,null,null);
			ctmPK.setShowPKNum(1,null,null);
			
			customItems_one.add(ctmPK);
		}
	}
	/**
	 * 豹子 扑克
	 */
	private void initPK_3() {
		for (int i = 1; i <=13 ; i++) {
			sampleNums_one.add(i);
			switch (i) {
					case 1:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("A A A");
//						ctmPK.setTwoTextStr("A");
//						ctmPK.setThreeTextStr("A");
						if(13 == bz_YL.size()  && bz_YL.get(0)!=null){
							ctmPK.setPK_yilouText(bz_YL.get(0));
						}
						break;
					case 10://J
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("10 10 10");
						ctmPK.getOneTextVew().setTextSize(13);
//						ctmPK.setTwoTextStr("J");
//						ctmPK.setThreeTextStr("J");
						if(13 == bz_YL.size()  && bz_YL.get(10)!=null){
							ctmPK.setPK_yilouText(bz_YL.get(10));
						}
						break;
					case 11://J
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("J J J");
//						ctmPK.setTwoTextStr("J");
//						ctmPK.setThreeTextStr("J");
						if(13 == bz_YL.size()  && bz_YL.get(10)!=null){
							ctmPK.setPK_yilouText(bz_YL.get(10));
						}
						break;
					case 12://Q
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("Q Q Q");
//						ctmPK.setTwoTextStr("Q");
//						ctmPK.setThreeTextStr("Q");
						if(13 == bz_YL.size()  && bz_YL.get(11)!=null){
							ctmPK.setPK_yilouText(bz_YL.get(11));
						}
						break;
					case 13://K
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr("K K K");
//						ctmPK.setTwoTextStr("K");
//						ctmPK.setThreeTextStr("K");
						if(13 == bz_YL.size()  && bz_YL.get(12)!=null){
							ctmPK.setPK_yilouText(bz_YL.get(12));
						}
						break;
					default:
						ctmPK=new CustomPK(mActivity);
						ctmPK.setOneTextStr(String.valueOf(i)+" "+String.valueOf(i)+" "+String.valueOf(i));
//						ctmPK.setTwoTextStr(String.valueOf(i));
//						ctmPK.setThreeTextStr(String.valueOf(i));
						if(13 == bz_YL.size()  && bz_YL.get(i)!=null){
							ctmPK.setPK_yilouText(bz_YL.get(i));
						}
						break;
			}
//			ctmPK.setShowPKNum(3,null,null);
			ctmPK.setShowPKNum(1,null,null);
			customItems_one.add(ctmPK);
		}
	}
    /**
     * 	包选  加在布局 ( 标题 为 快乐扑克)
     */
	private void changeContentView_0() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.pk3_title_klpk));//快乐扑克
		setShakeNotice("至少选一张牌");//摇一摇提示
		// 初始化特殊布局
		initcontentLayout.removeAllViews();
		initcontentLayout.addView(LinearLayout.inflate(mActivity, R.layout.f_klpk_bx, null));//布局默认是 猜对子 
		//	提示
		pk3_tv_one=(TextView) initcontentLayout.findViewById(R.id.pk3_tv_one);
	   //   选号容器
		containers[0]=(MGridView) initcontentLayout.findViewById(R.id.pk3_mgv_one);
		
		initActionUpListener();
		
		initTextAndView(5);
		
		containers[0].simpleInit(false, sampleNums_one, 1, customItems_one);
		reload(true, 0);
		
		containers[0].setActionUpListener(new ActionUpListener() {
					
			@Override
			public void onActionUp() {
                basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
				
				shakeListener.vibrate();//震动100毫秒
				changeBtnbgAndTextColors(ticket.getLotteryNums().get(0).size());
			}
		});
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
   	}	


	@Override
	public void clearbtn_DefaultSetting() {
		if(basebuy_btn_clear!=null){
			basebuy_btn_clear.setTag("white");
			basebuy_btn_clear.setTextColor(MyApp.res.getColor(R.color.white));
			basebuy_btn_clear.setPadding(10, 10, 10, 10);
			basebuy_btn_clear.setGravity(Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL);
			basebuy_btn_clear.setText("清 空");
			basebuy_btn_clear.invalidate();
		}
	}
	@Override
	public void showOrup() {
		Drawable mDrawable=null;
		if(isScroll){
			 isScroll=false;
			 drawerLayout.scrollTo(0, -300);
			  mDrawable=MyApp.res.getDrawable(R.drawable.h_up);
			  mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
						mDrawable.getMinimumHeight());
			 drop_down_d.setCompoundDrawables(null, null,mDrawable, null);
		}else{
			isScroll=true;
			drawerLayout.scrollTo(0, 0);
			 mDrawable=MyApp.res.getDrawable(R.drawable.h_down);
			  mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
						mDrawable.getMinimumHeight());
			 drop_down_d.setCompoundDrawables(null, null,mDrawable, null);
		}
		drop_down_d.invalidate();
	}
	@Override
	public void onReceiveYilou_start() {
		show(baseAwait_layout);
		fast3_expect_Rlyout.setVisibility(View.INVISIBLE);//  不显示 特殊title布局未获得更新后期号前
	}
	@Override
	public void onReceiveYilou_stop() {
		hide(baseAwait_layout);
		show(fast3_expect_Rlyout);
		dialog=new TCDialogs(mActivity);
		String issueStr=CustomTagEnum.lottery_pk3.getIssue();
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
		hide(baseAwait_layout);
		show(fast3_expect_Rlyout);
//		遗漏数据 重新请求 刷新
		 if(0==rx_Yl.size()||0==th_YL.size()
		    		||0==sz_YL.size()|| 0==ths_YL.size()
		    		||0==bz_YL.size()||0==dz_YL.size()
		    		||0==bx_YL.size()){
				booleanTag=true;
				sendgetYilouRequest();//请求遗漏
			}
	}

  
}
