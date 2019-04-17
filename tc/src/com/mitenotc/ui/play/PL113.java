package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.IntentFilter;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.RefreshYiLouReceiver;
/**
 * 113   多乐彩(11选5)
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "113",salesType = "0",childId="2")
public class PL113 extends BaseBuyFragment implements RefreshYiLou {
	private RelativeLayout elevenC5_two_RL;//千位布局
	private RelativeLayout elevenC5_three_RL;//百位布局
	
	
	private LinearLayout notice_linearLayout;//万位选号上的提示
	private LinearLayout notice_linearLayout1;//千位选号上的提示
	private LinearLayout notice_linearLayout2;//千位选号上的提示
	
	private TextView elevenC5_show_bonus_tv;//提示
	private Spannable redstr;//提示信息金额
	
	private TextView elevenC5_one_tv;//万位 TextView
	private TextView elevenC5_two_tv;//万位 TextViewe
	private TextView elevenC5_Iguess1_tv;//我认为（小字提示）（万位）
	private TextView elevenC5_Iguess1_tv1;//我认为（小字提示）（千位）
	
	private MessageJson paramsJson;
	private List<String> commonYLList;//普通遗漏
	private List<String> wWYLList;//万位遗漏
	private List<String> qWYLList;//千位遗漏
	private List<String> bWYLList;//百位遗漏
	
	private RefreshYiLouReceiver mLouReceiver;
	private TCDialogs dialog;//提示框
//	private int nextIssue=00;  // 下一期期号
	
	
	@Override
	public void onStart() 
	{
		super.onStart();
		registerBroadcastReceiver();// ----TODO  测试暂时取消  广播
		sendgetYilouRequest();
	}
	
	/**
	 * 注册广播
	 */
	private void registerBroadcastReceiver()
	{
        if(mLouReceiver==null){
        	mLouReceiver =new RefreshYiLouReceiver("113"); 
        	mLouReceiver.setmRefreshYiLou(this);
        	IntentFilter mFilter=new IntentFilter();
        	mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_start_loading");
        	mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_stop_loading");
        	mFilter.addAction("com.mitenotc.ui.play.on_lottery_"+MyApp.order.getLotteryId()+"_awardinfo_received");
        	
        	mActivity.registerReceiver(mLouReceiver, mFilter); 
        }
	}
	
	 	
	 	
   @Override
	public void onStop() 
   {
		super.onStop();
//		 if(mLouReceiver!=null)
//		 {
//			 mActivity.unregisterReceiver(mLouReceiver);
//		 }
		 shakeListener.stop();
		 clear();
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
		
		return View.inflate(mActivity, R.layout.f_elevenchoose5_pop_titile_rb, null);
	}
	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 *
	 */

	@Override
	protected void initContainers() {

		containers[0].setMiniNum(1);//万
		containers[1].setMiniNum(1);//千
		containers[2].setMiniNum(1);//百
		
		containers[0].simpleInit(1, MGridView.RED_MODE, 1, 11);//万
		containers[1].simpleInit(1, MGridView.RED_MODE, 1, 11);//千
		containers[2].simpleInit(1, MGridView.RED_MODE, 1, 11);//百
		
	   containers[0].set_YiLou_Visibility(true);//有遗漏
	   containers[1].set_YiLou_Visibility(true);
	   containers[2].set_YiLou_Visibility(true);
	   
	}
	
    /**
     * 玩法子布局
     * 
     */
	
	@Override
	protected View customLotteryView() {
		View v=View.inflate(mActivity, R.layout.play_eleven_choose5, null);
		
		 containers[0]=(MGridView)v.findViewById(R.id.wan_mgv);//万
		 containers[1]=(MGridView)v.findViewById(R.id.qian_mgv);//千
		 containers[2]=(MGridView)v.findViewById(R.id.bai_mgv);//百
		 
		 elevenC5_two_RL=(RelativeLayout) v.findViewById(R.id.elevenC5_two_RL);//千 布局
		 elevenC5_three_RL=(RelativeLayout) v.findViewById(R.id.elevenC5_three_RL);//百 布局
		 
		 notice_linearLayout=(LinearLayout) v.findViewById(R.id.notice_linearLayout);//万位提示 布局
		 notice_linearLayout1=(LinearLayout) v.findViewById(R.id.notice_linearLayout1);//千位 布局
		 notice_linearLayout2=(LinearLayout) v.findViewById(R.id.notice_linearLayout2);//百位 布局
		 
		 elevenC5_show_bonus_tv=(TextView) v.findViewById(R.id.elevenC5_show_bonus_tv);//猜中开奖...(提示)
		 elevenC5_one_tv=(TextView) v.findViewById(R.id.elevenC5_one_tv);//万位
		 elevenC5_two_tv=(TextView) v.findViewById(R.id.elevenC5_two_tv);//千位 
		 elevenC5_Iguess1_tv=(TextView) v.findViewById(R.id.elevenC5_Iguess1_tv);//万位小字提示
		 elevenC5_Iguess1_tv1=(TextView) v.findViewById(R.id.elevenC5_Iguess1_tv1);//千位小字提示
		 
		 fast3_await_cb.setVisibility(View.GONE);  //隐藏等待开奖提示；BaseBuyFragment 中控制倒计时
		 
		 sendgetYilouRequest();
		 return v;
	}
	
	/**
	 * 发送遗漏请求
	 */
	private void sendgetYilouRequest() {
		 paramsJson=new MessageJson();
		 paramsJson.put("A", "113");
		 submitData(111, 1204, paramsJson);
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean mBean = (MessageBean) msg.obj;
         switch (msg.arg1)
         {
		//		请求成功
				case 111:
		            if("0".equals(mBean.getA())){
			               initList();//初始化  容器
			               if(mBean.getE1()==null ||mBean.getE2()==null ||mBean.getE3()==null ||mBean.getE4()==null)
			               {
			            	   return;
			               }else 
			               {
			            	  try {
							//      		 C	普通遗漏值
											JSONObject je1=new JSONObject(mBean.getE1());
							//      		 D	万位遗漏值
											JSONObject je2=new JSONObject(mBean.getE2());
							//      		 E	千位遗漏值
											JSONObject je3=new JSONObject(mBean.getE3());
							//      		 F	百位遗漏值
											JSONObject je4=new JSONObject(mBean.getE4());
											
											
											commonYLList.clear();//清空容器
											wWYLList.clear();
											qWYLList.clear();
											bWYLList.clear();
											
											if(je1 !=null && je2 !=null && je3 !=null && je4!=null)
											{
												for (int i = 1; i <= 11; i++) 
												{
													commonYLList.add(je1.getString(""+i));//普通
													wWYLList.add(je2.getString(""+i));//万位
													qWYLList.add(je3.getString(""+i));//千位 
													bWYLList.add(je4.getString(""+i));//百位
												}
												toViewaddYiLouNums(Integer.parseInt(salesType+childId)); 
											}
                               
                              } catch (JSONException e) {
								e.printStackTrace();
							}
			            	  
			              }
			            
			         }
		        	
					break;
		
				default:
					break;
		}
	
		
	}
	/**
	 * 根据onMessageReceived  返回值添加遗漏值
	 */
	private void toViewaddYiLouNums(int tempNumTag)
	{
		switch (tempNumTag)
		{
//		//		       任选二
		case 2:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList); // 默认 界面
					containers[0].notifyDataSetChanged();
				}
			break;
