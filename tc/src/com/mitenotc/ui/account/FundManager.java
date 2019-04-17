package com.mitenotc.ui.account;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AnimationController;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SingleLayoutListView;
import com.mitenotc.utils.SingleLayoutListView.OnLoadMoreListener;
import com.mitenotc.utils.SingleLayoutListView.OnRefreshListener;

public class FundManager extends BaseFragment{

	private static int startIndex = 1;
	private static int startIndex1 = 1;
	private static int countPage1 = 2;// 记录上次分页的个数，初始化为2
	private static int countPage2 = 2;
	public static int maxNumber = 20; // 每页20条数据
	public boolean flag1;
	public boolean flag2;
	private ViewPager pager;
	private List<View> views;
	private PagerAdapter pagerAdapter;
	private SingleLayoutListView mListView1;
	private SingleLayoutListView mListView2;
	private MyAdapter mAdapter1;
	private MyAdapter mAdapter2;
	private LinearLayout inflate1;
	private LinearLayout inflate2;
	private ImageView circle1;
	private ImageView circle2;
	private TextView recordsText;
	private FrameLayout frame;
	private ImageView circle3;
	private ImageView circle4;
	private ImageView circle5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_records);
		network_notice_required = true;
		setTitleNav(R.string.acc_title_fund_manager, R.drawable.title_nav_back, 0);
		initBrListView();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mListView1.onRefreshComplete();
				break;
			case 2:
				mListView2.onRefreshComplete();
				break;
			}
		};
	};
	
	private void initCircle(){
		circle1.setImageResource(R.drawable.acc_circle_bg);
		circle2.setImageResource(R.drawable.acc_circle_bg);
	}
	
	@Override
	protected void onReLogin() {
		super.onReLogin();
		mListView1.pull2RefreshManually();
		configListViewPushListener1();
	}
	
	private void configListViewPushListener1() {
		mListView1.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1103,0);
			}
		});

		mListView1.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(1103,1);
			}
		});
	}

	private void configListViewPushListener2() {
		mListView2.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1113,2);
			}
		});

		mListView2.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(1113,3);
			}
		});
	}
	
	/** 获取网络数据 */
	private void sendRequest(int cmd,int type) {
		MessageJson msg;
		switch (cmd) {
			case 1103:
				switch (type) {
				case 0:
					msg = new MessageJson();
					startIndex = 1;
					msg.put("A",365); 
					msg.put("D",0); // 交易类型  查询全部
					msg.put("E", startIndex++); // 当前第一页
					msg.put("F", maxNumber);  // 每页20行
					submitData(type, cmd, msg);
					break;
				case 1:
					msg = new MessageJson();
					msg.put("A",365); 
					msg.put("D",0); // 交易类型  查询全部
					msg.put("E", startIndex++); // 当前第一页
					msg.put("F", maxNumber);  // 每页20行
					submitData(type, cmd, msg);
					break;
				}
				break;
			case 1113:
				switch (type) {
				case 2:
					msg = new MessageJson();
					startIndex1 = 1;
					msg.put("A",365); 
					msg.put("D",0); // 交易类型  查询全部
					msg.put("E", startIndex1++); // 当前第一页
					msg.put("F", maxNumber);  // 每页20行
					submitData(type, cmd, msg);
					break;
				case 3:
					msg = new MessageJson();
					msg.put("A",365); 
					msg.put("D",0); // 交易类型  查询全部
					msg.put("E", startIndex1++); // 当前第一页
					msg.put("F", maxNumber);  // 每页20行
					submitData(type, cmd, msg);
					break;
				}
				break;
			}

	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0:
			if (messageBean.getLIST() != null) {
				inflate1.setVisibility(View.GONE);
				if (mAdapter1 != null) {
					mAdapter1.mList = messageBean.getLIST();
					mAdapter1.notifyDataSetChanged();
					if (mAdapter1.mList.size() < 18) {
						mListView1.hideLoadMore();
					}
				}
			} else {
				// 暂时不做任何操作
				inflate1.setVisibility(View.VISIBLE);
				mListView1.hideLoadMore();
			}
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						handler.sendEmptyMessage(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();

			// mListView1.onRefreshComplete();// 下拉刷新完成
			break;
		case 1:
			countPage1 = startIndex;
			mListView1.onLoadMoreComplete();
			if (messageBean.getLIST() != null) {
				if (mAdapter1 != null) {
					mAdapter1.mList.addAll(messageBean.getLIST());
					mAdapter1.notifyDataSetChanged();
				}
			} else {
				mListView1.showLoadMoreText();
			}
			break;

		case 2:
			if (messageBean.getLIST() != null) {
				inflate2.setVisibility(View.GONE);
				if (mAdapter2 != null) {
					mAdapter2.mList = messageBean.getLIST();
					mAdapter2.notifyDataSetChanged();
					if (mAdapter2.mList.size() < 18) {
						mListView2.hideLoadMore();
					}
				}
			} else {
				inflate2.setVisibility(View.VISIBLE);
				mListView2.hideLoadMore();
				// 暂时不做任何操作
			}
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						handler.sendEmptyMessage(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			// mListView2.onRefreshComplete();// 下拉刷新完成
			break;
		case 3:
			countPage2 = startIndex1;
			mListView2.onLoadMoreComplete();
			if (messageBean.getLIST() != null) {
				if (mAdapter2 != null) {
					mAdapter2.mList.addAll(messageBean.getLIST());
					mAdapter2.notifyDataSetChanged();
				}
			} else {
				mListView2.showLoadMoreText();
			}
			break;
		}
	}

	@Override
	public void onNetworkRefresh() {
		super.onNetworkRefresh();
		mListView1.pull2RefreshManually();
		configListViewPushListener1();
		mListView1.hideLoadMore();
		mListView2.hideLoadMore();
		mListView1.onRefreshComplete();
		mListView1.onLoadMoreComplete();
		mListView2.onRefreshComplete();
		mListView2.onLoadMoreComplete();
	}

	private void initBrListView() {
		flag1 = false;
		flag2 = true;
		recordsText = (TextView) findViewById(R.id.tv_acc_msg_text1);
		circle1 = (ImageView) findViewById(R.id.iv_acc_records_circle1);
		circle2 = (ImageView) findViewById(R.id.iv_acc_records_circle2);
		circle3 = (ImageView) findViewById(R.id.iv_acc_records_circle3);
		circle4 = (ImageView) findViewById(R.id.iv_acc_records_circle4);
		circle5 = (ImageView) findViewById(R.id.iv_acc_records_circle5);
		circle3.setVisibility(View.GONE);
		circle4.setVisibility(View.GONE);
		circle5.setVisibility(View.GONE);
		recordsText.setText("资金记录");
		pager = (ViewPager) findViewById(R.id.ii_records_viewpager);
		views = new ArrayList<View>();
		configListView();
		pagerAdapter = new MyViewPagerAdapter();
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					AnimationController.slideIn(recordsText, 100, 100);
					recordsText.setText("资金记录");
					initCircle(); //初始化圆的背景
					circle1.setImageResource(R.drawable.acc_circle);
					startIndex = countPage1;
					if (flag1) {
						mListView1.pull2RefreshManually();
						configListViewPushListener1();
						flag1 = false;
					}
					break;
				case 1:
					AnimationController.slideIn(recordsText, 100, 100);
					recordsText.setText("积分记录");
					initCircle(); //初始化圆的背景
					circle2.setImageResource(R.drawable.acc_circle);
					startIndex1 = countPage2;
					if (flag2) {
						mListView2.pull2RefreshManually();
						configListViewPushListener2();
						flag2 = false;
					}
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
		
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void configListView() {
		// 第一个页面
				mAdapter1 = new MyAdapter(mActivity);
				mListView1 = new SingleLayoutListView(mActivity);
				mListView1.setAdapter(mAdapter1);
				configListViewPushListener1();
				frame = new FrameLayout(mActivity);
				frame.addView(mListView1, 0);
				inflate1 = (LinearLayout) View.inflate(mActivity,R.layout.m_acc_null_message, null);
				frame.addView(inflate1, 1);
				configListViewIntoList(mListView1);
				views.add(frame);
				// 第二个页面
				mAdapter2 = new MyAdapter(mActivity);
				mListView2 = new SingleLayoutListView(mActivity);
				mListView2.setAdapter(mAdapter2);
				configListViewPushListener2();
				frame = new FrameLayout(mActivity);
				frame.addView(mListView2, 0);
				inflate2 = (LinearLayout) View.inflate(mActivity,R.layout.m_acc_null_message, null);
				frame.addView(inflate2, 1);
				configListViewIntoList(mListView2);
				views.add(frame);
		
	}
	
	private void configListViewIntoList(SingleLayoutListView listView) {
		listView.setCanLoadMore(true);
		listView.setCanRefresh(true);
		listView.setAutoLoadMore(true);
		listView.setMoveToFirstItemAfterRefresh(true);
		listView.setDoRefreshOnUIChanged(true);
		listView.showLoadMoreText1();
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setDrawingCacheEnabled(false);
		
	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		public List<MessageBean> mList;

		public MyAdapter(Context pContext) {
			mInflater = LayoutInflater.from(pContext);
			if (mList != null) {
				mList.clear();
			}
			mList = new ArrayList<MessageBean>();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewFundHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.m_acc_me_fund_item1,null);
				holder = new ViewFundHolder();
				holder.number = (ImageView) convertView.findViewById(R.id.acc_fund_item_number);
//				holder.acc_me_bg = (LinearLayout) convertView.findViewById(R.id.ll_acc_me_fund_bg);
//				holder.item_gone = (LinearLayout) convertView.findViewById(R.id.ll_acc_me_item_gone);
				holder.tranType = (TextView) convertView.findViewById(R.id.tv_acc_fund_trantype);
				holder.tranTime = (TextView) convertView.findViewById(R.id.tv_acc_fund_tranTime);
				holder.tranMoney = (TextView) convertView.findViewById(R.id.tv_acc_fund_tranmoney);
				holder.tranState = (TextView) convertView.findViewById(R.id.tv_acc_fund_transtate);
				holder.tranRemark = (TextView) convertView.findViewById(R.id.tv_acc_fund_tranremark);
				holder.tranText = (TextView) convertView.findViewById(R.id.tv_acc_fund_text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewFundHolder) convertView.getTag();
			}

			//以下三行语句是复用之后的初始化，如果不初始化条目容易错位。
//			holder.number.setVisibility(0);
//			holder.item_gone.setVisibility(View.VISIBLE);
//			holder.acc_me_bg.setBackgroundResource(R.drawable.acc_msg_title_bg);
			holder.tranMoney.setText("");
			holder.tranType.setText("");
			holder.tranType.setTextColor(0);
			holder.tranText.setText("");
			holder.tranState.setText("");
			holder.tranRemark.setText("");
			holder.tranTime.setText("");
			holder.number.setBackgroundResource(0);
			
			//塞入数据
			MessageBean messageBean = mList.get(position);
			holder.tranType.setText(messageBean.getD());
			holder.tranTime.setText(messageBean.getA());
			holder.tranRemark.setText(messageBean.getI());
	
//			//给最后一个条目换个背景
//			if (position == getCount() - 1) {
//				holder.item_gone.setVisibility(View.GONE);
//				holder.acc_me_bg.setBackgroundResource(R.drawable.acc_me_white_bg);
//			}
			
			holder.tranText.setText(messageBean.getJ()+"：");
			if("金额".equals(messageBean.getJ())){
				holder.tranMoney.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(messageBean.getE()+""))));
			}else if("积分".equals(messageBean.getJ())){
				holder.tranMoney.setText(AccountUtils.fenToyuan(messageBean.getE()));
			}
			
			//交易类型
			switch (Integer.parseInt(messageBean.getF())) {
			case 0:
				holder.tranState.setText("成功");
				break;
			case 10016:
				holder.tranState.setText("提现办理中");
				break;
			case 10017:
				holder.tranState.setText("提现失败");
				break;
			case 10013:
				holder.tranState.setText("交易处理中");
				break;
			case 10025:
				holder.tranState.setText("部分退款");
				break;
			case 10035:
				holder.tranState.setText("充值交易中");
				break;
			case 10046:
				holder.tranState.setText("购买红包中");
				break;
			case 10059:
				holder.tranState.setText("等待付款中");
				break;
			case 10012:
				holder.tranState.setText("交易失败");
				break;
			case 11002:
				holder.tranState.setText("交易失败");
				break;
			}
			
			// 0 ：代表支出     1：代表收入
			if(!AppUtil.isNumeric(messageBean.getH()))
				return convertView;
			switch (Integer.parseInt(messageBean.getH())) {
			case 0:
				holder.number.setBackgroundResource(R.drawable.small_icon_fund_zhi);
				holder.tranType.setTextColor(MyApp.res.getColor(R.color.text_yellow));
//				holder.number.setVisibility(View.VISIBLE);
//				holder.number.setBackgroundResource(R.drawable.shape_acc_fund_pay);
				break;
			case 1:
				holder.number.setBackgroundResource(R.drawable.small_icon_fund_shou);
				holder.tranType.setTextColor(MyApp.res.getColor(R.color.text_red));
//				holder.number.setText("收");
//				holder.number.setVisibility(View.VISIBLE);
//				holder.number.setBackgroundResource(R.drawable.shape_acc_fund_income);
				break;
			}

			return convertView;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	private static class ViewFundHolder {
		//以下三个是界面效果的组件
		ImageView number;
		LinearLayout acc_me_bg;
		LinearLayout item_gone;
		TextView tranType; //交易类型
		TextView tranTime; //交易时间
		TextView tranMoney; //交易金额
		TextView tranState; //交易状态
		TextView tranRemark; //交易备注（文档中的交易说明）
		TextView tranText;
	}
	
	private class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = views.get(position);
			container.removeView(view);
		}
	}
}
