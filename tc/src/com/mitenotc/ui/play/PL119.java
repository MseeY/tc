package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Message;
import android.os.RecoverySystem;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.utils.AppUtil;
/**
 *119	江西时时彩
 * 
 * 套用的是时时彩的布局
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "119",salesType = "0" ,childId="1")
public class PL119 extends BaseBuyFragment {
	private View playView;//选号界面
	private Spannable bonusText;//奖金提示
	private RelativeLayout reLayout1;//总布局
	private RelativeLayout elevenC5_two_RL;//千位
	private RelativeLayout elevenC5_three_RL;//百位
	private RelativeLayout elevenC5_four_RL;//十位
	private RelativeLayout elevenC5_five_RL;//个位
	private RelativeLayout elevenC5_sex_RL;//大小单双时使用 十位
	private RelativeLayout elevenC5_seven_RL;//大小单双时使用 个位
	
	private TextView elevenC5_show_bonus_tv;//开奖提示
	private TextView elevenC5_one_tv;//万位提示
	private TextView elevenC5_two_tv;//千位提示
	private TextView elevenC5_three_tv;//百位位提示
	private TextView elevenC5_four_tv;//十位提示
	private TextView elevenC5_five_tv;//个位提示
	private TextView elevenC5_one_omit_tv;//万位遗漏
	private TextView elevenC5_two_omint_tv;//千位遗漏 
	
	
//	private MessageJson paramsJson;
//	private List<String> commonYLList;//普通遗漏
	
//	private List<String> WWYLList;//万位遗漏
//	private List<String> QWYLList;//千位遗漏
//	private List<String> BWYLList;//百位遗漏
//	private List<String> SWYLList;//十位遗漏
//	private List<String> GWYLList;//个位遗漏
	
//	每位至少选 n 个号
	private String eachText;


	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 *
	 */
	
	@Override
	protected void initContainers() {
		
		containers[0].setMaxNum(0);//容器最大值为0 表示不设置最大值
		containers[1].setMaxNum(0);
		containers[2].setMaxNum(0);
		containers[3].setMaxNum(0);
		containers[4].setMaxNum(0);
		
		containers[0].simpleInit(false,1, MGridView.RED_MODE, 0, 9,false);//最少选择一个 默认红色 0-9 之间
		containers[1].simpleInit(false,1, MGridView.RED_MODE, 0, 9,false);
		containers[2].simpleInit(false,1, MGridView.RED_MODE, 0, 9,false);
		containers[3].simpleInit(false,1, MGridView.RED_MODE, 0, 9,false);
		containers[4].simpleInit(false,1, MGridView.RED_MODE, 0, 9,false);
		
		
		
		containers[0].setMutualContainers(containers);
		containers[1].setMutualContainers(containers);
		containers[2].setMutualContainers(containers);
		containers[3].setMutualContainers(containers);
		containers[4].setMutualContainers(containers);
		
		//大小单双 专用
		containers[5].simpleInit_ssc_large_small();
		containers[6].simpleInit_ssc_large_small();
	
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
		return View.inflate(mActivity, R.layout.f_pl119_titile_rb, null);
	}
	
	@Override
	protected View customLotteryView() {
		playView=View.inflate(mActivity, R.layout.play_ssc, null);
		reLayout1  = (RelativeLayout) playView.findViewById(R.id.reLayout1);//总布局
		
		
		elevenC5_show_bonus_tv=(TextView) playView.findViewById(R.id.elevenC5_show_bonus_tv);//开奖提示
		elevenC5_one_omit_tv=(TextView) playView.findViewById(R.id.elevenC5_one_omit_tv);//万位遗漏
		elevenC5_two_omint_tv=(TextView) playView.findViewById(R.id.elevenC5_two_omint_tv);//千位遗漏 
		elevenC5_one_tv =(TextView) playView.findViewById(R.id.elevenC5_one_tv);//万位提示
		elevenC5_two_tv =(TextView) playView.findViewById(R.id.elevenC5_two_tv);//千位提示
		elevenC5_three_tv =(TextView) playView.findViewById(R.id.elevenC5_three_tv);//百位提示
		elevenC5_four_tv  =(TextView) playView.findViewById(R.id.elevenC5_four_tv);//十位提示
		elevenC5_five_tv  =(TextView) playView.findViewById(R.id.elevenC5_five_tv);//个位提示
		
		containers[0]=(MGridView) playView.findViewById(R.id.wan_mgv);//万位
		containers[1]=(MGridView) playView.findViewById(R.id.qian_mgv);//千位
		containers[2]=(MGridView) playView.findViewById(R.id.bai_mgv);//百位
		containers[3]=(MGridView) playView.findViewById(R.id.shi_mgv);//十位
		containers[4]=(MGridView) playView.findViewById(R.id.ge_mgv);//个位
		containers[5]=(MGridView) playView.findViewById(R.id.da_mgv);// 大小单双  十位
		containers[6]=(MGridView) playView.findViewById(R.id.xiao_mgv);// 大小单双  个位
		elevenC5_two_RL = (RelativeLayout) playView.findViewById(R.id.elevenC5_two_RL);//千位
		elevenC5_three_RL = (RelativeLayout) playView.findViewById(R.id.elevenC5_three_RL);//百位
		elevenC5_four_RL = (RelativeLayout) playView.findViewById(R.id.elevenC5_four_RL);//十位
		elevenC5_five_RL = (RelativeLayout) playView.findViewById(R.id.elevenC5_five_RL);//个位
		elevenC5_sex_RL = (RelativeLayout) playView.findViewById(R.id.elevenC5_sex_RL);//大小单双时使用    十位 默认是gone
		elevenC5_seven_RL = (RelativeLayout) playView.findViewById(R.id.elevenC5_seven_RL);//大小单双时使用     个位 默认是gone
		
		 fast3_await_cb.setVisibility(View.GONE);  //隐藏等待开奖提示；
//		 获取遗漏号码List  1204 查询号码遗漏值(11选5)
//		 输入参数：
//		 标签	描述
//		 A	玩法代码
//		 paramsJson=new MessageJson();
//		 paramsJson.put("A", "112");
//		 submitData(1, 1204, paramsJson);
		return playView;
	}

	

	@Override
	protected void onSaleTypeChanged(String tag) {
		eachText = getString(R.string.ssc_shakenotice_text7);//getString(R.string.ssc_shakenotice_each_text)+"1"+getString(R.string.ssc_shakenotice_each_text1);
		
		basebuy_tv_notice.setText("");//清空底部提示
		
		tag=tag.replace(",", "");
		int type=Integer.parseInt(tag);
		if(22==type){
			reLayout1.setVisibility(View.GONE);//大小单双
		}else{
			reLayout1.setVisibility(View.VISIBLE);//其它 布局时都显示
			elevenC5_sex_RL.setVisibility(View.GONE);//大小单双专用
			elevenC5_seven_RL.setVisibility(View.GONE);
		}
		//二星组选 ,三星组三复式 ,三星组六 ,三星组三单式
//		if(52==type||33==type||43==type||23==type){
//			removeYiLouTV(containers[0],containers[1]);
//		}else{
//			addYiLouTV(containers[0],containers[1]);
//		}
		
		switch (type) {
//		       一星直选
		case 1:
			initSaleType_1();
			break;
//			二星直选
	     case 2:
	    	 initSaleType_2();
			break;
//			二星组选
	     case 52:
	    	 initSaleType_52();	
			break;
//			三星直选
	     case 3:
	    	 initSaleType_3();		
			break;
//			三星组三单式
	     case 23:
	    	 initSaleType_23();	
			break;
//			三星组三复式
	     case 33:
	    	 initSaleType_33();		
			break;
			
//			三星组六
	     case 43:
	    	 initSaleType_43();	
	    	 
			break;
//			四星直选
	     case 4:
	    	 initSaleType_4();	
	    	 
			break;
			
//			五星直选
	     case 5:
	    	 initSaleType_5();	
	    	 
			break;
			
//			五星通选 35
	     case 35:
	    	 initSaleType_35();	
	    	
			break;
//			大小单双
	     case 22:
	    	 initSaleType_22();	
	    	 
			break;
//			任选一
	     case 6:
	    	 initSaleType_6();	
	    	 
			break;
//			任选二
	     case 7:
	    
	    	 initSaleType_7();	
			break;
		default:
			
			break;
		}

	}
	
	 /**
     * UI 界面选号 获取一个随机 int 数字  任选一  任选 二 用到
     * 
     */
	 static Random  mRandom=null;
	 public static  int[] getint(){
			int[] random2 = new int[2];
			Random random = new Random();
			random2[0] = random.nextInt(5);
			random2[1] = random.nextInt(5);
			while (random2[0]==random2[1]) {
				random2[0] = random.nextInt(5);
				
			}
			return random2;
	 
	 }
	/**
	 * 摇一摇随机选号
	 */
	protected void doShake() {
		int[] numInt= getint();
		if("6".equals(childId)){
			for (int i = 0; i < containers.length; i++) {
				containers[i].clear();
			}
			containers[numInt[0]].randomSelect();
			basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
			return;
		}else if("7".equals(childId)){
			for (int i = 0; i < containers.length; i++) {
				containers[i].clear();
			}
		
				containers[numInt[0]].randomSelect();
				containers[numInt[1]].randomSelect();
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
			return;
		}
		lotteryNums.clear();
		for (int i = 0; i < containers.length; i++) { 
			MGridView container = containers[i];
			if(container != null && container.getVisibility() == View.VISIBLE ){//是否设置为摇晃获取随机数&& container.isShakeble()
				lotteryNums.add(container.randomSelect());//最好在最后点确定的时候复制
			}
		}
		basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
	}
	/**
	 * 至少选 numStr 个号码
	 * @return
	 */
	private String zhishao(String numStr) {
		return "至少选"+numStr+"个号";
	}

	/**
	 * 取消容器
	 * 自我互斥
	 * @param containers1
	 * @param containers2
	 */
	private void recoverMutual(MGridView containers1, MGridView containers2){
		containers1.setSelfMutual(false);//取消互斥
		containers2.setSelfMutual(false);
}
//	/**
//	 * containers1
//	 * containers2
//	 * 去掉遗漏
//	 * @param containers1
//	 * @param containers2
//	 */
//	private void removeYiLouTV(MGridView containers1, MGridView containers2){
//		containers1.set_YiLou_Visibility(false);
//		containers2.set_YiLou_Visibility(false);
//
//	}
	
