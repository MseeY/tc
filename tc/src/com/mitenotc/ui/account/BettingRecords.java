package com.mitenotc.ui.account;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
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
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.user.BettingDetail;
import com.mitenotc.ui.user.ChaseDetail;
import com.mitenotc.utils.AnimationController;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SingleLayoutListView;
import com.mitenotc.utils.SingleLayoutListView.OnLoadMoreListener;
import com.mitenotc.utils.SingleLayoutListView.OnRefreshListener;

public class BettingRecords extends BaseFragment {
	private static int startIndex = 1;
	private static int countPage1 = 2;// 记录上次分页的个数，初始化为2
	private static int countPage2 = 2;
	private static int countPage3 = 2;
	private static int countPage4 = 2;
	private static int countPage5 = 2;
	public static int maxNumber = 20; // 每页20条数据
	public boolean flag1;
	public boolean flag2;
	public boolean flag3;
	public boolean flag4;
	public boolean flag5;
	private ViewPager pager;
	private List<View> views;
	private PagerAdapter pagerAdapter;
	private SingleLayoutListView mListView1;
	private SingleLayoutListView mListView2;
	private SingleLayoutListView mListView3;
	private SingleLayoutListView mListView4;
	private SingleLayoutListView mListView5;
	private MyAdapter mAdapter1;
	private MyAdapter mAdapter2;
	private MyAdapter mAdapter3;
	private MyAdapter mAdapter4;
	private MyAdapter mAdapter5;
	private LinearLayout inflate1;
	private LinearLayout inflate2;
	private LinearLayout inflate3;
	private LinearLayout inflate4;
	private LinearLayout inflate5;
	private ImageView circle1;
	private ImageView circle2;
	private ImageView circle3;
	private ImageView circle4;
	private ImageView circle5;
	private TextView recordsText;
	private FrameLayout frame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_records);
		network_notice_required = true;
		setTitleNav(CustomTagEnum.betting_record.getId(),R.string.acc_title_betting_records, R.drawable.title_nav_back,R.drawable.title_nav_menu);
		initBrListView();
	}
	
	private void initCircle(){
		circle1.setImageResource(R.drawable.acc_circle_bg);
		circle2.setImageResource(R.drawable.acc_circle_bg);
		circle3.setImageResource(R.drawable.acc_circle_bg);
		circle4.setImageResource(R.drawable.acc_circle_bg);
		circle5.setImageResource(R.drawable.acc_circle_bg);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setTitleNav(CustomTagEnum.betting_record.getId(),R.string.acc_title_betting_records, R.drawable.title_nav_back,R.drawable.title_nav_menu);
	}
	
	@Override
	protected void onReLogin() {
		super.onReLogin();
		mListView1.pull2RefreshManually();
		configListViewPushListener1();
	}
	
	@Override
	public void onNetworkRefresh() {
		super.onNetworkRefresh();
		mListView1.pull2RefreshManually();
		configListViewPushListener1();
		mListView1.hideLoadMore();
		mListView2.hideLoadMore();
		mListView3.hideLoadMore();
		mListView4.hideLoadMore();
		mListView5.hideLoadMore();
		mListView1.onRefreshComplete();
		mListView1.onLoadMoreComplete();
		mListView2.onRefreshComplete();
		mListView2.onLoadMoreComplete();
		mListView3.onRefreshComplete();
		mListView3.onLoadMoreComplete();
		mListView4.onRefreshComplete();
		mListView4.onLoadMoreComplete();
		mListView5.onRefreshComplete();
		mListView5.onLoadMoreComplete();
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
			case 3:
				mListView3.onRefreshComplete();
				break;
			case 4:
				mListView4.onRefreshComplete();
				break;
			case 5:
				mListView5.onRefreshComplete();
				break;
			}
		};
	};

	private void initBrListView() {
		flag1 = false;
		flag2 = true;
		flag3 = true;
		flag4 = true;
		flag5 = true;
		recordsText = (TextView) findViewById(R.id.tv_acc_msg_text1);
		circle1 = (ImageView) findViewById(R.id.iv_acc_records_circle1);
		circle2 = (ImageView) findViewById(R.id.iv_acc_records_circle2);
		circle3 = (ImageView) findViewById(R.id.iv_acc_records_circle3);
		circle4 = (ImageView) findViewById(R.id.iv_acc_records_circle4);
		circle5 = (ImageView) findViewById(R.id.iv_acc_records_circle5);
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
					recordsText.setText("全部记录");
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
					recordsText.setText("未支付记录");
					initCircle(); //初始化圆的背景
					circle2.setImageResource(R.drawable.acc_circle);
					startIndex = countPage2;
					if (flag2) {
						mListView2.pull2RefreshManually();
						configListViewPushListener2();
						flag2 = false;
					}
					break;
				case 2:
					AnimationController.slideIn(recordsText, 100, 100);
					recordsText.setText("中奖记录");
					initCircle(); //初始化圆的背景
					circle3.setImageResource(R.drawable.acc_circle);
					startIndex = countPage3;
					if (flag3) {
						mListView3.pull2RefreshManually();
						configListViewPushListener3();
						flag3 = false;
					}
					break;
				case 3:
					AnimationController.slideIn(recordsText, 100, 100);
					recordsText.setText("待开奖记录");
					initCircle(); //初始化圆的背景
					circle4.setImageResource(R.drawable.acc_circle);
					startIndex = countPage4;
					if (flag4) {
						mListView4.pull2RefreshManually();
						configListViewPushListener4();
						flag4 = false;
					}
					break;
				case 4:
					AnimationController.slideIn(recordsText, 100, 100);
					recordsText.setText("追号记录");
					initCircle(); //初始化圆的背景
					circle5.setImageResource(R.drawable.acc_circle);
					startIndex = countPage5;
					if (flag5) {
						mListView5.pull2RefreshManually();
						configListViewPushListener5();
						flag5 = false;
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
		// 第三个页面
		mAdapter3 = new MyAdapter(mActivity);
		mListView3 = new SingleLayoutListView(mActivity);
		mListView3.setAdapter(mAdapter3);
		configListViewPushListener3();
		frame = new FrameLayout(mActivity);
		frame.addView(mListView3, 0);
		inflate3 = (LinearLayout) View.inflate(mActivity,R.layout.m_acc_null_message, null);
		frame.addView(inflate3, 1);
		configListViewIntoList(mListView3);
		views.add(frame);
		// 第四个页面
		mAdapter4 = new MyAdapter(mActivity);
		mListView4 = new SingleLayoutListView(mActivity);
		mListView4.setAdapter(mAdapter4);
		configListViewPushListener4();
		frame = new FrameLayout(mActivity);
		frame.addView(mListView4, 0);
		inflate4 = (LinearLayout) View.inflate(mActivity,R.layout.m_acc_null_message, null);
		frame.addView(inflate4, 1);
		configListViewIntoList(mListView4);
		views.add(frame);
		// 第五个页面
		mAdapter5 = new MyAdapter(mActivity);
		mListView5 = new SingleLayoutListView(mActivity);
		mListView5.setAdapter(mAdapter5);
		configListViewPushListener5();
		frame = new FrameLayout(mActivity);
		frame.addView(mListView5, 0);
		inflate5 = (LinearLayout) View.inflate(mActivity,R.layout.m_acc_null_message, null);
		frame.addView(inflate5, 1);
		configListViewIntoList(mListView5);
		views.add(frame);
	}

	private void configListViewPushListener1() {
		mListView1.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1, 0, 0);
			}
		});

		mListView1.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(1, 1, 1);
			}
		});
	}

	private void configListViewPushListener2() {
		mListView2.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(2, 0, 2);
			}
		});

		mListView2.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(2, 1, 3);
			}
		});
	}

	private void configListViewPushListener3() {
		mListView3.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(3, 0, 4);
			}
		});

		mListView3.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(3, 1, 5);
			}
		});
	}

	private void configListViewPushListener4() {
		mListView4.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(4, 0, 6);
			}
		});

		mListView4.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(4, 1, 7);
			}
		});
	}

	private void configListViewPushListener5() {
		mListView5.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(5, 0, 8);
			}
		});

		mListView5.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				sendRequest(5, 1, 9);
			}
		});
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

	/** 获取网络数据 */
	private void sendRequest(int page, int type, int adapterType) {
		MessageJson msg;
		switch (type) {
		case 0:// 下拉刷新
			msg = new MessageJson();
			startIndex = 1;
			msg.put("B", page);
			msg.put("C", startIndex++);
			msg.put("D", maxNumber);
			submitData(adapterType, 1206, msg);
			break;
		case 1:// 上拉加载更多
			msg = new MessageJson();
			msg.put("B", page);
			msg.put("C", startIndex++);
			msg.put("D", maxNumber);
			submitData(adapterType, 1206, msg);
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
			countPage2 = startIndex;
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
		case 4:
			if (messageBean.getLIST() != null) {
				inflate3.setVisibility(View.GONE);
				if (mAdapter3 != null) {
					mAdapter3.mList = messageBean.getLIST();
					mAdapter3.notifyDataSetChanged();
					if (mAdapter3.mList.size() < 18) {
						mListView3.hideLoadMore();
					}
				}
			} else {
				inflate3.setVisibility(View.VISIBLE);
				mListView3.hideLoadMore();
				// 暂时不做任何操作
			}
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						handler.sendEmptyMessage(3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			// mListView3.onRefreshComplete();// 下拉刷新完成
			break;
		case 5:
			countPage3 = startIndex;
			mListView3.onLoadMoreComplete();
			if (messageBean.getLIST() != null) {
				if (mAdapter3 != null) {
					mAdapter3.mList.addAll(messageBean.getLIST());
					mAdapter3.notifyDataSetChanged();
				}
			} else {
				mListView3.showLoadMoreText();
			}
			break;
		case 6:
			if (messageBean.getLIST() != null) {
				inflate4.setVisibility(View.GONE);
				if (mAdapter4 != null) {
					mAdapter4.mList = messageBean.getLIST();
					mAdapter4.notifyDataSetChanged();
					if (mAdapter4.mList.size() < 18) {
						mListView4.hideLoadMore();
					}
				}
			} else {
				inflate4.setVisibility(View.VISIBLE);
				mListView4.hideLoadMore();
				// 暂时不做任何操作
			}
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						handler.sendEmptyMessage(4);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			// mListView4.onRefreshComplete();// 下拉刷新完成
			break;
		case 7:
			countPage4 = startIndex;
			mListView4.onLoadMoreComplete();
			if (messageBean.getLIST() != null) {
				if (mAdapter4 != null) {
					mAdapter4.mList.addAll(messageBean.getLIST());
					mAdapter4.notifyDataSetChanged();
				}
			} else {
				mListView4.showLoadMoreText();
			}
			break;
		case 8:
			if (messageBean.getLIST() != null) {
				inflate5.setVisibility(View.GONE);
				if (mAdapter5 != null) {
					mAdapter5.mList = messageBean.getLIST();
					mAdapter5.notifyDataSetChanged();
					if (mAdapter5.mList.size() < 18) {
						mListView5.hideLoadMore();
					}
				}
			} else {
				inflate5.setVisibility(View.VISIBLE);
				mListView5.hideLoadMore();
				// 暂时不做任何操作
			}
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						handler.sendEmptyMessage(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			// mListView5.onRefreshComplete();// 下拉刷新完成
			break;
		case 9:
			countPage5 = startIndex;
			mListView5.onLoadMoreComplete();
			if (messageBean.getLIST() != null) {
				if (mAdapter5 != null) {
					mAdapter5.mList.addAll(messageBean.getLIST());
					mAdapter5.notifyDataSetChanged();
				}
			} else {
				mListView5.showLoadMoreText();
			}
			break;
		}
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
			ViewRecordsHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.m_acc_records_item1,null);
				holder = new ViewRecordsHolder();
				holder.playName = (TextView) convertView.findViewById(R.id.tv_acc_records_playname);
				holder.buyTime = (TextView) convertView.findViewById(R.id.tv_acc_records_buytime);
				holder.money = (TextView) convertView.findViewById(R.id.tv_acc_records_money);
				holder.orderState = (TextView) convertView.findViewById(R.id.tv_acc_records_orderstate);
				holder.bonus = (TextView) convertView.findViewById(R.id.tv_acc_records_bonus);
				holder.text = (TextView) convertView.findViewById(R.id.tv_acc_records_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewRecordsHolder) convertView.getTag();
			}

			// 以下6行是为了初始化控件 以免复用的时候为止错乱
			holder.money.setText("");
			holder.money.setCompoundDrawablePadding(0);
			Drawable d1 = holder.money.getResources().getDrawable(R.drawable.acc_me_diamonds);
			d1.setBounds(0, 0, d1.getMinimumWidth(), d1.getMinimumHeight());
			holder.money.setCompoundDrawables(null, null, null, null);
			Drawable d2 = holder.money.getResources().getDrawable(R.drawable.acc_me_diamonds);
			d1.setBounds(0, 0, d2.getMinimumWidth(), d2.getMinimumHeight());
			holder.bonus.setCompoundDrawables(null, null, null, null);
			holder.bonus.setText("");
			holder.text.setText("");
			holder.orderState.setText("");
			holder.playName.setText("");
			holder.buyTime.setText("");

			// 以下是对数据的填充
			MessageBean messageBean = mList.get(position);
			holder.playName.setText(messageBean.getB1());
			holder.buyTime.setText(messageBean.getD());
			holder.orderState.setText(messageBean.getH0());

			// 如果彩种是模拟快三 就把积分前面加一个小钻石的图
			if ("IMI".equals(messageBean.getA().substring(0, 3))) {
				holder.text.setText("积分：");
				holder.money.setText(AccountUtils.fenToyuan(messageBean.getE()));
				holder.money.setCompoundDrawablePadding(5);
				AccountUtils.showArrowLeftDrawable(holder.money,R.drawable.acc_me_diamonds);

				// 如果有奖金，那么把奖金设置到对应的列表中
				if (Long.parseLong(messageBean.getG()) > 0&& !AppUtil.isEmpty(messageBean.getG())) {
					holder.bonus.setVisibility(View.VISIBLE);
					holder.bonus.setText(AccountUtils.fenToyuan(messageBean.getG()));
					holder.bonus.setCompoundDrawablePadding(5);
					AccountUtils.showArrowLeftDrawable(holder.bonus,R.drawable.acc_me_diamonds);
					holder.orderState.setText(messageBean.getH0() + "，");
				} else {
					holder.bonus.setVisibility(View.GONE);
				}

			} else {
				holder.text.setText("金额：");
				holder.money.setText("¥"
						+ FormatUtil.moneyFormat2String_int(Double
								.parseDouble(AccountUtils.fenToyuan(messageBean
										.getE() + ""))));
				// 如果有奖金，那么把奖金设置到对应的列表中
				if (Long.parseLong(messageBean.getG()) > 0
						&& !AppUtil.isEmpty(messageBean.getG())) {
					holder.bonus.setVisibility(View.VISIBLE);
					holder.bonus.setText("¥"
							+ AccountUtils.fenToyuan(messageBean.getG()));
					holder.orderState.setText(messageBean.getH0() + "，");
				} else {
					holder.bonus.setVisibility(View.GONE);
				}
			}

			onItemClick(convertView, messageBean);

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
	
	private void onItemClick(View convertView, final MessageBean messageBean) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AccountUtils.isFastClick(mActivity)){
					return;
				}
				String orderType = messageBean.getF();
				Bundle bundle = new Bundle();
				bundle.putString("orderId", messageBean.getA());//订单流水号 1212 中对于此信息会用到
				bundle.putString("lotteryId", messageBean.getB());
				bundle.putString("lotteryName", messageBean.getB1());
				bundle.putString("issue", messageBean.getC());//  期号  1212 中对于此信息会用到
				bundle.putString("orderTime", messageBean.getD());////System.out.println("messageBean.getE() = "+messageBean.getE());
				bundle.putString("orderAmount", messageBean.getE());
				bundle.putString("orderAward", messageBean.getG());
				bundle.putInt("order_state", Integer.parseInt(messageBean.getH()));
				bundle.putString("orderState", messageBean.getH0());
				if("1".equals(orderType)){//普通
					start(ThirdActivity.class,BettingDetail.class,bundle);
				}else if("2".equals(orderType)){//追号
				    start(ThirdActivity.class,ChaseDetail.class,bundle);
				}
			}
		});
	}

	private static class ViewRecordsHolder {
		TextView playName; // 玩法名称
		TextView buyTime; // 购彩时间
		TextView money; // 金额
		TextView orderState; // 订单状态
		TextView bonus;
		TextView text;
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

	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity, CustomTagEnum.betting_record.getId());
		setTitleNav(CustomTagEnum.betting_record.getId(),
				R.string.acc_title_betting_records, R.drawable.title_nav_back,
				R.drawable.title_nav_menu);
	}
}