//			任选三
		case 3:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			任选四
		case 4:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			任选五
		case 5:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			任选六
		case 6:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			任选七
		case 7:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			任选八			
		case 8:
				if(commonYLList!=null && commonYLList.size() ==11){
					
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			前一			
		case 1:
				if(wWYLList!=null && wWYLList.size() ==11){
					
					containers[0].set_YiLou_text(wWYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			前二直选
		case 39:
				if(wWYLList!=null && wWYLList.size() ==11 &&
						qWYLList!=null && qWYLList.size() ==11)
				{
					containers[0].set_YiLou_text(wWYLList);
					containers[1].set_YiLou_text(qWYLList);
					
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
				}
			break;
//			前二组选
		case 11:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			前三直选
		case 310:
				if(wWYLList!=null && wWYLList.size() ==11 &&
				qWYLList!=null && qWYLList.size() ==11
				&& bWYLList!=null && bWYLList.size() ==11)
				{
					containers[0].set_YiLou_text(wWYLList);
					containers[1].set_YiLou_text(qWYLList);
					containers[2].set_YiLou_text(bWYLList);
					
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
					containers[2].notifyDataSetChanged();
				}
			break;
			
//			前三组选
		case 12:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
				}
			break;
//			-------------------------------胆码---------------------------------
//			任选二
		case 22:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[1].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
				 }
		  break;
//			任选三		  
		case 23:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[1].set_YiLou_text(commonYLList);
					
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
				}
		  break;
