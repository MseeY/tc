package com.mitenotc.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.XListView.IXListViewListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

public class ChaseDetail extends BettingDetail{

	private List<MessageBean> mList_unfinished;
	private List<MessageBean> mList_unfinished_copy;
	private boolean isChecked_unfinished = true;
	private int mListSize_unfinished;
	private int lotteryMoney;//单期彩票的金额
	private long currentTime;
    private Bundle bundle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("追号详情");
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				if(System.currentTimeMillis() - currentTime > 10000){
					sendRequest();
					mListView.setRefreshTime(FormatUtil.timeFormat(System.currentTimeMillis()));
					currentTime = System.currentTimeMillis();
				}else {
					Toast.makeText(mActivity, "数据10秒内已经刷新过", 0).show(); 
				}
			}
			@Override
			public void onLoadMore() {}
		});
	}
	
	@Override
	protected void onOrderReceived(MessageBean messageBean) {
		if(mList == null){
			mList = new ArrayList<MessageBean>();
		}else {
			mList.clear();
		}
		
		if(mList_unfinished == null){
			mList_unfinished = new ArrayList<MessageBean>();
		}else {
			mList_unfinished.clear();
		}
		
//		lotteryMoney = Integer.parseInt(messageBean.getE());
		mList_copy = messageBean.getLIST1();
		for (MessageBean bean : mList_copy) {
			if(!"0".equals(bean.getC())){
				mList.add(bean);
			}else {
				mList_unfinished.add(bean);
			}
		}
		mListSize = mList.size();
		mListSize_unfinished = mList_unfinished.size();
		mAdapter = new ChaseDetailAdapter();
		mListView.setAdapter(mAdapter);
	}
	

	
	@Override
	protected void addHeadAndFoot() {//取消 footer 的添加
		mListView.addHeaderView(header);
	}
	