//	/**
//	 * containers1
//	 * 去掉遗漏
//	 * @param containers1
//	 */
//	private void removeYiLouTV(MGridView containers1){
//		containers1.set_YiLou_Visibility(false);
//
//	}
	/**
	 * containers1
	 * containers2
	 * 增加遗漏
	 * @param containers1
	 * @param containers2
	 */
	private void addYiLouTV(MGridView containers1, MGridView containers2){
		containers1.set_YiLou_Visibility(true);
		containers2.set_YiLou_Visibility(true);

	}
	
	
  /**
    * gone
    * 
    * 万位遗漏
    * 千位遗漏 
    * 
    */
	private void goneomintTV() {
		elevenC5_one_omit_tv.setVisibility(View.GONE);//万位遗漏
		elevenC5_two_omint_tv.setVisibility(View.GONE);//千位遗漏 
		
	}
//   /** 
//	 * 万位默认都是显示的
//	 * 
//	 * 所以要显示两个选号容器只需要 显示千位
//	 */
//	private void visibleomintTV(){
//		elevenC5_one_omit_tv.setVisibility(View.VISIBLE);//万位遗漏 
//		elevenC5_two_omint_tv.setVisibility(View.VISIBLE);//千位
//	}
   /**
	 * 万位默认都是显示的
	 * 
	 * 所以要显示三个选号容器只需要 
	 * 显示千位 和百位   : QB
	 */
	private void visibleQBRl(){
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位
		elevenC5_three_RL.setVisibility(View.VISIBLE);//百位
	}



	/**
	  * 千位 百位 十位 个位
	  * 布局全部gone
	  * 
	  * 只显示  万位布局
	  * 
	  */
	private void goneFourRl(){
		elevenC5_two_RL.setVisibility(View.GONE);//千位
		elevenC5_three_RL.setVisibility(View.GONE);//百位
		elevenC5_four_RL.setVisibility(View.GONE);//十位
		elevenC5_five_RL.setVisibility(View.GONE);//个位
		
		elevenC5_sex_RL.setVisibility(View.GONE);//个位
		elevenC5_seven_RL.setVisibility(View.GONE);//个位
	}	
	/**
	  * 千位 百位 十位 个位 四个
	  * 布局全部visible
	  * 
	  * 只显示  万位布局
	  * 
	  */
	private void visibleFourRl(){
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位
		elevenC5_three_RL.setVisibility(View.VISIBLE);//百位
		elevenC5_four_RL.setVisibility(View.VISIBLE);//十位
		elevenC5_five_RL.setVisibility(View.VISIBLE);//个位
	}
	/**
	  * 千位 百位 十位   三个
	  * 布局全部visible
	  * 
	  * 只显示  万位布局
	  * 
	  */
	private void visiblethreeRl(){
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位
		elevenC5_three_RL.setVisibility(View.VISIBLE);//百位
		elevenC5_four_RL.setVisibility(View.VISIBLE);//十位
	}
	
	
	/**
	 * 1 改变摇一摇右边的　　ＴｅｘｔＶｉｅｗ　提示
	 * 
	 * 2 万位上边的　ＴｅｘｔＶｉｅｗ　中奖奖金提示
	 * 
	 * 3 万位上的内容
	 */
	private void changetwoTextViewmsg(String shakeNotice,String wanStr,String s1,String s2){
		setShakeNotice(shakeNotice);//改变摇一摇右边的 提示
		elevenC5_one_tv.setText(wanStr);//万位上的内容
		bonusText=(Spannable) Html.fromHtml("<font color=#606060>"+s1+"</font><font color=#ff0000>"+s2+"</font>");
		elevenC5_show_bonus_tv.setText(bonusText);
	}
	/**
	 * 1 改变摇一摇右边的　　ＴｅｘｔＶｉｅｗ　提示
	 * 
	 * 2 万位上边的　ＴｅｘｔＶｉｅｗ　中奖奖金提示
	 * 
	 * 3 千位上的内容　
	 * 
	 * 4 万位上的内容
	 */
	private void changetwoTextViewmsg(String shakeNotice,String wanStr,String qianStr,String s1,String s2){
		setShakeNotice(shakeNotice);//改变摇一摇右边的 提示
		elevenC5_one_tv.setText(wanStr);//万位上的内容
		elevenC5_two_tv.setText(qianStr);//千位上的内容
		bonusText=(Spannable) Html.fromHtml("<font color=#606060>"+s1+"</font><font color=#ff0000>"+s2+"</font>");
		elevenC5_show_bonus_tv.setText(bonusText);
	}
	/**
	 * 1 改变摇一摇右边的　　ＴｅｘｔＶｉｅｗ　提示
	 * 
	 * 2 万位上边的　ＴｅｘｔＶｉｅｗ　中奖奖金提示
	 *
	 * 3 万位上的内容　
	 * 
	 * 4 千位上的内容
	 * 
	 * 5 百位上的内容
	 * 
	 */
	private void changetwoTextViewmsg(String shakeNotice,String wanStr,String qianStr,String baiStr,String s1,String s2){
		setShakeNotice(shakeNotice);//改变摇一摇右边的 提示
		elevenC5_one_tv.setText(wanStr);//万位上的内容
		elevenC5_two_tv.setText(qianStr);//千位上的内容
		elevenC5_three_tv.setText(baiStr);//百位上的内容
		bonusText=(Spannable) Html.fromHtml("<font color=#606060>"+s1+"</font><font color=#ff0000>"+s2+"</font>");
		elevenC5_show_bonus_tv.setText(bonusText);
	}
	/**
	 * 1 改变摇一摇右边的　　ＴｅｘｔＶｉｅｗ　提示
	 * 
	 * 2 万位上边的　ＴｅｘｔＶｉｅｗ　中奖奖金提示
	 *
	 * 3 万位上的内容　
	 * 
	 * 4 千位上的内容
	 * 
	 * 5 百位上的内容
	 * 
	 * 6 十位上的内容
	 */
	private void changetwoTextViewmsg(String shakeNotice,String wanStr,String qianStr,String baiStr,String shiStr,String s1,String s2){
		setShakeNotice(shakeNotice);//改变摇一摇右边的 提示
		elevenC5_one_tv.setText(wanStr);//万位上的内容
		elevenC5_two_tv.setText(qianStr);//千位上的内容
		elevenC5_three_tv.setText(baiStr);//百位上的内容
		elevenC5_four_tv.setText(shiStr);//十位上的内容
		bonusText=(Spannable) Html.fromHtml("<font color=#606060>"+s1+"</font><font color=#ff0000>"+s2+"</font>");
		elevenC5_show_bonus_tv.setText(bonusText);
	}
	/**
	 * 1 改变摇一摇右边的　　ＴｅｘｔＶｉｅｗ　提示
	 * 
	 * 2 万位上边的　ＴｅｘｔＶｉｅｗ　中奖奖金提示
	 *
	 * 3 万位上的内容　
	 * 
	 * 4 千位上的内容
	 * 
	 * 5 百位上的内容
	 * 
	 * 6 十位上的内容
	 * 
	 * 7 个位上的内容
	 */
	private void changetwoTextViewmsg(String shakeNotice,
			String wanStr,String qianStr,String baiStr,
			String shiStr,String geStr,
			String s1,String s2){
		setShakeNotice(shakeNotice);//改变摇一摇右边的 提示
		elevenC5_one_tv.setText(wanStr);//万位上的内容
		elevenC5_two_tv.setText(qianStr);//千位上的内容
		elevenC5_three_tv.setText(baiStr);//百位上的内容
		elevenC5_four_tv.setText(shiStr);//十位上的内容
		elevenC5_five_tv.setText(geStr);//个位上的内容
		bonusText=(Spannable) Html.fromHtml("<font color=#606060>"+s1+"</font><font color=#ff0000>"+s2+"</font>");
		elevenC5_show_bonus_tv.setText(bonusText);
	}

	private void initSaleType_1() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_1));
		anyOneOrTwoWay(0);
		
		goneFourRl();
		recoverMutual(containers[0], containers[1]);//取消自我互斥