//			任选四	
		case 24:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[1].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
				}
		  break;
//			任选五
		case 25:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[1].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
					
				}
			  break;
//		        任选六			  
		case 26:
				if(commonYLList!=null && commonYLList.size() ==11)
				{
					containers[0].set_YiLou_text(commonYLList);
					containers[1].set_YiLou_text(commonYLList);
					containers[0].notifyDataSetChanged();
					containers[1].notifyDataSetChanged();
				}
			 break;
//			任选七			 
		case 27:
			if(bWYLList!=null && bWYLList.size() ==11){
				
				containers[0].set_YiLou_text(bWYLList);
				containers[1].set_YiLou_text(bWYLList);
				
				containers[0].notifyDataSetChanged();
				containers[1].notifyDataSetChanged();
			}
		   break;
//		前二组选		   
		case 211:
			if(bWYLList!=null && bWYLList.size() ==11){
				containers[0].set_YiLou_text(bWYLList);
				containers[1].set_YiLou_text(bWYLList);
				
				containers[0].notifyDataSetChanged();
				containers[1].notifyDataSetChanged();
			}
			 break;
//		前三组选
		case 212:
			if(commonYLList!=null && commonYLList.size() ==11){
				
				containers[0].set_YiLou_text(commonYLList);
				containers[1].set_YiLou_text(commonYLList);
				
				containers[0].notifyDataSetChanged();
				containers[1].notifyDataSetChanged();
			}
		 break;
		 
		default:
			break;
		}
//		setv_ksIssue(true);
	}
	
