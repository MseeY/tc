package com.mitenotc.ui.play;


import android.graphics.Color;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import b.m;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MyToast;


/**
 * 106	大乐透
 *  
 * @author wanli
 *lotteryId = "118"
 *
 * 默认普通投注 ：salesType = "1"
 */

@BaseBuyAnnotation(lotteryId = "106",salesType = "0")
public class PL106 extends BaseBuyFragment{
	private RelativeLayout red_towed_relayout;//托码区
	private TextView red_mop_mgv_tv;//托码提示
	private TextView red_Bilecode_mgv_tv;//胆码提示
	private TextView towed_warm_prompt_tv;//蓝码提示
	private View view_container1_wire1;//线
	


	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 * 
	 * 0 胆码
	 * 1 托码
	 * 2 蓝码
	 */
	
	@Override
	protected void initContainers() {
		
		containers[0].setMiniNum(5);
		containers[1].setMiniNum(5);
		containers[0].simpleInit(1, MGridView.RED_MODE, 1, 35);
		containers[1].simpleInit(1, MGridView.RED_MODE, 1, 35);
		containers[2].simpleInit(1, MGridView.BLUE_MODE, 1, 12);
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
		return View.inflate(mActivity, R.layout.f_ssp_pop_titile_rb, null);
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
     View v=View.inflate(mActivity, R.layout.play_ssp, null);
      
		containers[0]=(MGridView)v.findViewById(R.id.ssq_red_number_container);
		containers[1]=(MGridView)v.findViewById(R.id.red_mop_mgv);
		containers[2]=(MGridView)v.findViewById(R.id.ssq_blue_number_container);
		
		 red_towed_relayout=(RelativeLayout) v.findViewById(R.id.red_towed_relayout);
	     red_mop_mgv_tv=(TextView) v.findViewById(R.id.red_mop_mgv_tv);//红球 胆码
	     red_Bilecode_mgv_tv=(TextView) v.findViewById(R.id.red_Bilecode_mgv_tv);//托码
	     towed_warm_prompt_tv=(TextView) v.findViewById(R.id.towed_warm_prompt_tv);//蓝码
	     
	     view_container1_wire1=v.findViewById(R.id.view_container1_wire1);
     
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
    	if(getMyBundle()!=null && getMyBundle().get("position") != null){
    		if(MyApp.order.isIs_dlt_pursue_mode_enabled()){
    			childId="1"; //chailid="1" 300;
    		}else{
    			childId="0";//chailid="0" 200;
    		}
    	}
		tag=tag.replace(",", "");
    	switch (Integer.parseInt(tag)) {
    	//普通
		 case 0:
			   initSaleType_0();
			   
			   break;
	    //胆拖
	        case 2:
	        	
	           initSaleType_2();
			
			break;	
		}
    	fast3_title_Layout.setVisibility(View.GONE);//BaseBuyFragment  中顶部的时间提示 只有高频彩才涉及时间提示
    }
    
    //把相应的saleType 
    private void initSaleType_0(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
    	changeShakeVisibility(true);
    	setTitleText(getString(R.string.dlt_pt_title));
    	goneAllTextView();
    	
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,2);
		cancelMutualContainers();//取消容器互斥
		
		containers[0].setMiniNum(5);
		containers[2].setMiniNum(2);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[2].setMaxNum(0);
    }

 

	private void initSaleType_2(){
		
		setTitleText(getString(R.string.dlt_dt_title));//大乐透 -胆拖投注
		
		visibleAllTextView();
		cancelMutualContainers();//取消容器互斥
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2);
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(4);
		containers[2].setMiniNum(2);
		
		containers[0].setMaxNum(4);
		containers[1].setMaxNum(0);
		containers[2].setMaxNum(0);
		
		//隐藏摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
    }
    /**
     * 隐藏三个TextView
     */
	   private void goneAllTextView() {
		    setShakeNotice(getString(R.string.da_le_tou_rule_text));//至少选择五个红球两个篮球
		    red_towed_relayout.setVisibility(View.GONE);//托码
			red_mop_mgv_tv.setVisibility(View.GONE);
			red_Bilecode_mgv_tv.setVisibility(View.GONE);
			view_container1_wire1.setVisibility(View.GONE);
			towed_warm_prompt_tv.setVisibility(View.GONE);
			
		}
    /**
     * 显示三个TextView
     */
	   private void visibleAllTextView() {
		    red_towed_relayout.setVisibility(View.VISIBLE);
			red_mop_mgv_tv.setVisibility(View.VISIBLE);
			red_Bilecode_mgv_tv.setVisibility(View.VISIBLE);
			view_container1_wire1.setVisibility(View.VISIBLE);
			towed_warm_prompt_tv.setVisibility(View.VISIBLE);
			
			red_mop_mgv_tv.setText(getString(R.string.da_le_tou_redmop_text));//拖码区-后区,至少选择1个
			red_Bilecode_mgv_tv.setText(getString(R.string.da_le_tou_red_text));//胆码区-前区,至少选择1个,最多选择4个
			towed_warm_prompt_tv.setText(getString(R.string.da_le_tou_text));//蓝码区,至少选择2个
			view_container1_wire1.setVisibility(View.VISIBLE);
				
	   }
	   /**
	     * 
	     * 大乐透  为了限制非理性投注  重写了ok();方法
	     */
    @Override
    protected void ok() {
    	if(ticket.getLotteryCount() >=10000){
			MyToast.showToast(mActivity, "单票选号金额不能超过20000元！");
			return;
       }
    	super.ok();
    }
      
	
}
