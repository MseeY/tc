package com.mitenotc.ui.lotteryinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.BodyFragment3;
import com.mitenotc.ui.ui_utils.AwardDetailCust;

public class AwardInfoDetail extends BodyFragment3{

	private View currentView;//当前被点击的 item的view
	private int clickedPosition = -1;
	private String lotteryId="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isaddjc=false;//用于单独剔除 竞彩条目的
		lotteryId= getMyBundle().getString("lotteryId");
		setTitleNav(Integer.parseInt(1+lotteryId),CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getLotteryName()+getString(R.string.title_nav_award),R.drawable.title_nav_back,R.drawable.title_nav_menu);
		mListView.setPullLoadEnable(true);
		clickedPosition = -1;
	}
	@Override
	protected void onXListViewLoad() {
		sendRequest(2);
	}
	@Override
	protected void onXListViewRefresh() {
		sendRequest(1);
	}
	
	@Override
	public void sendRequest(int key) {
		MessageJson messageJson = new MessageJson();
		Bundle myBundle = getMyBundle();
//		if(Arrays.asList(getResources().getStringArray(R.array.jczq_lottery)).contains(myBundle.getString("lotteryId"))){//竞彩足球
////			竞彩的特殊增加 4004 开奖信息  
//			MessageJson mB=new MessageJson();
////			A	a:可以是一个玩法（若是混关玩法，则需要对阵编号）
////			    b:也可以是多个玩法拼的字符串（不含混关）
////			    c:还可以为空（不含混关），查询所有对阵的开奖信息
//			mB.put("A", "218");
//			mB.put("G", "141202");//G	批次时间
//			mB.put("K", "1");//K	查询类型 1：竞彩足球 2：竞彩篮球
//			submitData(4004, 4004, mB);
//		}else{
			messageJson.put("A", myBundle.getString("lotteryId"));
			messageJson.put("B", myBundle.getString("issue"));
			if(mList != null && mList.size() > 0 && key == 2){//下拉加载时,获取当前 mlist 中的最后一期的 issue
				messageJson.put("B", mList.get(mList.size()-1).getC());
			}
			messageJson.put("C", "20");
			if(key == 1){
				messageJson.put("isforce", "1");
			}
			submitData(key, 1202, messageJson);
//		}
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 2:
			List<MessageBean> listTemp = messageBean.getLIST();
			if(listTemp == null){
				return;
			}
			if(mList == null){
				mList = new ArrayList<MessageBean>();//清除不存在的彩种
			}
			for (MessageBean bean : listTemp) {
				if(CustomTagEnum.getItemByLotteryId(Integer.parseInt(bean.getA())) != null){
						mList.add(bean);
				}
			}
			if(mAdapter == null){////System.out.println("madapter = ======================");
				mAdapter = new AwardInfoAdapter();
				mListView.setAdapter(mAdapter);
			}else {////System.out.println("dataset changed-----------------------------------");
				mAdapter.notifyDataSetChanged();
			}
			mListView.stopLoadMore();
			break;
		case 4004://竞彩的请求
			break;
		default:
			break;
		}
//		if(msg.arg1 == 2){//上拉加载
//			List<MessageBean> listTemp = messageBean.getLIST();
//			if(listTemp == null){
//				return;
//			}
//			if(mList == null){
//				mList = new ArrayList<MessageBean>();//清除不存在的彩种
//			}
//			for (MessageBean bean : listTemp) {
//				if(CustomTagEnum.getItemByLotteryId(Integer.parseInt(bean.getA())) != null){
//						mList.add(bean);
//				}
//			}
//			if(mAdapter == null){////System.out.println("madapter = ======================");
//				mAdapter = new AwardInfoAdapter();
//				mListView.setAdapter(mAdapter);
//			}else {////System.out.println("dataset changed-----------------------------------");
//				mAdapter.notifyDataSetChanged();
//			}
//			mListView.stopLoadMore();
//		}
	}
	
	@Override
	protected void onItemClicked(final int position, final View convertView,final MessageBean item,final ViewHolder holder) {
//		112	十一运
//		113	多乐彩
//		119	江西时时彩
//		120	福彩快三
//		121	山东快乐扑克3
//		122	福彩快三模拟投注
//		123	江苏快三
//		130  模拟 十一运
//		不显示开奖明细   8-21 此处增加屏蔽
		if(Arrays.asList(MyApp.res.getStringArray(R.array.dont_extend_lotteryId)).contains(lotteryId)){
			return ;
		}
		holder.iv_arrow.setImageResource(R.drawable.arrow_down);//图片默认为向下
		holder.award_info_tv_title.setVisibility(View.GONE);//标题设为不可见
		final AwardDetailCust custView = (AwardDetailCust) holder.award_info_detail_contianer.getTag();
//		holder.award_info_detail_contianer.setVisibility(View.VISIBLE);
		
		if(clickedPosition == position){//当前的item是选中的item
		   setVisibleMode(item, holder, custView);
		}else {
			setInvisibleMode(holder);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
//				if(position == mList.size()-1){
//					sendRequest(2);
//					return;
//				}
				
				if(clickedPosition == position){//点到了展开的item
					setInvisibleMode(holder);
					currentView = null;
					clickedPosition = -1;
				}else {//点到的是没有展开的item
					if(clickedPosition == -1){//当前没有展开的item
						setVisibleMode(item, holder, custView);
					}else {//当前有展开的item
						//前一个展开的item的处理
						currentView.findViewById(R.id.award_info_detail_contianer).setVisibility(View.GONE);
						ImageView current_iv_arrow = (ImageView) currentView.findViewById(R.id.award_info_iv_arrow);
						current_iv_arrow.setImageResource(R.drawable.arrow_down);
						
						setVisibleMode(item, holder, custView);
					}
					currentView = convertView;
					clickedPosition = position;
				}
			}
		});
	}

	private void setInvisibleMode(final ViewHolder holder) {
//		if(holder.award_info_detail_contianer.getVisibility() == View.VISIBLE)
			holder.award_info_detail_contianer.setVisibility(View.GONE);
		holder.iv_arrow.setImageResource(R.drawable.arrow_down);
	}

	private void setVisibleMode(final MessageBean item,final ViewHolder holder, final AwardDetailCust custView) {
//		if(holder.award_info_detail_contianer.getVisibility() != View.VISIBLE)
			holder.award_info_detail_contianer.setVisibility(View.VISIBLE);
		holder.iv_arrow.setImageResource(R.drawable.arrow_up);
		custView.show(item);
	}
	
	@Override
	protected void setExtras(ViewHolder holder) {
		////System.out.println("override");  
	}
	
//	@Override
//	public void onDestroy() {
//		//初始化上次点击后遗留可见的条目为不可见
//		for (int i = 0; i < mListView.getChildCount(); i++) {
//			ViewHolder holder = (ViewHolder) mListView.getChildAt(i).getTag();
//			setInvisibleMode(holder);
//		}
//		super.onDestroy();
//	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,Integer.parseInt("1"+getMyBundle().getString("lotteryId")));
		String lotteryId = getMyBundle().getString("lotteryId");
		System.out.println("b---------lotteryId :"+lotteryId);
		setTitleNav(Integer.parseInt(1+lotteryId),CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId)).getLotteryName()+getString(R.string.title_nav_award),R.drawable.title_nav_back,R.drawable.title_nav_menu);
	}
}
