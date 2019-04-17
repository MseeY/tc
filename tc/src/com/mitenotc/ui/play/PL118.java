package com.mitenotc.ui.play;


import android.graphics.Color;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MyToast;


/**
 * 118	双色球
 *  
 * @author wanli
 *lotteryId = "118" 05 06 20 21 26 30  + 11
 *
 * 默认普通投注 ：salesType = "1"
 */

@BaseBuyAnnotation(lotteryId = "118",salesType = "0")
public class PL118 extends BaseBuyFragment{
	private RelativeLayout red_towed_relayout;//拖码区布局
	private TextView red_mop_mgv_tv;//托码提示
	private TextView red_Bilecode_mgv_tv;//胆码提示
	private TextView towed_warm_prompt_tv;//蓝码区提示
	private View view_container1_wire1;//线
	private View   view1;
	
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
		containers[0].setMiniNum(6);
		containers[0].simpleInit(1, MGridView.RED_MODE, 1, 33);
		containers[1].simpleInit(1, MGridView.RED_MODE, 1, 33);
		containers[2].simpleInit(1, MGridView.BLUE_MODE, 1, 16);
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
//		2014-6-25 暂时取消掉胆拖投注
		if(view1==null){
			view1=View.inflate(mActivity, R.layout.f_ssp_pop_titile_rb, null);
		}
		return view1;
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
     red_towed_relayout=(RelativeLayout)v.findViewById(R.id.red_towed_relayout);//拖码区布局默认是gone
     containers[0]=(MGridView)v.findViewById(R.id.ssq_red_number_container);
	 containers[1]=(MGridView)v.findViewById(R.id.red_mop_mgv);
	 containers[2]=(MGridView)v.findViewById(R.id.ssq_blue_number_container);
	
     red_mop_mgv_tv=(TextView) v.findViewById(R.id.red_mop_mgv_tv);
     red_Bilecode_mgv_tv=(TextView) v.findViewById(R.id.red_Bilecode_mgv_tv);
     red_Bilecode_mgv_tv.setText(getString(R.string.zwl_dballcommon_toast_2_2));
     towed_warm_prompt_tv=(TextView) v.findViewById(R.id.towed_warm_prompt_tv);
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
    	tag=tag.replace(",", "");
    	switch (Integer.parseInt(tag)) {
    	//ssp普通
		   case 0:
		      initSaleType_0();
		     
			break;
		 //ssp胆拖
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
    	setTitleText(getString(R.string.bet_Ssp_towed_tv_3));//普通投注
    	
    	cancelMutualContainers();//取消容器互斥
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,2);
		
		
		red_towed_relayout.setVisibility(View.GONE);
		red_Bilecode_mgv_tv.setVisibility(View.VISIBLE);//胆码区提示
		red_Bilecode_mgv_tv.setText(getString(R.string.zwl_dballcommon_toast_2_2));
		towed_warm_prompt_tv.setVisibility(View.VISIBLE);//蓝码区提示
		view_container1_wire1.setVisibility(View.GONE);
		
		containers[0].setMiniNum(6);//最少
		containers[2].setMiniNum(1);//最少
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[2].setMaxNum(0);
    }

    private void initSaleType_2(){
    	//隐藏摇一摇条目,同时改变 摇一摇传感器的 开和关
    	changeShakeVisibility(false);
    	setTitleText(getString(R.string.bet_Ssp_towed_tv_4));//胆拖投注
    	
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2);
		cancelMutualContainers();//取消容器互斥
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		red_towed_relayout.setVisibility(View.VISIBLE);
		red_Bilecode_mgv_tv.setText(getString(R.string.zwl_dballcommon_toast_1_9));
		red_mop_mgv_tv.setVisibility(View.VISIBLE);
		red_Bilecode_mgv_tv.setVisibility(View.VISIBLE);
		view_container1_wire1.setVisibility(View.VISIBLE);
		containers[0].setMiniNum(1);
		containers[1].setMiniNum(2);
		containers[2].setMiniNum(1);
		
		containers[0].setMaxNum(5);
		containers[1].setMaxNum(24); // 后台服务器 拖码要求限制在顶多24 个以内   2014-6-5 
	    containers[2].setMaxNum(0);
    }
    
    /**
     * 
     * 双色球玩法  为了限制非理性投注  重写了ok();方法
     */
    @Override
    protected void ok() {
       if(ticket.getLotteryCount() >=10000){
    	   
			MyToast.showToast(mActivity, "单票选号金额不能超过20000元！");
			return;
       }else if("2".equals(salesType) && (containers[2].getSelectedBalls().size() >= 1  && 
    		   (containers[0].getSelectedBalls().size()+containers[1].getSelectedBalls().size()) > 24 )){
    	    MyToast.showToast(mActivity, "当篮球个数大于1时，红球总数不允许大于24个！");
    	    return;
       }else if("2".equals(salesType) && containers[2].getSelectedBalls().size() > 8  && 
    		   (containers[0].getSelectedBalls().size()+containers[1].getSelectedBalls().size()) > 16 ){
    	    MyToast.showToast(mActivity, "当篮球个数大于8时，红球总数不允许大于16个！");
    	    return;
       }
    	super.ok();
    }



	
}