//		elevenC5_one_omit_tv.setVisibility(View.VISIBLE);//遗漏  版本 一无遗漏
		
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text1)+1+getString(R.string.ssc_shakenotice_text2),
				getString(R.string.arrange_the_unit_tv), "猜中开奖号码最后1位,奖金", "11元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setSelfMutual(false);
		containers[0].setMiniNum(1);//至少
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
	}


	private void initSaleType_2() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_2));
		setShakeNotice(eachText);
		
		cancelMutualContainers();//取消容器互斥
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		
		elevenC5_two_RL.setVisibility(View.VISIBLE);//显示 千位 重用为个位
		elevenC5_three_RL.setVisibility(View.GONE);//百位
		elevenC5_four_RL.setVisibility(View.GONE);//十位
		elevenC5_five_RL.setVisibility(View.GONE);//个位
		
//		addYiLouTV(containers[0], containers[1]);//有遗漏
//		visibleomintTV();
		
		
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_each_text)+1+getString(R.string.ssc_shakenotice_text2),
			          "十位","个位" ,"按位猜中开奖号码后2位,奖金", "116元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
		containers[0].setSelfMutual(false);
		containers[1].setSelfMutual(false);
		containers[0].setMiniNum(1);//至少
		containers[1].setMiniNum(1);
		
		containers[0].setMaxNum(0);//最多
		containers[1].setMaxNum(0);
	}
	
	private void initSaleType_52() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_3));
		anyOneOrTwoWay(0);
		goneFourRl();