/*	@Override
	protected void onRightIconClicked() {
		super.onRightIconClicked();
		
		start(ThirdActivity.class,BettingDetail.class,getMyBundle());
	}*/

	class ChaseDetailAdapter extends BaseListAdapter{
		@Override
		public int getCount() {
			return mList.size()+1+mList_unfinished.size()+1;// 已完成, 未完成 三部分内容
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHodler hodler;
			if(convertView !=null && convertView.getTag() !=null){//如果可以复用,满足 getTag() 中有设置过
				hodler = (ViewHodler) convertView.getTag();
			}else {
				convertView = View.inflate(mActivity, R.layout.f_item_betting_detail, null);
				hodler = new ViewHodler(convertView);
				convertView.setTag(hodler);
			}
			
			//初始化 不可见性
			hodler.betting_detail_cb.setVisibility(View.GONE);
			hodler.betting_detail_ll_order.setVisibility(View.GONE);
			hodler.betting_detail_ll_bet_num.setVisibility(View.GONE);
			hodler.chase_detail_tv_award_money.setVisibility(View.GONE);
			//初始化 可见性
			if(position == 0|| position == mList.size()+1){
				
				if(position == 0){//设置已完成的checkbox的属性
					hodler.betting_detail_cb.setVisibility(View.VISIBLE);
					hodler.betting_detail_cb.setText("已完成 "+mListSize+" 期");
					hodler.betting_detail_cb.setChecked(isChecked);
					hodler.betting_detail_cb.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(isChecked = !isChecked){
								mList = mList_copy;
							}else {
								mList_copy = mList;
								mList = new ArrayList<MessageBean>();
							}
							mAdapter.notifyDataSetChanged();
						}
					});
				}else {//设置未完成的 checkbox的属性
					hodler.betting_detail_cb.setVisibility(View.VISIBLE);
					hodler.betting_detail_cb.setText("未完成 "+mListSize_unfinished+" 期");
					hodler.betting_detail_cb.setChecked(isChecked_unfinished);
					hodler.betting_detail_cb.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(isChecked_unfinished = !isChecked_unfinished){
								mList_unfinished = mList_unfinished_copy;
							}else {
								mList_unfinished_copy = mList_unfinished;
								mList_unfinished = new ArrayList<MessageBean>();
							}
							mAdapter.notifyDataSetChanged();
						}
					});
				}
				return convertView;
			}
			hodler.betting_detail_ll_order.setVisibility(View.VISIBLE);
			//背景图片的初始化
			hodler.betting_detail_ll_order.setBackgroundResource(R.drawable.btn_white_bg_no_bottom);
			if((position == mList.size()+1) || (position == 1+mList.size()+1+mList_unfinished.size())){//在 两个list的最后一个位置把背景换成有底的
				hodler.betting_detail_ll_order.setBackgroundResource(R.drawable.btn_white_bg_no_bottom1);
			}
			
			MessageBean item = null;
			if(position<=mList.size()+1){
				item = mList.get(position-1);
			}else {
				item = mList_unfinished.get(position-2-mList.size());
			}
			//奖金设置
			if(AppUtil.isNumeric(item.getD()) && Integer.parseInt(item.getD()) > 0){
				hodler.chase_detail_tv_award_money.setVisibility(View.VISIBLE);
				if(orderId.startsWith("IMI")){
					hodler.chase_detail_tv_award_money.setText(AccountUtils.fenToyuan(item.getD())+"积分");
				}else {
					hodler.chase_detail_tv_award_money.setText("¥"+AccountUtils.fenToyuan(item.getD()));
				}
			}
			String data = item.getE();
			hodler.betting_detail_tv_award_time.setText(item.getC0());
			String buyData = data.substring(5,10).replace("-", "月");
			hodler.betting_detail_tv_date.setText(buyData);
			hodler.betting_detail_tv_issue.setText(item.getA()+"期");////System.out.println("lottery money = "+lotteryMoney);////System.out.println("Integer.parseInt(item.getB()) = "+Integer.parseInt(item.getB()));
			if(orderId.startsWith("IMI")){//模拟购彩
				hodler.betting_detail_tv_bet_money.setText(AccountUtils.fenToyuan(item.getF())+"积分");
			}else {//现金购彩
				hodler.betting_detail_tv_bet_money.setText(FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(item.getF()))));
			}
			final MessageBean item_copy = item;
			if(!"0".equals(item.getC())){//等待付款的条目
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						bundle = new Bundle();
						bundle.putString("orderId", orderId);
						bundle.putString("A", item_copy.getA());
						bundle.putString("B", item_copy.getB());
						bundle.putString("C", item_copy.getC());
						bundle.putString("C0", item_copy.getC0());
						bundle.putString("D", item_copy.getD());
						bundle.putString("E", item_copy.getE());
						bundle.putString("F", item_copy.getF());
						bundle.putString("G", item_copy.getG());
//						String tempIssue="";
//						if(msgObj!=null && msgObj.getLIST1()!=null && msgObj.getLIST1().size() > position-1){
//							tempIssue=msgObj.getLIST1().get(position-1).getA();
							MessageJson jsonMsg=new MessageJson();
							jsonMsg.put("A", orderId);
							jsonMsg.put("B", item_copy.getA());
							submitData(1212, 1212, jsonMsg);
//						}
						
					}
				});
			}else {
//				Toast.makeText(mActivity, "等待追号中", 0).show();
			}
			return convertView;
		}
	}
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		 MessageBean messageBean = (MessageBean) msg.obj;//TODO 为什么这里用全局变量会使 msg.obj 为空?
		 switch (msg.arg1) {
		 case 1212://  新增接口  处理追号订单 用户选号凸显的问题 (与1207 查询订单购彩详情 中返回的messageBean.getLIST()数据相同,所以在此做)
			 if(messageBean!=null &&messageBean.getLIST()!=null ){
				 msgObj.setLIST(messageBean.getLIST());
//				 System.out.println("1212--msgObj"+msgObj.getLIST().toString());
				 String json = new Gson().toJson(msgObj);
				 bundle.putString("gson_msg", json);
				 start(SecondActivity.class,BettingDetail.class,bundle);
			 }else{
				 MyToast.showToast(mActivity, messageBean.getB());
			 }
			 break;
		 }
	}
	
}
