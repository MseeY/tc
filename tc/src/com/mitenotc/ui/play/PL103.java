package com.mitenotc.ui.play;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.BaseBuyFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.LogUtil;
/**
 * 103	七星彩
 * 
 * 套用了排列三的布局
 * 篮球区 是gone
 * @author wanli
 *
 */
@BaseBuyAnnotation(lotteryId = "103",salesType = "0")
public class PL103 extends BaseBuyFragment {
	private View mianView;//玩法主布局
	private Bundle mBundle;//数据传递用的bundle
	private RelativeLayout arrange_num_RL;//一位布局
	private RelativeLayout arrange_ten_unit_RL;//二位布局
	private RelativeLayout arrange_the_unit_RL;//三位布局
	private RelativeLayout four_RL;//四位布局
	private RelativeLayout five_RL;//五
	private RelativeLayout sex_RL;//六
	private RelativeLayout seven_RL;//七
	private String issue;//期次信息
	
	
	private TextView arrange_num_tv;//一位TextView
	private TextView arrange_ten_unit_tv;//二 位TextView
	private TextView arrange_the_unit_tv;//三 位TextView
	private TextView arrange_four_tv;//四  位TextView
	private TextView arrange_five_tv;//五
	private TextView arrange_sex_tv;//六
	private TextView arrange_seven_tv;//七
	
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
		
		containers[0].setMiniNum(1);//一 至少选择1个
		containers[1].setMiniNum(1);//二 至少选择1个
		containers[2].setMiniNum(1);//三 至少选择1个
		containers[3].setMiniNum(1);//四 至少选择1个
		containers[4].setMiniNum(1);//五
		containers[5].setMiniNum(1);//六
		containers[6].setMiniNum(1);//七

		
		containers[0].simpleInit(1, MGridView.RED_MODE, 0,9,false);//一 0-9
		containers[1].simpleInit(1, MGridView.RED_MODE, 0,9,false);//二 0-9
		containers[2].simpleInit(1, MGridView.RED_MODE, 0,9,false);//三 0-9
		containers[3].simpleInit(1, MGridView.RED_MODE, 0,9,false);//四 0-9
		containers[4].simpleInit(1, MGridView.RED_MODE, 0,9,false);
		containers[5].simpleInit(1, MGridView.RED_MODE, 0,9,false);
		containers[6].simpleInit(1, MGridView.RED_MODE, 0,9,false);
		
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
		
		arrange_num_RL=(RelativeLayout) v.findViewById(R.id.arrange_num_RL);//一位布局
		arrange_ten_unit_RL=(RelativeLayout) v.findViewById(R.id.arrange_ten_unit_RL);//二位布局
		arrange_the_unit_RL=(RelativeLayout) v.findViewById(R.id.arrange_the_unit_RL);//三位布局
		four_RL=(RelativeLayout) v.findViewById(R.id.four_RL);//四位布局
		five_RL=(RelativeLayout) v.findViewById(R.id.five_RL);
		sex_RL=(RelativeLayout) v.findViewById(R.id.sex_RL);
		seven_RL=(RelativeLayout) v.findViewById(R.id.seven_RL);
		
		four_RL.setVisibility(View.VISIBLE);//取消默认gone
		five_RL.setVisibility(View.VISIBLE);//五
		sex_RL.setVisibility(View.VISIBLE);//六
		seven_RL.setVisibility(View.VISIBLE);//七
		
		
        containers[0]=(MGridView) v.findViewById(R.id.arrange_num_mgv);//一
		containers[1]=(MGridView) v.findViewById(R.id.arrange_ten_mgv);//二
		containers[2]=(MGridView) v.findViewById(R.id.arrange_the_unit_mgv);//三
		containers[3]=(MGridView) v.findViewById(R.id.arrange_four_mgv);//四
		containers[4]=(MGridView) v.findViewById(R.id.arrange_five_mgv);//四
		containers[5]=(MGridView) v.findViewById(R.id.arrange_sex_mgv);//四
		containers[6]=(MGridView) v.findViewById(R.id.arrange_seven_mgv);//四
		