//		removeYiLouTV(containers[0]);//去遗漏
		
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		elevenC5_one_omit_tv.setVisibility(View.GONE);//遗漏提示
		
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text1)+2+getString(R.string.ssc_shakenotice_text2),
				"选号", "猜中开奖号码最后2位,顺序不限,奖金", "58元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setSelfMutual(false);
		containers[0].setMiniNum(2);//至少
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
	}
	
	private void initSaleType_3() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setShakeNotice(eachText);
		setTitleText(getString(R.string.ssc_title_4));
		anyOneOrTwoWay(0);
		
		cancelMutualContainers();//取消容器互斥
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		visibleQBRl();//  显示 布局
		
//		addYiLouTV(containers[0], containers[1]);版本 一无遗漏
//		elevenC5_one_omit_tv.setVisibility(View.VISIBLE);//万遗漏
//		containers[2].set_YiLou_Visibility(true);//添加遗漏
		
		
		changetwoTextViewmsg(getString(R.string.arrange_rule_tv),
				"百位","十位","个位", "猜中开奖号码最后3位,奖金", "1160元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2);
		containers[0].setSelfMutual(false);
		containers[1].setSelfMutual(false);
		containers[0].setMiniNum(1);//至少
		containers[1].setMiniNum(1);
		containers[2].setMiniNum(1);
		
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
		containers[2].setMaxNum(0);
	}
	
	private void initSaleType_23() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_5));
		setShakeNotice(getString(R.string.ssc_shakenotice_text3));//至少选择一个重码和一个单码
		
		cancelMutualContainers();//取消容器互斥
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		goneFourRl();//先gone 所有  后 Visible
		elevenC5_two_RL.setVisibility(View.VISIBLE);//千位
		
		goneomintTV();//千位 和万位上的遗漏
		
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text3),
				"重号","单号", "猜中开奖号码最后3位,顺序不限,奖金", "385元");
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
		containers[0].setSelfMutual(true);
		containers[1].setSelfMutual(true);
		containers[0].setMiniNum(1);//最少
		containers[1].setMiniNum(1);
		
		containers[0].setMaxNum(0);
		containers[1].setMaxNum(0);
		
		
	}
	
	private void initSaleType_33() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_6));
		