//	/***
//     * 设置title  当前销售期提示
//     * @return
//     */
//	private void setv_ksIssue(boolean isGone) {
//		 String issueStr = CustomTagEnum.getItemByLotteryId(120).getIssue();
//		 if(issueStr!=null && issueStr.length() > 5){
//			 issueStr = issueStr.substring(5, issueStr.length());
//			 fast3_expect_tv.setText("第"+issueStr +"期");
//			 if(issueStr!=null && !"".equals(issueStr)){
//				 nextIssue=Integer.parseInt(issueStr);
//			 }
//			 fast3_distance_tv.setText("距"+(nextIssue+1)+"期截止:");
//		 }else {
//			 fast3_expect_tv.setText("第00期");
//			 fast3_distance_tv.setText("距00期截止:");
//		 }
//		
//		 if(isGone){
//			 fast3_await_cb.setVisibility(View.GONE); //title 时间提示旁边显示等待开奖中  默认是gone
//		 }else {
//			 fast3_await_cb.setVisibility(View.VISIBLE); //title 时间提示旁边显示等待开奖中  默认是gone
//		 }
//	}
	// 初始化容器
	private void initList()
	{
		commonYLList=new ArrayList<String>();//普通遗漏
    	wWYLList=new ArrayList<String>();//万位遗漏
    	qWYLList=new ArrayList<String>();//千位遗漏
    	bWYLList=new ArrayList<String>();//百位遗漏
	}
	@Override
	protected void onSaleTypeChanged(String tag)
   {
		 
		tag=tag.replace(",", "");
        int tempNum = Integer.parseInt(tag);
		switch (tempNum)
		{
		//		       任选二
				case 2:
					initSaleType_2();//第一次初始化遗漏数据是在线程请求完成之后请求的
					
					break;
		//			任选三
				case 3:
					initSaleType_3();
					break;
		//			任选四
				case 4:
					initSaleType_4();
					break;
		//			任选五
				case 5:
					initSaleType_5();
					break;
		//			任选六
				case 6:
					initSaleType_6();
					break;
		//			任选七
				case 7:
					initSaleType_7();
					break;
		//			任选八			
				case 8:
					initSaleType_8();
					break;
		//			前一			
				case 1:
					initSaleType_1();
					break;
		//			前二直选
				case 39:
					initSaleType_39();
					break;
		//			前二组选
				case 11:
					initSaleType_11();
					break;
		//			前三直选
				case 310:
					initSaleType_310();
					break;
					
		//			前三组选
				case 12:
					initSaleType_12();
					break;
		//			-------------------------------胆码---------------------------------
		//			任选二
				case 22:
					initSaleType_22();
				  break;
		//			任选三		  
				case 23:
					initSaleType_23();
				  break;
		//			任选四	
				case 24:
					initSaleType_24();
				  break;
		//			任选五
				case 25:
					initSaleType_25();
					  break;
		//		        任选六			  
				case 26:
					initSaleType_26();
					 break;
		//			任选七			 
				case 27:
					initSaleType_27();
					
				   break;
		//		前二组选		   
				case 211:
					initSaleType_211();
					
					 break;
		//		前三组选
				case 212:
					initSaleType_212();
				 break;
			default:
				break;
		}
		basebuy_tv_notice.setText("");//清空底部提示
		toViewaddYiLouNums(tempNum);
	}
	
	/**
	 *  gone
	 *  千位布局
	 *   和百位布局
	 */
	private void goneRL()
	{
		elevenC5_two_RL.setVisibility(View.GONE);//千位布局gone
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局gone
	}	
	
	/**
	 *  visible
	 *  千位布局
	 *   和百位布局
	 */
	private void visibleRL()
	{
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		elevenC5_three_RL.setVisibility(View.VISIBLE);//百位布局VISIBLE
	}
	/**
	 * TextView 内容提示修改
	 * 
	 * **** 1任选类修改*******
	 * 
	 * elevenC5_show_bonus_tv----> 猜中开奖的全部  + chooseNums + 个号即奖+reds
	 *  elevenC5_one_tv----->选号
	 * reds 中奖规则中的（红色金额）部分
	 */
	private void changeTwoTVmsg(String wanweistr,String chooseNums,String reds)
	{
			redstr=(Spannable) Html.fromHtml(getString(R.string.show_guess_msg)+chooseNums
		   +getString(R.string.now_award)+"<font color=#ff0000>"+reds+"</font>");
			elevenC5_show_bonus_tv.setText(redstr);
			elevenC5_one_tv.setText(wanweistr);//万位选号
	}
	
	/**
	 * TextView 内容提示修改
	 * 
	 * **** 1任选类修改*******
	 * 
	 * elevenC5_show_bonus_tv----> 猜中开奖的全部  + chooseNums + 个号即奖+reds
	 *  elevenC5_one_tv----->选号
	 * 
	 */
	private void changeTwoTVmsg1(String wanweistr,String chooseNums,String reds)
	{
			redstr=(Spannable) Html.fromHtml(getString(R.string.show_guess_msg1)+chooseNums
		   +getString(R.string.now_award1)+"<font color=#ff0000>"+reds+"</font>");
			elevenC5_show_bonus_tv.setText(redstr);
			elevenC5_one_tv.setText(wanweistr);//万位选号
	}
	
	/**
	 * 提示信息 金额显示红色
	 * @param str1
	 * @param str2
	 * @return
	 * 
	 */
	private Spannable extracted(String str1,String str2)
	{
	    	redstr=(Spannable) Html.fromHtml(str1 +"<font color=#ff0000>"+str2+"</font>");
		 return redstr;
	}
	/**
	 * 任选二
	 */
	private void initSaleType_2() 
	{
		setTitleText(getString(R.string.anychoose_rbtn_text_1)); // 任选二
		goneRL();
		setShakeNotice(getString(R.string.group_choose_three_playmethod_tv2));
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		
		changeTwoTVmsg("选号","2","6元");//改变TextView 对应的text
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		
		containers[0].setMiniNum(2);//至少
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
	}
	
	/**
	 * 任选三
	 */
	private void initSaleType_3() {
		goneRL();
		setTitleText(getString(R.string.anychoose_rbtn_text_2));
		
		setShakeNotice(getString(R.string.group_choose_playmethod_tv));
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		
		changeTwoTVmsg("选号","3","19元");//改变TextView 对应的text
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(3);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
	}
	
	/**
	 * 任选四
	 */
	private void initSaleType_4() {
		goneRL();
		setTitleText(getString(R.string.anychoose_rbtn_text_3));
		setShakeNotice(getString(R.string.ssc_shakenotice_text1)+"4"
				+getString(R.string.ssc_shakenotice_text2));
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		changeTwoTVmsg("选号","4","78元");//改变TextView 对应的text
		
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(4);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
		
		
		
	}
	
	/**
	 * 任选五
	 */
	private void initSaleType_5() {
		goneRL();
		setTitleText(getString(R.string.anychoose_rbtn_text_4));
		setShakeNotice(getString(R.string.ssc_shakenotice_text1)+"5"
				+getString(R.string.ssc_shakenotice_text2));
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		changeTwoTVmsg("选号","5","540元");//改变TextView 对应的text
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(5);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
		
		
	}
	/**
	 * 任选六
	 */
	private void initSaleType_6() {
		goneRL();
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_5));
		changeTwoTVmsg("选号","6","90元");//改变TextView 对应的text
		setShakeNotice(getString(R.string.ssc_shakenotice_text1)+"6"
				+getString(R.string.ssc_shakenotice_text2));
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(6);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
		
		
	}
	/**
	 * 任选七
	 */
	private void initSaleType_7() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_6));
		
		goneRL();
		changeTwoTVmsg("选号","7","26元");//改变TextView 对应的text
		setShakeNotice(getString(R.string.ssc_shakenotice_text1)+"7"
				+getString(R.string.ssc_shakenotice_text2));
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(7);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
		
	}
	/**
	 * 任选八  协议中多乐彩只有 单式玩法
	 */
	private void initSaleType_8() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_7));//任选八
		goneRL();
		changeTwoTVmsg("选号","8","9元");//改变TextView 对应的text
		setShakeNotice(getString(R.string.ssc_shakenotice_text1)+"8"
				+getString(R.string.ssc_shakenotice_text2));
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(8);
		containers[0].setMaxNum(8);//重置最大数,如果最大数设为0,代表不设置最大数
		
		
	}
	/**
	 * 前一
	 */
	private void initSaleType_1() {
		goneRL();
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_8));
		
		changeTwoTVmsg1("选号","1","13元");//改变TextView 对应的text
		setShakeNotice(getString(R.string.ssc_shakenotice_text1)+"1"
				+getString(R.string.ssc_shakenotice_text2));
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(1);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
	}
	/**
	 * 前二直选
	 */
	private void initSaleType_39() {
		cancelMutualContainers();//取消容器互斥
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text_9));
		
		setShakeNotice(getString(R.string.ssc_shakenotice_text7));//每位至少选择一个号
		elevenC5_one_tv.setText("万位");
		redstr = extracted("与开奖前2位一一对应即奖", "130元");
		elevenC5_show_bonus_tv.setText(redstr);
		
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局gone
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		notice_linearLayout1.setVisibility(View.GONE);//隐藏千位选号上的提示
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局gone
		
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
		
		mutualContainers(0,1); //互斥区
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(1);
		
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 * 前二组选
	 */
	private void initSaleType_11() {
		goneRL();
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text0_1));
		
	    redstr = extracted("与开奖前2位顺序不限即奖" , "65元");
		elevenC5_show_bonus_tv.setText(redstr);
		elevenC5_one_tv.setText("选号");//万位选号
		setShakeNotice("至少选2个号");
		notice_linearLayout.setVisibility(View.GONE);//隐藏万位选号上的提示
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		
		
	    containers[0].set_YiLou_text(wWYLList);//改变为万位上自己对应的遗漏
		containers[0].setMiniNum(2);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
	}
	
	
	/**
	 * 前三直选
	 * 默认全部
	 */
	
	private void initSaleType_310() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text0_2));
		setShakeNotice(getString(R.string.ssc_shakenotice_text7));//每位至少选择一个号
		
		cancelMutualContainers();//取消容器互斥
		visibleRL();
		redstr = extracted("与开奖前3位一一对应即奖" , "1170元");
		elevenC5_show_bonus_tv.setText(redstr);
		
		elevenC5_one_tv.setText("万位");
		elevenC5_two_tv.setText("千位");
		
		notice_linearLayout.setVisibility(View.GONE);//万位选号上的提示
		notice_linearLayout1.setVisibility(View.GONE);//千位选号上的提示
		notice_linearLayout2.setVisibility(View.GONE);
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2);
		
		mutualContainers(0,1,2); //互斥区
		
		containers[0].set_YiLou_text(wWYLList);//改变为万位上自己对应的遗漏
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(1);
		containers[2].setMiniNum(1);
		
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
		containers[2].setMaxNum(0);
		
	}

	/**
	 * 前三组选
	 *
	 */
	
	private void initSaleType_12() {
		goneRL();
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.anychoose_rbtn_text0_3));
		
		notice_linearLayout.setVisibility(View.GONE);
