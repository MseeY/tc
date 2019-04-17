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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AnimationController;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SingleLayoutListView;
import com.mitenotc.utils.SingleLayoutListView.OnLoadMoreListener;
import com.mitenotc.utils.SingleLayoutListView.OnRefreshListener;

/**
 * 消息中心
 * 
 * @author ymx v 1.0
 */

public class MessageCenter extends BaseFragment implements OnClickListener {

	private TextView acc_msg_text1;// 网站公告
	private TextView acc_msg_text2;// 站内新闻
	private TextView acc_msg_text3;// 专家推荐
	private View acc_title_line; // 标题红线
	public boolean flag1;
	public boolean flag2;
	public boolean flag3;
	private ViewPager pager;
	private List<View> views;
	private PagerAdapter pagerAdapter;
	private SingleLayoutListView mListView1;
	private SingleLayoutListView mListView2;
	private SingleLayoutListView mListView3;
	private MyAdapter mAdapter1;
	private MyAdapter mAdapter2;
	private MyAdapter mAdapter3;
	private LinearLayout inflate1;
	private LinearLayout inflate2;
	private LinearLayout inflate3;
	private FrameLayout frame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_message_center1);
		initView();
//		initUnderLine();
		initViewPager();
		setMsgCenterListener();
	}

//	/** 初始化红线 */
//	private void initUnderLine() {
//		acc_title_line = findViewById(R.id.v_acc_title_line);
//		// 因为每个屏幕不一样，所以不能把红线宽度定死。
//		int width = AppUtil.getDisplayWidth(mActivity);
//		LayoutParams params = acc_title_line.getLayoutParams();
//		params.width = (width - 1) / 3; // 减去1是以为还有两条分割线，一个分割线是0.5dp
//		acc_title_line.setLayoutParams(params);
//	}

	/** 初始化ViewPager */
	private void initViewPager() {
		pager = (ViewPager) findViewById(R.id.ii_viewpager);
		views = new ArrayList<View>();
		initBodyView();
		pagerAdapter = new MyViewPagerAdapter();
		pager.setAdapter(pagerAdapter);
		
		Bundle myBundle = getMyBundle();
		if(myBundle!=null){
			String msgitem = myBundle.getString("MessageCenter");
			if("2".equals(msgitem)){
				flag1 = true;
				pager.setCurrentItem(2);
				initTextandimg();//初始化效果
				acc_msg_text3.setTextColor(Color.parseColor("#ef2d33"));
				acc_msg_img3.setVisibility(View.VISIBLE);
			}
		}
	}

	/** 初始化ViewPager内容体 */
	private void initBodyView() {

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
		mListView1.onRefreshComplete();
		mListView2.onRefreshComplete();
		mListView3.onRefreshComplete();
	}
	
	private void configListViewPushListener1() {
		mListView1.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1350, "1", 0);
			}
		});
	}

	private void configListViewPushListener2() {
		mListView2.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1350, "2", 2);
			}
		});
	}

	private void configListViewPushListener3() {
		mListView3.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1350, "3", 4);
			}
		});
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
			}
		};
	};
	
	private void configListViewIntoList(SingleLayoutListView listView) {
		listView.setCanLoadMore(false);
		listView.setCanRefresh(true);
		listView.setAutoLoadMore(false);
		listView.setMoveToFirstItemAfterRefresh(true);
		listView.setDoRefreshOnUIChanged(true);
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
			ViewMessageHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.m_acc_list_push_msg1, null);
				holder = new ViewMessageHolder();
				holder.msgTitle = (TextView) convertView.findViewById(R.id.tv_acc_msg_title);
				holder.msgContent = (TextView) convertView.findViewById(R.id.tv_acc_msg_content);
				holder.msgTime = (TextView) convertView.findViewById(R.id.tv_acc_msg_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewMessageHolder) convertView.getTag();
			}
			
			holder.msgTitle.setText("");
			holder.msgContent.setText("");
			holder.msgTime.setText("");

			MessageBean messageBean = mList.get(position);
			holder.msgTitle.setText(messageBean.getB());
			holder.msgContent.setText(messageBean.getD());
			holder.msgTime.setText(messageBean.getC());
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

	/** 获取网络数据 */
	private void sendRequest(int cmd, String type, int what) {
		MessageJson msg = new MessageJson();
		msg.put("A", type);
		msg.put("B", 50);
		submitData(what, cmd, msg);
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
				}
			} else {
				// 暂时不做任何操作
				inflate1.setVisibility(View.VISIBLE);
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
			break;
		case 2:
			if (messageBean.getLIST() != null) {
				inflate2.setVisibility(View.GONE);
				if (mAdapter2 != null) {
					mAdapter2.mList = messageBean.getLIST();
					mAdapter2.notifyDataSetChanged();
				}
			} else {
				inflate2.setVisibility(View.VISIBLE);
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
			break;
		
		case 4:
			if (messageBean.getLIST() != null) {
				inflate3.setVisibility(View.GONE);
				if (mAdapter3 != null) {
					mAdapter3.mList = messageBean.getLIST();
					mAdapter3.notifyDataSetChanged();
				}
			} else {
				inflate3.setVisibility(View.VISIBLE);
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
			break;
		}
	}
	
	private void onItemClick(View convertView, final MessageBean messageBean) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AccountUtils.isFastClick(mActivity)){
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("url", messageBean.getF());
				bundle.putString("title", "消息中心");
				start(ThirdActivity.class, TCWebFragment.class, bundle);
			}
		});
	}

	private static class ViewMessageHolder {
		TextView msgTitle; // 标题
		TextView msgContent; // 内容
		TextView msgTime; // 时间

	}
	
	private ImageView acc_msg_img1;
	private ImageView acc_msg_img2;
	private ImageView acc_msg_img3;
	private RelativeLayout msg_btn1;
	private RelativeLayout msg_btn2;
	private RelativeLayout msg_btn3;

	/** 初始化其他控件 */
	private void initView() {
		flag1 = false;
		flag2 = true;
		flag3 = true;
		acc_msg_text1 = (TextView) findViewById(R.id.tv_acc_msg_text1);
		acc_msg_text2 = (TextView) findViewById(R.id.tv_acc_msg_text2);
		acc_msg_text3 = (TextView) findViewById(R.id.tv_acc_msg_text3);
		acc_msg_img1 = (ImageView) findViewById(R.id.iv_acc_msg_img1);
		acc_msg_img2 = (ImageView) findViewById(R.id.iv_acc_msg_img2);
		acc_msg_img3 = (ImageView) findViewById(R.id.iv_acc_msg_img3);
		msg_btn1 = (RelativeLayout) findViewById(R.id.rl_acc_msg_btn1);
		msg_btn2 = (RelativeLayout) findViewById(R.id.rl_acc_msg_btn2);
		msg_btn3 = (RelativeLayout) findViewById(R.id.rl_acc_msg_btn3);
		msg_btn1.setOnClickListener(this);
		msg_btn2.setOnClickListener(this);
		msg_btn3.setOnClickListener(this);
	}

