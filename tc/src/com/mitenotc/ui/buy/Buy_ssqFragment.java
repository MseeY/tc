package com.mitenotc.ui.buy;

import android.view.View;

import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MGridView;

@BaseBuyAnnotation(lotteryId = "118",salesType = "1")
public class Buy_ssqFragment extends BaseBuyFragment{
	private long testTime;

	@Override
	protected void initContainers() {//初始化 containers 以及 containers 的父控件   
		containers[0] = (MGridView)lotteryView.findViewById(R.id.ssq_red_number_container);
	    containers[1] =(MGridView) lotteryView.findViewById(R.id.red_mop_mgv);
	    containers[2] =(MGridView) lotteryView.findViewById(R.id.ssq_blue_number_container);
		containers[0].setMiniNum(6);
		containers[1].setMiniNum(6);
		containers[2].simpleInit(1, MGridView.BLUE_MODE, 1, 16);
		
//		mutualContainers(0,1);
	}
	@Override
	protected View customContent_saleType() {//注意 content_saleType 必须在该方法中实例化
		return content_saleType = View.inflate(mActivity, R.layout.f_sale_type_ssq, null);
	}
	
	@Override
	protected View customLotteryView() {//注意 lotteryView 必须在该方法中实例化
		return lotteryView = View.inflate(mActivity, R.layout.play_ssp, null);
	}
	
	@Override
	protected void onSaleTypeChanged(String tag) {
		int saleTag = Integer.parseInt(tag);
		switch (saleTag) {
		case 0:
			//把彩种的布局切换到普通投注
			initSaleType_0();
			break;
		case 1:
			//把彩种的布局切换到普通投注
			initSaleType_0();
			break;
		case 2:
			//把彩种的布局切换到胆拖投注
			initSaleType_2();
			break;
		default:
			break;
		}
	}
	
	private void initSaleType_0() {
		cancelMutualContainers();
		
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,2);
		//显示摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(true);
		
		containers[0].setMaxNum(0);//重置最大数,如果最大数设为0,代表不设置最大数
	}
	private void initSaleType_2() {//胆拖投注 共3个容器
		////System.out.println("time_ssq1 = "+(testTime = System.currentTimeMillis()));
		cancelMutualContainers();
		////System.out.println("time_ssq2 = "+(System.currentTimeMillis() - testTime));
		mutualContainers(0,1);
		////System.out.println("time_ssq3 = "+(System.currentTimeMillis() - testTime));
		//把相应的saleType的 selectBalls 数据添加到 ticket 中
		reloadLotteryNums(0,1,2);
		////System.out.println("time_ssq4 = "+(System.currentTimeMillis() - testTime));
		//隐藏摇一摇条目,同时改变 摇一摇传感器的 开和关
		changeShakeVisibility(false);
		////System.out.println("time_ssq5 = "+(System.currentTimeMillis() - testTime));
		containers[0].setMaxNum(5);
		////System.out.println("time_ssq6 = "+(System.currentTimeMillis() - testTime));
	}
	
}