//		redstr=(Spannable) Html.fromHtml("<font color=#606060>"+"猜中开奖前3位顺序不限即奖"
//		        +"</font></font color=#ff0000>"+"195元"+"</font>");
		 redstr = extracted("猜中开奖前3位顺序不限即奖" , "195元");
		elevenC5_show_bonus_tv.setText(redstr);
		elevenC5_one_tv.setText("选号");//万位选号
		setShakeNotice("至少选择3个号");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setMiniNum(3);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
	}
	
	/**
	 *胆拖
	 *任选二
	 */
	
	private void initSaleType_22() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text_1));
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		 redstr = extracted("猜中开奖的任意2个号即奖" , "6元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的任意2个号即奖"
//				        +"</font></font color=#ff0000>"+"6元"+"</font>"));
//		小字提示
		elevenC5_Iguess1_tv.setText("请选择1个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
//		选号左边提示
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(1);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *胆拖
	 *任选三
	 */
	
	private void initSaleType_23() {
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text_2));
		changeShakeVisibility(false);//true显示
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		redstr = extracted("猜中开奖的任意3个号即奖", "19元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的任意3个号即奖"
//				        +"</font></font color=#ff0000>"+"19元"+"</font>"));
//		小字提示
		elevenC5_Iguess1_tv.setText("至少选择1个,最多2个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");
		
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMiniNum(2);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(2);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *胆拖
	 *任选四
	 */
	
	private void initSaleType_24() {
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text_3));
		changeShakeVisibility(false);//true显示
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		redstr = extracted("猜中开奖的任意4个号即奖", "78元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的任意4个号即奖"
//				        +"</font></font color=#ff0000>"+"78元"+"</font>"));
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
//		小字提示
		elevenC5_Iguess1_tv.setText("至少选择1个,最多3个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMiniNum(3);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(3);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *胆拖
	 *任选五
	 */
	
	private void initSaleType_25() {
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text_4));
		changeShakeVisibility(false);//true显示
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		redstr = extracted("猜中开奖的任意5个号即奖", "540元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的任意5个号即奖"
//				        +"</font></font color=#ff0000>"+"540元"+"</font>"));
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
//		小字提示
		elevenC5_Iguess1_tv.setText("至少选择1个,最多4个");
		elevenC5_Iguess1_tv1.setText("至少选2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMiniNum(4);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(4);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *胆拖
	 *任选六
	 */
	
	private void initSaleType_26() {
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text_5));
		changeShakeVisibility(false);//true显示
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		redstr = extracted("猜中开奖的全部6个号即奖", "90元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的全部5个号即奖"
//				        +"</font></font color=#ff0000>"+"90元"+"</font>"));
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
//		小字提示
		elevenC5_Iguess1_tv.setText("至少选择1个,最多5个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
	
		containers[0].setMiniNum(5);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(5);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *胆拖
	 *任选七
	 */
	
	private void initSaleType_27() {
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text_6));
		changeShakeVisibility(false);//true显示
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		 redstr = extracted("猜中开奖的全部7个号即奖", "26元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的全部5个号即奖"
//				        +"</font></font color=#ff0000>"+"26元"+"</font>"));
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
		
//		小字提示
		elevenC5_Iguess1_tv.setText("至少选择1个,最多6个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
		
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMiniNum(6);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(6);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}

	/**
	 *胆拖
	 *前二组
	 */
	
	private void initSaleType_211() {
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text0_1));
		changeShakeVisibility(false);//true显示
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		 redstr = extracted("猜中开奖的前2位顺序不限即奖", "65元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的前2位顺序不限即奖"
//				        +"</font></font color=#ff0000>"+"65元"+"</font>"));
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
//		小字提示
		elevenC5_Iguess1_tv.setText("请选择1个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(1);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *胆拖
	 *前三组
	 */
	
	private void initSaleType_212() 
	{
		setTitleText("胆拖-"+getString(R.string.anychoose_rbtn_text0_3));
		changeShakeVisibility(false);//摇一摇
		notice_linearLayout.setVisibility(View.VISIBLE);
		notice_linearLayout1.setVisibility(View.VISIBLE);
		
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位布局VISIBLE
		redstr = extracted("猜中开奖的前3位顺序不限即奖", "195元");
		elevenC5_show_bonus_tv.setText(redstr);
//				Html.fromHtml("<font color=#606060>"+"猜中开奖的前3位顺序不限即奖"
//		        +"</font></font color=#ff0000>"+"195元"+"</font>"));
		elevenC5_three_RL.setVisibility(View.GONE);//百位布局
//		小字提示
		elevenC5_Iguess1_tv.setText("至少选择1个,最多2个");
		elevenC5_Iguess1_tv1.setText("至少选择2个最多选择10个");//getString(R.string.elevenC5_Iguess_little_text));
		
		elevenC5_one_tv.setText("胆码");//万位
		elevenC5_two_tv.setText("拖码");//千位
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
        cancelMutualContainers();//取消容器互斥
		
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		containers[0].setMiniNum(2);
		containers[1].setMiniNum(1);
		containers[0].setMaxNum(2);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
	/**
	 *  实现刷新遗漏数据
	 */
	@Override
	public void onReceiveYilou_start() 
	{
		sendgetYilouRequest();//重新请求 实现遗漏的更新
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
	/**
	 * 实现刷新遗漏  的接口
	 */
	@Override
	public void onReceiveYilou_stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveYilou_awardinfo() {
		// TODO Auto-generated method stub
		
	}

	
}
