package com.mitenotc.ui.ui_utils;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.tc.R;
import com.mitenotc.utils.FormatUtil;

public class AwardDetailCust extends LinearLayout{
	
	public static int MODE_BALL = 0;//球类彩票//不显示右边的button
	public static int MODE_SHENGFU = 1;//足球类彩票//显示右边的Button
	private List<MessageBean> awardDetailList;//开奖详情list
	private LinearLayout ll_container;
	private MessageBean awardResult;//该期次的信息
	
//	private String[] award_level;

	public AwardDetailCust(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AwardDetailCust(Context context) {
		super(context);
		init();
	}
	public AwardDetailCust(Context context,MessageBean awardResult) {
		this(context);
		this.awardResult = awardResult;
		init();
	}

	private void init() {
//		award_level = getResources().getStringArray(R.array.award_level);
		View view = View.inflate(getContext(),R.layout.f_list_sfc , this);
		ll_container = (LinearLayout) view.findViewById(R.id.award_detail_ll_container);
		if(awardResult != null){
			awardDetailList = awardResult.getLIST();
			show();
		}
	}

	public void show() {
		String sale_info = awardResult.getG();//本期销量
		String award_total = awardResult.getH();//本期奖池
		awardDetailList = awardResult.getLIST();//开奖详情
		
		////System.out.println("sale_info = "+FormatUtil.moneyFormat(sale_info));
		////System.out.println("award_total = "+award_total);
		
		TextView tv_sale_info = (TextView) findViewById(R.id.sale_info);
		tv_sale_info.setText("本期销量 : "+FormatUtil.moneyFormat(sale_info));
		
		String[] codes_ball_lottery = getResources().getStringArray(R.array.codes_ball_lottery);
		String[] codes_frequentry_lottery = getResources().getStringArray(R.array.codes_frequentry_lottery);
		String[] codes_shengfu_lottery = getResources().getStringArray(R.array.codes_shengfu_lottery);
		if(Arrays.asList(codes_ball_lottery).contains(awardResult.getA()) && !Arrays.asList(codes_frequentry_lottery).contains(awardResult.getA())){//球类彩票, 且不是高频
			TextView tv_award_total = (TextView) findViewById(R.id.award_total);
			tv_award_total.setVisibility(View.VISIBLE);
			tv_award_total.setText("奖池累计 :"+FormatUtil.moneyFormat(award_total));
		}else if(Arrays.asList(codes_shengfu_lottery).contains(awardResult.getA())){//足球类彩票
			Button bt_shengfu = (Button) findViewById(R.id.ymx_sfcinfo_bt);
			bt_shengfu.setVisibility(View.VISIBLE);
		}
		for (MessageBean bean : awardDetailList) {
			View v = View.inflate(getContext(), R.layout.f_list_sfc_child, null);
			TextView awards = (TextView) v.findViewById(R.id.ymx_item_tv_text1);
			TextView awards_num = (TextView) v.findViewById(R.id.ymx_item_tv_text2);
			TextView award_money = (TextView) v.findViewById(R.id.ymx_item_tv_text3);
			awards.setText(bean.getA1());
			awards_num.setText(bean.getB());
			award_money.setText("¥"+bean.getC());
			
			
			ll_container.addView(v);
		}
	}
	
	public void show(MessageBean awardResult){
		this.awardResult = awardResult;
		ll_container.removeAllViews();
		show();
	}
}