//	/** 记录当前正在显示的Pager */
//	private int currentPagerPosition = 0;

	/** 设置监听事件 */
	private void setMsgCenterListener() {

		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				initTextandimg();//初始化效果
				switch (position) {
				case 0:
					acc_msg_text1.setTextColor(Color.parseColor("#ef2d33"));
					acc_msg_img1.setVisibility(View.VISIBLE);
					if (flag1) {
						mListView1.pull2RefreshManually();
						configListViewPushListener1();
						flag1 = false;
					}
					break;
				case 1:
					acc_msg_text2.setTextColor(Color.parseColor("#ef2d33"));
					acc_msg_img2.setVisibility(View.VISIBLE);
					if (flag2) {
						mListView2.pull2RefreshManually();
						configListViewPushListener2();
						flag2 = false;
					}
					break;
				case 2:
					acc_msg_text3.setTextColor(Color.parseColor("#ef2d33"));
					acc_msg_img3.setVisibility(View.VISIBLE);
					if (flag3) {
						mListView3.pull2RefreshManually();
						configListViewPushListener3();
						flag3 = false;
					}
					break;
				}
//
//				TranslateAnimation animation = new TranslateAnimation(
//						currentPagerPosition* AppUtil.getDisplayWidth(mActivity) / 3/2,position * AppUtil.getDisplayWidth(mActivity) / 3/2, 0, 0);
//				animation.setDuration(300);
//				animation.setFillAfter(true);
//				acc_title_line.startAnimation(animation);
//				currentPagerPosition = position;

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
		});

	}
	
	private void initTextandimg(){
		acc_msg_text1.setTextColor(Color.BLACK);
		acc_msg_text2.setTextColor(Color.BLACK);
		acc_msg_text3.setTextColor(Color.BLACK);
		acc_msg_img1.setVisibility(View.INVISIBLE);
		acc_msg_img2.setVisibility(View.INVISIBLE);
		acc_msg_img3.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_acc_msg_btn1:
			pager.setCurrentItem(0);

			break;
		case R.id.rl_acc_msg_btn2:
			pager.setCurrentItem(1);

			break;
		case R.id.rl_acc_msg_btn3:
			pager.setCurrentItem(2);
			break;
		}
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
	public void onStart() {
		super.onStart();
		setTitleNav(CustomTagEnum.message_center.getId(),R.string.acc_title_message_center,R.drawable.title_nav_back,R.drawable.title_nav_menu);
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,CustomTagEnum.message_center.getId());
		setTitleNav(CustomTagEnum.message_center.getId(),R.string.acc_title_message_center,R.drawable.title_nav_back,R.drawable.title_nav_menu);
	}
}
