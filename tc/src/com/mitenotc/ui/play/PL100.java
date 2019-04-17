package com.mitenotc.ui.play;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.tc.R;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;

/**
 * 100	排列三
 * 
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "100",salesType = "0")
public class PL100 extends BaseBuyFragment{
	private View mianView;//玩法主布局
	private RelativeLayout arrange_num_RL;//百位布局
	private RelativeLayout arrange_ten_unit_RL;//十位布局
	private RelativeLayout arrange_the_unit_RL;//个位布局
	private TextView arrange_num_tv;//百位TextView
	private TextView arrange_ten_unit_tv;//十位TextView
	private TextView arrange_the_unit_tv;//个位TextView
	private TextView arrange_top_playmethod_tv;//玩法提示
	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 * 
	 * 0 百位  {[组三单式（重号）]  ，[组三复式（无提示）,   组六（无提示）]}：                重用三次
	 * 1 十位  {[组三单式  （单号）] ：                                                                                 重用 一次
	 * 2 个位
	 */
	
	@Override
	protected void initContainers() {
		containers[0].setMiniNum(1);//百位至少选择1个
		containers[1].setMiniNum(1);//十位至少选择1个
		containers[2].setMiniNum(1);//个位至少选择1个
		
		containers[0].simpleInit(1, MGridView.RED_MODE, 0,9,false);//百 0-9
		containers[1].simpleInit(1, MGridView.RED_MODE, 0,9,false);//十 0-9
		containers[2].simpleInit(1, MGridView.RED_MODE, 0,9,false);//个 0-9
		
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
		
		return	mianView=View.inflate(mActivity, R.layout.f_arrange3_pop_titile_rb, null);
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
		View  v=View.inflate(mActivity, R.layout.play_arrange3, null);
		arrange_num_RL=(RelativeLayout) v.findViewById(R.id.arrange_num_RL);//百位布局
		arrange_ten_unit_RL=(RelativeLayout) v.findViewById(R.id.arrange_ten_unit_RL);//十位布局
		arrange_the_unit_RL=(RelativeLayout) v.findViewById(R.id.arrange_the_unit_RL);//个位布局
		
		
        containers[0]=(MGridView) v.findViewById(R.id.arrange_num_mgv);//百
		containers[1]=(MGridView) v.findViewById(R.id.arrange_ten_mgv);//十
		containers[2]=(MGridView) v.findViewById(R.id.arrange_the_unit_mgv);//个
		
		arrange_num_tv=(TextView) v.findViewById(R.id.arrange_num_tv);//百位
		arrange_ten_unit_tv=(TextView) v.findViewById(R.id.arrange_ten_unit_tv);//十位
		arrange_the_unit_tv=(TextView) v.findViewById(R.id.arrange_the_unit_tv);//个位
		arrange_top_playmethod_tv=(TextView) v.findViewById(R.id.arrange_top_playmethod_tv);//玩法提示
		
		return v;
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
		switch (Integer.parseInt(tag)) {
//	直选
			case 0:
				initSaleType_0();
				
				break;
//	组三单式
			case 3:
				initSaleType_3();
				
				break;
//	组三复式
			case 5:
				initSaleType_5();
				
				break;
//	组六
			case 4:
				initSaleType_4();
				
				break;
			
			default:
				break;
		}
		fast3_title_Layout.setVisibility(View.GONE);//BaseBuyFragment  中顶部的时间提示 只有高频彩才涉及时间提示
	}
	//取消容器互斥（自身）
   private void setSelffalse(){
		containers[0].setSelfMutual(false);
        containers[1].setSelfMutual(false);
	}
	 /**
	  * 直选
	  */
    private void initSaleType_0(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关（所有玩法都涉及摇一摇）
    	changeShakeVisibility(true);
    	setTitleText(getString(R.string.arrange3_rbtn0_text_pls));
    	setSelffalse();
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
    	 reloadLotteryNums(0,1,2);
    	 containers[0].setMiniNum(1);
    	 containers[1].setMiniNum(1);
    	 containers[2].setMiniNum(1);
    	 
    	 containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
    	 containers[1].setMaxNum(0);
    	 containers[2].setMaxNum(0);
    	 cancelMutualContainers();//取消容器互斥
    	 
    	 showAllRl();
 		
 		initSaleType_0_tv();
    }


	/**
	   * 组三单式
	   */
    private void initSaleType_3(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关（所有玩法都涉及摇一摇）
 		changeShakeVisibility(true);
 		setTitleText(getString(R.string.arrange3_rbtn1_text));
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
   	   reloadLotteryNums(0,1);
   	   cancelMutualContainers();//取消容器互斥
       mutualContainers(0,1);//重码 和 单码互斥
       
   	   containers[0].setMiniNum(1);//重码最大1
   	   containers[0].setSelfMutual(true);
       containers[1].setSelfMutual(true);
   	   containers[1].setMiniNum(1);//单码最大1
   	   
   	   showNumandTenRl();
   	   initSaleType_1_tv();
    }

	/**
	   * 组三复式
	   */
    private void initSaleType_5(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关（所有玩法都涉及摇一摇）
    	changeShakeVisibility(true);
    	setTitleText(getString(R.string.arrange3_rbtn2_text));
    	
    	setSelffalse();
    	cancelMutualContainers();//取消容器互斥
    	showNumRl();
    	initSaleType_2_tv();
 		
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
	   reloadLotteryNums(0);
	   
	   containers[0].setMiniNum(2);
	   containers[0].setMaxNum(0);//重码最大0 如果最大数设为0,代表不设置最大数
	  
    	
    }
  
     /**
	   * 组六
	   */
    private void initSaleType_4(){
    	setSelffalse();
    	setTitleText(getString(R.string.arrange3_rbtn3_text));
    	cancelMutualContainers();//取消容器互斥
    	showNumRl();
    	initSaleType_3_tv();
    	
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
 	    reloadLotteryNums(0);
 	   
    	
    	containers[0].setMiniNum(4);
    	containers[0].setMaxNum(0);//重码最大0 如果最大数设为0,代表不设置最大数
    }
    
    
    /**
     * VISIBLE 
     * 百位布局 和 十位布局
     * arrange_num_tv :重号
     * arrange_ten_unit_tv ：单号
     */
    private void showNumandTenRl() {
    	arrange_num_RL.setVisibility(View.VISIBLE);
    	arrange_ten_unit_RL.setVisibility(View.VISIBLE);
    	arrange_the_unit_RL.setVisibility(View.GONE);
    	
    	arrange_num_tv.setText(getString(R.string.group_choose_three_repeat_num));
    	arrange_ten_unit_tv.setText(getString(R.string.group_choose_three_single_num));
    	
	}
    /**
     * VISIBLE 
     * 百位布局 
     * GONE
     * arrange_num_tv 百位布局提示 
     * 
     */
	private void showNumRl() {
		arrange_num_RL.setVisibility(View.VISIBLE);
    	arrange_ten_unit_RL.setVisibility(View.GONE);
    	arrange_the_unit_RL.setVisibility(View.GONE);
    	arrange_num_tv.setVisibility(View.GONE);
	}
	
    /**
     * VISIBLE ALL
     * 
     */
    private void showAllRl() {
    	arrange_num_RL.setVisibility(View.VISIBLE);
    	arrange_ten_unit_RL.setVisibility(View.VISIBLE);
    	arrange_the_unit_RL.setVisibility(View.VISIBLE);
	}
    
    /**
     * 直选
     * 
     * 玩法提示
     * 规则提示
     */
	private void initSaleType_0_tv() {
		arrange_top_playmethod_tv.setText(getString(R.string.arrange_rule_tv));//每位至少选择一个号码
		arrange_num_tv.setVisibility(View.VISIBLE);
		arrange_num_tv.setText(getString(R.string.arrange_num_tv));//百位
		arrange_ten_unit_tv.setText(getString(R.string.arrange_ten_unit_tv));//十位
		arrange_the_unit_tv.setText(getString(R.string.arrange_the_unit_tv));//个位
		
		
	}
	
	/**
     * 组三单式
     * 
     * 玩法提示
     * 规则提示
     */
	private void initSaleType_1_tv() {
		arrange_top_playmethod_tv.setText(getString(R.string.group_choose_three_playmethod_tv));//至少选择1个重号和1个单号
		arrange_num_tv.setVisibility(View.VISIBLE);//百位
	}
	/**
     * 组三复式
     * 
     * 玩法提示
     * 规则提示
     */
	private void initSaleType_2_tv() {
		arrange_top_playmethod_tv.setText(getString(R.string.group_choose_three_playmethod_tv2));//至少选2个号
	}
	/**
     * 组六
     * 
     * 玩法提示
     * 规则提示
     */
	private void initSaleType_3_tv() {
		arrange_top_playmethod_tv.setText(getString(R.string.group_choose_playmethod_tv));//至少选4个号
	}
   
}