//		removeYiLouTV(containers[0]);//遗漏
		
		goneFourRl();//先gone
		setShakeNotice("至少选1个号");//zhishao("2"));
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text1)+2+getString(R.string.ssc_shakenotice_text2),
				"选号", "猜中开奖后3位,顺序不限,奖金", "385元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setSelfMutual(false);
		containers[0].setMiniNum(3);//最少
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
	}
	
	private void initSaleType_43() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_7));
		setShakeNotice("至少选1个号");//zhishao("3"));
		
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		goneFourRl();//先gone
		
		goneomintTV();//先gone 遗漏
//		elevenC5_one_omit_tv.setVisibility(View.VISIBLE);//万遗漏
//		removeYiLouTV(containers[0]);//去掉遗漏
		
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text1)+"3"+getString(R.string.ssc_shakenotice_text2),
				"选号", "猜中开奖后3位,顺序不限,奖金", "190元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		containers[0].setSelfMutual(false);
		containers[0].setMiniNum(3);//最少
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		
	}

	private void initSaleType_4() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_8));
		setShakeNotice(eachText);
		
		cancelMutualContainers();//取消容器互斥
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		visiblethreeRl();//显示 千位 百位 十位三个
		elevenC5_five_RL.setVisibility(View.GONE);
		
//		visibleomintTV();//万位 千位  遗漏
//		containers[2].set_YiLou_Visibility(true);
//		containers[3].set_YiLou_Visibility(true);
		
		changetwoTextViewmsg(getString(R.string.arrange_rule_tv),
				"千位", "百位","十位","个位","猜中开奖后4位,奖金", "10000元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2,3);
		containers[0].setSelfMutual(false);//自我互斥
		containers[1].setSelfMutual(false);
		containers[2].setSelfMutual(false);
		containers[3].setSelfMutual(false);
		
		containers[0].setMiniNum(1);//最少
		containers[1].setMiniNum(1);
		containers[2].setMiniNum(1);
		containers[3].setMiniNum(1);
		
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
		containers[2].setMaxNum(0);
		containers[3].setMaxNum(0);
	}
	
	private void initSaleType_5() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title_9));
		setShakeNotice(eachText);
		
		cancelMutualContainers();//取消容器互斥
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		
		visibleFourRl();
