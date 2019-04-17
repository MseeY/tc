package com.mitenotc.ui.play;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.tc.R;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MyToast;
/**
 * 117	七乐彩
 * 
 * 套用了双色球的布局
 * 篮球区 是gone
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "117",salesType = "0")
public class PL117 extends BaseBuyFragment {
	private RelativeLayout red_towed_relayout;//拖码区布局
    private TextView red_mop_mgv_tv;//托码提示
	private TextView red_Bilecode_mgv_tv;//胆码提示
	private TextView towed_warm_prompt_tv;//蓝码区提示
	private View view_container1_wire1;//线
	private View   view_wire;
	
	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 *
	 */
	
	@Override
	protected void initContainers() {
		containers[0].setMiniNum(7);
		containers[1].setMiniNum(6);
		containers[0].simpleInit(1, MGridView.RED_MODE, 1, 30);
		containers[1].simpleInit(1, MGridView.RED_MODE, 1, 30);
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
		return 	View.inflate(mActivity, R.layout.f_ssp_pop_titile_rb, null);
		
		
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
     view_wire = v.findViewById(R.id.view_wire);
     red_towed_relayout=(RelativeLayout) v.findViewById(R.id.red_towed_relayout);
	 containers[0]=(MGridView)v.findViewById(R.id.ssq_red_number_container);
	 containers[1]=(MGridView)v.findViewById(R.id.red_mop_mgv);
	 containers[2]=(MGridView)v.findViewById(R.id.ssq_blue_number_container);
     red_mop_mgv_tv=(TextView) v.findViewById(R.id.red_mop_mgv_tv);
     red_Bilecode_mgv_tv=(TextView) v.findViewById(R.id.red_Bilecode_mgv_tv);
     
     //因为重用的原因 没有优化好 所以有的部分的分别gone  -- TODO 
     containers[2].setVisibility(View.GONE);//蓝码区
     towed_warm_prompt_tv=(TextView) v.findViewById(R.id.towed_warm_prompt_tv);//提示
     view_container1_wire1=v.findViewById(R.id.view_container1_wire1);//线
     towed_warm_prompt_tv.setVisibility(View.GONE);
     view_container1_wire1.setVisibility(View.GONE);
     view_wire.setVisibility(View.GONE);
 	
     
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
    	//普通
	      case 0:
			   initSaleType_0();
			   
			   break;
	    //胆拖
	        case 2:
	         initSaleType_2();
			
			break;	
		
		default:
			break;
		}
    	fast3_title_Layout.setVisibility(View.GONE);//BaseBuyFragment  中顶部的时间提示 只有高频彩才涉及时间提示
    }
    
    //普通
    private void initSaleType_0(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
    	changeShakeVisibility(true);
    	view_wire.setVisibility(View.GONE);//线
    	
    	setTitleText(getString(R.string.qlc_pt_title));
    	
    	towTextViewGONE();//gone两个提示
    	
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0);
		cancelMutualContainers();//取消容器互斥
		
		containers[0].setMiniNum(7);
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
    }


   //胆拖
	private void initSaleType_2(){
		cancelMutualContainers();//取消容器互斥(先取消后增加  保守做法)
		//隐藏摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
		view_wire.setVisibility(View.VISIBLE);//线
		view_container1_wire1.setVisibility(View.VISIBLE);//线
		
		
		setTitleText(getString(R.string.qlc_dt_title));
		towTextViewVISIBLE();//显示两个提示
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1);
		mutualContainers(0,1);//0 1 (胆码区和托码区)容器互斥
		
		containers[0].setMaxNum(6);//重新设置容器大小
		containers[0].setMiniNum(1);//重新设置容器大小
		//从1-31中选择m（0<m<=6）个数字作为胆码，选择n（n+m>=8且n>=2）个不重复且不包含胆码的数字作为拖码  
		// 2014 -6-5  测试拖码超过20 后 
		containers[1].setMaxNum(20);
		containers[1].setVisibility(View.VISIBLE);
		
    }
	
	/**
	 * 隐藏两个提示
	 */
    private void towTextViewGONE() {
    	red_towed_relayout.setVisibility(View.GONE);
    	setShakeNotice(getString(R.string.qi_le_show_rule_text));//玩法简介
        red_mop_mgv_tv.setVisibility(View.GONE);//胆区提示
        red_Bilecode_mgv_tv.setVisibility(View.GONE);//拖区提示
	}
    /**
	 * 显示两个提示
	 */
    private void towTextViewVISIBLE() {
    	red_towed_relayout.setVisibility(View.VISIBLE);
        red_mop_mgv_tv.setVisibility(View.VISIBLE);//胆区提示
        red_Bilecode_mgv_tv.setVisibility(View.VISIBLE);//拖区提示
        
        red_Bilecode_mgv_tv.setText(getString(R.string.qi_le_red_text));//红球-胆码区,至少选择1个,最多选择6个
        red_mop_mgv_tv.setText(getString(R.string.qi_le_red_mop_text));//红球-拖码区,至少选择2个
        
    	containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
		containers[1].setMaxNum(0);
	}
    


    /**
     * 
     * 七乐彩 为了限制非理性投注  重写了ok();方法
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
