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
 * 102	排列五
 * 
 * 重用排列三的布局
 * 
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "102",salesType = "0")
public class PL102 extends BaseBuyFragment {
	private RelativeLayout arrange_num_RL;//万位布局
	private RelativeLayout arrange_ten_unit_RL;//千位布局
	private RelativeLayout arrange_the_unit_RL;//百位布局
	private RelativeLayout four_RL;//十位布局
	private RelativeLayout five_RL;//个位布局
	private TextView arrange_num_tv;//万位TextView
	private TextView arrange_ten_unit_tv;//千位TextView
	private TextView arrange_the_unit_tv;//百位TextVie
	private TextView arrange_four_tv;//十位TextView
	private TextView arrange_five_tv;//个位
	
	private TextView arrange_top_playmethod_tv;//玩法提示
	/**
	 * 重写
	 * 
	 * init 初始化选号容器
	 */


	
	@Override
	protected void initContainers() {

		containers[0].setMiniNum(1);//万位至少选择1个
		containers[1].setMiniNum(1);//千位至少选择1个
		containers[2].setMiniNum(1);//百位至少选择1个
		containers[3].setMiniNum(1);//十位至少选择1个
		containers[4].setMiniNum(1);//个位至少选择1个
//		不需要对数字格式化
		containers[0].setNumFormat(false);
		containers[1].setNumFormat(false);
		containers[2].setNumFormat(false);
		containers[3].setNumFormat(false);
		containers[4].setNumFormat(false);
		
		containers[0].simpleInit(1, MGridView.RED_MODE, 0,9,false);//万 0-9
		containers[1].simpleInit(1, MGridView.RED_MODE, 0,9,false);//千 0-9
		containers[2].simpleInit(1, MGridView.RED_MODE, 0,9,false);//百 0-9
		containers[3].simpleInit(1, MGridView.RED_MODE, 0,9,false);//十 0-9
		containers[4].simpleInit(1, MGridView.RED_MODE, 0,9,false);//个 0-9
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
		return null;
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
		
		arrange_num_RL=(RelativeLayout) v.findViewById(R.id.arrange_num_RL);//万位布局
		arrange_ten_unit_RL=(RelativeLayout) v.findViewById(R.id.arrange_ten_unit_RL);//千位布局
		arrange_the_unit_RL=(RelativeLayout) v.findViewById(R.id.arrange_the_unit_RL);//百位布局
		four_RL=(RelativeLayout) v.findViewById(R.id.four_RL);//十位布局
		five_RL=(RelativeLayout) v.findViewById(R.id.five_RL);//十位布局
		
		four_RL.setVisibility(View.VISIBLE);
		five_RL.setVisibility(View.VISIBLE);
		
		containers[0]=(MGridView) v.findViewById(R.id.arrange_num_mgv);//万
		containers[1]=(MGridView) v.findViewById(R.id.arrange_ten_mgv);//千
		containers[2]=(MGridView) v.findViewById(R.id.arrange_the_unit_mgv);//百
		containers[3]=(MGridView) v.findViewById(R.id.arrange_four_mgv);//十
		containers[4]=(MGridView) v.findViewById(R.id.arrange_five_mgv);
		
		arrange_num_tv=(TextView) v.findViewById(R.id.arrange_num_tv);//万位
		arrange_ten_unit_tv=(TextView) v.findViewById(R.id.arrange_ten_unit_tv);//千位
		arrange_the_unit_tv=(TextView) v.findViewById(R.id.arrange_the_unit_tv);//百位
		arrange_four_tv=(TextView) v.findViewById(R.id.arrange_four_tv);//十位
		arrange_five_tv=(TextView) v.findViewById(R.id.arrange_five_tv);//十位
		arrange_top_playmethod_tv=(TextView) v.findViewById(R.id.arrange_top_playmethod_tv);//玩法提示
//		initSaleType_0_tv();//显示 各个位置上的  提示
		
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
		initSaleType_0_tv();
		switch (Integer.parseInt(tag)) {
//	普通投注
			case 0:
				initSaleType_0();
				
				break;

			default:
				break;
		}
		fast3_title_Layout.setVisibility(View.GONE);//BaseBuyFragment  中顶部的时间提示 只有高频彩才涉及时间提示
	}
	
	 /**
	  * 排列五-普通投注
	  * 
	  */
    private void initSaleType_0(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关（所有玩法都涉及摇一摇）
 		changeShakeVisibility(true);
 		setTitleText(getString(R.string.pl5_title));
 		cancelMutualContainers();//取消容器互斥
    	 
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
    	 reloadLotteryNums(0,1,2,3,4);
    	 containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
    	 containers[1].setMaxNum(0);
    	 containers[2].setMaxNum(0);
    	 containers[3].setMaxNum(0);
    	 containers[4].setMaxNum(0);
    		
    }
 
    /**
     *  玩法提示
     * 规则提示
     */
	private void initSaleType_0_tv() {
		arrange_top_playmethod_tv.setText(getString(R.string.arrange_rule_tv));//每位至少选择一个号码
		arrange_num_tv.setText(getString(R.string.arrange_four_tv));//万位
		arrange_ten_unit_tv.setText(getString(R.string.arrange_qian_tv));//千位
		arrange_the_unit_tv.setText(getString(R.string.arrange_num_tv));//百位
		arrange_four_tv.setText(getString(R.string.arrange_ten_unit_tv));//十位
		arrange_five_tv.setText(getString(R.string.arrange_the_unit_tv));//个位		
	}
	
	@Override
	protected void ok() {
		if(ticket.getLotteryCount() >=10000){
			MyToast.showToast(mActivity, "单票选号金额不能超过20000元！");
			return;
        }
    	super.ok();
		
		
	}
   
}