//		visibleomintTV();//万位 千位  遗漏
		changetwoTextViewmsg(getString(R.string.arrange_rule_tv),
				"万位","千位", "百位","十位","个位","按位猜中全部5个号码,奖金", "116000元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2,3,4);
		
		
		allsetMinAndMax(1,0);
	}
	private void initSaleType_35() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title1_0));
		setShakeNotice(eachText);
		
		visibleFourRl();
//		visibleomintTV();//万位 千位  遗漏
		
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		changetwoTextViewmsg(getString(R.string.arrange_rule_tv),
				"万位","千位", "百位","十位","个位","按位猜中全部5个号码,最高奖金", "20460元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2,3,4);
		
		allsetMinAndMax(1,0);
			
	}
	private void initSaleType_22() {
		anyOneOrTwoWay(0);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title1_1));
		setShakeNotice(eachText);

		elevenC5_sex_RL.setVisibility(View.VISIBLE);//大小单双专用
		elevenC5_seven_RL.setVisibility(View.VISIBLE); 
//		removeYiLouTV(containers[5],containers[6]);
		
		changetwoTextViewmsg(getString(R.string.arrange_rule_tv),
				"十位","个位","猜中开奖最后2位的大小单双,奖金", "4元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(5,6);
		
		containers[5].setMaxNum(1);
		containers[6].setMaxNum(1);
		containers[5].setMiniNum(1);//最少
		containers[6].setMiniNum(1);
		containers[5].setSelfMutual(true);//自我互斥
		containers[6].setSelfMutual(true);
		
		
	}

	private void initSaleType_6() {
		
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		setTitleText(getString(R.string.ssc_title1_2));
		setShakeNotice(getString(R.string.any_one_choose_1text));
		
		recoverMutual(containers[0], containers[1]);
		visibleFourRl();
		
//		visibleomintTV();//万位 千位  遗漏
//		allAddYiLou(true);
		changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text5),
				"万位","千位", "百位","十位","个位","猜中开奖号码的对应1位,奖金", "11元");
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2,3,4);
		
		anyOneOrTwoWay(1);
		allsetMinAndMax(1,0);	
		
		}





	private void initSaleType_7() {
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		
		setTitleText(getString(R.string.ssc_title1_3));
		setShakeNotice(getString(R.string.any_two_choose_1text));
		visibleFourRl();
		
//		visibleomintTV();//万位 千位  遗漏
//		allAddYiLou(true);
		
		recoverMutual(containers[0], containers[1]);//取消自我互斥
		
	    changetwoTextViewmsg(getString(R.string.ssc_shakenotice_text6),
				"万位","千位", "百位","十位","个位","猜中开奖号码的对应2位,奖金", "116元");
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2,3,4);
		
		anyOneOrTwoWay(2);
		
		allsetMinAndMax(1,0);	
		
		
			
		}
	
	
	  /**
	   * 任一 任二  特有的处理方法
	   * @param i
	   */
	private void anyOneOrTwoWay(int i) {
		containers[0].setMutualContainer_select_num(i);
		containers[1].setMutualContainer_select_num(i);
		containers[2].setMutualContainer_select_num(i);
		containers[3].setMutualContainer_select_num(i);
		containers[4].setMutualContainer_select_num(i);
	}
	
//	private void allAddYiLou(boolean tag) {
//		containers[0].set_YiLou_Visibility(tag);
//		containers[1].set_YiLou_Visibility(tag);
//		containers[2].set_YiLou_Visibility(tag);
//		containers[3].set_YiLou_Visibility(tag);
//		containers[4].set_YiLou_Visibility(tag);
//	}
	 /**
	  * MGridView
	  * 设置最小值  和最大值  任一和任二  需要给全部容器
	  * @param min
	  * @param max
	  */
	private void allsetMinAndMax(int min,int max) {
		
		containers[0].setMiniNum(min);//最少
		containers[1].setMiniNum(min);
		containers[2].setMiniNum(min);
		containers[3].setMiniNum(min);
		containers[4].setMiniNum(min);
		
		
		containers[0].setMaxNum(max);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(max);
		containers[2].setMaxNum(max);
		containers[3].setMaxNum(max);
		containers[4].setMaxNum(max);
		
		containers[0].setSelfMutual(false);
		containers[1].setSelfMutual(false);
		containers[2].setSelfMutual(false);
		containers[3].setSelfMutual(false);
		containers[4].setSelfMutual(false);
	}


}