		arrange_num_tv=(TextView) v.findViewById(R.id.arrange_num_tv);//一
		arrange_ten_unit_tv=(TextView) v.findViewById(R.id.arrange_ten_unit_tv);//二
		arrange_the_unit_tv=(TextView) v.findViewById(R.id.arrange_the_unit_tv);//三
		arrange_four_tv=(TextView) v.findViewById(R.id.arrange_four_tv);//四
		arrange_top_playmethod_tv=(TextView) v.findViewById(R.id.arrange_top_playmethod_tv);//玩法提示
		arrange_five_tv=(TextView) v.findViewById(R.id.arrange_five_tv);//五
		arrange_sex_tv=(TextView) v.findViewById(R.id.arrange_sex_tv);//六
		arrange_seven_tv=(TextView) v.findViewById(R.id.arrange_seven_tv);//七
		
		arrange_num_tv.setText(getString(R.string.qi_xin_one_text));//一
		arrange_ten_unit_tv.setText(getString(R.string.qi_xin_two_text));//二
		arrange_the_unit_tv.setText(getString(R.string.qi_xin_three_text));//三
		arrange_four_tv.setText(getString(R.string.qi_xin_four_text));// 四 
		arrange_five_tv.setText(getString(R.string.qi_xin_five_text));// 五 
		arrange_sex_tv.setText(getString(R.string.qi_xin_sex_text));// 六 
		arrange_seven_tv.setText(getString(R.string.qi_xin_seven_text));// 七 
		arrange_top_playmethod_tv.setText(getString(R.string.arrange_rule_tv));//每位至少选择1个号码
		
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
			case 0:
				initSaleType_0();
				
				break;
          default:
				break;
		}
		fast3_title_Layout.setVisibility(View.GONE);//BaseBuyFragment  中顶部的时间提示 只有高频彩才涉及时间提示
	}
	
	 /**
	  * 普通投注
	  */
    private void initSaleType_0(){
    	//显示摇一摇条目,同时改变 摇一摇传感器的 开和关（所有玩法都涉及摇一摇）
    	changeShakeVisibility(true);
    	setTitleText(getString(R.string.qxc_title));
    	cancelMutualContainers();//取消容器互斥
    	initSaleType_0_tv();
 		
    	//把相应的saleType的 selectBalls 数据添加到 ticket 中
    	 reloadLotteryNums(0,1,2,3,4,5,6);
    	 containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
    	 containers[1].setMaxNum(0);
    	 containers[2].setMaxNum(0);
    	 containers[3].setMaxNum(0);
    	 containers[4].setMaxNum(0);
    	 containers[5].setMaxNum(0);
    	 containers[6].setMaxNum(0);
    	 

    	 
    	 
 		
    }


    /**
     * 默认只有 普通投注
     * 
     * 玩法提示
     * 规则提示
     */
	private void initSaleType_0_tv() {
		arrange_top_playmethod_tv.setText(getString(R.string.arrange_rule_tv));//每位至少选择一个号码
		arrange_num_tv.setVisibility(View.VISIBLE);
	}
	
	
	
	/**
	 * 获取注解
	 */
//	private void getannotation(){
//		BaseBuyAnnotation annotation = getClass().getAnnotation(BaseBuyAnnotation.class);
//		if(annotation != null){
//			lotteryId = annotation.lotteryId();
//			salesType = annotation.salesType();
//		}
//	}
//	@Override
//	protected void onMessageReceived(Message msg) {
//		MessageBean messageBean = (MessageBean) msg.obj;
//		switch (msg.arg2) {
//
//		case 1201:
//			issue = messageBean.getLIST().get(0).getD();
//			break;
//		default:
//			break;
//		}
//	}
	@Override
	protected void ok() {
		if(ticket.getLotteryCount() >=10000){
			MyToast.showToast(mActivity, "单票选号金额不能超过20000元！");
			return;
        }
    	super.ok();
		
		
	}

   
}
